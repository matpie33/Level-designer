package initialization;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.scene.Spatial;
import dto.ApplicationStateDTO;
import dto.NodeDTO;

import java.util.Optional;

public class CollisionListener implements PhysicsCollisionListener {

	private ApplicationStateDTO applicationStateDTO;

	public CollisionListener(ApplicationStateDTO applicationStateDTO) {
		this.applicationStateDTO = applicationStateDTO;
	}

	private Optional<NodeDTO> getOptionallySelectedModelData(Spatial nodeA) {
		return applicationStateDTO.getSelectedModels()
								  .stream()
								  .filter(geometryDTO -> geometryDTO.getNode()
																	.equals(nodeA))
								  .findFirst();
	}

	@Override
	public void collision(PhysicsCollisionEvent physicsCollisionEvent) {

		Spatial nodeA = physicsCollisionEvent.getNodeA();
		Spatial nodeB = physicsCollisionEvent.getNodeB();
		if (nodeA.equals(nodeB)) {
			return;
		}
		Optional<NodeDTO> nodeASelection = getOptionallySelectedModelData(
				nodeA);
		Optional<NodeDTO> nodeBSelection = getOptionallySelectedModelData(
				nodeB);
		if (!nodeASelection.isPresent() && !nodeBSelection.isPresent()) {
			return;
		}
		nodeASelection.ifPresent(node -> {
			node.setColliding(true);
			node.setCollidingSpatial(nodeB);
			node.setColisionPointLocalOnThisObject(physicsCollisionEvent.getLocalPointA());
			node.setDistanceToMoveOutOfCollision(physicsCollisionEvent.getDistance1());
		});
		nodeBSelection.ifPresent(node -> {
			node.setColliding(true);
			node.setCollidingSpatial(nodeA);
			node.setColisionPointLocalOnThisObject(physicsCollisionEvent.getLocalPointB());
			node.setDistanceToMoveOutOfCollision(physicsCollisionEvent.getDistance1());
		});

	}
}
