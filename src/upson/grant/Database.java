package upson.grant;

import java.sql.*;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class Database
{
    private final String url = "jdbc:mysql://localhost:3306/tweet_worker?serverTimezone=Australia/Melbourne";

    private final String username;
    private final String password;

    public Database(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public void insertTweet(Tweet tweet)
    {
        try(Connection connection = DriverManager.getConnection(url, this.username, this.password))
        {
            String query = "Insert into tweet(uid, sentiment, airline, message, date_created) values (?, ?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, tweet.getUID());
            statement.setString(2, tweet.getSentiment());
            statement.setString(3, tweet.getAirline());
            statement.setString(4, tweet.getMessage());
            statement.setTimestamp(5, tweet.getDateCreated());

            statement.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            System.out.println("Error " + sqlException.getMessage());
        }
    }

    public String findMessageByID(String stringTweetID)
    {
      String message = "";
        try(Connection connection = DriverManager.getConnection(url, this.username, this.password))
        {
            int tweetID = Integer.parseInt(stringTweetID);
            String query ="Select sentiment from tweet where uid = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, tweetID);

            ResultSet result = statement.executeQuery();
            while(result.next())
            {
                message = result.getString(1);
            }

        }
        catch( SQLException sqlException )
        {
            System.out.println("Error" + sqlException.getMessage());
        }
        return message;
    }

    public String findNumberOfTweets(String tweet)
    {
        int  numberOfRows = 0;
        String message = " ";
        try(Connection connection = DriverManager.getConnection(url, this.username, this.password))
        {
           String query = "Select * from tweet where message like ?";
           PreparedStatement statement = connection.prepareStatement(query);
           statement.setString(1, "%" + tweet + "%");

           ResultSet result = statement.executeQuery();
           while(result.next())
           {
               numberOfRows++;
           }
            if (numberOfRows > 0 )
            {
                message = String.valueOf(numberOfRows);
            }
            else
            {
                message = "No tweet has been found which contains the word" + tweet;
            }

           message = String.valueOf(numberOfRows);

        }
        catch( SQLException sqlException )
        {
            System.out.println("Error" + sqlException.getMessage());
        }
        return message;

    }

    public String numberofTweetsFromAirline( String airlineName )
    {
        int numberOfRows = 0;
        String message = "";
        try(Connection connection = DriverManager.getConnection(url, this.username, this.password))
        {
            String query = "Select * from tweet where airline like ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + airlineName + "%");

            ResultSet result = statement.executeQuery();
            while(result.next())
            {
                numberOfRows++;
            }
            if (numberOfRows > 0 )
            {
                message = String.valueOf(numberOfRows);
            }
            else
            {
                message = "No tweet has been found where the airline name is " + airlineName;
            }

        }
        catch( SQLException sqlException )
        {
            System.out.println("Error" + sqlException.getMessage());
        }
        return message;

    }

    public String mostFrequentCharacter( String stringTweetID )
    {
        String message = "";
        String messageWithoutSpace = "";
        int tweetID = Integer.parseInt(stringTweetID);

        try(Connection connection = DriverManager.getConnection(url, this.username, this.password))
        {
            String query = "Select message from tweet where uid = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, tweetID);
            ResultSet result = statement.executeQuery();

            while(result.next())
            {
                message = result.getString(1);
            }


        }
        catch( SQLException sqlException)
        {
            System.out.println("Error" + sqlException.getMessage());
        }
        return message;
    }

}
