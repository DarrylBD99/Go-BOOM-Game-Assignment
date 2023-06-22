package Game.Table;
import java.util.*;

import Game.Cards;

public class Player extends Cards{
    private int score = 0;

    public void remove_card(String card) { cards.remove(card); }

    public int get_score() { return score; }

    public void set_score(int score) { this.score = score; }
    
    public void increase_core() 
    {
        cards.forEach((card) -> {
            
            String card_rank = card.substring(1);
            List<String> rank_list = Arrays.asList(rank);
            int index = rank_list.indexOf(card_rank);
            if (index == rank_list.indexOf("A")) score += 1;
            else if (index > rank_list.indexOf("10")) score += 10;
            else score += index + 2;
        });
    }
    
    public void add_card(String card) {
        cards.add(card);
    }
    
    public void reset() {
        cards.clear();
    }
}
