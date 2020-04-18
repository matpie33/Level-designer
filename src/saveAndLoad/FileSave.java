package saveAndLoad;

import DTO.SpatialDTO;

import java.util.List;

public class FileSave {

	public static final String START_OBJECT = "Start object";
	public static final String PATH_TO_MODEL = "Path";
	public static final String POSITION = "Position";
	public static final String ROTATION = "Rotation";
	public static final String NEWLINE = "\n";
	public static final String PARAM_SEPARATOR = ": ";

	public String save(List<SpatialDTO> spatialDTOS) {
		StringBuilder text = new StringBuilder();
		for (SpatialDTO spatialDTO : spatialDTOS) {
			writeSpatial(spatialDTO, text);
		}
		return text.toString();
	}

	private void writeSpatial(SpatialDTO spatialDTO, StringBuilder builder) {
		builder.append(START_OBJECT);
		builder.append(NEWLINE);
		addParam(PATH_TO_MODEL, spatialDTO.getPathToModel(), builder);
		addParam(POSITION, spatialDTO.getPosition().toString(), builder);
		addParam(ROTATION, spatialDTO.getRotation().toString(), builder);
	}

	private void addParam(String name, String value, StringBuilder builder) {
		if (value != null) {
			builder.append(name)
				   .append(PARAM_SEPARATOR)
				   .append(value)
				   .append(NEWLINE);

		}
	}

}
