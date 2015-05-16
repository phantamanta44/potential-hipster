package io.github.phantamanta44.potentialhipster;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

public class PotentiallyHip extends Frame {
	
	public static final int[] BLANK_ROW = {0, 0, 0, 0, 0};
	public static final int[][] DEFAULT_MATRIX = {BLANK_ROW, BLANK_ROW, {0, 0, 1, 0, 0}, BLANK_ROW, BLANK_ROW};
	public static Color CORNFLOWER_BLUE = new Color(100, 149, 237);
	
	private static final long serialVersionUID = 1L;
	private static PotentiallyHip instance;
	private static Map<String, File> fileList = new HashMap<>();
	
	public static void main(String[] args) {
		// Instantiate window and display
		instance = new PotentiallyHip();
		instance.setVisible(true);
		updateInfo();
	}
	
	public static void updateMatrix(int[][] matrix) {
		instance.setMatrix(matrix);
		instance.updateFilterStack();
	}
	
	public static boolean isPowerOf(int base, int n) {
		while (n % base == 0)
			n = n / base;
		return n == 1;
	}
	
	public static void updateInfo(String s) {
		instance.setInfoText(s);
	}
	
	public static void updateInfo() {
		updateInfo("Mouseover a control");
	}
	
	private Dimension resizeDim = new Dimension(32, 32);
	private Color colorizeColor = Color.BLACK;
	private int[][] convolutionMatrix = DEFAULT_MATRIX;
	
	private Panel ioPanel;
	private Button loadBtn;
	private Button saveBtn;
	
	private Panel optPanel;
	private Checkbox doResize;
	private Checkbox doColorize;
	private Checkbox doConvolve;
	
	private Panel filePanel;
	private java.awt.List fileDisplay;
	
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
		ioPanel.setLayout(new GridLayout(3, 1));
		
		loadBtn = new Button("Load");
		saveBtn = new Button("Save");
		
		loadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fc.setDialogTitle("Select image files to load.");
				fc.setFileFilter(new FileFilter() {
					public boolean accept(File f) {
						return (f.isDirectory() || f.getName().matches(".*\\.png"));
					}
					public String getDescription() {
						return "PNG Image Files";
					}
				});
				fc.setMultiSelectionEnabled(true);
				fc.showOpenDialog(PotentiallyHip.this);
				if (fc.getSelectedFiles() != null) {
					for (File f : fc.getSelectedFiles())
						fileList.put(f.getAbsolutePath(), f);
					instance.updateFileList();
				}
			}
		});
		saveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setDialogTitle("Select a directory to save image files in.");
				fc.showSaveDialog(PotentiallyHip.this);
				if (fc.getSelectedFile() != null && fc.getSelectedFile().isDirectory()) {
					String path = fc.getSelectedFile().getAbsolutePath();
					for (Entry<String, File> e : fileList.entrySet()) {
						try {
							BufferedImage tempImg = ImageIO.read(e.getValue());
							BufferedImage image = new BufferedImage(tempImg.getWidth(PotentiallyHip.this), tempImg.getHeight(PotentiallyHip.this), BufferedImage.TYPE_INT_ARGB);
						    Graphics2D g2d = image.createGraphics();
						    g2d.drawImage(tempImg, 0, 0, null);
						    g2d.dispose();
						    
						    image = applyFilters(image);
						    
						    File outFile = new File(path, e.getValue().getName());
						    ImageIO.write(image, "PNG", outFile);
						}
						catch (Throwable th) {
							th.printStackTrace();
						}
					}
				}
			}
		});
		
		optPanel = new Panel();
		optPanel.setLayout(new GridLayout(1, 3));
		
		doResize = new Checkbox("Resize");
		doColorize = new Checkbox("Colorize");
		doConvolve = new Checkbox("Convolve");
		
		ItemListener optUpdater = new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				updateFilterStack();
			}
		};
		doResize.addItemListener(optUpdater);
		doColorize.addItemListener(optUpdater);
		doConvolve.addItemListener(optUpdater);
		
		optPanel.add(doResize);
		optPanel.add(doColorize);
		optPanel.add(doConvolve);
		
		ioPanel.add(loadBtn);
		ioPanel.add(saveBtn);
		ioPanel.add(optPanel);
		this.add(ioPanel);
		
		// File Panel
		filePanel = new Panel();
		filePanel.setLayout(new GridLayout());
		
		fileDisplay = new java.awt.List();
		fileDisplay.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				updateFilterStack();
			}
		});
		fileDisplay.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_DELETE) {
					if (fileDisplay.getSelectedItem() != null) {
						fileList.remove(fileDisplay.getSelectedItem());
						updateFileList();
					}
				}
			}
			public void keyReleased(KeyEvent event) { }
			public void keyTyped(KeyEvent event) { }
		});
		
		filePanel.add(fileDisplay);
		this.add(filePanel);
		
		// Information Panel
		infoPanel = new Panel();
		infoPanel.setLayout(new GridLayout(2, 1));
		
		infoLabel = new Label();
		displayImg = new ImagePanel();
		
		displayImg.setBackground(CORNFLOWER_BLUE);
		
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
				updateFilterStack();
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
						updateFilterStack();
					}
				}
				else {
					if (!tf.getText().matches("[0-9]{1,}x") || !isPowerOf(2, Integer.parseInt(tf.getText().replaceAll("x", ""))))
						tf.setForeground(Color.RED);
					else {
						tf.setForeground(Color.BLACK);
						int asp = Integer.parseInt(tf.getText().replaceAll("x", ""));
						resizeDim.setSize(asp, asp);
						updateFilterStack();
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
				updateFilterStack();
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
		
		displayMatrix = new Button("Modify Matrix");
		
		displayMatrix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new MatrixDialogue(convolutionMatrix).setVisible(true);
			}
		});
		
		convolvePanel.add(displayMatrix);
		this.add(convolvePanel);
		
		registerTooltipListeners();
	}
	
	private void registerTooltipListeners() {
		loadBtn.addMouseListener(new TooltipListener("Load image(s)"));
		saveBtn.addMouseListener(new TooltipListener("Save image(s)"));
		doResize.addMouseListener(new TooltipListener("Toggle resizing"));
		doColorize.addMouseListener(new TooltipListener("Toggle colorizing"));
		doConvolve.addMouseListener(new TooltipListener("Toggle convolution"));
		fileDisplay.addMouseListener(new TooltipListener("Preview/delete image(s)"));
		displayImg.addMouseListener(new TooltipListener("Image preview"));
		resizeMode.addMouseListener(new TooltipListener("Rescale mode"));
		resizeVal.addMouseListener(new TooltipListener("Rescale value"));
		redSlider.addMouseListener(new TooltipListener("Red colour slider"));
		greenSlider.addMouseListener(new TooltipListener("Green colour slider"));
		blueSlider.addMouseListener(new TooltipListener("Blue colour slider"));
		displayMatrix.addMouseListener(new TooltipListener("Modify convolution matrix"));
	}
	
	public void setInfoText(String s) {
		infoLabel.setText(s);
	}
	
	public void setMatrix(int[][] matrix) {
		convolutionMatrix = matrix;
	}
	
	public void updateFileList() {
		fileDisplay.removeAll();
		for (Entry<String, File> f : fileList.entrySet())
			fileDisplay.add(f.getKey());
	}
	
	public void updateFilterStack() {
		if (fileDisplay.getSelectedItem() != null) {
			displayImg.setImage(fileList.get(fileDisplay.getSelectedItem()));
			if (doResize.getState())
				displayImg.resizeImage(resizeDim);
			if (doColorize.getState())
				displayImg.colorizeImage(colorizeColor);
			if (doConvolve.getState())
				displayImg.convolveImage(convolutionMatrix);
		}
		else
			displayImg.clearImage();
		displayImg.repaint();
	}
	
	public BufferedImage applyFilters(BufferedImage img) {
		if (doResize.getState()) {
			Image scaled = img.getScaledInstance((int)resizeDim.getWidth(), (int)resizeDim.getHeight(), Image.SCALE_FAST);
			img = new BufferedImage((int)resizeDim.getWidth(), (int)resizeDim.getHeight(), BufferedImage.TYPE_INT_ARGB);
		    Graphics2D g2d = img.createGraphics();
		    g2d.drawImage(scaled, 0, 0, null);
		    g2d.dispose();
		}
		if (doColorize.getState()) {
			BufferedImageOp oper = FilterFactory.generateColorization(colorizeColor);
		    img = oper.filter(img, null);
		}
		if (doConvolve.getState()) {
			BufferedImageOp oper = FilterFactory.generateConvolution(convolutionMatrix, convolutionMatrix.length);
			img = oper.filter(img, null);
		}
		return img;
	}
	
	private class TooltipListener implements MouseListener {
		private String s;
		public TooltipListener(String desc) {
			s = desc;
		}
		public void mouseEntered(MouseEvent event) {
			PotentiallyHip.updateInfo(s);
		}
		public void mouseExited(MouseEvent event) {
			PotentiallyHip.updateInfo();
		}
		public void mouseClicked(MouseEvent event) { }
		public void mousePressed(MouseEvent event) { }
		public void mouseReleased(MouseEvent event) { }
	}
	
}
