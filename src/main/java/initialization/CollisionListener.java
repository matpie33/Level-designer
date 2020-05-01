package initialization;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import dto.ApplicationStateDTO;
import dto.NodeDTO;

import java.util.Optional;
import java.util.function.Function;

public class CollisionListener implements PhysicsCollisionListener {

	public static final float x = 120f;
	private ApplicationStateDTO applicationStateDTO;

	public CollisionListener(ApplicationStateDTO applicationStateDTO) {
		this.applicationStateDTO = applicationStateDTO;
	}

	private void moveInDirection(Spatial nodeA, Vector3f directionToMove) {
		nodeA.getControl(CharacterControl.class)
			 .setWalkDirection(directionToMove);

	}

	private Optional<NodeDTO> getOptionallySelectedModelData(Spatial nodeA) {
		return applicationStateDTO.getSelectedModels()
								  .stream()
								  .filter(geometryDTO -> geometryDTO.getNode()
																	.equals(nodeA))
								  .findFirst();
	}

	private boolean isSpatialNotSelected(Spatial nodeA) {
		Optional<NodeDTO> selectedModelData = getOptionallySelectedModelData(
				nodeA);
		return !selectedModelData.isPresent();
	}

	private Vector3f findDirectionToMove(Spatial nodeA, Spatial nodeB) {

		Vector3f overlappingSpatialExtents = getExtents(nodeB);
		Vector3f myExtents = getExtents(nodeA);
		Vector3f directionToMove = getDirectionToMove(nodeB,
				overlappingSpatialExtents, myExtents, nodeA);

		return directionToMove.mult(0.1f);
	}

	private Vector3f getDirectionToMove(Spatial overlappingSpatial,
			Vector3f overlappingSpatialExtents, Vector3f myExtents,
			Spatial me) {
		Vector3f directionToMove;
		Function<Vector3f, Float> getX = Vector3f::getX;
		Function<Vector3f, Float> getY = Vector3f::getY;
		Function<Vector3f, Float> getZ = Vector3f::getZ;
		if (checkDirection(overlappingSpatial, myExtents,
				overlappingSpatialExtents, getX, me)) {
			float signum = Math.signum(
					getX.apply(me.getLocalTranslation()) - getX.apply(
							overlappingSpatial.getLocalTranslation()));
			directionToMove = new Vector3f(signum, 0, 0);
		}
		else if (checkDirection(overlappingSpatial, myExtents,
				overlappingSpatialExtents, getY, me)) {
			float signum = Math.signum(
					getY.apply(me.getLocalTranslation()) - getY.apply(
							overlappingSpatial.getLocalTranslation()));
			directionToMove = new Vector3f(0, signum, 0);
		}
		else {
			float signum = Math.signum(
					getZ.apply(me.getLocalTranslation()) - getZ.apply(
							overlappingSpatial.getLocalTranslation()));
			directionToMove = new Vector3f(0, 0, signum);
		}
		return directionToMove;
	}

	private boolean checkDirection(Spatial overlappingSpatial,
			Vector3f myExtents, Vector3f overlappingSpatialExtents,
			Function<Vector3f, Float> getCoordinate, Spatial me) {
		Vector3f localTranslation = overlappingSpatial.getLocalTranslation();
		float yC = getCoordinate.apply(localTranslation);
		float yS = getCoordinate.apply(me.getLocalTranslation());
		return Math.abs(yS - yC)
				> getCoordinate.apply(myExtents) + getCoordinate.apply(
				overlappingSpatialExtents);
	}

	private Vector3f getExtents(Spatial spatial) {
		return ((BoundingBox) spatial.getWorldBound()).getExtent(
				new Vector3f());
	}

	@Override
	public void collision(PhysicsCollisionEvent physicsCollisionEvent) {

		Spatial nodeA = physicsCollisionEvent.getNodeA();
		if (isSpatialNotSelected(nodeA) && isSpatialNotSelected(
				physicsCollisionEvent.getNodeB())) {
			return;
		}
		Vector3f directionToMove = findDirectionToMove(nodeA,
				physicsCollisionEvent.getNodeB());
		moveInDirection(nodeA, directionToMove);
	}
}
