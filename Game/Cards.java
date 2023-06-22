package Game;
import java.util.*;

public abstract class Cards{
    protected static String[] suit = {"d", "s", "h", "c"};
    protected static String[] rank = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    protected Set<String> cards = new HashSet<String>();

    public abstract void add_card(String card);

    public abstract void reset();

    public String remove_card()
    {
        Iterator<String> cards_Iterator = cards.iterator();
        String card = cards_Iterator.next();
        cards.remove(card);

        return card;
    }

    public String getCardSuit(String card) {
        return card.substring(0, 1);
    }

    public String getCardRank(String card) {
        return card.substring(1);
    }

    public Set<String> get_cards() { return cards; }

    public void printCards() {
        System.out.print("[");
        Iterator<String> cards_Iterator = cards.iterator();

        for (int i = 0; i < cards.size(); i++) {
            System.out.print(cards_Iterator.next());
            if (i != cards.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    public void set_cards(Set<String> cards)
    {
        this.cards = cards;
    }
}
