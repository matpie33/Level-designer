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
	private ModelsLoadAppState modelsLoadAppState;
	private ApplicationStateDTO applicationStateDTO;
	private List<AbstractController> controllers = new ArrayList<>();
	private ModelToSceneAdder modelToSceneAdder;

	public ControllersInitializer(AppSettings settings, LevelEditor levelEditor,
			BitmapFont guiFont, ModelsLoadAppState modelsLoadAppState,
			ApplicationStateDTO applicationStateDTO,
			ModelToSceneAdder modelToSceneAdder) {
		this.settings = settings;
		this.levelEditor = levelEditor;
		this.guiFont = guiFont;
		this.modelsLoadAppState = modelsLoadAppState;
		this.applicationStateDTO = applicationStateDTO;
		this.modelToSceneAdder = modelToSceneAdder;
	}

	public void initilize() {
		create();
		controllers.forEach(AbstractController::setUp);
	}

	private void create() {

		ModelSelectionController modelSelectionController = new ModelSelectionController(
				levelEditor.getCamera(), levelEditor.getRootNode(),
				applicationStateDTO);
		controllers.add(modelSelectionController);
		KeysSetup keysSetup = new KeysSetup(levelEditor.getInputManager(),
				modelSelectionController, applicationStateDTO);
		keysSetup.setUp();
		controllers.add(new ExitController(applicationStateDTO,
				levelEditor.getGuiNode(), guiFont, levelEditor, settings,
				new FileSaveAndLoad()));
		controllers.add(new SaveController(applicationStateDTO,
				levelEditor.getRootNode()));
		controllers.add(new ModelDuplicationController(applicationStateDTO,
				levelEditor.getRootNode(), modelSelectionController,
				modelToSceneAdder, modelsLoadAppState, levelEditor.getCamera()));
		controllers.add(new ModelDeleteController(applicationStateDTO));
		controllers.add(new ModelRotationController(applicationStateDTO));
		controllers.add(
				new AccelerationDecelerationController(applicationStateDTO,
						levelEditor.getFlyByCamera()));
		controllers.add(new ModelsLoadController(applicationStateDTO,
				levelEditor.getGuiNode(), modelsLoadAppState,
				levelEditor.getRootNode(), levelEditor, modelToSceneAdder));
	}

	public List<AbstractController> getControllers() {
		return controllers;
	}
}
