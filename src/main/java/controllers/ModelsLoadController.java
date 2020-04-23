package controllers;

import dto.ApplicationStateDTO;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import initialization.GuiController;
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

	public ModelsLoadController(ApplicationStateDTO applicationStateDTO,
			Node guiNode, ModelsLoader modelsLoader, Node rootNode,
			LevelEditor levelEditor) {
		this.applicationStateDTO = applicationStateDTO;
		this.guiNode = guiNode;
		this.modelsLoader = modelsLoader;
		this.rootNode = rootNode;
		this.levelEditor = levelEditor;
	}

	@Override
	public void update() {
		if (applicationStateDTO.isLoadModelRequested()) {
			applicationStateDTO.setLoadModelRequested(false);
			Set<String> pathsToFiles = modelsLoader.getModelsRelativePaths();
			System.out.println("before: "+pathsToFiles);
			for (Spatial child : rootNode.getChildren()) {
				pathsToFiles.remove(child.getKey()
										 .getName()
										 .replace("/", ""));
			}
			System.out.println("after: "+pathsToFiles);
			if (!pathsToFiles.isEmpty()){
				guiController.addListOfModels(pathsToFiles);
			}

		}
	}

	@Override
	public void setUp() {
		guiController = new GuiController(levelEditor, guiNode, modelsLoader);
		guiController.initialize();
	}
}