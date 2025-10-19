import java.util.ArrayList;
import java.util.SortedSet;

public class Account {
    private ArrayList<Hero> heroes;
    private int gamesCompleted;
    private Information information;

    public Account(ArrayList<Hero> heroes, int gamesCompleted, Information information) {
        this.heroes = heroes;
        this.gamesCompleted = gamesCompleted;
        this.information = information;
    }

    public ArrayList<Hero> getHeroes() {
        return heroes;
    }

    public int getGamesCompleted() {
        return gamesCompleted;
    }

    public Information getInformation() {
        return information;
    }
    public void setGamesCompleted(int gamesCompleted) {
        this.gamesCompleted = gamesCompleted;
    }

    static class Information {
        private Credentials credentials;
        private SortedSet<String> favoriteGames;
        private String name;
        private String country;

        private Information(Builder builder) {
            this.credentials = builder.credentials;
            this.favoriteGames = builder.favoriteGames;
            this.name = builder.name;
            this.country = builder.country;
        }

        public Credentials getCredentials() {
            return credentials;
        }

        public SortedSet<String> getFavoriteGames() {
            return favoriteGames;
        }

        public String getName() {
            return name;
        }

        public String getCountry() {
            return country;
        }

        public static class Builder {
            private Credentials credentials;
            private SortedSet<String> favoriteGames;
            private String name;
            private String country;

            public Builder setCredentials(Credentials credentials) {
                this.credentials = credentials;
                return this;
            }

            public Builder setFavoriteGames(SortedSet<String> favoriteGames) {
                this.favoriteGames = favoriteGames;
                return this;
            }

            public Builder setName(String name) {
                this.name = name;
                return this;
            }

            public Builder setCountry(String country) {
                this.country = country;
                return this;
            }

            public Information build() {
                return new Information(this);
            }
        }
    }
}
