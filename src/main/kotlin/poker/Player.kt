package poker

import org.json.JSONObject

class Player {
    fun betRequest(game_state: JSONObject): Int {
        if (Math.random() * 100 < 20) {
          return Math.floor(JSONObject.players filter {it.name="RedPanda"} it.stack/50)
        }
        return 1
    }

    fun showdown() {
    }

    fun version(): String {
        return "Kotlin Player 0.0.1"
    }
}
