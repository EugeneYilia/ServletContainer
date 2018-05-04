package server;

import catalina.Container;
import jasper.ServletParser;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static server.Constants.*;

public class Processor implements Runnable {
    public Socket socket = null;
    private Container container = null;
    private Request request ;
    public static ExecutorService executorService = null;
    private boolean isJsp2ClassExist = false;


    public static void init() {
        executorService = Executors.newFixedThreadPool(PROCESSOR_POOL_SIZE);
    }

    public Processor(Socket socket, Container container) {
        this.socket = socket;
        this.container = container;
    }

    public static void assign(Socket socket, Container container) {
        Thread thread = new Thread(new Processor(socket, container));
        executorService.execute(thread);
    }

    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {
            request = createRequest(inputStream);
            Response response = createResponse(outputStream);
            response.setRequest(request);
            RequestFacade requestFacade = new RequestFacade(request);
            ResponseFacade responseFacade = new ResponseFacade(response);
            if(request.getRequestURI().contains(".jsp")){
                String jsp2ClassName = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/"),request.getRequestURI().length()-4)+"_jsp.class";
                String fileName = "webroot/servlet"+jsp2ClassName;
                //System.out.println(fileName);
                File file  = new File(fileName);
                if(!file.exists()){
                    parseJsp();
                }
                String newRequestUri = request.getRequestURI().substring(0,request.getRequestURI().length()-4)+"_jsp";
                //System.out.println(newRequestUri);
                request.setRequestURI(newRequestUri);
                container.invoke(requestFacade, responseFacade);
            }
            else if(!request.getRequestURI().contains("/servlet/")){
                response.sendStaticResource();
            }else{
                container.invoke(requestFacade, responseFacade);
            }
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    public Request createRequest(InputStream inputStream) {
        return new Request(inputStream);
    }

    public Response createResponse(OutputStream outputStream) {
        Response response = new Response(outputStream);
        response.init(request);
        return response;
    }

    public void parseJsp(){
        ServletParser servletParser = new ServletParser(new File("webroot"+request.getRequestURI()));
        try {
            servletParser.parse();
            //System.out.println(servletParser.parse());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
