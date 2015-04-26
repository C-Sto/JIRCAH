/**
 * Created by Cameron on 28/03/2015.
 */
public class PlayedCard{
    String player;
    Card card;

    PlayedCard(String player, Card card){
        this.player = player;
        this.card = card;
    }

    Card getCard(){
        return card;
    }

    String getPlayer(){
        return player;
    }

}
