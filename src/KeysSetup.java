import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;

public class KeysSetup implements ActionListener {

	public static final String SELECT_MODEL = "select";
	private static final String UNSELECT_MODEL = "unselect";
	private InputManager inputManager;
	private ModelSelectionController modelSelectionController;

	public KeysSetup(InputManager inputManager,
			ModelSelectionController modelSelectionController) {
		this.inputManager = inputManager;
		this.modelSelectionController = modelSelectionController;
	}

	public void setUp() {
		inputManager.addMapping(SELECT_MODEL,
				new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addMapping(UNSELECT_MODEL,
				new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
		inputManager.addListener(this, SELECT_MODEL, UNSELECT_MODEL);
	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		if (SELECT_MODEL.equals(name) && isPressed) {
			modelSelectionController.selectCurrentSpatial();
		}
		if (UNSELECT_MODEL.equals(name) && isPressed) {
			modelSelectionController.unselectCurrentSpatial();
		}
	}
}
