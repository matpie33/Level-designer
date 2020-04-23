package controllers;

import dto.ApplicationStateDTO;
import com.jme3.scene.Node;
import saveAndLoad.FileSaveAndLoad;

public class SaveController implements AbstractController {

	private ApplicationStateDTO applicationStateDTO;
	private Node rootNode;
	private FileSaveAndLoad fileSaveAndLoad = new FileSaveAndLoad();

	public SaveController(ApplicationStateDTO applicationStateDTO, Node rootNode) {
		this.applicationStateDTO = applicationStateDTO;
		this.rootNode = rootNode;
	}

	@Override
	public void setUp() {

	}

	@Override
	public void update() {
		if (applicationStateDTO.isSaveRequested()) {
			applicationStateDTO.setSaveRequested(false);
			fileSaveAndLoad.save(rootNode.getChildren());
		}
	}

}
