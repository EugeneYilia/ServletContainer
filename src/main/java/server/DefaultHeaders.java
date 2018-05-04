package server;

public class DefaultHeaders {
    public static final String CONNECTION_NAME = "connection";//判断是否为持久连接
    public static final String CONTENT_TYPE_NAME = "content-type";//请求内容的类型 只要有就是非get
    public static final String CONTENT_LENGTH_NAME = "content-length";//内容的长度 只要有就是非get
    public static final String COOKIE_NAME = "cookie";
}
