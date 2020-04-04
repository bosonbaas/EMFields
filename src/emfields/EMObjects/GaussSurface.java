/*
Developed for use by the LeTourneau University Physics Department

Andrew "Boson" Baas
modified 8/8/2015

This class extends the EMObj class. Its purpose is to store the information for
    a gaussian surface.
*/

package emfields.EMObjects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

public class GaussSurface extends EMObj {

    //Stores all of the coordinates of the guassian surface vertices
    private final ArrayList<Dimension> gaussCoord = new ArrayList<>();
    
    //Stores all of the fluxes of the corresponding sides
    private final ArrayList<Double> fluxes = new ArrayList<>();
    
    private EMVec vect;     //Vector which is used to determine flux
    
    private double angle = 0;           //Stores total angle changes
    private double totFlux = 0;         //Stores the total flux of the surface
    private boolean finished = false;   //Flag for if the surface is finished
    
    //Used for debugging (see update function)
    /*
    double min;
    double max;
    */

    //Adds another section to the the gaussian surface
    public void add(Dimension point, ArrayList<EMRod> rods) {
        gaussCoord.add(point);
        double flux = 0;
        if (gaussCoord.size() > 1) {
            flux = calcFlux(rods, point.getWidth(), point.getHeight(), gaussCoord.size() - 1);
        }
        totFlux += flux;
        fluxes.add(flux);
        angle += gaussAngleDif(gaussCoord, gaussCoord.size() - 1);
    }

    //Returns the coordinates of a point on the surface
    public Dimension get(int index) {
        return gaussCoord.get(index);
    }

    //Returns the number of points on the surface
    public int size() {
        return gaussCoord.size();
    }

    //Draws the gaussian surface
    public void paintObj(Graphics g, ArrayList<EMRod> rods, int width, int height, boolean pointCharge) {
        this.pointCharge = pointCharge;
        if (pointCharge) {
            return;
        }
        if (finished) {
            update(rods);
        }
        for (int i = 0; i < gaussCoord.size() - 1; i++) {
            int height1 = gaussCoord.get(i).height;
            int height2 = gaussCoord.get(i + 1).height;
            int width1 = gaussCoord.get(i).width;
            int width2 = gaussCoord.get(i + 1).width;
            int heightTot = height1 - height2;
            int widthTot = width1 - width2;
            double size = Math.sqrt(heightTot * heightTot + widthTot * widthTot);
            g.drawLine(width1, height1, width2, height2);
            if (i < fluxes.size() - 1) {
                int yDif = (int) (widthTot / size * fluxes.get(i + 1) / size);
                int xDif = (int) (-heightTot / size * fluxes.get(i + 1) / size);
                g.setColor(Color.GRAY);
                g.fillPolygon(new int[]{width1, width2, width2 + xDif, width1 + xDif}, new int[]{height1, height2, height2 + yDif, height1 + yDif}, 4);
                g.setColor(Color.black);
            }
        }
        if (finished) {
            g.setFont(new Font("default", Font.BOLD, 12));
            System.out.println(totFlux + " " + angle / (Math.PI * -2) + " " + ((totFlux * angle > 0) ? -.5 : .5));
            g.drawString("Q = " + (int) (totFlux / 6323.0 * -angle / Math.abs(angle) + ((totFlux * angle > 0) ? -.5 : .5)), (int) gaussCoord.get(0).getWidth() + 10, (int) gaussCoord.get(0).getHeight() - 10);
            System.out.println(totFlux);
        } else if (vect != null) {
            vect.paintObj(g, rods, 0, 0, pointCharge);
        }

    }

