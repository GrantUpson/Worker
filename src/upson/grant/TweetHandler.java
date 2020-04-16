package upson.grant;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

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
            while (true)
            {
                String date = "2020-03-01";
                ObjectInputStream tweet_inStream = new ObjectInputStream(serverConnection.getInputStream());
                //Tweet tweet = (Tweet)tweet_inStream.readObject();
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                Date myDate = formatter.parse(date);
                java.sql.Date sqlDate = new java.sql.Date(myDate.getTime());
                Tweet tweet = new Tweet(1, "Hello", "World", "jet star", myDate);
                Database tweet_database = new Database("root", "");
                tweet_database.insertIntoDatabase(tweet);
                System.out.println("Tweet Handler!");
            }
        }
        catch(IOException | ParseException exception)
        {
            System.out.println("Error: " + exception.getMessage());
        }

    }
}
