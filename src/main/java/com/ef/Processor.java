package com.ef;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Processor {
    private static final String USER = "root";
    private static final String PASSWORD = "mypassword";
    private static final String URL = "localhost:3306";
    private static final String DB = "test";
    private static final int BATCH_SIZE = 2000;

    private Connection connection;

    public void saveEntries(List<Entry> entries) {
        try {
            System.out.println("Starting save all entries to DB");
            String sql = "insert into log_entry(date_time, ip_address, http_head, http_status_code, user_agent) " +
                         "values (?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            int count = 0;

            for (Entry entry : entries) {
                preparedStatement.setTimestamp(1, Timestamp.from(entry.getDateTime().toInstant(ZoneOffset.UTC)));
                preparedStatement.setString(2, entry.getIpAddress());
                preparedStatement.setString(3, entry.getHttpHead());
                preparedStatement.setInt(4, entry.getHttpStatusCode());
                preparedStatement.setString(5, entry.getUserAgent());
                preparedStatement.addBatch();
                count++;
                if (count % BATCH_SIZE == 0) {
                    preparedStatement.executeBatch();
                    connection.commit();
                    System.out.println("Batch of size " + BATCH_SIZE + " saved");
                }
            }
            preparedStatement.executeBatch();
            preparedStatement.close();
            connection.commit();
            System.out.println("Saving all entries to DB finished, save " + entries.size() + " entries");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void processEntriesForThreshold(LocalDateTime startDate, ChronoUnit duration, Integer threshold) {
        Map<String, Integer> ipToRequestsNum = findIps(startDate, duration, threshold);
        saveBlockedIps(ipToRequestsNum, threshold, duration);
        printBlockedIps(ipToRequestsNum);
    }

    private void printBlockedIps(Map<String, Integer> ipToRequestsNum) {
        System.out.println("IPs exceeded threshold");
        ipToRequestsNum.keySet()
                       .forEach(System.out::println);
    }

    private void saveBlockedIps(Map<String, Integer> ipToRequestsNum, Integer threshold, ChronoUnit duration) {
        try {
            System.out.println("Starting saving blocked IPs");
            String sql = "insert into blocked_ip(ip_address, comment) " +
                         "values (?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            for (Map.Entry<String, Integer> entry : ipToRequestsNum.entrySet()) {
                preparedStatement.setString(1, entry.getKey());
                String comment = String.format(
                        "Ip blocked due to exceeded number of calls: %d calls, with threshold %d, for duration %s",
                        entry.getValue(), threshold, duration);
                preparedStatement.setString(2, comment);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            preparedStatement.close();
            connection.commit();
            System.out.println("Saving all blocked IPs to DB finished, save " + ipToRequestsNum.size() + " ips");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Integer> findIps(LocalDateTime startDate, ChronoUnit duration, Integer threshold) {
        Map<String, Integer> ipToRequestsNum = new HashMap<>();
        System.out.println("Fetching IPs with number of requests more than: " + threshold +
                           ", from " + startDate + ", for duration: " + duration);
        String sql = "select ip_address, count(ip_address) as requests_number " +
                     "from log_entry " +
                     "where  ? <= date_time " +
                     "and date_time < ? " +
                     "group by ip_address " +
                     "having requests_number > ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setTimestamp(1, Timestamp.from(startDate.toInstant(ZoneOffset.UTC)));
            preparedStatement.setTimestamp(2, Timestamp.from(startDate.plus(1, duration).toInstant(ZoneOffset.UTC)));
            preparedStatement.setInt(3, threshold);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String ip = resultSet.getString("ip_address");
                int requestsNum = resultSet.getInt("requests_number");
                ipToRequestsNum.put(ip, requestsNum);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ipToRequestsNum;
    }

    public void closeDbConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void connectToDb() {
        try {
            connection = DriverManager
                    .getConnection(String.format("jdbc:mysql://%s/%s", URL, DB), USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Connection Failed!", e);
        }

        if (connection != null) {
            System.out.println("Database connected");
        } else {
            throw new RuntimeException("Failed to make connection!");
        }
    }
}
