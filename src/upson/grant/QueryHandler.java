package upson.grant;

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
            System.out.println("Query Handler!");
        }
    }
}
