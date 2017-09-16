package tomcat.core;

import tomcat.http.HttpRequest;
import tomcat.http.HttpResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by junhong on 17/9/15.
 */
public class StandarPipeline implements Pipeline {

    private Valve firstValve;

    private Valve basicValve;

    private Container container;

    @Override
    public void invoke(HttpRequest request, HttpResponse response) throws IOException, ServletException {
        getFirst().invoke(request, response);
    }

    @Override
    public Valve getFirst() {
        if(firstValve != null)
            return firstValve;

        return basicValve;
    }

    public void setFirstValve(Valve firstValve) {
        this.firstValve = firstValve;
    }

    @Override
    public void setBasicValve(Valve valve) {
        this.basicValve = valve;
    }

    @Override
    public Valve getBasicValve() {
        return null;
    }

    @Override
    public void addValve(Valve valve) {
        if(firstValve == null) {
            firstValve = valve;
            firstValve.setNext(basicValve);
            return;
        }

        Valve currentValve = firstValve;
        while (currentValve != null){
            if(currentValve.getNext() == basicValve){
                currentValve.setNext(valve);
                valve.setNext(basicValve);
                break;
            }
            currentValve = currentValve.getNext();
        }
    }

    @Override
    public Valve[] getValves() {

        ArrayList<Valve> valveList = new ArrayList<>();
        Valve currentValve = firstValve;
        if (currentValve == null) {
            currentValve = basicValve;
        }
        while (currentValve != null) {
            valveList.add(currentValve);
            currentValve = currentValve.getNext();
        }

        return valveList.toArray(new Valve[1]);
    }

    @Override
    public void removeValve(Valve valve) {
        if(valve == null) return;

        Valve currentValve;
        if(valve == firstValve) {
            firstValve = firstValve.getNext();
            currentValve = null;
            // 这个时候相当于已经移除了
        }else{
            currentValve = firstValve;
        }

        while (currentValve!= null){
            if(currentValve.getNext() == valve){
                currentValve.setNext(valve.getNext());
                // 直接用valve的next值搞定
                break;
            }
            currentValve = currentValve.getNext();
        }

        if(firstValve == basicValve)
            // 如果只剩下了一个阀门了,就只保留basicValve了
            firstValve = null;

        // TODO: 17/9/15  移除了阀门,则需要停止该阀门的生命周期
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public Container getContainer() {
        return this.container;
    }
}
