package model.creature;

import java.util.Stack;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import model.creature.movements.MovementInterface;
import model.creature.vision.Vision;
import model.gameloop.ActorInterface;
import model.maze.MazeModelInterface;

public class Creature implements ActorInterface {
	private double countdown = 1;
	private DoubleProperty x, y;
	private DoubleProperty velocity;
	private IntegerProperty lifes;
	private MovementInterface movementStrategy;
	private final CreatureType type;
	private MazeModelInterface maze;
	private Vision vision;
	private VisitedMap map;
	private Stack<double[]> lastMovements;

	public Creature(double x, double y, double velocity, int lifes, MovementInterface movementStrategy, Vision vision, CreatureType type,
			MazeModelInterface maze) {
		this(new SimpleDoubleProperty(x), new SimpleDoubleProperty(y), new SimpleDoubleProperty(velocity), new SimpleIntegerProperty(lifes), movementStrategy,
				vision, type, maze);
	}

	public Creature(DoubleProperty x, DoubleProperty y, DoubleProperty velocity, IntegerProperty lifes, MovementInterface movementStrategy, Vision vision,
			CreatureType type, MazeModelInterface maze) {
		this.x = x;
		this.y = y;
		this.velocity = velocity;
		this.lifes = lifes;
		this.movementStrategy = movementStrategy;
		this.type = type;
		this.vision = vision;
		this.maze = maze;
		this.map = new VisitedMap(maze.getMaxWallX(), maze.getMaxWallY());
		markCurrentFieldVisited();
		lastMovements = new Stack<>();
	}

	public void move() {
		move(1);
	}
	public void move(double dt) {
		double[] dir = movementStrategy.getMoveDirection(maze, vision, map, getX(), getY());
		move(dir, dt);
	}
	
	public void move(double dirX, double dirY) {
		move(new double[]{dirX, dirY});
	}
	public void move(double[] dir) {
		move(dir, 1);
	}
	private void move(double[] dir, double dt) {
		boolean backtracking = false;
		if (dir == null) {
			if(lastMovements.isEmpty()) {
				dir = new double[] {1,0};
			} else { //backtracking
				double[] last = lastMovements.pop();
				dir = new double[] {last[0]*-1, last[1]*-1};				
				markCurrentFieldUseless();
				backtracking = true;
			}
		}
		setX(getX() + getVelocity() * dir[0]*dt);
		setY(getY() + getVelocity() * dir[1]*dt);
		markCurrentFieldVisited();
		if (!backtracking) {
			lastMovements.push(dir);
		}
	}

	private void markCurrentFieldVisited() {
		map.markVisited((int) getX(),(int) getY());
	}
	private void markCurrentFieldUseless() {
		map.markUseless((int) getX(),(int) getY());
	}

	/**
	 * @return the x value
	 */
	public double getX() {
		return x.get();
	}

	/**
	 * @param x
	 *            the x to set
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
	 * @param y
	 *            the y to set
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
	 * @param velocity
	 *            the velocity to set
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
	 * @param lifes
	 *            the lifes to set
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
	 * @param movementStrategy
	 *            the movementStrategy to set
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

	public VisitedMap getVisitedMap() {
		return map;
	}

	@Override
	public void act(double dt) {
		if (countdown < dt) {
			move();
			countdown = 0.5;
		} else {
			countdown -= dt;
		}
	}
}
