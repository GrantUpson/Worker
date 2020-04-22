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
                Database queryDatabase = new Database("USER", "PASS");
                ObjectOutputStream sendQuery = new ObjectOutputStream(serverConnection.getOutputStream());
                ObjectInputStream newQuery = new ObjectInputStream(serverConnection.getInputStream());
                Query query = (Query)newQuery.readObject();

               switch(query.getType())
               {
                   case MESSAGE:
                       String message = queryDatabase.findMessageByID(query.getRequest());
                       sendQuery.writeObject(message);
                       break;

                   case CONTAINS_WORD:
                       String containsWord = queryDatabase.findNumberOfTweets(query.getRequest());
                       sendQuery.writeObject(containsWord);
                       break;

                   case FROM_AIRLINE:
                       String fromAirline = queryDatabase.numberofTweetsFromAirline(query.getRequest());
                       sendQuery.writeObject(fromAirline);
                       break;

                   case MOST_FREQUENT_CHARACTER:

                       int stringSum = 0;
                       int asciiValue;
                       int stringLength;
                       int mostFrequentCharacterValue;

                       char mostFrequentCharacter=' ';
                       char[] charArray;

                       String messageCharacter = "";
                       String messageFromTweetID = queryDatabase.mostFrequentCharacter(query.getRequest());
                       String[] stringArray = messageFromTweetID.split("\\W+");
                       String lowerCaseString = "";
                       String wholeString = new String();

                       for (int i = 0; i < stringArray.length; i++)
                       {
                           wholeString = wholeString + stringArray[i];

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

                       sendQuery.writeObject(messageCharacter);

                       break;
               }




            }
            catch(IOException ioe)
            {
                System.out.println("Error: Cannot connect with the client" + ioe);
            }
            catch(ClassNotFoundException cnf)
            {
                System.out.println("Error: Cannot connect with the client" + cnf);
            }

        }
    }
}
