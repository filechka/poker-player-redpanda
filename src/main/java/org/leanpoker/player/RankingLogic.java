package org.leanpoker.player;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.ArrayList;

public class RankingLogic
{

    public static RankResponse doGet(ArrayList<Card> cards)
    {
        final CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://rainman.leanpoker.org/rank");

        return null;
    }
}
