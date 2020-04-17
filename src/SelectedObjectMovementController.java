import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;

public class SelectedObjectMovementController {

	private SelectionStateDTO selectionStateDTO;

	public SelectedObjectMovementController(SelectionStateDTO selectionStateDTO) {
		this.selectionStateDTO = selectionStateDTO;
	}

	public void update() {
		Geometry currentlySelectedModel = selectionStateDTO.getCurrentlySelectedModel();
		if (currentlySelectedModel != null) {
			if (selectionStateDTO.isMovingForward()) {
				moveInDirection(currentlySelectedModel, Vector3f.UNIT_Z, -1);
			}
			if (selectionStateDTO.isMovingBackward()) {
				moveInDirection(currentlySelectedModel, Vector3f.UNIT_Z, 1);
			}
			if (selectionStateDTO.isMovingRight()) {
				moveInDirection(currentlySelectedModel, Vector3f.UNIT_X, 1);
			}
			if (selectionStateDTO.isMovingLeft()) {
				moveInDirection(currentlySelectedModel, Vector3f.UNIT_X, -1);
			}
			if (selectionStateDTO.isMovingUp()) {
				moveInDirection(currentlySelectedModel, Vector3f.UNIT_Y, 1);
			}
			if (selectionStateDTO.isMovingDown()) {
				moveInDirection(currentlySelectedModel, Vector3f.UNIT_Y, -1);
			}
		}
	}

	private void moveInDirection(Geometry currentlySelectedModel,
			Vector3f unitZ, int multiplier) {
		currentlySelectedModel.setLocalTranslation(
				currentlySelectedModel.getLocalTranslation()
									  .add(unitZ.mult(multiplier * 0.01f)));
	}

}
