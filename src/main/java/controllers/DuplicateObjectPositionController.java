package controllers;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

import java.util.ArrayList;

public class DuplicateObjectPositionController {

	private Camera camera;
	private Node rootNode;

	public DuplicateObjectPositionController(Camera camera, Node rootNode) {
		this.camera = camera;
		this.rootNode = rootNode;
	}

	public Vector3f findWhereToPlaceClone(Node clonedSpatial) {
		BoundingBox clonedSpatialSize = (BoundingBox) clonedSpatial.getWorldBound();
		Vector3f position = clonedSpatial.getLocalTranslation()
										 .clone();
		ArrayList<Vector3f> vectors = getPointsToCheckCollisionsWith(
				clonedSpatialSize, position);
		CollisionResult closestCollision = new CollisionResult(null,
				Float.MAX_VALUE);
		Vector3f rightDirection = camera.getLeft()
										.negate();
		rightDirection = new Vector3f(Math.round(rightDirection.getX()),
				Math.round(rightDirection.getY()),
				Math.round(rightDirection.getZ()));
		for (Vector3f vector : vectors) {
			CollisionResult farthestCollision = collide(vector, rightDirection);
			if (farthestCollision != null) {
				if (!farthestCollision.getGeometry()
									  .getParent()
									  .equals(clonedSpatial)
						&& farthestCollision.getDistance()
						< closestCollision.getDistance()) {
					closestCollision = farthestCollision;
				}
			}
		}
		return calculateNewPosition(clonedSpatial, position, closestCollision,
				rightDirection);

	}

	private Vector3f calculateNewPosition(Node clonedSpatial, Vector3f position,
			CollisionResult closestCollision, Vector3f rightDirection) {
		CollisionShape collisionShape = clonedSpatial.getControl(
				GhostControl.class)
													 .getCollisionShape();
		float radius;
		if (collisionShape instanceof SphereCollisionShape) {
			radius = ((SphereCollisionShape) collisionShape).getRadius() * 2;
		}
		else if (collisionShape instanceof CapsuleCollisionShape) {

			radius = ((CapsuleCollisionShape) collisionShape).getRadius() * 2;
		}
		else if (collisionShape instanceof BoxCollisionShape) {
			boolean movesToX = rightDirection.getX() != 0;
			Vector3f halfExtents = ((BoxCollisionShape) collisionShape).getHalfExtents();
			radius = movesToX ? halfExtents.getX() : halfExtents.getZ();
			radius *= 2;
		}
		else {
			throw new UnsupportedOperationException(
					"Unsupported shape: " + collisionShape.getClass());
		}
		if (closestCollision.getDistance() == Float.MAX_VALUE) {
			closestCollision.setDistance(0);
		}
		float x = closestCollision.getDistance() + radius + 0.2f;
		Vector3f extent = rightDirection.mult(x);
		return position.add(extent);
	}

	private ArrayList<Vector3f> getPointsToCheckCollisionsWith(
			BoundingBox worldBound, Vector3f position) {
		Vector3f addX = position.add(
				new Vector3f(worldBound.getXExtent(), 0, 0));
		Vector3f addY = position.add(
				new Vector3f(0, worldBound.getYExtent(), 0));
		Vector3f addZ = position.add(
				new Vector3f(0, 0, worldBound.getZExtent()));
		Vector3f addXY = addX.add(addY);
		Vector3f addXZ = addX.add(addZ);
		Vector3f addYZ = addY.add(addZ);
		ArrayList<Vector3f> vectors = new ArrayList<>();
		vectors.add(addX);
		vectors.add(addY);
		vectors.add(addZ);
		vectors.add(addXZ);
		vectors.add(addXY);
		vectors.add(addYZ);
		vectors.add(position.add(Vector3f.ZERO.clone()));
		return vectors;
	}

	private CollisionResult collide(Vector3f position,
			Vector3f rightDirection) {

		Ray ray = new Ray(position, rightDirection);
		CollisionResults collisionResults = new CollisionResults();
		rootNode.collideWith(ray, collisionResults);
		return collisionResults.getFarthestCollision();
	}

}
