package org.emp.gl.clients;

import org.emp.gl.timer.service.TimerChangeListener;
import org.emp.gl.timer.service.TimerService;
import org.emp.gl.lookup.Lookup;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;

public class Horloge implements TimerChangeListener {
    private String name;
    private TimerService timerService;
    private JFrame frame;
    private ClockPanel clockPanel;

    // Constructeur avec lookup
    public Horloge(String name) {
        this(name, Lookup.getInstance().getService(TimerService.class));
    }

    // Constructeur avec injection de dépendance
    public Horloge(String name, TimerService timerService) {
        this.name = name;
        this.timerService = timerService;

        if (this.timerService != null) {
            this.timerService.addTimeChangeListener(this);
        }

        initializeGUI();
        System.out.println("Horloge " + name + " initialisée!");
    }

    private void initializeGUI() {
        // Création de la fenêtre
        frame = new JFrame("Horloge - " + name);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);

        // Panel de l'horloge
        clockPanel = new ClockPanel();
        clockPanel.setPreferredSize(new Dimension(300, 300));

        // Panel d'information
        JPanel infoPanel = new JPanel();
        infoPanel.add(new JLabel("Horloge: " + name));

        // Assemblage
        frame.setLayout(new BorderLayout());
        frame.add(clockPanel, BorderLayout.CENTER);
        frame.add(infoPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null); // Centrer la fenêtre
        frame.setVisible(true);
    }

    private void updateClockDisplay() {
        if (timerService != null) {
            int hours = timerService.getHeures();
            int minutes = timerService.getMinutes();
            int seconds = timerService.getSecondes();
            clockPanel.setTime(hours, minutes, seconds);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("seconde".equals(evt.getPropertyName())) {
            // Mise à jour dans l'EDT (Event Dispatch Thread) pour Swing
            SwingUtilities.invokeLater(this::updateClockDisplay);
        }
    }

    // Classe interne pour l'affichage graphique de l'horloge
    private class ClockPanel extends JPanel {
        private int hours;
        private int minutes;
        private int seconds;

        public void setTime(int hours, int minutes, int seconds) {
            this.hours = hours;
            this.minutes = minutes;
            this.seconds = seconds;
            repaint(); // Redessiner le composant
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int width = getWidth();
            int height = getHeight();
            int centerX = width / 2;
            int centerY = height / 2;
            int radius = Math.min(width, height) / 2 - 20;

            // Fond de l'horloge
            g.setColor(Color.WHITE);
            g.fillOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);
            g.setColor(Color.BLACK);
            g.drawOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);

            // Dessiner les marques des heures
            drawHourMarks(g, centerX, centerY, radius);

            // Dessiner les aiguilles
            drawHand(g, centerX, centerY, hours % 12 * 30 + minutes / 2, radius * 0.5, Color.BLACK, 3); // Aiguille heures
            drawHand(g, centerX, centerY, minutes * 6, radius * 0.7, Color.BLUE, 2); // Aiguille minutes
            drawHand(g, centerX, centerY, seconds * 6, radius * 0.9, Color.RED, 1); // Aiguille secondes

            // Centre de l'horloge
            g.setColor(Color.RED);
            g.fillOval(centerX - 5, centerY - 5, 10, 10);
        }

        private void drawHourMarks(Graphics g, int centerX, int centerY, int radius) {
            g.setColor(Color.BLACK);
            for (int i = 0; i < 12; i++) {
                double angle = Math.toRadians(i * 30 - 90);
                int x1 = (int) (centerX + (radius - 10) * Math.cos(angle));
                int y1 = (int) (centerY + (radius - 10) * Math.sin(angle));
                int x2 = (int) (centerX + radius * Math.cos(angle));
                int y2 = (int) (centerY + radius * Math.sin(angle));
                g.drawLine(x1, y1, x2, y2);
            }
        }

        private void drawHand(Graphics g, int centerX, int centerY, double angle, double length, Color color, int thickness) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(thickness));

            double radianAngle = Math.toRadians(angle - 90);
            int endX = (int) (centerX + length * Math.cos(radianAngle));
            int endY = (int) (centerY + length * Math.sin(radianAngle));

            g2d.drawLine(centerX, centerY, endX, endY);
        }
    }
}