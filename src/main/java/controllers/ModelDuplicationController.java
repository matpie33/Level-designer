package controllers;

import com.jme3.scene.Node;
import dto.ApplicationStateDTO;
import dto.GeometryDTO;

import java.util.ArrayList;
import java.util.List;

public class ModelDuplicationController implements AbstractController {

	private ApplicationStateDTO applicationStateDTO;
	private Node rootNode;
	private ModelSelectionController modelSelectionController;
	private static final int OFFSET = 5;
	private int coordinateOffsetFromDuplicatedModel = OFFSET;
	private List<GeometryDTO> previousDuplicatedModels = new ArrayList<>();

	public ModelDuplicationController(ApplicationStateDTO applicationStateDTO,
			Node rootNode, ModelSelectionController modelSelectionController) {
		this.applicationStateDTO = applicationStateDTO;
		this.rootNode = rootNode;
		this.modelSelectionController = modelSelectionController;
	}

	@Override
	public void update() {
		if (applicationStateDTO.isDuplicateModelRequested()
				&& !applicationStateDTO.getSelectedModels()
									   .isEmpty()) {
			modelSelectionController.returnCurrentlySelectedModelToPreviousColor();
			applicationStateDTO.setDuplicateModelRequested(false);
			if (previousDuplicatedModels.equals(
					applicationStateDTO.getSelectedModels())) {
				coordinateOffsetFromDuplicatedModel += OFFSET;
			}
			else {
				coordinateOffsetFromDuplicatedModel = OFFSET;
			}
			previousDuplicatedModels.clear();
			previousDuplicatedModels.addAll(applicationStateDTO.getSelectedModels());
			for (GeometryDTO selectedModel : applicationStateDTO.getSelectedModels()) {
				Node parent = selectedModel.getGeometry()
										   .getParent();
				Node clone = parent.clone(true);
				clone.setLocalTranslation(clone.getLocalTranslation()
											   .setX(clone.getLocalTranslation()
														  .getX()
													   + coordinateOffsetFromDuplicatedModel));
				rootNode.attachChild(clone);
			}

			modelSelectionController.returnCurrentlySelectedModelToSelectionMarkerColor();
		}
	}

	@Override
	public void setUp() {

	}
}
