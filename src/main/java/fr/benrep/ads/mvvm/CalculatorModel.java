package fr.benrep.ads.mvvm;

/**
 * MVVM - MODEL
 *
 * Responsabilités :
 * - Représenter les données métier brutes
 * - Être un POJO passif sans logique de présentation
 *
 * Différence avec MVC/MVP :
 * Le Model est encore plus passif. C'est le ViewModel qui le lit
 * et expose des propriétés observables adaptées à la vue.
 * Le Model n'a ni listener (MVC) ni contrat de vue (MVP).
 */
public class CalculatorModel {

    private int operandA = 0;
    private int operandB = 0;
    private int result = 0;
    private String operation = "+";

    public void reset() {
        this.operandA = 0;
        this.operandB = 0;
        this.result = 0;
        this.operation = "+";
    }

    public int getOperandA()            { return operandA; }
    public void setOperandA(int a)      { this.operandA = a; }

    public int getOperandB()            { return operandB; }
    public void setOperandB(int b)      { this.operandB = b; }

    public int getResult()              { return result; }
    public void setResult(int result)   { this.result = result; }

    public String getOperation()        { return operation; }
    public void setOperation(String op) { this.operation = op; }
}