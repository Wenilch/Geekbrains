package ru.geekbrains;

import org.junit.Assert;
import org.junit.Test;
import ru.geekbrains.nds_provider.NdsProvider;

public class TestCalcNds {

    @Test
    public void testCalcNds() {
        CalcNds calcNds = new CalcNds(new MockNdsProvider());
        Double result = calcNds.priceWithNds(1000.);
        Assert.assertEquals(1200., result, 0.000001);
    }

    static class MockNdsProvider implements NdsProvider {

        @Override
        public double getNdsValue() {
            return 0.2;
        }
    }
}
