package ru.tocorin;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tocorin.wyrm.*;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static boolean running = true;

    public static void main(String[] args) throws Exception {
        final WyrmStartupSettings settings = new WyrmStartupSettings(args);

        final ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,
                2,
                60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(Constants.DEFAULT_QUEUE_SIZE),
                new WyrmThreadFactory(),
                new WyrmRejectHandler()
        );

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                executor.shutdown();
                if (executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    log.info("executor closed");
                } else {
                    log.warn("cannot stop executor");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }));
        log.info("starting dsn server on {}", settings.getPort());
        try (DatagramSocket socket = new DatagramSocket(settings.getPort())) {
            while (running) {
                final DatagramPacket packet = WyrmTask.newPacket();
                socket.receive(packet);
                log.info("submit new task");
                executor.submit(new WyrmTask(socket, packet));
            }
        }
    }
}