package dto;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;

public class NodeDTO {

	private Node node;
	private ColorRGBA colorRGBA;
	private boolean isColliding;

	public boolean isColliding() {
		return isColliding;
	}

	public void setColliding(boolean colliding) {
		isColliding = colliding;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public ColorRGBA getColorRGBA() {
		return colorRGBA;
	}

	public void setColor(ColorRGBA colorRGBA) {
		this.colorRGBA = colorRGBA;
	}
}