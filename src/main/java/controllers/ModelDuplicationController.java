package controllers;

import com.jme3.bullet.control.GhostControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import dto.ApplicationStateDTO;
import dto.GeometryDTO;
import initialization.ModelsLoader;
import initialization.SpatialsControlsInitializer;

public class ModelDuplicationController implements AbstractController {

	private ApplicationStateDTO applicationStateDTO;
	private Node rootNode;
	private ModelSelectionController modelSelectionController;
	private SpatialsControlsInitializer spatialsControlsInitializer;
	private ModelsLoader modelsLoader;
	private DuplicateObjectPositionController duplicateObjectPositionController;

	public ModelDuplicationController(ApplicationStateDTO applicationStateDTO,
			Node rootNode, ModelSelectionController modelSelectionController,
			SpatialsControlsInitializer spatialsControlsInitializer,
			ModelsLoader modelsLoader, Camera camera) {
		this.applicationStateDTO = applicationStateDTO;
		this.rootNode = rootNode;
		this.modelSelectionController = modelSelectionController;
		this.spatialsControlsInitializer = spatialsControlsInitializer;
		this.modelsLoader = modelsLoader;
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
			for (GeometryDTO selectedModel : applicationStateDTO.getSelectedModels()) {
				Node parent = selectedModel.getGeometry()
										   .getParent();
				Node clone = copyModel(parent);
				Vector3f whereToPlaceClone = duplicateObjectPositionController.findWhereToPlaceClone(
						parent);
				clone.setLocalTranslation(whereToPlaceClone);
				clone.getControl(GhostControl.class)
					 .setPhysicsLocation(whereToPlaceClone);
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
