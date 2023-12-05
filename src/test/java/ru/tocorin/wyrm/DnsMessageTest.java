package ru.tocorin.wyrm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DnsMessageTest {

    private static final byte[] header = {
            (byte) 0xEA, (byte) 0x1F,
            // QR OPCODE AA TC RD   RA Z RCODE
            (byte) 0b1_0101_1_0_1, (byte) 0b0_101_1001,
            0x5, 0xA,
            0x5, 0x7,
            0x2, 0x1,
            0x2, 0x2
    };

    @Test
    void wrongMessageSize() {
        assertThrows(IllegalArgumentException.class, () -> {
            new DnsMessage(new byte[513]);
        });
    }

    @Test
    void parseHeader() {
        DnsMessage.Header msgHeader = new DnsMessage(header).getHeader();
        // 1st and 2nd bytes
        assertEquals(0xEA1F, msgHeader.getPacketId());
        // 3rd byte
        assertEquals(1, msgHeader.getQr());
        assertEquals(5, msgHeader.getOpcode());
        assertEquals(1, msgHeader.getAa());
        assertEquals(0, msgHeader.getTc());
        assertEquals(1, msgHeader.getRd());
        //4th byte
        assertEquals(0, msgHeader.getRa());
        assertEquals(5, msgHeader.getZ());
        assertEquals(9, msgHeader.getRCode());
        // 5th and 6th bytes
        assertEquals(0x050A, msgHeader.getQdCount());
        // 7th and 8th bytes
        assertEquals(0x0507, msgHeader.getAnCount());
        // 9th and 10th bytes
        assertEquals(0x0201, msgHeader.getNsCount());
        // 11th and 12th bytes
        assertEquals(0x0202, msgHeader.getArCount());
    }
}