package Controllers;

import DTO.ApplicationStateDTO;
import com.jme3.scene.Geometry;

public class ModelDeleteController implements AbstractController {

	private ApplicationStateDTO applicationStateDTO;

	public ModelDeleteController(ApplicationStateDTO applicationStateDTO) {
		this.applicationStateDTO = applicationStateDTO;
	}

	@Override
	public void update() {
		if (applicationStateDTO.getCurrentlySelectedModel() != null
				&& applicationStateDTO.isDeleteRequested()) {
			applicationStateDTO.setDeleteRequested(false);
			Geometry selectedModel = applicationStateDTO.getCurrentlySelectedModel();
			selectedModel.getParent()
						 .removeFromParent();
		}
	}

	@Override
	public void setUp() {

	}
}
