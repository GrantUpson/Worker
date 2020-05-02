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
    private final int RECONNECTION_ATTEMPTS = 3;

    private final String hostname;
    private final int port;
    private int reconnectionAttempts;
    private int maxCapacity;
    private int currentCapacity;
    private boolean notified;

    public Worker(String hostname, int port)
    {
        this.hostname = hostname;
        this.port = port;
        reconnectionAttempts = 0;
        currentCapacity = 0;
    }

    public void connect()
    {
        try(Socket serverConnection = new Socket(hostname, port);
            ObjectOutputStream messageSender = new ObjectOutputStream(serverConnection.getOutputStream());
            ObjectInputStream messageReceiver = new ObjectInputStream(serverConnection.getInputStream()))
        {
            Database database = null;
            reconnectionAttempts = 0;

            Message initializingMessage = (Message)messageReceiver.readObject();

            if(initializingMessage instanceof Capacity)
            {
                database = new Database("root", "");
                maxCapacity = ((Capacity) initializingMessage).getMaximumCapacity();
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

           // if(reconnectionAttempts < 5)
            System.out.println("Attempting to reconnect, attempt: " + reconnectionAttempts);
           // reconnectionAttempts++;
           // connect();
        }
    }

    public void executeQuery(Query query, Database database)
    {
        long startTime = System.currentTimeMillis();

        switch(query.getType())
        {
            case MESSAGE:
                query.setResult(database.findMessageByID(query.getRequest()));
                query.setBill(2.50);
                break;
            case CONTAINS_WORD:
                query.setResult(database.findNumberOfTweetsContainingWord(query.getRequest()));
                query.setBill(3.50);
                break;
            case FROM_AIRLINE:
                query.setResult(database.findNumberOfTweetsFromAirline(query.getRequest()));
                query.setBill(1.50);
                break;
            case MOST_FREQUENT_CHARACTER:
                String message = database.findMessageByID(query.getRequest());

                if(!message.equalsIgnoreCase("Invalid tweet ID"))
                {
                    query.setResult(mostFrequentCharacter(message));
                }
                else
                {
                    query.setResult(message);
                }

                query.setBill(5.00);

                break;
        }

        query.setTimeTakenToCompute(System.currentTimeMillis() - startTime);
    }

    public void storeTweet(Tweet tweet, Database database)
    {
        if(!notified)
        {
            if(currentCapacity >= maxCapacity)
            {
                tweet.setResult("Full");
                notified = true;
            }
            else
            {
                currentCapacity++;
                database.insertTweet(tweet);
                tweet.setResult("Success");
                //System.out.println("Tweet stored.");
            }
        }
        else
        {
            tweet.setResult("Waiting on additional worker");
        }
    }

    public void retrieveHeartbeat(Heartbeat heartbeat)
    {
        heartbeat.setResult("Heartbeat Good [Testing] for worker of ID: " + heartbeat.getWorkerID());
        System.out.println("Set Heartbeat Result");
    }

    public String mostFrequentCharacter(String message)
    {
        int[] freq = new int[message.length()];
        char minChar = message.charAt(0), maxChar = message.charAt(0);
        int max;

        char string[] = message.toCharArray();

        for (int i = 0; i < string.length; i++)
        {
            freq[i] = 1;
            for (int j = i + 1; j < string.length; j++)
            {
                if (string[i] == string[j] && string[i] != ' ' && string[i] != '0')
                {
                    freq[i]++;
                    string[j] = '0';
                }
            }
        }

        max = freq[0];
        for (int i = 0; i < freq.length; i++)
        {
            if (max < freq[i])
            {
                max = freq[i];
                maxChar = string[i];
            }
        }

        return Character.toString(maxChar);
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
