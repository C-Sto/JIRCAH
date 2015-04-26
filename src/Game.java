import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.jibble.pircbot.Colors;

/**
 * Created by Cameron on 26/03/2015.
 * Game logic, fun fun fun
 *
 * todo: add black card to winning players winning cards
 * todo: stats
 * todo: have a winner - first to 10? multiple game modes? sky is the limit
 * todo: handle the case when all the black cards r gone :(
 *
 */
public class Game {

    boolean playing = false;
    Deck deck = new Deck();
    String chan = "#CardsAgainstHumanity";
    Player judge;
    int judgeIndex;
    IrcBot bot;
    ArrayList<PlayedCard> playedWhiteCards = new ArrayList<PlayedCard>();
    ArrayList<String> playerStrings = new ArrayList<String>();
    HashMap<String, Player> players = new HashMap<String, Player>();
    Card blackCard;
    Game(IrcBot bot){
        this.bot = bot;
    }
    //create a new game - clears all containers and initializes the deck
    void newGame(String nick){
        if(playing){
            bot.sendMessage(chan, "Game already in progress, type !join to join in.");
            return;
        }
        players.clear();
        deck.initialize();
        bot.sendMessage(chan, Colors.BOLD + "Starting a new game of Cards Against Humanity, type !join to join in.");
        addPlayer(nick);
        playing = true;
    }
    //actually starts the game if enough players are playing
    void startGame(){
        //can't play with less than 3
        if(players.keySet().size()>2){
            bot.sendMessage(chan,"Starting game...");
            judge = players.get(players.keySet().toArray()[0]);
            judgeIndex = 0;
            startRound();
        }else {
            bot.sendMessage(chan, "Not enough players, invite more people to play!");
        }
    }
    //begins a round (read question, show players their cards)
    void startRound(){
        //judge should already be set, if it's not, that's unhandled behaviour and there are likely dragons
        bot.sendMessage(chan, Colors.BOLD+"Judge is "+Colors.BOLD+judge.getNick());
        //take a black card
        blackCard = deck.getBlackCard();
        //show the black card (read it out)
        bot.sendMessage(chan, Colors.BOLD+"Q: "+Colors.BOLD+blackCard.getText());
        //show everyone their cards - this should be a function!
        for(String p: players.keySet()){
            //don't show the judge, they don't need to see
            if(judge.getNick().equals(p))continue;
            bot.sendNotice(p,players.get(p).getHand());
        }
    }
    //reads out the played cards, this should probably be a one time only thing that ends the round..todo?
    void readCards(String nick){
        //only let the judge do this, or chaos
        if(judge.getNick().equals(nick)){
            //shuffle the cards before reading them out, or it would be easy to guess who played what
            Collections.shuffle(playedWhiteCards);
            String ret = "";
            //could probably consolidate the for, but for-each is cooler
            int i = 0;
            for(PlayedCard c: playedWhiteCards){
                ret+=Colors.BOLD+" "+i+". "+Colors.BOLD+c.getCard().getText();
                i++;
            }
            //read out the question again before showing the answers
            bot.sendMessage(chan, blackCard.getText());
            bot.sendMessage(chan, ret);
        }
    }
    //send the black card back to the deck - might want to give to player instead in the future
    void returnBlackCard(){
        deck.returnBlackCard(blackCard);
        blackCard = null;
    }
    //lets the judge pick a winner, then starts a new round
    void pickWinner(String nick, int choice){
        try {
            if (judge.getNick().equals(nick)) {
                String winnerString = playedWhiteCards.get(choice).getPlayer();
                bot.sendMessage(chan, Colors.BOLD+"Winner is: " +Colors.BOLD+
                        winnerString +
                        Colors.BOLD+" With: " +Colors.BOLD+
                        playedWhiteCards.get(choice).getCard().getText());
                //increment win amount - would be a good spot to 'add card' in the future possibly
                players.get(winnerString).addWin();
                //show wins
                bot.sendMessage(chan, winnerString+" has "+players.get(winnerString).getWins()+" wins.");
                //if give card to player happens, make sure to get rid of this method.
                returnBlackCard();
                nextJudge();
                //return the white cards to the deck
                for (int i = 0; i < playedWhiteCards.size(); i++) {
                    deck.returnWhiteCard(playedWhiteCards.get(0).getCard());
                    playedWhiteCards.remove(0);
                }
                //start a new round
                startRound();

            }
        }catch (Exception e){
            bot.sendMessage(chan, "There was an error picking the winner, try again.");
        }
    }
    private void nextJudge(){
        //rotate judge. There has to be a better way of doing this.
        //check index of current judge - in case someone left, or joined or something
        judgeIndex = playerStrings.indexOf(judge.getNick());
        //increment
        judgeIndex++;
        //if it wraps - go to beginning
        if (judgeIndex > playerStrings.size()-1) judgeIndex = 0;
        judge = players.get(playerStrings.get(judgeIndex));
    }
    //stops a currently running game
    void stopGame(){
        if(playing)bot.sendMessage(chan, "Stopping CAH");
        playing = false;
    }
    //adds a player to the game - should be able to do at any time
    void addPlayer(String nick){
        //chaos happens if the player is already playing and is added again
        if(players.containsKey(nick)){
            bot.sendMessage(chan, nick + "you are already playing!");
            return;
        }
        //add the player to the map and the strings list
        players.put(nick, new Player(deck, nick));
        playerStrings.add(nick);
        bot.sendMessage(chan,nick+" has joined the game.");
    }
    //remove the player from the game
    void remPlayer(String nick){
        if(players.containsKey(nick)){
            //change judge so the game doesn't break if the judge leaves
            if(judge.getNick().equals(nick)){
                nextJudge();
            }
            players.remove(nick);
            playerStrings.remove(nick);
            bot.sendMessage(chan, nick + " has been removed.");
            //handle the case when a player leaves and the game has too few players
            if(players.keySet().size()<3){
                bot.sendMessage(chan,"Not enough players.");
                stopGame();
            }
        }else{
            bot.sendMessage(chan, "You can't leave if you are not playing.");
        }
    }
    //player plays a card
    void playCard(String nick, int val){
        //first bad case is judge trying to play a card, always, every time
        if(judge.equals(nick)){
            bot.sendNotice(nick, "You can't play a card if you are the judge!");
            return;
        }
        //second bad case is player trying to play two cards, always, every time
        if(!hasPlayed(nick)){
            playedWhiteCards.add(new PlayedCard(nick, players.get(nick).playCard(val)));
            bot.sendMessage(chan, nick+ " has played their card.");
            if (playedWhiteCards.size() == playerStrings.size() - 1) {
                bot.sendMessage(chan, "All players have played a card, automatically reading them out.");
                readCards(judge.getNick());
            }
        }else{
            bot.sendNotice(nick, "You have already played a card, wait for next round.");
        }
    }
    //track if player has played a card or not, surprisingly important
    boolean hasPlayed(String nick){
        for(PlayedCard c: playedWhiteCards){
            if(c.getPlayer().equals(nick))return true;
        }
        return false;
    }
}
