package upson.grant;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

import jdk.jshell.Snippet;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static java.time.chrono.JapaneseEra.values;

public class QueryHandler implements Runnable
{
    private String hostname;
    private int port;

    public QueryHandler(String hostname, int port)
    {
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public void run()
    {
        while(true)
        {
            try(Socket serverConnection = new Socket(hostname, port))
            {
                Database queryDatabase = new Database("root", "");
                ObjectInputStream newQuery = new ObjectInputStream(serverConnection.getInputStream());
                Query query = (Query)newQuery.readObject();

               switch(query.getType())
               {
                   case MESSAGE:
                       query.setResult(queryDatabase.findMessageByID(query.getRequest()));
                       break;
                   case CONTAINS_WORD:
                       query.setResult(queryDatabase.findNumberOfTweetsContainingWord(query.getRequest()));
                       break;
                   case FROM_AIRLINE:
                       query.setResult(queryDatabase.findNumberOfTweetsFromAirline(query.getRequest()));
                       break;
                   case MOST_FREQUENT_CHARACTER:
                       String message = queryDatabase.findMessageByID(query.getRequest());
                       query.setResult(mostFrequentCharacter(message, queryDatabase));
                       break;
               }

               ObjectOutputStream sendQuery = new ObjectOutputStream(serverConnection.getOutputStream());
               sendQuery.writeObject(query);
               sendQuery.flush();
               sendQuery.reset();
            }
            catch(IOException ioe)
            {
                System.out.println("Error: " + ioe);
            }
            catch(ClassNotFoundException cnf)
            {
                System.out.println("Error: " + cnf);
            }
        }
    }

    public String mostFrequentCharacter(String message, Database queryDatabase)
    {
        int stringSum = 0;
        int asciiValue;
        int stringLength;
        int mostFrequentCharacterValue;

        char mostFrequentCharacter=' ';
        char[] charArray;

        String messageCharacter = "";
        String[] stringArray = message.split("\\W+");
        String lowerCaseString = "";
        String wholeString = new String();

        for (int i = 0; i < stringArray.length; i++)
        {
            wholeString += stringArray[i];
        }

        lowerCaseString = wholeString.toLowerCase();
        stringLength = lowerCaseString.length();
        charArray = lowerCaseString.toCharArray();

        for( int i = 0; i < charArray.length; i++)
        {
            asciiValue = charArray[i];
            stringSum = asciiValue + stringSum;
        }

        mostFrequentCharacterValue =(int)Math.ceil(stringSum/stringLength);
        mostFrequentCharacter = (char)mostFrequentCharacterValue;
        messageCharacter = Character.toString(mostFrequentCharacter);

        return messageCharacter;
    }
}
