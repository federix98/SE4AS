package it.univaq.seas.executor;

import java.util.HashMap;
import java.util.Map;


/**
 * @author federico
 */
public class ActuatorData {

    private Map<String, String> data = null;

    public ActuatorData() {
        this.data = new HashMap<String,String>();
    }

    public void insertData(String key, String value) {
        this.data.put(key, value);
    }

    public void removeData(String key) {
        this.data.remove(key);
    }

    public Map<String, String> getData() {
        System.out.println(data.toString());
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
