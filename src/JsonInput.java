import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

public class JsonInput {
    public static ArrayList<Account> deserializeAccounts() {
        String accountPath = "D:\\LUCRARI\\TUDOR\\UPB\\AN 2\\SEM1\\POO\\tema1\\accounts.json";
        try {
            String content = new String((Files.readAllBytes(Paths.get(accountPath))));
            JSONObject obj = (JSONObject)new JSONParser().parse(content);
            JSONArray accountsArray = (JSONArray) obj.get("accounts");

            ArrayList<Account> accounts = new ArrayList<>();
            for (int i = 0; i < accountsArray.size(); i++) {
                JSONObject accountJson = (JSONObject) accountsArray.get(i);
                // name, country, games_number
                String name = (String) accountJson.get("name");
                String country = (String) accountJson.get("country");
                int gamesNumber = Integer.parseInt((String) accountJson.get("maps_completed"));

                // Credentials
                Credentials credentials = null;
                try {
                    JSONObject credentialsJson = (JSONObject) accountJson.get("credentials");
                    String email = (String) credentialsJson.get("email");
                    String password = (String) credentialsJson.get("password");

                    credentials = new Credentials(email, password);
                } catch (Exception e) {
                    System.out.println("! This account doesn't have all credentials !");
                }

                // Favorite games
                SortedSet<String> favoriteGames = new TreeSet<>();
                try {
                    JSONArray games = (JSONArray) accountJson.get("favorite_games");
                    for (int j = 0; j < games.size(); j++) {
                        favoriteGames.add((String) games.get(j));
                    }
                } catch (Exception e) {
                    System.out.println("! This account doesn't have favorite games !");
                }

                // Heroes
                ArrayList<Hero> heroes = new ArrayList<>();
                class HeroFactory {
                    public static Hero createHero(String profession, String hname, int experience, int level) {
                        switch (profession) {
                            case "Warrior":
                                return new Warrior(hname, experience, level);
                            case "Rogue":
                                return new Rogue(hname, experience, level);
                            case "Mage":
                                return new Mage(hname, experience, level);
                            default:
                                throw new IllegalArgumentException("Unknown profession: " + profession);
                        }
                    }
                }

                try {
                    JSONArray heroesListJson = (JSONArray) accountJson.get("characters");
                    for (int j = 0; j < heroesListJson.size(); j++) {
                        JSONObject heroJson = (JSONObject) heroesListJson.get(j);

                        String hname = (String) heroJson.get("name");
                        String profession = (String) heroJson.get("profession");
                        String level = (String) heroJson.get("level");
                        int lvl = Integer.parseInt(level);
                        int experience = ((Number) heroJson.get("experience")).intValue();

                        Hero newHero = HeroFactory.createHero(profession, hname, experience, lvl);
                        heroes.add(newHero);
                    }
                } catch (Exception e) {
                    System.out.println("! This account doesn't have heroes !");
                }

                Account.Information information = new Account.Information.Builder()
                        .setCredentials(credentials)
                        .setFavoriteGames(favoriteGames)
                        .setName(name)
                        .setCountry(country)
                        .build();
                Account account = new Account(heroes, gamesNumber, information);
                accounts.add(account);
            }
            return accounts;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
