package dto;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;

public class ApplicationStateDTO {
	private Geometry currentlyHoveredModel;
	private ColorRGBA previousColorOfHoveredModel;

	private Geometry currentlySelectedModel;
	private ColorRGBA previousColorOfSelectedModel;
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

	public Geometry getCurrentlyHoveredModel() {
		return currentlyHoveredModel;
	}

	public void setCurrentlyHoveredModel(Geometry currentlyHoveredModel) {
		this.currentlyHoveredModel = currentlyHoveredModel;
	}

	public ColorRGBA getPreviousColorOfHoveredModel() {
		return previousColorOfHoveredModel;
	}

	public void setPreviousColorOfHoveredModel(
			ColorRGBA previousColorOfHoveredModel) {
		this.previousColorOfHoveredModel = previousColorOfHoveredModel;
	}

	public Geometry getCurrentlySelectedModel() {
		return currentlySelectedModel;
	}

	public void setCurrentlySelectedModel(Geometry currentlySelectedModel) {
		this.currentlySelectedModel = currentlySelectedModel;
	}

	public ColorRGBA getPreviousColorOfSelectedModel() {
		return previousColorOfSelectedModel;
	}

	public void setPreviousColorOfSelectedModel(
			ColorRGBA previousColorOfSelectedModel) {
		this.previousColorOfSelectedModel = previousColorOfSelectedModel;
	}

	public void clearSelectedModel() {
		currentlySelectedModel = null;
		previousColorOfSelectedModel = null;
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
}
