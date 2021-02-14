package org.mazerunner.model.creature.movements;

import java.util.Arrays;
import org.mazerunner.model.creature.VisitedMap;
import org.mazerunner.model.creature.vision.Vision;
import org.mazerunner.util.Util;

public class RandomMovement implements MovementInterface {

  private static final double[] right = {1, 0};
  private static final double[] left = {-1, 0};
  private static final double[] up = {0, -1};
  private static final double[] down = {0, 1};
  private double[] lastDirection;
  private double pSameDirection;
  private double pTurn;
  private double pTurnAround;

  public RandomMovement() {
    this(0.6, 0.35);
  }

  public RandomMovement(double probabilitySameDirection, double probabilityTurn) {
    this.pSameDirection = probabilitySameDirection;
    this.pTurn = probabilityTurn;
    this.pTurnAround = 1 - probabilitySameDirection - probabilityTurn;
  }

  @Override
  public double[] getNextGoal(Vision vision, VisitedMap visited, double currentX, double currentY) {
    double[] probabilitiesRLUD = new double[] {0.97, 0.01, 0.01, 0.01}; // default to right.
    if (lastDirection == right) {
      probabilitiesRLUD = new double[] {pSameDirection, pTurnAround, pTurn / 2, pTurn / 2};
    } else if (lastDirection == left) {
      probabilitiesRLUD = new double[] {pTurnAround, pSameDirection, pTurn / 2, pTurn / 2};
    } else if (lastDirection == up) {
      probabilitiesRLUD = new double[] {pTurn / 2, pTurn / 2, pSameDirection, pTurnAround};
    } else if (lastDirection == down) {
      probabilitiesRLUD = new double[] {pTurn / 2, pTurn / 2, pTurnAround, pSameDirection};
    }
    probabilitiesRLUD =
        adaptProbabilitiesOnAccessibility(visited, currentX, currentY, probabilitiesRLUD);
    double[] direction =
        Util.chooseAtRandom(new double[][] {right, left, up, down}, probabilitiesRLUD);
    lastDirection = direction;
    double[] newPosition = getNewPosition(currentX, currentY, direction);
    return newPosition;
  }

  private double[] adaptProbabilitiesOnAccessibility(
      VisitedMap visited, double currentX, double currentY, double[] probabilitiesRLUD) {
    double[] adaptedProbabilities = Arrays.copyOf(probabilitiesRLUD, probabilitiesRLUD.length);
    double[][] directions = {right, left, up, down};
    for (int i = 0; i < directions.length; i++) {
      if (adaptedProbabilities[i] == 0) continue;
      if (!isVisitable(visited, getNewPosition(currentX, currentY, directions[i]))) {
        for (int j = 0; j < directions.length; j++) {
          if (j != i) {
            adaptedProbabilities[j] = adaptedProbabilities[j] / (1 - adaptedProbabilities[i]);
          }
        }
        adaptedProbabilities[i] = 0;
      }
    }
    return adaptedProbabilities;
  }

  private boolean isVisitable(VisitedMap visited, double[] pos) {
    double x = pos[0];
    double y = pos[1];
    return (x >= 0 && y >= 0 && !visited.isWall((int) x, (int) y));
  }

  private double[] getNewPosition(double currentX, double currentY, double[] direction) {
    return new double[] {currentX + direction[0], currentY + direction[1]};
  }
}
