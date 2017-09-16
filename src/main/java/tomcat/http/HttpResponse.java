package tomcat.http;

import tomcat.core.Connector;
import tomcat.core.Container;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * Created by junhong on 17/9/3.
 */
public class HttpResponse implements ServletResponse {

    private OutputStream outputStream;
    private HttpRequest httpRequest;

    private Connector connector;

    private Container container;

    public HttpResponse() {
    }

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

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setHttpRequest(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public void returnMessage(){
        String messageFormat = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: %d\r\n" +
                "Server-name: junhong\r\n" +
                "\r\n" +
                "%s";
        String content = getContent();
        String message = String.format(messageFormat, content.length(), content);

        try {
            this.outputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getContent(){
        if(this.httpRequest == null)
            throw new NullPointerException("httprequest is null");
        StringBuffer sb = new StringBuffer();

        sb.append("<h4>URL : ").append(this.httpRequest.getUri()).append("</h4>");
        sb.append("<h4>Method : ").append(this.httpRequest.getMethod()).append("</h4>");

        Iterator it = this.httpRequest.getRequestHead().entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
            sb.append("<h5>").append(entry.getKey()).append(" : ").append(entry.getValue()).append("</h5>");
        }

        return sb.toString();
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        PrintWriter printWriter = new PrintWriter(this.outputStream, true);
        return printWriter;
    }

    @Override
    public void setCharacterEncoding(String charset) {

    }

    @Override
    public void setContentLength(int len) {

    }

    @Override
    public void setContentType(String type) {

    }

    @Override
    public void setBufferSize(int size) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale loc) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public void setContentLengthLong(long length) {

    }
}
