package fr.benrep.ads.backend.domain.port;

/**
 * PORT (entrée) de l'architecture hexagonale.
 * Définit le contrat que le frontend utilise pour communiquer avec le backend.
 * Chaque modèle de conception frontend appelle ce port, garantissant
 * une indépendance totale entre le backend et le frontend.
 */
public interface CalculatorUseCase {

    /**
     * Additionne deux entiers.
     * Calcul intentionnellement simple pour ne pas introduire de latence backend.
     *
     * @param firstNumber premier opérande
     * @param secondNumber second opérande
     * @return résultat de firstNumber + b
     */
    int add(int firstNumber, int secondNumber);

    /**
     * Soustrait b de firstNumber.
     *
     * @param firstNumber premier opérande
     * @param secondNumber second opérande
     * @return résultat de firstNumber - b
     */
    int subtract(int firstNumber, int secondNumber);

    /**
     * Multiplie deux entiers.
     *
     * @param firstNumber premier opérande
     * @param secondNumber second opérande
     * @return résultat de firstNumber * secondNumber
     */
    int multiply(int firstNumber, int secondNumber);
}