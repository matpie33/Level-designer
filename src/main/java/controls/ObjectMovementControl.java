package controls;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import dto.ApplicationStateDTO;
import dto.NodeDTO;
import dto.OptionsDTO;

import java.util.Optional;

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
		Optional<NodeDTO> selectionData = applicationStateDTO.getSelectedModels()
															 .stream()
															 .filter(node -> node.getNode()
																				 .equals(spatial))
															 .findFirst();
		selectionData.ifPresent(this::move);
	}

	@Override
	protected void controlRender(RenderManager renderManager,
			ViewPort viewPort) {

	}

	private void move(NodeDTO selectionData) {
		Vector3f walkDirection = Vector3f.ZERO;
		if (selectionData.isColliding()) {
			moveOutOfCollision(selectionData);
			selectionData.setColliding(false);
			selectionData.setCollidingSpatial(null);
			return;
		}
		moveWithKeyPress(walkDirection);
	}

	private void moveWithKeyPress(Vector3f walkDirection) {
		float movementSpeed = OptionsDTO.getInstance()
										.getModelMovementSpeed();
		CharacterControl control = spatial.getControl(CharacterControl.class);
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
		if (walkDirection != null && control != null) {
			control.setWalkDirection(walkDirection);
		}
		if (control == null && walkDirection != null) {
			spatial.setLocalTranslation(spatial.getLocalTranslation()
											   .add(walkDirection));
		}
	}

	private void moveOutOfCollision(NodeDTO nodeData) {
		Vector3f directionToMove = findDirectionToMove(spatial, nodeData);
		Vector3f add = spatial.getLocalTranslation()
							  .add(directionToMove);
		spatial.setLocalTranslation(add);
		spatial.getControl(GhostControl.class)
			   .setPhysicsLocation(add);
		spatial.getControl(CharacterControl.class)
			   .setPhysicsLocation(add);
	}

	private Vector3f findDirectionToMove(Spatial spatialToMove,
			NodeDTO nodeData) {
		Vector3f colisionPointLocal = nodeData.getColisionPointLocalOnThisObject();
		float distanceToMove = nodeData.getDistanceToMoveOutOfCollision();
		Vector3f extent = ((BoundingBox) spatialToMove.getWorldBound()).getExtent(
				new Vector3f());
		Vector3f vectorToMove = new Vector3f();
		float distanceToMoveWithAdditionalValue = distanceToMove + 0.5f;
		if (colisionPointLocal.getX() == extent.getX()) {
			vectorToMove = new Vector3f(distanceToMoveWithAdditionalValue, 0,
					0);
		}
		if (colisionPointLocal.getY() == extent.getY()) {
			vectorToMove = new Vector3f(0, distanceToMoveWithAdditionalValue,
					0);
		}
		if (colisionPointLocal.getZ() == extent.getZ()) {
			vectorToMove = new Vector3f(0, 0,
					distanceToMoveWithAdditionalValue);
		}
		return vectorToMove;
	}

}
