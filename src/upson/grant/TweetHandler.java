package upson.grant;

public class TweetHandler implements Runnable
{
    private String hostname;
    private int port;

    public TweetHandler(String hostname, int port)
    {
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public void run()
    {
        while(true)
        {
            System.out.println("Tweet Handler!");
        }
    }
}
