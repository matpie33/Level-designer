package controllers;

import com.jme3.bounding.BoundingBox;
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
			CollisionResults results = collide(vector, rightDirection);
			CollisionResult farthestCollision = checkForSpaceBetweenObjectsEnoughToPutClone(
					results, clonedSpatial, rightDirection);
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

	private CollisionResult checkForSpaceBetweenObjectsEnoughToPutClone(
			CollisionResults results, Node clonedSpatial,
			Vector3f rightDirection) {
		Vector3f extentInGivenDirection = ((BoundingBox) clonedSpatial.getWorldBound()).getExtent(
				new Vector3f())
																					   .mult(rightDirection);
		float lengthOfClonedObject = extentInGivenDirection.length();
		float distanceToPreviousObject = 0;
		CollisionResult previousCollidingObject = null;
		for (CollisionResult result : results) {
			float lengthOfCurrentObject = ((BoundingBox) result.getGeometry()
															   .getWorldBound()).getExtent(
					new Vector3f())
																				.mult(rightDirection)
																				.length();
			if (result.getDistance() - distanceToPreviousObject
					- lengthOfClonedObject - lengthOfCurrentObject
					> lengthOfClonedObject + 0.5f) {
				return previousCollidingObject;
			}
			distanceToPreviousObject = result.getDistance();
			previousCollidingObject = result;
		}
		return results.getFarthestCollision();
	}

	private Vector3f calculateNewPosition(Node clonedSpatial, Vector3f position,
			CollisionResult closestCollision, Vector3f rightDirection) {
		float sizeOfClonedSpatialInGivenDirection = calculateSizeOfSpatialInGivenDirection(
				rightDirection, clonedSpatial);
		if (closestCollision.getDistance() == Float.MAX_VALUE) {
			closestCollision.setDistance(0);
		}
		float distanceToMove = closestCollision.getDistance()
				+ sizeOfClonedSpatialInGivenDirection + 0.2f;
		Vector3f extent = rightDirection.mult(distanceToMove);
		return position.add(extent);
	}

	private float calculateSizeOfSpatialInGivenDirection(
			Vector3f rightDirection, Node spatial) {
		Vector3f extent = ((BoundingBox) spatial.getWorldBound()).getExtent(
				new Vector3f());

		return extent.mult(rightDirection)
					 .length();
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

	private CollisionResults collide(Vector3f position,
			Vector3f rightDirection) {

		Ray ray = new Ray(position, rightDirection);
		CollisionResults collisionResults = new CollisionResults();
		rootNode.collideWith(ray, collisionResults);
		return collisionResults;
	}

}
