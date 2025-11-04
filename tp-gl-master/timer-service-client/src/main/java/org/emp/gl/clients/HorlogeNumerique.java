package org.emp.gl.clients;

import org.emp.gl.timer.service.TimerChangeListener;
import org.emp.gl.timer.service.TimerService;
import org.emp.gl.lookup.Lookup;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;

public class HorlogeNumerique implements TimerChangeListener {
    private String name;
    private TimerService timerService;
    private JFrame frame;
    private JLabel timeLabel;

    // Constructeur avec lookup
    public HorlogeNumerique(String name) {
        this(name, Lookup.getInstance().getService(TimerService.class));
    }

    // Constructeur avec injection de dépendance
    public HorlogeNumerique(String name, TimerService timerService) {
        this.name = name;
        this.timerService = timerService;

        if (this.timerService != null) {
            this.timerService.addTimeChangeListener(this);
        }

        initializeGUI();
        System.out.println("Horloge Numérique " + name + " initialisée!");
    }

    private void initializeGUI() {
        // Création de la fenêtre
        frame = new JFrame("Horloge Numérique - " + name);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);

        // Configuration du label pour l'affichage du temps
        timeLabel = new JLabel("00:00:00", SwingConstants.CENTER);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 32));
        timeLabel.setForeground(Color.BLUE);
        timeLabel.setBackground(Color.BLACK);
        timeLabel.setOpaque(true);
        timeLabel.setPreferredSize(new Dimension(200, 80));

        // Panel d'information
        JPanel infoPanel = new JPanel();
        infoPanel.add(new JLabel("Horloge Numérique: " + name));

        // Assemblage
        frame.setLayout(new BorderLayout());
        frame.add(timeLabel, BorderLayout.CENTER);
        frame.add(infoPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null); // Centrer la fenêtre
        frame.setVisible(true);

        // Afficher l'heure initiale
        updateTimeDisplay();
    }

    private void updateTimeDisplay() {
        if (timerService != null) {
            int hours = timerService.getHeures();
            int minutes = timerService.getMinutes();
            int seconds = timerService.getSecondes();

            // Formatage HH:MM:SS
            String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            timeLabel.setText(timeString);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("seconde".equals(evt.getPropertyName())) {
            // Mise à jour dans l'EDT (Event Dispatch Thread) pour Swing
            SwingUtilities.invokeLater(this::updateTimeDisplay);
        }
    }
}