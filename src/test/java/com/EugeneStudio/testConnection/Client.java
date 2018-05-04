package com.EugeneStudio.testConnection;

import java.io.IOException;
import java.net.Socket;

/**
 * 客户端
 */
public class Client {
    public static void main(String[] args) {
        for (int i = 0; i < 30; i++) {
            try {
                Socket socket = new Socket("127.0.0.1", 9999);
                System.out.println("client connection:" + (i + 1));
            } catch (IOException e) {
                System.out.println("9999端口正忙");
            }
        }
    }
}
