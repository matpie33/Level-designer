package initialization;

import com.jme3.font.BitmapFont;
import com.jme3.system.AppSettings;
import controllers.*;
import dto.ApplicationStateDTO;
import saveAndLoad.FileSaveAndLoad;
import start.LevelEditor;

import java.util.ArrayList;
import java.util.List;

public class ControllersInitializer {

	private AppSettings settings;
	private LevelEditor levelEditor;
	private BitmapFont guiFont;
	private ModelsLoader modelsLoader;

	private List<AbstractController> controllers = new ArrayList<>();

	public ControllersInitializer(AppSettings settings, LevelEditor levelEditor,
			BitmapFont guiFont, ModelsLoader modelsLoader) {
		this.settings = settings;
		this.levelEditor = levelEditor;
		this.guiFont = guiFont;
		this.modelsLoader = modelsLoader;
	}

	public void initilize() {
		create();
		controllers.forEach(AbstractController::setUp);
	}

	private void create() {
		ApplicationStateDTO applicationStateDTO = new ApplicationStateDTO();
		SpatialsControlsInitializer spatialsControlsInitializer = new SpatialsControlsInitializer(
				levelEditor.getStateManager(), applicationStateDTO,
				levelEditor.getRootNode(), levelEditor.getCamera());
		spatialsControlsInitializer.attachGhostControl(levelEditor.getRootNode()
																  .getChildren());
		ModelSelectionController modelSelectionController = new ModelSelectionController(
				levelEditor.getCamera(), levelEditor.getRootNode(),
				applicationStateDTO);
		controllers.add(modelSelectionController);
		KeysSetup keysSetup = new KeysSetup(levelEditor.getInputManager(),
				modelSelectionController, applicationStateDTO);
		keysSetup.setUp();
		controllers.add(new ModelMovementController(applicationStateDTO,
				levelEditor.getCamera()));
		controllers.add(new ExitController(applicationStateDTO,
				levelEditor.getGuiNode(), guiFont, levelEditor, settings,
				new FileSaveAndLoad()));
		controllers.add(new SaveController(applicationStateDTO,
				levelEditor.getRootNode()));
		controllers.add(new ModelDuplicationController(applicationStateDTO,
				levelEditor.getRootNode(), modelSelectionController,
				spatialsControlsInitializer));
		controllers.add(new ModelDeleteController(applicationStateDTO));
		controllers.add(new ModelsLoadController(applicationStateDTO,
				levelEditor.getGuiNode(), modelsLoader,
				levelEditor.getRootNode(), levelEditor));
	}

	public List<AbstractController> getControllers() {
		return controllers;
	}
}
