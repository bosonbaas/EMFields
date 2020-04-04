/*
Developed for use by the LeTourneau University Physics Department

Andrew "Boson" Baas
modified 8/9/2015

This class extends the EMObj abstract class. This class is the basis of the
    other EMObj classes, as it is used in the generation of the others. It also
    acts as a GUI object in itself.
*/

package emfields.EMObjects;

import emfields.EMPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class EMVec extends EMObj {

    //Stores the vector location
    private double xDim;
    private double yDim;
    
    //Stores the different charge magnitude contributions
    private double[][] standMag;
    
    //Stores the vector's magnitudes
    private double xMag;
    private double yMag;
    
    //Stores the vector's parent
    private EMPanel parent = null;

    //Acts as the electromagnetic constant
    private final double K = 1000;

    //Initializes the vector
    public EMVec(int xDim, int yDim, ArrayList rods) {
        this.xDim = xDim;
        this.yDim = yDim;
        updateMag(rods, pointCharge);
    }

    //Initializes the vector
    public EMVec(double xDim, double yDim, ArrayList rods) {
        this.xDim = xDim;
        this.yDim = yDim;
        updateMag(rods, pointCharge);
    }

    //Initializes the vector
    public EMVec(int xDim, int yDim) {
        this.xDim = xDim;
        this.yDim = yDim;
    }

    //Initializes the vector
    public EMVec(int xDim, int yDim, ArrayList rods, EMPanel parent) {
        this.xDim = xDim;
        this.yDim = yDim;
        this.parent = parent;
        updateMag(rods, pointCharge);
    }

    //Initializes the vector
    public void updateMag(ArrayList rods, boolean pointCharge) {
        this.pointCharge = pointCharge;
        calcStand(rods);
        xMag = calcXMag(standMag);
        yMag = calcYMag(standMag);
    }

    //Calculates the different magnitude contributions of each charge
    private void calcStand(ArrayList<EMRod> rods) {
        standMag = new double[3][rods.size()];

        for (int i = 18; i < rods.size(); i++) {
            EMRod charge = rods.get(i);
            double xDif = (charge.getxDim() - xDim);
            double yDif = (charge.getyDim() - yDim);
            double distSq = xDif * xDif + yDif * yDif;
            standMag[0][i - 18] = calcField(charge, distSq) / Math.sqrt(distSq);
            standMag[1][i - 18] = xDif;
            standMag[2][i - 18] = yDif;
        }
    }

    //Calculates the total x-magnitude of the vector
    private double calcXMag(double[][] standard) {
        double finalMag = 0;
        for (int i = 0; i < standard[0].length; i++) {
            finalMag += standard[0][i] * standard[1][i];
        }
        return finalMag;
    }

    //Calculates the total y-magnitude of the vector
    private double calcYMag(double[][] standard) {
        double finalMag = 0;
        for (int i = 0; i < standard[0].length; i++) {
            finalMag += standard[0][i] * standard[2][i];
        }
        return finalMag;
    }

    //Draws the vector arrow
    public void paintObj(Graphics g, ArrayList<EMRod> rods, int width, int height, boolean pointCharge) {
        updateMag(rods, pointCharge);
        g.setColor(Color.BLACK);
        g.fillOval((int) xDim - 3, (int) yDim - 3, 6, 6);
        g.drawLine((int) xDim, (int) yDim, (int) (xDim + xMag), (int) (yDim + yMag));

        g.setColor(Color.BLACK);
        if (getMag() > 0) {
            g.drawLine((int) (xDim + xMag), (int) (yDim + yMag), (int) (xDim + xMag)
                    + (int) (((xMag) * -.5 + (yMag) * -.5) / Math.sqrt(getMag()) * 2),
                    (int) ((yDim + yMag) - ((xMag) * -.5 - (yMag) * -.5) / Math.sqrt(getMag()) * 2));
            g.drawLine((int) (xDim + xMag), (int) (yDim + yMag), (int) (xDim + xMag)
                    + (int) (((xMag) * -.5 + (yMag) * .5) / Math.sqrt(getMag()) * 2),
                    (int) ((yDim + yMag) + ((xMag) * -.5 - (yMag) * .5) / Math.sqrt(getMag()) * 2));
        }
    }

    //Calculates the field magnitude from a certain EMRod
    private double calcField(EMRod charge, double distSqr) {
        if (pointCharge) {
            return -(K * 500 * charge.getCharge()) / distSqr;
        } else {
            return -(K * charge.getCharge()) / Math.sqrt(distSqr);
        }
    }

    //Returns the vector's x-dimension
    public double getxDim() {
        return xDim;
    }

    //Sets the vector's x-dimension
    public void setxDim(double xDim) {
        this.xDim = xDim;
    }

    //Returns the vecor's y-dimension
    public double getyDim() {
        return yDim;
    }

    //Sets the vector's y-dimension
    public void setyDim(double yDim) {
        this.yDim = yDim;
    }

    //Returns the vector's x-magnitude
    public double getxMag() {
        return xMag;
    }

    //Returns the vector's y-magnitude
    public double getyMag() {
        return yMag;
    }

    //Returns the vector's magnitude
    public double getMag() {
        return Math.sqrt(xMag * xMag + yMag * yMag);
    }

    //Returns the object's type
    @Override
    public String getType() {
        return "FieldVector";
    }

    //Returns the coordinates of the point in terms of traditional Euclidian coordinates
    @Override
    public int[] getDim() {
        if (parent != null) {
            return new int[]{(int) (this.getxDim() + .5), (int) (parent.getHeight() - this.getyDim() + .5), (int) (this.getxDim() + this.getxMag() + .5), (int) (parent.getHeight() - this.getyDim() - this.getyMag() + .5)};
        } else {
            return null;
        }
    }
}
