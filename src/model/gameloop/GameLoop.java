package model.gameloop;

import static model.GameState.GAMEOVER;

import model.GameModelInterface;

public class GameLoop {
	GameModelInterface game;
	
	public GameLoop(GameModelInterface game) {
		this.game = game;
	}

	public void startGameLoop() {
		final double timeStep = 0.0166;
		final double maxDelta = 0.05;
		long previousTime = System.nanoTime();
		double accumulatedTime = 0;

		while (game.getState() != GAMEOVER)
		{
		    long currentTime = System.nanoTime();
		    double deltaTime = (currentTime - previousTime) / 1_000_000_000.0;

		    if (deltaTime > maxDelta) {
		        deltaTime = maxDelta;
		    }

		    accumulatedTime += deltaTime;

//		    processEvents();

		    while (accumulatedTime >= timeStep) {
		        game.update(timeStep);
		        accumulatedTime -= timeStep;
		    }

//		    renderFrame();

		    previousTime = currentTime;
		}
	}
}
