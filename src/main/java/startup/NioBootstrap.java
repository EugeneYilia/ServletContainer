package startup;

import server.NIOConnector;
import server.Processor;

public class NioBootstrap {
    public static void main(String[] args) {
        Processor.init();
        new NIOConnector().init().start();
    }
}
