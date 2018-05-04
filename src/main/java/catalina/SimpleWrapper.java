package catalina;

import server.Request;
import server.RequestFacade;
import server.Response;
import server.ResponseFacade;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import java.io.IOException;

public class SimpleWrapper implements Wrapper, Pipeline {
    private Servlet instance = null;
    private String servletClass;
    private Loader loader;
    private String name;
    private SimplePipeline pipeline = new SimplePipeline(this);
    protected Container parent = null;

    public SimpleWrapper() {
        pipeline.setBasic(new SimpleWrapperValve());
    }

    public Valve getBasic() {
        return null;
    }

    @Override
    public void setBasic(Valve valve) {

    }

    public synchronized void addValve(Valve valve) {
        pipeline.addValve(valve);
    }

    @Override
    public Valve[] getValves() {
        return new Valve[0];
    }

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {

    }

    @Override
    public void removeValve(Valve valve) {

    }

    @Override
    public long getAvailable() {
        return 0;
    }

    @Override
    public void setAvailable(long available) {

    }

    @Override
    public String getJspFile() {
        return null;
    }

    @Override
    public void setJspFile(String jspFile) {

    }

    @Override
    public int getLoadOnStartup() {
        return 0;
    }

    @Override
    public void setLoadOnStartup(int value) {

    }

    @Override
    public String getRunAs() {
        return null;
    }

    @Override
    public void setRunAs(String runAs) {

    }

    @Override
    public String getServletClass() {
        return null;
    }

    @Override
    public void setServletClass(String servletClass) {

    }

    @Override
    public boolean isUnavailable() {
        return false;
    }

    @Override
    public void addInitParameter(String name, String value) {

    }

    @Override
    public void addSecurityReference(String name, String link) {

    }

    public Servlet allocate() throws ServletException {
        // Load and initialize our instance if necessary
        if (instance==null) {
            try {
                instance = loadServlet();
            }
            catch (ServletException e) {
                throw e;
            }
            catch (Throwable e) {
                throw new ServletException("Cannot allocate a servlet instance", e);
            }
        }
        return instance;
    }

    @Override
    public void deallocate(Servlet servlet) throws ServletException {

    }

    @Override
    public String findInitParameter(String name) {
        return null;
    }

    @Override
    public String[] findInitParameters() {
        return new String[0];
    }

    @Override
    public String findSecurityReference(String name) {
        return null;
    }

    @Override
    public String[] findSecurityReferences() {
        return new String[0];
    }

    @Override
    public void load() throws ServletException {

    }

    @Override
    public void removeInitParameter(String name) {

    }

    @Override
    public void removeSecurityReference(String name) {

    }

    @Override
    public void unavailable(UnavailableException unavailable) {

    }

    @Override
    public void unload() throws ServletException {

    }

    private Servlet loadServlet() throws ServletException {
        if (instance!=null)
            return instance;

        Servlet servlet = null;
        String actualClass = servletClass;
        if (actualClass == null) {
            throw new ServletException("servlet class has not been specified");
        }

        Loader loader = getLoader();
        // Acquire an instance of the class loader to be used
        if (loader==null) {
            throw new ServletException("No loader.");
        }
        ClassLoader classLoader = loader.getClassLoader();

        // Load the specified servlet class from the appropriate class loader
        Class classClass = null;
        try {
            if (classLoader!=null) {
                classClass = classLoader.loadClass(actualClass);
            }
        }
        catch (ClassNotFoundException e) {
            throw new ServletException("Servlet class not found");
        }
        // Instantiate and initialize an instance of the servlet class itself
        try {
            servlet = (Servlet) classClass.newInstance();
        }
        catch (Throwable e) {
            throw new ServletException("Failed to instantiate servlet");
        }

        // Call the initialization method of this servlet
        try {
            servlet.init(null);
        }
        catch (Throwable f) {
            throw new ServletException("Failed initialize servlet.");
        }
        return servlet;
    }

    public String getInfo() {
        return null;
    }

    public Loader getLoader() {
        if (loader != null)
            return (loader);
        return (null);
    }

    public void setLoader(Loader loader) {
        this.loader = loader;
    }

    @Override
    public void invoke(RequestFacade requestFacade, ResponseFacade responseFacade) throws IOException, ServletException {

    }
}
