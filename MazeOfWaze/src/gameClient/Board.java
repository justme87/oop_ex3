package gameClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.json.JSONObject;

import dataStructure.DGraph;
import dataStructure.Edge;
import dataStructure.Node;
import dataStructure.edge_data;
import dataStructure.graph;
import utils.Point3D;

public class Board {
	
	private graph g;
	private List <Fruit> fruits;
	private List <Robot> robots;
	private int Rcounter, Fcounter;
	private Queue <Fruit> waitingList;
	
	public Board () {
		this.fruits = new ArrayList<Fruit>();
		this.robots = new ArrayList<Robot>();
		this.waitingList = new LinkedList<>();
		this.Fcounter = 0;
		this.Rcounter = 0;
		}

	public graph getG() {
		return g;
	}

	public void setG(graph g) {
		this.g = g;
	}

	public List<Fruit> getFruits() {
		return fruits;
	}

	public void setFruits(List<Fruit> fruits) {
		this.fruits = fruits;
	}

	public List<Robot> getRobots() {
		return robots;
	}

	public void setRobots(List<Robot> robots) {
		this.robots = robots;
	}

	public int getRcounter() {
		return Rcounter;
	}

	public void setRcounter(int rcounter) {
		Rcounter = rcounter;
	}

	public int getFcounter() {
		return Fcounter;
	}

	public void setFcounter(int fcounter) {
		Fcounter = fcounter;
	}

	public Queue<Fruit> getWaitingList() {
		return waitingList;
	}

	public void setWaitingList(Queue<Fruit> waitingList) {
		this.waitingList = waitingList;
	}
	
	public void putFruitOnEdge (Fruit fruit) {
		Point3D p = fruit.getLocation();
		ArrayList <edge_data> edges = (ArrayList <edge_data>)((DGraph)g).getAllE();
		ArrayList <edge_data> AL = new ArrayList <edge_data>();
		for (edge_data ed : edges) {
			if (onEdge((Edge)ed, p))
				AL.add(ed);
		}
		edge_data ed = null;
		if(fruit.getType()==1) {
			double max = Double.MIN_VALUE;
			for (int i=0; i<1; i++) {
				if (max<AL.get(i).getWeight()) {
					max = AL.get(i).getWeight();
					ed = AL.get(i);
				}
			}
		}
		else {
			double min = Double.MAX_VALUE;
			for (int i=0;i<1;i++) {
				if (min > AL.get(i).getWeight()) {
					min = AL.get(i).getWeight();
					ed = AL.get(i);
				}
			}
		}
		fruit.setEdge((Edge)ed);
	}

	private boolean onEdge(Edge e, Point3D p) {
		Point3D p1 = this.g.getNode(e.getSrc()).getLocation();
		Point3D p2 = this.g.getNode(e.getDest()).getLocation();
		if (Math.abs((p1.distance2D(p) + p2.distance2D(p)) - p1.distance2D(p2)) <= 0.0001)
			return true;
		return false;
	}
	
	public void addFruits (List<String> LS) {
		try {
			fruits = new ArrayList <Fruit>();
			Fcounter = 0;
			Iterator <String> is = LS.iterator();
			while (is.hasNext()) {
				Fruit f = new Fruit(Fcounter, is.next());
				putFruitOnEdge (f);
				fruits.add(f);
				Fcounter++;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addRobot (Robot r) {
		try {
			robots.add(r);
			r.setDest(-1);
			Rcounter++;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addRobots (List<String> LS) {
		try {
			robots = new ArrayList <Robot>();
			Iterator <String> is = LS.iterator();
			while (is.hasNext()) {
				Robot r = new Robot(is.next());
				robots.add(r);
				Rcounter++;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Robot findRobot (int id) {
		for (Robot r : robots) {
			if (r.getId() == id)
				return r;
		}
		System.out.println("Robot does not exist");
		return null;
	}
	
	public void update (List<String> LS) {
		try {
			for (String s : LS) {
				JSONObject obj = new JSONObject (s);
				JSONObject j = obj.getJSONObject("Robot");
				Robot r = findRobot(j.getInt("id"));
				r.update(s);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int move (Robot r, Node n) {
		return 1;
	}
}
