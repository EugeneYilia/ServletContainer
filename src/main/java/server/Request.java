package server;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.security.Principal;
import java.util.*;

import static server.Constants.*;

public class Request implements HttpServletRequest {

    private InputStream inputStream = null;
    private String method;
    private String protocal;
    private String requestURI;
    private String queryString;
    private String requestedSessionId;
    private int contentLength;
    private String contentType;
    private boolean hasRequestedSessionURL = false;
    private String protocolVersion;
    private String protocolSchema;
    private String connection;
    private String charsetEncoding = "utf-8";//初始默认的编码方式

    private Map<String, String> headers = new HashMap<String, String>();
    private List<Cookie> cookies = new ArrayList<Cookie>();
    private Map<String, Object> attributes = new HashMap<>();
    private Map<String, String[]> parameters = new HashMap<>();//?后面query中查询的参数

    public Request(InputStream inputStream) {
        this.inputStream = inputStream;
        parse();
    }

    public void parse() {
        readFirstLine();//请求行的解析
        readMessageHeaders();//消息头的解析
    }

    public void readFirstLine() {
        StringBuilder stringBuilder = null;
        int i;
        try {
            stringBuilder = new StringBuilder(BUFFER_SIZE);
            while ((char) (i = inputStream.read()) != '\r') {
                stringBuilder.append((char) i);
                //System.out.println("(char)i->"+(char)i);
            }
            inputStream.read();//读取掉\n
            String requestLine = stringBuilder.toString();
            //System.out.println("requestLine->"+requestLine);
            parseFirstLine(requestLine);
        } catch (IOException | ServletException e) {
            e.printStackTrace();
            System.out.println("error request line");
        }
    }

    public void parseFirstLine(String firstLine) throws ServletException {
        //System.out.println("firstLine->"+firstLine);
        if (firstLine == null || firstLine.length() == 0) {
            return;
        }

        String[] requestString = firstLine.split(" ");
        /*=======================method part===================*/
        String method = requestString[0];
        if (method == null || method.length() == 0) {
            return;
        }
        if (method == null || method.length() == 0) {
            throw new ServletException("missing request method");
        }
        setMethod(method);

        /*=======================uri part=======================*/
        String uri = requestString[1];
        if (uri == null || uri.length() == 0) {
            return;
        }
        if (uri == null || uri.length() == 0) {
            throw new ServletException("missing request uri");
        }

        int position = uri.indexOf('?');
        if (position > 0) {
            String queryString = uri.substring(position + 1);
            setQueryString(queryString);
            parseQueryString(queryString);
            uri = uri.substring(0, position);
        }

        String match = ";jsessionid=";
        position = uri.indexOf(";");
        if (position >= 0) {
            String requestSessionId = uri.substring(position + match.length());
            int position2 = requestSessionId.indexOf(";");
            if (position2 >= 0) {
                requestSessionId = requestSessionId.substring(0, position2);
            }
            setRequestSessionId(requestSessionId);
            setRequestedSessionURL(true);
            uri = uri.substring(0, position);
        } else {
            setRequestedSessionURL(false);
        }
        String normalizedURI = normalizedUri(uri);
        if (normalizedURI == null || normalizedURI.length() == 0) {
            throw new ServletException("invalid uri");
        }
        setRequestURI(normalizedURI);

        /*=====================protocol part====================*/
        String protocol = requestString[2];
        if (protocol == null || protocol.length() == 0) {
            throw new ServletException("missing request protocol");
        }
        setProtocal(protocol);
        position = protocol.indexOf("/");
        String schema = protocol.substring(0, position);
        setProtocolSchema(schema);
        String version = protocol.substring(position + 1);
        setProtocolVersion(version);
    }

    public void parseQueryString(String queryString) {
        int location = queryString.indexOf("=");
        int location2 = queryString.indexOf("&");//not exist  ->  -1
        boolean hasNextString = false;
        System.out.println(queryString);
        while (true) {
            if (location > 0) {
                String key, value;
                if (location2 > 0) {
                    key = queryString.substring(0, location);
                    value = queryString.substring(location + 1, location2);
                    hasNextString = true;
                } else {
                    key = queryString.substring(0, location);
                    value = queryString.substring(location + 1);
                    hasNextString = false;
                }
                String[] values = parameters.get(key);
                if (values == null) {
                    values = new String[1];
                    values[0] = value;
                    parameters.put(key, values);
                } else {
                    values = parameters.get(key);
                    ArrayList arrayList = new ArrayList(Arrays.asList(values));
                    arrayList.add(value);
                    parameters.put(key, (String[]) arrayList.toArray());
                }
            }
            if (hasNextString) {
                queryString = queryString.substring(location2 + 1);
                location = queryString.indexOf("=");
                location2 = queryString.indexOf("&");//not exist  ->  -1
            } else {
                break;
            }
        }
    }

