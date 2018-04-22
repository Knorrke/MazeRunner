package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import model.tower.NoTower;
import model.tower.Tower;

public class Wall {

	private IntegerProperty x, y;
	private ObjectProperty<Tower> tower;
	
	public Wall(int x, int y) {
		this(new SimpleIntegerProperty(x),new SimpleIntegerProperty(y));
	}
	
	public Wall(IntegerProperty x, IntegerProperty y) {
		this.x = x;
		this.y = y;
		this.tower = new SimpleObjectProperty<Tower>(new NoTower());
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
	 * @return the tower
	 */
	public Tower getTower() {
		return tower.get();
	}

	/**
	 * @param tower the tower to set
	 */
	public void setTower(Tower tower) {
		this.tower.set(tower);
	}

	public ObjectProperty<Tower> towerProperty() {
		return tower;
	}
}
