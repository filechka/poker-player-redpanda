package poker

import org.json.JSONObject

class Player {
    fun betRequest(game_state: JSONObject): Int {
        val currentPlayerIndex = game_state.get("in_action")
        val allPlayers = game_state.getJSONArray("players")

        return 0
    }

    fun showdown() {
    }

    fun version(): String {
        return "Kotlin Player 0.0.1"
    }
}
