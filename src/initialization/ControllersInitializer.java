package initialization;

import Controllers.*;
import DTO.SelectionStateDTO;
import com.jme3.font.BitmapFont;
import com.jme3.system.AppSettings;
import main.LevelEditor;

import java.util.ArrayList;
import java.util.List;

public class ControllersInitializer {

	private AppSettings settings;
	private LevelEditor levelEditor;
	private BitmapFont guiFont;

	private List<AbstractController> controllers = new ArrayList<>();

	public ControllersInitializer(AppSettings settings, LevelEditor levelEditor,
			BitmapFont guiFont) {
		this.settings = settings;
		this.levelEditor = levelEditor;
		this.guiFont = guiFont;
	}

	public void initilize() {
		create();
		controllers.forEach(AbstractController::setUp);
	}

	private void create() {
		SelectionStateDTO selectionStateDTO = new SelectionStateDTO();
		ModelSelectionController modelSelectionController = new ModelSelectionController(
				levelEditor.getCamera(), levelEditor.getRootNode(),
				selectionStateDTO);
		controllers.add(modelSelectionController);
		KeysSetup keysSetup = new KeysSetup(levelEditor.getInputManager(),
				modelSelectionController, selectionStateDTO);
		keysSetup.setUp();
		controllers.add(new SelectedObjectMovementController(selectionStateDTO,
				levelEditor.getCamera()));
		controllers.add(new ExitController(selectionStateDTO,
				levelEditor.getGuiNode(), guiFont, levelEditor, settings));
		controllers.add(new SaveController(selectionStateDTO,
				levelEditor.getRootNode()));
	}

	public List<AbstractController> getControllers() {
		return controllers;
	}
}
