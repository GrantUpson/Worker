package upson.grant;

import java.sql.*;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class Database
{
    private final String URL = "jdbc:mysql://localhost:3306/tweet_worker?serverTimezone=Australia/Melbourne";
    private final String USERNAME;
    private final String PASSWORD;

    public Database(String username, String password)
    {
        this.USERNAME = username;
        this.PASSWORD = password;
    }

    public void insertTweet(Tweet tweet)
    {
        try(Connection connection = DriverManager.getConnection(URL, this.USERNAME, this.PASSWORD))
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

    public String findMessageByID(String ID)
    {
        String message = "Invalid tweet ID";

        try(Connection connection = DriverManager.getConnection(URL, this.USERNAME, this.PASSWORD))
        {
            long tweetID = Long.parseLong(ID);
            String query ="Select message from tweet where uid = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, tweetID);

            ResultSet result = statement.executeQuery();

            while(result.next())
            {
                message = result.getString(1);
            }
        }
        catch(SQLException sqlException)
        {
            System.out.println("Error: " + sqlException.getMessage());
        }
        catch(NumberFormatException numberFormatException)
        {

        }

        return message;
    }

    public String findNumberOfTweetsContainingWord(String word)
    {
        int amount = 0;
        String message = "0";

        try(Connection connection = DriverManager.getConnection(URL, this.USERNAME, this.PASSWORD))
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

        try(Connection connection = DriverManager.getConnection(URL, this.USERNAME, this.PASSWORD))
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
