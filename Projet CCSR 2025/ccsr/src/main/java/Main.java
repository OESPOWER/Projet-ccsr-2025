import subscriber.Subscriber;
import utils.SubscriptionManager;
import publisher.Publisher;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

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
            System.out.println("3️⃣ Modifier la fréquence des alertes");
            System.out.println("4️⃣ Modifier mes mots-clés");
            System.out.println("5️⃣ Se désinscrire");
            System.out.println("6️⃣ Quitter");

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
                    Set<String> keywords = new HashSet<>(Arrays.asList(keywordsInput.split("[,\\s]+")));

                    System.out.println("Choisissez la fréquence des alertes :");
                    System.out.println("1️⃣ Immédiate");
                    System.out.println("2️⃣ Quotidienne");
                    System.out.println("3️⃣ Hebdomadaire");
                    System.out.print("👉 Choisissez une option : ");
                    int frequencyOption = scanner.nextInt();
                    scanner.nextLine(); // Consommer le retour à la ligne

                    String alertFrequency;
                    switch (frequencyOption) {
                        case 1:
                            alertFrequency = "immediate";
                            break;
                        case 2:
                            alertFrequency = "daily";
                            break;
                        case 3:
                            alertFrequency = "weekly";
                            break;
                        default:
                            System.out.println("⚠️ Option invalide, fréquence par défaut : immédiate");
                            alertFrequency = "immediate";
                            break;
                    }

                    subscriptionManager.addSubscriber(email, keywords, alertFrequency);
                    break;

                case 2:
                    System.out.print("Entrez l'alerte à publier : ");
                    String alert = scanner.nextLine();
                    System.out.print("Entrez les mots-clés associés (séparés par des virgules ou des espaces) : ");
                    String alertKeywords = scanner.nextLine();
                    publisher.publishAlert(alert, alertKeywords);
                    break;

                case 3:
                    System.out.print("Entrez votre email : ");
                    String updateEmail = scanner.nextLine().toLowerCase();
                    if (subscriptionManager.isSubscriber(updateEmail)) {
                        System.out.println("Choisissez la nouvelle fréquence des alertes :");
                        System.out.println("1️⃣ Immédiate");
                        System.out.println("2️⃣ Quotidienne");
                        System.out.println("3️⃣ Hebdomadaire");
                        System.out.print("👉 Choisissez une option : ");
                        int newFrequencyOption = scanner.nextInt();
                        scanner.nextLine(); // Consommer le retour à la ligne

                        String newAlertFrequency;
                        switch (newFrequencyOption) {
                            case 1:
                                newAlertFrequency = "immediate";
                                break;
                            case 2:
                                newAlertFrequency = "daily";
                                break;
                            case 3:
                                newAlertFrequency = "weekly";
                                break;
                            default:
                                System.out.println("⚠️ Option invalide, fréquence inchangée.");
                                continue; // Revenir au menu
                        }

                        subscriptionManager.setAlertFrequency(updateEmail, newAlertFrequency);
                    } else {
                        System.out.println("❌ L'email " + updateEmail + " n'est pas un abonné.");
                    }
                    break;

                case 4:
                    System.out.print("Entrez votre email : ");
                    String updateKeywordsEmail = scanner.nextLine().toLowerCase();
                    if (subscriptionManager.isSubscriber(updateKeywordsEmail)) {
                        System.out.print("Entrez vos nouveaux mots-clés (séparés par des virgules ou des espaces) : ");
                        String newKeywordsInput = scanner.nextLine();
                        Set<String> newKeywords = new HashSet<>(Arrays.asList(newKeywordsInput.split("[,\\s]+")));
                        subscriptionManager.updateSubscriberKeywords(updateKeywordsEmail, newKeywords);
                    } else {
                        System.out.println("❌ L'email " + updateKeywordsEmail + " n'est pas un abonné.");
                    }
                    break;

                case 5:
                    System.out.print("Entrez votre email : ");
                    String unsubscribeEmail = scanner.nextLine().toLowerCase();
                    subscriptionManager.removeSubscriber(unsubscribeEmail);
                    break;

                case 6:
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