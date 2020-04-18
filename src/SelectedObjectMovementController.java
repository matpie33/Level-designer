import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;

public class SelectedObjectMovementController {

	public static final float SLOW_CAMERA_SPEED = 0.01f;
	private SelectionStateDTO selectionStateDTO;
	private Camera camera;

	public SelectedObjectMovementController(SelectionStateDTO selectionStateDTO,
			Camera camera) {
		this.selectionStateDTO = selectionStateDTO;
		this.camera = camera;
	}

	public void update() {
		Geometry currentlySelectedModel = selectionStateDTO.getCurrentlySelectedModel();
		if (currentlySelectedModel != null) {
			if (selectionStateDTO.isMovingForward()) {
				Vector3f dir = camera.getDirection();
				Vector3f xyzDir = new Vector3f(Math.round(dir.getX()),
						Math.round(dir.getY()), Math.round(dir.getZ()));
				currentlySelectedModel.setLocalTranslation(
						currentlySelectedModel.getLocalTranslation()
											  .add(xyzDir.mult(
													  SLOW_CAMERA_SPEED)));
			}
			if (selectionStateDTO.isMovingBackward()) {
				Vector3f dir = camera.getDirection();
				Vector3f xyzDir = new Vector3f(Math.round(dir.getX()),
						Math.round(dir.getY()), Math.round(dir.getZ()));
				currentlySelectedModel.setLocalTranslation(
						currentlySelectedModel.getLocalTranslation()
											  .add(xyzDir.mult(-SLOW_CAMERA_SPEED)));
			}
			if (selectionStateDTO.isMovingRight()) {
				currentlySelectedModel.setLocalTranslation(
						currentlySelectedModel.getLocalTranslation()
											  .add(camera.getLeft()
														 .mult(-SLOW_CAMERA_SPEED)));
			}
			if (selectionStateDTO.isMovingLeft()) {
				currentlySelectedModel.setLocalTranslation(
						currentlySelectedModel.getLocalTranslation()
											  .add(camera.getLeft()
														 .mult(SLOW_CAMERA_SPEED)));
			}
			if (selectionStateDTO.isMovingUp()) {
				currentlySelectedModel.setLocalTranslation(
						currentlySelectedModel.getLocalTranslation()
											  .add(camera.getUp()
														 .mult(SLOW_CAMERA_SPEED)));
			}
			if (selectionStateDTO.isMovingDown()) {
				currentlySelectedModel.setLocalTranslation(
						currentlySelectedModel.getLocalTranslation()
											  .add(camera.getUp()
														 .mult(-SLOW_CAMERA_SPEED)));
			}
		}
	}

}
