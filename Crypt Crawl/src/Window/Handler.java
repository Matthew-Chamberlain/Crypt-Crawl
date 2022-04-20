package Window;
import java.awt.Graphics;
import java.util.LinkedList;

import Game.GameObject;
import Game.ID;
import Game.CurrentData;

public class Handler {

	public LinkedList<GameObject> object = new LinkedList<GameObject>(); // initiating linked list of game objects // example of a linked list
	
	// this method is called once every tick of the game loop. This method is responsible for looping through all of the objects currently in the game and calling their tick methods
	public void tick() {
		//for loop that loops through all the game objects in the list
		for(int i = 0; i < object.size(); i++) {
			GameObject entity = object.get(i);  // getting an item from the list and stores it in a game object variable entity
			entity.tick(); // calls the tick method of the entity
			if(!entity.isActive()) { // checks whether the entities active variable is false
				removeObject(entity); // calls the removeObject list, parsing the entity being checked
			}
			
		}
	}
	//this method is called once every tick of the game loop. The purpose of this method is to loop through all of the objects in the list of game objects and call their render methods
	public void render(Graphics g) {
		for(int i = 0; i < object.size(); i++) { // looping through all the objects in the list of game objects
			object.get(i).render(g); // calling the render method for each object in the list
		}
	}
	// this method is responsible for clearing all the graphics currently being displayed on the screen. this is done by clearing all objects from the list, the objects are then redrawn
	public void clearGraphics() {
		object.clear(); // making the list of game objects empty // example of linked list maintains 
	}
	
	//this method is responsible for adding the parsed object to the list of game objects
	public void addObject(GameObject object) {
		this.object.add(object); //adding an object to the list // example of linked list maintenance 
	}
	
	// this method is used for removing a parsed object from the list
	public void removeObject(GameObject object) {
		this.object.remove(object); // removing an object from the list // example of linked list maintenance
	}
	
}