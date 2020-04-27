package upson.grant;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Worker
{
    private final String hostname;
    private final int port;

    public Worker(String hostname, int port)
    {
        this.hostname = hostname;
        this.port = port;
    }

    public void connect()
    {
        try(Socket serverConnection = new Socket(hostname, port);
            ObjectOutputStream messageSender = new ObjectOutputStream(serverConnection.getOutputStream());
            ObjectInputStream messageReceiver = new ObjectInputStream(serverConnection.getInputStream()))
        {
            Database database = null;

            Message initializingMessage = (Message)messageReceiver.readObject();

            if(initializingMessage instanceof Capacity)
            {
                database = new Database("grant", "Killthemall2", ((Capacity)initializingMessage).getMaximumCapacity());
            }

            while(true)
            {
                Message message = (Message)messageReceiver.readObject();

                if(message instanceof Query) { executeQuery((Query)message, database); }
                if(message instanceof Tweet) { storeTweet((Tweet)message, database); }
                if(message instanceof Heartbeat) { retrieveHeartbeat((Heartbeat)message); }

                messageSender.writeObject(message);
                messageSender.flush();
            }
        }
        catch(IOException | ClassNotFoundException exception)
        {
            System.out.println("Connection has been closed: " + exception.getMessage());
        }
    }

    public void executeQuery(Query query, Database database)
    {
        switch(query.getType())
        {
            case MESSAGE:
                query.setResult(database.findMessageByID(query.getRequest()));
                break;
            case CONTAINS_WORD:
                query.setResult(database.findNumberOfTweetsContainingWord(query.getRequest()));
                break;
            case FROM_AIRLINE:
                query.setResult(database.findNumberOfTweetsFromAirline(query.getRequest()));
                break;
            case MOST_FREQUENT_CHARACTER:
                String message = database.findMessageByID(query.getRequest());
                query.setResult(mostFrequentCharacter(message));
                break;
        }
    }

    public void storeTweet(Tweet tweet, Database database)
    {
        database.insertTweet(tweet);
        tweet.setResult("successful");
        //TODO
    }

    public void retrieveHeartbeat(Heartbeat heartbeat)
    {
        heartbeat.setResult("Heartbeat Good [Testing]");
        //TODO
    }

    public String mostFrequentCharacter(String message)
    {

        return "Meow";
    }

    public static void main(String[] args)
    {
        if(args.length != 2)
        {
            System.out.println("Error: Invalid number of arguments");
        }
        else
        {
            new Worker(args[0], Integer.parseInt(args[1])).connect();
        }
    }
}
