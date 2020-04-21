package Controllers;

import DTO.ApplicationStateDTO;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import saveAndLoad.FileSaveAndLoad;
import start.LevelEditor;

public class ExitController implements AbstractController {

	public static final String CONFIRM_TEXT = "Are you sure? Press 'y' or 'n'.";
	private ApplicationStateDTO applicationStateDTO;
	private Node guiNode;
	private BitmapFont guiFont;
	private BitmapText exitConfirmText;
	private LevelEditor levelEditor;
	private AppSettings settings;
	private FileSaveAndLoad fileSaveAndLoad;

	public ExitController(ApplicationStateDTO applicationStateDTO, Node guiNode,
			BitmapFont guiFont, LevelEditor levelEditor, AppSettings settings,
			FileSaveAndLoad fileSaveAndLoad) {
		this.applicationStateDTO = applicationStateDTO;
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
		if (applicationStateDTO.isExitRequested() && !guiNode.hasChild(
				exitConfirmText)) {
			guiNode.attachChild(exitConfirmText);
		}
		if (applicationStateDTO.isExitRequested() && Boolean.TRUE.equals(
				applicationStateDTO.isExitConfirmed())) {
			fileSaveAndLoad.save(levelEditor.getRootNode()
											.getChildren());
			levelEditor.stop();
		}
		if (applicationStateDTO.isExitRequested() && Boolean.FALSE.equals(
				applicationStateDTO.isExitConfirmed())) {
			guiNode.detachChild(exitConfirmText);
			applicationStateDTO.setExitConfirmed(null);
			applicationStateDTO.setExitRequested(false);
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
