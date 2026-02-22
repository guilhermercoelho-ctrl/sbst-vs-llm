package br.ufjf.ia;

public class FinanceCalculator {

    // Operações básicas
    public double add(double a, double b) { return a + b; }

    public double sub(double a, double b) { return a - b; }

    public double mul(double a, double b) { return a * b; }

    public double div(double a, double b) {
        if (b == 0.0) throw new IllegalArgumentException("Division by zero");
        return a / b;
    }

    // Funções matemáticas
    public double ln(double x) {
        if (x <= 0.0) throw new IllegalArgumentException("ln domain error");
        return Math.log(x);
    }

    public double log10(double x) {
        if (x <= 0.0) throw new IllegalArgumentException("log10 domain error");
        return Math.log10(x);
    }

    // Funções financeiras
    // FV = PV * (1 + r)^n
    public double futureValue(double presentValue, double rate, int periods) {
        if (periods < 0) throw new IllegalArgumentException("periods must be >= 0");
        if (rate < -1.0) throw new IllegalArgumentException("rate must be >= -1.0");
        return presentValue * Math.pow(1.0 + rate, periods);
    }

    // PV = FV / (1 + r)^n
    public double presentValue(double futureValue, double rate, int periods) {
        if (periods < 0) throw new IllegalArgumentException("periods must be >= 0");
        if (rate <= -1.0) throw new IllegalArgumentException("rate must be > -1.0");
        return futureValue / Math.pow(1.0 + rate, periods);
    }

    // Juros compostos acumulados: FV - PV
    public double compoundInterest(double principal, double rate, int periods) {
        double fv = futureValue(principal, rate, periods);
        return fv - principal;
    }

    // Payback simples: número de períodos para recuperar investimento
    // Retorna 0 se investimento <= 0; -1 se cashflow <= 0
    public int paybackPeriods(double investment, double cashflowPerPeriod) {
        if (investment <= 0) return 0;
        if (cashflowPerPeriod <= 0) return -1;

        double remaining = investment;
        int periods = 0;

        while (remaining > 0) {
            remaining -= cashflowPerPeriod;
            periods++;
            if (periods > 1_000_000) throw new IllegalStateException("too many periods");
        }
        return periods;
    }
}
