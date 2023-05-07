/*
 * Copyright 2023 DiffPlug
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.diffplug.bazel.spotless;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.diffplug.spotless.extra.integration.DiffMessageFormatter;

public class SpotlessCheck {
	public static void main(String[] args) throws IOException {
		int argPtr = 0;

		String configFile = args[argPtr++];
		String reportFile = args[argPtr++];
		String subpath = args[argPtr++];
		String targetName = args[argPtr++];
		List<String> sourceFiles = Arrays.asList(Arrays.copyOfRange(args, argPtr, args.length));

		Path workspaceDir = new File(".").toPath();
		Path outputDirectory = new File(args[0]).toPath().getParent();

		SpotlessFormatter formatter = new SpotlessFormatter(workspaceDir, configFile);
		List<File> failedFiles = formatter.processFiles(outputDirectory, sourceFiles);

		try (FileWriter writer = new FileWriter(reportFile)) {
			for (File f : failedFiles) {
				writer.write("Failed " + f);
			}
		}

		if (failedFiles.isEmpty()) {
			System.exit(0);
		}

		Collections.sort(failedFiles);
		System.out.println(DiffMessageFormatter.builder()
				.runToFix("To fix, run:\n\nbazel run //" + subpath + ":" + targetName + ".apply\n")
				// .runToFix(getRunToFixMessage().get())
				.formatterFolder(
						workspaceDir,
						outputDirectory,
						formatter.getEncoding())
				.problemFiles(failedFiles)
				.getMessage());
		System.exit(1);
	}
}
