/*
Developed for use by the LeTourneau University Physics Department

Andrew "Boson" Baas
modified 8/9/2015

This class extends the EMObj abstract class, and stores the information and
    calculations relating to an Equipotential Line.
*/

package emfields.EMObjects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

public class EquiLine extends EMObj {

    //Constants for Equipotential Line generation
    public final double PRECISION = 1;      //Determines the length of side lines
    public final double COLOR_CONST = 2.75; //Determines color gradient
    
    private EMVec normal;       //Stores the Vector at the point being analyzed

    //Starting point coordinates
    private final double xInit;
    private final double yInit;
   
    private double potStand;    //Stores the potential at the starting point
    
    //Initializes the Equipotential Line variables
    public EquiLine(int xInit, int yInit, ArrayList<EMRod> rods) {
        this.xInit = xInit;
        this.yInit = yInit;
        normal = new EMVec(xInit, yInit);
        normal.updateMag(rods, pointCharge);
        potStand = calcPot(rods, xInit, yInit);
    }

    //Draws the Field Line object on the parent Panel
    public void paintObj(Graphics g, ArrayList<EMRod> rods, int width, int height, boolean pointCharge) {
        this.pointCharge = pointCharge;
        potStand = calcPot(rods, xInit, yInit);
        
        //Calculates both sides of the Field Line, starting at the point
        for (int sign = -1; sign < 2; sign += 2) {
            double xDim = xInit;
            double yDim = yInit;
            double tempPot = calcPot(rods, xDim, yDim);
            int counter = 0;
            int xPrev = (int) xInit;
            int yPrev = (int) yInit;
            double[] dif = new double[2];
            boolean flag = true;
            while (xDim > -400 && xDim < width + 400 && yDim > -400 && yDim < height + 400 && flag) {
                normal.setxDim(xDim);
                normal.setyDim(yDim);
                normal.updateMag(rods, pointCharge);
                dif = calcDif();
                dif[0] *= sign;
                dif[1] *= sign;
                tempPot = calcPot(rods, xDim + dif[0], yDim + dif[1]);
                double[] dif2 = calcDifPos();
                if (tempPot < potStand) {
                    while (tempPot < potStand && xDim + dif[0] > -400 && xDim + dif[0] < width + 400 && yDim + dif[1] > -400 && yDim + dif[1] < height + 400) {
                        dif[0] += dif2[0] * .1;
                        dif[1] += dif2[1] * .1;
                        tempPot = calcPot(rods, xDim + dif[0], yDim + dif[1]);
                    }
                } else {
                    while (tempPot > potStand && xDim + dif[0] < width + 400 && yDim + dif[1] > -400 && yDim + dif[1] < height + 400) {
                        dif[0] -= dif2[0] * .1;
                        dif[1] -= dif2[1] * .1;
                        tempPot = calcPot(rods, xDim + dif[0], yDim + dif[1]);
                    }
                }
                if ((xPrev - xDim) > 1 || (xPrev - xDim) < -1 || (yPrev - yDim) > 1 || (yPrev - yDim) < -1) {
                    if (xDim > 0 && xDim < width && yDim > 0 && yDim < height - 100 && counter != 1) {
                        double colorMag = normal.getMag() * COLOR_CONST;
                        if (colorMag < 200 / 1.5) {
                            g.setColor(new Color((int) (200 - colorMag * 1.5), (int) (200 - colorMag * 1.5), (int) (245 - colorMag / 4)));
                        } else if (colorMag < 200) {
                            g.setColor(new Color((int) (colorMag - 200.0 / 2.0) * 2, 0, (int) (245 - colorMag / 4)));
                        } else {
                            g.setColor(new Color(255, 0, 0));
                        }
                        g.drawLine(xPrev, yPrev, (int) (xDim), (int) (yDim));
                    }
                    xPrev = (int) xDim;
                    yPrev = (int) yDim;
                }
                xDim += dif[0];
                yDim += dif[1];
                for (int i = 18; i < rods.size(); i++) {
                    EMRod rod = rods.get(i);
                    double xDist = rod.getxDim() - xDim;
                    double yDist = rod.getyDim() - yDim;
                    if (xDist * xDist + yDist * yDist < 100) {
                        flag = false;
                        break;
                    }
                }

                if (counter > 20 && Math.abs(xDim - xInit) < 1 && Math.abs(yDim - yInit) < 1) {
                    break;
                }
                if (counter > 3000) {
                    break;
                }
                counter++;
            }
        }
        g.setColor(Color.black);
        g.setFont(new Font("default", Font.BOLD, 12));
        g.drawString((int) (-potStand * 100) / 100. + " ", (int) xInit + 7, (int) yInit - 7);
    }
    
    //Calculates the distance the next point is from the previous point in one direction
    private double[] calcDif() {
        double xVal = normal.getxMag();
        double yVal = normal.getyMag();
        double dist = Math.sqrt(xVal * xVal + yVal * yVal);
        double[] dif = new double[2];
        dif[0] = -yVal / dist * PRECISION;
        dif[1] = xVal / dist * PRECISION;
        return dif;
    }

    //Calculates the distance the next point is from the previous point in the other direction
    private double[] calcDifPos() {
        double xVal = normal.getxMag();
        double yVal = normal.getyMag();
        double dist = Math.sqrt(xVal * xVal + yVal * yVal);
        double[] dif = new double[2];
        dif[0] = xVal / dist * PRECISION;
        dif[1] = yVal / dist * PRECISION;
        return dif;
    }

    //Calculates the potential at the point
    private double calcPot(ArrayList<EMRod> rods, double xDim, double yDim) {
        double potential = 0;
        for (int i = 18; i < rods.size(); i++) {
            EMRod rod = rods.get(i);
            double xDif = rod.getxDim() - xDim;
            double yDif = rod.getyDim() - yDim;
            if (pointCharge) {
                potential += -rod.getCharge() / Math.sqrt(xDif * xDif + yDif * yDif) * 1000.0;
            } else {
                potential += rod.getCharge() * Math.log(Math.sqrt(xDif * xDif + yDif * yDif));
            }

        }
        return potential;
    }

    //Returns the object's type
    @Override
    public String getType() {
        return "Equipotential";
    }

    //Overridden so it can extend EMObj
    @Override
    public int[] getDim() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
