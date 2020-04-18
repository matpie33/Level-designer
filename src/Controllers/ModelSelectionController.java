package Controllers;

import DTO.SelectionStateDTO;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.material.MatParam;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

import java.util.Arrays;
import java.util.Optional;

public class ModelSelectionController implements AbstractController {

	private Camera camera;
	private Node rootNode;
	private SelectionStateDTO selectionStateDTO;

	private static final ColorRGBA COLOR_OF_HOVERED_MODEL = ColorRGBA.Red;
	private static final ColorRGBA COLOR_OF_SELECTED_MODEL = ColorRGBA.Green;
	private static final String AMBIENT_COLOR = "Ambient";
	private static final String COLOR = "Color";

	public ModelSelectionController(Camera camera, Node rootNode,
			SelectionStateDTO selectionStateDTO) {
		this.camera = camera;
		this.rootNode = rootNode;
		this.selectionStateDTO = selectionStateDTO;
	}

	@Override
	public void setUp() {

	}

	@Override
	public void update() {
		Ray ray = new Ray(camera.getLocation(), camera.getDirection());
		CollisionResults collisionResults = new CollisionResults();
		rootNode.collideWith(ray, collisionResults);
		if (collisionResults.size() > 0) {
			CollisionResult closestCollision = collisionResults.getClosestCollision();
			Geometry geometry = closestCollision.getGeometry();
			if (geometry.equals(
					selectionStateDTO.getCurrentlySelectedModel())) {
				return;
			}
			clearPreviouslyHoveredModel();
			selectionStateDTO.setPreviousColorOfHoveredModel(
					setColor(geometry, COLOR_OF_HOVERED_MODEL));
			selectionStateDTO.setCurrentlyHoveredModel(geometry);
		}
		else {

			clearPreviouslyHoveredModel();

		}
	}

	private void clearPreviouslyHoveredModel() {
		if (selectionStateDTO.getCurrentlyHoveredModel() != null
				&& !selectionStateDTO.getCurrentlyHoveredModel()
									 .equals(selectionStateDTO.getCurrentlySelectedModel())) {

			setColor(selectionStateDTO.getCurrentlyHoveredModel(),
					selectionStateDTO.getPreviousColorOfHoveredModel());
			selectionStateDTO.clearHoveredModel();
		}
	}

	private ColorRGBA setColor(Geometry geometry, ColorRGBA color) {
		ColorRGBA previousColorOfHoveredModel = null;
		Optional<MatParam> optionalMaterial = geometry.getMaterial()
													  .getMaterialDef()
													  .getMaterialParams()
													  .stream()
													  .filter(this::oneOfColorParams)
													  .findFirst();
		if (optionalMaterial.isPresent()) {
			MatParam material = optionalMaterial.get();
			MatParam param = geometry.getMaterial()
									 .getParam(material.getName());
			previousColorOfHoveredModel =
					param != null ? (ColorRGBA) param.getValue() : null;
			if (color != null) {
				geometry.getMaterial()
						.setColor(material.getName(), color);
			}
			else {
				geometry.getMaterial()
						.clearParam(material.getName());
			}
		}
		return previousColorOfHoveredModel;
	}

	private boolean oneOfColorParams(MatParam materialParameter) {
		return Arrays.asList(COLOR, AMBIENT_COLOR)
					 .contains(materialParameter.getName());
	}

	public void selectCurrentSpatial() {
		if (selectionStateDTO.getCurrentlySelectedModel() != null){
			unselectCurrentSpatial();
		}
		if (selectionStateDTO.getCurrentlyHoveredModel() != null) {

			markCurrentModelAsSelected();
		}
	}

	private void markCurrentModelAsSelected() {
		selectionStateDTO.setCurrentlySelectedModel(
				selectionStateDTO.getCurrentlyHoveredModel());
		setColor(selectionStateDTO.getCurrentlyHoveredModel(),
				COLOR_OF_SELECTED_MODEL);
		selectionStateDTO.setPreviousColorOfSelectedModel(
				selectionStateDTO.getPreviousColorOfHoveredModel());
	}

	public void unselectCurrentSpatial() {
		if (selectionStateDTO.getCurrentlySelectedModel() == null) {
			return;
		}
		setColor(selectionStateDTO.getCurrentlySelectedModel(),
				selectionStateDTO.getPreviousColorOfSelectedModel());
		selectionStateDTO.clearSelectedModel();
	}
}
