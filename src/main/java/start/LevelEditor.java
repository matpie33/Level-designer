package start;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.system.AppSettings;
import controllers.AbstractController;
import dto.ApplicationStateDTO;
import dto.OptionsDTO;
import dto.SpatialDTO;
import initialization.*;
import saveAndLoad.FileLoad;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class LevelEditor extends SimpleApplication {

	private ControllersInitializer controllersInitializer;
	private final static boolean readFromFile = false;
	private ModelsLoader modelsLoader;
	private FileLoad fileLoad;
	private ModelToSceneAdder modelToSceneAdder;
	private ApplicationStateDTO applicationStateDTO;
	private PhysicsSpaceInitializer physicsSpaceInitializer;

	public static void main(String[] args) {
		loadGame();

	}

	private static void loadGame() {
		LevelEditor gameApplication = new LevelEditor();
		AppSettings settings = new AppSettings(true);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		//		settings.setResolution(screenSize.width, screenSize.height);
		gameApplication.setShowSettings(false);
		gameApplication.setSettings(settings);
		gameApplication.start();
	}

	@Override
	public void simpleInitApp() {
		applicationStateDTO = new ApplicationStateDTO();
		flyCam.setMoveSpeed(50f);
		initializeOptions();
		addLight();
		loadModels();
		controllersInitializer = new ControllersInitializer(settings, this,
				guiFont, modelsLoader, applicationStateDTO, modelToSceneAdder);
		controllersInitializer.initilize();
		initCrosshair();
		inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);

	}

	private void initializeOptions() {
		OptionsDTO optionsDTO = OptionsDTO.getInstance();
		optionsDTO.setModelMovementSpeed(0.3f);
	}

	private void loadModels() {
		PathToModelsReader pathToModelsReader = new PathToModelsReader();
		List<String> paths = pathToModelsReader.readPaths();
		modelsLoader = new ModelsLoader(assetManager, cam, rootNode);
		modelsLoader.setPaths(paths);
		InputStream inputStream = readFile();
		modelsLoader.findAllModelsInPaths();
		initializePhysicsSpace();
		fileLoad = new FileLoad();
		List<SpatialDTO> spatials = readFromFile ?
				fileLoad.readFile(inputStream) :
				modelsLoader.loadModels();
		modelToSceneAdder = new ModelToSceneAdder(modelsLoader, stateManager,
				rootNode, cam, applicationStateDTO);
		modelToSceneAdder.addModels(spatials);

	}

	private void initializePhysicsSpace() {
		physicsSpaceInitializer = new PhysicsSpaceInitializer(stateManager,
				applicationStateDTO);
		physicsSpaceInitializer.initialize();
	}

	private FileInputStream readFile() {
		try {
			return new FileInputStream("./level.txt");
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void simpleUpdate(float tpf) {

		controllersInitializer.getControllers()
							  .forEach(AbstractController::update);
		super.simpleUpdate(tpf);

	}

	private void addLight() {
		AmbientLight sun = new AmbientLight();
		sun.setColor(ColorRGBA.White);
		rootNode.addLight(sun);
	}

	protected void initCrosshair() {
		guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
		BitmapText ch = new BitmapText(guiFont, false);
		ch.setSize(guiFont.getCharSet()
						  .getRenderedSize() * 2);
		ch.setText("+");
		ch.setLocalTranslation(settings.getWidth() / 2 - ch.getLineWidth() / 2,
				settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
		guiNode.attachChild(ch);
	}

}
