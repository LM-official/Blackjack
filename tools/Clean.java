package tools;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;

/**
 * Cross-platform cleanup for JBlackJack — no shell, no git, no build tool.
 *
 * Run it with the JDK on any device (Windows, macOS, Linux), from the project root:
 *
 * <pre>
 *   java tools/Clean.java            remove build output + scratch + OS cruft
 *   java tools/Clean.java --deep     ALSO remove a legacy ~/.JBlackJack from older versions
 *   java tools/Clean.java --dry-run  show what would be removed, delete nothing
 * </pre>
 *
 * It only ever deletes build artifacts and throwaway files; {@code src/} and the
 * {@code .git/} directory are never touched.
 *
 * @author Mercuri Lorenzo,
 * @author Matricola: 2145783.
 */
public class Clean {
	private static boolean dryRun = false;
	private static int removed = 0;

	public static void main(String[] args) throws IOException {
		boolean deep = false;
		for (String a : args) {
			switch (a) {
				case "--dry-run":
				case "-n":
					dryRun = true;
					break;
				case "--deep":
					deep = true;
					break;
				case "--help":
				case "-h":
					help();
					return;
				default:
					System.err.println("Unknown option: " + a);
					help();
					System.exit(1);
			}
		}

		Path root = Paths.get("").toAbsolutePath();
		System.out.println((dryRun ? "[dry-run] " : "") + "Cleaning " + root);

		// 1) build output produced by the README build commands / Eclipse.
		remove(root.resolve("bin"));
		remove(root.resolve("build"));
		remove(root.resolve("JBlackJack.jar"));

		// 2) throwaway files that can land in the project root.
		removeRootScratch(root);

		// 3) OS/editor cruft anywhere in the tree (but never inside .git).
		removeOsCruft(root);

		// 4) optional: remove a legacy ~/.JBlackJack left by older versions
		//    (accounts now live in the project's src/accounts.txt).
		if (deep) {
			remove(Paths.get(System.getProperty("user.home"), ".JBlackJack"));
		}

		System.out.println((dryRun ? "Would remove " : "Removed ") + removed + " item(s).");
		if (!deep) {
			System.out.println("Tip: add --deep to also remove a legacy ~/.JBlackJack from older versions");
		}
	}

	/** Deletes a file or a whole directory tree (counts it as one removed item). */
	private static void remove(Path path) throws IOException {
		if (!Files.exists(path)) {
			return;
		}
		System.out.println("  - " + path);
		removed++;
		if (dryRun) {
			return;
		}
		if (Files.isDirectory(path)) {
			deleteTree(path);
		} else {
			Files.delete(path);
		}
	}

	/** Removes loose scratch files (logs, source lists, JVM crash dumps) from the root. */
	private static void removeRootScratch(Path root) throws IOException {
		try (Stream<Path> entries = Files.list(root)) {
			for (Path p : (Iterable<Path>) entries::iterator) {
				if (!Files.isRegularFile(p)) {
					continue;
				}
				String name = p.getFileName().toString();
				boolean scratch = name.endsWith(".log") || name.endsWith(".out") || name.endsWith(".err")
						|| name.equals("sources.txt") || name.equals(".sources.tmp")
						|| name.startsWith("hs_err_pid") || name.startsWith("replay_pid");
				if (scratch) {
					remove(p);
				}
			}
		}
	}

	/** Removes macOS/editor artifacts (.DS_Store, ._*) without descending into .git. */
	private static void removeOsCruft(Path root) throws IOException {
		Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
				return dir.getFileName().toString().equals(".git") ? FileVisitResult.SKIP_SUBTREE : FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				String name = file.getFileName().toString();
				if (name.equals(".DS_Store") || name.startsWith("._")) {
					remove(file);
				}
				return FileVisitResult.CONTINUE;
			}
		});
	}

	private static void deleteTree(Path dir) throws IOException {
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

	private static void help() {
		System.out.println("Usage: java tools/Clean.java [--deep] [--dry-run]");
		System.out.println("  (no args)   remove bin/, build/, JBlackJack.jar, scratch files and OS cruft");
		System.out.println("  --deep      also delete a legacy ~/.JBlackJack (accounts now live in src/accounts.txt)");
		System.out.println("  --dry-run   list what would be removed without deleting anything");
	}
}