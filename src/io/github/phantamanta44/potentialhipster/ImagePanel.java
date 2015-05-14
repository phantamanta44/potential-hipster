package io.github.phantamanta44.potentialhipster;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.awt.Toolkit;
import java.net.URL;

public class ImagePanel extends Panel {
	
	private static final long serialVersionUID = 1L;
	Image image;
  
    public ImagePanel() {
    	
    }
    
    public ImagePanel(String fn)
    {
        loadImage(fn);
    }
  
    public void paint(Graphics g)
    {
        super.paint(g);
        if (image != null) {
	        int w = getWidth();
	        int h = getHeight();
	        int imageWidth = image.getWidth(this);
	        int imageHeight = image.getHeight(this);
	        int x = (w - imageWidth)/2;
	        int y = (h - imageHeight)/2;
	        g.drawImage(image, x, y, this);
        }
    }
  
    /**
     * For the ScrollPane or other Container.
     */
    public Dimension getPreferredSize()
    {
        return new Dimension(image.getWidth(this), image.getHeight(this));
    }
    
    public void setImage(String fn) {
    	loadImage(fn);
    }
    
    public void clearImage() {
    	image = null;
    }
  
    private void loadImage(String fileName)
    {
        URL url = getClass().getResource(fileName);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        image = toolkit.getImage(url);
        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(image, 0);
        try
        {
            tracker.waitForID(0);
        }
        catch(InterruptedException ie)
        {
            System.out.println("interrupt: " + ie.getMessage());
        }
    }
}