package fr.benrep.ads.mvvm;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * MVVM - OBSERVABLE PROPERTY
 *
 * Implémentation maison d'une propriété observable (data-binding).
 * Simule le mécanisme de binding de JavaFX (Property/ObservableValue)
 * ou d'Android LiveData, mais en Swing pur.
 *
 * Rôle dans MVVM :
 * - Chaque propriété du ViewModel est une ObservableProperty
 * - La View s'y abonne via bind()
 * - Quand le ViewModel modifie la valeur (set()), tous les abonnés sont notifiés
 * - C'est le DATA BINDING : la Vue ne sait pas d'où vient la donnée,
 *   elle réagit juste aux changements.
 *
 * @param <T> type de la valeur observée
 */
public class ObservableProperty<T> {

    private T value;
    private final List<Consumer<T>> observers = new ArrayList<>();

    public ObservableProperty(T initialValue) {
        this.value = initialValue;
    }

    /** Modifie la valeur et notifie tous les observateurs. */
    public void set(T newValue) {
        this.value = newValue;
        observers.forEach(obs -> obs.accept(newValue));
    }

    /** Retourne la valeur courante. */
    public T get() {
        return value;
    }

    /**
     * Abonne un observateur : appelé immédiatement avec la valeur courante
     * puis à chaque changement (comportement "sticky" comme LiveData).
     */
    public void bind(Consumer<T> observer) {
        observers.add(observer);
        observer.accept(value); // notification initiale
    }
}