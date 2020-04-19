package Controllers;

import DTO.ApplicationStateDTO;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class SelectedObjectMovementController implements AbstractController {

	public static final float SLOW_CAMERA_SPEED = 0.01f;
	private ApplicationStateDTO applicationStateDTO;
	private Camera camera;

	public SelectedObjectMovementController(
			ApplicationStateDTO applicationStateDTO,
			Camera camera) {
		this.applicationStateDTO = applicationStateDTO;
		this.camera = camera;
	}

	@Override
	public void update() {
		Spatial model = applicationStateDTO.getCurrentlySelectedModel();
		if (model != null) {
			Node currentlySelectedModel = model.getParent();
			if (applicationStateDTO.isMovingForward()) {
				Vector3f dir = camera.getDirection();
				Vector3f xyzDir = new Vector3f(Math.round(dir.getX()),
						Math.round(dir.getY()), Math.round(dir.getZ()));
				currentlySelectedModel.setLocalTranslation(
						currentlySelectedModel.getLocalTranslation()
											  .add(xyzDir.mult(
													  SLOW_CAMERA_SPEED)));
			}
			if (applicationStateDTO.isMovingBackward()) {
				Vector3f dir = camera.getDirection();
				Vector3f xyzDir = new Vector3f(Math.round(dir.getX()),
						Math.round(dir.getY()), Math.round(dir.getZ()));
				currentlySelectedModel.setLocalTranslation(
						currentlySelectedModel.getLocalTranslation()
											  .add(xyzDir.mult(
													  -SLOW_CAMERA_SPEED)));
			}
			if (applicationStateDTO.isMovingRight()) {
				currentlySelectedModel.setLocalTranslation(
						currentlySelectedModel.getLocalTranslation()
											  .add(camera.getLeft()
														 .mult(-SLOW_CAMERA_SPEED)));
			}
			if (applicationStateDTO.isMovingLeft()) {
				currentlySelectedModel.setLocalTranslation(
						currentlySelectedModel.getLocalTranslation()
											  .add(camera.getLeft()
														 .mult(SLOW_CAMERA_SPEED)));
			}
			if (applicationStateDTO.isMovingUp()) {
				currentlySelectedModel.setLocalTranslation(
						currentlySelectedModel.getLocalTranslation()
											  .add(camera.getUp()
														 .mult(SLOW_CAMERA_SPEED)));
			}
			if (applicationStateDTO.isMovingDown()) {
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
