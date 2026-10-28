import java.nio.file.;
import java.io.IOException;
import java.util.;
public class Main {
public static void main(String[] args) throws IOException {
Files.write(Paths.get("log1.txt"), Arrays.asList(
"Application started",
"Error occurred",
"Application running"
));
    Files.write(Paths.get("log2.txt"), Arrays.asList(
        "User logged in",
        "Error found",
        "Application stopped"
    ));

    String[] files = {"log1.txt", "log2.txt"};
    String keyword = "Error";

    int totalLines = 0;
    int keywordLines = 0;

    for (String file : files) {
        Path path = Paths.get(file);

        List<String> lines = Files.readAllLines(path);

        for (String line : lines) {
            totalLines++;

            if (line.contains(keyword)) {
                keywordLines++;
            }
        }
        long size = Files.size(path);
        System.out.println("File: " + file);
        System.out.println("Size: " + size + " bytes");
    }

    System.out.println("Total lines: " + totalLines);
    System.out.println("Lines containing keyword: " + keywordLines);
}
}
