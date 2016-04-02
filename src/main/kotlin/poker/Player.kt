package poker

import org.json.JSONObject
import com.fasterxml.jackson.module.kotlin.*

class Player {
    fun betRequest(game_state: JSONObject): Int {

        val mapper = jacksonObjectMapper()

        val currentPlayerIndex = game_state.get("in_action")
        val allPlayers = game_state.getJSONArray("players")
        val state: GameState = mapper.readValue<GameState>(game_state)

        //current_buy_in - players[in_action][bet]
        return state.current_buy_in - state.players[state.current_buy_in].bet
    }

    fun showdown() {
    }

    fun version(): String {
        return "Kotlin Player 0.0.1"
    }
}

data class GameState(
    val game_state: String,
    val players: Array<SimplePlayer>,
    val current_buy_in: Int,
    val in_action: Int,
    val community_cards: Array<String>)

data class SimplePlayer(
    val bet: Int
)