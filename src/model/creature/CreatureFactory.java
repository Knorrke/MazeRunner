package model.creature;

import java.util.ArrayList;
import java.util.List;

import model.creature.movements.MovementInterface;

public class CreatureFactory {

	public static List<Creature> createAll(CreatureGroup creatures) {
		ArrayList<Creature> list = new ArrayList<>();
		for (int i=0; i<creatures.getNumber(); i++) {
			list.add(create(creatures.getType()));
		}
		return list;
	}
	
	public static Creature create(CreatureType type, double x, double y) {
		double velocity=10;
		int lifes = 10;
		MovementInterface movementStrategy = () -> new double[]{x+1,y};
		return new Creature(x, y, velocity, lifes, movementStrategy, type);
	}

	public static Creature create(CreatureType type) {
		return create(type, 0, 0);
	}
}
