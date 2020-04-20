package Controllers;

import DTO.ApplicationStateDTO;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import initialization.GuiInitializer;
import initialization.ModelsLoader;
import main.LevelEditor;

import java.util.Set;

public class ModelsLoadController implements AbstractController {

	private ApplicationStateDTO applicationStateDTO;
	private Node guiNode;
	private ModelsLoader modelsLoader;
	private Node rootNode;
	private LevelEditor levelEditor;

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
			GuiInitializer guiInitializer = new GuiInitializer(levelEditor,
					guiNode);
			guiInitializer.initialize();
			applicationStateDTO.setLoadModelRequested(false);
			Set<String> pathsToFiles = modelsLoader.getPathsToFiles();
			for (Spatial child : rootNode.getChildren()) {
				pathsToFiles.remove(child.getKey()
										 .getName()
										 .replace("/", ""));
			}
		}
	}

	@Override
	public void setUp() {

	}
}
