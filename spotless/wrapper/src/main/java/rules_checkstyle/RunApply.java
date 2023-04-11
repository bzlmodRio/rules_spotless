
package rules_spotless;

import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class RunApply {

    public static void main(String[] args) throws IOException {
		System.out.println(System.getenv());
		String report_file = args[0];
		System.out.println("Running apply");
		List<String> sourceFiles = Arrays.asList(Arrays.copyOfRange(args, 2, args.length));
		System.out.println(sourceFiles);
		Path workspaceDir = new File(System.getenv("BUILD_WORKSPACE_DIRECTORY")).toPath();
		Path output_directory = new File(System.getenv("BUILD_WORKSPACE_DIRECTORY")).toPath();
		

		JavaFormatter formatter = new JavaFormatter();
		FormatExecutor executor = new FormatExecutor(workspaceDir);
		List<File> failedFiles = executor.processFiles(formatter.getExtension(), output_directory, sourceFiles);
		System.out.println("Running apply2");
		System.out.println(failedFiles);
		
      	//try(FileWriter myWriter = new FileWriter(report_file)) {
		//	for (File f : failedFiles) {
		//		Files.copy(new File(workspaceDir.toFile(), "fjdlskfj"), new File(output_directoryr.toFile(), "fjdlskfj"), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
		//	}
		//}

		// myWriter.close();

		// if (failedFiles.isEmpty()) {
		// 	System.exit(0);
		// }
		
		// System.exit(1);
    }

}