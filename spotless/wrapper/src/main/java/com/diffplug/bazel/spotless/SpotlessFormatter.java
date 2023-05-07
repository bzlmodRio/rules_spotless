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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import com.diffplug.bazel.spotless.config.SpotlessConfig;
import com.diffplug.bazel.spotless.config.SpotlessConfigOrBuilder;
import com.diffplug.spotless.PaddedCell;

import com.google.protobuf.TextFormat;

public class SpotlessFormatter {
	private final Path rootDir;
	private final BaseFormatter formatter;

	public SpotlessFormatter(Path rootDir, String configFile) throws IOException {
		this.rootDir = rootDir;

		BufferedReader br = new BufferedReader(new FileReader(configFile));
		SpotlessConfig.Builder builder = SpotlessConfig.newBuilder();
		TextFormat.merge(br, builder);
		SpotlessConfig config = builder.build();

		switch(config.getLanguageConfigCase()) {
			case GROOVY: {
				formatter = new GroovyFormatter(config.getGenericConfig(), config.getGroovy());
				break;
			}
			case JAVA: {
				formatter = new JavaFormatter(config.getGenericConfig(), config.getJava());
				break;
			}
			case JSON: {
				formatter = new JsonFormatter(config.getGenericConfig(), config.getJson());
				break;
			}
			case YAML: {
				formatter = new YamlFormatter(config.getGenericConfig(), config.getYaml());
				break;
			}
			case LANGUAGECONFIG_NOT_SET:
				throw new IllegalArgumentException("Unknown oneof");
			default:
				throw new IllegalArgumentException("Unknown oneof (in default)");
		}
	}

	public List<File> processFiles(Path outputDirectory, List<String> inputFiles) throws IOException {
		List<File> failedFiles = new ArrayList<>();

		for (String inputFile : inputFiles) {
			File input = new File(rootDir.toFile(), inputFile);
			File output = new File(outputDirectory.toFile(), inputFile);

			Path parentDir = output.toPath().getParent();
			if (parentDir == null) {
				throw new IllegalStateException("Every file has a parent folder.");
			}
			Files.createDirectories(parentDir);

			if (!processFile(input, output)) {
				failedFiles.add(input);
			}
		}

		return failedFiles;
	}

	public boolean processFile(File input, File output) throws IOException {
		PaddedCell.DirtyState dirtyState = PaddedCell.calculateDirtyState(formatter.buildFormatter(rootDir), input);
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

	public String getEncoding() {
		return formatter.getEncoding();
	}
}
