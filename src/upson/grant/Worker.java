package upson.grant;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

public class Worker
{
    public void connect(String hostname, int tweetPort, int queryPort)
    {
        new Thread(new TweetHandler(hostname, tweetPort)).start();
        new Thread(new QueryHandler(hostname, queryPort)).start();
    }

    public static void main(String[] args)
    {
        if(args.length != 3)
        {
            System.out.println("Error: Invalid number of arguments");
        }
        else
        {
            new Worker().connect(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        }
    }
}
