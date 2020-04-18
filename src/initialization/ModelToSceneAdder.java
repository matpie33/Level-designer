package initialization;

import DTO.SpatialDTO;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.util.ArrayList;
import java.util.List;

public class ModelToSceneAdder {

	private Node rootNode;
	private Vector3f currentObjectCoordinate = new Vector3f(0, -10, -20);
	private List<SpatialDTO> spatialData = new ArrayList<>();

	public ModelToSceneAdder(Node rootNode) {
		this.rootNode = rootNode;
	}

	public void addSpatials(List<Spatial> spatials) {
		spatials.forEach(spatial -> {
			spatial.setLocalTranslation(currentObjectCoordinate);
			if (!spatialData.isEmpty()) {
				SpatialDTO spatialData = findSpatialData(spatial.getKey().getName());
				spatial.setLocalTranslation(spatialData.getPosition());
				spatial.setLocalRotation(spatialData.getRotation());
			}
			else{
				currentObjectCoordinate.setX(currentObjectCoordinate.getX() + 10);
			}
			rootNode.attachChild(spatial);
		});
	}

	private SpatialDTO findSpatialData(String fileName) {
		return spatialData.stream()
						  .filter(spatial -> spatial.getPathToModel()
													.equals(fileName))
						  .findFirst()
						  .orElseThrow(() -> throwException(fileName));
	}

	private IllegalArgumentException throwException(String fileName) {
		return new IllegalArgumentException("Spatial data not found for "
				+ "spatial with path: " + fileName);
	}

	public void addSpatialData(List<SpatialDTO> spatialDTOS) {
		spatialData = spatialDTOS;
	}
}
