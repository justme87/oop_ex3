package gameClient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONObject;

import utils.Point3D;

public class Robot implements irobot {

	private int id;
	private Point3D location;
	private int src, dest;
	private double value, speed;
	private Fruit fruit;
    private BlockingQueue<Integer> targets;
    
    public Robot (int src) {
    	this.src = src;
    }
	
    public Robot (String s) {
    	try {
    	JSONObject obj = new JSONObject (s);
    	JSONObject r = obj.getJSONObject("Robot");
    	this.id = r.getInt("id");
    	this.src = r.getInt("src");
    	this.dest = -1;
    	this.value = r.getDouble("value");
    	this.speed = r.getDouble("speed");
    	String [] pos = r.getString("pos").split(",");
    	this.location = new Point3D (Double.parseDouble(pos[0]), Double.parseDouble(pos[1]));
    	this.targets = new LinkedBlockingQueue<Integer>();
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void update (String s) {
    	try {
        	JSONObject obj = new JSONObject (s);
        	JSONObject r = obj.getJSONObject("Robot");
        	this.src = r.getInt("src");
        	this.dest = -1;
        	this.value = r.getDouble("value");
        	this.speed = r.getDouble("speed");
        	String [] pos = r.getString("pos").split(",");
        	this.location = new Point3D (Double.parseDouble(pos[0]), Double.parseDouble(pos[1]));
        	}
        	catch (Exception e) {
        		e.printStackTrace();
        	}
    }
	@Override
	public Point3D getCurrentLocation() {
		return this.location;
	}

	@Override
	public double currentScore() {
		return 0;
	}

	@Override
	public int getId() {
		return this.id;
	}

	public Point3D getLocation() {
		return location;
	}

	public void setLocation(Point3D location) {
		this.location = location;
	}

	public int getSrc() {
		return src;
	}

	public void setSrc(int src) {
		this.src = src;
	}

	public int getDest() {
		return dest;
	}

	public void setDest(int dest) {
		this.dest = dest;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public Fruit getFruit() {
		return fruit;
	}

	public void setFruit(Fruit fruit) {
		this.fruit = fruit;
	}

	public BlockingQueue<Integer> getTargets() {
		return targets;
	}

	public void setTargets(BlockingQueue<Integer> targets) {
		this.targets = targets;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	
	
}
