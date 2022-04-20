package Game;

public enum ID { // This is an enumerator class containing identifications for all the types of objects. These identifications are needed as there can be multiple identifications for each object. 
				 // For example there are three different identifications that use the player class, one for each of the classes the player can be in the game.
				 // An enumerator class is used to define a collection of constants 
	Warrior(),
	Rogue(),
	Mage(),
	Enemy(),
	BossEnemy(),
	Wall(),
	DoorUp(),
	DoorDown(),
	DoorLeft(),
	DoorRight(),
	DoorClosed(),
	TrapDoor(),
	MagicBolt(),
	BossAttack(),
	Heart();
}