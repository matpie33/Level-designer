package controls;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import dto.ApplicationStateDTO;
import dto.NodeDTO;
import dto.OptionsDTO;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class CollisionPreventControl extends AbstractControl
		implements PhysicsTickListener {

	public static final float x = 120f;
	private ApplicationStateDTO applicationStateDTO;
	private Spatial spatial;
	private boolean overlapsOtherObject;
	private Vector3f positionToMoveTo;

	private Camera camera;

	public CollisionPreventControl(ApplicationStateDTO applicationStateDTO,
			Spatial spatial, Camera camera) {
		this.applicationStateDTO = applicationStateDTO;
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
		Optional<NodeDTO> selectedModelData = getOptionallySelectedModelData();
		if (!selectedModelData.isPresent()) {
			return;
		}
		NodeDTO geometryData = selectedModelData.get();
		if (overlapsOtherObject && positionToMoveTo != null) {
			control.setPhysicsLocation(positionToMoveTo.clone());
		}
		else {
			geometryData.setColliding(false);
			positionToMoveTo = null;
		}
		moveBasedOnKeyPress(geometryData);
	}

	private Optional<NodeDTO> getOptionallySelectedModelData() {
		return applicationStateDTO.getSelectedModels()
								  .stream()
								  .filter(geometryDTO -> geometryDTO.getNode()
																	.equals(spatial))
								  .findFirst();
	}

	private boolean overlapsOtherObjects(GhostControl control) {
		if (true){
			return false;
		}
		List<PhysicsCollisionObject> overlappingObjects = control.getOverlappingObjects();
		return overlappingObjects.size() > 1 || (overlappingObjects.size() == 1
				&& !spatial.equals(overlappingObjects.get(0)
													 .getUserObject()));
	}

	private boolean isThisSpatialSelected() {
		Optional<NodeDTO> selectedModelData = getOptionallySelectedModelData();
		return selectedModelData.isPresent();
	}

	private void findDirectionToMove() {

		if (!isThisSpatialSelected()) {
			return;
		}

		Optional<Spatial> first = getAnyOfCollidingObjects();
		if (!first.isPresent()) {
			return;
		}
		Spatial overlappingSpatial = first.get();
		CollisionShape collisionShape = overlappingSpatial.getControl(
				GhostControl.class)
														  .getCollisionShape();

		Vector3f overlappingSpatialExtents = getExtents(overlappingSpatial,
				collisionShape);

		CollisionShape myShape = spatial.getControl(GhostControl.class)
										.getCollisionShape();
		Vector3f myExtents = getExtents(spatial, myShape);
		Vector3f directionToMove = getDirectionToMove(overlappingSpatial,
				overlappingSpatialExtents, myExtents);

		positionToMoveTo = spatial.getLocalTranslation()
								  .clone()
								  .add(directionToMove.mult(0.1f));
	}

	private Vector3f getDirectionToMove(Spatial overlappingSpatial,
			Vector3f overlappingSpatialExtents, Vector3f myExtents) {
		Vector3f directionToMove;
		Function<Vector3f, Float> getX = Vector3f::getX;
		Function<Vector3f, Float> getY = Vector3f::getY;
		Function<Vector3f, Float> getZ = Vector3f::getZ;
		if (checkDirection(overlappingSpatial, myExtents,
				overlappingSpatialExtents, getX)) {
			float signum = Math.signum(
					getX.apply(spatial.getLocalTranslation()) - getX.apply(
							overlappingSpatial.getLocalTranslation()));
			directionToMove = new Vector3f(signum, 0, 0);
		}
		else if (checkDirection(overlappingSpatial, myExtents,
				overlappingSpatialExtents, getY)) {
			float signum = Math.signum(
					getY.apply(spatial.getLocalTranslation()) - getY.apply(
							overlappingSpatial.getLocalTranslation()));
			directionToMove = new Vector3f(0, signum, 0);
		}
		else {
			float signum = Math.signum(
					getZ.apply(spatial.getLocalTranslation()) - getZ.apply(
							overlappingSpatial.getLocalTranslation()));
			directionToMove = new Vector3f(0, 0, signum);
		}
		return directionToMove;
	}

	private boolean checkDirection(Spatial overlappingSpatial,
			Vector3f myExtents, Vector3f overlappingSpatialExtents,
			Function<Vector3f, Float> getCoordinate) {
		Vector3f localTranslation = overlappingSpatial.getLocalTranslation();
		float yC = getCoordinate.apply(localTranslation);
		float yS = getCoordinate.apply(spatial.getLocalTranslation());
		return Math.abs(yS - yC)
				> getCoordinate.apply(myExtents) + getCoordinate.apply(
				overlappingSpatialExtents);
	}

	private Vector3f getExtents(Spatial spatial,
			CollisionShape collisionShape) {
		Vector3f extents;
		if (!(collisionShape instanceof BoxCollisionShape)) {
			extents = ((BoundingBox) spatial.getWorldBound()).getExtent(
					new Vector3f());
		}
		else {
			extents = ((BoxCollisionShape) collisionShape).getHalfExtents();

		}
		return extents;
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

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {

	}

	private void moveBasedOnKeyPress(NodeDTO model) {
		if (model != null && !model.isColliding()) {
			Node currentlySelectedNode = model.getNode();
			GhostControl control = currentlySelectedNode.getControl(
					GhostControl.class);
			float movementSpeed = OptionsDTO.getInstance()
											.getModelMovementSpeed();
			if (applicationStateDTO.isMovingForward()) {
				Vector3f dir = camera.getDirection();
				Vector3f xyzDir = new Vector3f(Math.round(dir.getX()),
						Math.round(dir.getY()), Math.round(dir.getZ()));
				control.setPhysicsLocation(control.getPhysicsLocation()
												  .add(xyzDir.mult(
														  movementSpeed)));
			}
			if (applicationStateDTO.isMovingBackward()) {
				Vector3f dir = camera.getDirection();
				Vector3f xyzDir = new Vector3f(Math.round(dir.getX()),
						Math.round(dir.getY()), Math.round(dir.getZ()));
				control.setPhysicsLocation(control.getPhysicsLocation()
												  .add(xyzDir.mult(
														  -movementSpeed)));
			}
			if (applicationStateDTO.isMovingRight()) {
				control.setPhysicsLocation(control.getPhysicsLocation()
												  .add(camera.getLeft()
															 .mult(-movementSpeed)));
			}
			if (applicationStateDTO.isMovingLeft()) {
				control.setPhysicsLocation(control.getPhysicsLocation()
												  .add(camera.getLeft()
															 .mult(movementSpeed)));
			}
			if (applicationStateDTO.isMovingUp()) {
				control.setPhysicsLocation(control.getPhysicsLocation()
												  .add(camera.getUp()
															 .mult(movementSpeed)));
			}
			if (applicationStateDTO.isMovingDown()) {
				control.setPhysicsLocation(control.getPhysicsLocation()
												  .add(camera.getUp()
															 .mult(-movementSpeed)));
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
			findDirectionToMove();
		}
	}

}
