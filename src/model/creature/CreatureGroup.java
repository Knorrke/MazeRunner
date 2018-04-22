package model.creature;

public class CreatureGroup {
	private final CreatureType type;
	private final int number;
	
	public CreatureGroup(CreatureType type, int number) {
		this.type = type;
		this.number = number;
	}

	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}
	
	/**
	 * @return the type
	 */
	public CreatureType getType() {
		return type;
	}
	
}
