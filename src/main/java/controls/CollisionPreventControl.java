package controls;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.control.GhostControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import dto.ApplicationStateDTO;
import dto.GeometryDTO;
import enums.Direction;

import java.util.*;

public class CollisionPreventControl extends AbstractControl
		implements PhysicsTickListener {

	public static final float x = 120f;
	private ApplicationStateDTO applicationStateDTO;
	private Node rootNode;
	private List<Vector3f> possibleDirectionsToMove = Arrays.asList(
			Vector3f.UNIT_X, Vector3f.UNIT_X.negate(), Vector3f.UNIT_Y,
			Vector3f.UNIT_Y.negate(), Vector3f.UNIT_Z,
			Vector3f.UNIT_Z.negate());
	private Spatial spatial;
	private boolean overlapsOtherObject;
	private Vector3f positionToMoveTo;

	public static final float SLOW_CAMERA_SPEED = 0.3f;
	private Camera camera;

	public CollisionPreventControl(ApplicationStateDTO applicationStateDTO,
			Node rootNode, Spatial spatial, Camera camera) {
		this.applicationStateDTO = applicationStateDTO;
		this.rootNode = rootNode;
		this.spatial = spatial;
		this.camera = camera;
	}

	@Override
	protected void controlUpdate(float tpf) {
		spatial.setLocalTranslation(spatial.getControl(GhostControl.class)
										   .getPhysicsLocation());
	}

	private void moveOutOfCollision() {
		GhostControl control = spatial.getControl(GhostControl.class);
		Optional<GeometryDTO> selectedModelData = applicationStateDTO.getSelectedModels()
																	 .stream()
																	 .filter(geometryDTO -> geometryDTO.getGeometry()
																									   .getParent()
																									   .equals(spatial))
																	 .findFirst();
		if (!selectedModelData.isPresent()) {
			return;
		}
		GeometryDTO geometryData = selectedModelData.get();
		if (overlapsOtherObject) {
			control.setPhysicsLocation(positionToMoveTo.clone());
		}
		else {
			geometryData.setColliding(false);
			positionToMoveTo = null;
			moveBasedOnKeyPress(geometryData);
		}
	}

	private boolean overlapsOtherObjects(GhostControl control) {
		return control.getOverlappingCount() > 1 || (
				control.getOverlappingCount() == 1 && !spatial.equals(
						control.getOverlappingObjects()
							   .get(0)
							   .getUserObject()));
	}

	private void getCoordinatesToMoveTo(Vector3f directionToMove) {
		Vector3f position = spatial.getLocalTranslation()
								   .clone();
		if (directionToMove.getX() != 0) {
			position.setX(position.getX() + directionToMove.getX());
		}
		if (directionToMove.getY() != 0) {
			position.setY(position.getY() + directionToMove.getY());
		}
		if (directionToMove.getZ() != 0) {
			position.setZ(position.getZ() + directionToMove.getZ());
		}
		positionToMoveTo = position;
	}

	private Vector3f findDirectionToMove() {

		Optional<Spatial> first = getAnyOfCollidingObjects();
		if (!first.isPresent()) {
			return spatial.getControl(GhostControl.class)
						  .getPhysicsLocation();
		}
		Spatial overlappingSpatial = first.get();
		Direction directionToMove = getSmallestExtentOfSpatial(
				overlappingSpatial);
		Map<Vector3f, Float> directionAndDistanceToObstacle = findDistancesToObstaclesInAllDirections(
				overlappingSpatial);

		return decideOnDirection(directionAndDistanceToObstacle,
				directionToMove);

	}

	private Optional<Spatial> getAnyOfCollidingObjects() {
		GhostControl control = spatial.getControl(GhostControl.class);
		return control.getOverlappingObjects()
					  .stream()
					  .map(PhysicsCollisionObject::getUserObject)
					  .map(Spatial.class::cast)
					  .filter(Objects::nonNull)
					  .filter(spatial -> !spatial.equals(this.spatial))
					  .findFirst();
	}

	private Map<Vector3f, Float> findDistancesToObstaclesInAllDirections(
			Spatial overlappingSpatial) {
		Map<Vector3f, Float> directionAndDistanceToObstacle = new HashMap<>();

		for (Vector3f direction : possibleDirectionsToMove) {
			Vector3f position = overlappingSpatial.getLocalTranslation()
												  .clone();
			Ray ray = new Ray(position, direction);
			CollisionResults collisionResults = new CollisionResults();
			rootNode.collideWith(ray, collisionResults);
			float closestCollisionDistance = Float.MAX_VALUE;
			for (CollisionResult collisionResult : collisionResults) {
				float distance = collisionResult.getDistance();
				if (distance < closestCollisionDistance
						&& !collisionResult.getGeometry()
										   .getParent()
										   .equals(this.spatial)) {
					closestCollisionDistance = distance;
				}
			}
			directionAndDistanceToObstacle.put(direction,
					closestCollisionDistance);

		}
		return directionAndDistanceToObstacle;
	}

	private Vector3f decideOnDirection(
			Map<Vector3f, Float> directionAndDistanceToObstacle,
			Direction directionToMove) {
		float minDistance = Float.MAX_VALUE;
		Vector3f vectorWithMinDistance = null;
		for (Map.Entry<Vector3f, Float> entry : directionAndDistanceToObstacle.entrySet()) {
			Vector3f direction = entry.getKey();
			if (directionToMove.isVectorSameDirection(direction)
					&& entry.getValue()
							.equals(Float.MAX_VALUE)) {
				return entry.getKey();
			}
			else if (entry.getValue() < minDistance) {
				minDistance = entry.getValue();
				vectorWithMinDistance = direction;
			}
		}
		return vectorWithMinDistance;
	}

	private Direction getSmallestExtentOfSpatial(Spatial overlappingSpatial) {
		BoundingBox worldBound = (BoundingBox) overlappingSpatial.getWorldBound();
		float xExtent = worldBound.getXExtent();
		float yExtent = worldBound.getYExtent();
		float zExtent = worldBound.getZExtent();
		float minXY = Math.min(xExtent, yExtent);
		float min = Math.min(minXY, zExtent);
		Direction direction;
		if (min == xExtent) {
			direction = Direction.X;
		}
		else if (min == yExtent) {
			direction = Direction.Y;
		}
		else {
			direction = Direction.Z;
		}
		return direction;
	}

	private IllegalArgumentException exc() {
		return null;
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {

	}

	private void moveBasedOnKeyPress(GeometryDTO model) {
		if (model != null && !model.isColliding()) {
			Node currentlySelectedModel = model.getGeometry()
											   .getParent();
			GhostControl control = currentlySelectedModel.getControl(
					GhostControl.class);
			if (applicationStateDTO.isMovingForward()) {
				Vector3f dir = camera.getDirection();
				Vector3f xyzDir = new Vector3f(Math.round(dir.getX()),
						Math.round(dir.getY()), Math.round(dir.getZ()));
				control.setPhysicsLocation(control.getPhysicsLocation()
												  .add(xyzDir.mult(
														  SLOW_CAMERA_SPEED)));
			}
			if (applicationStateDTO.isMovingBackward()) {
				Vector3f dir = camera.getDirection();
				Vector3f xyzDir = new Vector3f(Math.round(dir.getX()),
						Math.round(dir.getY()), Math.round(dir.getZ()));
				control.setPhysicsLocation(control.getPhysicsLocation()
												  .add(xyzDir.mult(
														  -SLOW_CAMERA_SPEED)));
			}
			if (applicationStateDTO.isMovingRight()) {
				control.setPhysicsLocation(control.getPhysicsLocation()
												  .add(camera.getLeft()
															 .mult(-SLOW_CAMERA_SPEED)));
			}
			if (applicationStateDTO.isMovingLeft()) {
				control.setPhysicsLocation(control.getPhysicsLocation()
												  .add(camera.getLeft()
															 .mult(SLOW_CAMERA_SPEED)));
			}
			if (applicationStateDTO.isMovingUp()) {
				control.setPhysicsLocation(control.getPhysicsLocation()
												  .add(camera.getUp()
															 .mult(SLOW_CAMERA_SPEED)));
			}
			if (applicationStateDTO.isMovingDown()) {
				control.setPhysicsLocation(control.getPhysicsLocation()
												  .add(camera.getUp()
															 .mult(-SLOW_CAMERA_SPEED)));
			}
		}
	}

	@Override
	public void prePhysicsTick(PhysicsSpace space, float tpf) {
		moveOutOfCollision();

	}

	@Override
	public void physicsTick(PhysicsSpace space, float tpf) {

		overlapsOtherObject = overlapsOtherObjects(
				spatial.getControl(GhostControl.class));
		if (overlapsOtherObject) {
			findCoordinatesToMoveAwayFromCollision();
		}
	}

	private void findCoordinatesToMoveAwayFromCollision() {
		Vector3f directionToMove = findDirectionToMove();
		getCoordinatesToMoveTo(directionToMove);
	}

}
