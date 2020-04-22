package upson.grant;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/*
@author Grant Upson : 385831
@author Adib Shadman : 468684
 */

public class TweetHandler implements Runnable
{
    private String hostname;
    private int port;
    private boolean connected;

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
            Database tweetDatabase = new Database("root", "");
            ObjectInputStream newTweet = new ObjectInputStream(serverConnection.getInputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(serverConnection.getOutputStream()));
            connected = true;

            while (connected)
            {
                try
                {
                    Tweet tweet = (Tweet)newTweet.readObject();
                    boolean success = tweetDatabase.insertTweet(tweet);
                    writer.write(success + "\r\n");
                    writer.flush();

                    if(!success)
                    {
                        serverConnection.close();
                        System.out.println("Database full. Disconnecting tweet connection from: " + serverConnection.getInetAddress());
                        connected = false;
                    }
                }
                catch(ClassNotFoundException cnfException)
                {
                    System.out.println("Error: " + cnfException.getMessage());
                }
            }
        }
        catch(IOException ioException)
        {
            System.out.println(ioException.getMessage());
        }
    }
}
