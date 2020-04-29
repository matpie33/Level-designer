package dto;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class SpatialDTO {

	private Vector3f position= Vector3f.ZERO;
	private Quaternion rotation = new Quaternion();
	private String pathToModel;
	private Spatial spatial;

	public Spatial getSpatial() {
		return spatial;
	}

	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Quaternion getRotation() {
		return rotation;
	}

	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
	}

	public String getPathToModel() {
		return pathToModel;
	}

	public void setPathToModel(String pathToModel) {
		this.pathToModel = pathToModel;
	}

	@Override
	public String toString() {
		return " path: " + pathToModel + " Pos: " + position + " rotation: "
				+ rotation;
	}
}
