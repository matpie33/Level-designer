import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;

import java.awt.*;

public class LevelEditor extends SimpleApplication {

	public static void main(String[] args) {
		LevelEditor levelEditor = loadGame();

	}

	private static LevelEditor loadGame() {
		LevelEditor gameApplication = new LevelEditor();
		AppSettings settings = new AppSettings(true);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		//		settings.setResolution(screenSize.width, screenSize.height);
		gameApplication.setShowSettings(false);
		gameApplication.setSettings(settings);
		gameApplication.start();
		return gameApplication;
	}

	@Override
	public void simpleInitApp() {
		ModelsLoader modelsLoader = new ModelsLoader(assetManager);
		modelsLoader.loadModels();
	}
}
