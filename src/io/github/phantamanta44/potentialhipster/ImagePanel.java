package io.github.phantamanta44.potentialhipster;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.io.File;

import javax.imageio.ImageIO;

public class ImagePanel extends Panel {
	
	private static final long serialVersionUID = 1L;
	Image image;
  
    public void paint(Graphics g) {
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
  
    public Dimension getPreferredSize() {
        return new Dimension(image.getWidth(this), image.getHeight(this));
    }
    
    public void setImage(File f) {
    	loadImage(f);
    }
    
    public void clearImage() {
    	image = null;
    }
  
    private void loadImage(File file) {
        try {
			image = ImageIO.read(file);
		}
        catch (Throwable ex) {
			ex.printStackTrace();
		}
    }
}