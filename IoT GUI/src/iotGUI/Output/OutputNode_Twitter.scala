package iotGUI.Output

import java.lang.System
import java.util.Date

import twitter4j.TwitterFactory
import twitter4j.Twitter
import twitter4j.conf.ConfigurationBuilder

object OutputNode_Twitter {
	def SendTweet(str: String): Int =
	{
		// Config and create a twitter object
	    val cb = new ConfigurationBuilder()
	    cb.setDebugEnabled(true)
	      .setOAuthConsumerKey("M0SZajzyZthAgps4sCqeqldrg")
	      .setOAuthConsumerSecret("pmSRVYCFV1PG4SqPjOP6z7PeLtXx2caudbVQxOGq1urQwWudrz")
	      .setOAuthAccessToken("2927147052-ssnXo2OdBcIznVOyN0ym7cAyCMVvqR38wCeTN3U")
	      .setOAuthAccessTokenSecret("BS0lBbWZM3G2KlaoLbd0E1kYT6Tn5iJaFjJIWJR87kTk9")
	    val tf = new TwitterFactory(cb.build())
	    val twitter = tf.getInstance()
	
	    // Use the twitter object to post a tweet
	    val now = new Date
	    val result = twitter.updateStatus(str + " " + now);
	    
	    return 0
	}
}
