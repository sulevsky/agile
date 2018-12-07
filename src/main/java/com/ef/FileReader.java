package com.ef;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileReader {

    public static List<Entry> readEntries(String path) {
        try (Stream<String> lines = Files.lines(Paths.get(path), Charset.defaultCharset())) {
            return lines.map(FileReader::parseLine)
                        .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Entry parseLine(String line) {
        String[] split = line.split("\\|");
        LocalDateTime dateTime = LocalDateTime.parse(split[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        String ipAddress = split[1];
        String httpHead = trim(split[2]);
        Integer httpStatusCode = Integer.parseInt(split[3]);
        String userAgent = trim(split[4]);
        return new Entry(dateTime, ipAddress, httpHead, httpStatusCode, userAgent);
    }

    private static String trim(String input) {
        return input.substring(1, input.length() - 1);
    }
}
