package filesize;

import org.apache.commons.cli.*;


public class FileSizeCLI {
	public static void main(String[] args) {
		Option humanReadFormat = new Option("h", false, "Human readable format");
		humanReadFormat.setRequired(false);

		Option sumAll = new Option("c", false, "Calculate size of all files");
		sumAll.setRequired(false);

		Option siFormat = new Option("si", false, "Use SI values of prefixes");
		siFormat.setRequired(false);

		Options options = new Options();
		options.addOption(humanReadFormat);
		options.addOption(sumAll);
		options.addOption(siFormat);

		CommandLineParser clParser = new DefaultParser();
		CommandLine commandLine;

		try {
			commandLine = clParser.parse(options, args);
			FileSize fSize = new FileSize(commandLine.getArgList());

			fSize.setStandard(commandLine.hasOption("si") ? SIZE_STANDARD.IEC : SIZE_STANDARD.JEDEC);
			fSize.setReadable(commandLine.hasOption("h"));
			boolean isCommon = commandLine.hasOption("c");

			if (isCommon)
				System.out.println(fSize.getSumFile());
			else System.out.println(fSize.getEachFile());
		}
		catch (Exception e) {
			System.err.println(e.getMessage());

			try {
				Thread.sleep(2000L);
			}
			catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
				throw new RuntimeException(ex);
			}

			System.exit(1);
		}
	}
}
