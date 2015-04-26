import org.jibble.pircbot.PircBot;

/**
 * Created by Cameron on 26/03/2015.
 */
public class IrcBot extends PircBot {

    Game g;
    String network;
    String chan;

    IrcBot(String network, String chan, String nick){
        setName(nick);
        this.network = network;
        this.chan = chan;
        setLogin("CAHBot");
    }

    void setGame(Game g){
        this.g = g;
    }
    //this is the function that is called EVERY time a message is received/sent.
    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        //short circuit so no processing is done without good reason
        if(!message.startsWith("!"))return;
        //never know when we might need SoMeSoRtOfStRiNg
        String upperMsg = message;
        //make it lower so it doesn't break the switch
        message = message.toLowerCase();
        //not sure why I created another variable here, I'm sure there is a good reason for it
        String trigger = message;
        //because spaces break things
        if(trigger.contains(" ")) trigger = trigger.split(" ")[0];
        //send game commands to the game appropriately
        switch(trigger){
            default:
                break;
            case "!cah":
                g.newGame(sender);
                break;
            case "!join":
                g.addPlayer(sender);
                break;
            case "!start":
                g.startGame();
                break;
            case "!read":
                g.readCards(sender);
                break;
            case "!stop":
                g.stopGame();
                break;
            case "!play":
                try {
                    g.playCard(sender, Integer.parseInt(message.split(" ")[1]));
                    break;
                }catch(Exception e) {
                    sendNotice(sender, "Error playing card, probably not a number.");
                    break;
                }
            case "!pick":
                try {
                    g.pickWinner(sender, Integer.parseInt(message.split(" ")[1]));
                    break;
                }catch(Exception e){
                    sendNotice(sender, "Error picking card, probably not a number.");
                    break;
                }
            case "!leave":
                g.remPlayer(sender);
                break;
        }

    }
}
