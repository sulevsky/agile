package com.ef;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import picocli.CommandLine;

@CommandLine.Command(name = "Parser", header = "%n@|green Parse logs|@")
public class Main implements Runnable {
    @CommandLine.Option(names = {"--startDate"}, required = true,
                        description = "Start date to analyze logs. yyyy-MM-dd.HH:mm:ss format. Example `2017-01-01.13:00:00`")
    private String startDate;

    @CommandLine.Option(names = {"--duration"}, required = true, description = "Duration (hourly, daily)")
    private String duration;

    @CommandLine.Option(names = {"--threshold"}, required = true, description = "Threshold. Integer")
    private Integer threshold;

    public void run() {
        LocalDateTime parsedStartDate = parseStartDate(startDate);
        ChronoUnit parsedDuration = parseDuration(duration);

        List<Entry> entries = FileReader.readEntries("access.log");

        Processor processor = new Processor();
        processor.connectToDb();
        processor.saveEntries(entries);
        processor.processEntriesForThreshold(parsedStartDate, parsedDuration, threshold);
        processor.closeDbConnection();
    }

    public static void main(String... args) {
        CommandLine.run(new Main(), args);
    }

    private LocalDateTime parseStartDate(String unparsedStartDate) {
        return LocalDateTime.parse(unparsedStartDate, DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss"));
    }

    private ChronoUnit parseDuration(String unparsedDuration) {
        switch (unparsedDuration) {
            case "hourly":
            case "HOURLY":
                return ChronoUnit.HOURS;
            case "daily":
            case "DAILY":
                return ChronoUnit.DAYS;
            default:
                throw new RuntimeException("Incorrect duration format");
        }
    }
}
