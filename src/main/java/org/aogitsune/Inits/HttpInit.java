package org.aogitsune.Inits;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;

import net.minestom.server.instance.InstanceContainer;
import org.aogitsune.Class.CrossplayDataClass;
import org.aogitsune.Utils.PlayersDataUtil;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.concurrent.*;


public class HttpInit {
    private static int countData = 0;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public HttpInit(CrossplayDataClass crossplayDataClass,
                    CrossplayDataManager crossplayDataManager,
                    InstanceContainer instanceContainer) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/crossplay", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream is = exchange.getRequestBody();
                String json = new BufferedReader(new InputStreamReader(is))
                        .lines()
                        .reduce("", (acc, line) -> acc + line);

                // Respond
                String jsonResponse = gson.toJson(crossplayDataClass);
                crossplayDataClass.Chats.clear();
                crossplayDataClass.Movements.clear();
                countData = 0;

                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(jsonResponse.getBytes());
                os.close();

                crossplayDataManager.executeData(gson.fromJson(json, CrossplayDataClass.class));
            } else {
                exchange.sendResponseHeaders(405, -1); // Method not allowed
            }
        });

        server.setExecutor(null);
        server.start();
        System.out.println("Java server started on http://localhost:8080");

        collectData(crossplayDataClass, instanceContainer);
    }

    private void collectData(CrossplayDataClass crossplayDataClass, InstanceContainer instanceContainer) {
        scheduler.scheduleAtFixedRate(() -> {
            countData ++;
            if (!instanceContainer.getPlayers().isEmpty() && gson.toJson(crossplayDataClass).length() < 200000 && countData <= 20) {
                instanceContainer.getPlayers().forEach(player -> crossplayDataClass.Movements.add(PlayersDataUtil.getPlayerData(player)));
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }
}
