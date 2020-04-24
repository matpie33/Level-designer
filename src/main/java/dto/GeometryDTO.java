package dto;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;

public class GeometryDTO {

	private Geometry geometry;
	private ColorRGBA colorRGBA;
	private boolean isColliding;

	public boolean isColliding() {
		return isColliding;
	}

	public void setColliding(boolean colliding) {
		isColliding = colliding;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public ColorRGBA getColorRGBA() {
		return colorRGBA;
	}

	public void setColor(ColorRGBA colorRGBA) {
		this.colorRGBA = colorRGBA;
	}
}
