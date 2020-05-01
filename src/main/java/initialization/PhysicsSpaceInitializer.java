package initialization;

import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import dto.ApplicationStateDTO;

public class PhysicsSpaceInitializer {

	private AppStateManager stateManager;
	private CollisionListener listener;

	public PhysicsSpaceInitializer(AppStateManager stateManager,
			ApplicationStateDTO applicationStateDTO) {
		this.stateManager = stateManager;
		listener = new CollisionListener(applicationStateDTO);
	}

	public void initialize() {
		BulletAppState state = new BulletAppState();
		state.setDebugEnabled(true);
		stateManager.attach(state);
		state.getPhysicsSpace()
			 .addCollisionListener(listener);
	}

}
