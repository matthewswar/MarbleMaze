/*
 * Tyson Nottingham
 * Matthew Swartzendruber
 * 6/16/2013
 * Homework 4: Marble Maze
 */

package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Light;
import javax.media.j3d.PointLight;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import model.Stage;

import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

import controller.KeyboardHandler;
import controller.WorldRotator;

/**
 * 
 * @author Nottingham, Swartzendruber
 * @version June 16th, 2013
 * 
 * A window that contains the viewport into the virtual world.
 *
 */
@SuppressWarnings("serial")
public class WorldDirector extends JFrame
{
	/**
	 * The amount of times the world is updated every second.
	 */
	public static final int UPDATE_RATE = 60;
	
	/**
	 * The viewport into the virtual world.
	 */
	private final Canvas3D _canvas;
	
	/**
	 * The timer that ticks UPDATE_RATE times a second.
	 */
	private final Timer _time;
	
	/**
	 * The transformation matrix of the camera.
	 */
	private TransformGroup _viewTransform;
	
	/**
	 * The stage that the marble traverses around.
	 */
	private Stage _stage;
	
	/**
	 * The score the player previously received.
	 */
	private int _previousScore;
	
	/**
	 * Creates a window that displays the virtual world to the user.
	 * 
	 * @param theLevel The level the marble must traverse.
	 */
	public WorldDirector(final Stage theLevel)
	{
		super("Marble Maze");
		_stage = theLevel;
		_previousScore = 0;
		_canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration())
		{
			private final String _wins = "Wins: ";
			private final String _score = "Score: ";
			private final String _lastScore = "Previous Score: ";

			public void postRender()
			{
				// Draw on the canvas the amount of wins and the score
				final String winCount = _wins + _stage.getWins();
				final String scoreCount = _score + _stage.getScore();
				final String last = _lastScore + _previousScore;
				final int resolution = getSize().height; 
				final int fontSize = (int)Math.round(resolution / 25);
				final Font font = new Font("Impact", Font.PLAIN, fontSize);
				final FontMetrics fm = getGraphics2D().getFontMetrics(font);
				this.getGraphics2D().setColor(Color.WHITE);
				this.getGraphics2D().setFont(font);
				this.getGraphics2D().drawString(scoreCount, 10, fm.getHeight());
				this.getGraphics2D().drawString(winCount, 
						getSize().width - fm.stringWidth(winCount) - 10, fm.getHeight());
				this.getGraphics2D().drawString(last, 10, 
						getSize().height - fm.getHeight() / 5);
				this.getGraphics2D().flush(false);
			}
		};
		
		// Create a timer but do not start it yet
		_time = new Timer(1000 / UPDATE_RATE, new ActionListener()
		{
			public void actionPerformed(final ActionEvent theEvent) 
			{
				_canvas.stopRenderer();
				tick();
				_canvas.startRenderer();
			}
		});
	}
	
	/**
	 * Populates the window and starts the timer.
	 */
	public void start()
	{
		setPreferredSize(new Dimension(800, 600));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setupWorld();
		add(_canvas);
		_time.start();
		
		pack();
		
		if (Toolkit.getDefaultToolkit().
		        isFrameStateSupported(JFrame.MAXIMIZED_BOTH)) {
			setExtendedState(getExtendedState() |
			        JFrame.MAXIMIZED_BOTH);
		}
		setVisible(true);
	}
	
	/**
	 * Adds all of the necessary components of the virtual world render.
	 */
	private void setupWorld()
	{
		final SimpleUniverse su = new SimpleUniverse(_canvas);
		final ViewingPlatform viewPlatform = su.getViewingPlatform();
		viewPlatform.setNominalViewingTransform();
		
		final View view = su.getViewer().getView();
		view.setSceneAntialiasingEnable(true);
		view.setBackClipDistance(100);
		
		_viewTransform = viewPlatform.getViewPlatformTransform();
		final Transform3D trans = new Transform3D();
		final Transform3D rot = new Transform3D();
		rot.rotX(-Math.PI / 2);
		_viewTransform.getTransform(trans);
		trans.mul(rot);
		_viewTransform.setTransform(trans);
		       
        su.addBranchGraph(createLighting());
        su.addBranchGraph(_stage);
		_canvas.addMouseMotionListener(new WorldRotator(_canvas, _stage));
		_canvas.addKeyListener(new KeyboardHandler(_stage.getPlayer(), _viewTransform));
	}
	
	/**
	 * @return A branch group for the lighting of the world.
	 */
	private BranchGroup createLighting()
	{
		final BranchGroup lighting = new BranchGroup();
        Light light = new PointLight(new Color3f(.9f, .9f, .9f),
        		new Point3f(25, 50, 50), new Point3f(1, 0, 0));
        light.setInfluencingBounds(new BoundingSphere(new Point3d(), 1000));
        lighting.addChild(light);

        light = new PointLight(new Color3f(.9f, .9f, .9f),
        		new Point3f(-25, -50, -100), new Point3f(1, 0, 0));
        light.setInfluencingBounds(new BoundingSphere(new Point3d(), 1000));
        lighting.addChild(light);

        light = new DirectionalLight(new Color3f(.05f, .05f, .1f),
        		new Vector3f(0, 1, 0));
        light.setInfluencingBounds(new BoundingSphere(new Point3d(), 1000));
        lighting.addChild(light);
        
        lighting.compile(); 
        return lighting;
	}
	
	/**
	 * Updates the stage, checks for win/lose states, and ensures the camera follows the player.
	 */
	private void tick()
	{
		_stage.update();
		final Vector3f playerPos = _stage.getPlayer().position;
		final Transform3D trans = new Transform3D();
		_viewTransform.getTransform(trans);
		trans.setTranslation(new Vector3f(playerPos.x, playerPos.y + 200, playerPos.z));
		_viewTransform.setTransform(trans);
		if (playerPos.y < -100)
		{
			handleGameLost();
		}
		else if (_stage.getPlayer().isWinner())
		{
			handleGameWin();
		}
		
	}
	
	/**
	 * Handles lose states.
	 */
	private void handleGameLost()
	{
		if (_stage.getScore() == 0)
			_stage.reset();
		else
			_stage.getPlayer().reset();
	}
	
	/**
	 * Handles win states.
	 */
	private void handleGameWin()
	{
		_previousScore = _stage.getScore();
		_stage.reset();
		_stage.playerWin();
	}
	
}
