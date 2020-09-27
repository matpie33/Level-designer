package initialization;

import com.jme3.app.state.AppStateManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import controls.ObjectMovementControl;
import dto.ApplicationStateDTO;
import dto.SpatialDTO;

import java.util.List;

public class ModelToSceneAdder {

	private ModelsLoadAppState modelsLoadAppState;
	private AppStateManager appStateManager;
	private Node rootNode;
	private Camera camera;
	private ApplicationStateDTO applicationStateDTO;

	public ModelToSceneAdder(ModelsLoadAppState modelsLoadAppState,
			AppStateManager appStateManager, Node rootNode, Camera camera,
			ApplicationStateDTO applicationStateDTO) {
		this.modelsLoadAppState = modelsLoadAppState;
		this.appStateManager = appStateManager;
		this.rootNode = rootNode;
		this.camera = camera;
		this.applicationStateDTO = applicationStateDTO;
	}

	public void addModels(List<SpatialDTO> spatials) {
		spatials.forEach(spatialData -> {
			Spatial spatial = loadModel(spatialData);
			addControls(spatial);
			setRotationAndLocation(spatialData, spatial);
			rootNode.attachChild(spatial);
		});
	}

	private void setRotationAndLocation(SpatialDTO spatialData,
			Spatial spatial) {
		spatial.setLocalRotation(spatialData.getRotation());
		spatial.setLocalTranslation(spatialData.getPosition());
		CharacterControl control = spatial.getControl(CharacterControl.class);
		if (control != null) {

			control.setPhysicsLocation(spatialData.getPosition());
			control.setViewDirection(spatialData.getRotation()
												.mult(new Vector3f(0, 0, 1)));
		}

	}

	public void addControls(Spatial spatial) {

		spatial.setName(spatial.getName() + " parent");
		String name = spatial.getKey()
							 .getName();
		if (!name.contains("map") && !name.contains("house")) {
			CollisionShape shape = new BoxCollisionShape(
					((BoundingBox) spatial.getWorldBound()).getExtent(
							new Vector3f()));
			GhostControl ghostControl = new GhostControl(shape);
			spatial.addControl(ghostControl);
			CharacterControl control = new CharacterControl(shape, 0.1f);
			spatial.addControl(control);
			control.setGravity(Vector3f.ZERO);
			BulletAppState state = appStateManager.getState(BulletAppState.class);
			state.getPhysicsSpace()
				 .add(control);
			state.getPhysicsSpace()
				 .add(ghostControl);
		}
		ObjectMovementControl objectMovementControl = new ObjectMovementControl(
				applicationStateDTO, camera);
		spatial.addControl(objectMovementControl);
	}

	private Spatial loadModel(SpatialDTO spatialData) {
		String pathToModel = spatialData.getPathToModel();
		return modelsLoadAppState.loadModel(pathToModel);
	}

}
