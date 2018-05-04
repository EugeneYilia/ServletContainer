package catalina;

import server.Request;
import server.Response;

import javax.servlet.ServletException;
import java.io.IOException;


public interface ValveContext {


    //-------------------------------------------------------------- Properties


    /**
     * Return descriptive information about this ValveContext implementation.
     */
    public String getInfo();


    //---------------------------------------------------------- Public Methods


    /**
     * Cause the <code>invoke()</code> method of the next Valve that is part of
     * the Pipeline currently being processed (if any) to be executed, passing
     * on the specified request and response objects plus this
     * <code>ValveContext</code> instance.  Exceptions thrown by a subsequently
     * executed Valve (or a Filter or Servlet at the application level) will be
     * passed on to our caller.
     *
     * If there are no more Valves to be executed, an appropriate
     * ServletException will be thrown by this ValveContext.
     *
     * @param request The request currently being processed
     * @param response The response currently being created
     *
     * @exception IOException if thrown by a subsequent Valve, Filter, or
     *  Servlet
     * @exception ServletException if thrown by a subsequent Valve, Filter,
     *  or Servlet
     * @exception ServletException if there are no further Valves configured
     *  in the Pipeline currently being processed
     */
    public void invokeNext(Request request, Response response)
            throws IOException, ServletException;


}
