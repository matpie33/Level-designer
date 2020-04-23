package saveAndLoad;

import com.jme3.scene.Spatial;
import dto.SpatialDTO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileSaveAndLoad {

	public static final String FILENAME = "level.txt";
	private FileSave fileSave = new FileSave();

	public void save(List<Spatial> spatials) {
		List<SpatialDTO> spatialsDTO = createSpatialDTOs(spatials);
		String text = fileSave.save(spatialsDTO);
		createFile(text);
	}

	private void createFile(String text) {
		Path file = Paths.get(FILENAME);
		writeFile(text, file);
	}

	private void writeFile(String text, Path file) {
		try {
			Files.write(file, text.getBytes());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<SpatialDTO> createSpatialDTOs(List<Spatial> spatials) {
		List<SpatialDTO> spatialDTOS = new ArrayList<>();
		for (Spatial spatial : spatials) {
			SpatialDTO spatialDTO = new SpatialDTO();
			spatialDTO.setPathToModel(spatial.getKey()
											 .getName());
			spatialDTO.setPosition(spatial.getLocalTranslation());
			spatialDTO.setRotation(spatial.getLocalRotation());
			spatialDTOS.add(spatialDTO);
		}
		return spatialDTOS;
	}

}
