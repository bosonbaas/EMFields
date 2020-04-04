/*
Developed for use by the LeTourneau University Physics Department

Andrew "Boson" Baas
modified 8/8/2015

This class extends the JLabel class, yet acts as a button. Its specific purpose
    is to toggle between mouse modes.
*/
package emfields;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class EMButton extends JLabel {

    private final EMMenu parent;    //Menu which houses this button
    private final int mouseFunc;    //Mouse mode assigned to button
    private EMButton button = this; //Button object for use in listeners
    private boolean mouseClicked;   //Fixes multiple click issue
    private final ImageIcon mono;   //Allows reset function to access mono image

    // Initializes the object, expands it, and adds listeners
    public EMButton(String text, int mouseFunc, ImageIcon mono,
            ImageIcon chromo, EMMenu parent) {
        super();

        this.parent = parent;
        this.mouseFunc = mouseFunc;
        this.mono = mono;

        setToolTipText(text);
        setPreferredSize(new Dimension(137, 75));
        setBackground(Color.red);

        if (parent.getFunc() == this.mouseFunc) {
            setIcon(chromo);
        } else {
            setIcon(mono);
        }

        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);

                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setIcon(chromo);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);

                if (parent.mouseFunc != mouseFunc) {
                    setIcon(mono);
                }
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                mouseClicked = true;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (!mouseClicked) {
                    if (mouseFunc == 3) {
                        if ((e.getX() >= (button.getWidth() - 20))
                                && (e.getX() <= (button.getWidth()))
                                && (e.getY() >= (button.getHeight() - 25))
                                && (e.getY() <= (button.getHeight()))) {
                            parent.parent.openGaussExplanation();
                        } else {
                            parent.setFunc(mouseFunc, e);
                        }
                    } else {
                        parent.setFunc(mouseFunc, e);
                    }
                }
                mouseClicked = true;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                mouseClicked = false;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                if (!mouseClicked) {
                    mouseClicked(e);
                }
            }

        });
    }

    //Calibrates the button image with the parent's button status
    public void resetButton() {
        if (parent.mouseFunc != mouseFunc) {
            setIcon(mono);
        }
    }
}
