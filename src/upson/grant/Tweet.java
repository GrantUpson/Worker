package upson.grant;

import java.util.Date;
public class Tweet
{
    private int tweetID ;
    private String sentiment = " ";
    private String airline = " ";
    private String message = " ";
    private Date dateCreated ;

     public Tweet(int tweetID, String sentiment, String airline, String message, Date dateCreated)
     {
         this.tweetID = tweetID;
         this.sentiment = sentiment;
         this.airline = airline;
         this.message = message;
         this. dateCreated = dateCreated;
     }

     public int getTweetID()
     {
        return tweetID;
     }
     public String getSentiment()
     {
         return sentiment;
     }
     public String getAirline()
     {
         return airline;
     }
     public String getMessage()
     {
         return message;
     }
     public Date getDateCreated()
     {
         return dateCreated;
     }


}
