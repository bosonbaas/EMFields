/*
Developed for use by the LeTourneau University Physics Department

Andrew "Boson" Baas
modified 8/8/2015

This abstract class defines what every object the user interacts with must have.
*/
package emfields.EMObjects;

import java.awt.Graphics;
import java.util.ArrayList;

public abstract class EMObj {

    boolean pointCharge = false;

    abstract public void paintObj(Graphics g, ArrayList<EMRod> rods, int width,
            int height, boolean pointCharge);

    abstract public String getType();

    abstract public int[] getDim();
}
