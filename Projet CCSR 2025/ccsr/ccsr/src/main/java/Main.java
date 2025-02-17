import subscriber.Subscriber;
import utils.SubscriptionManager;
import publisher.Publisher;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        SubscriptionManager subscriptionManager = new SubscriptionManager();
        Publisher publisher = new Publisher("localhost:9092", "alerts-topic");

        // Lancer le consommateur Kafka dans un thread
        Thread consumerThread = new Thread(() -> {
            Subscriber subscriber = new Subscriber("localhost:9092", subscriptionManager);
            subscriber.subscribeToTopic("alerts-topic");
        });
        consumerThread.start();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== MENU ===");
            System.out.println("1️⃣ S'enregistrer");
            System.out.println("2️⃣ Publier une alerte");
            System.out.println("3️⃣ Quitter");

            System.out.print("👉 Choisissez une option : ");

            // Vérifier si l'entrée est un entier
            if (!scanner.hasNextInt()) {
                System.out.println("⚠️ Option invalide, veuillez entrer un nombre entier.");
                scanner.nextLine();  // Consommer l'entrée invalide
                continue;  // Demander à l'utilisateur de saisir une nouvelle option
            }

            int option = scanner.nextInt();
            scanner.nextLine();  // Consommer le retour à la ligne

            switch (option) {
                case 1:
                    System.out.print("Entrez votre email : ");
                    String email = scanner.nextLine().toLowerCase();
                    System.out.print("Entrez vos mots-clés (séparés par des virgules ou des espaces) : ");
                    String keywordsInput = scanner.nextLine();
                    subscriptionManager.addSubscriber(email, keywordsInput); // Passer la chaîne brute
                    break;

                case 2:
                    System.out.print("Entrez l'alerte à publier : ");
                    String alert = scanner.nextLine();
                    System.out.print("Entrez les mots-clés associés (séparés par des virgules ou des espaces) : ");
                    String alertKeywords = scanner.nextLine();
                    publisher.publishAlert(alert, alertKeywords); // Passer les mots-clés bruts
                    break;
                case 3:
                    System.out.println("👋 Au revoir !");
                    publisher.close();
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("⚠️ Option invalide !");
            }
        }
    }
}