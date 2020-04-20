package initialization;

import com.jme3.scene.Node;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.event.MouseAppState;
import com.simsilica.lemur.style.BaseStyles;
import main.LevelEditor;

public class GuiInitializer {

	private LevelEditor levelEditor;
	private Node guiNode;

	public GuiInitializer(LevelEditor levelEditor, Node guiNode) {
		this.levelEditor = levelEditor;
		this.guiNode = guiNode;
	}

	public void initialize() {
		init();
		addButton();
	}

	private void addButton() {

		Container myWindow = new Container();
		guiNode.attachChild(myWindow);
		myWindow.setLocalTranslation(300, 300, 0);

		Label label = new Label("Hello, World.");
		myWindow.addChild(label);
		Button clickMe = myWindow.addChild(new Button("Click Me"));
		clickMe.addClickCommands(source -> {
			removeMouse();
			guiNode.detachChild(myWindow);
		});
	}

	private void init() {
		GuiGlobals.initialize(levelEditor);
		BaseStyles.loadGlassStyle();
		GuiGlobals.getInstance()
				  .getStyles()
				  .setDefaultStyle("glass");
	}

	private void removeMouse() {
		MouseAppState state = levelEditor.getStateManager()
										 .getState(MouseAppState.class);
		levelEditor.getStateManager()
				   .detach(state);
	}

}
