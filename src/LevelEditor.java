import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;

import java.awt.*;

public class LevelEditor  extends SimpleApplication {

	public static void main (String [] args){
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

	}
}
