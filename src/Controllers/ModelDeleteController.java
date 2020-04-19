package Controllers;

import DTO.SelectionStateDTO;
import com.jme3.scene.Geometry;

public class ModelDeleteController implements AbstractController {

	private SelectionStateDTO selectionStateDTO;

	public ModelDeleteController(SelectionStateDTO selectionStateDTO) {
		this.selectionStateDTO = selectionStateDTO;
	}

	@Override
	public void update() {
		if (selectionStateDTO.isDeleteRequested()){
			selectionStateDTO.setDeleteRequested(false);
			Geometry selectedModel = selectionStateDTO.getCurrentlySelectedModel();
			selectedModel.getParent().removeFromParent();
		}
	}

	@Override
	public void setUp() {

	}
}
