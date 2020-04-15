import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.scene.Spatial;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelsLoader {

	private static final String PATH_TO_MODELS = "C:/test/Games/Game/src/main/resources/models/";
	private static final String PATH_TO_SCENE = "C:/test/Games/Game/src/main/resources/scene/";

	private AssetManager assetManager;
	private List<Spatial> models = new ArrayList<>();

	public ModelsLoader(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	public List<Spatial> loadModels() {
		assetManager.registerLocator(PATH_TO_MODELS, FileLocator.class);
		assetManager.registerLocator(PATH_TO_SCENE, FileLocator.class);
		File dir = new File(PATH_TO_MODELS);
		File[] files = dir.listFiles(
				(dir1, name) -> endsWith(name, ".mesh" + ".xml") || endsWith(
						name, ".scene"));
		Arrays.stream(files)
			  .map(file -> loadModel(file.getName()))
			  .forEach(models::add);
		return models;
	}

	private boolean endsWith(String fileName, String searchedSuffix) {
		return fileName.toLowerCase()
					   .endsWith(searchedSuffix);
	}

	private Spatial loadModel(String fileName) {
		return assetManager.loadModel("/" + fileName);
	}

}
