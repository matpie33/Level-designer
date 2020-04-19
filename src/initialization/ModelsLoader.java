package initialization;

import DTO.SpatialDTO;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import saveAndLoad.FileLoad;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModelsLoader {

	private static final String PATH_TO_MODELS = "C:/test/Games/Game/src/main/resources/models/";

	private AssetManager assetManager;
	private Vector3f currentObjectCoordinate = new Vector3f(0, -10, -20);
	private FileLoad fileLoad;

	public ModelsLoader(AssetManager assetManager) {
		this.assetManager = assetManager;
		assetManager.registerLocator(PATH_TO_MODELS, FileLocator.class);
		fileLoad = new FileLoad();
	}

	public List<Spatial> loadModels() {

		return loadFromFiles();
	}

	public List<Spatial> loadModelsFromFile(String filePath) {
		return loadFromData(fileLoad.readFile(filePath));
	}

	private List<Spatial> loadFromData(List<SpatialDTO> spatialDTOS) {
		return spatialDTOS.stream()
						  .map(data -> {
							  Spatial spatial = loadModel(
									  data.getPathToModel());
							  spatial.setLocalRotation(data.getRotation());
							  spatial.setLocalTranslation(data.getPosition());
							  return spatial;
						  })
						  .collect(Collectors.toList());
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
