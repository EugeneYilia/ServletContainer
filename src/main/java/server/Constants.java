package server;

public class Constants {
    public static final int PROCESSOR_POOL_SIZE = 50;
    public static final int BACKLOG = 20;
    public static int BUFFER_SIZE = 1024;
    public static final String SERVER_NAME = "EUGENE_LIU";
    public static final String CONFIG_LOCATION = "webroot\\configuration\\web.json";
    public static final String LOCALHOST = "localhost";
    public static final String RFC1123_DATE = "EEE, dd MMM yyyy HH:mm:ss zzz";

    public static int PORT = 80;
    public static String WEB_ROOT = "webroot";
    public static String SERVLET_ROOT = "webroot\\servlet";

    private Constants(){}

    public static void setBufferSize(int bufferSize) {
        BUFFER_SIZE = bufferSize;
    }

    public static int getBufferSize() {
        return BUFFER_SIZE;
    }
}
