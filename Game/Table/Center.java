package Game.Table;
import java.util.*;

import Game.Cards;

public class Center extends Cards{
    public Center()
    {
        cards = new LinkedHashSet<String>();
    }

    private String first_card;

    public int get_first_player()
    {
        String[] p1 = {"A", "5", "9", "K"};
        String[] p2 = {"2", "6", "10"};
        String[] p3 = {"3", "7", "J"};
        String[] p4 = {"4", "8", "Q"};

        for (int i = 0; i < 4; i++)
        {
            if (first_card.contains(p1[i])) { return 1; }
            if (first_card.contains(p2[i])) { return 2; }
            if (first_card.contains(p3[i])) { return 3; }
            if (first_card.contains(p4[i])) { return 4; }
        }
        
        return 0;
    }

    public int compareRanks(String rank1, String rank2) {
        int rank1Index = Arrays.asList(rank).indexOf(rank1);
        int rank2Index = Arrays.asList(rank).indexOf(rank2);
        return Integer.compare(rank1Index, rank2Index);
    }
    
    public boolean check_playable_card(String card)
    {
        if (cards.isEmpty()) return true;
        if (getCardSuit(card).equals(get_center_suit())) return true;
        if (getCardRank(card).equals(get_center_rank())) return true;

        return false;
    }

    public int getTrickWinner(int player_count) {
        String leadSuit = get_center_suit();
        String highestRankCard = "";
        int winner_key = 0;
        Iterator<String> cards_Iterator = cards.iterator();
        int counter = 0;
        while (cards_Iterator.hasNext())
        {
            String card = cards_Iterator.next();
            String cardSuit = getCardSuit(card);
            String cardRank = getCardRank(card);
            
            if (cardSuit.equals(leadSuit)) {
                if (highestRankCard.isEmpty() || compareRanks(cardRank, getCardRank(highestRankCard)) > 0) {
                    highestRankCard = card;
                    winner_key = counter;
                }
            }
            counter++;
        }
        if (cards.size() > player_count) winner_key -= 1;
        return winner_key;
    }

    public boolean check_same_suit_or_rank(String c) {
        if (cards.isEmpty()) {
            return true;
        }

        String centerSuit = get_center_suit();
        String centerRank = get_center_rank();

        String cardSuit = c.substring(0, 1);
        String cardRank = c.substring(1);

        return cardSuit.equals(centerSuit) || cardRank.equals(centerRank);
    }
    
    public String get_center_suit() { return getCardSuit(first_card); }
    
    public String get_center_rank() { return getCardRank(first_card); }

    public String get_center() { return first_card; }

    public void add_card(String card) {
        if (cards.size() <= 0)
        {
            first_card = card;
        }
        cards.add(card);
    }

    public void reset() {
        first_card = "";
        cards.clear();
    }
}
