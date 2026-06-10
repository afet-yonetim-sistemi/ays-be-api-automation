package org.ays.common.datasource;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.ays.common.util.AysConfigurationProperty;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
public class AysSshTunnel {

    private static final int SSH_PORT = 22;
    private static final int SESSION_CONNECT_TIMEOUT_IN_MILLISECONDS = 15000;
    private static final List<String> DEFAULT_SSH_FILE_NAMES = List.of(
            "id_rsa",
            "id_ed25519"
    );

    private static Session session;
    private static String tunneledDatabaseUrl;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(AysSshTunnel::close));
    }

    private AysSshTunnel() {
    }

    public static synchronized void open() {

        if (session != null && session.isConnected()) {
            return;
        }


        try {

            JSch jsch = new JSch();
            addIdentity(jsch);

            session = jsch.getSession(
                    AysConfigurationProperty.Ssh.USERNAME,
                    AysConfigurationProperty.Ssh.HOST,
                    SSH_PORT
            );
            session.setConfig("StrictHostKeyChecking", "no");
            session.setDaemonThread(true);
            session.connect(SESSION_CONNECT_TIMEOUT_IN_MILLISECONDS);

            tunneledDatabaseUrl = generateTunneledDatabaseUrl();

            log.info("SSH tunnel established through {} on tunneled database url: {}", AysConfigurationProperty.Ssh.HOST, tunneledDatabaseUrl);

        } catch (JSchException exception) {
            close();
            throw new IllegalStateException("SSH tunnel could not be established!", exception);
        }
    }

    private static void addIdentity(JSch jsch) throws JSchException {

        final String privateKeyPem = AysConfigurationProperty.Ssh.PRIVATE_KEY_PEM;
        if (privateKeyPem != null && !privateKeyPem.isBlank()) {
            jsch.addIdentity(
                    AysConfigurationProperty.Ssh.USERNAME,
                    privateKeyPem.getBytes(StandardCharsets.UTF_8),
                    null,
                    null
            );
            return;
        }

        final String privateKeyPath = AysConfigurationProperty.Ssh.PRIVATE_KEY_PATH;
        if (privateKeyPath != null && !privateKeyPath.isBlank()) {
            jsch.addIdentity(privateKeyPath);
            return;
        }

        jsch.addIdentity(findDefaultPrivateKeyPath().toString());
    }

    private static Path findDefaultPrivateKeyPath() {
        final Path sshDirectory = Path.of(System.getProperty("user.home"), ".ssh");
        return DEFAULT_SSH_FILE_NAMES.stream()
                .map(sshDirectory::resolve)
                .filter(Files::exists)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "SSH private key could not be found! Define 'ssh.private_key_pem' or 'ssh.private_key_path' or place a default key under " + sshDirectory
                ));
    }

    private static String generateTunneledDatabaseUrl() throws JSchException {
        final String databaseUrl = AysConfigurationProperty.Database.URL;
        final URI databaseUri = URI.create(databaseUrl.replaceFirst("^jdbc:", ""));
        int forwardedPort = session.setPortForwardingL(0, databaseUri.getHost(), databaseUri.getPort());
        return databaseUrl.replace(String.valueOf(databaseUri.getPort()), String.valueOf(forwardedPort));
    }


    public static synchronized void close() {

        if (session == null) {
            return;
        }

        if (session.isConnected()) {
            session.disconnect();
            log.info("SSH tunnel closed.");
        }

        session = null;
        tunneledDatabaseUrl = null;
    }


    public static synchronized String getTunneledDatabaseUrl() {
        return tunneledDatabaseUrl;
    }

}
