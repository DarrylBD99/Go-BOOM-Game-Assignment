package Game.Table;
import java.util.*;
import Game.Cards;

public class Deck extends Cards{
    public Deck() {
        generateDeck();
        shuffleDeck();
    }

    private void generateDeck() {
        for (String s : suit) {
            for (String r : rank) {
                cards.add(s + r);
            }
        }
    }

    private void shuffleDeck() {
        List<String> cards_list = new ArrayList<String>(cards);
        Collections.shuffle(cards_list);
        cards = new LinkedHashSet<String>(cards_list);
    }
    
    public Set<String> distributeCards(int numPlayers, int numCards) {
        Set<String> playerCards = new HashSet<String>();
        for (int j = 0; j < numCards; j++) {
            playerCards.add(remove_card());
        }
        return playerCards;
    }

    public Map<Integer, Player> distributeCards(int numPlayers, int numCards, Map<Integer, Player> players) {
        for (int i = 1; i <= numPlayers; i++) {
            Player player = players.get(i);
            player.reset();
            for (int j = 0; j < numCards; j++) {
                player.add_card(remove_card());
            }
        }
        return players;
    }

    public boolean isCard(String c)
    {
        String c_suit = getCardSuit(c);
        String c_rank = getCardRank(c);
        List<String> suit_l = Arrays.asList(suit);
        List<String> rank_l = Arrays.asList(rank);

        if (suit_l.contains(c_suit) && rank_l.contains(c_rank)) return true;
        return false;
    }

    public void add_card(String card) { cards.add(card); }

    public void reset()
    {
        cards.clear();
        generateDeck();
        shuffleDeck();
    }
}
