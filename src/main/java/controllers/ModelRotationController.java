package controllers;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import dto.ApplicationStateDTO;
import dto.GeometryDTO;

public class ModelRotationController implements AbstractController {
	private ApplicationStateDTO applicationStateDTO;
	private final Quaternion BY_Y_90_DEGREE_ROTATION;

	public ModelRotationController(ApplicationStateDTO applicationStateDTO) {
		Quaternion quaternion = new Quaternion();
		this.BY_Y_90_DEGREE_ROTATION = quaternion.fromAngleAxis(FastMath.DEG_TO_RAD * 90,
				Vector3f.UNIT_Y);
		this.applicationStateDTO = applicationStateDTO;
	}

	@Override
	public void update() {
		if (applicationStateDTO.getSelectedModels() != null
				&& applicationStateDTO.isRotationRequested()) {
			applicationStateDTO.setRotationRequested(false);
			for (GeometryDTO selectedModel : applicationStateDTO.getSelectedModels()) {
				selectedModel.getGeometry()
							 .getParent()
							 .rotate(BY_Y_90_DEGREE_ROTATION);
			}
		}
	}

	@Override
	public void setUp() {

	}
}
