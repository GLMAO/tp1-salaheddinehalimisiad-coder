package org.emp.gl.clients;

import org.emp.gl.timer.service.TimerChangeListener;
import org.emp.gl.timer.service.TimerService;
import org.emp.gl.lookup.Lookup;

import java.beans.PropertyChangeEvent;

public class CompteARebours implements TimerChangeListener {
    private String name;
    private TimerService timerService;
    private int counter;

    public CompteARebours(String name, int counter) {
        this.name = name;
        this.counter = counter;
        this.timerService = Lookup.getInstance().getService(TimerService.class);

        if (this.timerService != null) {
            this.timerService.addTimeChangeListener(this);
        }

        System.out.println("Compte à rebours " + name + " initialisé avec valeur = " + counter + "!");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("seconde".equals(evt.getPropertyName())) {
            // Afficher la valeur actuelle
            System.out.println(name + " : " + counter);

            // Si le compteur arrive à 0, se désinscrire
            if (counter == 0) {
                if (timerService != null) {
                    timerService.removeTimeChangeListener(this);
                }
                System.out.println(name + " TERMINÉ et désinscrit!");
                return;
            }

            // Décrémenter le compteur
            counter--;
        }
    }
}