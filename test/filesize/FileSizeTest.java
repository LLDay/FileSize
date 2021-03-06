package filesize;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class FileSizeTest {
	public FileSizeTest() {
		testDirectory = new File("testDirectory");
		directory1_75MB = new File("testDirectory/1.75MB");
		emptyFile = new File("testDirectory/0KB.txt");
		file3_20KB = new File("testDirectory/3.20KB.txt");
		file144KB = new File("testDirectory/144KB.txt");

		ArrayList<String> fileStr = new ArrayList<>();
		fileStr.add(directory1_75MB.getPath());
		fileStr.add(file3_20KB.getPath()); //!
		fileStr.add(file144KB.getPath());
		fileStr.add(file3_20KB.getPath()); //!

		fs = new FileSize(fileStr);
	}

	private String getRoundingNumber(double d) {
		return String.format("%.2f", d);
	}

	private String getReadableJEDEC(long bytes) {
		double d = bytes;

		while (d > 1024.0)
			d /= 1024.0;

		return getRoundingNumber(d);
	}

	private String getReadableIEC(double bytes) {
		double d = bytes;

		while (d > 1000.0)
			d /= 1000.0;

		return getRoundingNumber(d);
	}

	@Test
	public void fileSize() {
		//from file properties
		assertEquals(1989808, FileSizeCore.getFileSize(testDirectory));
		assertEquals(1839020, FileSizeCore.getFileSize(directory1_75MB));
		assertEquals(147510, FileSizeCore.getFileSize(file144KB));
		assertEquals(3278, FileSizeCore.getFileSize(file3_20KB));
		assertEquals(0, FileSizeCore.getFileSize(emptyFile));

		ArrayList<File> listFiles = new ArrayList<>();
		listFiles.add(directory1_75MB);
		listFiles.add(file144KB);
		listFiles.add(file3_20KB);
		listFiles.add(emptyFile);

		assertEquals(FileSizeCore.getFileSize(testDirectory), FileSizeCore.getSumSize(listFiles));

		assertThrows(IllegalArgumentException.class, () -> {
			ArrayList<String> args = new ArrayList<>();
			args.add("nonexistentFile.txt");
			new FileSize(args);
		});

		assertThrows(IllegalArgumentException.class, () -> {
			ArrayList<String> args = new ArrayList<>();
			new FileSize(args);
		});

		assertThrows(NullPointerException.class, () -> {
			ArrayList<String> args = null;
			new FileSize(args);
		});
	}

	@Test
	void repeatTest() {
		String result = fs.getEachFile();
		assertTrue(result.split("\n").length == 3);
	}

	@Test
	void keyExTest() {
		//default settings
		assertEquals(SIZE_STANDARD.JEDEC, fs.getStandard());
		assertEquals(true, fs.getReadable());

		String defStr = fs.getSumFile();

		//changed settings
		fs.setStandard(SIZE_STANDARD.IEC);
		assertFalse(fs.getSumFile().equals(defStr));
		assertTrue(fs.getSumFile().contains("MB"));
		fs.setStandard(SIZE_STANDARD.JEDEC);

		fs.setReadable(false);
		assertFalse(fs.getSumFile().equals(defStr));
		String nonReadableStr = fs.getSumFile();
		assertTrue(!nonReadableStr.contains("MB") || !nonReadableStr.contains("KB"));
		fs.setReadable(true);

		assertEquals(fs.getSumFile(), defStr);
	}

	@Test
	public void convertToKB() {
		long dirSize = FileSizeCore.getFileSize(directory1_75MB);
		double dirJEDEC = dirSize / 1024.0;
		double dirIEC = dirSize / 1000.0;

		assertEquals(getRoundingNumber(dirJEDEC), FileSizeCore.convertToKB(dirSize, SIZE_STANDARD.JEDEC));
		assertEquals(getRoundingNumber(dirIEC), FileSizeCore.convertToKB(dirSize, SIZE_STANDARD.IEC));

		long fileSize = FileSizeCore.getFileSize(file144KB);
		double fileJEDEC = fileSize / 1024.0;
		double fileIEC = fileSize / 1000.0;

		assertEquals(getRoundingNumber(fileJEDEC), FileSizeCore.convertToKB(fileSize, SIZE_STANDARD.JEDEC));
		assertEquals(getRoundingNumber(fileIEC), FileSizeCore.convertToKB(fileSize, SIZE_STANDARD.IEC));
	}

	@Test
	public void convertToReadable() {
		long dirSize = FileSizeCore.getFileSize(testDirectory);
		long fileSize = FileSizeCore.getFileSize(emptyFile);

		assertEquals(getReadableJEDEC(dirSize) + "MB", FileSizeCore.convertToReadable(dirSize, SIZE_STANDARD.JEDEC));
		assertEquals(getReadableJEDEC(fileSize) + "KB", FileSizeCore.convertToReadable(fileSize, SIZE_STANDARD.JEDEC));

		assertEquals(getReadableIEC(dirSize) + "MB", FileSizeCore.convertToReadable(dirSize, SIZE_STANDARD.IEC));
		assertEquals(getReadableIEC(fileSize) + "KB", FileSizeCore.convertToReadable(fileSize, SIZE_STANDARD.IEC));

		assertEquals("1,00MB", FileSizeCore.convertToReadable(1024 * 1024, SIZE_STANDARD.JEDEC));
		assertEquals("1,00MB", FileSizeCore.convertToReadable(1000 * 1000, SIZE_STANDARD.IEC));

		assertEquals("1024,00TB", FileSizeCore.convertToReadable(1024L * 1024L * 1024L * 1024L * 1024L, SIZE_STANDARD.JEDEC));
	}


	private File testDirectory;
	private File directory1_75MB;
	private File emptyFile;
	private File file3_20KB;
	private File file144KB;
	private FileSize fs;
}
