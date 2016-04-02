package org.leanpoker.player;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;

public class RankingLogic
{

    public static RankResponse doGet(ArrayList<Card> cards) throws IOException {
        Gson g = new Gson();
        final CloseableHttpClient client = HttpClients.createDefault();
        String s = g.toJson(cards);
        HttpGet httpget = new HttpGet("http://rainman.leanpoker.org/rank?cards="+ s +"");

        System.out.println("Executing request " + httpget.getRequestLine());
        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
            @Override
            public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }
        };

        String responseBody = client.execute(httpget, responseHandler);
        System.out.println("----------------------------------------");
        System.out.println(responseBody);

        return g.fromJson(new JsonParser().parse(responseBody).getAsJsonObject(), RankResponse.class);
    }
}
