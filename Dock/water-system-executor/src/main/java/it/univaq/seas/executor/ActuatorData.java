package it.univaq.seas.executor;

import java.util.HashMap;
import java.util.Map;

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
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
