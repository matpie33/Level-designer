package main;

import Controllers.AbstractController;
import DTO.SpatialDTO;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import initialization.ControllersInitializer;
import initialization.ModelToSceneAdder;
import initialization.ModelsLoader;
import saveAndLoad.FileLoad;

import java.awt.*;
import java.util.List;

public class LevelEditor extends SimpleApplication {

	private ControllersInitializer controllersInitializer;
	private FileLoad fileLoad = new FileLoad();
	private final static boolean readFromFile = true;

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
		controllersInitializer = new ControllersInitializer(settings, this,
				guiFont);
		controllersInitializer.initilize();
		flyCam.setMoveSpeed(50f);
		addLight();
		ModelToSceneAdder modelToSceneAdder = new ModelToSceneAdder(rootNode);
		ModelsLoader modelsLoader = new ModelsLoader(assetManager);
		if (readFromFile){
			List<SpatialDTO> spatialDTOS = fileLoad.readFile("level.txt");
			modelToSceneAdder.addSpatialData(spatialDTOS);
		}
		List<Spatial> spatials = modelsLoader.loadModels();
		modelToSceneAdder.addSpatials(spatials);

		initCrossHairs();
		inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);


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

	protected void initCrossHairs() {
		guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
		BitmapText ch = new BitmapText(guiFont, false);
		ch.setSize(guiFont.getCharSet()
						  .getRenderedSize() * 2);
		ch.setText("+");
		ch.setLocalTranslation(
				settings.getWidth() / 2 - ch.getLineWidth() / 2,
				settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
		guiNode.attachChild(ch);
	}

}
