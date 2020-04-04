/*
Developed for use by the LeTourneau University Physics Department

Andrew "Boson" Baas
modified 8/8/2015

This class extends a JPanel, and acts as the housing for all of the options
    for the EMPanel.
*/
package emfields;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JCheckBox;

public class EMMenu extends JPanel {

    protected final EMPanel parent; //Keeps this connected to its parent
    public int mouseFunc = 0;       //Stores the mouse mode
    ArrayList<EMButton> buttons;    //A structure to keep track of the buttons

    //All of the icons for the buttons are loaded here
    private final ImageIcon monoVec
            = new ImageIcon(this.getClass().getResource("Images/monoVec.png"));
    private final ImageIcon chromoVec
            = new ImageIcon(this.getClass().getResource("Images/chromoVec.png"));
    private final ImageIcon monoEqui
            = new ImageIcon(this.getClass().getResource("Images/monoEqui.png"));
    private final ImageIcon chromoEqui
            = new ImageIcon(this.getClass().getResource("Images/chromoEqui.png"));
    private final ImageIcon monoField
            = new ImageIcon(this.getClass().getResource("Images/monoField.png"));
    private final ImageIcon chromoField
            = new ImageIcon(this.getClass().getResource("Images/chromoField.png"));
    private final ImageIcon monoGauss
            = new ImageIcon(this.getClass().getResource("Images/monoGauss.png"));
    private final ImageIcon chromoGauss
            = new ImageIcon(this.getClass().getResource("Images/chromoGauss.png"));

    //Initializes the EMMenu, adds all of the buttons, and initializes them
    public EMMenu(EMPanel parent) {
        this.parent = parent;
        
        setPreferredSize(new Dimension(137 * 6, 75));
        setBackground(new Color(204, 210, 227));
        setLayout(new GridLayout(1, 6));
        this.setBorder(BorderFactory.createEmptyBorder(0,
                (parent.getPreferredSize().width - 137 * 6) / 2, 0,
                (parent.getPreferredSize().width - 137 * 6) / 2));

        JButton undo = new JButton("Undo");
        
        JCheckBox threeD = new JCheckBox("Point Charges");
        JCheckBox grid = new JCheckBox("Grid");
        JCheckBox coordinates = new JCheckBox("Coordinates");
        
        JPanel checkBoxes = new JPanel();
        
        checkBoxes.setLayout(new GridLayout(3, 0));
        checkBoxes.add(coordinates);
        checkBoxes.add(grid);
        checkBoxes.add(threeD);
        
        buttons = new ArrayList<>();

        buttons.add(new EMButton("Electric Force", 0, monoVec, chromoVec, this));
        buttons.add(new EMButton("Field Lines", 1, monoField, chromoField, this));
        buttons.add(new EMButton("Equipotential Lines", 2, monoEqui, chromoEqui, this));
        buttons.add(new EMButton("Gauss's Law", 3, monoGauss, chromoGauss, this));

        for (int i = 0; i < 4; i++) {
            add(buttons.get(i));
        }
        add(undo);
        add(checkBoxes);

        grid.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                parent.setGrid(grid.isSelected());
            }
        });

        undo.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                parent.undo();
            }
        });

        threeD.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                parent.setPointCharge(threeD.isSelected());
            }

        });

        coordinates.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                parent.setCoordinates(coordinates.isSelected());
            }

        });
    }

    public void setFunc(int mouseFunc, MouseEvent e) {
        if (!(parent.isPointCharge() && mouseFunc == 3)) {
            int prev = this.mouseFunc;
            parent.setFunc(mouseFunc, e);
            this.mouseFunc = mouseFunc;
            buttons.get(prev).resetButton();
        }

    }

    public void resetBorder() {
        this.setBorder(BorderFactory.createEmptyBorder(0, (parent.getWidth() - 137 * 6) / 2, 0, (parent.getWidth() - 137 * 6) / 2));
    }

    public int getFunc() {
        return this.mouseFunc;
    }
}
