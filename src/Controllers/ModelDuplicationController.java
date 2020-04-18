package Controllers;

import DTO.SelectionStateDTO;
import com.jme3.scene.Node;

public class ModelDuplicationController implements AbstractController {

	private SelectionStateDTO selectionStateDTO;
	private Node rootNode;
	private ModelSelectionController modelSelectionController;
	private static final int OFFSET = 5;
	private int coordinateOffsetFromDuplicatedModel = OFFSET;
	private Node previousDuplicatedModel;

	public ModelDuplicationController(SelectionStateDTO selectionStateDTO,
			Node rootNode, ModelSelectionController modelSelectionController) {
		this.selectionStateDTO = selectionStateDTO;
		this.rootNode = rootNode;
		this.modelSelectionController = modelSelectionController;
	}

	@Override
	public void update() {
		if (selectionStateDTO.isDuplicateModel()) {

			modelSelectionController.returnCurrentlySelectedModelToPreviousColor();
			selectionStateDTO.setDuplicateModel(false);
			Node parent = selectionStateDTO.getCurrentlySelectedModel()
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
