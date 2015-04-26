import java.io.FileInputStream;
import java.util.Properties;

/**
 * Created by Cameron on 26/03/2015.
 *
 * Program entry point
 */
public class Main {



    public static void main(String[] args){
        //put config file loading here
        FileInputStream in = null;
        try {
            Properties props = new Properties();
            in = new FileInputStream("settings.cfg");
            props.load(in);
            in.close();
            IrcBot bot = new IrcBot(props.getProperty("NETWORK"),props.getProperty("CHAN"),props.getProperty("NICK","CAHBot"));
            //need to pass the bot object into the game object so that the game logic can send messages easily
            Game g = new Game(bot, props.getProperty("CHAN"));
            //similar concept - lets the irc part talk to the game part easier
            bot.setGame(g);
            bot.connect("bau5.ausdjforums.com");
            bot.joinChannel("#ausdjforums");
        }catch(Exception e){
            e.printStackTrace();
            if(in!=null)try{in.close();}catch (Exception e2){}
        }

    }

}
