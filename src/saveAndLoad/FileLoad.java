package saveAndLoad;

import Constants.FileConstants;
import DTO.SpatialDTO;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileLoad {


	public void readFile(String filePath) {
		List<String> lines = readLinesFromFile(filePath);
		readSpatials(lines);
	}

	private void readSpatials(List<String> lines) {
		List<SpatialDTO> spatials = new ArrayList<>();
		SpatialDTO currentlyReadedSpatial = null;
		for (String line : lines) {
			if (line.equals(FileConstants.START_OBJECT)) {
				currentlyReadedSpatial = new SpatialDTO();
				spatials.add(currentlyReadedSpatial);
			}
			else {
				readParams(line, currentlyReadedSpatial);
			}
		}
	}

	private void readParams(String line, SpatialDTO currentlyReadedSpatial) {
		String[] split = line.split(FileConstants.PARAM_SEPARATOR);
		String paramName = split[0];
		String paramValue = split[1];
		switch (paramName) {
		case FileConstants.PATH_TO_MODEL:
			currentlyReadedSpatial.setPathToModel(paramValue);
			break;
		case FileConstants.POSITION:
			currentlyReadedSpatial.setPosition(readVector(paramValue));
			break;
		case FileConstants.ROTATION:
			currentlyReadedSpatial.setRotation(readQuaternion(paramValue));
			break;
		}

	}

	private Quaternion readQuaternion(String paramValue) {
		String replace = removeBrackets(paramValue);
		String[] split = replace.split(FileConstants.COMMA_SPACE);
		return new Quaternion(Float.parseFloat(split[0]),
				Float.parseFloat(split[1]), Float.parseFloat(split[2]),
				Float.parseFloat(split[3]));
	}

	private String removeBrackets(String paramValue) {
		return paramValue.replace("(", "")
						 .replace(")", "");
	}

	private Vector3f readVector(String paramValue) {
		String replace = removeBrackets(paramValue);
		String[] split = replace.split(FileConstants.COMMA_SPACE);
		return new Vector3f(Float.parseFloat(split[0]),
				Float.parseFloat(split[1]), Float.parseFloat(split[2]));
	}

	private List<String> readLinesFromFile(String filePath) {
		try {
			return Files.readAllLines(Paths.get(filePath),
					StandardCharsets.UTF_8);
		}
		catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

}
