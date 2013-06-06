package edu.mines.acmX.exhibit.modules.home_screen.view;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

//TODO get library to be included in jar
public class TwitterDisplay extends DisplayElement {

	private Twitter twitter; 
	private List<String> twitterMessages;
	private static final int CYCLE_TIME = 20;
	private String currentMessage;
	private int currentMessageIndex;
	
	public TwitterDisplay(PApplet parent, double weight) {
		super(parent, weight);
		twitter = new TwitterFactory().getInstance();
		twitterMessages = retrieveAllMessages();
		currentMessageIndex = 0;
		currentMessage = twitterMessages.get(currentMessageIndex);
		currentMessageIndex++;
	}

	@Override
	public void update(int x, int y) {
		originX = x;
		originY = y;
		if(PApplet.second() % CYCLE_TIME == 0) {
			currentMessage = twitterMessages.get(currentMessageIndex);
			currentMessageIndex++;
			if(currentMessageIndex == twitterMessages.size()) {
				currentMessageIndex = 0;
			}
		}
	}

	@Override
	public void draw() {
		parent.textSize(32);
		parent.fill(0, 0, 0);
		parent.textAlign(PApplet.CENTER, PApplet.CENTER);
		parent.text(currentMessage, originX + width / 2, originY + height / 2);
	}

	public String retrieveLatestMessage() {
		if (!retrieveAllMessages().isEmpty()) {
			return retrieveAllMessages().get(0);
		}
		return "";
	}
	
	public List<String> retrieveAllMessages() {
		List<String> messageList = new ArrayList<String>();
		try {
			if (!authorizeAccess()) {
				return messageList;
				//returns an empty list
			}
			ResponseList<Status> twitterStatuses = twitter.getMentionsTimeline();
			for (Status status : twitterStatuses) {
				messageList.add("@" + status.getUser().getScreenName() + " - " + status.getText());
			}
		} catch (TwitterException e) {
			System.out.println("oops");
		}
		return messageList;
	}
	
	private boolean authorizeAccess() throws TwitterException {
		try {
			RequestToken requestToken = twitter.getOAuthRequestToken();
			AccessToken accessToken = null;
			accessToken = getAccessToken(requestToken, accessToken);
		} catch (IllegalStateException e) {
			if (!twitter.getAuthorization().isEnabled()) {
				System.out.println("OAuth consumer key/secret not set");
				return false;
			}
			
		}
		return true;
	}
	
	private AccessToken getAccessToken(RequestToken requestToken, AccessToken accessToken) {
		while (accessToken == null) {
			try {
				accessToken = twitter.getOAuthAccessToken(requestToken);
			} catch (TwitterException e) {
				if (e.getStatusCode() == 401) {
					System.out.println("unable to get access token");
				} else {
					System.out.println("Problem, status code " + e);
				}
			}
		}
		return accessToken;
	}
}
