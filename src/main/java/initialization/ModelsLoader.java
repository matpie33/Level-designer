package initialization;

import DTO.SpatialDTO;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import saveAndLoad.FileLoad;

import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class ModelsLoader {

	private static final String PATH_TO_MODELS = "C:/test";
	private Node rootNode;
	private List<String> paths = new ArrayList<>();
	private AssetManager assetManager;
	private Vector3f currentObjectCoordinate = new Vector3f(0, -10, -20);
	private FileLoad fileLoad;
	private Set<String> pathsToFiles = new HashSet<>();
	private Camera camera;

	public ModelsLoader(AssetManager assetManager, Camera camera,
			Node rootNode) {
		this.assetManager = assetManager;
		fileLoad = new FileLoad();
		this.camera = camera;
		this.rootNode = rootNode;
		assetManager.registerLocator(PATH_TO_MODELS, FileLocator.class);
		paths.add(PATH_TO_MODELS);
	}

	public List<Spatial> loadModels() {

		return loadFromFiles();
	}

	public List<Spatial> loadModelsFromFile(InputStream filePath) {
		paths.forEach(this::findAllModelsPaths);
		return loadFromData(fileLoad.readFile(filePath));
	}

	private void findAllModelsPaths(String path) {
		File dir = new File(path);
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
		return new HashSet<>(pathsToFiles);
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
					 })
					 .collect(Collectors.toList());
	}

	private boolean endsWith(String fileName, String searchedSuffix) {
		return fileName.toLowerCase()
					   .endsWith(searchedSuffix);
	}

	private Spatial loadModel(String fileName) {
		return assetManager.loadModel(fileName);
	}

	public void addModel(String filename) {
		System.out.println("loading model: "+filename);
		Spatial spatial = loadModel(filename);
		spatial.setLocalTranslation(camera.getLocation()
										  .add(camera.getDirection()
													 .mult(15)));
		rootNode.attachChild(spatial);
	}

	public void setPaths(List<String> lines) {
		paths.addAll(lines);
		lines.forEach(
				path -> assetManager.registerLocator(path, FileLocator.class));
	}
}
