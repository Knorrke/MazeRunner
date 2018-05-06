package model.creature;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import model.creature.movements.MovementInterface;

public class Creature {

	private DoubleProperty x,y;
	private DoubleProperty velocity;
	private IntegerProperty lifes;
	private MovementInterface movementStrategy;
	private final CreatureType type;
	
	public Creature(double x, double y, double velocity, 
			int lifes, MovementInterface movementStrategy, CreatureType type) {
		this(
			new SimpleDoubleProperty(x), 
			new SimpleDoubleProperty(y), 
			new SimpleDoubleProperty(velocity),
			new SimpleIntegerProperty(lifes),
			movementStrategy,
			type
		);
	}
	
	public Creature(DoubleProperty x, DoubleProperty y, DoubleProperty velocity, 
			IntegerProperty lifes, MovementInterface movementStrategy, CreatureType type) {
		this.x = x;
		this.y = y;
		this.velocity = velocity;
		this.lifes = lifes;
		this.movementStrategy = movementStrategy;
		this.type = type;
	}

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

	/**
	 * @return the type
	 */
	public CreatureType getType() {
		return type;
	}
	
}
