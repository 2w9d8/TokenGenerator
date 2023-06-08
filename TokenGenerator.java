import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;

public class TokenGenerator {

	public static class DataFile {
		private File dataFile;

		public DataFile(String dataFileName) {
			this.dataFile = new File(System.getProperty("user.dir"), dataFileName);
			this.dataFile.getParentFile().mkdirs();
			if (!dataFile.exists()) {
				try {
					dataFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		public void write(String string) {
			if (this.dataFile.exists() && this.dataFile != null) {
				try (FileWriter fileWriter = new FileWriter(dataFile, true)) {
					fileWriter.append(string).append(System.lineSeparator());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static String base64Encode(String input) {
		byte[] encodedBytes = Base64.getEncoder().encode(input.getBytes(StandardCharsets.UTF_8));
		return new String(encodedBytes, StandardCharsets.UTF_8).replace("=", "");
	}

	public static String createToken() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(base64Encode(randomChars("0123456789".toCharArray(), 18))).append(".");
		stringBuilder.append(
				randomChars("-_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray(), 6))
				.append(".");
		stringBuilder.append(
				randomChars("-_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray(), 38));
		return stringBuilder.toString();
	}

	public static String randomChars(char[] charset, int length) {
		StringBuilder result = new StringBuilder();
		Random random = new Random();

		for (int x = 1; x <= length; x++) {
			result.append(charset[random.nextInt(charset.length)]);
		}

		return result.toString();
	}

	private static DataFile dataFile;

	public static void main(String[] args) {
		dataFile = new DataFile("tokens.txt");
		try {
			long tokens = Long.parseLong(args[0]);
			if (tokens <= 0) {
				System.err.println("[!] Please enter a positive number greater than zero..!");
				System.exit(0);
			} else {
				int createdTokens = 0;
				for (long x = 0; x < tokens; x++) {
					createdTokens++;
					String token = createToken();
					System.out.println(("[%count%] " + createToken() + " created successfully !").replace("%count%",
							String.valueOf(createdTokens)));
					getDataFile().write(token);
				}
			}
		} catch (NumberFormatException e) {
			System.err.println("[!] Please enter a valid numeric value..!");
			System.exit(0);
		}
	}

	public static DataFile getDataFile() {
		if (dataFile == null) {
			dataFile = new DataFile("tokens.txt");
		}
		return dataFile;
	}
}
