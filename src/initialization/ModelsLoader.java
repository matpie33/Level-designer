package initialization;

import DTO.SpatialDTO;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import saveAndLoad.FileLoad;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class ModelsLoader {

	private static final String PATH_TO_MODELS = "C:/test/Games/Game/src/main/resources/models/";

	private AssetManager assetManager;
	private Vector3f currentObjectCoordinate = new Vector3f(0, -10, -20);
	private FileLoad fileLoad;
	private Set<String> pathsToFiles = new HashSet<>();

	public ModelsLoader(AssetManager assetManager) {
		this.assetManager = assetManager;
		assetManager.registerLocator(PATH_TO_MODELS, FileLocator.class);
		fileLoad = new FileLoad();
	}

	public List<Spatial> loadModels() {

		return loadFromFiles();
	}

	public List<Spatial> loadModelsFromFile(String filePath) {
		findAllModelsPaths();
		List<Spatial> spatials = loadFromData(fileLoad.readFile(filePath));
		return spatials;
	}

	private void findAllModelsPaths() {
		File dir = new File(PATH_TO_MODELS);
		File[] files = dir.listFiles(
				(dir1, name) -> endsWith(name, ".mesh" + ".xml"));
		for (File file : files) {
			pathsToFiles.add(file.getName());
		}
	}

	private List<Spatial> loadFromData(List<SpatialDTO> spatialDTOS) {
		return spatialDTOS.stream()
						  .map(data -> {
							  String pathToModel = data.getPathToModel();
							  Spatial spatial = loadModel(pathToModel);
							  spatial.setLocalRotation(data.getRotation());
							  spatial.setLocalTranslation(data.getPosition());
							  return spatial;
						  })
						  .collect(Collectors.toList());
	}

	public Set<String> getPathsToFiles() {
		return pathsToFiles;
	}

	private List<Spatial> loadFromFiles() {

		File dir = new File(PATH_TO_MODELS);
		File[] files = dir.listFiles(
				(dir1, name) -> endsWith(name, ".mesh" + ".xml"));
		return Arrays.stream(files)
					 .map(file -> {
						 Spatial spatial = loadModel("/" + file.getName());
						 spatial.setLocalTranslation(currentObjectCoordinate);
						 currentObjectCoordinate.setX(
								 currentObjectCoordinate.getX() + 10);
						 return spatial;
					 }).collect(Collectors.toList());
	}

	private boolean endsWith(String fileName, String searchedSuffix) {
		return fileName.toLowerCase()
					   .endsWith(searchedSuffix);
	}

	private Spatial loadModel(String fileName) {
		return assetManager.loadModel(fileName);
	}

}
