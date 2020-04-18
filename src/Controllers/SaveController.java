package Controllers;

import DTO.SelectionStateDTO;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import saveAndLoad.FileSaveAndLoad;

public class SaveController implements AbstractController {

	private SelectionStateDTO selectionStateDTO;
	private Node rootNode;
	private FileSaveAndLoad fileSaveAndLoad = new FileSaveAndLoad();

	public SaveController(SelectionStateDTO selectionStateDTO, Node rootNode) {
		this.selectionStateDTO = selectionStateDTO;
		this.rootNode = rootNode;
	}

	@Override
	public void setUp() {

	}

	@Override
	public void update() {
		if (selectionStateDTO.isSaveRequested()) {
			selectionStateDTO.setSaveRequested(false);
			fileSaveAndLoad.save(rootNode.getChildren());
		}
	}

}
