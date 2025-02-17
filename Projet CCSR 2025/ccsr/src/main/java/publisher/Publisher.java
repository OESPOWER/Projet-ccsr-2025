package publisher;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class Publisher {
    private final KafkaProducer<String, String> producer;
    private final String topic;

    public Publisher(String bootstrapServers, String topic) {
        this.topic = topic;
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        this.producer = new KafkaProducer<>(props);
    }

    public void publishAlert(String alert, String keywords) {
        String message = alert + "|" + keywords; // Combiner l'alerte et les mots-clés
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);
        producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                System.out.println("✅ Alerte envoyée : " + alert);
            } else {
                System.err.println("❌ Erreur d'envoi : " + exception.getMessage());
            }
        });
    }

    public void close() {
        producer.close();
    }
}