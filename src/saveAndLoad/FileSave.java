package saveAndLoad;

import DTO.SpatialDTO;

import java.util.List;

public class FileSave {

	public static final String START_OBJECT = "Start object";
	public static final String PATH_TO_MODEL = "Path";
	public static final String LOCATION_ON_SCREEN = "Location";
	public static final String ROTATION = "rotation";
	public static final String END_OBJECT = "End object";
	public static final String NEWLINE = "\n";

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
		addParam(LOCATION_ON_SCREEN, spatialDTO.getPosition().toString(), builder);
		addParam(ROTATION, spatialDTO.getRotation().toString(), builder);
		builder.append(END_OBJECT);
		builder.append(NEWLINE);
	}

	private void addParam(String name, String value, StringBuilder builder) {
		if (value != null) {
			builder.append(name)
				   .append(": ")
				   .append(value)
				   .append(NEWLINE);

		}
	}

}
