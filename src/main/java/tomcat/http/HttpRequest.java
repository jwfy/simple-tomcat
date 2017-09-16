package tomcat.http;

import tomcat.core.Connector;
import tomcat.core.Container;

import javax.servlet.*;
import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by junhong on 17/9/3.
 */
public class HttpRequest implements ServletRequest {

    private InputStream inputStream;
    private String protocol;   // HTTP/1.1
    private String uri;        // test.com
    private String method;          // GET
    private static String head_re = "\\s*([A-Z]{3,4})\\s+(.+)\\s+(.+)\\s*";
    private static String parament_re = "\\s*(.+)?\\s*:\\s*(.+)\\s*";
    private Map<String, String> requestHead = new HashMap<String, String>();
    private Container container;

    private Connector connector;

    public Connector getConnector() {
        return connector;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getUri() {
        return uri;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, String> getRequestHead() {
        return requestHead;
    }

    /**
     * 解析inputStream
     */
    public void parse() throws Exception {
        InputStreamReader inr = new InputStreamReader(inputStream, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inr);

        String head = bufferedReader.readLine();
        parseRequest(head);

        String currentLine = null;
        while (!(currentLine = bufferedReader.readLine()).isEmpty()){
            Pattern pattern = Pattern.compile(parament_re);
            Matcher matcher = pattern.matcher(currentLine);

            if(matcher.find()){
                String name = matcher.group(1);
                String value = matcher.group(2);
                this.requestHead.put(name, value);
            }
        }
    }

    private void parseRequest(String head) throws Exception {
        // 解析 GET TEST HTTP/1.1
        if(head == null || head.length() <= 0)
            throw new NullPointerException("head is invalid");
        Pattern pattern = Pattern.compile(head_re);
        Matcher matcher = pattern.matcher(head);
        if(matcher.find()){
            this.method = matcher.group(1);
            this.uri = matcher.group(2);
            this.protocol = matcher.group(3);
            return;
        }
        throw new Exception("head info:" + head + "is invalid");
    }

    @Override
    public Object getAttribute(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {

    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public String getParameter(String name) {
        return requestHead.getOrDefault(name, null);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return null;
    }

    @Override
    public String[] getParameterValues(String name) {
        return new String[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return null;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public void setAttribute(String name, Object o) {

    }

    @Override
    public void removeAttribute(String name) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }

    @Override
    public String getRealPath(String path) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    @Override
    public long getContentLengthLong() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }


}
