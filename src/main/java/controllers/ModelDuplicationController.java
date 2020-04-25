package controllers;

import com.jme3.scene.Node;
import dto.ApplicationStateDTO;
import dto.GeometryDTO;
import initialization.SpatialsControlsInitializer;

import java.util.ArrayList;
import java.util.List;

public class ModelDuplicationController implements AbstractController {

	private ApplicationStateDTO applicationStateDTO;
	private Node rootNode;
	private ModelSelectionController modelSelectionController;
	private static final int OFFSET = 5;
	private int coordinateOffsetFromDuplicatedModel = OFFSET;
	private List<GeometryDTO> previousDuplicatedModels = new ArrayList<>();
	private SpatialsControlsInitializer spatialsControlsInitializer;

	public ModelDuplicationController(ApplicationStateDTO applicationStateDTO,
			Node rootNode, ModelSelectionController modelSelectionController,
			SpatialsControlsInitializer spatialsControlsInitializer) {
		this.applicationStateDTO = applicationStateDTO;
		this.rootNode = rootNode;
		this.modelSelectionController = modelSelectionController;
		this.spatialsControlsInitializer = spatialsControlsInitializer;
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
			previousDuplicatedModels.addAll(
					applicationStateDTO.getSelectedModels());
			for (GeometryDTO selectedModel : applicationStateDTO.getSelectedModels()) {
				Node parent = selectedModel.getGeometry()
										   .getParent();
				Node clone = parent.clone(true);
				clone.setLocalTranslation(clone.getLocalTranslation()
											   .setX(clone.getLocalTranslation()
														  .getX()
													   + coordinateOffsetFromDuplicatedModel));
				int numControls = clone.getNumControls();
				for (int i = 0; i < numControls; i++) {
					clone.removeControl(clone.getControl(0));
				}
				rootNode.attachChild(clone);
				spatialsControlsInitializer.attachControl(clone);
			}

			modelSelectionController.returnCurrentlySelectedModelToSelectionMarkerColor();
		}
	}

	@Override
	public void setUp() {

	}
}
