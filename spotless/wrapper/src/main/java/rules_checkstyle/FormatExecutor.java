
package rules_spotless;

import com.diffplug.spotless.FormatterStep;
import com.diffplug.spotless.FormatExceptionPolicy;
import com.diffplug.spotless.FormatExceptionPolicyStrict;

import java.nio.charset.Charset;
import com.diffplug.spotless.generic.LicenseHeaderStep;
import com.diffplug.spotless.java.FormatAnnotationsStep;
import com.diffplug.spotless.java.GoogleJavaFormatStep;
import com.diffplug.spotless.java.ImportOrderStep;
import com.diffplug.spotless.java.PalantirJavaFormatStep;
import java.nio.file.StandardCopyOption;
import com.diffplug.spotless.java.RemoveUnusedImportsStep;
import com.diffplug.spotless.LineEnding;
import com.diffplug.spotless.Formatter;
import com.diffplug.spotless.PaddedCell;
import com.diffplug.spotless.generic.EndWithNewlineStep;
import com.diffplug.spotless.generic.IndentStep;
import com.diffplug.spotless.generic.LicenseHeaderStep;
import com.diffplug.spotless.generic.LicenseHeaderStep.YearMode;
import com.diffplug.spotless.generic.NativeCmdStep;
import com.diffplug.spotless.generic.PipeStepPair;
import com.diffplug.spotless.generic.ReplaceRegexStep;
import com.diffplug.spotless.generic.ReplaceStep;
import com.diffplug.spotless.generic.TrimTrailingWhitespaceStep;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors

public class FormatExecutor {

	private FormatExceptionPolicy exceptionPolicy = new FormatExceptionPolicyStrict();
    private LineEnding.Policy lineEndingPolicy = LineEnding.PLATFORM_NATIVE.createPolicy();
    private String encoding = "UTF-8";

    private final Path rootDir;

    public FormatExecutor(Path rootDir) {
        this.rootDir = rootDir;
    }

    public List<File> processFiles(FormatExtension formatExtension, Path outputDirectory, List<String> inputFiles) throws IOException {
		List<File> failedFiles = new ArrayList<>();

		for (String inputFile : inputFiles) {
			File input = new File(rootDir.toFile(), inputFile);
			File output = new File(outputDirectory.toFile(), inputFile);
			
			Path parentDir = output.toPath().getParent();
			if (parentDir == null) {
				throw new IllegalStateException("Every file has a parent folder.");
			}
			Files.createDirectories(parentDir);

			if (!processFile(formatExtension, input, output)) {
				failedFiles.add(input);
			}
		}

        return failedFiles;
    }

	public boolean processFile(FormatExtension formatExtension, File input, File output) throws IOException {
		PaddedCell.DirtyState dirtyState = PaddedCell.calculateDirtyState(buildFormatter(formatExtension.getSteps()), input);
        // System.out.println("Processing " + input);
        if (dirtyState.isClean()) {
			return true;
        } else if (dirtyState.didNotConverge()) {
			System.out.println("Skipping '" + input + "' because it does not converge.  Run {@code spotlessDiagnose} to understand why");
		} else {
			Path parentDir = output.toPath().getParent();
			if (parentDir == null) {
				throw new IllegalStateException("Every file has a parent folder.");
			}
			Files.createDirectories(parentDir);
			// Need to copy the original file to the tmp location just to remember the file attributes
			Files.copy(input.toPath(), output.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
			dirtyState.writeCanonicalTo(output);
		}

		return false;
	}

	public String getEncoding() { return encoding; }
    
	private Formatter buildFormatter(ArrayList<FormatterStep> steps) {
		return Formatter.builder()
				.lineEndingsPolicy(lineEndingPolicy)
				.encoding(Charset.forName(encoding))
				.rootDir(rootDir)
				.steps(steps)
				.exceptionPolicy(exceptionPolicy)
				.build();
	}
}