package edu.mines.acmX.exhibit.modules.home_screen;

import edu.mines.acmX.exhibit.input_services.events.EventManager;
import edu.mines.acmX.exhibit.input_services.events.EventType;
import edu.mines.acmX.exhibit.stdlib.graphics.Coordinate3D;
import edu.mines.acmX.exhibit.stdlib.graphics.HandPosition;
import edu.mines.acmX.exhibit.stdlib.input_processing.receivers.HandReceiver;

import java.awt.*;

public class MyHandReceiver extends HandReceiver {

	private Coordinate3D position;
	int handID = -1;
	
	public MyHandReceiver() {
		position = new Coordinate3D(0, 0, 0);
	}

    @Override
	public void handCreated(HandPosition pos) {
		if (handID == -1) {
			handID = pos.getId();
		}
		position = pos.getPosition();
	}

    @Override
	public void handUpdated(HandPosition pos) {
		if (handID == -1)  {
			handID = pos.getId();
		}
		if (pos.getId() == handID) {
			position = pos.getPosition();
		}
	}	

    @Override
	public void handDestroyed(int id) {
		if (id == handID) {
			handID = -1;
		}
	}
	
	public int whichHand() {
		return handID;
	}
	
	public float getX() {
		return position.getX();
	}
	
	public float getY() {
		return position.getY();
	}
	
	public float getZ() {
		return position.getZ();
	}
	
	public void killHand() {
		handID = -1;
	}
}
