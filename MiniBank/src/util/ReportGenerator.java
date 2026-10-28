package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class ReportGenerator {

    public static void generate(Path logsFolder, Path reportFile)
            throws IOException {

        long totalDeposits = 0;
        long totalWithdrawals = 0;

        StringBuilder report = new StringBuilder();

        report.append("===== MiniBank Transaction Report =====\n");

        if (Files.exists(logsFolder) && Files.isDirectory(logsFolder)) {
            try (DirectoryStream<Path> files =
                    Files.newDirectoryStream(logsFolder)) {

                for (Path file : files) {
                    if (!Files.isRegularFile(file)) {
                        continue;
                    }

                    report.append("\nFile: ")
                            .append(file.getFileName())
                            .append("\n");

                    try (BufferedReader reader =
                            Files.newBufferedReader(file)) {

                        String line;

                        while ((line = reader.readLine()) != null) {
                            report.append(line).append("\n");

                            String lowerLine = line.toLowerCase();

                            try {
                                if (lowerLine.startsWith("deposit:")) {
                                    totalDeposits += extractAmount(line);
                                } else if (lowerLine.startsWith("withdrawal:")) {
                                    totalWithdrawals += extractAmount(line);
                                }
                            } catch (NumberFormatException e) {
                                report.append(
                                        "Could not read transaction amount.\n");
                            }
                        }
                    }

                    BasicFileAttributes attributes = Files.readAttributes(
                            file, BasicFileAttributes.class);

                    report.append("File Size: ")
                            .append(attributes.size())
                            .append(" bytes\n");

                    report.append("Last Modified: ")
                            .append(attributes.lastModifiedTime())
                            .append("\n");
                }
            }
        } else {
            report.append("Logs folder does not exist.\n");
        }

        report.append("\nTotal Deposits: ").append(totalDeposits);
        report.append("\nTotal Withdrawals: ").append(totalWithdrawals);
        report.append("\n=======================================\n");

        Files.writeString(reportFile, report.toString());
    }

    private static long extractAmount(String line) {
        int colonIndex = line.indexOf(':');
        int separatorIndex = line.indexOf('|');

        if (colonIndex == -1 || separatorIndex == -1
                || separatorIndex <= colonIndex) {
            throw new NumberFormatException("Invalid transaction format");
        }

        String amountText = line.substring(
                colonIndex + 1, separatorIndex).trim();

        return Long.parseLong(amountText);
    }
}
