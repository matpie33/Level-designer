package Controllers;

import DTO.SelectionStateDTO;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import main.LevelEditor;
import saveAndLoad.FileSaveAndLoad;

public class ExitController implements AbstractController {

	public static final String CONFIRM_TEXT = "Are you sure? Press 'y' or 'n'.";
	private SelectionStateDTO selectionStateDTO;
	private Node guiNode;
	private BitmapFont guiFont;
	private BitmapText exitConfirmText;
	private LevelEditor levelEditor;
	private AppSettings settings;
	private FileSaveAndLoad fileSaveAndLoad;

	public ExitController(SelectionStateDTO selectionStateDTO, Node guiNode,
			BitmapFont guiFont, LevelEditor levelEditor, AppSettings settings,
			FileSaveAndLoad fileSaveAndLoad) {
		this.selectionStateDTO = selectionStateDTO;
		this.guiNode = guiNode;
		this.guiFont = guiFont;
		this.levelEditor = levelEditor;
		this.settings = settings;
		this.fileSaveAndLoad = fileSaveAndLoad;
	}

	@Override
	public void setUp() {
		createExitConfirmationText();
	}

	@Override
	public void update() {
		if (selectionStateDTO.isExitRequested() && !guiNode.hasChild(
				exitConfirmText)) {
			guiNode.attachChild(exitConfirmText);
		}
		if (Boolean.TRUE.equals(selectionStateDTO.isExitConfirmed())) {
			fileSaveAndLoad.save(levelEditor.getRootNode()
											.getChildren());
			levelEditor.stop();
		}
		if (Boolean.FALSE.equals(selectionStateDTO.isExitConfirmed())) {
			guiNode.detachChild(exitConfirmText);
			selectionStateDTO.setExitConfirmed(null);
		}

	}

	private void createExitConfirmationText() {
		exitConfirmText = new BitmapText(guiFont, false);
		exitConfirmText.setSize(guiFont.getCharSet()
									   .getRenderedSize());
		exitConfirmText.setColor(ColorRGBA.Blue);
		exitConfirmText.setText(CONFIRM_TEXT);
		exitConfirmText.setLocalTranslation(
				settings.getWidth() / 2 - exitConfirmText.getLineWidth() / 2,
				settings.getHeight() / 2 - exitConfirmText.getLineHeight() / 2,
				0);
	}

}
