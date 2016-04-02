package org.leanpoker.player;

import com.google.gson.JsonElement;

import java.util.Map;

public class Player {

    static final String VERSION = "Default Java folding player";

    public static int betRequest(JsonElement request)
    {
        Integer current_buy_in = request.getAsJsonObject().get("current_buy_in").getAsInt();
        Integer player_in_action = request.getAsJsonObject().get("in_action").getAsInt();
        return request.getAsJsonObject().get("current_buy_in").getAsInt() -
                request.getAsJsonObject().get("players").getAsJsonArray().get(player_in_action).getAsJsonObject().get("bet").getAsInt();
    }

    public static void showdown(JsonElement game) {
    }
}
