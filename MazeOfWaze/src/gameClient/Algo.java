package gameClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.edge_data;
import dataStructure.node_data;

public class Algo {
	
	private Board board;
	private Graph_Algo ga;
	
	public Algo (Board b) {
		this.board = b;
		this.ga = new Graph_Algo (b.getG());
	}
	
	public void setBoard (Board b) {
		this.board = b;
	}
	
	public Fruit closeToRobot (Robot r) {
		List <Fruit> FL = this.board.getFruits();
		Double close = Double.POSITIVE_INFINITY;
		Fruit ans = null;
		double x = 0;
		for (Fruit f : FL) {
			x = this.ga.shortestPathDist(r.getSrc(), f.getEdge().getSrc()) + f.getEdge().getWeight();
			if (x<close) {
				close = x;
				ans = f;
			}
		}
		return ans;
	}
	
	public List<Robot> closeToFruit (Fruit f){
		List<Robot> ans = new ArrayList<Robot>();
		List<Robot> robots = this.board.getRobots();
		List <Double> dist = new ArrayList<Double>();
		for (Robot r : robots)
			dist.add(ga.shortestPathDist(r.getSrc(), f.getEdge().getSrc()));
		List <Double> dists = new ArrayList<Double>();
		for (int i=0; i<robots.size(); i++) {
			int j=0;
			while (j<ans.size() && dists.get(j) < dist.get(i))
				j++;
			ans.add(j, robots.get(i));
			dists.add(j, dist.get(i));
		}
		return ans;
	}
	
	public  List<Robot> closeToNode (int id){
		List<Robot> ans = new ArrayList<Robot>();
		List<Robot> robots = this.board.getRobots();
		List <Double> dist = new ArrayList<Double>();
		for (Robot r : robots)
			dist.add(this.ga.shortestPathDist(r.getSrc(), id));
		List <Double> dists = new ArrayList<Double>();
		for (int i=0; i<robots.size(); i++) {
			int j=0;
			while (j<ans.size() && dists.get(j) < dist.get(i))
				j++;
			ans.add(j, robots.get(i));
			dists.add(j, dist.get(i));
		}
		return ans;
	}
	
	public List<Integer> placeRobots (int num){
		List <Integer> ans = new ArrayList<Integer>();
		int c = 0;
		for (Fruit f : this.board.getFruits()) {
			ans.add(f.getEdge().getSrc());
			c++;
		}
		for (int i = c; i < num; i++)
			ans.add(1+(int)Math.random()*this.board.getG().getV().size());
		return ans;
	}
	
	public void autoPlay (game_service g) {
		for (Robot r : this.board.getRobots()) {
			if (r.getDest() == -1) {
				Collection <edge_data> edges = this.board.getG().getE(r.getSrc());
				Iterator <edge_data> ei = edges.iterator();
				int size = edges.size();
				int x = (int) (Math.random()*size);
				for (int i=0; i<x; i++)
					ei.next();
				r.setDest(ei.next().getDest());
				g.chooseNextEdge(r.getId(), r.getDest());
				this.board.update(g.move());
				this.board.addFruits(g.getFruits());
				System.out.println("Robot " + r.getId() + " is moving to " + r.getDest());
			}
		}
	}
	
	public void hamdani (game_service g) {
		for (Fruit f : this.board.getFruits()) {
			Robot r = closeToFruit(f).get(0);
			if (r.getTargets().size() == 0) {
				List<node_data> NL = ga.shortestPath(r.getSrc(), f.getEdge().getSrc());
				BlockingQueue <Integer> bq = r.getTargets();
				for (node_data nd : NL)
					bq.add(nd.getKey());
				bq.add(f.getEdge().getDest());
				if (bq.peek() == r.getSrc())
					bq.remove();
				r.setFruit(f);
				f.setAssigned(true);
			}
		}
		for (Robot r : board.getRobots()) {
			if (r.getDest() == -1 && r.getTargets().size()>0) {
				r.setDest(r.getTargets().remove());
				g.chooseNextEdge(r.getId(), r.getDest());
				System.out.println("Robot " + r.getId() + " is moving to " + r.getDest());
			}
			if (r.getDest() == -1 && r.getTargets().size() == 0) {
				if (r.getFruit() !=null)
					r.getFruit().setAssigned(false);
				r.setFruit(null);
			}
		}
		this.board.update(g.move());
		this.board.addFruits(g.getFruits());
	}
	
	public void basicGame (game_service g) {
		for (Robot r : this.board.getRobots()) {
			if (r.getTargets().size() == 0) {
				if (r.getFruit()!=null)
					r.getFruit().setAssigned(false);
				Fruit f = closeToRobot (r);
				if (f.isAssigned()) {
					int x = (int) Math.random() * this.board.getFcounter();
					f = this.board.getFruits().get(x);
				}
				f.setAssigned(true);
				r.setFruit(f);
				List<node_data> NL = ga.shortestPath(r.getSrc(), f.getEdge().getSrc());
				for (node_data nd : NL)
					r.getTargets().add(nd.getKey());
				r.getTargets().add(f.getEdge().getSrc());
				if (r.getTargets().peek() == r.getSrc())
					r.getTargets().remove();
			}
			else {
				if (r.getDest() == 1) {
					r.setDest(r.getTargets().remove());
					System.out.println("Robot " + r.getId() + " is moving to " + r.getDest());
					g.chooseNextEdge(r.getId(), r.getDest());
				}
			}
			this.board.update(g.move());
			this.board.addFruits(g.getFruits());
		}
	}
	
}
