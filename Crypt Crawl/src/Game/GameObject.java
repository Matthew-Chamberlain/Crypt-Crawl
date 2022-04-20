package Game;
import java.awt.Graphics;
import java.awt.Rectangle;

import Game.CurrentData;

public abstract class GameObject { // the game object class is abstract as it contains abstract methods that are not implemented and need to be inherited by sub classes to be used. The game object class acts as a kind of interface for the game objects as it acts as a blue print class for the other game objects
//example of polymorhphism
	//these variables are protected which means they can only be used by classes that inherit them // they are examples of meaningful identifier names 
	protected int x, y; // stores the x and y coordinates of the object
	protected ID id; // stores the id of the object
	protected int velX; // stores the x velocity of the object
	protected int velY; // stores the y velocity of the object
	protected String key; // stores the facing direction of the object
	protected boolean attacking = false; // stores a boolean to determine whether the object is attacking
	protected boolean hit; // stores a boolean to determine whether an object is hit
	protected boolean active = true; // stores a boolean to determine whether the object is active
	protected int health; // stores the current health of the object
	protected int maxHealth; // stores the maximum health of the object
	protected int damage; // stores the damage the object can do
	protected int EXP; // stores the current experience points of the object
	public int maxEXP; // stores the maximum experience points of the user

	// the constructor of this class sets the values of the essential things that all game objects need to have. It is parsed the x coordiante and y coordiante of where it will be placed and the ID of the object
	public GameObject(int x, int y, ID id) {
		this.x = x; // sets the x variable of the object equal to the one parsed
		this.y = y; // sets the y variable of the object equal to the one parsed
		this.id = id; // sets the ID of the object to the one parsed
	}
	
	// abstract methods that will be implemented for objects that inherit this class
	public abstract void tick();
	public abstract void render(Graphics g);
	public abstract Rectangle getBounds();
	
	// getters and setters for each of the variables. The setters take in a value and then assign it to the value in the class. The getters return the value of the variable 
	// these getter and setter methods are examples of cohesive subroutines
	//these getters and setter methods are examples of modles with appropriate interfaces
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	public void setId(ID id) {
		this.id = id;
	}
	public  ID getID() {
		return id;
	}
	public void SetVelX(int velX) {
		this.velX = velX;
	}
	public void SetVelY(int velY) {
		this.velY = velY;
	}
	public int getVelX() {
		return velX;
	}
	public int getVelY() {
		return velY;
	}
	public void SetKey(String key) {
		this.key = key;
	}
	public String getKey() {
		return key;
	}
	public void SetAttacking(boolean attacking) {
		this.attacking = attacking;
	}
	public boolean isAttacking() {
		return attacking;
	}
	public int getHealth() {
		return health;
	}

	public void setHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int health) {
		this.health = health;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public int getEXP() {
		return EXP;
	}

	public void setEXP(int EXP) {
		this.EXP = EXP;
	}
	
	public int getMaxEXP() {
		return maxEXP;
	}

	public void setMaxEXP(int maxEXP) {
		this.maxEXP = maxEXP;
	}
	
	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public void SetHit(boolean hit) {
		this.hit = hit;
	}
	public boolean isHit() {
		return hit;
	}

}