
package rules_spotless;

import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import com.diffplug.spotless.extra.integration.DiffMessageFormatter;
import java.nio.file.Path;

public final class RunCheck {

    public static void main(String[] args) throws IOException {
		String report_file = args[0];
		String subpath = args[1];
		System.out.println(subpath);
		List<String> sourceFiles = Arrays.asList(Arrays.copyOfRange(args, 2, args.length));
		System.out.println(new File(sourceFiles.get(0)).getAbsolutePath());

		Path workspaceDir = new File(".").toPath();
		Path output_directory = new File(args[0]).toPath().getParent();

		JavaFormatter formatter = new JavaFormatter();

		FormatExecutor executor = new FormatExecutor(workspaceDir);
		List<File> failedFiles = executor.processFiles(formatter.getExtension(), output_directory, sourceFiles);

      	try(FileWriter myWriter = new FileWriter(report_file)) {
			for (File f : failedFiles) {
				myWriter.write("Hello");
			}
		}

		if (failedFiles.isEmpty()) {
			System.exit(0);
		}
		
		if (!failedFiles.isEmpty()) {
			Collections.sort(failedFiles);
			System.out.println(DiffMessageFormatter.builder()
					.runToFix("To fix, run:\n\nbazel run //" + subpath + ":spotless.apply\n")
					// .runToFix(getRunToFixMessage().get())
					.formatterFolder(
							workspaceDir,
							output_directory,
							executor.getEncoding())
					.problemFiles(failedFiles)
					.getMessage());
		}
		System.exit(1);
    }

}