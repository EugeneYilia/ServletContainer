package server;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class SessionContext implements HttpSessionContext {

    private static Map<String, Session> sessionContext = new HashMap<>();

    @Override
    public HttpSession getSession(String sessionId) {
        Session session = sessionContext.get(sessionId);
        if (session != null && session.getState()) {
            return session;
        } else {
            Session newSession = new Session();
            sessionContext.put(newSession.getId(),newSession);
            return newSession;
        }

    }

    @Override
    public Enumeration<String> getIds() {
        return Collections.enumeration(sessionContext.keySet());
    }

    public static Map<String, Session> getSessionContext() {
        return sessionContext;
    }
}
