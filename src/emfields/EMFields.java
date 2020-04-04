/*
Developed for use by the LeTourneau University Physics Department

Andrew "Boson" Baas
modified 8/8/2015

The purpose of this program is to emulate and display different aspects of 
    electrostatic fields. If you look hard enough through this code, you might
    find an easter-egg or two.

This class extends the JFrame and is used to open and house the EMPanel
*/

package emfields;

import java.awt.Color;
import javax.swing.*;

public class EMFields extends JFrame {

    public EMFields() {
        add(new EMPanel());
    }

    //Opens the EMPanel object
    public static void main(String[] args) {
        EMFields frame = new EMFields();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setBackground(Color.white);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(EMFields.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
