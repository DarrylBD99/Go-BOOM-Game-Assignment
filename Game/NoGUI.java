package Game;

import java.util.*;
import java.io.*;

import Game.Table.*;

public class NoGUI implements GameBase {
    public Deck deck = new Deck();
    public Center center = new Center();
    public Map<Integer, Player> players = new HashMap<Integer, Player>();
    public int turn;
    public int trick;
    public int round = 1;

    private int turn_game;
    private List<Integer> skipped_player_keys = new ArrayList<Integer>();
    private String save_dir = "Save/";

    Scanner input = new Scanner(System.in);

    public void startup() {
        trick = 1;
        deck.reset();
        center.reset();
        center.add_card(deck.remove_card());
        for (int i = 1; i <= player_count; i++) players.get(i).set_cards(deck.distributeCards(player_count, card_count));
        turn = center.get_first_player(); ////
        turn_game = 0;
    }

    public void printHelp() {
        System.out.println("How to play: \n" +
                "- Each player is given 7 cards\n" +
                "- The game is seperated by tricks\n" +
                "- A card is placed in the center in the first trick\n" +
                "- The player that plays first will depend on the card in the center\n" +
                "- During the players turn, he must place a card with the same suit or rank as the first card in the center\n"
                +
                "- The person who played the card with the highest rank wins the trick\n" +
                "- During the next trick, the winner of the previous trick gets to play the first card\n\n" +

                "Score: \n" +
                "- When a player finishes their hand, the round ends.\n" +
                "- The score gets totaled up depending on the ranks of the cards in every player's hand\n\n" +
                "A       <- 1 point\n" +
                "2-10    <- 2-10 point\n" +
                "J, Q, K <- 10 point\n\n" +
                " - The game ends once one of the player's score reaches to 100\n" +
                " - The player with the lowest score wins\n\n" +

                "Commands: \n" +
                "S -> Restarts the game along with all the player's score\n" +
                "X -> Forcefully exiting the game\n" +
                "D -> Draws card from the deck until a card that can be played is drawn\n" +
                "save -> Save the current game progress\n" +
                "load -> Load the previos saved game\n");
    }

    public void printGame() {
        System.out.println("Round " + round);
        System.out.println("Trick No. " + trick);

        players.forEach((Player_ID, Player) -> System.out.println("Player " + Player_ID + ": " + Player.get_cards())); ////
        System.out.print("Center: ");
        center.printCards();
        System.out.print("\nDeck: ");
        deck.printCards();

        System.out.print("Score: ");
        players.forEach((Player_ID, Player) -> {
            System.out.print("Player " + Player_ID + " = " + Player.get_score());
            if (Player_ID < player_count)
                System.out.print(" | ");
            else
                System.out.print("\n");
        }); ////

        System.out.println("\nInput help for instructions\n");
        System.out.println("Turn: Player " + turn); //
    }

    public void end_game() {
        System.out.print("Are you sure you want to exit the game? (y/n): ");
        String choice = input.nextLine().toLowerCase();
        if (choice.equals("y"))
        {
            System.out.print("Do you want to save the game? (y/n): ");
            String choice2 = input.nextLine().toLowerCase();
            if (choice2.equals("y")) saveGame();
            System.out.println("Game closed");
            System.exit(0);
        }
    }

    public void restart() {
        System.out.print("Are you sure you want to start a new game? (y/n): ");
        String choice = input.nextLine().toLowerCase();
        if (choice.equals("y"))
        {
            System.out.println("New Game");
            round = 1;

            for (int i = 1; i <= player_count; i++) players.get(i).set_score(0);

            startup();
        }
        
    }

    public boolean check_hand(Player player) {
        Set<String> hand = player.get_cards();
        Iterator<String> hand_Iterator = hand.iterator();

        while (hand_Iterator.hasNext()) {
            String card = hand_Iterator.next();
            if (center.check_playable_card(card))
                return true;
        }
        return false;
    }

    public void draw_cards() {
        Player currentPlayer = players.get(turn);
        String drawnCard;

        while (!deck.get_cards().isEmpty()) {
            drawnCard = deck.remove_card();
            currentPlayer.add_card(drawnCard);
            System.out.println("Player " + turn + " draws a card: " + drawnCard);

            if (center.check_same_suit_or_rank(drawnCard)) {
                // Autoplay after drawing a card
                play_card(drawnCard);
                return;
            }
        }

        System.out.println("No more cards in the deck!");
    }

