package io.github.phantamanta44.potentialhipster;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PotentiallyHip extends Frame {
	
	private static final long serialVersionUID = 1L;
	private static PotentiallyHip instance;
	
	public static void main(String[] args) {
		instance = new PotentiallyHip();
		instance.setVisible(true);
	}
	
	private Panel ioPanel;
	private Button loadBtn;
	private Button saveBtn;
	
	private Panel filePanel;
	private java.awt.List fileList;
	
	private Panel infoPanel;
	private Label infoLabel;
	private ImagePanel displayImg;
	
	private PotentiallyHip() {
		this.setSize(new Dimension(480, 240));
		this.setLayout(new GridLayout(2, 3));
		this.setTitle("potential-hipster");
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				System.exit(0);
			}
		});
		
		ioPanel = new Panel();
		ioPanel.setLayout(new GridLayout(2, 1));
		
		loadBtn = new Button("Load");
		saveBtn = new Button("Save");
		
		ioPanel.add(loadBtn);
		ioPanel.add(saveBtn);
		this.add(ioPanel);
		
		filePanel = new Panel();
		filePanel.setLayout(new GridLayout());
		
		fileList = new java.awt.List();
		
		fileList.add("lorem ipsum dolor set amet");
		
		filePanel.add(fileList);
		this.add(filePanel);
		
		infoPanel = new Panel();
		infoPanel.setLayout(new GridLayout(2, 1));
		
		infoLabel = new Label();
		displayImg = new ImagePanel();
		
		infoPanel.add(infoLabel);
		infoPanel.add(displayImg);
		this.add(infoPanel);
		
		this.add(new Label("Resize stuff here"));
		this.add(new Label("Recolour stuff here"));
		this.add(new Label("Convolution matrix here"));
	}
	
}
