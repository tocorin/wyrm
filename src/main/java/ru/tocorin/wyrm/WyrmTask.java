package ru.tocorin.wyrm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.UUID;

public class WyrmTask implements Runnable {
    private final static Logger log = LoggerFactory.getLogger(WyrmTask.class);
    private final String id = UUID.randomUUID().toString();
    private final DatagramSocket datagramSocket;
    private final DatagramPacket datagramPacket;

    public WyrmTask(DatagramSocket datagramSocket, DatagramPacket packet) {
        this.datagramSocket = datagramSocket;
        this.datagramPacket = packet;
    }

    @Override
    public void run() {
        try {
            log.info("process");
            log.info("received: {}|{}", new String(datagramPacket.getData()), datagramPacket.getAddress());
            DnsMessage message = parseMessage(datagramPacket);
            datagramSocket.send(datagramPacket);
        } catch (Exception e) {
            log.error("got error", e);
        }

    }

    public static DatagramPacket newPacket() {
        return newPacket(new byte[Constants.MESSAGE_SIZE]);
    }

    public static DatagramPacket newPacket(byte[] buffer) {
        return new DatagramPacket(buffer, buffer.length);
    }

    public static DnsMessage parseMessage(DatagramPacket packet) {
        return null;
    }

    @Override
    public String toString() {
        return "WyrmTask{" +
                "id='" + id + '\'' +
                '}';
    }
}
