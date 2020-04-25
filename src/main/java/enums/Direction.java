package enums;

import com.jme3.math.Vector3f;

public enum Direction {
	X, Y, Z;

	public boolean isVectorSameDirection(Vector3f direction) {
		switch (this) {
		case X:
			return direction.getX() != 0;
		case Y:
			return direction.getY() != 0;
		case Z:
			return direction.getZ() != 0;
		}
		throw new IllegalArgumentException("Unknown direction: " + this);
	}
}
