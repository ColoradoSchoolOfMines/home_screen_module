package edu.mines.acmX.exhibit.modules.home_screen.view.inputmethod;

public abstract class VirtualClick {
	
	protected int duration;
	protected int startMillis = 0;
	
	public VirtualClick(int duration) {
		this.duration = duration;
	}
	
	public abstract void update(int mouseX, int mouseY, int millis);
	public abstract boolean durationCompleted(int millis);
	
}
