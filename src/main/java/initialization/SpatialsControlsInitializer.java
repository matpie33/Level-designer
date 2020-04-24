package initialization;

import com.jme3.app.state.AppStateManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.scene.Spatial;
import controls.CollisionPreventControl;
import dto.ApplicationStateDTO;

import java.util.List;

public class SpatialsControlsInitializer {

	private AppStateManager appStateManager;
	private BulletAppState bulletAppState;
	private ApplicationStateDTO applicationStateDTO;

	public SpatialsControlsInitializer(AppStateManager appStateManager,
			ApplicationStateDTO applicationStateDTO) {
		this.appStateManager = appStateManager;
		this.applicationStateDTO = applicationStateDTO;
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
			attachControl(spatial);
		}
	}

	public void attachControl(Spatial spatial) {
		BoundingBox size = (BoundingBox) spatial.getWorldBound();
		float maxXY = Math.max(size.getXExtent(), size.getYExtent());
		float maximumDimension = Math.max(maxXY, size.getZExtent());
		CollisionShape shape = new SphereCollisionShape(maximumDimension);
		if (spatial.getKey()
				   .getName()
				   .contains("map")) {
			shape = CollisionShapeFactory.createMeshShape(spatial);
		}
		GhostControl ghostControl = new GhostControl(shape);
		spatial.addControl(
				new CollisionPreventControl(applicationStateDTO));
		spatial.addControl(ghostControl);
		bulletAppState.getPhysicsSpace()
					  .add(spatial);
	}

}
