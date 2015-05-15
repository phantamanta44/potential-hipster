package io.github.phantamanta44.potentialhipster;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PotentiallyHip extends Frame {
	
	public static final int[] BLANK_ROW = {0, 0, 0, 0, 0};
	public static final int[][] DEFAULT_MATRIX = {BLANK_ROW, BLANK_ROW, {0, 0, 1, 0, 0}, BLANK_ROW, BLANK_ROW};
	
	private static final long serialVersionUID = 1L;
	private static PotentiallyHip instance;
	
	public static void main(String[] args) {
		// Instantiate window and display
		instance = new PotentiallyHip();
		instance.setVisible(true);
		instance.setInfoText("Load a resource pack.");
	}
	
	public static void updateMatrix(int[][] matrix) {
		instance.setMatrix(matrix);
	}
	
	public static boolean isPowerOf(int base, int n) {
		while (n % base == 0)
			n = n / base;
		return n == 1;
	}
	
	private Dimension resizeDim = new Dimension(32, 32);
	private Color colorizeColor = Color.BLACK;
	private int[][] convolutionMatrix = DEFAULT_MATRIX;
	
	private Panel ioPanel;
	private Button loadBtn;
	private Button saveBtn;
	
	private Panel filePanel;
	private java.awt.List fileList;
	
	private Panel infoPanel;
	private Label infoLabel;
	private ImagePanel displayImg;
	
	private Panel resizePanel;
	private Choice resizeMode;
	private TextField resizeVal;
	
	private Panel colourPanel;
	private JSlider redSlider, greenSlider, blueSlider;
	
	private Panel convolvePanel;
	private Button displayMatrix;
	
	private PotentiallyHip() {
		// Set up window
		this.setSize(new Dimension(480, 240));
		this.setLayout(new GridLayout(2, 3));
		this.setTitle("potential-hipster");
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				PotentiallyHip.this.dispose();
				System.exit(0);
			}
		});
		
		// Input-Output Panel
		ioPanel = new Panel();
		ioPanel.setLayout(new GridLayout(2, 1));
		
		loadBtn = new Button("Load");
		saveBtn = new Button("Save");
		
		ioPanel.add(loadBtn);
		ioPanel.add(saveBtn);
		this.add(ioPanel);
		
		// File Panel
		filePanel = new Panel();
		filePanel.setLayout(new GridLayout());
		
		fileList = new java.awt.List();
		
		fileList.add("lorem ipsum dolor set amet");
		
		filePanel.add(fileList);
		this.add(filePanel);
		
		// Information Panel
		infoPanel = new Panel();
		infoPanel.setLayout(new GridLayout(2, 1));
		
		infoLabel = new Label();
		displayImg = new ImagePanel();
		
		infoPanel.add(infoLabel);
		infoPanel.add(displayImg);
		this.add(infoPanel);
		
		// Resize Panel
		resizePanel = new Panel();
		resizePanel.setLayout(new GridLayout(2, 1));
		
		resizeMode = new Choice();
		resizeVal = new TextField("100%");
		
		resizeMode.addItem("By Percentage");
		resizeMode.addItem("By Resolution");
		resizeMode.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				if (event.getItem().equals("By Percentage")) {
					resizeVal.setText("100%");
				}
				else {
					resizeVal.setText("32x");
				}
			}
		});
		resizeVal.addTextListener(new TextListener() {
			public void textValueChanged(TextEvent event) {
				TextField tf = (TextField)event.getSource();
				if (resizeMode.getSelectedItem() == "By Percentage") {
					if (!tf.getText().matches("[0-9]{1,}%"))
						tf.setForeground(Color.RED);
					else {
						tf.setForeground(Color.BLACK);
						Double scale = Double.parseDouble(tf.getText().replaceAll("%", "")) * 0.01D;
						int asp = (int)(32 * scale);
						resizeDim.setSize(asp, asp);
					}
				}
				else {
					if (!tf.getText().matches("[0-9]{1,}x") || !isPowerOf(2, Integer.parseInt(tf.getText().replaceAll("x", ""))))
						tf.setForeground(Color.RED);
					else {
						tf.setForeground(Color.BLACK);
						int asp = Integer.parseInt(tf.getText().replaceAll("x", ""));
						resizeDim.setSize(asp, asp);
					}
				}
			}
		});
		
		resizePanel.add(resizeMode);
		resizePanel.add(resizeVal);
		this.add(resizePanel);
		
		// Colorizing Panel
		colourPanel = new Panel();
		colourPanel.setLayout(new GridLayout(1, 3));
		
		redSlider = new JSlider(SwingConstants.VERTICAL, 0, 255, 0);
		blueSlider = new JSlider(SwingConstants.VERTICAL, 0, 255, 0);
		greenSlider = new JSlider(SwingConstants.VERTICAL, 0, 255, 0);
		
		redSlider.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED));
		greenSlider.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.GREEN));
		blueSlider.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));
		ChangeListener colUpdater = new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				colorizeColor = new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue());
			}
		};
		redSlider.addChangeListener(colUpdater);
		greenSlider.addChangeListener(colUpdater);
		blueSlider.addChangeListener(colUpdater);
		
		colourPanel.add(redSlider);
		colourPanel.add(greenSlider);
		colourPanel.add(blueSlider);
		this.add(colourPanel);
		
		// Convolution Panel
		convolvePanel = new Panel();
		convolvePanel.setLayout(new GridLayout());
		
		displayMatrix = new Button("Display Matrix");
		
		displayMatrix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new MatrixDialogue(convolutionMatrix).setVisible(true);
			}
		});
		
		convolvePanel.add(displayMatrix);
		this.add(convolvePanel);
	}
	
	public void setInfoText(String s) {
		infoLabel.setText(s);
	}
	
	public void setMatrix(int[][] matrix) {
		convolutionMatrix = matrix;
	}
	
}