    //Determines the angle at an edge in the gaussian surface
    private double gaussAngleDif(ArrayList<Dimension> gaussCoord, int index) {
        double angleDif = 0;
        if (index > 2) {
            angleDif = Math.atan2(gaussCoord.get(index - 1).getHeight() - gaussCoord.get(index - 2).getHeight(), gaussCoord.get(index - 1).getWidth() - gaussCoord.get(index - 2).getWidth()) - Math.atan2(gaussCoord.get(index).getHeight() - gaussCoord.get(index - 1).getHeight(), gaussCoord.get(index).getWidth() - gaussCoord.get(index - 1).getWidth());
            if (angleDif > Math.PI) {
                angleDif -= 2 * Math.PI;
            } else if (angleDif < -Math.PI) {
                angleDif += 2 * Math.PI;
            }
        }
        return angleDif;
    }

    //Warps up the calculations for the gaussian surface
    public void wrapUp(ArrayList<EMRod> rods) {
        gaussCoord.add(gaussCoord.get(0));
        angle += gaussAngleDif(gaussCoord, gaussCoord.size() - 1);
        gaussCoord.add(gaussCoord.get(1));
        angle += gaussAngleDif(gaussCoord, gaussCoord.size() - 1);
        
        double prevFlux = totFlux;
        int size = gaussCoord.size() - 1;
        totFlux += calcFlux(rods, gaussCoord.get(size - 1).getWidth(), gaussCoord.get(size - 1).getHeight(), size - 1);
        fluxes.add(totFlux - prevFlux);
        finished = true;
        
        //Used for debugging (see update function)
        /*
        min = totFlux;
        max = totFlux;
        */
    }

    //Returns the total angle traversed by the surface
    public double getAngle() {
        return angle;
    }

    //Returns the total flux in the gaussian surface
    public double getTotFlux() {
        return totFlux;
    }

    //Calculates the flux for a side of the surface
    private double calcFlux(ArrayList<EMRod> rods, double xDim, double yDim, int index) {
        double gaussDif = 0;
        if (index > 0) {
            double xDif = gaussCoord.get(index - 1).getWidth() - xDim;
            double yDif = gaussCoord.get(index - 1).getHeight() - yDim;
            double length = Math.sqrt((xDif * xDif) + (yDif * yDif));
            for (double i = 0; i < length; i += .1) {
                vect = new EMVec((xDim + i * xDif / length), (yDim + i * yDif / length), rods);
                vect.updateMag(rods, pointCharge);
                gaussDif += vect.getMag() * .1 * Math.sin(Math.atan2(-vect.getyMag(), vect.getxMag()) - Math.atan2(yDif, -xDif));
            }
        }
        return gaussDif;
    }

    //Returns the type of this object
    @Override
    public String getType() {
        return "Gauss";
    }

    //Had to be included in order to implement EMObj
    @Override
    public int[] getDim() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //Updates the fluxes on the gaussian surface
    public void update(ArrayList<EMRod> rods) {
        fluxes.clear();
        totFlux = 0;
        angle = 0;
        for (int i = 0; i < gaussCoord.size() - 2; i++) {
            double flux = 0;
            if (gaussCoord.size() > 1) {
                flux = calcFlux(rods, gaussCoord.get(i).getWidth(), gaussCoord.get(i).getHeight(), i);
            }
            totFlux += flux;
            fluxes.add(flux);
            angle += gaussAngleDif(gaussCoord, i);
        }
        angle += gaussAngleDif(gaussCoord, gaussCoord.size() - 2);
        angle += gaussAngleDif(gaussCoord, gaussCoord.size() - 1);
        double flux;
        int size = gaussCoord.size() - 2;
        flux = calcFlux(rods, gaussCoord.get(size).getWidth(), gaussCoord.get(size).getHeight(), size);
        totFlux += flux;
        fluxes.add(flux);
        
        //Used this for debugging when adding optimizations to these calculations
        /*
        if (totFlux > max) {
            max = totFlux;
        }
        if (totFlux < min) {
            min = totFlux;
        }
        System.out.println("difference: " + (max - min));
        System.out.println("middle: " + ((max + min) / 2));
        */
    }

}
