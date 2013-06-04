package edu.mines.acmX.exhibit.modules.home_screen;

import edu.mines.acmX.exhibit.input_services.hardware.HandPosition;
import edu.mines.acmX.exhibit.input_services.hardware.drivers.openni.HandReceiver;
import edu.mines.acmX.exhibit.stdlib.graphics.Coordinate3D;

public class MyHandReceiver extends HandReceiver {

	private Coordinate3D position;
	int handID = -1;
	
	public MyHandReceiver() {
		position = new Coordinate3D(0, 0, 0);
	}
	
	public void handCreated(HandPosition pos) {
		if (handID == -1) {
			handID = pos.id;
		}
		position = pos.position;
	}
	
	public void handUpdated(HandPosition pos) {
		if (handID == -1)  {
			handID = pos.id;
		}
		if (pos.id == handID) {
			position = pos.position;
		}
	}	
	
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
}
