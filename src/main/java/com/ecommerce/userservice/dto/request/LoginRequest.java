package com.ecommerce.userservice.dto.request;

public record LoginRequest(
        String accountName,
        String password,
        String ipAddress,// Supports both IPv4 and IPv6 addresses
        String loginTimestamp,
        String deviceType,
        String deviceOS,
        String browserName,
        String browserVersion,
        String failureReason
) {
}
