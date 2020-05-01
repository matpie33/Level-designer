package controllers;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.scene.Node;
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
			CharacterControl characterControl = selectedNode.getControl(
					CharacterControl.class);
			control.getPhysicsSpace()
				   .remove(control);
			characterControl.getPhysicsSpace()
				   .remove(characterControl);
			selectedNode.removeControl(control);
			selectedNode.removeControl(characterControl);
			selectedNode.removeFromParent();
		}
		applicationStateDTO.clearSelectedModels();
		applicationStateDTO.setDeleteRequested(false);
	}

	@Override
	public void setUp() {

	}
}
