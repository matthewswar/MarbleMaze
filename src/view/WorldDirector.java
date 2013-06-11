package view;

import java.awt.Dimension;
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

@SuppressWarnings("serial")
public class WorldDirector extends JFrame
{
	public static final int UPDATE_RATE = 60;
	private final Canvas3D _canvas;
	private final Timer _time;
	private final Stage _stage;
	
	public WorldDirector()
	{
		super("Marble Maze");
		_stage = new Stage(50, 50);
		_canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
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
	
	private void setupWorld()
	{
		_canvas.addMouseMotionListener(new WorldRotator(_canvas, _stage));
		_canvas.addKeyListener(new KeyboardHandler(_stage.getPlayer()));
		final SimpleUniverse su = new SimpleUniverse(_canvas);
		final ViewingPlatform viewPlatform = su.getViewingPlatform();
		viewPlatform.setNominalViewingTransform();
		
		final View view = su.getViewer().getView();
		view.setSceneAntialiasingEnable(true);
		view.setBackClipDistance(100);
		
		final TransformGroup viewTransform = viewPlatform.getViewPlatformTransform();
		final Transform3D trans = new Transform3D();
		final Transform3D rot = new Transform3D();
		rot.rotX(-0.25f);
		viewTransform.getTransform(trans);
		trans.setTranslation(new Vector3f(0, 25, 100));
		trans.mul(rot);
		viewTransform.setTransform(trans);
		       
        su.addBranchGraph(createLighting());
        su.addBranchGraph(_stage);
        
	}
	
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
	
	private void tick()
	{
		_stage.update();
	}
	
}
