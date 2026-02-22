package br.ufjf.ia;

import org.junit.Test;
import static org.junit.Assert.*;

public class FinanceCalculatorTest {

    @Test
    public void testAdd() {
        FinanceCalculator c = new FinanceCalculator();
        assertEquals(5.0, c.add(2.0, 3.0), 1e-9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDivByZero() {
        FinanceCalculator c = new FinanceCalculator();
        c.div(10.0, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLnDomainError() {
        FinanceCalculator c = new FinanceCalculator();
        c.ln(0.0);
    }

    @Test
    public void testFutureValueSimple() {
        FinanceCalculator c = new FinanceCalculator();
        // 100 * (1.1)^2 = 121
        assertEquals(121.0, c.futureValue(100.0, 0.10, 2), 1e-9);
    }
}
