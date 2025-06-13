package network;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SSHConnection implements Runnable {
    private static final Logger logger = LogManager.getLogger(SSHConnection.class);

    private final String sshUser;
    private final String sshHost;
    private final int sshPort;
    private final String sshPassword;

    private final int localPort;
    private final String dbHost;
    private final int dbPort;

    private Session session;
    private volatile boolean isConnected = false;

    public SSHConnection(String sshUser, String sshPassword, String sshHost, int sshPort, int localPort, String dbHost, int intPort) {
        this.sshUser = sshUser;
        this.sshPassword = sshPassword;
        this.sshHost = sshHost;
        this.sshPort = sshPort;
        this.localPort = localPort;
        this.dbHost = dbHost;
        this.dbPort = intPort;
    }

    @Override
    public void run() {
        try {
            JSch jsch = new JSch();

            session = jsch.getSession(sshUser, sshHost, sshPort);
            session.setPassword(sshPassword);

            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("ConnectTimeout", "10000");


            session.setServerAliveInterval(30000);
            session.setServerAliveCountMax(3);

            session.connect();

            session.setPortForwardingL(localPort, dbHost, dbPort);

            logger.info("SSH-туннель создан: localhost:{} -> {}:{} через {}", localPort, dbHost, dbPort, sshHost);

            isConnected = true;

            while (session.isConnected() && !Thread.currentThread().isInterrupted()) {
                Thread.sleep(1000);
            }

        } catch (JSchException e) {
            logger.error("Ошибка SSH-туннеля (JSchException): {}", e.getMessage(), e);
        } catch (InterruptedException e) {
            logger.warn("SSH-туннель был прерван (InterruptedException): {}", e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("Неизвестная ошибка SSH-туннеля: {}", e.getMessage(), e);
        } finally {
            close();
        }
    }

    public boolean waitUntilConnected(long timeoutMs) throws InterruptedException {
        long startTime = System.currentTimeMillis();

        while (!isConnected && (System.currentTimeMillis() - startTime) < timeoutMs) {
            Thread.sleep(200);
        }
        return isConnected;
    }

    public void close() {
        if (session != null && session.isConnected()) {
            session.disconnect();
            logger.info("SSH-туннель закрыт.");
        } else if (session != null) {
            logger.warn("Попытка закрыть SSH-туннель, который уже был отключен.");
        } else {
            logger.warn("Попытка закрыть SSH-туннель, который не был инициализирован.");
        }
        isConnected = false;
    }
}