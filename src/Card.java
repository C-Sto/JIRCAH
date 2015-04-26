/**
 * Created by Cameron on 26/03/2015.
 */
public class Card {
    //both black and white cards can just be cards
    String text;

    Card(String text){
        this.text = text;
    }

    String getText(){
        return text;
    }

   public String toString(){
        return getText();
    }
}
