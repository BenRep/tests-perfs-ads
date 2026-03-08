package fr.benrep.ads.mvp;

/**
 * MVP - MODEL
 *
 * Responsabilités :
 * - Détenir l'état de l'application
 * - Être un POJO passif : aucun listener, aucune notification
 *
 * Différence clé avec MVC :
 * Dans MVP, le Model ne notifie PERSONNE. C'est le Presenter qui lit
 * le Model et met à jour la View manuellement. Le Model est donc plus
 * simple et plus testable (pas de couplage Observer).
 */
public class CalculatorModel {

    private int operandA = 0;
    private int operandB = 0;
    private int result = 0;
    private String operation = "+";
    private String statusMessage = "Prêt";

    public void reset() {
        this.operandA = 0;
        this.operandB = 0;
        this.result = 0;
        this.operation = "+";
        this.statusMessage = "Réinitialisé";
    }

    // Getters / Setters simples — pas de notification
    public int getOperandA()            { return operandA; }
    public void setOperandA(int a)      { this.operandA = a; }

    public int getOperandB()            { return operandB; }
    public void setOperandB(int b)      { this.operandB = b; }

    public int getResult()              { return result; }
    public void setResult(int result)   { this.result = result; }

    public String getOperation()        { return operation; }
    public void setOperation(String op) { this.operation = op; }

    public String getStatusMessage()          { return statusMessage; }
    public void setStatusMessage(String msg)  { this.statusMessage = msg; }
}