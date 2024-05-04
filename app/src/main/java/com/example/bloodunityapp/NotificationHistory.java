package com.example.bloodunityapp;

import java.util.ArrayList;
import java.util.List;

public class NotificationHistory {
    private static List<String> notifications = new ArrayList<>();

    public static void addNotification(String notification) {
        notifications.add(notification);
    }

    public static List<String> getNotifications() {
        return notifications;
    }

    
}
