package controllers;

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
			applicationStateDTO.setDeleteRequested(false);
			selectedModel.getGeometry()
						 .getParent()
						 .removeFromParent();
		}
	}

	@Override
	public void setUp() {

	}
}
