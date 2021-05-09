package csc2a.px.model.game;

import javafx.animation.AnimationTimer;

public abstract class GameLoop extends AnimationTimer {

	boolean isPaused = false, pauseScheduled = false, playScheduled = false;

	long lastFrameTimeNanos = 0;
	
	@Override
	public void handle(long now) {
	    if (playScheduled) {
	        isPaused = false;
	        playScheduled = false;
	    }
	    if (pauseScheduled) {
	        isPaused = true;
	        pauseScheduled = false;
	    }
		if (!isPaused) {
			if ((now - lastFrameTimeNanos) / 1e9 > 5) {
				lastFrameTimeNanos = now;
			}
	        float secondsSinceLastFrame = (float) ((now - lastFrameTimeNanos) / 1e9);
	        lastFrameTimeNanos = now;
	        tick(secondsSinceLastFrame);
	    }
		
	}

	public void pause() {
	    if (!isPaused) {
	        pauseScheduled = true;
	    }
	}
	
	public void play() {
	    if (isPaused) {
	        playScheduled = true;
	    }
	}

	public abstract void tick(float deltaTime);
}
