package upson.grant;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/*
@author Grant Upson : 385831
@author Adib Shadman : 468684
 */

public class TweetHandler implements Runnable
{
    private String hostname;
    private int port;

    public TweetHandler(String hostname, int port)
    {
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public void run()
    {
        try(Socket serverConnection = new Socket(hostname, port))
        {
            Database tweetDatabase = new Database("grant", "Killthemall2");
            ObjectInputStream newTweet = new ObjectInputStream(serverConnection.getInputStream());

            while (true)
            {
                try
                {
                    Tweet tweet = (Tweet)newTweet.readObject();
                    tweetDatabase.insertTweet(tweet);
                }
                catch(ClassNotFoundException cnfException)
                {
                    System.out.println("Error: " + cnfException.getMessage());
                }
            }
        }
        catch(IOException ioException)
        {
            System.out.println("Error: " + ioException.getMessage());
        }
    }
}
