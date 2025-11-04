package org.emp.gl.time.service.impl;

import org.emp.gl.timer.service.TimerService;
import org.emp.gl.timer.service.TimerChangeListener;

import java.beans.PropertyChangeSupport;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

public class DummyTimeServiceImpl implements TimerService {
    int dixiemeDeSeconde;
    int minutes;
    int secondes;
    int heures;
    private final PropertyChangeSupport propertyChangeSupport;

    public DummyTimeServiceImpl() {
        propertyChangeSupport = new PropertyChangeSupport(this);
        setTimeValues();

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                timeChanged();
            }
        };
        timer.scheduleAtFixedRate(task, 100, 100);
    }

    private void setTimeValues() {
        LocalTime localTime = LocalTime.now();
        setSecondes(localTime.getSecond());
        setMinutes(localTime.getMinute());
        setHeures(localTime.getHour());
        setDixiemeDeSeconde(localTime.getNano() / 100000000);
    }

    @Override
    public void addTimeChangeListener(TimerChangeListener pl) {
        propertyChangeSupport.addPropertyChangeListener(pl);
    }

    @Override
    public void removeTimeChangeListener(TimerChangeListener pl) {
        propertyChangeSupport.removePropertyChangeListener(pl);
    }

    private void timeChanged() {
        setTimeValues();
    }

    public void setDixiemeDeSeconde(int newDixiemeDeSeconde) {
        if (dixiemeDeSeconde == newDixiemeDeSeconde) return;
        int oldValue = dixiemeDeSeconde;
        dixiemeDeSeconde = newDixiemeDeSeconde;
        dixiemeDeSecondesChanged(oldValue, dixiemeDeSeconde);
    }

    private void dixiemeDeSecondesChanged(int oldValue, int newValue) {
        propertyChangeSupport.firePropertyChange(TimerChangeListener.DIXEME_DE_SECONDE_PROP, oldValue, newValue);
    }

    public void setSecondes(int newSecondes) {
        if (secondes == newSecondes) return;
        int oldValue = secondes;
        secondes = newSecondes;
        secondesChanged(oldValue, secondes);
    }

    private void secondesChanged(int oldValue, int secondes) {
        propertyChangeSupport.firePropertyChange(TimerChangeListener.SECONDE_PROP, oldValue, secondes);
    }

    public void setMinutes(int newMinutes) {
        if (minutes == newMinutes) return;
        int oldValue = minutes;
        minutes = newMinutes;
        minutesChanged(oldValue, minutes);
    }

    private void minutesChanged(int oldValue, int minutes) {
        propertyChangeSupport.firePropertyChange(TimerChangeListener.MINUTE_PROP, oldValue, minutes);
    }

    public void setHeures(int newHeures) {
        if (heures == newHeures) return;
        int oldValue = heures;
        heures = newHeures;
        heuresChanged(oldValue, heures);
    }

    private void heuresChanged(int oldValue, int heures) {
        propertyChangeSupport.firePropertyChange(TimerChangeListener.HEURE_PROP, oldValue, heures);
    }

    @Override
    public int getDixiemeDeSeconde() {
        return dixiemeDeSeconde;
    }

    @Override
    public int getHeures() {
        return heures;
    }

    @Override
    public int getMinutes() {
        return minutes;
    }

    @Override
    public int getSecondes() {
        return secondes;
    }
}