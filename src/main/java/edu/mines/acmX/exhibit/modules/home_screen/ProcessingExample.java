package edu.mines.acmX.exhibit.modules.home_screen;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import org.OpenNI.Context;
import org.OpenNI.DepthGenerator;
import org.OpenNI.DepthMap;
import org.OpenNI.DepthMetaData;
import org.OpenNI.GeneralException;
import org.OpenNI.GestureGenerator;
import org.OpenNI.GestureRecognizedEventArgs;
import org.OpenNI.IObservable;
import org.OpenNI.IObserver;
import org.OpenNI.ImageGenerator;
import org.OpenNI.ImageMetaData;
import org.OpenNI.OutArg;
import org.OpenNI.ScriptNode;

import edu.mines.acmX.exhibit.modules.home_screen.gameoflife.Grid;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

/*
 * TODO (in order)
 * Cleaning out the unnecessary crap
 * Start the Home Screen Skeleton (visual skeleton)
 * Implement one functioning backdrop
 * Hand tracking
 * Getting the feeds working
 * Connecting the Home Screen to the Module API
 */
public class ProcessingExample extends PApplet {
	private Grid g;
	private static final int DIM = 20;
	
	
	private Context context;

	private GestureGenerator gestureGen;

	private DepthGenerator depthGen;

	private ImageGenerator imageGen;

	private Dimension rgbDim;

	private int[] rgbImageArray;
	
	private static OutArg<ScriptNode> scriptNode;
    private static final String SAMPLE_XML_FILE = "openni_config.xml";
    
	class MyGestureRecognized implements IObserver<GestureRecognizedEventArgs> {
		public void update(IObservable<GestureRecognizedEventArgs> observable,
                           GestureRecognizedEventArgs args) {
			System.out.println("Captured a wave");
		}
	}
	
	
	public void setup() {
		g = new Grid();
		g.setup();
		
		size(screenWidth, screenHeight);
		background(81,81,81);
		
		scriptNode = new OutArg<ScriptNode>();
		try {
			
//			PrintWriter writer = new PrintWriter("BLAH.txt", "UTF-8");
//			writer.println("BLA");
//			writer.close();
			context = Context.createFromXmlFile(SAMPLE_XML_FILE, scriptNode);
		} catch (Exception e) {
			System.out.println("Error! " + e);
		}
		try {
			gestureGen = GestureGenerator.create(context);
	        gestureGen.addGesture("Wave");
	        gestureGen.getGestureRecognizedEvent().addObserver(new MyGestureRecognized());
	        System.out.println("Beginning capture!");
	        
	        context.startGeneratingAll();
	        
	     // depth generator map
            depthGen = DepthGenerator.create(context);
            DepthMetaData depthMD = depthGen.getMetaData();
            
            // Visual spectrum map generator
            imageGen = ImageGenerator.create(context);
            ImageMetaData imageMD = imageGen.getMetaData();
		} catch (GeneralException e) {
			e.printStackTrace();
		}
		
		
		/*
		 	Refactor into using OpenNi from the SDK rather than SimpleOpenNI
		 */
		
		/*
		 Detect screen size so you can properly scale everything
		 load module data
		 load twitter/weather...etc data
		 
		 */
	}
	

	
	public boolean sketchFullScreen() {
		return true;
	}
	
	public void update() {
		//g.nextGeneration();
	}
	
	public void draw() {
		update();
		stroke(66, 66, 66);
		//ellipse(mouseX, mouseY, 10, 10);
		strokeWeight(5);
		for (int i = 0; i < Grid.SIZE; ++i) {
				line(i * DIM, 0, i * DIM, screenHeight);
				line(0, i * DIM, screenWidth, i * DIM);
		}

	}
	
    public static void main( String[] args )
    {
        PApplet.main(new String[] {"ProcessingExample"});
    }
    
	public BufferedImage getDepthData(){
        DepthMetaData depthMD = depthGen.getMetaData();

        width = depthMD.getFullXRes();
        height = depthMD.getFullYRes();
        
        DepthMap dm = depthMD.getData();
        

        ShortBuffer depth = depthMD.getData().createShortBuffer();
        depth.rewind();

        BufferedImage bimg = new BufferedImage( width, height, BufferedImage.TYPE_BYTE_GRAY );

        while(depth.remaining() > 0)
        {
            int pos = depth.position();
            short pixel = depth.get();
            bimg.setRGB( pos%width, pos/width, pixel );
        }

        return bimg;
    }
    
    public BufferedImage getVisualData(){
        ImageMetaData imageMD = imageGen.getMetaData();

        rgbDim = new Dimension(imageMD.getFullXRes(), imageMD.getFullYRes());
        rgbImageArray = new int[rgbDim.width * rgbDim.height];
        BufferedImage bimg = new BufferedImage(rgbDim.width, rgbDim.height, BufferedImage.TYPE_INT_RGB);

        int i = 0;
        int r = 0;
        int g = 0;
        int b = 0;

        ByteBuffer rgbBuffer = imageMD.getData().createByteBuffer();
        for (int x = 0; x < rgbDim.width; x++) {
            for (int y = 0; y < rgbDim.height; y++) {
                i = y * rgbDim.width + x;
                r = rgbBuffer.get(i * 3) & 0xff;
                g = rgbBuffer.get(i * 3 + 1) & 0xff;
                b = rgbBuffer.get(i * 3 + 2) & 0xff;
                rgbImageArray[i] = (r << 16) | (g << 8) | b;
                bimg.setRGB( x, y, (r << 16) | (g << 8) | b );
            }
        }

        return bimg;
    }
    
	public PImage buffImagetoPImage(BufferedImage bimg) {
		PImage img = new PImage(bimg.getWidth(), bimg.getHeight(), PConstants.ARGB);
		bimg.getRGB(0, 0, img.width, img.height, img.pixels, 0, img.width);
		return img;
	}
	
	private BufferedImage buffToImage(ByteBuffer pixels) {
		int[] pixelInts = new int[width * height];
		for (int i = 0; i < width*height; ++i) {
			int tmp = 0xFF;
			for (int j = 0; j < 3; ++j) {
				tmp = tmp << 8;
				tmp = tmp | (pixels.get() & 0xFF);
			}
			
			pixelInts[i] = tmp;
		}
		
		BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		im.setRGB(0,  0, width, height, pixelInts, 0, width);
		return im;
	}
}
