package gameClient;

import dataStructure.edge_data;
import utils.Point3D;

/**
 * interface to show capabilities of a Fruit
 */
public interface ifruit {
    public Point3D getLocation();

    public double getValue();

    public edge_data getEdge();

    public int getType();
}
