package org.emp.gl.core.launcher;

import org.emp.gl.clients.CompteARebours;
import org.emp.gl.clients.Horloge;
import org.emp.gl.clients.HorlogeNumerique;
import org.emp.gl.time.service.impl.DummyTimeServiceImpl;
import org.emp.gl.lookup.Lookup;
import org.emp.gl.timer.service.TimerService;

import java.util.Random;

/**
 * Application principale pour tester le service de timer
 * avec le pattern Observer et interfaces graphiques
 */
public class App {

    public static void main(String[] args) {
        long durationSeconds = -1;

        // Gestion des arguments de ligne de commande
        if (args != null && args.length > 0) {
            try {
                durationSeconds = Long.parseLong(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Argument non numérique, exécution indéfinie.");
            }
        }

        testDuTimeService(durationSeconds);
    }

    private static void testDuTimeService(long durationSeconds) {
        System.out.println("=== DÉMARRAGE DE L'APPLICATION ===");

        // Configuration du service dans le Lookup
        Lookup.getInstance().addService(
                TimerService.class,
                new DummyTimeServiceImpl()
        );

        System.out.println("Service TimerService initialisé avec DummyTimeServiceImpl");

        // Création de 2 horloges analogiques
        System.out.println("Création des horloges analogiques...");
        new Horloge("Analogique 1");
        new Horloge("Analogique 2");

        // Création d'une horloge numérique
        System.out.println("Création de l'horloge numérique...");
        new HorlogeNumerique("Numérique 1");

        // Création d'un compte à rebours avec valeur 5
        System.out.println("Création du compte à rebours C-5...");
        new CompteARebours("C-5", 5);

        // Création de 10 comptes à rebours avec valeurs aléatoires entre 10 et 20
        System.out.println("Création de 10 comptes à rebours aléatoires...");
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            int value = 10 + rnd.nextInt(11); // 10..20 inclus
            new CompteARebours("C-R" + i, value);
        }

        System.out.println("Tous les composants ont été initialisés!");
        System.out.println("=== L'APPLICATION EST MAINTENANT FONCTIONNELLE ===");
        System.out.println();

        // Maintenir le thread principal actif pour que les timers fonctionnent
        try {
            if (durationSeconds > 0) {
                System.out.println("Application démarrée pour " + durationSeconds + " secondes...");
                System.out.println("Les horloges graphiques s'actualiseront chaque seconde.");
                System.out.println("Les comptes à rebours s'afficheront dans la console.");
                Thread.sleep(durationSeconds * 1000L);
                System.out.println("Temps écoulé! Arrêt de l'application.");
            } else {
                System.out.println("Application démarrée indéfiniment");
                System.out.println("Les horloges graphiques s'actualiseront chaque seconde.");
                System.out.println("Les comptes à rebours s'afficheront dans la console.");
                System.out.println("Appuyez sur Ctrl+C pour arrêter.");
                Thread.sleep(Long.MAX_VALUE);
            }
        } catch (InterruptedException e) {
            System.out.println("Application interrompue!");
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Méthode utilitaire pour effacer la console
     */
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Ignorer les erreurs de clear screen
            System.out.println("\n".repeat(50)); // Solution de repli
        }
    }
}