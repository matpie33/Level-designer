package controllers;

import com.jme3.bullet.control.GhostControl;
import com.jme3.scene.Node;
import controls.CollisionPreventControl;
import dto.ApplicationStateDTO;
import dto.NodeDTO;

public class ModelDeleteController implements AbstractController {

	private ApplicationStateDTO applicationStateDTO;

	public ModelDeleteController(ApplicationStateDTO applicationStateDTO) {
		this.applicationStateDTO = applicationStateDTO;
	}

	@Override
	public void update() {
		if (!applicationStateDTO.isDeleteRequested()) {
			return;
		}
		for (NodeDTO selectedModel : applicationStateDTO.getSelectedModels()) {
			Node selectedNode = selectedModel.getNode();
			GhostControl control = selectedNode.getControl(GhostControl.class);
			CollisionPreventControl collisionPreventControl = selectedNode.getControl(
					CollisionPreventControl.class);
			control.getPhysicsSpace()
				   .removeTickListener(collisionPreventControl);
			control.getPhysicsSpace()
				   .remove(control);
			selectedNode.removeControl(CollisionPreventControl.class);
			selectedNode.removeControl(GhostControl.class);
			selectedNode.removeFromParent();
		}
		applicationStateDTO.clearSelectedModels();
		applicationStateDTO.setDeleteRequested(false);
	}

	@Override
	public void setUp() {

	}
}
