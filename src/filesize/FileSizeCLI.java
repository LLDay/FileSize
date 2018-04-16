package filesize;

import org.apache.commons.cli.*;

import java.util.ArrayList;
import java.util.HashSet;

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
			HashSet<String> set = new HashSet<>(commandLine.getArgList());
			FileSize fSize = new FileSize(new ArrayList<>(set));

			fSize.setStandard(commandLine.hasOption("si") ? SIZE_STANDARD.IEC : SIZE_STANDARD.JEDEC);
			fSize.setReadable(commandLine.hasOption("h"));
			boolean isCommon = commandLine.hasOption("c");

			if (isCommon)
				System.out.println(fSize.getSumFile());
			else System.out.println(fSize.getEachFile());
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
}
