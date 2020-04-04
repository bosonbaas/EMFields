/*
Developed for use by the LeTourneau University Physics Department

Andrew "Boson" Baas
modified 8/9/2015

This class holds the graphical and numerical data for a charged rod.
*/

package emfields.EMObjects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class EMRod {

    //Numerical data concerning the rod
    private final int charge;   //Charge value of the rod
    private int xDim;           //Position of the rod
    private int yDim;           //Position of the rod

    //Initializes the EMRod variables
    public EMRod(int charge, int xDim, int yDim) {
        this.charge = charge;
        this.xDim = xDim;
        this.yDim = yDim;
    }

    //Draws the EMRods
    public void paintRods(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (charge > 0) {
            g.setColor(new Color(92, 145, 255));
        } else {
            g.setColor(new Color(255, 140, 140));
        }
        g.fillOval(xDim - 10, yDim - 10, 20, 20);
        g2.setStroke(new BasicStroke((float) 1.5));
        if (charge > 0) {
            g.setColor(new Color(72, 125, 235));
            g.drawOval(xDim - 8, yDim - 8, 16, 16);
            g.setColor(new Color(52, 105, 215));
            g.drawOval(xDim - 9, yDim - 9, 18, 18);
            g.setColor(new Color(32, 85, 195));
            g.drawOval(xDim - 10, yDim - 10, 20, 20);
        } else {
            g.setColor(new Color(235, 120, 120));
            g.drawOval(xDim - 8, yDim - 8, 16, 16);
            g.setColor(new Color(215, 100, 100));
            g.drawOval(xDim - 9, yDim - 9, 18, 18);
            g.setColor(new Color(195, 80, 80));
            g.drawOval(xDim - 10, yDim - 10, 20, 20);
        }
        g.setColor(Color.black);
        g.setFont(new Font("default", Font.BOLD, 12));

        if (charge < 0) {
            g.drawString(Integer.toString(charge), xDim - 5, yDim + 5);
        } else {
            g.drawString(Integer.toString(charge), xDim - 3, yDim + 5);
        }
    }

    //Sets the coordinates of a rod
    public void setDim(int xDim, int yDim) {
        this.xDim = xDim;
        this.yDim = yDim;
    }

    //Returns the charge of the rod
    public int getCharge() {
        return charge;
    }

    //Returns the x dimension of the rod
    public int getxDim() {
        return xDim;
    }

    //Returns the y dimension of the rod
    public int getyDim() {
        return yDim;
    }

}
