package Game;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// this class holds information about things that are currently in use by the program such as things such as class the user is player as. They have been put in this class as they are needed by a number of other classes
public class CurrentData {

	public static String selectedClass; // stores the class the user is currently playing as
	public static int[][] currentFloor = new int[20][25]; // stores the room the user is currently in
	public static List<int[][]> previousRoom = new ArrayList<int[][]>(); // stores a list of all the rooms the user has been in. // this is an example  of a list
	public static int roomsX, roomsY, currentHealth, enemyCount, Level, playerLevel, currentEXP; // stores the position on the 3x3 the current room is. Stores the current health the user is on. Stores the floor the player is on. Stores the level the player is. Stores the current amount of EXP the player is on.
	public static float red, green, blue; // stores float values to be used for red, green and blue colours
	public static boolean alive = true, floorCleared = false; // stores whether the user is alive and whether the floor the user is on has been cleared
}