package com.ef;

import java.time.LocalDateTime;

public class Entry {
    private final LocalDateTime dateTime;
    private final String ipAddress;
    private final String httpHead;
    private final Integer httpStatusCode;
    private final String userAgent;

    public Entry(LocalDateTime dateTime, String ipAddress, String httpHead, Integer httpStatusCode,
                 String userAgent) {
        this.dateTime = dateTime;
        this.ipAddress = ipAddress;
        this.httpHead = httpHead;
        this.httpStatusCode = httpStatusCode;
        this.userAgent = userAgent;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getHttpHead() {
        return httpHead;
    }

    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getUserAgent() {
        return userAgent;
    }

    @Override
    public String toString() {
        return "Entry{" +
               "dateTime=" + dateTime +
               ", ipAddress='" + ipAddress + '\'' +
               ", httpHead='" + httpHead + '\'' +
               ", httpStatusCode=" + httpStatusCode +
               ", userAgent='" + userAgent + '\'' +
               '}';
    }
}
