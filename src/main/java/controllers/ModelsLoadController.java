package controllers;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import dto.ApplicationStateDTO;
import initialization.GuiController;
import initialization.ModelToSceneAdder;
import initialization.ModelsLoader;
import start.LevelEditor;

import java.util.Set;

public class ModelsLoadController implements AbstractController {

	private ApplicationStateDTO applicationStateDTO;
	private Node guiNode;
	private ModelsLoader modelsLoader;
	private Node rootNode;
	private LevelEditor levelEditor;
	private GuiController guiController;
	private ModelToSceneAdder modelToSceneAdder;

	public ModelsLoadController(ApplicationStateDTO applicationStateDTO,
			Node guiNode, ModelsLoader modelsLoader, Node rootNode,
			LevelEditor levelEditor,
			ModelToSceneAdder modelToSceneAdder) {
		this.applicationStateDTO = applicationStateDTO;
		this.guiNode = guiNode;
		this.modelsLoader = modelsLoader;
		this.rootNode = rootNode;
		this.levelEditor = levelEditor;
		this.modelToSceneAdder = modelToSceneAdder;
	}

	@Override
	public void update() {
		if (applicationStateDTO.isLoadModelRequested()) {
			applicationStateDTO.setLoadModelRequested(false);
			Set<String> pathsToFiles = modelsLoader.getModelsRelativePaths();
			for (Spatial child : rootNode.getChildren()) {
				pathsToFiles.remove(child.getKey()
										 .getName()
										 .replace("/", ""));
			}
			if (!pathsToFiles.isEmpty()) {
				guiController.addListOfModels(pathsToFiles,
						modelToSceneAdder);
			}

		}
	}

	@Override
	public void setUp() {
		guiController = new GuiController(levelEditor, guiNode, modelsLoader);
		guiController.initialize();
	}
}
