package org.leanpoker.player;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Map;

class PlayerLogic {

    static final String VERSION = "Default Java folding player";

    public static int betRequest(JsonElement request)
    {
        Integer current_buy_in = request.getAsJsonObject().get("current_buy_in").getAsInt();
        Integer player_in_action = request.getAsJsonObject().get("in_action").getAsInt();
        Integer minimum_raise = request.getAsJsonObject().get("minimum_raise").getAsInt();
        Integer pot = request.getAsJsonObject().get("pot").getAsInt();
        Integer small_blind = request.getAsJsonObject().get("small_blind").getAsInt();
        final JsonArray players = request.getAsJsonObject().get("players").getAsJsonArray();
        Gson gson = new Gson();

        try {
            final Game game = gson.fromJson(request, Game.class);
            game.setStack(players.get(player_in_action).getAsJsonObject().get("stack").getAsInt());
            System.out.println(game);
        } catch (Exception e)
        {
            e.printStackTrace(); // hehehe
        }

        if (game.minimum_raise > game.stack/3) {
          Random rand = new Random();
          int n = rand.nextInt(100);
          if (n > 80)
          {
            return 0;
          }
        }

        return request.getAsJsonObject().get("current_buy_in").getAsInt() -
                players.get(player_in_action).getAsJsonObject().get("bet").getAsInt();
    }

    public static void showdown(JsonElement game) {
    }
}


class Game {
    ArrayList<Card> community_cards;
    ArrayList<Player> players;
    Integer minimum_raise;
    Integer stack;

    public void setStack(stack) {
      this.stack = stack;
    }

    @Override
    public String toString() {
        return "Game{" +
                "community_cards=" + community_cards +
                ", players=" + players +
                ", minimum_raise=" + minimum_raise +
                '}';
    }
}
class Player {
    String id;
    ArrayList<Card> hole_cards;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Card> getHole_cards() {
        return hole_cards;
    }

    public void setHole_cards(ArrayList<Card> hole_cards) {
        this.hole_cards = hole_cards;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id='" + id + '\'' +
                ", hole_cards=" + hole_cards +
                '}';
    }
}

class Card {
    String rank;
    String suit;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "Card{" +
                "rank='" + rank + '\'' +
                ", suit='" + suit + '\'' +
                '}';
    }
}
