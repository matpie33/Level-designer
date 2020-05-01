package dto;

import com.jme3.math.Vector3f;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class DirectionToMoveDTO {

	private final Function<Vector3f, Float> getCoordinateFunction;
	private final BiConsumer<Vector3f, Float> setCoordinateFunction;
	private final boolean positiveDirection;
	private float amountToMove;

	public DirectionToMoveDTO(Function<Vector3f, Float> getCoordinateFunction,
			BiConsumer<Vector3f, Float> setCoordinateFunction, boolean positiveDirection) {
		this.getCoordinateFunction = getCoordinateFunction;
		this.setCoordinateFunction = setCoordinateFunction;
		this.positiveDirection = positiveDirection;
	}

	public BiConsumer<Vector3f, Float> getSetCoordinateFunction() {
		return setCoordinateFunction;
	}

	public Function<Vector3f, Float> getCoordinateFunction() {
		return getCoordinateFunction;
	}

	public boolean isPositiveDirection() {
		return positiveDirection;
	}

	public float getAmountToMove() {
		return amountToMove;
	}

	public void setAmountToMove(float amountToMove) {
		this.amountToMove = amountToMove;
	}
}
