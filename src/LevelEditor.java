import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;

import java.awt.*;
import java.util.List;

public class LevelEditor extends SimpleApplication {

	public static void main(String[] args) {
		 loadGame();

	}

	private static void loadGame() {
		LevelEditor gameApplication = new LevelEditor();
		AppSettings settings = new AppSettings(true);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		//		settings.setResolution(screenSize.width, screenSize.height);
		gameApplication.setShowSettings(false);
		gameApplication.setSettings(settings);
		gameApplication.start();
	}

	@Override
	public void simpleInitApp() {
		flyCam.setMoveSpeed(50f);
		ModelsLoader modelsLoader = new ModelsLoader(assetManager);
		List<Spatial> spatials = modelsLoader.loadModels();
		addLight();
		ModelToSceneAdder modelToSceneAdder = new ModelToSceneAdder(rootNode);
		modelToSceneAdder.addSpatials(spatials);
		initCrossHairs();
	}

	private void addLight() {
		AmbientLight sun = new AmbientLight();
		sun.setColor(ColorRGBA.White);
		rootNode.addLight(sun);
	}

	protected void initCrossHairs() {
		guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
		BitmapText ch = new BitmapText(guiFont, false);
		ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
		ch.setText("+"); // crosshairs
		ch.setLocalTranslation( // center
				settings.getWidth() / 2 - ch.getLineWidth()/2,
				settings.getHeight() / 2 + ch.getLineHeight()/2, 0);
		guiNode.attachChild(ch);
	}

}
