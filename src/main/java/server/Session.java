package server;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.*;

public class Session implements HttpSession{
    private String sessionId = "";
    private long creationTime = 0L;
    private boolean state = true;
    private Map<String,Object> attributes = new HashMap<String,Object>();
    private int maxInactiveInterval = -1;

    public Session(){
        creationTime = Calendar.getInstance().getTimeInMillis();
        sessionId = UUID.randomUUID().toString().replace("-","");
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public String getId() {
        return sessionId;
    }

    @Override
    public long getLastAccessedTime() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        this.maxInactiveInterval = interval;
    }

    @Override
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return (HttpSessionContext) SessionContext.getSessionContext();
    }

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public Object getValue(String name) {
        return getAttribute(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return Collections.enumeration(attributes.keySet());
    }

    @Override
    public String[] getValueNames() {
        return (String[]) attributes.keySet().toArray();
    }

    @Override
    public void setAttribute(String name, Object value) {
        attributes.put(name,value);
    }

    @Override
    public void putValue(String name, Object value) {
        attributes.put(name,value);
    }

    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    @Override
    public void removeValue(String name) {
       removeAttribute(name);
    }

    @Override
    public void invalidate() {
       this.state = false;
    }

    @Override
    public boolean isNew() {
        return false;
    }

    public boolean getState(){
        return this.state;
    }
}
