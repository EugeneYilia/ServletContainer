package com.EugeneStudio.testConnection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * 服务器端
 */
public class Server {
    ServerSocket server = null;
    int serverPort = 9999;
    int backlog = 3;//ServerSocket构造函数中的backlog参数

    public Server() throws Exception {
        //server = new ServerSocket(serverPort, backlog, InetAddress.getByName("172.26.143.129"));
        server = new ServerSocket(serverPort, backlog, InetAddress.getByName("172.20.10.2"));
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.service();
//      Thread.currentThread().sleep(1000*600);//持续600秒
    }

    public void service() {
        int count = 1;
        while(true) {
            Socket socket = null;
            try {
                socket = server.accept();//下面在处理当前套接字的时候，再有连接到来，将会放在服务器的serverSocket的请求队列中等待
                Thread.currentThread().sleep(1000);//持续600秒
                System.out.println("new connection has connected,num:" + count++);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}