package initialization;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PathToModelsReader {

	public static final String PATH_TO_FILE = "./paths.txt";

	public List<String> readPaths() {
		InputStream resourceAsStream = loadFile();
		InputStreamReader streamReader = new InputStreamReader(
				resourceAsStream);
		BufferedReader bufferedReader = new BufferedReader(streamReader);
		return readFile(bufferedReader);
	}

	private List<String> readFile(BufferedReader bufferedReader) {
		try {
			return readLines(bufferedReader);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	private InputStream loadFile() {
		InputStream resourceAsStream = null;
		try {
			resourceAsStream = new FileInputStream(PATH_TO_FILE);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return resourceAsStream;
	}

	private List<String> readLines(BufferedReader bufferedReader)
			throws IOException {
		String line;
		List<String> lines = new ArrayList<>();
		while ((line = bufferedReader.readLine()) != null) {
			lines.add(line);
		}
		return lines;
	}

}
