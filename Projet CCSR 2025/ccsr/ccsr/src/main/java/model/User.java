package model;

import java.util.List;

public class User {
    private String email;
    private List<String> keywords;
    private String alertFrequency; // "daily", "weekly", "immediate"

    public User(String email, List<String> keywords, String alertFrequency) {
        this.email = email;
        this.keywords = keywords;
        this.alertFrequency = alertFrequency;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public String getAlertFrequency() {
        return alertFrequency;
    }

    public void setAlertFrequency(String alertFrequency) {
        this.alertFrequency = alertFrequency;
    }
}