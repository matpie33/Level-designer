package dto;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;

import java.util.ArrayList;
import java.util.List;

public class ApplicationStateDTO {

	private boolean multiselectionEnabled;
	private Node currentlyHoveredModel;
	private ColorRGBA previousColorOfHoveredModel;

	private List<NodeDTO> selectedModels = new ArrayList<>();
	private boolean movingForward;
	private boolean movingBackward;
	private boolean movingLeft;
	private boolean movingRight;
	private boolean movingUp;
	private boolean movingDown;
	private boolean exitRequested;
	private Boolean exitConfirmed;
	private boolean saveRequested;
	private boolean duplicateModelRequested;
	private boolean deleteRequested;
	private boolean loadModelRequested;
	private boolean isCollisionDetected;
	private boolean isRotationRequested;
	private boolean isAccelerationRequested;
	private boolean isDecelerationRequested;

	public boolean isDecelerationRequested() {
		return isDecelerationRequested;
	}

	public void setDecelerationRequested(boolean decelerationRequested) {
		isDecelerationRequested = decelerationRequested;
	}

	public boolean isAccelerationRequested() {
		return isAccelerationRequested;
	}

	public void setAccelerationRequested(boolean accelerationRequested) {
		isAccelerationRequested = accelerationRequested;
	}

	public boolean isRotationRequested() {
		return isRotationRequested;
	}

	public void setRotationRequested(boolean rotationRequested) {
		isRotationRequested = rotationRequested;
	}

	public boolean isCollisionDetected() {
		return isCollisionDetected;
	}

	public void setCollisionDetected(boolean collisionDetected) {
		isCollisionDetected = collisionDetected;
	}

	public boolean isMultiselectionEnabled() {
		return multiselectionEnabled;
	}

	public void setMultiselectionEnabled(boolean multiselectionEnabled) {
		this.multiselectionEnabled = multiselectionEnabled;
	}

	public List<NodeDTO> getSelectedModels() {
		return selectedModels;
	}


	public boolean isLoadModelRequested() {
		return loadModelRequested;
	}

	public void setLoadModelRequested(boolean loadModelRequested) {
		this.loadModelRequested = loadModelRequested;
	}

	public boolean isDeleteRequested() {
		return deleteRequested;
	}

	public void setDeleteRequested(boolean deleteRequested) {
		this.deleteRequested = deleteRequested;
	}

	public boolean isMovingUp() {
		return movingUp;
	}

	public void setMovingUp(boolean movingUp) {
		this.movingUp = movingUp;
	}

	public boolean isMovingDown() {
		return movingDown;
	}

	public void setMovingDown(boolean movingDown) {
		this.movingDown = movingDown;
	}

	public boolean isMovingBackward() {
		return movingBackward;
	}

	public void setMovingBackward(boolean movingBackward) {
		this.movingBackward = movingBackward;
	}

	public boolean isMovingLeft() {
		return movingLeft;
	}

	public void setMovingLeft(boolean movingLeft) {
		this.movingLeft = movingLeft;
	}

	public boolean isMovingRight() {
		return movingRight;
	}

	public void setMovingRight(boolean movingRight) {
		this.movingRight = movingRight;
	}

	public Node getCurrentlyHoveredModel() {
		return currentlyHoveredModel;
	}

	public void setCurrentlyHoveredModel(Node currentlyHoveredModel) {
		this.currentlyHoveredModel = currentlyHoveredModel;
	}

	public ColorRGBA getPreviousColorOfHoveredModel() {
		return previousColorOfHoveredModel;
	}

	public void setPreviousColorOfHoveredModel(
			ColorRGBA previousColorOfHoveredModel) {
		this.previousColorOfHoveredModel = previousColorOfHoveredModel;
	}


	public void clearSelectedModels() {
		selectedModels.clear();
	}

	public void clearHoveredModel() {
		currentlyHoveredModel = null;
		previousColorOfHoveredModel = null;
	}

	public void setMovingForward(boolean movingForward) {
		this.movingForward = movingForward;
	}

	public boolean isMovingForward() {
		return movingForward;
	}

	public void setExitRequested(boolean isPressed) {
		exitRequested = isPressed;
	}

	public boolean isExitRequested() {
		return exitRequested;
	}

	public Boolean isExitConfirmed() {
		return exitConfirmed;
	}

	public void setExitConfirmed(Boolean exitConfirmed) {
		this.exitConfirmed = exitConfirmed;
	}

	public boolean isSaveRequested() {
		return saveRequested;
	}

	public void setSaveRequested(boolean saveRequested) {
		this.saveRequested = saveRequested;
	}

	public boolean isDuplicateModelRequested() {
		return duplicateModelRequested;
	}

	public void setDuplicateModelRequested(boolean duplicateModelRequested) {
		this.duplicateModelRequested = duplicateModelRequested;
	}

	public void selectModel(Node node,
			ColorRGBA color) {
		NodeDTO nodeDTO = new NodeDTO();
		nodeDTO.setNode(node);
		nodeDTO.setColor(color);
		selectedModels.add(nodeDTO);
	}
}
