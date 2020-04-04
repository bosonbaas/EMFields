/*
Developed for use by the LeTourneau University Physics Department

Andrew "Boson" Baas
modified 8/8/2015

This class extends the JPanel, and is the main graphical interface for the field
    emulation. This manages all of the display and interactive features of the
    actual emulation
*/

package emfields;

import emfields.EMObjects.EquiLine;
import emfields.EMObjects.FieldLine;
import emfields.EMObjects.GaussSurface;
import emfields.EMObjects.EMVec;
import emfields.EMObjects.EMObj;
import emfields.EMObjects.EMRod;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class EMPanel extends JPanel {
    
    //Constants which define various aspects of the EMPanel layout
    private final int TOP_MENU_Y = 75;      //Height of the top menu
    private final int BOTTOM_MENU_Y = 100;  //Height of the bottom menu
    private final int GRID_SPACE = 30;      //Spacing between grid points
    private final Color BACKGROUND = new Color(245, 245, 250);  //Background color
    
    //Various environment flags
    private boolean trash = false;  //Flags if cursor is holding node over trash
    private boolean noPic = false;  //Flags if trash image should look open
    private boolean grid = false;   //Flags if a grid should be present
    private boolean pointCharge = false;    //Flags if charges are point charges
    private boolean coordinates = false;    //Flags if coordinates should be displayed
    private boolean gaussExpl = false;      //Flags if the guassian explanation is displayed
    private int gaussPage = 0;              //Gives the current page of the gauss explanation
    
    //Images for the trash can
    private final Image trashOpen 
            = new ImageIcon(this.getClass().getResource("Images/trashOpen.png")).getImage();
    private final Image trashClosed 
            = new ImageIcon(this.getClass().getResource("Images/trashClosed.png")).getImage();
    
    //Images for the Gaussian Explanation
    private ArrayList<Image> gaussImg = new ArrayList<>();
    
    //Hold the different objects to be represented on the screen
    private ArrayList<EMRod> rods = new ArrayList<>();
    private ArrayList<EMObj> objects = new ArrayList<>();
    
    //Used in handling the manipulation of EMRods
    private int tempInd;    //Stores the index of a held rod
    private EMRod temp;     //Stores the current rod being manipulated
    
    //GaussSurface handling variables
    private GaussSurface surface;   //Stores the GaussSurface being generated
    
    //Stores the previous mouse position for various uses
    private int prevX = 0;
    private int prevY = 0;
    
    //Stores the current dimensions of the EMPanel
    private int width;
    private int height;
    
    //Stores the various mouse modes
    private int mouseFunc = 0;      //Stores the current mouse mode
    public final int EM_VEC = 0;    //Mouse places field vectors
    public final int EM_LINE = 1;   //Mouse places field lines
    public final int EM_EQUI = 2;   //Mouse places equipotential lines
    public final int EM_GAUS = 3;   //Mouse creates gaussian surfaces
    
    private EMPanel parent = this;  //Allows variable access within listeners

    //Initializes the EMPanel object and all mouse listeners
    public EMPanel() {
        
        //Initializes the array of Gaussian Explanations
        gaussImg.add(new ImageIcon(this.getClass().getResource("Images/GaussExplanation1.png")).getImage());
        gaussImg.add(new ImageIcon(this.getClass().getResource("Images/GaussExplanation2.png")).getImage());
        gaussImg.add(new ImageIcon(this.getClass().getResource("Images/GaussExplanation3.png")).getImage());
        gaussImg.add(new ImageIcon(this.getClass().getResource("Images/GaussExplanation4.png")).getImage());
        gaussImg.add(new ImageIcon(this.getClass().getResource("Images/GaussExplanation5.png")).getImage());
        gaussImg.add(new ImageIcon(this.getClass().getResource("Images/GaussExplanation6.png")).getImage());
        gaussImg.add(new ImageIcon(this.getClass().getResource("Images/GaussExplanation7.png")).getImage());
        gaussImg.add(new ImageIcon(this.getClass().getResource("Images/GaussExplanation8.png")).getImage());
        this.setLayout(new BorderLayout());
        setBackground(BACKGROUND);
        this.setSize(1000, 1000);
        this.setPreferredSize(new Dimension(1000, 1000));
        
        EMMenu menu = new EMMenu(this);
        this.add(menu, BorderLayout.NORTH);
        
        width = getWidth();
        height = getHeight();
        
        int offset = (this.getWidth() - 40 * 18) / 2;
        
        //Initializes all source EMRods
        for (int i = 0; i < 18; i++) {
            if (i - 9 < 0) {
                rods.add(new EMRod(i - 9, 40 * i + offset,
                        this.getHeight() - 50));
            } else {
                rods.add(new EMRod(i - 8, 40 * i + offset,
                        this.getHeight() - 50));
            }

        }

        //Handles the resizing of the Panel
        this.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                int offset = (getWidth() - 40 * 18) / 2;
                for (int i = 0; i < 18; i++) {
                    if (i - 9 < 0) {
                        rods.get(i).setDim(40 * i + offset,
                                getHeight() - 50);
                    } else {
                        rods.get(i).setDim(40 * i + offset,
                                getHeight() - 50);
                    }

                }
                height = getHeight();
                width = getWidth();
                menu.resetBorder();
                revalidate();
                repaint();
            }

        });
        
        //Defines all static mouse functions
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                
                if(gaussExpl){
                    gaussPage += 1;
                    
                    if(gaussPage == 8){
                        openGaussExplanation();
                        gaussPage = 0;
                    }
                    revalidate();
                    repaint();
                    return;
                }
                
                //Decides if the mouse is on a rod
                for (int i = 0; i < rods.size(); i++) {
                    temp = rods.get(i);
                    tempInd = i;
                    if (Math.pow((temp.getxDim() - e.getX()), 2)
                            + Math.pow((temp.getyDim() - e.getY()), 2) < 100) {
                        if (i < 18) {
                            temp = new EMRod(temp.getCharge(), e.getX(),
                                    e.getY());
                            rods.add(temp);
                            tempInd = rods.size() - 1;
                        }
                        break;
                    }
                    temp = null;
                }

                //Uses mouse mode to determine what object to create
                if (temp == null) {
                    switch (mouseFunc) {
                        case 0:
                            objects.add(new EMVec(e.getX(), e.getY(), rods, parent));
                            break;
                        case 1:
                            objects.add(new FieldLine(e.getX(), e.getY()));
                            break;
                        case 2:
                            objects.add(new EquiLine(e.getX(), e.getY(), rods));
                            break;
                        case 3:
                            prevX = e.getX();
                            prevY = e.getY();
                            surface = new GaussSurface();
                            objects.add(surface);
                            surface.add(new Dimension(prevX, prevY), rods);

                    }
                }
                
                revalidate();
                repaint();

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                
                if(gaussExpl){
                    return;
                }
                
                temp = null;
                
                if (trash) {
                    rods.remove(tempInd);
                    trash = false;
                    noPic = false;
                }
                
                if (mouseFunc == 3 && surface != null) {
                    surface.wrapUp(rods);
                    surface = null;
                }
                
                revalidate();
                repaint();
            }

        });
        
        //Defines all moving mouse functions
        this.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                
                if(gaussExpl){
                    return;
                }
                
                if (temp != null) {
                    
                    //Moves the EMRod which is being held
                    if (grid) {
                        temp.setDim((e.getX() + GRID_SPACE / 2) / GRID_SPACE * GRID_SPACE + 1, (e.getY()) / GRID_SPACE * GRID_SPACE + 15);
                    } else {
                        temp.setDim(e.getX(), e.getY());
                    }
                    if (e.getX() > (getWidth() - 72)
                            && e.getY() > (getHeight() - 196)
                            && e.getY() < getHeight() - BOTTOM_MENU_Y) {
                        trash = true;
                        noPic = false;
                    } else if (e.getY() > getHeight() - BOTTOM_MENU_Y || 
                            e.getY() < TOP_MENU_Y) {
                        trash = true;
                        noPic = true;
                    } else {
                        trash = false;
                    }
                    
                    revalidate();
                    repaint();
                } else if (mouseFunc == 3) {
                    
                    //Adds to the GaussSurface which is being generated
                    if (((e.getX() - prevX) * (e.getX() - prevX) + (e.getY() - prevY) * (e.getY() - prevY)) > 25) {
                        surface.add(new Dimension(e.getX(), e.getY()), rods);

                        prevX = e.getX();
                        prevY = e.getY();
                        revalidate();
                        repaint();
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                
                if(gaussExpl){
                    return;
                }
                
                //Decides which mouse cursor to show
                if (e.getX() < 25 && e.getY() < 32 + TOP_MENU_Y && e.getY() > TOP_MENU_Y) {
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                } else {
                    for (int i = 0; i < rods.size(); i++) {
                        temp = rods.get(i);
                        if (Math.pow((temp.getxDim() - e.getX()), 2)
                                + Math.pow((temp.getyDim() - e.getY()), 2) < 100) {
                            setCursor(new Cursor(Cursor.HAND_CURSOR));
                            if (coordinates) {
                                parent.setToolTipText("(" + temp.getxDim() + ", " + (parent.getHeight() - temp.getyDim()) + ")");
                            }
                            break;
                        }
                        if (i == rods.size() - 1) {
                            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            temp = null;
                            parent.setToolTipText("");
                        }
                    }
                    
                    //Displays the coordinates of a point if coordinates is true
                    if (coordinates && temp == null) {
                        EMVec tempObj;
                        for (int i = 0; i < objects.size(); i++) {
                            if (objects.get(i).getType().contentEquals("FieldVector")) {
                                tempObj = (EMVec) objects.get(i);
                                if (Math.pow((tempObj.getxDim() - e.getX()), 2)
                                        + Math.pow((tempObj.getyDim() - e.getY()), 2) < 25) {
                                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                                    int[] dimensions = tempObj.getDim();
                                    parent.setToolTipText("B:(" + dimensions[0]
                                            + ", " + dimensions[1] + ")\n"
                                            + "T:(" + dimensions[2]
                                            + ", " + dimensions[3] + ")");
                                    break;
                                }
                                if (i == objects.size() - 1) {
                                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                                    parent.setToolTipText("");
                                }
                            }
                        }
                    }
                }
            }

        });
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        //Draws the grid
        if (grid) {
            for (int x = 1; x <= this.getWidth(); x += GRID_SPACE) {
                for (int y = TOP_MENU_Y; y <= this.getHeight() - BOTTOM_MENU_Y; y += GRID_SPACE) {
                    g.drawLine(x, y, x, y);
                }
            }
        }
        
        //Draws the trash
        if (trash && !noPic) {
            g.drawImage(trashOpen, this.getWidth() - 72, this.getHeight() - 196, this);
        } else {
            g.drawImage(trashClosed, this.getWidth() - 72, this.getHeight() - 196, this);
        }
        
        //Draws all objects on the Panel
        for (EMObj object : objects) {
            object.paintObj(g, rods, width, height, pointCharge);
        }
        
        //Generates the different menus
        if (temp != null) {
            for (int i = 18; i < rods.size(); i++) {
                if (!rods.get(i).equals(temp)) {
                    rods.get(i).paintRods(g);
                }
            }
            paintMenus(g);
            temp.paintRods(g);
        } else {
            for (int i = 18; i < rods.size(); i++) {
                rods.get(i).paintRods(g);
            }
            paintMenus(g);
        }
        
        if(gaussExpl){
            g.drawImage(gaussImg.get(gaussPage), 10, 10, parent);
        }

    }

    //Sets the mode of the mouse
    public void setFunc(int mouseFunc, MouseEvent e) {
        this.mouseFunc = mouseFunc;
    }

    //Sets whether the grid is utilized
    public void setGrid(boolean grid) {
        this.grid = grid;
        revalidate();
        repaint();
    }
    
    //Returns if the grid is utilized
    public boolean getGrid() {
        return this.grid;
    }

    //Removes the last object placed, not including EMRods
    public void undo() {
        if (objects.size() > 0) {
            objects.remove(objects.size() - 1);
            revalidate();
            repaint();
        }
    }

    //Draws the upper and lower menus
    private void paintMenus(Graphics g) {
        g.setColor(new Color(230, 230, 235));
        g.fillRect(0, parent.getHeight() - BOTTOM_MENU_Y, parent.getWidth(), parent.getHeight());
        g.setColor(Color.black);
        g.drawLine(0, getHeight() - BOTTOM_MENU_Y, getWidth(), getHeight() - BOTTOM_MENU_Y);
        g.drawLine(0, TOP_MENU_Y, getWidth(), TOP_MENU_Y);
        for (int i = 0; i < 18; i++) {
            rods.get(i).paintRods(g);
        }
    }

    //Returns whether the charge is a point charge
    public boolean isPointCharge() {
        return pointCharge;
    }

    //Sets whether the charge is a point charge
    public void setPointCharge(boolean pointCharge) {
        this.pointCharge = pointCharge;
        revalidate();
        repaint();
    }

    //Returns if coordinates are displayed
    public boolean isCoordinates() {
        return coordinates;
    }

    //Sets whether coordinates are displayed
    public void setCoordinates(boolean coordinates) {
        this.coordinates = coordinates;
    }

    //Opens the explanation for the Gaussian Surface algorithm
    public void openGaussExplanation() {
        gaussExpl = !gaussExpl;
        this.revalidate();
        this.repaint();
    }
}
