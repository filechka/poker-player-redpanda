package org.leanpoker.player;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

class PlayerLogic {

    static final String VERSION = "grumpy space princess 1.0";

    public static int betRequest(JsonElement request)
    {
        Integer current_buy_in = request.getAsJsonObject().get("current_buy_in").getAsInt();
        Integer player_in_action = request.getAsJsonObject().get("in_action").getAsInt();
        Integer minimum_raise = request.getAsJsonObject().get("minimum_raise").getAsInt();
        Integer pot = request.getAsJsonObject().get("pot").getAsInt();
        Integer small_blind = request.getAsJsonObject().get("small_blind").getAsInt();
        final JsonArray players = request.getAsJsonObject().get("players").getAsJsonArray();
        System.out.println(player_in_action);

        Gson gson = new Gson();

        try {
            final Game game = gson.fromJson(request, Game.class);
            System.out.println(game);
            ArrayList<Card> ourCards = game.players.get(player_in_action).hole_cards;
            ArrayList cards = new ArrayList<>(game.community_cards);
            cards.addAll(ourCards);
            RankingLogic.doGet(cards);

            final Card first = ourCards.get(0);
            final Card second = ourCards.get(1);

            if (first.equals(second))  { // pair
                return callAmount(request) + minimum_raise * 3;
            }

            if (request.getAsJsonObject().get("round").getAsInt() > 0) {
                //flop and more
                ArrayList table  = new ArrayList<>(game.community_cards);
                for (int i = 0; i < table.size(); i++) {
                    if (table.get(i).equals(first)) return callAmount(request) + minimum_raise * 3;
                    if (table.get(i).equals(second)) return callAmount(request) + minimum_raise * 3;
                }

            }

            if (game.current_buy_in > players.get(player_in_action).getAsJsonObject().get("stack").getAsInt()/3) {
                return randExit(callAmount(request), 20);
            }

            if (request.getAsJsonObject().get("round").getAsInt() > 1) {
                if (current_buy_in > players.get(player_in_action).getAsJsonObject().get("stack").getAsInt()/4) {
                    return 0;
                }
            }

            if (request.getAsJsonObject().get("bet_index").getAsInt() > 2) {
                if (game.current_buy_in > players.get(player_in_action).getAsJsonObject().get("stack").getAsInt()/3) {
                    if (randExit(callAmount(request), 90) == 0) return 0;
                }
            }


        } catch (Exception e) {
            e.printStackTrace(); // hehehe
        }
        return randExit(callAmount(request), 90);

    }

    public static Integer callAmount(JsonElement request)
    {
        final JsonArray players = request.getAsJsonObject().get("players").getAsJsonArray();
        Integer player_in_action = request.getAsJsonObject().get("in_action").getAsInt();
        return request.getAsJsonObject().get("current_buy_in").getAsInt() -
                players.get(player_in_action).getAsJsonObject().get("bet").getAsInt();
    }
    public static void showdown(JsonElement game) {
    }

    public static boolean shitOnTable(JsonElement request, Game game, ArrayList<Card> ourCards) {

        RankResponse r = null;
        try {
            r = RankingLogic.doGet(ourCards);

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(r);

        return r.rank == 0;
    }

    public static int randExit(int money, int perc) {
        Random rand = new Random();
        int n = rand.nextInt(100);
        if (n < perc) {
            return 0;
        }
        return money;
    }
}


class Game {
    ArrayList<Card> community_cards;
    ArrayList<Player> players;
    Integer minimum_raise;
    Integer current_buy_in;

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
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        return rank.equals(card.rank);
    }

    @Override
    public String toString() {
        return "Card{" +
                "rank='" + rank + '\'' +
                ", suit='" + suit + '\'' +
                '}';
    }
}

class RankResponse
{
    Integer rank;
    String value;
    ArrayList<Card> cards;
    ArrayList<Card> cards_used;

    @Override
    public String toString()
    {
        return "RankResponse{" +
                "rank=" + rank +
                ", cards=" + cards +
                ", cards_used=" + cards_used +
                '}';
    }
}
