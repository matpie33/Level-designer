package dto;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class NodeDTO {

	private Node node;
	private ColorRGBA colorRGBA;
	private boolean isColliding;
	private Spatial collidingSpatial;
	private float distanceToMoveOutOfCollision;
	private Vector3f colisionPointLocalOnThisObject;

	public float getDistanceToMoveOutOfCollision() {
		return distanceToMoveOutOfCollision;
	}

	public void setDistanceToMoveOutOfCollision(float distanceToMoveOutOfCollision) {
		this.distanceToMoveOutOfCollision = distanceToMoveOutOfCollision;
	}

	public Vector3f getColisionPointLocalOnThisObject() {
		return colisionPointLocalOnThisObject;
	}

	public void setColisionPointLocalOnThisObject(Vector3f colisionPointLocalOnThisObject) {
		this.colisionPointLocalOnThisObject = colisionPointLocalOnThisObject;
	}

	public Spatial getCollidingSpatial() {
		return collidingSpatial;
	}

	public void setCollidingSpatial(Spatial collidingSpatial) {
		this.collidingSpatial = collidingSpatial;
	}

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
