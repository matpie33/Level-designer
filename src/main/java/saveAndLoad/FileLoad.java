package saveAndLoad;

import Constants.FileConstants;
import DTO.SpatialDTO;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileLoad {

	public List<SpatialDTO> readFile(InputStream filePath) {
		List<String> lines = readLines(filePath);
		return readSpatials(lines);
	}

	private List<String> readLines(InputStream filePath){
		try {
			return readLinesFromFile(filePath);
		}
		catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	private List<SpatialDTO> readSpatials(List<String> lines) {
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
		return spatials;
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

	private List<String> readLinesFromFile(InputStream filePath)
			throws IOException {
		InputStreamReader streamReader = new InputStreamReader(filePath);
		BufferedReader bufferedReader = new BufferedReader(streamReader);
		String line;
		List<String> lines = new ArrayList<>();
		while ((line = bufferedReader.readLine()) != null) {
			lines.add(line);
		}
		return lines;
	}

}
