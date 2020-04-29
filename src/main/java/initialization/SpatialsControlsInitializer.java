package initialization;

import com.jme3.app.state.AppStateManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import controls.CollisionPreventControl;
import dto.ApplicationStateDTO;

import java.util.List;

public class SpatialsControlsInitializer {

	private AppStateManager appStateManager;
	private BulletAppState bulletAppState;
	private ApplicationStateDTO applicationStateDTO;
	private Node rootNode;
	private Camera camera;

	public SpatialsControlsInitializer(AppStateManager appStateManager,
			ApplicationStateDTO applicationStateDTO, Node rootNode,
			Camera camera) {
		this.appStateManager = appStateManager;
		this.applicationStateDTO = applicationStateDTO;
		this.rootNode = rootNode;
		this.camera = camera;
	}

	public void attachControls(List<Spatial> spatials) {
		attachControl(spatials);

	}


	private void attachControl(List<Spatial> spatials) {
		for (Spatial spatial : spatials) {
			attachControl(spatial);
		}
	}

	public void attachControl(Spatial spatial) {
		CollisionShape shape = new BoxCollisionShape(
				((BoundingBox) spatial.getWorldBound()).getExtent(
						new Vector3f()));
		if (spatial.getKey()
				   .getName()
				   .contains("map")) {
			shape = CollisionShapeFactory.createMeshShape(spatial);
		}
		CollisionPreventControl collisionPreventControl = new CollisionPreventControl(
				applicationStateDTO, spatial, camera);
		spatial.addControl(collisionPreventControl);
		BulletAppState bulletAppState = appStateManager.getState(BulletAppState.class);
		bulletAppState.getPhysicsSpace()
						   .addTickListener(collisionPreventControl);
	}

}
