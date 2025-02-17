package utils;

import java.util.*;

public class SubscriptionManager {
    // Map pour stocker les mots-clés des abonnés
    private Map<String, Set<String>> subscriberKeywords = new HashMap<>();
    // Map pour stocker la fréquence des alertes
    private Map<String, String> alertFrequencies = new HashMap<>();

    // Ajouter un abonné avec ses mots-clés et sa fréquence d'alertes
    public void addSubscriber(String email, Set<String> keywords, String alertFrequency) {
        if (!subscriberKeywords.containsKey(email)) {
            subscriberKeywords.put(email, keywords);
            alertFrequencies.put(email, alertFrequency);
            System.out.println("✅ Abonné ajouté : " + email + " avec les mots-clés : " + keywords + " et fréquence : " + alertFrequency);
        } else {
            System.out.println("❌ L'email " + email + " est déjà inscrit.");
        }
    }

    // Vérifier si un email est un abonné
    public boolean isSubscriber(String email) {
        return subscriberKeywords.containsKey(email);
    }

    // Récupérer les abonnés pour des mots-clés spécifiques
    public Set<String> getSubscribersForKeywords(String keywordsInput) {
        Set<String> subscribersForKeywords = new HashSet<>();
        String[] alertKeywords = keywordsInput.split("[,\\s]+");

        for (Map.Entry<String, Set<String>> entry : subscriberKeywords.entrySet()) {
            for (String alertKeyword : alertKeywords) {
                String normalizedAlertKeyword = alertKeyword.trim().toLowerCase();
                if (entry.getValue().contains(normalizedAlertKeyword)) {
                    subscribersForKeywords.add(entry.getKey());
                    break; // Pas besoin de vérifier les autres mots-clés pour cet utilisateur
                }
            }
        }
        return subscribersForKeywords;
    }

    // Mettre à jour la fréquence des alertes
    public void setAlertFrequency(String email, String frequency) {
        if (alertFrequencies.containsKey(email)) {
            alertFrequencies.put(email, frequency);
            System.out.println("✅ Fréquence des alertes mise à jour pour : " + email);
        } else {
            System.out.println("❌ L'email " + email + " n'est pas un abonné.");
        }
    }

    // Récupérer la fréquence des alertes
    public String getAlertFrequency(String email) {
        return alertFrequencies.getOrDefault(email, "immediate"); // Par défaut, fréquence immédiate
    }
    // Méthode pour mettre à jour les mots-clés d'un abonné
    public void updateSubscriberKeywords(String email, Set<String> newKeywords) {
        if (subscriberKeywords.containsKey(email)) {
            subscriberKeywords.put(email, newKeywords);
            System.out.println("✅ Mots-clés mis à jour pour : " + email);
        } else {
            System.out.println("❌ L'email " + email + " n'est pas un abonné.");
        }
    }

    // Méthode pour supprimer un abonné
    public void removeSubscriber(String email) {
        if (subscriberKeywords.remove(email) != null) {
            alertFrequencies.remove(email); // Supprimer également la fréquence des alertes
            System.out.println("✅ Abonné supprimé : " + email);
        } else {
            System.out.println("❌ L'email " + email + " n'est pas un abonné.");
        }
    }
}