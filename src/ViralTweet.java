import twitter4j.*;
import twitter4j.api.DirectMessagesResources;
import twitter4j.api.TrendsResources;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.TwitterFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ViralTweet {

    public static void main(String args[]) throws Exception{
        // The factory instance is re-useable and thread safe.
        Twitter twitter = TwitterFactory.getSingleton();
        authenticate(twitter);

        System.out.println();

        // The factory instance is re-useable and thread safe.
        Query query = new Query("betabertrand");
        QueryResult result = twitter.search(query);
        for (Status status : result.getTweets()) {
            System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
        }

//        // The factory instance is re-useable and thread safe.
//        List<Status> statuses = twitter.getHomeTimeline();
//        System.out.println("Showing home timeline.");
//        for (Status status : statuses) {
//            System.out.println(status.getUser().getName() + ":" +
//                    status.getText());
//        }










    }
    private static void storeAccessToken(int useId, AccessToken accessToken){
        //store accessToken.getToken()
        //store accessToken.getTokenSecret()
    }

    public static String[] getTopTrends(Twitter twitter) throws TwitterException{
        Trends popTrend;
        ArrayList<String> trendList = new ArrayList<>();
        popTrend = twitter.trends().getPlaceTrends(	2459115 /* NYC */);
        for (Trend t : popTrend.getTrends()) {
            trendList.add(t.getName());
        }

        return (String[]) trendList.toArray();
    }

    public static void authenticate(Twitter twitter) throws TwitterException, IOException {
        twitter.setOAuthConsumer("jGtXpSBZtcPsXQwUbcihEJaJ9", "qcEfFTwIXBOHJNKHwspmsRNfIOpSddR6od0lHGkBUcdmv37iqF");
        RequestToken requestToken = twitter.getOAuthRequestToken();
        AccessToken accessToken = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (null == accessToken) {
            System.out.println("Open the following URL and grant access to your account:");
            System.out.println(requestToken.getAuthorizationURL());
            System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
            String pin = br.readLine();
            try{
                if(pin.length() > 0){
                    accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                }else{
                    accessToken = twitter.getOAuthAccessToken();
                }
            } catch (TwitterException te) {
                if(401 == te.getStatusCode()){
                    System.out.println("Unable to get the access token.");
                }else{
                    te.printStackTrace();
                }
            }
        }
    }
}
