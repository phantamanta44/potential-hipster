package io.github.phantamanta44.potentialhipster;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Panel;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
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
    
    public void convolveImage(int[][] matrix) {
    	BufferedImageOp oper = FilterFactory.generateConvolution(matrix, matrix.length);
    	image = oper.filter(image, null);
    }
  
    private void loadImage(File file) {
        try {
			Image tempImg = ImageIO.read(file);
			image = new BufferedImage(tempImg.getWidth(this), tempImg.getHeight(this), BufferedImage.TYPE_INT_ARGB);
		    Graphics2D g2d = image.createGraphics();
		    g2d.drawImage(tempImg, 0, 0, null);
		    g2d.dispose();
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
		BufferedImageOp oper = FilterFactory.generateColorization(col);
	    image = oper.filter(image, null);
	}
}