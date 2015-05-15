package io.github.phantamanta44.potentialhipster;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Panel;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.ShortLookupTable;
import java.io.File;

import javax.imageio.ImageIO;

public class ImagePanel extends Panel {
	
	private static final long serialVersionUID = 1L;
	BufferedImage image;
  
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
    
    public void convolveImage(int[][] matrixRaw) {
    	float[] matrix = new float[25];
    	for (int i = 0; i < matrixRaw.length; i++) {
    		for (int j = 0; j < matrixRaw[i].length; j++) {
    			matrix[i + j] = matrixRaw[i][j];
    		}
    	}
    	Kernel kernel = new Kernel(5, 5, matrix);
    	ConvolveOp oper = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
    	image = oper.filter(image, null);
    }
  
    private void loadImage(File file) {
        try {
			image = ImageIO.read(file);
		}
        catch (Throwable ex) {
			ex.printStackTrace();
		}
    }

	public void resizeImage(Dimension dim) {
		Image scaled = image.getScaledInstance((int)dim.getWidth(), (int)dim.getHeight(), Image.SCALE_FAST);
		image = new BufferedImage((int)dim.getWidth(), (int)dim.getHeight(), BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = image.createGraphics();
	    g2d.drawImage(scaled, 0, 0, null);
	    g2d.dispose();
	}

	public void colorizeImage(Color col) {
		short[] alpha = new short[256];
	    short[] red = new short[256];
	    short[] green = new short[256];
	    short[] blue = new short[256];

	    for (short i = 0; i < 256; i++) {
	        alpha[i] = i;
	        red[i] = (short)((col.getRed() + i*.3)/2);
	        green[i] = (short)((col.getGreen() + i*.59)/2);
	        blue[i] = (short)((col.getBlue() + i*.11)/2);
	    }

	    short[][] data = new short[][] {
	            red, green, blue, alpha
	    };

	    LookupTable lookupTable = new ShortLookupTable(0, data);
	    LookupOp oper = new LookupOp(lookupTable, null);
	    image = oper.filter(image, null);
	}
}