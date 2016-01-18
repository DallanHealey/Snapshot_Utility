import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Snapshot
{

	private static boolean isScreenshot = false;
	private final static GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
	private final static GraphicsDevice[] devices = g.getScreenDevices();
	private final static int SCREEN_WIDTH = devices[0].getDisplayMode().getWidth();
	private final static int SCREEN_HEIGHT = devices[0].getDisplayMode().getHeight();
	private static int clickCount = 1;
	static Point m1, m2;
	static JLabel pic;
	public static JFrame window;
	public static JScrollPane scroll;
	public static JPanel scrollPanel;

	public static TrayIcon trayIcon;
	public static SystemTray systemTray;
	public static Image icon;

	static BufferedImage image;

	public static void main(String[] args) throws AWTException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		Robot robot = new Robot();
		icon = Toolkit.getDefaultToolkit().getImage("./icon/icon.png");
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		systemTray = SystemTray.getSystemTray();
		PopupMenu popup = new PopupMenu();
		MenuItem item = new MenuItem("Take Screenshot");
		item.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					pic = new JLabel(new ImageIcon(takeScreenshotFullsize(robot).getScaledInstance(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, Image.SCALE_SMOOTH)));
				}
				catch (InterruptedException e1)
				{
					e1.printStackTrace();
				}

				scrollPanel.add(pic);
				window.setVisible(true);
				window.pack();
			}
		});
		popup.add(item);
		popup.addSeparator();
		item = new MenuItem("Exit");
		item.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		popup.add(item);

		trayIcon = new TrayIcon(icon, "Snapshot", popup);
		trayIcon.setImageAutoSize(true);
		systemTray.add(trayIcon);

		GridLayout grid = new GridLayout(0, 1);

		window = new JFrame("Test");
		scrollPanel = new JPanel(new BorderLayout());

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(grid);
		window.setSize(600, 400);
		window.setVisible(false);
		// pic = new JLabel(new
		// ImageIcon(takeScreenshotFullsize(robot).getScaledInstance(SCREEN_WIDTH
		// / 2, SCREEN_HEIGHT / 2, Image.SCALE_SMOOTH)));
		// scrollPanel.add(pic);
		scroll = new JScrollPane(scrollPanel);
		window.add(scrollPanel);
		window.pack();

		window.addMouseListener(new MouseListener()
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

		window.addKeyListener(new KeyListener()
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
					clickCount = 1;
					System.out.println("Worked");
					if (m2 != null)
					{
						window.remove(pic);
						grid.setColumns(2);

						window.setVisible(false);
						window.pack();

						image = takeScreenshot(robot, m1, m2);
						pic = new JLabel(new ImageIcon(image));
						window.add(pic);
						window.setVisible(true);

						isScreenshot = true;

						JButton saveButton = new JButton("Save");

						saveButton.addActionListener(new ActionListener()
						{
							@Override
							public void actionPerformed(ActionEvent e)
							{
								try
								{
									ImageIO.write(image, "png", new File("test.png"));
								}
								catch (IOException e1)
								{
									System.out.println("Could not save image");
									e1.printStackTrace();
								}
							}
						});

						window.add(saveButton);
						System.out.println("Button added");
						window.pack();
					}
					else
					{

						System.out.println("Could not take screenshot");
					}

				}
			}

			@Override
			public void keyReleased(KeyEvent e)
			{

			}

		});

	}

	private static BufferedImage takeScreenshot(Robot robot, Point m1, Point m2)
	{
		BufferedImage pic = null;
		if (Math.abs(m2.x * 2 - m1.x * 2) == 0 || Math.abs(m2.y * 2 - m1.y * 2) == 0)
		{
			System.out.println("Error taking screenshot. Please make sure that the heigh and width is greather than 0.");
		}
		else
		{
			pic = robot.createScreenCapture(new Rectangle(m1.x * 2, m1.y * 2, Math.abs(m2.x * 2 - m1.x * 2), Math.abs(m2.y * 2 - m1.y * 2)));
		}
		return pic;
	}

	private static BufferedImage takeScreenshotFullsize(Robot robot) throws InterruptedException
	{
		Thread.sleep(2000);
		System.out.println("Screenshot taken");
		BufferedImage pic = null;

		pic = robot.createScreenCapture(new Rectangle(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT));

		return pic;
	}
}
