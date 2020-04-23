package saveAndLoad;

import constants.FileConstants;
import dto.SpatialDTO;

import java.util.List;

public class FileSave {


	public String save(List<SpatialDTO> spatialDTOS) {
		StringBuilder text = new StringBuilder();
		for (SpatialDTO spatialDTO : spatialDTOS) {
			writeSpatial(spatialDTO, text);
		}
		return text.toString();
	}

	private void writeSpatial(SpatialDTO spatialDTO, StringBuilder builder) {
		builder.append(FileConstants.START_OBJECT);
		builder.append(FileConstants.NEWLINE);
		addParam(FileConstants.PATH_TO_MODEL, spatialDTO.getPathToModel(), builder);
		addParam(FileConstants.POSITION, spatialDTO.getPosition().toString(), builder);
		addParam(FileConstants.ROTATION, spatialDTO.getRotation().toString(), builder);
	}

	private void addParam(String name, String value, StringBuilder builder) {
		if (value != null) {
			builder.append(name)
				   .append(FileConstants.PARAM_SEPARATOR)
				   .append(value)
				   .append(FileConstants.NEWLINE);

		}
	}

}
