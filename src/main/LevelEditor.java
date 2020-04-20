package main;

import Controllers.AbstractController;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import initialization.ControllersInitializer;
import initialization.GuiController;
import initialization.ModelsLoader;

import java.awt.*;
import java.util.List;

public class LevelEditor extends SimpleApplication {

	private ControllersInitializer controllersInitializer;
	private final static boolean readFromFile = true;
	private ModelsLoader modelsLoader;
	private GuiController guiController;

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
		flyCam.setMoveSpeed(50f);
		addLight();
		loadModels();
		controllersInitializer = new ControllersInitializer(settings, this,
				guiFont, modelsLoader);
		controllersInitializer.initilize();
		initCrosshair();
		inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);

	}

	private void loadModels() {
		modelsLoader = new ModelsLoader(assetManager, cam, rootNode);
		List<Spatial> spatials = readFromFile ?
				modelsLoader.loadModelsFromFile("level.txt") :
				modelsLoader.loadModels();
		spatials.forEach(rootNode::attachChild);

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
