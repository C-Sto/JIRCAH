import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Cameron on 26/03/2015.
 * deck class, does deck stuff as expected
 */
public class Deck {

    ArrayList<Card> BLACK_CARDS = new ArrayList<Card>();
    ArrayList<Card> WHITE_CARDS = new ArrayList<Card>();

    //initialize the deck on creation
    Deck(){
        initBlackCards();
        initWhiteCards();
        shuffleDecks();
    }
    //initialize the deck on demand
    public void initialize(){
        initBlackCards();
        initWhiteCards();
        shuffleDecks();
    }
    //read the black cards from the file, and turn them into cards
    void initBlackCards(){
        try{
            String bigString = new String(Files.readAllBytes(Paths.get("blackCards.txt")));
            String[] stringList = bigString.split("<>");
            for (String s: stringList){
                BLACK_CARDS.add(new Card(s));
            }
        }catch(Exception e){
            e.printStackTrace();
            System.exit(0);
        }
    }
    //read the white cards from the file, turn them into cards
    void initWhiteCards() {
        try {
            String bigString = new String(Files.readAllBytes(Paths.get("whiteCards.txt")));
            String[] stringList = bigString.split("<>");
            for (String s : stringList) {
                WHITE_CARDS.add(new Card(s));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
    //get a white card from the deck and remove it
    Card getWhiteCard(){
        Card ret = WHITE_CARDS.get(0);
        WHITE_CARDS.remove(0);
        return ret;
    }
    //get a black card from the deck and remove it
    Card getBlackCard(){
        Card ret = BLACK_CARDS.get(0);
        BLACK_CARDS.remove(0);
        return ret;
    }
    //shuffle both black and white cards
    void shuffleDecks(){
        Collections.shuffle(BLACK_CARDS);
        Collections.shuffle(WHITE_CARDS);
    }
    //add the card back to the appropriate deck
    void returnWhiteCard(Card c){
        WHITE_CARDS.add(c);
    }
    void returnBlackCard(Card c){
        BLACK_CARDS.add(c);
    }
}
