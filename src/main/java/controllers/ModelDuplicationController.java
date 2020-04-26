package controllers;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import dto.ApplicationStateDTO;
import dto.GeometryDTO;
import initialization.ModelsLoader;
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
	private ModelsLoader modelsLoader;

	public ModelDuplicationController(ApplicationStateDTO applicationStateDTO,
			Node rootNode, ModelSelectionController modelSelectionController,
			SpatialsControlsInitializer spatialsControlsInitializer,
			ModelsLoader modelsLoader) {
		this.applicationStateDTO = applicationStateDTO;
		this.rootNode = rootNode;
		this.modelSelectionController = modelSelectionController;
		this.spatialsControlsInitializer = spatialsControlsInitializer;
		this.modelsLoader = modelsLoader;
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
				Node clone = copyModel(parent);
				clone.setLocalTranslation(clone.getLocalTranslation()
											   .setX(clone.getLocalTranslation()
														  .getZ()
													   + coordinateOffsetFromDuplicatedModel));
				rootNode.attachChild(clone);
			}

			modelSelectionController.returnCurrentlySelectedModelToSelectionMarkerColor();
		}
	}

	private Node copyModel(Node parent) {
		Spatial spatial = modelsLoader.loadModel(parent.getKey()
													   .getName());
		spatialsControlsInitializer.attachControl(spatial);
		return (Node) spatial;
	}

	@Override
	public void setUp() {

	}
}
