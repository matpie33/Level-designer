package Controllers;

import DTO.ApplicationStateDTO;
import com.jme3.scene.Node;

public class ModelDuplicationController implements AbstractController {

	private ApplicationStateDTO applicationStateDTO;
	private Node rootNode;
	private ModelSelectionController modelSelectionController;
	private static final int OFFSET = 5;
	private int coordinateOffsetFromDuplicatedModel = OFFSET;
	private Node previousDuplicatedModel;

	public ModelDuplicationController(ApplicationStateDTO applicationStateDTO,
			Node rootNode, ModelSelectionController modelSelectionController) {
		this.applicationStateDTO = applicationStateDTO;
		this.rootNode = rootNode;
		this.modelSelectionController = modelSelectionController;
	}

	@Override
	public void update() {
		if (applicationStateDTO.isDuplicateModel()) {

			modelSelectionController.returnCurrentlySelectedModelToPreviousColor();
			applicationStateDTO.setDuplicateModel(false);
			Node parent = applicationStateDTO.getCurrentlySelectedModel()
											 .getParent();
			if (parent == previousDuplicatedModel){
				coordinateOffsetFromDuplicatedModel += OFFSET;
			}
			else{
				coordinateOffsetFromDuplicatedModel = OFFSET;
			}
			Node clone = parent.clone(true);
			clone.setLocalTranslation(clone.getLocalTranslation()
										   .setX(clone.getLocalTranslation()
													  .getX() + coordinateOffsetFromDuplicatedModel));
			rootNode.attachChild(clone);
			previousDuplicatedModel = parent;
			modelSelectionController.returnCurrentlySelectedModelToSelectionMarkerColor();
		}
	}

	@Override
	public void setUp() {

	}
}
