package gameClient;

import utils.Point3D;

public interface irobot {
    public Point3D getCurrentLocation();

    public double currentScore();

    public int getId();

    public Fruit getFruit();

    public double getSpeed();

    public void setId(int id);

    public void setDest(int dest);

    public void setLocation(Point3D p);

    public void setFruit(Fruit f);
}
