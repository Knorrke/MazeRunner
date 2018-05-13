package application.model.gameloop;

import application.model.GameModelInterface;
import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {
	GameModelInterface game;

	public GameLoop(GameModelInterface game) {
		this.game = game;
	}

	private static final float timeStep = 0.0166f;
	private static final float maxTimeStep = 0.05f;
	private long previousTime = 0;
	private float accumulatedTime = 0;

//	private float secondsElapsedSinceLastFpsUpdate = 0f;
//	private int framesSinceLastFpsUpdate = 0;

	@Override
	public void handle(long currentTime) {
		if (previousTime == 0) {
			previousTime = currentTime;
			return;
		}

		float secondsElapsed = (currentTime - previousTime) / 1e9f; /* nanoseconds to seconds */
		float secondsElapsedCapped = Math.min(secondsElapsed, maxTimeStep);
		accumulatedTime += secondsElapsedCapped;
		previousTime = currentTime;

		while (accumulatedTime >= timeStep) {
			game.update(timeStep);
			accumulatedTime -= timeStep;
		}
//		renderer.run();

//		secondsElapsedSinceLastFpsUpdate += secondsElapsed;
//		framesSinceLastFpsUpdate++;
//		if (secondsElapsedSinceLastFpsUpdate >= 0.5f) {
//			int fps = Math.round(framesSinceLastFpsUpdate / secondsElapsedSinceLastFpsUpdate);
//			fpsReporter.accept(fps);
//			secondsElapsedSinceLastFpsUpdate = 0;
//			framesSinceLastFpsUpdate = 0;
//		}
	}

	@Override
	public void stop() {
		previousTime = 0;
		accumulatedTime = 0;
//		secondsElapsedSinceLastFpsUpdate = 0f;
//		framesSinceLastFpsUpdate = 0;
		super.stop();
	}
}
