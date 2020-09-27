package initialization;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import dto.SpatialDTO;
import saveAndLoad.FileLoad;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class ModelsLoadAppState extends AbstractAppState {

	private Node rootNode;
	private List<String> paths = new ArrayList<>();
	private AssetManager assetManager;
	private Vector3f currentObjectCoordinate = new Vector3f(0, -10, -20);
	private FileLoad fileLoad;
	private Set<String> modelsRelativePaths = new HashSet<>();
	private Camera camera;

	public ModelsLoadAppState(AssetManager assetManager, Camera camera,
			Node rootNode) {
		this.assetManager = assetManager;
		fileLoad = new FileLoad();
		this.camera = camera;
		this.rootNode = rootNode;
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
	}

	public List<SpatialDTO> loadModels() {

		List<SpatialDTO> spatials = new ArrayList<>();
		for (String path : paths) {
			spatials.addAll(loadModelsFromFile(path));
		}
		return spatials;
	}

	private void findAllModelsInPath(String path) {
		File dir = new File(path);
		File[] files = dir.listFiles(
				(dir1, name) -> isModelOrSceneExtension(name));
		for (File file : files) {
			modelsRelativePaths.add(file.getName());
		}
	}

	public Set<String> getModelsRelativePaths() {
		return new HashSet<>(modelsRelativePaths);
	}

	private List<SpatialDTO> loadModelsFromFile(String path) {

		File dir = new File(path);
		File[] files = dir.listFiles(
				(dir1, name) -> isModelOrSceneExtension(name));
		return Arrays.stream(files)
					 .map(file -> {
						 SpatialDTO spatial = new SpatialDTO();
						 spatial.setPathToModel("/" + file.getName());
						 Vector3f clone = currentObjectCoordinate.clone();
						 spatial.setPosition(clone);
						 currentObjectCoordinate.setX(
								 currentObjectCoordinate.getX() + 10);
						 return spatial;
					 })
					 .collect(Collectors.toList());
	}

	private boolean isModelOrSceneExtension(String name) {
		return endsWith(name, ".mesh.xml") || endsWith(name, ".scene");
	}

	private boolean endsWith(String fileName, String searchedSuffix) {
		return fileName.toLowerCase()
					   .endsWith(searchedSuffix);
	}

	public Spatial loadModel(String fileName) {
		return assetManager.loadModel(fileName);
	}

	public Spatial addModel(String filename) {
		Spatial spatial = loadModel(filename);
		spatial.setLocalTranslation(camera.getLocation()
										  .add(camera.getDirection()
													 .mult(15)));
		rootNode.attachChild(spatial);
		return spatial;
	}

	public void setPaths(List<String> lines) {
		paths.addAll(lines);
		lines.forEach(
				path -> assetManager.registerLocator(path, FileLocator.class));
	}

	public void findAllModelsInPaths() {
		paths.forEach(this::findAllModelsInPath);
	}
}
