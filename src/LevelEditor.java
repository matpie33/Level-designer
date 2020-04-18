import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;

import java.awt.*;
import java.util.List;

public class LevelEditor extends SimpleApplication {

	private ModelSelectionController modelSelectionController;
	private SelectionStateDTO selectionStateDTO;
	private KeysSetup keysSetup;
	private SelectedObjectMovementController selectedObjectMovementController;
	private ExitController exitController;
	private SaveController saveController;

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
		ModelsLoader modelsLoader = new ModelsLoader(assetManager);
		List<Spatial> spatials = modelsLoader.loadModels();
		addLight();
		ModelToSceneAdder modelToSceneAdder = new ModelToSceneAdder(rootNode);
		modelToSceneAdder.addSpatials(spatials);
		initCrossHairs();
		selectionStateDTO = new SelectionStateDTO();
		modelSelectionController = new ModelSelectionController(cam, rootNode,
				selectionStateDTO);
		keysSetup = new KeysSetup(inputManager, modelSelectionController,
				selectionStateDTO);
		keysSetup.setUp();
		selectedObjectMovementController = new SelectedObjectMovementController(
				selectionStateDTO, cam);
		inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);
		exitController = new ExitController(selectionStateDTO, guiNode,
				guiFont, this, settings);
		exitController.setUp();
		saveController = new SaveController(selectionStateDTO, rootNode);

	}

	@Override
	public void simpleUpdate(float tpf) {

		exitController.update();
		modelSelectionController.update();
		selectedObjectMovementController.update();
		saveController.update();
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
		ch.setText("+"); // crosshairs
		ch.setLocalTranslation( // center
				settings.getWidth() / 2 - ch.getLineWidth() / 2,
				settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
		guiNode.attachChild(ch);
	}

}
