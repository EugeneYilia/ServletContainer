package server;

import config.ServletConfig;
import config.ServletConfigContext;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static server.Constants.BUFFER_SIZE;
import static server.Constants.RFC1123_DATE;
import static server.Constants.WEB_ROOT;
import static server.DefaultHeaders.CONNECTION_NAME;
import static server.DefaultHeaders.CONTENT_TYPE_NAME;

public class Response implements HttpServletResponse {
    private OutputStream outputStream = null;
    private PrintWriter printWriter = null;

    private Request request = null;

    private String protocol = "HTTP/1.1";
    private int status = 200;
    private String statusMessage;
    private String contentType;
    private int contentLength = -1;
    private String characterEncoding;
    private String connectionName;
    private String uri;
    private boolean isErrorStatusCommited = false;
    private boolean isServlet = false;
    private boolean isHeadersCommited = false;

    private List<Cookie> cookies = new ArrayList<>();
    private Map<String, String> headers = new HashMap<>();

    public void init(Request request) {
        uri = request.getRequestURI();
    }

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void addCookie(Cookie cookie) {
        if (isHeadersCommited) {
            return;
        } else {
            cookies.add(cookie);
        }

    }

    public void sendStaticResource() {
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fileInputStream = null;
        File file = new File(WEB_ROOT, uri);
        try {
            if (file.exists()) {
                sendHeaders();
                fileInputStream = new FileInputStream(file);
                int length;
                while ((length = fileInputStream.read(bytes, 0, BUFFER_SIZE)) != -1) {
                    outputStream.write(bytes, 0, length);
                    outputStream.flush();
                }
            } else {
                String mainMessage = "<h1>Eugene Hint:File Not Found</h1>";
                String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: " + mainMessage.getBytes("utf-8").length + "\r\n" +
                        "\r\n" +
                        mainMessage;
                outputStream.write(errorMessage.getBytes("utf-8"));
            }
        } catch (FileNotFoundException e) {
            System.out.println(request.getRequestURI()+" 资源不存在");
            //e.printStackTrace();
            try {
                sendError(SC_NOT_FOUND, getStatusMessage(SC_NOT_FOUND));
                sendHeaders();
            }catch (IOException e2){

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }

    public boolean containsHeader(String s) {
        return headers.containsKey(s);
    }

    public String encodeURL(String s) {
        return null;
    }

    public String encodeRedirectURL(String s) {
        return null;
    }

    public String encodeUrl(String s) {
        return null;
    }

    public String encodeRedirectUrl(String s) {
        return null;
    }

    public void sendError(int i, String s) throws IOException {
        if (!isErrorStatusCommited) {
            setStatus(i);
            setStatusMessage(getStatusMessage(i));
        }
        isErrorStatusCommited = true;
    }


    public void sendHeaders() {
        if (isHeadersCommited) {
            return;
        }
        PrintWriter printWriter = null;
        try {
            printWriter = getPrintWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        statusMessage = getStatusMessage(status);

        printWriter.print(protocol + " ");
        printWriter.print(status + " ");
        printWriter.println(statusMessage);

        if (contentType != null) {
            printWriter.println("Content-Type: " + contentType);
        }

        printWriter.println("Last-Modified: "+System.currentTimeMillis());

        if (contentLength >= 0) {
            printWriter.print("Content-Length: ");
            printWriter.println(contentLength);
        }

        if (cookies != null && cookies.size() > 0) {
            for (int i = 0; i < cookies.size(); i++) {
                printWriter.print("Set-Cookie: ");
                printWriter.print(cookies.get(i).getName());
                printWriter.print("=");
                printWriter.println(cookies.get(i).getValue());
                printWriter.print(";");
                if (cookies.get(i).getDomain() != null) {
                    printWriter.print(" Path=");
                    printWriter.print(cookies.get(i).getDomain());
                }
                printWriter.println();
            }
        }

        if (request.getRequestURI().contains("/servlet/")) {
            String correspondingServletName = "";
            ArrayList<ServletConfig> servletConfigArrayList = ServletConfigContext.getServletConfigArrayList();
            for(int i =0;i< servletConfigArrayList.size();i++){
                if(servletConfigArrayList.get(i).getMappingUri().equals(request.getRequestURI())){
                    correspondingServletName = servletConfigArrayList.get(i).getClassName();
                    break;
                }
            }
            if(correspondingServletName.length()!=0) {
                printWriter.print("Set-Cookie: ");
                printWriter.print("JSESSIONID=");
                printWriter.print(request.getSession().getId());
                printWriter.print(";");
                printWriter.print(" Path=");
                printWriter.println("/");
            }
        }

        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                printWriter.print(entry.getKey());
                printWriter.print(": ");
                printWriter.println(entry.getValue());
            }
        }

        printWriter.println();

        isHeadersCommited = true;
    }

    public void sendError(int i) throws IOException {
        if (!isErrorStatusCommited) {
            setStatus(i);
        }
        isErrorStatusCommited = true;
    }

    public void sendRedirect(String newLocation) throws IOException {
        setStatus(SC_FOUND);
        setStatusMessage(getStatusMessage(SC_FOUND));
        setHeader("Location", newLocation);
    }

    public void setDateHeader(String name, long value) {
        if (name == null || name.length() == 0) {
            return;
        }

        if (isHeadersCommitted()) {
            return;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(RFC1123_DATE, Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        setHeader(name, simpleDateFormat.format(value));
    }

    public void addDateHeader(String name, long value) {
        setDateHeader(name, value);
    }

    public void setHeader(String s, String s1) {
        if (isHeadersCommited) {
            return;
        } else {
            headers.put(s, s1);
        }


        if (s.equalsIgnoreCase(CONNECTION_NAME)) {
            this.connectionName = s1;
        } else if (s.equalsIgnoreCase(CONTENT_TYPE_NAME)) {
            setContentType(s1);
        } else if (s.equalsIgnoreCase(CONTENT_TYPE_NAME)) {
            int contentLength = -1;
            try {
                contentLength = Integer.parseInt(s1.trim());
            } catch (NumberFormatException e) {

            }
            if (contentLength >= 0) {
                setContentLength(contentLength);
            }
        }
        //cookie的添加需要自己addCookie来进行添加
    }

    public void addHeader(String s, String s1) {
        setHeader(s, s1);
    }

    public void setIntHeader(String s, int i) {

    }

    public void addIntHeader(String s, int i) {

    }

    public void setStatus(int i) {
        this.status = i;
    }

    public void setStatus(int i, String s) {
        this.status = i;
        this.statusMessage = s;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusMessage(int status) {
        switch (status) {
            case SC_OK:
                return ("OK");
            case SC_ACCEPTED:
                return ("Accepted");
            case SC_BAD_GATEWAY:
                return ("Bad Gateway");
            case SC_BAD_REQUEST:
                return ("Bad Request");
            case SC_CONFLICT:
                return ("Conflict");
            case SC_CONTINUE:
                return ("Continue");
            case SC_CREATED:
                return ("Created");
            case SC_EXPECTATION_FAILED:
                return ("Expectation Failed");
            case SC_FORBIDDEN:
                return ("Forbidden");
            case SC_GATEWAY_TIMEOUT:
                return ("Gateway Timeout");
            case SC_GONE:
                return ("Gone");
            case SC_HTTP_VERSION_NOT_SUPPORTED:
                return ("HTTP Version Not Supported");
            case SC_INTERNAL_SERVER_ERROR:
                return ("Internal Server Error");
            case SC_LENGTH_REQUIRED:
                return ("Length Required");
            case SC_METHOD_NOT_ALLOWED:
                return ("Method Not Allowed");
            case SC_MOVED_PERMANENTLY:
                return ("Moved Permanently");
            case SC_MOVED_TEMPORARILY:
                return ("Moved Temporarily");
            case SC_MULTIPLE_CHOICES:
                return ("Multiple Choices");
            case SC_NO_CONTENT:
                return ("No Content");
            case SC_NON_AUTHORITATIVE_INFORMATION:
                return ("Non-Authoritative Information");
            case SC_NOT_ACCEPTABLE:
                return ("Not Acceptable");
            case SC_NOT_FOUND:
                return ("Not Found");
            case SC_NOT_IMPLEMENTED:
                return ("Not Implemented");
            case SC_NOT_MODIFIED:
                return ("Not Modified");
            case SC_PARTIAL_CONTENT:
                return ("Partial Content");
            case SC_PAYMENT_REQUIRED:
                return ("Payment Required");
            case SC_PRECONDITION_FAILED:
                return ("Precondition Failed");
            case SC_PROXY_AUTHENTICATION_REQUIRED:
                return ("Proxy Authentication Required");
            case SC_REQUEST_ENTITY_TOO_LARGE:
                return ("Request Entity Too Large");
            case SC_REQUEST_TIMEOUT:
                return ("Request Timeout");
            case SC_REQUEST_URI_TOO_LONG:
                return ("Request URI Too Long");
            case SC_REQUESTED_RANGE_NOT_SATISFIABLE:
                return ("Requested Range Not Satisfiable");
            case SC_RESET_CONTENT:
                return ("Reset Content");
            case SC_SEE_OTHER:
                return ("See Other");
            case SC_SERVICE_UNAVAILABLE:
                return ("Service Unavailable");
            case SC_SWITCHING_PROTOCOLS:
                return ("Switching Protocols");
            case SC_UNAUTHORIZED:
                return ("Unauthorized");
            case SC_UNSUPPORTED_MEDIA_TYPE:
                return ("Unsupported Media Type");
            case SC_USE_PROXY:
                return ("Unsupported Media Type");
            case 207:
                return ("Multi-Status");
            case 422:
                return ("semantic error");
            case 423:
                return ("Locked");
            case 507:
                return ("Insufficient Storage");
            default:
                return ("HTTP Response Status " + status);
        }
    }

    public String getHeader(String s) {
        return headers.get(s);
    }

    public Collection<String> getHeaders(String s) {
        return headers.values();
    }

    public Collection<String> getHeaderNames() {
        return headers.keySet();
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public String getContentType() {
        return contentType;
    }

    public ServletOutputStream getOutputStream() throws IOException {
        sendHeaders();
        return getServletOutputStream();
    }

    public ServletOutputStream getServletOutputStream() throws IOException {
        return new CoyoteOutputStream(this.outputStream);
    }

    public PrintWriter getWriter() throws IOException {
        sendHeaders();//发送已经设置好的消息头
        return getPrintWriter();
    }

    public PrintWriter getPrintWriter() throws IOException {
        return new PrintWriter(getServletOutputStream(), true);
    }

    public void setCharacterEncoding(String s) {
        this.characterEncoding = s;
    }

    public void setContentLength(int i) {
        contentLength = i;
    }

    public void setContentType(String s) {
        contentType = s;
    }

    public void setBufferSize(int i) {
        Constants.setBufferSize(i);
    }

    public int getBufferSize() {
        return Constants.getBufferSize();
    }

    public void flushBuffer() throws IOException {
        outputStream.flush();
    }

    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return isHeadersCommited;
    }

    public boolean isHeadersCommitted() {
        return isHeadersCommited;
    }

    public void reset() {

    }

    public void setLocale(Locale locale) {

    }

    public Locale getLocale() {
        return null;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getContentLength() {
        return this.contentLength;
    }

    public void setCommited(boolean isHeaderscommited) {
        isHeadersCommited = isHeadersCommited;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
