import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class New_Snapshot
{
    //TODO: Make JFrame full screen
    //TODO: Make file save dialog
    //TODO: Make rectangle remove at area selected like Micrsoft clip tool

        static int clickCount = 1;
        static Point m1, m2;


        public static void main(String[] args) throws AWTException {
            Robot robot = new Robot();

            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            frame.setUndecorated(true);
            frame.setBackground(new Color(.8f, .8f, .8f, .1f));
            frame.setAlwaysOnTop(true);


            frame.addMouseListener(new MouseListener()
            {

                @Override
                public void mouseClicked(MouseEvent e)
                {
                    if (clickCount == 1)
                    {
                        m1 = new Point(e.getPoint());
                        System.out.println(m1.getLocation());
                        System.out.println("Click count: " + clickCount);
                    }
                    else if (clickCount == 2)
                    {
                        m2 = new Point(e.getPoint());
                        System.out.println(m2.getLocation());
                        System.out.println("Click count: " + clickCount);
                    }
                    else
                    {
                        // Nothing
                    }
                    clickCount++;
                }

                @Override
                public void mousePressed(MouseEvent e)
                {

                }

                @Override
                public void mouseReleased(MouseEvent e)
                {

                }

                @Override
                public void mouseEntered(MouseEvent e)
                {

                }

                @Override
                public void mouseExited(MouseEvent e)
                {

                }

            });

            frame.addKeyListener(new KeyListener()
            {

                @Override
                public void keyTyped(KeyEvent e)
                {

                }

                @Override
                public void keyPressed(KeyEvent e)
                {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    {
                        frame.setAlwaysOnTop(false);
                        frame.setVisible(false);
                        frame.pack();

                        BufferedImage pic = takeScreenshot(robot, m1, m2);
                        File outputfile = new File("saved.png");
                        try {
                            ImageIO.write(pic, "png", outputfile);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        System.out.println("Took pic");
                        System.exit(0);
                    }
                    else
                    {
                        System.out.println("Could not take screenshot");
                    }

                    }

                @Override
                public void keyReleased(KeyEvent e)
                {

                }

            });
            frame.setVisible(true);
        }

    private static BufferedImage takeScreenshot(Robot robot, Point m1, Point m2)
    {

        BufferedImage pic = null;
        if (Math.abs(m2.x - m1.x) == 0 || Math.abs(m2.y - m1.y) == 0)
        {
            System.out.println("Error taking screenshot. Please make sure that the heigh and width is greather than 0.");
        }
        else
        {
            pic = robot.createScreenCapture(new Rectangle(m1.x, m1.y, Math.abs(m2.x - m1.x), Math.abs(m2.y - m1.y)));
        }
        return pic;
    }
}
