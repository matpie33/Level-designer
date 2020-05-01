package controls;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import dto.ApplicationStateDTO;
import dto.DirectionToMoveDTO;
import dto.NodeDTO;
import dto.OptionsDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

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
			moveOutOfCollision(selectionData.getCollidingSpatial());
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
		if (walkDirection != null) {
			control.setWalkDirection(walkDirection);
		}
	}

	private void moveOutOfCollision(Spatial collidingSpatial) {
		Vector3f directionToMove = findDirectionToMove(spatial,
				collidingSpatial);
		Vector3f add = spatial.getLocalTranslation()
							  .add(directionToMove);
		spatial.setLocalTranslation(add);
		spatial.getControl(GhostControl.class)
			   .setPhysicsLocation(add);
		spatial.getControl(CharacterControl.class)
			   .setPhysicsLocation(add);
	}

	private Vector3f findDirectionToMove(Spatial spatialToMove,
			Spatial spatialToStay) {

		Vector3f overlappingSpatialExtents = getExtents(spatialToStay);
		Vector3f myExtents = getExtents(spatialToMove);
		DirectionToMoveDTO directionToMove = getDirectionToMove(spatialToStay,
				overlappingSpatialExtents, myExtents, spatialToMove);

		Vector3f vectorToMove = new Vector3f();
		directionToMove.getSetCoordinateFunction()
					   .accept(vectorToMove,
							   directionToMove.getAmountToMove() * (
									   directionToMove.isPositiveDirection() ?
											   1 :
											   -1));
		return vectorToMove;
	}

	private DirectionToMoveDTO getDirectionToMove(Spatial spatialToStay,
			Vector3f overlappingSpatialExtents, Vector3f myExtents,
			Spatial spatialToMove) {
		Function<Vector3f, Float> getX = Vector3f::getX;
		Function<Vector3f, Float> getY = Vector3f::getY;
		Function<Vector3f, Float> getZ = Vector3f::getZ;
		BiConsumer<Vector3f, Float> setX = Vector3f::setX;
		BiConsumer<Vector3f, Float> setY = Vector3f::setY;
		BiConsumer<Vector3f, Float> setZ = Vector3f::setZ;
		List<DirectionToMoveDTO> directionToMoveDTOS = Arrays.asList(
				new DirectionToMoveDTO(getX, setX, true),
				new DirectionToMoveDTO(getX, setX, false),
				new DirectionToMoveDTO(getY, setY, true),
				new DirectionToMoveDTO(getY, setY, false),
				new DirectionToMoveDTO(getZ, setZ, true),
				new DirectionToMoveDTO(getZ, setZ, false));
		float minAmountToMove = Float.MAX_VALUE;
		DirectionToMoveDTO bestDirection = null;
		for (DirectionToMoveDTO directionToMoveDTO : directionToMoveDTOS) {
			float amountToMoveInDirection = findAmountToMoveNeededDirectionX(
					spatialToStay, overlappingSpatialExtents, myExtents,
					spatialToMove, directionToMoveDTO.getCoordinateFunction(),
					directionToMoveDTO.isPositiveDirection() ? 1 : -1);
			if (amountToMoveInDirection < minAmountToMove) {
				minAmountToMove = amountToMoveInDirection;
				bestDirection = directionToMoveDTO;
			}
		}
		assert bestDirection != null;
		bestDirection.setAmountToMove(minAmountToMove);
		return bestDirection;

	}

	private float findAmountToMoveNeededDirectionX(Spatial spatialToStay,
			Vector3f overlappingSpatialExtents, Vector3f myExtents,
			Spatial spatialToMove, Function<Vector3f, Float> getCoordinate,
			int modifier) {
		float amountToMoveNeeded =
				getCoordinate.apply(spatialToMove.getLocalTranslation())
						- Math.abs(
						getCoordinate.apply(spatialToStay.getLocalTranslation())
								+ modifier * getCoordinate.apply(
								overlappingSpatialExtents)
								+ getCoordinate.apply(myExtents) * modifier);
		return Math.abs(amountToMoveNeeded);
	}

	private boolean checkDirection(Spatial spatialToStay, Vector3f myExtents,
			Vector3f overlappingSpatialExtents,
			Function<Vector3f, Float> getCoordinate, Spatial spatialToMove) {
		Vector3f localTranslation = spatialToStay.getLocalTranslation();
		float yC = getCoordinate.apply(localTranslation);
		float yS = getCoordinate.apply(spatialToMove.getLocalTranslation());
		return Math.abs(yS - yC)
				<= getCoordinate.apply(myExtents) + getCoordinate.apply(
				overlappingSpatialExtents);
	}

	private Vector3f getExtents(Spatial spatial) {
		Vector3f extents;
		CollisionShape collisionShape = spatial.getControl(GhostControl.class)
											   .getCollisionShape();
		if (!(collisionShape instanceof BoxCollisionShape)) {
			extents = ((BoundingBox) spatial.getWorldBound()).getExtent(
					new Vector3f());
		}
		else {
			extents = ((BoxCollisionShape) collisionShape).getHalfExtents();

		}
		return extents;
	}

}
