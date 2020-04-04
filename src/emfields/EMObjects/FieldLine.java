/*
Developed for use by the LeTourneau University Physics Department

Andrew "Boson" Baas
modified 8/9/2015

This class extends the abstract class EMObj, and stores the information and
    calculations relating to a Field Line.
*/

package emfields.EMObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class FieldLine extends EMObj {

    //Constants relating to generation of a Field Line
    public final double PRECISION = 1;      //Precision present in Field line formation
    public final double COLOR_CONST = 2.75; //Determines color gradient
   
    private EMVec tangent;      //Stores the Vector at the point being analyzed
    
    //Store the origin of the field line
    private final double xInit;
    private final double yInit;

    //Initializes the Field Line variables
    public FieldLine(int xInit, int yInit) {
        this.xInit = xInit;
        this.yInit = yInit;
        tangent = new EMVec(xInit, yInit);
    }

    //Draws the Field Line object on the parent Panel
    @Override
    public void paintObj(Graphics g, ArrayList<EMRod> rods, int width, int height, boolean pointCharge) {
        this.pointCharge = pointCharge;

        double xDim = xInit;
        double yDim = yInit;
        int xPrev = (int) xInit;
        int yPrev = (int) yInit;
        double[] dif;
        boolean flag = true;
        
        //Calculates one side of the Field Line, starting at the point
        while (xDim > -400 && xDim < width + 400 && yDim > -400 && yDim < height + 400 && flag) {
            tangent.updateMag(rods, pointCharge);
            tangent.setxDim(xDim);
            tangent.setyDim(yDim);
            dif = calcDif();
            if ((xPrev - xDim) > 1 || (xPrev - xDim) < -1 || (yPrev - yDim) > 1 || (yPrev - yDim) < -1) {
                if (xDim > 0 && xDim < width && yDim > 0 && yDim < height - 100) {
                    double colorMag = tangent.getMag() * COLOR_CONST;
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
            for (EMRod rod : rods) {
                double xDist = rod.getxDim() - xDim;
                double yDist = rod.getyDim() - yDim;
                if (xDist * xDist + yDist * yDist < 100) {
                    flag = false;
                    break;
                }
            }
        }

        xDim = xInit;
        yDim = yInit;
        xPrev = (int) xInit;
        yPrev = (int) yInit;
        flag = true;
        
        
        //Calculates the other side of the Field Line, starting at the point
        while (xDim > -400 && xDim < width + 400 && yDim > -400 && yDim < height + 400 && flag) {
            tangent.updateMag(rods, pointCharge);
            tangent.setxDim(xDim);
            tangent.setyDim(yDim);
            dif = calcDif();
            if ((xPrev - xDim) > 1 || (xPrev - xDim) < -1 || (yPrev - yDim) > 1 || (yPrev - yDim) < -1) {
                if (xDim > 0 && xDim < width && yDim > 0 && yDim < height - 100) {
                    double colorMag = tangent.getMag() * COLOR_CONST;
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
            xDim -= dif[0];
            yDim -= dif[1];
            for (EMRod rod : rods) {
                double xDist = rod.getxDim() - xDim;
                double yDist = rod.getyDim() - yDim;
                if (xDist * xDist + yDist * yDist < 100) {
                    flag = false;
                    break;
                }
            }
        }
    }

    //Calculates the distance the next point is from the previous point
    public double[] calcDif() {
        double xVal = tangent.getxMag();
        double yVal = tangent.getyMag();
        double dist = Math.sqrt(xVal * xVal + yVal * yVal);
        double[] dif = new double[2];
        dif[0] = xVal / dist * PRECISION;
        dif[1] = yVal / dist * PRECISION;
        return dif;
    }

    //Returns the object's type
    @Override
    public String getType() {
        return "FieldLine";
    }

    //Put in here because it had to be overriden
    @Override
    public int[] getDim() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
