package org.leanpoker.player;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Random;

class PlayerLogic {

    static final String VERSION = "Princess Bubblegum";

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


            if (request.getAsJsonObject().get("rank").getAsInt() > 2) {
                if (game.minimum_raise > players.get(player_in_action).getAsJsonObject().get("stack").getAsInt()/4) {
                        return 0;
                }
            }

            if (request.getAsJsonObject().get("bet_index").getAsInt() > 2) {
                if (game.minimum_raise > players.get(player_in_action).getAsJsonObject().get("stack").getAsInt()/3) {
                    if (randExit(1, 70) == 0) return 0;
                }
            }


            if (shitOnTable(request, game)) {
                return 0;
            }

            ArrayList<Card> ourCards = game.players.get(player_in_action).hole_cards;
            ArrayList cards = new ArrayList<>(game.community_cards);
            cards.addAll(ourCards);
            RankResponse r = RankingLogic.doGet( cards );
            System.out.println(r);

            final Card first = ourCards.get(0);
            final Card second = ourCards.get(1);

            if (first.equals(second))  { // pair
                return request.getAsJsonObject().get("current_buy_in").getAsInt() -
                        players.get(player_in_action).getAsJsonObject().get("bet").getAsInt() + minimum_raise * 3;
            }

            if (game.minimum_raise > players.get(player_in_action).getAsJsonObject().get("stack").getAsInt()/3) {
                return randExit(request.getAsJsonObject().get("current_buy_in").getAsInt() -
                        players.get(player_in_action).getAsJsonObject().get("bet").getAsInt(), 20);
            }


            System.out.println(game);

        } catch (Exception e) {
            e.printStackTrace(); // hehehe
        }

        return request.getAsJsonObject().get("current_buy_in").getAsInt() -
                players.get(player_in_action).getAsJsonObject().get("bet").getAsInt();

    }

    public static void showdown(JsonElement game) {
    }

    public static boolean shitOnTable(JsonElement request, Game game) {
        //ArrayList<Card> tableCards =
        return false;
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
    Integer stack;

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

        if (rank != null ? !rank.equals(card.rank) : card.rank != null) return false;
        return !(suit != null ? !suit.equals(card.suit) : card.suit != null);

    }

    @Override
    public int hashCode() {
        int result = rank != null ? rank.hashCode() : 0;
        result = 31 * result + (suit != null ? suit.hashCode() : 0);
        return result;
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
