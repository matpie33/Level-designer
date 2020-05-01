package controllers;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.material.MatParam;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import dto.ApplicationStateDTO;
import dto.NodeDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ModelSelectionController implements AbstractController {

	private Camera camera;
	private Node rootNode;
	private ApplicationStateDTO applicationStateDTO;

	private static final ColorRGBA COLOR_OF_HOVERED_MODEL = ColorRGBA.Red;
	private static final ColorRGBA COLOR_OF_SELECTED_MODEL = ColorRGBA.Green;
	private static final String AMBIENT_COLOR = "Ambient";
	private static final String COLOR = "Color";

	public ModelSelectionController(Camera camera, Node rootNode,
			ApplicationStateDTO applicationStateDTO) {
		this.camera = camera;
		this.rootNode = rootNode;
		this.applicationStateDTO = applicationStateDTO;
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
			Node geometry = getNode(closestCollision);
			if (isModelSelected(geometry)) {
				return;
			}
			clearPreviouslyHoveredModel();
			applicationStateDTO.setPreviousColorOfHoveredModel(
					setColor(geometry, COLOR_OF_HOVERED_MODEL));
			applicationStateDTO.setCurrentlyHoveredModel(geometry);
		}
		else {

			clearPreviouslyHoveredModel();

		}
	}

	private Node getNode(CollisionResult closestCollision) {
		for (Node spatial = closestCollision.getGeometry()
											.getParent();
			 spatial != null; spatial = spatial.getParent()) {
			if (spatial.getControl(CharacterControl.class) != null) {
				return spatial;
			}
		}
		return null;
	}

	private boolean isModelSelected(Node node) {
		return applicationStateDTO.getSelectedModels()
								  .stream()
								  .map(NodeDTO::getNode)
								  .anyMatch(selectedNode -> selectedNode.equals(
										  node));
	}

	private void clearPreviouslyHoveredModel() {
		if (applicationStateDTO.getCurrentlyHoveredModel() != null
				&& !isModelSelected(
				applicationStateDTO.getCurrentlyHoveredModel())) {
			setColor(applicationStateDTO.getCurrentlyHoveredModel(),
					applicationStateDTO.getPreviousColorOfHoveredModel());
			applicationStateDTO.clearHoveredModel();
		}
	}

	public void returnCurrentlySelectedModelToPreviousColor() {
		applicationStateDTO.getSelectedModels()
						   .forEach(model -> setColor(model.getNode(),
								   model.getColorRGBA()));
	}

	public void returnCurrentlySelectedModelToSelectionMarkerColor() {
		applicationStateDTO.getSelectedModels()
						   .forEach(model -> setColor(model.getNode(),
								   COLOR_OF_SELECTED_MODEL));
	}

	private ColorRGBA setColor(Node node, ColorRGBA color) {
		ColorRGBA previousColorOfHoveredModel = null;
		List<Geometry> geometry = getGeometries(node);
		geometry.forEach(
				geo -> getColorRGBA(color, previousColorOfHoveredModel, geo));
		return ColorRGBA.Red;
	}

	private ColorRGBA getColorRGBA(ColorRGBA color,
			ColorRGBA previousColorOfHoveredModel, Geometry geometry) {
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

	private List<Geometry> getGeometries(Node node) {

		List<Geometry> geometries = new ArrayList<>();
		for (Spatial child : node.getChildren()) {
			if (child instanceof Geometry) {
				geometries.add((Geometry) child);
			}
			else {
				for (Spatial spatial : ((Node) child).getChildren()) {
					if (spatial instanceof Node) {
						geometries.addAll(getGeometries((Node) spatial));
					}
				}

			}
		}
		return geometries;

	}

	private boolean oneOfColorParams(MatParam materialParameter) {
		return Arrays.asList(COLOR, AMBIENT_COLOR)
					 .contains(materialParameter.getName());
	}

	public void selectCurrentSpatial() {
		if (!applicationStateDTO.isMultiselectionEnabled()) {
			if (!applicationStateDTO.getSelectedModels()
									.isEmpty()) {
				unselectCurrentSpatial();
			}
		}
		if (applicationStateDTO.getCurrentlyHoveredModel() != null) {
			markCurrentModelAsSelected();
		}

	}

	private void markCurrentModelAsSelected() {
		applicationStateDTO.selectModel(
				applicationStateDTO.getCurrentlyHoveredModel(),
				applicationStateDTO.getPreviousColorOfHoveredModel());
		setColor(applicationStateDTO.getCurrentlyHoveredModel(),
				COLOR_OF_SELECTED_MODEL);
	}

	public void unselectCurrentSpatial() {
		if (applicationStateDTO.getSelectedModels()
							   .isEmpty()) {
			return;
		}
		returnCurrentlySelectedModelToPreviousColor();
		applicationStateDTO.clearSelectedModels();
	}
}
