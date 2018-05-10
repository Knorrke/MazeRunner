package model.maze;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import model.gameloop.ActorInterface;
import model.maze.tower.AbstractTower;
import model.maze.tower.NoTower;

public class Wall implements ActorInterface {

	private IntegerProperty x, y;
	private ObjectProperty<AbstractTower> abstractTower;
	
	public Wall(int x, int y) {
		this(new SimpleIntegerProperty(x),new SimpleIntegerProperty(y));
	}
	
	public Wall(IntegerProperty x, IntegerProperty y) {
		this.x = x;
		this.y = y;
		this.abstractTower = new SimpleObjectProperty<AbstractTower>(new NoTower());
	}

	/**
	 * @return the x position
	 */
	public int getX() {
		return x.get();
	}

	/**
	 * @return the x property
	 */
	public IntegerProperty xProperty() {
		return x;
	}
	

	/**
	 * @return the y position
	 */
	public int getY() {
		return y.get();
	}
	
	/**
	 * @return the y property
	 */
	public IntegerProperty yProperty() {
		return y;
	}
	

	/**
	 * @return the position as array
	 */
	public int[] getPosition() {
		return new int[]{x.get(),y.get()};
	}

	/**
	 * @return the abstractTower
	 */
	public AbstractTower getTower() {
		return abstractTower.get();
	}

	/**
	 * @param abstractTower the abstractTower to set
	 */
	public void setTower(AbstractTower abstractTower) {
		this.abstractTower.set(abstractTower);
	}

	public ObjectProperty<AbstractTower> towerProperty() {
		return abstractTower;
	}

	@Override
	public void act(double dt) {
		getTower().act(dt);
	}
}
