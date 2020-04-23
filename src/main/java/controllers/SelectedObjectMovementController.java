package controllers;

import dto.ApplicationStateDTO;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import dto.GeometryColorDTO;

public class SelectedObjectMovementController implements AbstractController {

	public static final float SLOW_CAMERA_SPEED = 0.05f;
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
		for (GeometryColorDTO selectedModel : applicationStateDTO.getSelectedModels()) {
			move(selectedModel);
		}
	}

	private void move(GeometryColorDTO model) {
		if (model != null) {
			Node currentlySelectedModel = model.getGeometry().getParent();
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
