package server;

import com.beust.jcommander.Parameter;
import com.google.gson.JsonObject;

public class Request {
    @Parameter(names = "-t", required = true)
    private String type;

    @Parameter(names = "-k")
    private String key;

    @Parameter(names = "-v")
    private String value;

    public String toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", type);
        json.addProperty("key", key);
        if (value != null) {
            json.addProperty("value", value);
        }
        return json.toString();
    }
}