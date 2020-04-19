package initialization;

import DTO.SpatialDTO;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelsLoader {

	private static final String PATH_TO_MODELS = "C:/test/Games/Game/src/main/resources/models/";
	private static final String PATH_TO_SCENE = "C:/test/Games/Game/src/main/resources/scene/";

	private AssetManager assetManager;
	private List<SpatialDTO> spatialData = new ArrayList<>();
	private Vector3f currentObjectCoordinate = new Vector3f(0, -10, -20);
	private Node rootNode;

	public ModelsLoader(AssetManager assetManager, Node rootNode) {
		this.assetManager = assetManager;
		this.rootNode = rootNode;
	}

	public void loadModels() {
		assetManager.registerLocator(PATH_TO_MODELS, FileLocator.class);
		assetManager.registerLocator(PATH_TO_SCENE, FileLocator.class);
		if (spatialData.isEmpty()) {
			loadFromFiles();
		}
		else {
			loadFromData();
		}
	}

	private void loadFromData() {
		spatialData.stream()
				   .map(data -> {
					   Spatial spatial = loadModel(data.getPathToModel());
					   spatial.setLocalRotation(data.getRotation());
					   spatial.setLocalTranslation(data.getPosition());
					   return spatial;
				   })
				   .forEach(rootNode::attachChild);
	}

	private void loadFromFiles() {

		File dir = new File(PATH_TO_MODELS);
		File[] files = dir.listFiles(
				(dir1, name) -> endsWith(name, ".mesh" + ".xml") || endsWith(
						name, ".scene"));
		Arrays.stream(files)
			  .map(file -> {
				  Spatial spatial = loadModel("/" + file.getName());
				  spatial.setLocalTranslation(currentObjectCoordinate);
				  currentObjectCoordinate.setX(
						  currentObjectCoordinate.getX() + 10);
				  return spatial;
			  })
			  .forEach(rootNode::attachChild);
	}

	private boolean endsWith(String fileName, String searchedSuffix) {
		return fileName.toLowerCase()
					   .endsWith(searchedSuffix);
	}

	private Spatial loadModel(String fileName) {
		return assetManager.loadModel(fileName);
	}

	public void addSpatialData(List<SpatialDTO> spatialDTOS) {
		spatialData = spatialDTOS;
	}

}
