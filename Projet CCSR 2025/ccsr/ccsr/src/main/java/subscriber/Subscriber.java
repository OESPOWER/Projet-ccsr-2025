package subscriber;

import org.apache.kafka.clients.consumer.*;
import utils.EmailSender;
import utils.GoogleSearchService;
import utils.SubscriptionManager;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

public class Subscriber {
    private KafkaConsumer<String, String> consumer;
    private SubscriptionManager subscriptionManager;

    public Subscriber(String bootstrapServers, SubscriptionManager subscriptionManager) {
        this.subscriptionManager = subscriptionManager;

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "alert-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        this.consumer = new KafkaConsumer<>(props);
    }

    public void subscribeToTopic(String topic) {
        consumer.subscribe(Collections.singletonList(topic));
        System.out.println("Abonné au topic : " + topic);

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

            for (ConsumerRecord<String, String> record : records) {
                String[] parts = record.value().split("\\|");
                String alert = parts[0];
                String keywords = parts[1];

                System.out.println("📩 Message reçu: " + alert + " (Mots-clés: " + keywords + ")");

                // Récupérer les abonnés pour les mots-clés de l'alerte
                Set<String> matchingEmails = subscriptionManager.getSubscribersForKeywords(keywords);
                if (!matchingEmails.isEmpty()) {
                    System.out.println("🔍 Emails trouvés pour les mots-clés " + keywords + " : " + matchingEmails);

                    // Recherche Google pour les mots-clés
                    try {
                        List<String> searchResults = GoogleSearchService.search(keywords);
                        String emailContent = alert + "\n\nRésultats de recherche :\n";
                        for (String result : searchResults) {
                            emailContent += result + "\n";
                        }

                        for (String email : matchingEmails) {
                            EmailSender.sendEmail(email, "Alerte reçue", emailContent);
                            System.out.println("📧 Email envoyé à " + email + " pour l'alerte : " + alert);
                        }
                    } catch (IOException e) {
                        System.err.println("❌ Erreur lors de la recherche Google : " + e.getMessage());
                    }
                } else {
                    System.out.println("❌ Aucun abonné trouvé pour les mots-clés : " + keywords);
                }
            }
        }
    }
}