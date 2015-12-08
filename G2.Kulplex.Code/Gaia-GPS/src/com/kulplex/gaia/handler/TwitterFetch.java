/**
 * TwitterFetch
 * 
 * This class handles the fetching of twitter posts
 * 
 * Please note that API it is used for fetching info
 * 
 * @author Alberto Vaccari
 * 
 */

package com.kulplex.gaia.handler;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import java.util.List;

public class TwitterFetch {
	public List<Status> twitter_fetch(String user) {
        // gets Twitter instance with default credentials
        Twitter twitter = new TwitterFactory().getInstance();
        List<Status> statuses = null;
        try {
            statuses = twitter.getUserTimeline(user);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        }
        return statuses;
    }
}
