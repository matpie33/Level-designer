import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.material.MatParam;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

import java.util.Arrays;

public class CameraController {

	private Camera camera;
	private Node rootNode;

	private static final ColorRGBA COLOR_OF_HOVERED_MODEL = ColorRGBA.Red;
	private static final String AMBIENT_COLOR = "Ambient";
	private static final String COLOR = "Color";

	private Geometry currentlySelectedModel;
	private ColorRGBA previousColorOfSelectedModel;

	public CameraController(Camera camera, Node rootNode) {
		this.camera = camera;
		this.rootNode = rootNode;
	}

	public void update() {
		Ray ray = new Ray(camera.getLocation(), camera.getDirection());
		CollisionResults collisionResults = new CollisionResults();
		rootNode.collideWith(ray, collisionResults);
		if (collisionResults.size() > 0) {
			CollisionResult closestCollision = collisionResults.getClosestCollision();
			Geometry geometry = closestCollision.getGeometry();
			clearPreviouslySelectedModel();
			setColor(geometry, COLOR_OF_HOVERED_MODEL);
			currentlySelectedModel = geometry;
		}
		else {
			clearPreviouslySelectedModel();
		}
	}

	private void clearPreviouslySelectedModel() {
		if (currentlySelectedModel != null) {

			setColor(currentlySelectedModel, previousColorOfSelectedModel);
			currentlySelectedModel = null;
			previousColorOfSelectedModel = null;
		}
	}

	private void setColor(Geometry geometry, ColorRGBA color) {
		geometry.getMaterial()
				.getMaterialDef()
				.getMaterialParams()
				.stream()
				.filter(this::oneOfColorParams)
				.findFirst()
				.ifPresent(material -> {
					MatParam param = geometry.getMaterial()
											 .getParam(material.getName());
					previousColorOfSelectedModel =
							param != null ? (ColorRGBA) param.getValue() : null;
					if (color != null){
						geometry.getMaterial()
								.setColor(material.getName(), color);
					}
					else{
						geometry.getMaterial().clearParam(material.getName());
					}
				});
	}

	private boolean oneOfColorParams(MatParam materialParameter) {
		return Arrays.asList(COLOR, AMBIENT_COLOR)
					 .contains(materialParameter.getName());
	}

}
