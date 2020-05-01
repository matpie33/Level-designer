package controls;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import dto.ApplicationStateDTO;
import dto.OptionsDTO;

public class ObjectMovementControl extends AbstractControl {

	private ApplicationStateDTO applicationStateDTO;
	private Camera camera;

	public ObjectMovementControl(ApplicationStateDTO applicationStateDTO,
			Camera camera) {
		this.applicationStateDTO = applicationStateDTO;
		this.camera = camera;
	}

	@Override
	protected void controlUpdate(float v) {
		boolean isSelected = applicationStateDTO.getSelectedModels()
												.stream()
												.anyMatch(node -> node.getNode()
																	  .equals(spatial));
		if (isSelected){
			moveBasedOnKeyPress();
		}
	}

	@Override
	protected void controlRender(RenderManager renderManager,
			ViewPort viewPort) {

	}

	private void moveBasedOnKeyPress() {
		float movementSpeed = OptionsDTO.getInstance()
										.getModelMovementSpeed();
		CharacterControl control = spatial.getControl(CharacterControl.class);
		Vector3f walkDirection = null;
		if (applicationStateDTO.isMovingForward()) {
			Vector3f dir = camera.getDirection();
			Vector3f xyzDir = new Vector3f(Math.round(dir.getX()),
					Math.round(dir.getY()), Math.round(dir.getZ()));
			walkDirection = xyzDir.clone()
								  .mult(movementSpeed);
		}
		if (applicationStateDTO.isMovingBackward()) {
			Vector3f dir = camera.getDirection();
			Vector3f xyzDir = new Vector3f(Math.round(dir.getX()),
					Math.round(dir.getY()), Math.round(dir.getZ()));
			walkDirection = xyzDir.mult(-movementSpeed);
		}
		if (applicationStateDTO.isMovingRight()) {
			walkDirection = camera.getLeft()
								  .mult(-movementSpeed);
		}
		if (applicationStateDTO.isMovingLeft()) {
			walkDirection = camera.getLeft()
								  .mult(movementSpeed);
		}
		if (applicationStateDTO.isMovingUp()) {
			walkDirection = camera.getUp()
								  .mult(movementSpeed);
		}
		if (applicationStateDTO.isMovingDown()) {
			walkDirection = camera.getUp()
								  .mult(-movementSpeed);
		}
		if (walkDirection != null){
			control.setWalkDirection(walkDirection);
		}
	}

}
