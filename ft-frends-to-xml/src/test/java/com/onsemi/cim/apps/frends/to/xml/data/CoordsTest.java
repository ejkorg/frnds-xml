package com.onsemi.cim.apps.frends.to.xml.data;

import org.junit.Test;
import static org.junit.Assert.*;

public class CoordsTest {

    @Test
    public void testEqualsAndHashCode() {
        Coords a = new Coords((short)10, (short)20);
        Coords b = new Coords((short)10, (short)20);
        Coords c = new Coords((short)11, (short)20);
        Coords d = new Coords(null, (short)20);
        Coords e = new Coords(null, (short)20);

        assertTrue(a.equals(b));
        assertEquals(a.hashCode(), b.hashCode());

        assertFalse(a.equals(c));
        assertFalse(a.equals(d));

        assertTrue(d.equals(e));
        assertEquals(d.hashCode(), e.hashCode());
    }
}
