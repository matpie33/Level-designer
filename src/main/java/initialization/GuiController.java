package initialization;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.ListBox;
import com.simsilica.lemur.core.VersionedList;
import com.simsilica.lemur.style.BaseStyles;
import start.LevelEditor;

import java.util.Set;

public class GuiController {

	private LevelEditor levelEditor;
	private Node guiNode;
	private GuiGlobals guiGlobals;
	private ModelsLoader modelsLoader;

	public GuiController(LevelEditor levelEditor, Node guiNode,
			ModelsLoader modelsLoader) {
		this.levelEditor = levelEditor;
		this.guiNode = guiNode;
		this.modelsLoader = modelsLoader;
	}

	public void initialize() {
		init();
	}

	public void addListOfModels(Set<String> pathsToFiles,
			ModelToSceneAdder modelToSceneAdder) {

		guiGlobals.setCursorEventsEnabled(true);
		Container container = new Container();
		guiNode.attachChild(container);
		container.setLocalTranslation(300, 300, 0);

		ListBox<String> listBox = new ListBox<>();
		VersionedList<String> model = listBox.getModel();
		model.addAll(pathsToFiles);
		container.addChild(listBox);
		listBox.addClickCommands(source -> {
			Integer selection = source.getSelectionModel()
									  .getSelection();
			Spatial spatial = modelsLoader.addModel((String) source.getModel()
																   .get(selection));
			modelToSceneAdder.addControls(spatial);
			guiNode.detachChild(container);
			guiGlobals.setCursorEventsEnabled(false);
		});
	}

	private void init() {
		GuiGlobals.initialize(levelEditor);
		guiGlobals = GuiGlobals.getInstance();
		BaseStyles.loadGlassStyle();
		guiGlobals.getStyles()
				  .setDefaultStyle("glass");
		guiGlobals.setCursorEventsEnabled(false);
	}

}
