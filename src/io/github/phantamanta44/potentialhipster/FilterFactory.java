package io.github.phantamanta44.potentialhipster;

import java.awt.Color;
import java.awt.image.BufferedImageOp;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.ShortLookupTable;

import com.jhlabs.image.ConvolveFilter;

public class FilterFactory {
	
	public static BufferedImageOp generateConvolution(int[][] matrix, int dim, boolean alpha) {
		float[] kernelArray = new float[dim * dim];
    	for (int i = 0; i < matrix.length; i++) {
    		for (int j = 0; j < matrix[i].length; j++)
    			kernelArray[(i * 5) + j] = matrix[i][j];
    	}
    	Kernel kernel = new Kernel(dim, dim, kernelArray);
    	ConvolveFilter oper = new ConvolveFilter(kernel);
    	oper.setEdgeAction(ConvolveFilter.CLAMP_EDGES);
    	oper.setUseAlpha(alpha);
    	return oper;
	}
	
	public static BufferedImageOp generateConvolution(int[][] matrix, int dim) {
		return generateConvolution(matrix, dim, true);
	}
	
	public static BufferedImageOp generateColorization(Color col) {
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
	    return oper;
	}
	
}
