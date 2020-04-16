import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;

public class SelectionStateDTO {
	private Geometry currentlyHoveredModel;
	private ColorRGBA previousColorOfHoveredModel;

	private Geometry currentlySelectedModel;
	private ColorRGBA previousColorOfSelectedModel;

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
}
