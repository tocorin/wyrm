package ru.tocorin.wyrm;

import java.util.Objects;

/**
 * Dns Message format
 */
public class DnsMessage {
    private final Header header;

    public DnsMessage(byte[] message) {
        if(message.length > Constants.MESSAGE_SIZE){
            throw new IllegalArgumentException("a dns message size is 512 bytes long, yours - " + message.length);
        }
        header = fromMessage(message);
    }

    public Header getHeader() {
        return header;
    }

    /**
     * The header contains the following fields:
     *                                     1  1  1  1  1  1
     *       0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5
     *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     *     |                      ID                       |
     *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     *     |QR|   Opcode  |AA|TC|RD|RA|   Z    |   RCODE   |
     *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     *     |                    QDCOUNT                    |
     *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     *     |                    ANCOUNT                    |
     *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     *     |                    NSCOUNT                    |
     *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     *     |                    ARCOUNT                    |
     *     +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
     */
    public static class Header {
        /**
         * A 16 bit identifier assigned by the program that
         * generates any kind of query. This identifier is copied
         * the corresponding reply and can be used by the requester
         * to match up replies to outstanding queries
         */
        private final int packetId;
        /**
         * A one bit field that specifies whether this message is a
         * query (0), or a response (1)
         */
        private final int qr;
        /**
         * A four bit field that specifies kind of query in this
         * message.  This value is set by the originator of a query
         * and copied into the response.  The values are:
         * 0               a standard query (QUERY)
         * 1               an inverse query (IQUERY)
         * 2               a server status request (STATUS)
         * 3-15            reserved for future use
         */
        private final int opcode;
        /**
         * Authoritative Answer - this bit is valid in responses,
         * and specifies that the responding name server is an
         * authority for the domain name in question section.
         * Note that the contents of the answer section may have
         * multiple owner names because of aliases.  The AA bit
         * corresponds to the name which matches the query name, or
         * the first owner name in the answer section.
         */
        private final int aa;
        /**
         * TrunCation - specifies that this message was truncated
         * due to length greater than that permitted on the
         */
        private final int tc;
        /**
         * Recursion Desired - this bit may be set in a query and
         * is copied into the response.  If RD is set, it directs
         * the name server to pursue the query recursively.
         * Recursive query support is optional.
         */
        private final int rd;
        /**
         * Recursion Available - this be is set or cleared in a
         * response, and denotes whether recursive query support is
         * available in the name server.
         */
        private final int ra;
        /**
         * Reserved for future use.  Must be zero in all queries
         * and responses
         */
        private final int z;
        /**
         * Response code - this 4 bit field is set as part of
         * responses.  The values have the following
         * interpretation:
         * 0               No error condition
         * 1               Format error - The name server was
         * unable to interpret the query.
         * 2               Server failure - The name server was
         * unable to process this query due to a
         * problem with the name server.
         * 3               Name Error - Meaningful only for
         * responses from an authoritative name
         * server, this code signifies that the
         * domain name referenced in the query does
         * not exist.
         * 4               Not Implemented - The name server does
         * not support the requested kind of query.
         * 5               Refused - The name server refuses to
         * perform the specified operation for
         * policy reasons.  For example, a name
         * server may not wish to provide the
         * information to the particular requester,
         * or a name server may not wish to perform
         * a particular operation (e.g., zone
         * transfer) for particular data.
         * 6-15            Reserved for future use.
         */
        private final int rCode;
        /**
         * an unsigned 16-bit integer specifying the number of entries in the question section.
         */
        private final int qdCount;
        /**
         * an unsigned 16-bit integer specifying the number of
         * resource records in the answer section.
         */
        private final int anCount;
        /**
         * an unsigned 16-bit integer specifying the number of name
         * server resource records in the authority records
         * section
         */
        private final int nsCount;
        /**
         * an unsigned 16-bit integer specifying the number of
         * resource records in the additional records section
         */
        private final int arCount;

        public Header(int packetId, int qr, int opcode, int aa, int tc, int rd, int ra, int z,
                      int rCode, int qdCount, int anCount, int nsCount, int arCount) {
            this.packetId = packetId;
            this.qr = qr;
            this.opcode = opcode;
            this.aa = aa;
            this.tc = tc;
            this.rd = rd;
            this.ra = ra;
            this.z = z;
            this.rCode = rCode;
            this.qdCount = qdCount;
            this.anCount = anCount;
            this.nsCount = nsCount;
            this.arCount = arCount;
        }

        public int getPacketId() {
            return packetId;
        }

        public int getQr() {
            return qr;
        }

        public int getOpcode() {
            return opcode;
        }

        public int getAa() {
            return aa;
        }

        public int getTc() {
            return tc;
        }

        public int getRd() {
            return rd;
        }

        public int getRa() {
            return ra;
        }

        public int getZ() {
            return z;
        }

        public int getRCode() {
            return rCode;
        }

        public int getQdCount() {
            return qdCount;
        }

        public int getAnCount() {
            return anCount;
        }

        public int getNsCount() {
            return nsCount;
        }

        public int getArCount() {
            return arCount;
        }
    }

    /**
     * Deserializes header bytes composition to pojo
     * @param bytes message
     * @return header
     */
    private static Header fromMessage(byte[] bytes) {
        return new Header(
                // byte promotion to int, nice ^^
                ((((int) bytes[0] & 0xFF) << 8) | bytes[1]),
                (bytes[2] & 0xFF) >> 7,
                (((bytes[2] & 0xFF) << 1) & 0xFF) >> 4,
                (((bytes[2] & 0xFF) << 5) & 0xFF) >> 7,
                (((bytes[2] & 0xFF) << 6) & 0xFF) >> 7,
                (((bytes[2] & 0xFF) << 7) & 0xFF) >> 7,
                (bytes[3] & 0xFF) >> 7,
                (((bytes[3] & 0xFF) << 1) & 0xFF) >> 5,
                (((bytes[3] & 0xFF) << 4) & 0xFF) >> 4,
                ((bytes[4] & 0xFF) << 8) | bytes[5],
                ((bytes[6] & 0xFF) << 8) | bytes[7],
                ((bytes[8] & 0xFF) << 8) | bytes[9],
                ((bytes[10] & 0xFF) << 8) | bytes[11]
        );
    }

    public enum MessageType {
        QUERY, RESPONSE
    }
}
