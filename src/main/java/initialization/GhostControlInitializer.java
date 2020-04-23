package initialization;

import com.jme3.app.state.AppStateManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.scene.Spatial;

import java.util.List;

public class GhostControlInitializer {

	private AppStateManager appStateManager;
	private BulletAppState bulletAppState;

	public GhostControlInitializer(AppStateManager appStateManager) {
		this.appStateManager = appStateManager;
	}

	public void attachGhostControl(List<Spatial> spatials) {
		initializeBulletAppState();
		attachControl(spatials);

	}

	private void initializeBulletAppState() {
		bulletAppState = new BulletAppState();
		bulletAppState.setDebugEnabled(true);
		appStateManager.attach(bulletAppState);
	}

	private void attachControl(List<Spatial> spatials) {
		for (Spatial spatial : spatials) {
			BoundingBox size = (BoundingBox) spatial.getWorldBound();
			float maxXY = Math.max(size.getXExtent(), size.getYExtent());
			float maximumDimension = Math.max(maxXY, size.getZExtent());
			CollisionShape shape = new SphereCollisionShape(
					maximumDimension);
			if (spatial.getKey()
					   .getName()
					   .contains("map")) {
				shape = CollisionShapeFactory.createMeshShape(spatial);
			}
			GhostControl ghostControl = new GhostControl(shape);
			spatial.addControl(ghostControl);
			bulletAppState.getPhysicsSpace()
						  .add(spatial);
		}
	}

}
