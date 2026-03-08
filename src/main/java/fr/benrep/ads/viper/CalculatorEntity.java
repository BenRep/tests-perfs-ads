package fr.benrep.ads.viper;

/**
 * VIPER - ENTITY
 *
 * Équivalent du Model dans les autres patterns.
 * Représente uniquement les données métier brutes.
 *
 * Dans VIPER, l'Entity est encore plus isolée que dans les autres patterns :
 * elle n'est manipulée QUE par l'Interactor.
 * Ni la View, ni le Presenter, ni le Router n'y accèdent directement.
 */
public class CalculatorEntity {

    private int operandA;
    private int operandB;
    private String operation;
    private int result;

    public CalculatorEntity(int operandA, int operandB, String operation, int result) {
        this.operandA  = operandA;
        this.operandB  = operandB;
        this.operation = operation;
        this.result    = result;
    }

    public static CalculatorEntity empty() {
        return new CalculatorEntity(0, 0, "+", 0);
    }

    public int getOperandA()              { return operandA; }
    public void setOperandA(int a)        { this.operandA = a; }

    public int getOperandB()              { return operandB; }
    public void setOperandB(int b)        { this.operandB = b; }

    public String getOperation()          { return operation; }
    public void setOperation(String op)   { this.operation = op; }

    public int getResult()                { return result; }
    public void setResult(int result)     { this.result = result; }
}