import java.util.ArrayList;

/**
 * Created by Cameron on 26/03/2015.
 *
 * Important note: Don't hate this class, hate the one with the logic. *rimshot*
 *
 */
public class Player {
    private String nick;
    //it's important to pass around containers like this, because it makes life much simpler in the long term
    private Deck d;
    private int wins = 0;
    private ArrayList<Card> hand = new ArrayList<Card>();

    Player(Deck d, String nick) {
        this.d = d;
        this.nick = nick;
        for(int i = 0;i<10;i++){
            hand.add(d.getWhiteCard());
        }
    }
    //returns a numbered string representation of the cards in the hand
    String getHand(){
        String ret = "";
        int i = 0;
        for(Card c: hand){
            ret+=" "+i+". "+c.getText();
            i++;

        }
        return ret;
    }
    //Hi, my name is
    String getNick(){
        return nick;
    }
    //sends card, gets new card from the deck
    Card playCard(int index){
        Card ret = hand.get(index);
        hand.remove(index);
        hand.add(d.getWhiteCard());
        return ret;
    }
    void addWin(){
        wins++;
    }
    int getWins(){
        return wins;
    }
}