    public void play_card(String card) {
        center.add_card(card);
        players.get(turn).remove_card(card); ////
        System.out.println("Player " + turn + " plays " + card); ////

        turn = (turn % player_count) + 1; ////
        turn_game++;
    }

    public void process_card(String card) {
        Player player = players.get(turn); ////
        Set<String> hand = player.get_cards();

        if (hand.contains(card) && center.check_playable_card(card))
            play_card(card);
        else
            System.out.println("Error: Invalid card");
    }

    public void saveGame() {
        // check whether previous game progress is exists or not
        if(new File(save_dir + "gameInfo.txt").exists()) {
            System.out.print("There already exists another save file\nWould you like the overwrite it? (y/n): ");
            String choice = input.nextLine().toLowerCase();
            if (!choice.equals("y")) return;
            new File(save_dir + "gameInfo.txt").delete();
            new File(save_dir + "deck.txt").delete();
            new File(save_dir + "center.txt").delete();
            for(int i = 1; i <= player_count; i++) new File(save_dir + "player.txt" + i + ".txt").delete();
        }

        try {
            FileWriter writer = new FileWriter(save_dir + "gameInfo.txt");
            writer.write(turn + "|");
            writer.write(trick + "|");
            writer.write(round + "|");
            writer.write(turn_game + "|");
            for (int i = 0; i < skipped_player_keys.size(); i++) writer.write(skipped_player_keys.get(i) + "|");
            writer.close();
        } catch (IOException e) {
            System.out.println("File does not exists");
        }

        // save remaining cards in the deck
        try {
            FileWriter writer = new FileWriter(save_dir + "deck.txt");
            for(String card: deck.get_cards()) writer.write(card + "|");
            writer.close();
        } catch (IOException e) {
            System.out.println("File does not exists");
        }

        // save current cards in the center
        try {
            FileWriter writer = new FileWriter(save_dir + "center.txt");
            for(String card: center.get_cards()) writer.write(card + "|");
            writer.close();
        } catch (IOException e) {
            System.out.println("File does not exists");
        }

        // save players' score and hands
        for(int i = 1; i <= player_count; i++) {
            try {
                FileWriter writer = new FileWriter(save_dir + "player" + i + ".txt");
                Player player = players.get(i);
                writer.write(player.get_score() + "|");
                for(String card: player.get_cards()) writer.write(card + "|");
                writer.close();
            } catch (IOException e) {
                System.out.println("File does not exists");
            }
        }

        System.out.println("Game saved successfully.");
    }

