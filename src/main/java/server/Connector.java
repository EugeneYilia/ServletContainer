package server;


import catalina.Container;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Connector {
    private ServerSocket serverSocket = null;
    private boolean isStopped = false;
    private Container container;

    public Connector init(Container container){
        this.container = container;
        return this;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(Constants.PORT, Constants.BACKLOG);
            while (!isStopped) {
                Socket socket = serverSocket.accept();
                Processor.assign(socket,container);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        isStopped = true;
    }

    public void close() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void setContainer(Container container){
        this.container = container;
    }

    public Container getContainer(){
        return this.container;
    }
}
