package com.andmal.graalvm.lambda;

import com.amazonaws.lambda.thirdparty.com.google.gson.JsonArray;
import com.amazonaws.lambda.thirdparty.com.google.gson.JsonObject;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HelloWorldRequestHandler implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    @Override
    public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent event, Context context) {
        try {
            System.out.println("Inside HelloWorldRequestHandler::handleRequest()");
            Document document =
                    Jsoup.parse(new URL("https://en.wikipedia.org/wiki/2023_Turkey%E2%80%93Syria_earthquake"), 10000);

            Elements links = document.select("a");

            List<String> href = links.stream().map(l -> l.attr("href")).collect(Collectors.toList());

            JsonArray jsonArray = new JsonArray();
            JsonObject object = new JsonObject();

            href.forEach(link -> {
                object.addProperty("href", link);
                jsonArray.add(object);
            });

            object.addProperty("count", href.size());

            return APIGatewayV2HTTPResponse.builder()
                    .withHeaders(Map.of(
                            "Country", "Ukraine",
                            "Content-Type", "application/json",
                            "Accept", "application/json"
                    ))
                    .withBody(jsonArray.toString())
                    .withStatusCode(200)
                    .build();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
