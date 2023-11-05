package server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {
    private static final Gson gson = new Gson();

    private ResponseUtil() {

    }

    public static JsonObject responseOkWithValue(String value) {
        Map<String, String> json = new HashMap<>(10);
        json.put(CommandUtils.RESPONSE, CommandUtils.RESPONSE_OK);
        json.put(CommandUtils.VALUE, value);
        return JsonParser.parseString(gson.toJson(json)).getAsJsonObject();
    }

    public static JsonObject responseOkWithJsonObj(JsonObject jsonObj) {
        Map<String, JsonElement> json = new HashMap<>(10);
        json.put(CommandUtils.RESPONSE, JsonParser.parseString(CommandUtils.RESPONSE_OK).getAsJsonPrimitive());
        json.put(CommandUtils.VALUE, jsonObj);
        return JsonParser.parseString(gson.toJson(json)).getAsJsonObject();
    }

    public static JsonObject responseOk() {
        JsonObject json = new JsonObject();
        json.addProperty(CommandUtils.RESPONSE, CommandUtils.RESPONSE_OK);
        return json;
    }

    public static JsonObject responseError() {
        Map<String, String> json = new HashMap<>(10);
        json.put(CommandUtils.RESPONSE, CommandUtils.RESPONSE_ERROR);
        json.put(CommandUtils.REASON, CommandUtils.RESPONSE_ERROR_REASON);
        return JsonParser.parseString(new Gson().toJson(json)).getAsJsonObject();
    }

    public static JsonObject responseOkBinary(JsonElement value) {
        if (value.isJsonPrimitive()) {
            return responseOkWithValue(value.getAsString());
        } else {
            return responseOkWithJsonObj(value.getAsJsonObject());
        }
    }
}