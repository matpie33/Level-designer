package dto;

public class OptionsDTO {

	private static final OptionsDTO INSTANCE = new OptionsDTO();

	private OptionsDTO() {

	}

	public static OptionsDTO getInstance() {
		return INSTANCE;
	}

	private float modelMovementSpeed;

	public float getModelMovementSpeed() {
		return modelMovementSpeed;
	}

	public void setModelMovementSpeed(float modelMovementSpeed) {
		this.modelMovementSpeed = modelMovementSpeed;
	}
}
