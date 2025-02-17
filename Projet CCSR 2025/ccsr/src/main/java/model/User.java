package model;

import java.util.Set;

public class User {
    private String email;
    private Set<String> keywords;
    private String alertFrequency; // "immediate", "daily", "weekly"

    public User(String email, Set<String> keywords, String alertFrequency) {
        this.email = email;
        this.keywords = keywords;
        this.alertFrequency = alertFrequency;
    }

    public String getEmail() {
        return email;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    public String getAlertFrequency() {
        return alertFrequency;
    }

    public void setAlertFrequency(String alertFrequency) {
        this.alertFrequency = alertFrequency;
    }
}