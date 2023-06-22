package Game;

import Game.Table.*;

public interface GameBase {

    public int player_count = 4;
    public int card_count = 7;
    void startup();
    void end_game();
    void restart();
    void draw_cards();
    void play_card(String card);
    void process_card(String card);
    void process_command(String command);

    int get_trick_winner();

    boolean check_score();
    int get_game_winner();
    boolean check_round_end();
    void increase_score();
    boolean check_hand(Player player);
}
