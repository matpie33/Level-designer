package controllers;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import dto.ApplicationStateDTO;
import dto.NodeDTO;
import initialization.ModelToSceneAdder;
import initialization.ModelsLoadAppState;

public class ModelDuplicationController implements AbstractController {

	private ApplicationStateDTO applicationStateDTO;
	private Node rootNode;
	private ModelSelectionController modelSelectionController;
	private ModelsLoadAppState modelsLoadAppState;
	private DuplicateObjectPositionController duplicateObjectPositionController;
	private ModelToSceneAdder modelToSceneAdder;

	public ModelDuplicationController(ApplicationStateDTO applicationStateDTO,
			Node rootNode, ModelSelectionController modelSelectionController,
			ModelToSceneAdder modelToSceneAdder,
			ModelsLoadAppState modelsLoadAppState, Camera camera) {
		this.applicationStateDTO = applicationStateDTO;
		this.rootNode = rootNode;
		this.modelSelectionController = modelSelectionController;
		this.modelToSceneAdder = modelToSceneAdder;
		this.modelsLoadAppState = modelsLoadAppState;
		duplicateObjectPositionController = new DuplicateObjectPositionController(
				camera, rootNode);
	}

	@Override
	public void update() {
		if (applicationStateDTO.isDuplicateModelRequested()
				&& !applicationStateDTO.getSelectedModels()
									   .isEmpty()) {
			modelSelectionController.returnCurrentlySelectedModelToPreviousColor();
			applicationStateDTO.setDuplicateModelRequested(false);
			for (NodeDTO selectedModel : applicationStateDTO.getSelectedModels()) {
				Node selectedNode = selectedModel.getNode();
				Node clone = copyModel(selectedNode);
				Vector3f whereToPlaceClone = duplicateObjectPositionController.findWhereToPlaceClone(
						selectedNode);
				clone.setLocalTranslation(whereToPlaceClone);
				CharacterControl control = clone.getControl(
						CharacterControl.class);
				if (control != null) {
					control
						 .setPhysicsLocation(whereToPlaceClone);
				}
				rootNode.attachChild(clone);
			}

			modelSelectionController.returnCurrentlySelectedModelToSelectionMarkerColor();
		}
	}

	private Node copyModel(Node parent) {
		Spatial spatial = modelsLoadAppState.loadModel(parent.getKey()
															 .getName());
		modelToSceneAdder.addControls(spatial);
		return (Node) spatial;
	}

	@Override
	public void setUp() {

	}
}
