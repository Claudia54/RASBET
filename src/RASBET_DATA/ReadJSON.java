package RASBET.RASBET_DATA;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.*;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import RASBET.RASBET.Game;

public class ReadJSON {
    private String filename = "src/RASBET_DATA/teste.json";
    private String URL = "http://ucras.di.uminho.pt/v1/games/";

    public List<Game> readFile(int idSport) {
        List<Game> games = new ArrayList<>();
        try {
            FileReader fr = new FileReader(filename);
            JSONParser parser = new JSONParser();
            JSONArray gamesList = (JSONArray) parser.parse(fr);
            ListIterator<JSONObject> gamesListIterator = gamesList.listIterator();
            while (gamesListIterator.hasNext()) {
                JSONObject j = gamesListIterator.next();
                games.add(parseGameObject(j, idSport));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return games;
    }

    public static Game parseGameObject(JSONObject game, int idSport) {
        JSONArray bookmakersarray = (JSONArray) game.get("bookmakers");
        Map<String, Float> odds = new HashMap<>();
        ListIterator<JSONObject> bookmakersIterator = bookmakersarray.listIterator();
        boolean sucesso = false;
        while (bookmakersIterator.hasNext()) {
            JSONObject j = bookmakersIterator.next();
            if (j.get("key").equals("betclic")) {
                odds = parseOddsObject(j);
                sucesso = true;
            }
        }

        if(!sucesso){
            bookmakersIterator = bookmakersarray.listIterator();
            JSONObject j = bookmakersIterator.next();
            odds = parseOddsObject(j);
        }

        String id = (String) game.get("id");
        String away = (String) game.get("awayTeam");
        String home = (String) game.get("homeTeam");
        String scores = (String) game.get("scores");
        String date = (String) game.get("commenceTime"); //2022-10-21T19:15:00.000Z

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        ZonedDateTime dateGameZ = ZonedDateTime.parse(date);
        LocalDateTime dateGame = dateGameZ.toLocalDateTime();

        float oddHome = odds.get(home).floatValue();
        float oddAway = odds.get(away).floatValue();
        float draw = odds.get("Draw").floatValue();

        Game g;
        if((boolean) game.get("completed") == false){
            g = new Game(id, idSport, home, oddHome,draw,oddAway, away, dateGame);
        }
        else{
            String out = Game.parseResult(scores);
            g = new Game(id, idSport, Game.closed(), home, oddHome, draw, oddAway, away, out, dateGame);
        }
        return g;
    }

    private static Map<String, Float> parseOddsObject(JSONObject bookmakers) {
        Map<String, Float> map = new HashMap<>();
        JSONArray markets = (JSONArray) bookmakers.get("markets");
        ListIterator<JSONObject> marketsIterator = markets.listIterator();
        while (marketsIterator.hasNext()) {
            JSONArray outcomes = (JSONArray) marketsIterator.next().get("outcomes");
            ListIterator<JSONObject> outcomesIterator = outcomes.listIterator();
            while (outcomesIterator.hasNext()) {
                JSONObject j = outcomesIterator.next();
                String name = (String) j.get("name");
                float price = 0;
                Object priceAux = j.get("price");
                if (priceAux == null)
                    price = 0;
                if (priceAux instanceof Float)
                    price = (Float) priceAux;
                try {
                    price = Float.valueOf(priceAux.toString());
                }catch (Exception e){}
                map.put(name, price);
            }
        }
        return map;
    }

    public void getRequest(){
        try {
            java.net.URL url = new URL(this.URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("accept","application/json");
            con.setRequestMethod("GET");

            int status = con.getResponseCode();

            //Read data from request and writes into file?
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            BufferedWriter out = new BufferedWriter(new FileWriter(this.filename));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                out.write(inputLine);
            }
            in.close();
            out.close();
        }catch (Exception e){}
    }


}
