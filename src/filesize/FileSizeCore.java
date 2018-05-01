package filesize;

import java.io.File;

enum SIZE_STANDARD {
	JEDEC, //1024
	IEC    //1000
}

/**
 * The class FileSizeCore is used to return string representation of a file size
 * and to calculate size of a file and a directory
 *
 * @author LLDay
 */
public class FileSizeCore {
	/**
	 * @param file is some file
	 * @return a file size in bytes if a file exist
	 */
	public static long getFileSize(File file) {
		if (!file.exists())
			throw new IllegalArgumentException("File " + file.getName() + " is not exist");

		File[] listFiles = file.listFiles();

		// a file
		if (listFiles == null)
			return file.length();

		// a directory
		long result = 0;
		for (File someFile : listFiles)
			if (someFile.isFile())
				result += someFile.length();
			else result += getFileSize(someFile);

		return result;
	}

	/**
	 * @param listFiles some file list
	 * @return common size of all files from a list
	 */
	public static long getSumSize(Iterable<File> listFiles) {
		if (listFiles == null)
			throw new IllegalArgumentException("List of files is not initialized");

		long result = 0;
		for (File file : listFiles) {
			if (!file.exists())
				throw new IllegalArgumentException("File " + file.getName() + " is not exist");
			result += getFileSize(file);
		}

		return result;
	}

	/**
	 * @param bytes is size of a file in bytes
	 * @param standard is usable standard to calculate a size of files
	 * @return string representation in KB
	 */
	public static String convertToKB(long bytes, SIZE_STANDARD standard) {
		int conversionVal = standard == SIZE_STANDARD.JEDEC ? 1024 : 1000;

		return String.format("%.2f", (double) bytes / conversionVal);
	}

	/**
	 * @param bytes is size of a file in bytes
	 * @param standard is usable standard to calculate a size of files
	 * @return string representation in useful format (KB, MB, GB, TB)
	 */
	public static String convertToReadable(long bytes, SIZE_STANDARD standard) {
		int conversionVal = standard == SIZE_STANDARD.JEDEC ? 1024 : 1000;

		double kBytes = (double) bytes / conversionVal;

		String prefix[] = {"KB", "MB", "GB", "TB"};
		final int maxIndex = prefix.length - 1;
		int index = 0;

		while (index < maxIndex && kBytes >= conversionVal) {
			kBytes /= conversionVal;
			index++;
		}

		return String.format("%.2f", kBytes) + prefix[index];
	}
}
