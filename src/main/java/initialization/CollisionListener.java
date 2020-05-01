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

	private Vector3f findDirectionToMove(Spatial spatialToMove,
			Spatial spatialToStay) {

		Vector3f overlappingSpatialExtents = getExtents(spatialToStay);
		Vector3f myExtents = getExtents(spatialToMove);
		Vector3f directionToMove = getDirectionToMove(spatialToStay,
				overlappingSpatialExtents, myExtents, spatialToMove);

		return directionToMove.mult(0.1f);
	}

	private Vector3f getDirectionToMove(Spatial spatialToStay,
			Vector3f overlappingSpatialExtents, Vector3f myExtents,
			Spatial spatialToMove) {
		Vector3f directionToMove;
		Function<Vector3f, Float> getX = Vector3f::getX;
		Function<Vector3f, Float> getY = Vector3f::getY;
		Function<Vector3f, Float> getZ = Vector3f::getZ;
		if (checkDirection(spatialToStay, myExtents, overlappingSpatialExtents,
				getX, spatialToMove)) {
			float signum = Math.signum(
					getX.apply(spatialToMove.getLocalTranslation())
							- getX.apply(spatialToStay.getLocalTranslation()));
			directionToMove = new Vector3f(signum, 0, 0);
		}
		else if (checkDirection(spatialToStay, myExtents,
				overlappingSpatialExtents, getY, spatialToMove)) {
			float signum = Math.signum(
					getY.apply(spatialToMove.getLocalTranslation())
							- getY.apply(spatialToStay.getLocalTranslation()));
			directionToMove = new Vector3f(0, signum, 0);
		}
		else {
			float signum = Math.signum(
					getZ.apply(spatialToMove.getLocalTranslation())
							- getZ.apply(spatialToStay.getLocalTranslation()));
			directionToMove = new Vector3f(0, 0, signum);
		}
		return directionToMove;
	}

	private boolean checkDirection(Spatial spatialToStay, Vector3f myExtents,
			Vector3f overlappingSpatialExtents,
			Function<Vector3f, Float> getCoordinate, Spatial spatialToMove) {
		Vector3f localTranslation = spatialToStay.getLocalTranslation();
		float yC = getCoordinate.apply(localTranslation);
		float yS = getCoordinate.apply(spatialToMove.getLocalTranslation());
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
		Spatial nodeB = physicsCollisionEvent.getNodeB();
		Optional<NodeDTO> nodeASelection = getOptionallySelectedModelData(
				nodeA);
		Optional<NodeDTO> nodeBSelection = getOptionallySelectedModelData(
				nodeB);
		if (!nodeASelection.isPresent() && !nodeBSelection.isPresent()) {
			return;
		}
		Spatial spatialToMove = nodeASelection.isPresent() ? nodeA : nodeB;
		Spatial spatialToStay = nodeASelection.isPresent() ? nodeB : nodeA;
		Vector3f directionToMove = findDirectionToMove(spatialToMove,
				spatialToStay);
		moveInDirection(spatialToMove, directionToMove);
	}
}
