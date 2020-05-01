package initialization;

import com.jme3.app.state.AppStateManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import controls.ObjectMovementControl;
import dto.ApplicationStateDTO;
import dto.SpatialDTO;

import java.util.List;

public class ModelToSceneAdder {

	private ModelsLoader modelsLoader;
	private AppStateManager appStateManager;
	private Node rootNode;
	private Camera camera;
	private ApplicationStateDTO applicationStateDTO;

	public ModelToSceneAdder(ModelsLoader modelsLoader,
			AppStateManager appStateManager, Node rootNode, Camera camera,
			ApplicationStateDTO applicationStateDTO) {
		this.modelsLoader = modelsLoader;
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
		control.setPhysicsLocation(spatialData.getPosition());
	}

	public void addControls(Spatial spatial) {
		CollisionShape shape = new BoxCollisionShape(
				((BoundingBox) spatial.getWorldBound()).getExtent(
						new Vector3f()));
		String name = spatial.getKey()
							 .getName();
		if (name.contains("map") || name.contains("house")) {
			shape = CollisionShapeFactory.createMeshShape(spatial);
		}
		ObjectMovementControl objectMovementControl = new ObjectMovementControl(
				applicationStateDTO, camera);
		spatial.addControl(objectMovementControl);
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

	private Spatial loadModel(SpatialDTO spatialData) {
		String pathToModel = spatialData.getPathToModel();
		return modelsLoader.loadModel(pathToModel);
	}

}
