package controllers;

import com.jme3.bullet.control.GhostControl;
import com.jme3.scene.Node;
import controls.CollisionPreventControl;
import dto.ApplicationStateDTO;
import dto.GeometryDTO;

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
		for (GeometryDTO selectedModel : applicationStateDTO.getSelectedModels()) {
			Node parent = selectedModel.getGeometry()
									   .getParent();
			GhostControl control = parent.getControl(GhostControl.class);
			CollisionPreventControl collisionPreventControl = parent.getControl(
					CollisionPreventControl.class);
			control.getPhysicsSpace()
				   .removeTickListener(collisionPreventControl);
			control.getPhysicsSpace()
				   .remove(control);
			parent.removeControl(CollisionPreventControl.class);
			parent.removeControl(GhostControl.class);
			parent.removeFromParent();
		}
		applicationStateDTO.clearSelectedModels();
		applicationStateDTO.setDeleteRequested(false);
	}

	@Override
	public void setUp() {

	}
}
