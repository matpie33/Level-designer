package controllers;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.material.MatParam;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import dto.ApplicationStateDTO;
import dto.GeometryDTO;

import java.util.Arrays;
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
			Geometry geometry = closestCollision.getGeometry();
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

	private boolean isModelSelected(Geometry geometry) {
		return applicationStateDTO.getSelectedModels()
								  .stream()
								  .map(GeometryDTO::getGeometry)
								  .anyMatch(selectedGeometry -> selectedGeometry.equals(geometry));
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
						   .forEach(model -> setColor(model.getGeometry(),
								   model.getColorRGBA()));
	}

	public void returnCurrentlySelectedModelToSelectionMarkerColor() {
		applicationStateDTO.getSelectedModels()
						   .forEach(model -> setColor(model.getGeometry(),
								   COLOR_OF_SELECTED_MODEL));
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
