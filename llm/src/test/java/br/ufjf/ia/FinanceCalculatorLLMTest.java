package br.ufjf.ia;

import org.junit.Test;

import static org.junit.Assert.*;

public class FinanceCalculatorLLMTest {

    private final FinanceCalculator c = new FinanceCalculator();
    private static final double EPS = 1e-9;

    // -----------------------
    // Básicas
    // -----------------------

    @Test
    public void add_shouldSumPositives() {
        assertEquals(5.0, c.add(2.0, 3.0), EPS);
    }

    @Test
    public void add_shouldHandleNegatives() {
        assertEquals(-1.0, c.add(2.0, -3.0), EPS);
    }

    @Test
    public void sub_shouldSubtract() {
        assertEquals(-1.0, c.sub(2.0, 3.0), EPS);
        assertEquals(5.0, c.sub(2.0, -3.0), EPS);
    }

    @Test
    public void mul_shouldMultiply() {
        assertEquals(6.0, c.mul(2.0, 3.0), EPS);
        assertEquals(-6.0, c.mul(2.0, -3.0), EPS);
        assertEquals(0.0, c.mul(2.0, 0.0), EPS);
    }

    @Test
    public void div_shouldDivide() {
        assertEquals(2.0, c.div(10.0, 5.0), EPS);
        assertEquals(-2.0, c.div(10.0, -5.0), EPS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void div_shouldThrowOnZero() {
        c.div(1.0, 0.0);
    }

    @Test
    public void div_shouldHandleSmallNumbers() {
        assertEquals(0.5, c.div(1.0, 2.0), EPS);
    }

    // -----------------------
    // ln / log10
    // -----------------------

    @Test
    public void ln_shouldReturnZeroForOne() {
        assertEquals(0.0, c.ln(1.0), EPS);
    }

    @Test
    public void ln_shouldBePositiveForGreaterThanOne() {
        assertTrue(c.ln(2.0) > 0.0);
    }

    @Test
    public void ln_shouldBeNegativeForBetweenZeroAndOne() {
        assertTrue(c.ln(0.5) < 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ln_shouldThrowForZero() {
        c.ln(0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ln_shouldThrowForNegative() {
        c.ln(-0.0001);
    }

    @Test
    public void log10_shouldReturnZeroForOne() {
        assertEquals(0.0, c.log10(1.0), EPS);
    }

    @Test
    public void log10_shouldReturnOneForTen() {
        assertEquals(1.0, c.log10(10.0), EPS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void log10_shouldThrowForZero() {
        c.log10(0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void log10_shouldThrowForNegative() {
        c.log10(-10.0);
    }

    // -----------------------
    // futureValue / presentValue
    // -----------------------

    @Test
    public void futureValue_shouldReturnPVWhenPeriodsZero() {
        assertEquals(123.45, c.futureValue(123.45, 0.10, 0), EPS);
    }

    @Test
    public void futureValue_shouldHandleZeroRate() {
        assertEquals(100.0, c.futureValue(100.0, 0.0, 10), EPS);
    }

    @Test
    public void futureValue_shouldGrowForPositiveRate() {
        assertEquals(121.0, c.futureValue(100.0, 0.10, 2), EPS);
    }

    @Test
    public void futureValue_shouldShrinkForNegativeRate() {
        // 100*(0.9)^2 = 81
        assertEquals(81.0, c.futureValue(100.0, -0.10, 2), EPS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void futureValue_shouldThrowForNegativePeriods() {
        c.futureValue(100.0, 0.10, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void futureValue_shouldThrowForRateLessThanMinusOne() {
        c.futureValue(100.0, -1.0000001, 1);
    }

    @Test
    public void presentValue_shouldReturnFVWhenPeriodsZero() {
        assertEquals(200.0, c.presentValue(200.0, 0.10, 0), EPS);
    }

    @Test
    public void presentValue_shouldInvertFutureValueForSameRateAndPeriods() {
        double pv = 150.0;
        double r = 0.07;
        int n = 5;

        double fv = c.futureValue(pv, r, n);
        double pv2 = c.presentValue(fv, r, n);

        assertEquals(pv, pv2, 1e-6); // erro numérico acumulado
    }

    @Test(expected = IllegalArgumentException.class)
    public void presentValue_shouldThrowForNegativePeriods() {
        c.presentValue(100.0, 0.10, -2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void presentValue_shouldThrowForRateMinusOneOrLess() {
        c.presentValue(100.0, -1.0, 1);
    }

    // -----------------------
    // compoundInterest
    // -----------------------

    @Test
    public void compoundInterest_shouldBeZeroWhenRateZero() {
        assertEquals(0.0, c.compoundInterest(100.0, 0.0, 10), EPS);
    }

    @Test
    public void compoundInterest_shouldBeFVMinusPrincipal() {
        double principal = 100.0;
        double r = 0.10;
        int n = 2;

        double fv = c.futureValue(principal, r, n);
        double ci = c.compoundInterest(principal, r, n);

        assertEquals(fv - principal, ci, EPS);
        assertEquals(21.0, ci, EPS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void compoundInterest_shouldPropagateInvalidPeriods() {
        c.compoundInterest(100.0, 0.10, -1);
    }

    // -----------------------
    // paybackPeriods
    // -----------------------

    @Test
    public void paybackPeriods_shouldReturnZeroWhenInvestmentNonPositive() {
        assertEquals(0, c.paybackPeriods(0.0, 10.0));
        assertEquals(0, c.paybackPeriods(-100.0, 10.0));
    }

    @Test
    public void paybackPeriods_shouldReturnMinusOneWhenCashflowNonPositive() {
        assertEquals(-1, c.paybackPeriods(100.0, 0.0));
        assertEquals(-1, c.paybackPeriods(100.0, -10.0));
    }

    @Test
    public void paybackPeriods_shouldComputeExactPayback() {
        assertEquals(10, c.paybackPeriods(100.0, 10.0));
        assertEquals(4, c.paybackPeriods(100.0, 25.0));
    }

    @Test
    public void paybackPeriods_shouldRoundUpWhenNotDivisible() {
        // 100 / 30 = 3.333... => precisa de 4 períodos
        assertEquals(4, c.paybackPeriods(100.0, 30.0));
    }

    @Test
    public void paybackPeriods_shouldHandleVerySmallCashflow() {
        // Deve terminar sem overflow lógico
        int p = c.paybackPeriods(1.0, 0.1);
        assertTrue(p >= 10); // 10 períodos dá exatamente 1.0 (considerando double)
    }
}
