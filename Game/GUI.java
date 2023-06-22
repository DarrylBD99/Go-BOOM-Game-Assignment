package Game;
import java.util.*;
import java.awt.*;
import java.io.*;

import Game.Table.*;

import javax.swing.*;

public class GUI extends JFrame implements GameBase{
    public Deck deck = new Deck();
    public Center center = new Center();
    public Map<Integer, Player> players;

    public int turn;
    public int trick;
    public int turn_game;
    public int round = 1;
    Font game_font;

    JButton draw_button;

    public GUI() {

        get_font();
        create_draw_button();
        
        this.setTitle("OOPDS Assignment: Go Boom Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(1024, 768);
        this.setResizable(false);
        this.setVisible(true);
        this.add(draw_button);

        // setup
        for (int i = 1; i <= player_count; i++) players.put(i, new Player());
        startup();
    }
    
    // Buttons

    private void create_draw_button() {
        draw_button = new JButton("Draw");
        draw_button.setBounds(50, 100, 80, 40);
        draw_button.setFocusable(false);
        draw_button.setHorizontalTextPosition(JButton.CENTER);
        draw_button.setVerticalTextPosition(JButton.BOTTOM);

        draw_button.setFont(game_font);
        draw_button.addActionListener(e -> draw_cards());
    }

    private void get_font()
    {
        try {
            game_font = Font.createFont(Font.TRUETYPE_FONT, new File("Pixeloid.ttf")).deriveFont(16f);
            GraphicsEnvironment gEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            gEnvironment.registerFont(game_font);

        } catch (IOException | FontFormatException e) {
            System.out.println("Error: " + e);
        }
    }

    public void startup() {
        trick = 1;
        deck.reset();
        center.reset();
        center.add_card(deck.remove_card());
        for (int i = 1; i <= player_count; i++) players.get(i).set_cards(deck.distributeCards(player_count, card_count));
        turn = center.get_first_player(); ////
        turn_game = 0;
    }

    // Implemented from GameBase
    public void draw_cards() {
        Player currentPlayer = players.get(turn); ////
        String drawnCard;

        do {
            drawnCard = deck.remove_card();
            currentPlayer.add_card(drawnCard);
            System.out.println("Player " + turn + " draws a card: " + drawnCard);
            draw_card_anim(turn, drawnCard);
        } while (!center.check_same_suit_or_rank(drawnCard));

        // autoplay after drawing a card
        play_card(drawnCard);
    }
    
    public void play_card(String card) {
        center.add_card(card);
        players.get(turn).remove_card(card); ////
        System.out.println("Player " + turn + " plays " + card); ////
        play_card_anim(turn, card);

        turn = (turn % player_count) + 1; ////
        turn_game++;
    }
    
    public void process_card(String card) {
        Player player = players.get(turn); ////
        Set<String> hand = player.get_cards();
        
        if (hand.contains(card) && center.check_playable_card(card)) play_card(card);
        else System.out.println("Error: Invalid card");
    }
    
    public void process_command(String command) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'process_command'");
    }
    
    public int get_trick_winner() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get_trick_winner'");
    }
    
    public boolean check_score() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'check_score'");
    }
    
    public int get_game_winner() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get_round_winner'");
    }
    
    public void increase_score() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'increase_score'");
    }

	public boolean check_round_end() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'check_round_end'");
	}

	public boolean check_hand(Player player) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'check_hand'");
	}
    
    public void end_game() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'end_game'");
    }
    
    public void restart() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'restart'");
    }


    // GUI Animated
    void draw_card_anim(int index, String card)
    {
        
    }
    
    void play_card_anim(int index, String card)
    {
        
    }
}