package controls;

import com.jme3.bullet.control.GhostControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import dto.ApplicationStateDTO;
import dto.GeometryDTO;

import java.util.Optional;

public class CollisionPreventControl extends AbstractControl {

	private Vector3f previousPositions;
	private ApplicationStateDTO applicationStateDTO;
	private Vector3f directionOfMovingBackFromObstacle;

	public CollisionPreventControl(ApplicationStateDTO applicationStateDTO) {
		this.applicationStateDTO = applicationStateDTO;
	}

	@Override
	protected void controlUpdate(float tpf) {
		GhostControl control = spatial.getControl(GhostControl.class);
		Optional<GeometryDTO> modelFromSelected = applicationStateDTO.getSelectedModels()
																	 .stream()
																	 .filter(geometryDTO -> geometryDTO.getGeometry()
																									   .getParent()
																									   .equals(spatial))
																	 .findFirst();
		if (!modelFromSelected.isPresent()) {
			return;
		}
		GeometryDTO geometryData = modelFromSelected.get();
		if (control.getOverlappingCount() > 0 && previousPositions != null) {
			Vector3f position = spatial.getLocalTranslation();
			if (directionOfMovingBackFromObstacle == null) {
				directionOfMovingBackFromObstacle = position.subtract(
						previousPositions);
			}
			spatial.setLocalTranslation(
					position.subtract(directionOfMovingBackFromObstacle));
			geometryData.setColliding(true);
		}
		else {
			geometryData.setColliding(false);
			directionOfMovingBackFromObstacle = null;
			Vector3f vector = spatial.getLocalTranslation()
									 .clone();
			previousPositions = vector;
		}
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {

	}
}
