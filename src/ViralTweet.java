import twitter4j.*;
import twitter4j.api.DirectMessagesResources;
import twitter4j.api.TrendsResources;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.TwitterFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class ViralTweet {

    public static void main(String args[]) throws Exception{
        // The factory instance is re-useable and thread safe.
        Twitter twitter = TwitterFactory.getSingleton();
        authenticate(twitter);

        System.out.println();
        System.out.println();
        System.out.println();

        System.out.print(generateViralTweet(wordGram(convertStringArrayToText(getTrendSearchResults(twitter, getTopTrends(twitter))))));


        //Status status = twitter.updateStatus("hello world");


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
        String[] tList = new String[trendList.size()];
        //tList = trendList.toArray(tList);
        return trendList.toArray(tList);
    }

    public static String[] getTrendSearchResults(Twitter twitter, String[] trends) throws TwitterException {
        ArrayList<String> searchResultList = new ArrayList();
        for (String t : trends) {
            Query query = new Query(t);
            QueryResult result = twitter.search(query);
            for (Status status : result.getTweets()) {
                //System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
                searchResultList.add(status.getText().replace("RT", ""));
            }
        }
        String[] sRList = new String[searchResultList.size()];
        //sRList = searchResultList.toArray(sRList);
        return searchResultList.toArray(sRList);
    }

    public static String convertStringArrayToText(String[] arr) {
        StringBuilder builder = new StringBuilder();
        for(String s : arr) {
            builder.append(s);
        }
        return builder.toString();
    }

    public static void tweetViralTweet(Twitter twitter) throws TwitterException{

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

    public static Map<String, ArrayList<String>> wordGram(String text) {

        Scanner scanner = new Scanner(text);
        Map<String, ArrayList<String>> wordMap = new HashMap<>();
        String currentWord = scanner.next(); //.replaceAll("[^a-zA-Z ]", "").toLowerCase();;
        String nextWord;
        do {
            nextWord = scanner.next(); //.replaceAll("[^a-zA-Z ]", "").toLowerCase();
            if (!wordMap.containsKey(currentWord)) {
                ArrayList<String> wordList = new ArrayList<>();
                wordList.add(nextWord);
                wordMap.put(currentWord, wordList);
            } else {
                ArrayList<String> wordList = wordMap.get(currentWord);
                wordList.add(nextWord);
                wordMap.put(currentWord, wordList);
            }
            currentWord = nextWord; //.replaceAll("[^a-zA-Z ]", "").toLowerCase();; //Ue this to remove punctiation

        } while (scanner.hasNext());
        {
            currentWord = nextWord;
            if (!wordMap.containsKey(currentWord)) {
                ArrayList<String> wordList = new ArrayList<>();
                wordMap.put(currentWord, wordList);
            } else {
                ArrayList<String> wordList = wordMap.get(currentWord);
                wordMap.put(currentWord, wordList);
            }

        }
        return wordMap;
    }

    public static String generateViralTweet(Map<String, ArrayList<String>> map) {

        Random random = new Random();
        List<String> keys = new ArrayList<>(map.keySet());
        String randomKey = keys.get(random.nextInt(keys.size()));
        String currWord = randomKey;
        String textResult = "";
        int tweetLimit = 140;

        do {
            textResult = textResult + " " + currWord;
            currWord = map.get(currWord).get(random.nextInt(map.get(currWord).size()));
            tweetLimit -= currWord.length();


        } while (tweetLimit > 0);
        {
            textResult = textResult + " " + currWord;

        }

        return textResult;
    }
}
