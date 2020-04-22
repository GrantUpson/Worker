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

    private int currentRows;

    public Database(String username, String password)
    {
        this.username = username;
        this.password = password;
        this.currentRows = 0;
    }

    public boolean insertTweet(Tweet tweet)
    {
        final int MAX_ROWS = 50;
        currentRows++;

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

        return  currentRows != MAX_ROWS;
    }

    public String findMessageByID(String ID)
    {
        String message = "";

        try(Connection connection = DriverManager.getConnection(url, this.username, this.password))
        {
            int tweetID = Integer.parseInt(ID);
            String query ="Select message from tweet where uid = ?";
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
            System.out.println("Error: " + sqlException.getMessage());
        }

        return message;
    }

    public String findNumberOfTweetsContainingWord(String word)
    {
        int amount = 0;
        String message = "";

        try(Connection connection = DriverManager.getConnection(url, this.username, this.password))
        {
           String query = "Select * from tweet where message like ?";
           PreparedStatement statement = connection.prepareStatement(query);
           statement.setString(1, "%" + word + "%");
           ResultSet result = statement.executeQuery();

           while(result.next())
           {
               amount++;
           }

           message = amount > 0 ? String.valueOf(amount) : "0";
        }
        catch( SQLException sqlException )
        {
            System.out.println("Error: " + sqlException.getMessage());
        }

        return message;
    }

    public String findNumberOfTweetsFromAirline(String name)
    {
        int amount = 0;
        String message = "";

        try(Connection connection = DriverManager.getConnection(url, this.username, this.password))
        {
            String query = "Select * from tweet where airline like ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name + "%");
            ResultSet result = statement.executeQuery();

            while(result.next())
            {
                amount++;
            }

            message = amount > 0 ? String.valueOf(amount) : "0";
        }
        catch( SQLException sqlException )
        {
            System.out.println("Error: " + sqlException.getMessage());
        }

        return message;
    }
}
