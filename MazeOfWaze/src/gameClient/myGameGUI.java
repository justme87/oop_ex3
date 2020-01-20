package gameClient;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.graph;
import dataStructure.node_data;
import gui.Graph_GUI;
import utils.Point3D;
import utils.StdDraw;

public class myGameGUI implements Runnable {
	
	public static void play (int level, String gameMode)  throws IOException {
		Board b = new Board();
		game_service game = Game_Server.getServer(level);
		String g = game.getGraph();
		DGraph dg = new DGraph();
		dg.init(g);
		b.setG(dg);
		System.out.println(game.toString());
		
		StdDraw.setCanvasSize(1000, 1000);
		Graph_GUI.setLastGraph(dg);
		Thread gui = new Thread (new Graph_GUI());
		gui.start();
		try {
			gui.join();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		b.addFruits(game.getFruits());
		drawFruits(b);
		
		int RobotsCounter = 0;
		try {
			RobotsCounter = new JSONObject(game.toString()).getJSONObject("GameServer").getInt("robots");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		Algo algo = new Algo(b);
		Point3D p = StdDraw.getLastLoc();
		for (int i=0; i<RobotsCounter; i++) {
			if (gameMode.equals("Manual")) {
				while(StdDraw.getLastLoc() == null || close(p, StdDraw.getLastLoc())) {
					try {
						Thread.sleep(500);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				p = StdDraw.getLastLoc();
				StdDraw.setPenRadius(0.05);
				StdDraw.setPenColor(Color.blue);
				StdDraw.point(p.x(), p.y());
				int src = locNode(p, b.getG());
				game.addRobot(src);
			}
			else {
				List <Integer> l = algo.placeRobots(RobotsCounter);
				for (int j=0;j<l.size();j++) {
					game.addRobot(l.get(i));
					StdDraw.setPenRadius(0.05);
					StdDraw.setPenColor(Color.blue);
					p = b.getG().getNode(l.get(i)).getLocation();
					StdDraw.point(p.x(), p.y());
				}
			}
		}
		StdDraw.enableDoubleBuffering();
		game.startGame();
		b.addRobots(game.getRobots());
		while(game.isRunning()) {
			System.out.println(game.timeToEnd());
			if (gameMode.equals("Manual")) {
				if (StdDraw.getLastLoc() != null || !close(p, StdDraw.getLastLoc()) || locNode(StdDraw.getLastLoc(), b.getG()) != -1) {
					p = StdDraw.getLastLoc();
					move (game, b, locNode(p, b.getG()), algo);
				}
			}
			else {
				algo.basicGame(game);
			}
			repaint(game, b, gui);
			
			try {
				Thread.sleep(100);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println(game.toString());
	}
	
	private static void repaint(game_service gs, Board b, Thread t) {
		StdDraw.clear();
		Graph_GUI.setLastGraph(b.getG());
		t = new Thread (new Graph_GUI());
		t.start();
		try {
			t.join();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		drawFruits(b);
		drawRobots(b);
		StdDraw.show();
	}
	
	private static int locNode (Point3D p, graph g) {
		ArrayList<node_data> NL = (ArrayList<node_data>)g.getV();
		for (node_data nd : NL) {
			if (close(nd.getLocation(), p))
				return nd.getKey();
		}
		return -1;
	}
	
	private static void drawFruits(Board b) {
		for (int i=0; i<b.getFruits().size(); i++) {
			if (b.getFruits().get(i).getType() == -1)
				StdDraw.picture(b.getFruits().get(i).getLocation().x(),b.getFruits().get(i).getLocation().y() , "banana.jpg", 0.0008, 0.0008);
			else
				StdDraw.picture(b.getFruits().get(i).getLocation().x(),b.getFruits().get(i).getLocation().y() , "apple.jpg", 0.0008, 0.0008);
		}
	}
	
	private static void drawRobots(Board b) {
		List<Robot> robots = b.getRobots();
		StdDraw.setPenRadius(0.05);
		StdDraw.setPenColor(Color.green);
		for (int i=0; i<robots.size(); i++)
			StdDraw.point(robots.get(i).getLocation().x(), robots.get(i).getLocation().y());
	}
	
	private static boolean close (Point3D p1, Point3D p2) {
		if (p1==null || p2==null)
			return false;
		double epsilonX = 0.001;
		double epsilonY = 0.001;
		if (Math.abs(p1.x() - p2.x())>epsilonX)
			return false;
		if (Math.abs(p1.y() - p2.y())>epsilonY)
			return false;
		return true;
	}
	
	private static void move (game_service gs, Board b, int x, Algo a) {
		int robotToMove = a.closeToNode(x).get(0).getId();
		b.update(gs.move());
		b.addFruits(gs.getFruits());
		if (b.getRobots().get(robotToMove).getDest()==-1) {
			b.update(gs.move());
			gs.chooseNextEdge(robotToMove, x);
			Robot robot = b.getRobots().get(robotToMove);
			robot.setDest(x);
		}
		System.out.println("Robot " + robotToMove + " is moving to " + x);
	}
	
	public static void main (String[] args) {
		Thread t = new Thread(new myGameGUI());
		t.start();
	}
	
	
	@Override
	public void run() {
		while(true) {
			while (StdDraw.getMode().equals("") || StdDraw.getMap().equals("")) {
				try {
					Thread.sleep(500);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				play(Integer.parseInt(StdDraw.getMap().substring(1)), StdDraw.getMode());
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			StdDraw.setMap("");
			StdDraw.setMode("");
		}
	}
	
}
