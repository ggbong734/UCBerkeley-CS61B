import static org.junit.Assert.*;

import org.junit.Test;

public class FlikTest {

    @Test
    public void testFlik() {
        Integer a = 1;
        Integer b = 1;
        assertTrue(Flik.isSameNumber(a,b));

        Integer c = 0;
        Integer d = 0;
        assertTrue(Flik.isSameNumber(c,d));

        Integer e = -5;
        Integer f = -5;
        assertTrue(Flik.isSameNumber(e,f));
    }
}