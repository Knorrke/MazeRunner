package model.creature;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import model.creature.movements.MovementInterface;

public class Creature {

	private DoubleProperty x,y;
	private DoubleProperty velocity;
//	private DoubleProperty visualRange;
	private IntegerProperty lifes;
	private MovementInterface movementStrategy;
	
	public void move() {
		double[] pos = movementStrategy.move();
		x.set(pos[0]);
		y.set(pos[1]);
	}

	/**
	 * @return the x value
	 */
	public double getX() {
		return x.get();
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x.set(x);
	}

	/**
	 * @return the x property
	 */
	public DoubleProperty xProperty() {
		return x;
	}
	
	/**
	 * @return the y value
	 */
	public double getY() {
		return y.get();
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y.set(y);
	}
	
	/**
	 * @return the y property
	 */
	public DoubleProperty yProperty() {
		return y;
	}

	/**
	 * @return the velocity value
	 */
	public double getVelocity() {
		return velocity.get();
	}

	/**
	 * @param velocity the velocity to set
	 */
	public void setVelocity(double velocity) {
		this.velocity.set(velocity);
	}

	/**
	 * @return the velocity property
	 */
	public DoubleProperty velocityProperty() {
		return velocity;
	}
//
//	/**
//	 * @return the visualRange value
//	 */
//	public double getVisualRange() {
//		return visualRange.get();
//	}
//
//	/**
//	 * @param visualRange the visualRange to set
//	 */
//	public void setVisualRange(double visualRange) {
//		this.visualRange.set(visualRange);
//	}
//
//	/**
//	 * @return the visualRange property
//	 */
//	public DoubleProperty visualRangeProperty() {
//		return visualRange;
//	}
	
	/**
	 * @return the lifes value
	 */
	public int getLifes() {
		return lifes.get();
	}

	/**
	 * @param lifes the lifes to set
	 */
	public void setLifes(int lifes) {
		this.lifes.set(lifes);
	}

	/**
	 * @return the lifes property
	 */
	public IntegerProperty lifesProperty() {
		return lifes;
	}
	
	/**
	 * @param movementStrategy the movementStrategy to set
	 */
	public void setMovementStrategy(MovementInterface movementStrategy) {
		this.movementStrategy = movementStrategy;
	}
	
}
