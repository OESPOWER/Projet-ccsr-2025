package utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GoogleSearchService {
    private static final String API_KEY = "AIzaSyDpgJnDV923LpsVMRX5NgHAYtDb1fvYVd4";
    private static final String SEARCH_ENGINE_ID = "d074e68c554dc4d72";

    public static List<String> search(String query) throws IOException {
        List<String> results = new ArrayList<>();
        String urlString = "https://www.googleapis.com/customsearch/v1?q=" + query + "&key=" + API_KEY + "&cx=" + SEARCH_ENGINE_ID;

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        Scanner scanner = new Scanner(connection.getInputStream());
        StringBuilder response = new StringBuilder();
        while (scanner.hasNextLine()) {
            response.append(scanner.nextLine());
        }
        scanner.close();

        // Parse the JSON response
        JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
        JsonArray items = jsonObject.getAsJsonArray("items");

        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                JsonObject item = items.get(i).getAsJsonObject();
                String title = item.get("title").getAsString();
                String link = item.get("link").getAsString();
                results.add(title + " - " + link);
            }
        }

        return results;
    }
}