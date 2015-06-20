import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import twitter4j.ResponseList;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.Trend;
import twitter4j.Twitter;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Trends;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;


public class TwitterTopStory {

	final static int max = 100000;
	 static int count = 0;
   public static void main(String[] args) throws TwitterException {
   	//just fill this
   	 ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
          .setOAuthConsumerKey("JuYU1A5dqNOsMQRdWYuPxLHKh")
          .setOAuthConsumerSecret("0hFLoWuSmjPMa2Wo9exgVLUbSnxHsiZFM1jATSuJeWk3G0g8wm")
          .setOAuthAccessToken("2873502034-wOC7orTDf0SiChzc5oLEhuaP0ZuF5FkRQio8dtr")
          .setOAuthAccessTokenSecret("0nMSvMtsB7JiV1taroRQI4URdO1vSQvJwDWKQdZz1euXK");
        try{
       final TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

       Twitter twitter = new TwitterFactory().getInstance();
       //       Trend[] trends = ((Trend) twitter).getCurrentTrends();


       /*
   	final Connection conn = DBConnection.createConnection();
       StatusListener listener = new StatusListener() {
           @Override
           public void onStatus(Status status) {
               System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
           	if(count<max)
           	{ 
					if(status.getId()!=0 && status.getGeoLocation()!=null&&status.getLang().equalsIgnoreCase("en"))
					{
	            		System.out.println("Tweet" + status.getUser().getScreenName() + " - " + status.getText());
	            		PreparedStatement preparedStatement = null;
	            		try {
							preparedStatement = conn.prepareStatement("insert into twitter values(?,?,?,?,?)");
							preparedStatement.setString(1, Long.toString(status.getId()));
							preparedStatement.setString(2,status.getUser().getScreenName() );
							preparedStatement.setDouble(4, status.getGeoLocation().getLatitude());
							preparedStatement.setDouble(5, status.getGeoLocation().getLongitude());
							preparedStatement.setString(3, status.getText());
							preparedStatement.executeUpdate();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            		count++;
					}
           	}
					else{
	            		twitterStream.removeListener(this);
	    				twitterStream.shutdown();	
						
	            	}
           	
           
           
           }

           @Override
           public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
            //   System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
           }

           @Override
           public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
              // System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
           }

           @Override
           public void onScrubGeo(long userId, long upToStatusId) {
               //System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
           }

           @Override
           public void onStallWarning(StallWarning warning) {
               //System.out.println("Got stall warning:" + warning);
           }

           @Override
           public void onException(Exception ex) {
               ex.printStackTrace();
           }
       };
       twitterStream.addListener(listener);
       twitterStream.sample();
       */
    }
    catch(Exception e)
    {
   	System.out.println(e); 
    }
       
   }

}
