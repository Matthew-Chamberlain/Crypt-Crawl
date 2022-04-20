package Game;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Window.MapArrays;

public class AStar { // this class meets objective 7 // this class has examples of subroutines with common purposes grouped // this class is an example of complex algorithm
	
	private static boolean[] blocked; // an boolean array that will hold all the tiles that cannot be walked on,
	
	private static int width; // stores how many tiles fit the width of the current room
	
	private static int height; // stores how many tiles fit the height of the current room

	// this method is used to initialise the grid for the first room that is loaded, it is parsed a 2D array world which is the room being loaded
	public static void initialise(int[][] world) {
		
		width = 25; // sets the width to 25
		height = 20; // sets the height to 20
		blocked = new boolean[width * height]; // sets the blocked array to contain the same number of elements as there are tiles on the map
		
		for (int x = 0; x < 25 * 20; x++) { // loops through all the positions on the map
			// checks the tiles against the corresponding value on the 2D array. If the value is a 1 or an 8 then then the tiles position in the blocked array is set to false, otherwise the tile is considered blocked
			try {
				int curr = world[x/width][x%width]; // turns a 2D array into a 1D array
				if(curr == 1||curr==8) {
					blocked[x] = false;
					
				} else {
					blocked[x] = true;
					
				}
				
				// as i am using arrays a null pointer exception has been used here in case the location along the array cannot be found, if the position cannot be found then the tile is set to unblocked
			} catch (NullPointerException e) { // this is an example of good exception handling 
				blocked[x] = false;
			}
		}
		
	}
	// this method is used to update the grid when the player switches between rooms, so a new room is being checked. It is parsed a 2D array called world, which is the room the player has switched to
	public static void updateGrid(int[][] world) {
		for (int x = 0; x < 25 * 20; x++) {
			try {
				// checks the tiles against the corresponding value on the 2D array. If the value is a 1 or an 8 then then the tiles position in the blocked array is set to false, otherwise the tile is considered blocked
				int curr = world[x/width][x%width]; // turns the 2D array into a 1D array
				if(curr==1||curr==8) {
					blocked[x] = false;
				} else {
					blocked[x] = true;
				}
			// as i am using arrays a null pointer exception has been used here in case the location along the array cannot be found, if the position cannot be found then the tile is set to unblocked
			} catch (NullPointerException e) { // this is an example of good exception handling
				blocked[x] = false;
			}
		}
	}
	// this method is used to create a list of points between a starting point and an end point. The starting point is where the point where the enemy is and the end point is the point where the player is
	public static List<Point> findPath(Point s, Point e) {
		List<Point> closedSet = new ArrayList<Point>(); // creates a new array list of points that will hold the tiles that are blocked or should not be followed to obtain the shortest path
		List<Point> openSet = new ArrayList<Point>(); // the open list is an array list of points that will hold data about points that are being considered for the shortest path
		HashMap<Point, Point> cameFrom = new HashMap<Point, Point>(); //this hash map uses the current cell as the key and the cell that it came from as the object
		HashMap<Point, Integer> currentDistance = new HashMap<Point, Integer>(); // this hash map stores the current cell as the key and the distance from the cell to the enemy as the object
		HashMap<Point, Integer> predictedDistance = new HashMap<Point, Integer>(); // this hash map stores the current cell as the key and the distance from the player as the object
		openSet.add(s); // adds the starting point to the open list
		currentDistance.put(s, 0); //adds the starting node to the current distance hash map and sets the distance to 0
		predictedDistance.put(s, getPredictedDistance(s, e)); // adds the starting node to the predicted distance hash map and sets the distance equal to the hypotenous between the starting node and the end node
		while (!openSet.isEmpty()) { // runs the indented code while the open set list is not empty
			Point current = null; // sets the current point to null
			for (int i = 0; i < openSet.size(); i++) { // loops through all elements in the open set
				Point temp = openSet.get(i); // creates a temporary point at the current element 
				if (i == 0) { 
					current = temp; // the purpose of this if function is to ensure that the current point is not equal to null to avoid any null pointer errors
					continue;
				}
				if (getPredictedDistance(current, e) > getPredictedDistance(temp, e)) { //test to see whether the node currently being checked is closer to the end point than the saved node
					current = temp;
				}
			}
			if (current.x == e.x && current.y == e.y) { // if the current x and y are equal to the players x and y then the destination has been reached and the path can be reconstructed by calling the recontsruct method
				return reconstructPath(cameFrom, e);
			}
			openSet.remove(current); // remove the current point from the open set
			closedSet.add(current); // add the current point to the closed set
			for (Point neighbor : getNeightborNodes(current)) { // loops through all the neighbour nodes
				int tempCurrentDistance = currentDistance.get(current) + 1; // sets a variable equal to the current node + 1 in order to simulate moving forwards one
				if (closedSet.contains(neighbor) && tempCurrentDistance >= currentDistance.get(neighbor)) { // skip this if the rest of the code for one iteration if the neighbour has already been checked and the temporary distance is further from the enemy than the current distance
					continue;
				}
				if (!closedSet.contains(neighbor) || tempCurrentDistance < currentDistance.get(neighbor)) { // if the neighbour has not been checked or if the temporary distance from the enemy is less than the current distance from the enemy to the neighbour
					if (cameFrom.containsKey(neighbor)) { // if the came from hash map contains the neighbour then replace the object with the key being neighbour as the current node
						cameFrom.replace(neighbor, current);
					} else { // otherwise add the neighbour to the hash map
						cameFrom.put(neighbor, current);
					}
					currentDistance.put(neighbor, tempCurrentDistance); // adds the neighbour to the current distance hash map
					predictedDistance.put(neighbor, currentDistance.get(neighbor) + getPredictedDistance(neighbor, e)); 
					if (!openSet.contains(neighbor)) { // if the open does contain the neighbour, add the neighbour
						openSet.add(neighbor);
					}
				}
			}
		}
		return null;
	}

