package tools;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Cross-platform builder for JBlackJack — no shell, no Maven/Gradle.
 *
 * Runs on any device with a JDK, from the project root:
 *
 * <pre>
 *   java tools/Build.java     compile + bundle resources -> JBlackJack.jar
 * </pre>
 *
 * It does exactly what the two `javac` + `jar` commands in the README do, but in
 * one short command (no long, easy-to-truncate line). The resulting jar is
 * self-contained and runnable anywhere with:
 *
 * <pre>
 *   java -jar JBlackJack.jar
 * </pre>
 *
 * @author Mercuri Lorenzo,
 * @author Matricola: 2145783.
 */
public class Build {
	private static final String MAIN_CLASS = "view.JBlackJack";
	private static final String RELEASE = "17";

	public static void main(String[] args) throws IOException {
		Path root = Paths.get("").toAbsolutePath();
		Path src = root.resolve("src");
		Path classes = root.resolve("build").resolve("classes");
		Path jar = root.resolve("JBlackJack.jar");

		if (!Files.isDirectory(src)) {
			fail("Run this from the project root (no 'src' folder found in " + root + ").");
		}

		// 1) fresh output directory.
		deleteTree(root.resolve("build"));
		Files.createDirectories(classes);

		// 2) collect and compile every source.
		List<String> sources;
		try (Stream<Path> walk = Files.walk(src)) {
			sources = walk.filter(p -> p.toString().endsWith(".java"))
			              .map(Path::toString)
			              .collect(Collectors.toList());
		}
		if (sources.isEmpty()) {
			fail("No .java sources found under " + src + ".");
		}

		javax.tools.JavaCompiler compiler = javax.tools.ToolProvider.getSystemJavaCompiler();
		if (compiler == null) {
			fail("No Java compiler available — please run this with a JDK (not just a JRE).");
		}

		List<String> javacArgs = new ArrayList<>();
		javacArgs.add("--release");
		javacArgs.add(RELEASE);
		javacArgs.add("-d");
		javacArgs.add(classes.toString());
		javacArgs.addAll(sources);

		System.out.println("Compiling " + sources.size() + " sources (Java " + RELEASE + ") ...");
		if (compiler.run(null, null, System.err, javacArgs.toArray(new String[0])) != 0) {
			fail("Compilation failed.");
		}

		// 3) package the runnable jar: classes + resources straight from src.
		java.util.spi.ToolProvider jarTool = java.util.spi.ToolProvider.findFirst("jar")
		        .orElseThrow(() -> new IllegalStateException("The 'jar' tool is not available in this JDK."));
		int rc = jarTool.run(System.out, System.err,
		        "--create", "--file", jar.toString(),
		        "--main-class", MAIN_CLASS,
		        "-C", classes.toString(), ".",
		        "-C", src.toString(), "img",
		        "-C", src.toString(), "sounds",
		        "-C", src.toString(), "accounts.txt");
		if (rc != 0) {
			fail("Packing the jar failed.");
		}

		System.out.println();
		System.out.println("Build complete: " + jar);
		System.out.println("Run on any device:  java -jar " + jar.getFileName());
	}

	private static void deleteTree(Path dir) throws IOException {
		if (!Files.exists(dir)) {
			return;
		}
		Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}
			@Override
			public FileVisitResult postVisitDirectory(Path d, IOException e) throws IOException {
				Files.delete(d);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	private static void fail(String message) {
		System.err.println("Build error: " + message);
		System.exit(1);
	}
}