    public void parseHeaderLine(String headerLine) {
        int position = headerLine.indexOf(":");
        String key = headerLine.substring(0, position).toLowerCase();
        String value = headerLine.substring(position + 1).trim();
        addHeader(key, value);
        if (key.equalsIgnoreCase(DefaultHeaders.CONNECTION_NAME)) {
            setConnection(value);
        } else if (key.equalsIgnoreCase(DefaultHeaders.CONTENT_LENGTH_NAME)) {
            setContentLength(Integer.parseInt(value));
        } else if (key.equalsIgnoreCase(DefaultHeaders.CONTENT_TYPE_NAME)) {
            setContentType(value);
        } else if (key.equalsIgnoreCase(DefaultHeaders.COOKIE_NAME)) {
            parseCookie(value);
        }
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void parseCookie(String value) {
        while (value != null && value.length() != 0) {
            boolean hasMore = true;
            int position = value.indexOf("=");
            int position2 = value.indexOf(";");
            String cookieName = value.substring(0, position).trim();//删掉cookie名字部分额外的空格
            String cookieValue = "";
            if (position2 == -1) {//最后一个
                cookieValue = value.substring(position + 1).trim();
                hasMore = false;
            } else {
                cookieValue = value.substring(position + 1, position2).trim();
            }
            cookies.add(new Cookie(cookieName, cookieValue));
            if (!hasMore) {
                break;
            }
            value = value.substring(position2 + 1);
        }
    }

    public void readMessageHeaders() {
        StringBuilder stringBuilder = null;
        int i;
        try {
            while (true) {
                stringBuilder = new StringBuilder(BUFFER_SIZE);
                while ((char) (i = inputStream.read()) != '\r') {
                    stringBuilder.append((char) i);
                }
                inputStream.read();//读取掉剩下的\n
                String headerLine = stringBuilder.toString();
                if (headerLine == null || headerLine.length() == 0) {
                    break;
                }
                parseHeaderLine(headerLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error headers");
        }
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void setProtocolSchema(String protocolSchema) {
        this.protocolSchema = protocolSchema;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public void setProtocal(String protocal) {
        this.protocal = protocal;
    }

    public void setRequestURI(String uri) {
        this.requestURI = uri;
    }

    public String normalizedUri(String uri) {
        if (uri == null) {
            return null;
        }
        String normalizedURI = uri;
        if (!normalizedURI.startsWith("/")) {
            normalizedURI = "/" + normalizedURI;
        }
        int index = 0;
        while ((index = normalizedURI.indexOf("\\")) >= 0) {
            normalizedURI = normalizedURI.replace('\\', '/');
        }
        while ((index = normalizedURI.indexOf("//")) > 0) {
            normalizedURI = normalizedURI.replace("//", "/");
        }
        return normalizedURI;
    }

    public void setRequestedSessionURL(boolean exist) {
        hasRequestedSessionURL = exist;
    }

    public void setRequestSessionId(String requestSessionId) {
        this.requestedSessionId = requestSessionId;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getAuthType() {
        return null;
    }

    public Cookie[] getCookies() {
        return (Cookie[]) cookies.toArray();
    }

    public long getDateHeader(String s) {
        return 0;
    }

    public String getHeader(String s) {
        return headers.get(s);
    }

    public Enumeration<String> getHeaders(String s) {//headers的value枚举变量
        return Collections.enumeration(headers.values());
    }

    public Enumeration<String> getHeaderNames() {//headers的key枚举变量
        return Collections.enumeration(headers.keySet());
    }

    public int getIntHeader(String s) {
        return headers.size();
    }

    public String getMethod() {
        return method;
    }

    public String getPathInfo() {
        return null;
    }

    public String getPathTranslated() {
        return null;
    }

    public String getContextPath() {
        return requestURI;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getRemoteUser() {
        return null;
    }

    public boolean isUserInRole(String s) {
        return false;
    }

    public Principal getUserPrincipal() {
        return null;
    }

    public String getRequestedSessionId() {
        if (hasRequestedSessionURL) {
            return requestedSessionId;
        } else {
            String jsessionid = "";
            for (Cookie cookie : cookies) {
                if (cookie.getName().trim().equalsIgnoreCase("JSESSIONID")) {
                    jsessionid = cookie.getValue();
                    break;
                }
            }
            return jsessionid;
        }
    }

    public String getRequestURI() {
        return requestURI;
    }

    public StringBuffer getRequestURL() {
        return null;
    }

    public String getServletPath() {
        return null;
    }



    public HttpSession getSession(boolean b) {
        return getSession();
    }

    public HttpSession getSession() {
        Session session = null;
        SessionContext sessionContext = new SessionContext();
        if (hasRequestedSessionURL) {
            session = (Session) sessionContext.getSession(requestedSessionId);
        } else {
            String jsessionid = "";
            for (Cookie cookie : cookies) {
                if (cookie.getName().trim().equalsIgnoreCase("JSESSIONID")) {
                    jsessionid = cookie.getValue();
                    break;
                }
            }
            session = (Session) sessionContext.getSession(jsessionid);
        }
        return session;
    }

    public boolean isRequestedSessionIdValid() {
        return !hasRequestedSessionURL;
    }

    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    public boolean isRequestedSessionIdFromURL() {
        return hasRequestedSessionURL;
    }

    public boolean isRequestedSessionIdFromUrl() {
        return hasRequestedSessionURL;
    }

    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
        return false;
    }

    public void login(String s, String s1) throws ServletException {

    }

    public void logout() throws ServletException {

    }

    public Collection<Part> getParts() throws IOException, ServletException {
        return null;
    }

    public Part getPart(String s) throws IOException, ServletException {
        return null;
    }

    public Object getAttribute(String s) {
        return attributes.get(s);
    }

    public Enumeration<String> getAttributeNames() {
        return null;
    }

    public String getCharacterEncoding() {
        return null;
    }

    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {
        this.charsetEncoding = s;
    }

    public int getContentLength() {
        return contentLength;
    }

    public String getContentType() {
        return contentType;
    }

    public ServletInputStream getInputStream() throws IOException {
        return (ServletInputStream) inputStream;
    }

    public String getParameter(String s) {
        if (method.equalsIgnoreCase("get")) {

        } else {
            if (contentLength > 0 && contentType.equals("application/x-www-form-urlencoded")) {
                int max = contentLength;
                int len = 0;
                byte[] bytes = new byte[max];
                while (len < max) {
                    try {
                        int next = inputStream.read(bytes, len, max - len);
                        if (next < 0) {
                            break;//读完整个字节流之后，再就都不到东西了，此时跳出循环
                        }
                        len += next;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (len < max) {//读取到的不完整
                    throw new RuntimeException("content length mismatch");
                }
                String requestBody = bytes.toString();
                while (requestBody != null && requestBody.length() != 0) {
                    boolean hasMore = true;
                    String parameterName = "";
                    String parameterValue = "";
                    int position = requestBody.indexOf("=");
                    int position2 = requestBody.indexOf("&");
                    if (position2 > 0) {
                        parameterName = requestBody.substring(0, position);
                        parameterValue = requestBody.substring(position + 1, position2);
                    } else {
                        parameterName = requestBody.substring(0, position);
                        parameterValue = requestBody.substring(position + 1);
                        hasMore = false;
                    }
                    String[] values = parameters.get(parameterName);
                    if (values == null) {
                        values = new String[1];
                        values[0] = parameterValue;
                        parameters.put(parameterName, values);
                    } else {
                        values = parameters.get(parameterName);
                        ArrayList arrayList = new ArrayList(Arrays.asList(values));
                        arrayList.add(parameterValue);
                        parameters.put(parameterName, (String[]) arrayList.toArray());
                    }
                    if (!hasMore) {
                        break;
                    }
                    requestBody = requestBody.substring(position2 + 1);
                }
            }
        }
        String[] strings = parameters.get(s);
        if (strings != null) {
            return strings[0];
        } else {
            return null;
        }
    }

    public Enumeration<String> getParameterNames() {
/*
        Enumeration var7 = Collections.enumeration(parameters.keySet());
        while(var7.hasMoreElements()) {
            String var6 = (String)var7.nextElement();
            System.out.println(var6+" -> "+getParameter(var6));
        }
*/
        return Collections.enumeration(parameters.keySet());
    }

    public String[] getParameterValues(String s) {
        return parameters.get(s);
    }

    public Map<String, String[]> getParameterMap() {
        return parameters;
    }

    public String getProtocol() {
        return protocal;
    }

    public String getScheme() {
        return protocolSchema;
    }

    public String getServerName() {
        return Constants.SERVER_NAME;
    }

    public int getServerPort() {
        return Constants.PORT;
    }

    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    public String getRemoteAddr() {
        return null;
    }

    public String getRemoteHost() {
        return null;
    }

    public void setAttribute(String s, Object o) {
        attributes.put(s, o);
    }

    public void removeAttribute(String s) {
        attributes.remove(s);
    }

    public Locale getLocale() {
        return null;
    }

    public Enumeration<Locale> getLocales() {
        return null;
    }

    public boolean isSecure() {
        return false;
    }

    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    public String getRealPath(String s) {
        return null;
    }

    public int getRemotePort() {
        return 0;
    }

    public String getLocalName() {
        return null;
    }

    public String getLocalAddr() {
        return null;
    }

    public int getLocalPort() {
        return PORT;
    }

    public ServletContext getServletContext() {
        return null;
    }

    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    public boolean isAsyncStarted() {
        return false;
    }

    public boolean isAsyncSupported() {
        return false;
    }

    public AsyncContext getAsyncContext() {
        return null;
    }

    public DispatcherType getDispatcherType() {
        return null;
    }
}
