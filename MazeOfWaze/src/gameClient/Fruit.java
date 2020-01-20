package gameClient;

import org.json.JSONObject;

import dataStructure.Edge;
import dataStructure.edge_data;
import utils.Point3D;

public class Fruit implements ifruit {
    private int id;
    private double value;
    private int type;
    private Point3D location;
    private Edge edge;
    boolean assigned;
    
    public Fruit (int id, String s) {
    	try {
    		this.id = id;
    		JSONObject obj = new JSONObject (s);
    		JSONObject f = obj.getJSONObject("Fruit");
    		this.value = f.getDouble("value");
    		this.type = f.getInt("type");
    		String [] pos = f.getString("pos").split(",");
    		this.location = new Point3D (Double.parseDouble(pos[0]), Double.parseDouble(pos[1]));
    		assigned = false;
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Point3D getLocation() {
		return location;
	}

	public void setLocation(Point3D location) {
		this.location = location;
	}

	public Edge getEdge() {
		return edge;
	}

	public void setEdge(Edge edge) {
		this.edge = edge;
	}

	public boolean isAssigned() {
		return assigned;
	}

	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}
    
	
    
}
