package utils;

import java.util.*;

public class SubscriptionManager {
    // Map pour stocker les mots-clés des abonnés
    private Map<String, Set<String>> subscriberKeywords = new HashMap<>();

    // Ajouter un abonné avec ses mots-clés
    public void addSubscriber(String email, String keywordsInput) {
        if (!subscriberKeywords.containsKey(email)) {
            // Séparer les mots-clés par des virgules ou des espaces
            Set<String> keywords = new HashSet<>();
            for (String keyword : keywordsInput.split("[,\\s]+")) { // Séparer par virgules ou espaces
                keywords.add(keyword.trim().toLowerCase()); // Normaliser chaque mot-clé
            }
            subscriberKeywords.put(email, keywords);
            System.out.println("✅ Abonné ajouté : " + email + " avec les mots-clés : " + keywords);
        } else {
            System.out.println("❌ L'email " + email + " est déjà inscrit.");
        }
    }
    public void setAlertFrequency(String email, String frequency) {
        if (subscriberKeywords.containsKey(email)) {
            // Mettre à jour la fréquence des alertes
            System.out.println("✅ Fréquence des alertes mise à jour pour : " + email);
        } else {
            System.out.println("❌ L'email " + email + " n'est pas un abonné.");
        }
    }

    // Vérifier si un email est un abonné
    public boolean isSubscriber(String email) {
        return subscriberKeywords.containsKey(email);
    }

    public Set<String> getSubscribersForKeywords(String keywordsInput) {
        Set<String> subscribersForKeywords = new HashSet<>();
        // Séparer les mots-clés de l'alerte par des virgules ou des espaces
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
    // Récupérer la liste des abonnés
    public Set<String> getSubscribers() {
        return subscriberKeywords.keySet();
    }
}