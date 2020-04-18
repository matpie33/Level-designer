import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class SaveController {

	private SelectionStateDTO selectionStateDTO;
	private Node rootNode;

	public SaveController(SelectionStateDTO selectionStateDTO, Node rootNode) {
		this.selectionStateDTO = selectionStateDTO;
		this.rootNode = rootNode;
	}

	public void update() {
		if (selectionStateDTO.isSaveRequested()) {
			selectionStateDTO.setSaveRequested(false);
			for (Spatial child : rootNode.getChildren()) {
				System.out.println("child: " + child.getName() + " coord: "
						+ child.getWorldTranslation() + " rotation: "
						+ child.getWorldRotation());
			}
		}
	}

}