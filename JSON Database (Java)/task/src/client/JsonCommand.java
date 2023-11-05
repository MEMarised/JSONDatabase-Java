package client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonCommand {
    private final String type;
    private final String[] key;
    private final com.google.gson.JsonObject value;

    private JsonCommand(JsonObjectBuilder builder) {
        this.type = builder.type;
        this.key = builder.key;
        this.value = builder.value;
    }

    public static class JsonObjectBuilder {
        private final String type;
        private String[] key;
        private com.google.gson.JsonObject value;

        public JsonObjectBuilder(String type) {
            this.type = type;
        }

        public JsonObjectBuilder key(String[] key) {
            this.key = key;
            return this;
        }

        public JsonObjectBuilder value(com.google.gson.JsonObject value) {
            this.value = value;
            return this;
        }

        public JsonCommand build() {
            return new JsonCommand(this);
        }
    }

    public static JsonObjectBuilder builder(String type) {
        return new JsonObjectBuilder(type);
    }

    public String[] getKey() {
        return key;
    }

    public com.google.gson.JsonObject getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public static JsonObject readFromFile(String fileName) throws IOException {
        String jsonData = Files.readString(Paths.get(fileName));
        return new Gson().fromJson(jsonData, JsonObject.class);
    }

}