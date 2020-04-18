import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;

public class KeysSetup implements ActionListener {

	public static final String SELECT_MODEL = "select";
	public static final String MOVE_FORWARD = "moveForward";
	public static final String MOVE_BACKWARD = "moveBackward";
	public static final String MOVE_LEFT = "moveLeft";
	public static final String MOVE_RIGHT = "moveRight";
	public static final String MOVE_UP = "moveUp";
	public static final String MOVE_DOWN = "moveDown";
	public static final String CONFIRM_EXIT = "confirmExit";
	public static final String REJECT_EXIT = "rejectExit";
	public static final String EXIT = "exit";
	private static final String UNSELECT_MODEL = "unselect";
	private InputManager inputManager;
	private ModelSelectionController modelSelectionController;
	private SelectionStateDTO selectionStateDTO;

	public KeysSetup(InputManager inputManager,
			ModelSelectionController modelSelectionController, SelectionStateDTO selectionStateDTO) {
		this.inputManager = inputManager;
		this.modelSelectionController = modelSelectionController;
		this.selectionStateDTO = selectionStateDTO;
	}

	public void setUp() {
		inputManager.addMapping(SELECT_MODEL,
				new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addMapping(UNSELECT_MODEL,
				new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
		inputManager.addMapping(MOVE_FORWARD, new KeyTrigger(KeyInput.KEY_T));
		inputManager.addMapping(MOVE_BACKWARD, new KeyTrigger(KeyInput.KEY_G));
		inputManager.addMapping(MOVE_LEFT, new KeyTrigger(KeyInput.KEY_F));
		inputManager.addMapping(MOVE_RIGHT, new KeyTrigger(KeyInput.KEY_H));
		inputManager.addMapping(MOVE_UP, new KeyTrigger(KeyInput.KEY_5));
		inputManager.addMapping(MOVE_DOWN, new KeyTrigger(KeyInput.KEY_B));
		inputManager.addMapping(EXIT, new KeyTrigger(KeyInput.KEY_ESCAPE));
		inputManager.addMapping(CONFIRM_EXIT, new KeyTrigger(KeyInput.KEY_Y));
		inputManager.addMapping(REJECT_EXIT, new KeyTrigger(KeyInput.KEY_N));
		inputManager.addListener(this, SELECT_MODEL, UNSELECT_MODEL,
				MOVE_FORWARD, MOVE_BACKWARD, MOVE_LEFT, MOVE_RIGHT, MOVE_UP,
				MOVE_DOWN, EXIT, CONFIRM_EXIT, REJECT_EXIT);
	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		if (SELECT_MODEL.equals(name) && isPressed) {
			modelSelectionController.selectCurrentSpatial();
		}
		if (UNSELECT_MODEL.equals(name) && isPressed) {
			modelSelectionController.unselectCurrentSpatial();
		}
		if (MOVE_FORWARD.equals(name)) {
			selectionStateDTO.setMovingForward(isPressed);
		}
		if (MOVE_BACKWARD.equals(name)) {
			selectionStateDTO.setMovingBackward(isPressed);
		}
		if (MOVE_LEFT.equals(name)) {
			selectionStateDTO.setMovingLeft(isPressed);
		}
		if (MOVE_RIGHT.equals(name)) {
			selectionStateDTO.setMovingRight(isPressed);
		}
		if (MOVE_UP.equals(name)) {
			selectionStateDTO.setMovingUp(isPressed);
		}
		if (MOVE_DOWN.equals(name)) {
			selectionStateDTO.setMovingDown(isPressed);
		}
		if (EXIT.equals(name) && isPressed) {
			selectionStateDTO.setExitRequested(true);
		}
		if (CONFIRM_EXIT.equals(name)) {
			if (selectionStateDTO.isExitRequested() && isPressed){
				selectionStateDTO.setExitConfirmed(true);
			}
		}
		if (REJECT_EXIT.equals(name)) {
			if (selectionStateDTO.isExitRequested() && isPressed){
				selectionStateDTO.setExitConfirmed(false);
				selectionStateDTO.setExitRequested(false);
			}
		}
	}
}
