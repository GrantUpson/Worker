package upson.grant;
/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
 */
import java.sql.*;

public class Database
{
    private final String url = "jdbc:mysql://localhost:3306/tweets?serverTimezone=Australia/Melbourne";

    private final String username;
    private final String password;

    public Database(String username, String password)
    {
        this.username = username;
        this.password = password;
    }


    public void insertIntoDatabase(Tweet tweet)
    {
        try(Connection connection = DriverManager.getConnection(url, this.username, this.password))
        {
            String query = "Insert into tweets(tweet_id, sentiment, airline, message, date_created) values (?, ?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, tweet.getTweetID());
            statement.setString(2, tweet.getSentiment());
            statement.setString(3, tweet.getAirline());
            statement.setString(4, tweet.getMessage());
            statement.setString(5, tweet.getDateCreated());

            statement.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            System.out.println("Error " + sqlException.getMessage());
        }
    }


}
