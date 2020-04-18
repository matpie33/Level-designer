import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class SelectedObjectMovementController implements AbstractController {

	public static final float SLOW_CAMERA_SPEED = 0.01f;
	private SelectionStateDTO selectionStateDTO;
	private Camera camera;

	public SelectedObjectMovementController(SelectionStateDTO selectionStateDTO,
			Camera camera) {
		this.selectionStateDTO = selectionStateDTO;
		this.camera = camera;
	}

	@Override
	public void update() {
		Spatial model = selectionStateDTO.getCurrentlySelectedModel();
		if (model != null) {
			Node currentlySelectedModel = model.getParent();
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
											  .add(xyzDir.mult(
													  -SLOW_CAMERA_SPEED)));
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

	@Override
	public void setUp() {

	}

}
