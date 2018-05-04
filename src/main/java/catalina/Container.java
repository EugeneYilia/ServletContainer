package catalina;



import server.RequestFacade;
import server.ResponseFacade;

import javax.servlet.ServletException;
import java.io.IOException;

public interface Container {
    public void invoke(RequestFacade requestFacade, ResponseFacade responseFacade)
            throws IOException, ServletException;
}
