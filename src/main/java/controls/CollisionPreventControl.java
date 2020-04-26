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
import dto.GeometryDTO;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CollisionPreventControl extends AbstractControl
		implements PhysicsTickListener {

	public static final float x = 120f;
	private ApplicationStateDTO applicationStateDTO;
	private Spatial spatial;
	private boolean overlapsOtherObject;
	private Vector3f positionToMoveTo;

	public static final float SLOW_CAMERA_SPEED = 0.3f;
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
		if (overlapsOtherObject && positionToMoveTo != null) {
			control.setPhysicsLocation(positionToMoveTo.clone());
		}
		else {
			geometryData.setColliding(false);
			positionToMoveTo = null;
			moveBasedOnKeyPress(geometryData);
		}
	}

	private boolean overlapsOtherObjects(GhostControl control) {
		List<PhysicsCollisionObject> overlappingObjects = control.getOverlappingObjects();
		return overlappingObjects.size() > 1 || (overlappingObjects.size() == 1
				&& !spatial.equals(overlappingObjects.get(0)
													 .getUserObject()));
	}

	private void findDirectionToMove() {

		Optional<GeometryDTO> selectedModelData = applicationStateDTO.getSelectedModels()
																	 .stream()
																	 .filter(geometryDTO -> geometryDTO.getGeometry()
																									   .getParent()
																									   .equals(spatial))
																	 .findFirst();
		if (!selectedModelData.isPresent()) {
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
		Vector3f directionToMove = camera.getLeft()
										 .negate();
		directionToMove = new Vector3f(Math.round(directionToMove.getX()),
				Math.round(directionToMove.getY()),
				Math.round(directionToMove.getZ()));

		Vector3f overlappingSpatialLocation = overlappingSpatial.getControl(
				GhostControl.class)
																.getPhysicsLocation();

		CollisionShape myShape = spatial.getControl(GhostControl.class)
										.getCollisionShape();
		Vector3f myExtents = getExtents(spatial, myShape).mult(directionToMove);
		Vector3f vectorToMove = overlappingSpatialExtents.mult(directionToMove)
														 .add(myExtents)
														 .add(directionToMove.mult(
																 0.2f));
		positionToMoveTo = overlappingSpatialLocation.clone()
													 .add(vectorToMove);

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
			findDirectionToMove();
		}
	}

}
