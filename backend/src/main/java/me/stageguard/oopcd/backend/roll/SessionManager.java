package me.stageguard.oopcd.backend.roll;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class SessionManager {
    public static final SessionManager INSTANCE = new SessionManager();
    @SuppressWarnings("SpellCheckingInspection")
    private static final char[] MAPPING = "AB1CD2EF3GH4IJ5KL6MN7OP8QR9ST0UVWXYZ".toCharArray();

    private final Random random;
    private final HashMap<String, RollSession> sessions;

    private RollSession.Config defaultConfig;

    private SessionManager() {
        random = new Random();
        sessions = new HashMap<>();
        defaultConfig = new RollSession.Config(
                4, Arrays.asList(0.0, 0.2, 0.5, 0.7), Arrays.asList(5, 4, 3, 3), false
        );
    }

    public String createNewSession() throws SQLException {
        var sessionKey = generateSessionKey(8);
        if (sessions.containsKey(sessionKey)) {
            createNewSession();
        }
        sessions.put(sessionKey, new RollSession(defaultConfig));
        return sessionKey;
    }

    public void setDefaultConfig(RollSession.Config config) {
        this.defaultConfig = config;
    }

    public void deleteSession(String sessionKey) {
        sessions.remove(sessionKey);
    }

    public RollSession getSession(String sessionKey) {
        return sessions.getOrDefault(sessionKey, null);
    }

    public String generateSessionKey(int bit) {
        var key = new StringBuilder();
        for (var i = 0; i < bit; i++) {
            key.append(MAPPING[random.nextInt(MAPPING.length - 1)]);
        }
        return key.toString();
    }
}
