package controllers;

import com.jme3.input.FlyByCamera;
import dto.ApplicationStateDTO;
import dto.OptionsDTO;

public class AccelerationDecelerationController implements AbstractController {

	private ApplicationStateDTO applicationStateDTO;
	private FlyByCamera flyByCamera;
	private static final float CAMERA_SPEED_INCREASE_DECREASE_VALUE = 25F;
	private static final float MODEL_MOVEMENT_SPEED_INCREASE_DECREASE_VALUE = 0.5f;

	public AccelerationDecelerationController(
			ApplicationStateDTO applicationStateDTO, FlyByCamera flyByCamera) {
		this.applicationStateDTO = applicationStateDTO;
		this.flyByCamera = flyByCamera;
	}

	@Override
	public void update() {
		if (applicationStateDTO.isAccelerationRequested()) {
			applicationStateDTO.setAccelerationRequested(false);
			changeSpeed(1);
		}
		if (applicationStateDTO.isDecelerationRequested()) {
			applicationStateDTO.setDecelerationRequested(false);
			changeSpeed(-1);
		}
	}

	private void changeSpeed(int multiplier) {
		float moveSpeed = flyByCamera.getMoveSpeed();
		moveSpeed += CAMERA_SPEED_INCREASE_DECREASE_VALUE * multiplier;
		if (moveSpeed < 1f) {
			moveSpeed = 1f;
		}
		flyByCamera.setMoveSpeed(moveSpeed);
		OptionsDTO optionsDTO = OptionsDTO.getInstance();
		optionsDTO.setModelMovementSpeed(optionsDTO.getModelMovementSpeed()
				+ multiplier * MODEL_MOVEMENT_SPEED_INCREASE_DECREASE_VALUE);
		if (optionsDTO.getModelMovementSpeed() < 0.1f) {
			optionsDTO.setModelMovementSpeed(0.1f);
		}
	}

	@Override
	public void setUp() {

	}
}