	// this method is used to return an integer the predicted distance between the starting node and the end node, the starting node is where the enemy is and the end node is the node where the player is
	private static int getPredictedDistance(Point s, Point e) {
		return Math.abs(s.x - e.x) + Math.abs(s.y - e.y); // gets the distance between the enemy and the player 
	}

	// this method is used to reconstruct the path between the point it came from and the end point
	private static List<Point> reconstructPath(HashMap<Point, Point> cameFrom, Point current) {
		if (!cameFrom.containsKey(current)) { // if the hash map does not contain the end point 
			List<Point> path = new ArrayList<Point>(); // create a new array list that will hold all the nodes the enemy will follow to get to the player
			path.add(current); // adds end node to the path
			return path; // returns the array list
		}
		List<Point> path = reconstructPath(cameFrom, cameFrom.get(current)); // recursive call used to build the path from the end point to the enenmy
		path.add(current); //adds the current point to the array list
		return path;
	}

	// this method is used to get all of the neighbour nodes of the cell the enemy is currently on
	private static Iterable<Point> getNeightborNodes(Point node) {
		List<Point> nodes = new ArrayList<Point>(); // creates an array list of nodes
		try {
			if (node.y - 1 >= 0) { //if the y value of the node above the current node is larger than 0 then run code
				if (!blocked[node.x + ((node.y - 1) * width)]) { //if the node above is not blocked then run code
					nodes.add(new Point(node.x, node.y - 1)); //add node to nodes list
				} else {
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		try {
			if (node.x + 1 < width) { //if the x value of the node above the current node is less than the width then run code
				if (!blocked[(node.x + 1) + (node.y * width)]) { //if the node above is not blocked then run code
					nodes.add(new Point(node.x + 1, node.y)); //add node to nodes list
				} else {
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		try {
			if (node.y + 1 < height) { //if the y value of the node above the current node is less than the height then run code
				if (!blocked[node.x + ((node.y + 1) * width)]) { //if the node above is not blocked then run code
					nodes.add(new Point(node.x, node.y + 1)); //add node to nodes list
				} else {
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		try {
			if (node.x - 1 >= 0) { //if the x value of the node above the current node is greater than 0 then run code
				if (!blocked[(node.x - 1) + (node.y * width)]) { //if the node above is not blocked then run code
					nodes.add(new Point(node.x - 1, node.y)); //add node to nodes list
				} else {
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		return nodes; // return the list of neighbour nodes
	}
}
