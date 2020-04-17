import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.util.List;

public class ModelToSceneAdder {

	private Node rootNode;
	private Vector3f firstObjectCoordinate = new Vector3f(0, -10, -20);

	public ModelToSceneAdder(Node rootNode) {
		this.rootNode = rootNode;
	}

	public void addSpatials(List<Spatial> spatials) {
		spatials.forEach(spatial -> {
			spatial.setLocalTranslation(firstObjectCoordinate);
			firstObjectCoordinate.setX(firstObjectCoordinate.getX() + 10);
			rootNode.attachChild(spatial);
		});
	}

}
