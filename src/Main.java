/**
 * Created by Cameron on 26/03/2015.
 *
 * Program entry point
 */
public class Main {



    public static void main(String[] args){
        //put config file loading here
        IrcBot bot = new IrcBot();
        //need to pass the bot object into the game object so that the game logic can send messages easily
        Game g = new Game(bot);
        //similar concept - lets the irc part talk to the game part easier
        bot.setGame(g);
        try {
            bot.connect("bau5.ausdjforums.com");
            bot.joinChannel("#ausdjforums");
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
