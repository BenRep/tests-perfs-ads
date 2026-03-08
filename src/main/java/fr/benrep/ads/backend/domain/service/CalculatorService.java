package fr.benrep.ads.backend.domain.service;

import fr.benrep.ads.backend.domain.port.CalculatorUseCase;

/**
 * SERVICE DOMAINE de l'architecture hexagonale.
 * Contient la logique métier pure, sans dépendance à aucun framework
 * ni à aucune technologie frontend ou infrastructure.
 *
 * Ce service implémente le port d'entrée CalculatorUseCase.
 * Les calculs sont volontairement simples (O(1)) pour ne pas
 * introduire de latence côté backend lors des mesures de performance.
 */
public class CalculatorService implements CalculatorUseCase {

    @Override
    public int add(final int firstNumber, final int secondNumber) {
        return firstNumber + secondNumber;
    }

    @Override
    public int subtract(final int firstNumber, final int secondNumber) {
        return firstNumber - secondNumber;
    }

    @Override
    public int multiply(final int firstNumber, final int secondNumber) {
        return firstNumber * secondNumber;
    }
}