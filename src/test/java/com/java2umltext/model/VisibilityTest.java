package com.java2umltext.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VisibilityTest {

    @Test
    void testFromStringPublic() {
        assertEquals(Visibility.PUBLIC, Visibility.fromString("public"));
    }

    @Test
    void testFromStringPrivate() {
        assertEquals(Visibility.PRIVATE, Visibility.fromString("private"));
    }

    @Test
    void testFromStringProtected() {
        assertEquals(Visibility.PROTECTED, Visibility.fromString("protected"));
    }

    @Test
    void testFromStringDefault() {
        assertEquals(Visibility.DEFAULT, Visibility.fromString("default"));
    }

    @Test
    void testFromStringInvalid() {
        assertNull(Visibility.fromString("invalid"));
    }

    @Test
    void testSymbolPublic() {
        assertEquals('+', Visibility.PUBLIC.symbol());
    }

    @Test
    void testSymbolPrivate() {
        assertEquals('-', Visibility.PRIVATE.symbol());
    }

    @Test
    void testSymbolProtected() {
        assertEquals('#', Visibility.PROTECTED.symbol());
    }

    @Test
    void testSymbolDefault() {
        assertEquals('~', Visibility.DEFAULT.symbol());
    }
}
