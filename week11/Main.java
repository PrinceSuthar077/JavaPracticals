import java.nio.file.;
import java.nio.file.attribute.BasicFileAttributes;
import java.io.;
import java.time.Instant;
public class Main {
public static void main(String[] args) throws IOException {
Path folder = Paths.get("MyFolder");
Path report = Paths.get("report.txt");
    try (BufferedWriter writer = Files.newBufferedWriter(report);
         var paths = Files.walk(folder)) {

        for (Path path : (Iterable<Path>) paths::iterator) {
            if (Files.isRegularFile(path)) {
                BasicFileAttributes attr = Files.readAttributes(
                    path, BasicFileAttributes.class
                );

                String name = path.getFileName().toString();
                long size = attr.size();
                Instant modified = attr.lastModifiedTime().toInstant();

                String line = "Name: " + name
                    + ", Size: " + size + " bytes"
                    + ", Modified: " + modified;

                writer.write(line);
                writer.newLine();

                System.out.println(line);
            }
        }
    }

    System.out.println("Report saved to report.txt");
}
}
