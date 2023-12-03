package ru.tocorin.wyrm;

import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WyrmStartupSettingsTest {
    private static WyrmStartupSettings args (String... args) throws ParseException {
        return new WyrmStartupSettings(args);
    }

    @Test
    void testSettings() throws ParseException {
        final WyrmStartupSettings settings = args("-p", "2838");
        assertEquals(2838, settings.getPort());
    }
}