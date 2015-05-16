package io.github.phantamanta44.potentialhipster;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class MatrixDialogue extends Frame {

	private static final long serialVersionUID = 1L;
	private TextField[][] matrix = new TextField[5][5];
	
	public MatrixDialogue(int[][] initialMatrix) {
		initMatrix(initialMatrix);
		this.setSize(new Dimension(216, 216));
		this.setTitle("Convolution Matrix");
		this.setLayout(new GridLayout(5, 5));
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				MatrixDialogue.this.dispose();
			}
		});
	}
	
	private void initMatrix(int[][] intMatrix) {
		for (int i = 0; i < intMatrix.length; i++) {
			for (int j = 0; j < intMatrix[i].length; j++) {
				matrix[i][j] = new TextField(((Integer)intMatrix[i][j]).toString());
				matrix[i][j].addTextListener(new TextListener() {
					public void textValueChanged(TextEvent event) {
						TextField tf = (TextField)event.getSource();
						if (!tf.getText().matches("\\d{1,3}")
								|| Integer.parseInt(tf.getText()) > 255
								|| Integer.parseInt(tf.getText()) < 0)
							tf.setForeground(Color.RED);
						else
							tf.setForeground(Color.BLACK);
						updateMatrix();
					}
				});
				this.add(matrix[i][j]);
			}
		}
	}
	
	private void updateMatrix() throws NumberFormatException { // Stupid hacky stuff
		List<List<Integer>> intMatrix = new ArrayList<>(5);
		for (int i = 0; i < matrix.length; i++) {
			intMatrix.add(new ArrayList<Integer>(5));
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j].getText().matches("-?[0-9]{1,3}"))
					intMatrix.get(i).add(Integer.parseInt(matrix[i][j].getText()));
				else
					intMatrix.get(i).add(0);
			}
		}
		int[][] rawMatrix = new int[5][5];
		for (int i = 0; i < rawMatrix.length; i++) {
			for (int j = 0; j < rawMatrix[i].length; j++) {
				rawMatrix[i][j] = intMatrix.get(i).get(j);
			}
		}
		PotentiallyHip.updateMatrix(rawMatrix);
	}
}
