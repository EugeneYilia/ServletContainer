package startup;


import catalina.Container;
import catalina.SimpleContainer2;
import config.ServletConfigContext;
import server.Connector;
import server.Processor;

public class Bootstrap {
    public static void main(String[] args) {
        Container container = new SimpleContainer2();
        Connector connector = new Connector();
        ServletConfigContext.init();
        Processor.init();
        connector.init(container);
        try {
            connector.start();
            // make the application wait until we press a key.
            System.in.read();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