    public void load_game() {
        File file;
        String[] gameInfo;

        // check whether previous game progress is exists or not
        file = new File(save_dir + "gameInfo.txt");
        if(!file.exists()) {
            System.out.println("No previous game progress");
            return;
        }

        // load basic game info
        file = new File(save_dir + "gameInfo.txt");
        try {
            Scanner file_scanner = new Scanner(file);
            gameInfo = file_scanner.nextLine().split("[|]");
            turn = Integer.parseInt(gameInfo[0]);
            trick = Integer.parseInt(gameInfo[1]);
            round = Integer.parseInt(gameInfo[2]);
            turn_game = Integer.parseInt(gameInfo[3]);
            
            skipped_player_keys.clear();
            for (int i = 0; i < (gameInfo.length - 4); i++) skipped_player_keys.add(Integer.parseInt(gameInfo[i + 4]));
            file_scanner.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File does not exists");
        }

        // load deck info
        file = new File(save_dir + "deck.txt");
        try {
            Scanner file_scanner = new Scanner(file);
            deck.cards.clear();
            if (file_scanner.hasNextLine())
            {
                gameInfo = file_scanner.nextLine().split("[|]");
                for(String card: gameInfo) deck.add_card(card);
                file_scanner.close();
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File does not exists");
        }

        // load center info
        file = new File(save_dir + "center.txt");
        try {
            Scanner file_scanner = new Scanner(file);
            center.cards.clear();
            if (file_scanner.hasNextLine())
            {
                gameInfo = file_scanner.nextLine().split("[|]");
                for(String card: gameInfo) center.add_card(card);
                file_scanner.close();
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File does not exists");
        }

        // load players' info
        for(int i = 1; i <= player_count; i++) {
            file = new File(save_dir + "player" + i + ".txt");
            try {
                Scanner file_scanner = new Scanner(file);
                gameInfo = file_scanner.nextLine().split("[|]");
                Player player = players.get(i);
                player.reset();
                player.set_score(Integer.parseInt(gameInfo[0]));
                for (int k = 1; k < gameInfo.length; k++) player.add_card(gameInfo[k]);
                file_scanner.close();
            } catch (FileNotFoundException e) {
                System.out.println("File does not exists");
            }
        }
    }

    public void process_command(String command) {
        if (command.toLowerCase().equals("s")) restart(); // start a new game
        else if (command.toLowerCase().equals("x")) end_game(); // exit game
        else if (command.toLowerCase().equals("d")) draw_cards(); // draw card
        else if (command.toLowerCase().equals("save")) saveGame(); // save game
        else if (command.toLowerCase().equals("load")) load_game(); // load previous game
        else if (command.toLowerCase().equals("help")) printHelp(); // help command
        else if (deck.isCard(command)) process_card(command); // check if the input is valid card
        else
            System.out.println("Error: Invalid command");
    }

    public int get_trick_winner() {
        int trickWinnerIndex = center.getTrickWinner(player_count);
        if (trickWinnerIndex >= 0) {
            int trickWinnerKey = (trickWinnerIndex + turn);

            if ((trickWinnerKey % player_count) == 0)
                trickWinnerKey = player_count;
            else
                trickWinnerKey = trickWinnerKey % player_count;
            for (int i = 0; i < skipped_player_keys.size(); i++) {
                int skipped_player_index = Math.abs((skipped_player_keys.get(i) - turn) % 4);
                if (skipped_player_index <= trickWinnerIndex) {
                    trickWinnerKey = (trickWinnerKey % player_count) + 1;
                }
            }

            System.out.println("Player " + trickWinnerKey + " wins the trick!");
            turn = trickWinnerKey; // Set the turn to the winning player
            trick++; // Increase the value of trick by 1
            return turn;
        }
        System.out.println("Error: No winner for the trick!");
        return 0;
    }

    public boolean check_score() {
        for (int i = 1; i <= player_count; i++) {
            if (players.get(i).get_score() >= 100)
                return true; ////
        }
        return false;
    }

    public int get_game_winner() {
        int lowest_score = 100;
        int winner = 0;
        for (int i = 1; i <= player_count; i++) {
            int player_score = players.get(i).get_score(); ////
            if (player_score < lowest_score)
                lowest_score = player_score;
            winner = i;
        }

        return winner;
    }

    public void print_end() {
        int winner = get_game_winner();
        System.out.println("Player " + (winner + 1) + " has won the game!\n");
        System.out.println("Score:");
        players.forEach((Player_ID, Player) -> {
            System.out.print("Player " + Player_ID + " <- " + Player.get_score());
            if (Player_ID == winner)
                System.out.print(" W");
            System.out.print("\n");
        }); ////
    }

    public boolean check_round_end() {
        for (int i = 1; i <= player_count; i++)
        {
            if (players.get(i).get_cards().isEmpty()) {
                System.out.println("BOOM! Player " + i + " has no more cards to play."); ////
                System.out.println("Round " + round + " has ended.");
                return true;
            }
        }
        return false;
    }

     public void increase_score()
    {
        for (int i = 1; i <= player_count; i++)
        {
            // Update the scores or perform any other necessary actions for the winner
            players.get(i).increase_core(); ////
        }
    }


    public NoGUI() {
        // setup
        for (int i = 1; i <= player_count; i++) players.put(i, new Player());
        startup();

        while (true) {
            // game
            String cmd = "";
            skipped_player_keys.clear();
            turn_game = 0;
            while (turn_game < players.size()) {
                if (deck.get_cards().isEmpty() && !check_hand(players.get(turn))) ////
                {
                    System.out.println("Deck is empty and Player " + turn + " has no playable cards\n" +
                            "Player " + turn + "'s turn is skipped\n");
                    skipped_player_keys.add(turn);
                    turn = (turn % player_count) + 1;
                    turn_game++;
                    continue;
                }
                printGame();
                System.out.print("Command: ");

                cmd = input.nextLine();
                System.out.print("\n");
                process_command(cmd);
                System.out.print("\n");
                if (check_round_end()) ////
                {
                    increase_score();
                    startup();
                    round++;
                }
            }

            // check if a player reaches 100
            if (check_score()) {
                print_end();
                break;
            }

            // check who won trick
            int winner = get_trick_winner();
            // reset center

            center.reset();
            if (winner <= 0)
                center.add_card(deck.remove_card());
        }
        input.close();
        end_game();
    }
}
