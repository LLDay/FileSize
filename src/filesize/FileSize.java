package filesize;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The class FileSize is used to extended string output a size of files and directories
 *
 * @author LLDay
 */
public class FileSize {
	/**
	 * Constructor
	 *
	 * @param listFilesStr is a list with paths to files
	 */
	public FileSize(List<String> listFilesStr) {
		if (listFilesStr == null)
			throw new NullPointerException("String listFilesStr in not initialized");

		if (listFilesStr.isEmpty())
			throw new IllegalArgumentException("List of filenames is empty");

		listFiles = new ArrayList<>();
		readable = true;
		setStandard(SIZE_STANDARD.JEDEC);

		for (String fileStr : listFilesStr) {
			File tmpFile = new File(fileStr);
			if (!tmpFile.exists())
				throw new IllegalArgumentException("File " + tmpFile.getName() + " is not exist");

			if (!listFiles.contains(tmpFile))
				listFiles.add(tmpFile);
		}
	}

	/**
	 * Sets standard to calculate size of files
	 *
	 * @param standard is JEDEC or IEC from enum SIZE_STANDARD
	 */
	public void setStandard(SIZE_STANDARD standard) {
		this.standard = standard;
	}

	/**
	 * Gets usable standard to calculate size of files
	 *
	 * @return JEDEC or IEC standards
	 */
	public SIZE_STANDARD getStandard() {
		return standard;
	}

	/**
	 * Sets whether the function should have readable output
	 */
	public void setReadable(boolean useReadableFormat) {
		readable = useReadableFormat;
	}

	/**
	 * @return should the function have readable output
	 */
	public boolean getReadable() {
		return readable;
	}

	/**
	 * @return the name and the size of each file from list
	 */
	public String getEachFile() {
		StringBuilder strBuff = new StringBuilder();

		for (File file : listFiles)
			strBuff.append(String.format("%1$-40s", file.getName())).
					append(getConvertSize(FileSizeCore.getFileSize(file))).
					append('\n');

		return strBuff.toString();
	}

	/**
	 * @return the common size of all files from list
	 */
	public String getSumFile() {
		return "Common size: " + getConvertSize(FileSizeCore.getSumSize(listFiles));
	}

	/**
	 * @param bytes is size of file
	 * @return string representation of the number of bytes
	 */
	private String getConvertSize(long bytes) {
		if (readable)
			return FileSizeCore.convertToReadable(bytes, standard);
		return FileSizeCore.convertToKB(bytes, standard);
	}


	private List<File> listFiles;
	private boolean readable;
	private SIZE_STANDARD standard;
}
