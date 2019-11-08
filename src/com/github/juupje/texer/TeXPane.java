package com.github.juupje.texer;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.scilab.forge.jlatexmath.Box;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import com.github.juupje.calculator.helpers.Printer;
import com.github.juupje.calculator.mathobjects.MathObject;

public class TeXPane extends JPanel {
	private static final long serialVersionUID = 8359735885061129030L;
	private Graphics2D g2d;
	private float scale = 40;
	private Box box;
	private JPanel canvasPanel;
	private JSlider slider;
	private MathObject mo;
	
	public TeXPane(MathObject mo, int width, int height) {
		this.mo = mo;
		createIcon();
		setSize(width, height);
		setLayout(new BorderLayout());
		add(canvasPanel = new JPanel() {
			private static final long serialVersionUID = -4951132986232501347L;
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				float offsetX = (canvasPanel.getWidth()/scale-box.getWidth())/2;
				float offsetY = (canvasPanel.getHeight()/scale + box.getHeight()/2f)/2;
				g2d = (Graphics2D) g;
				 RenderingHints rh = new RenderingHints(
			             RenderingHints.KEY_TEXT_ANTIALIASING,
			             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				g2d.addRenderingHints(rh);
				g2d.scale(scale, scale);
				box.draw(g2d, offsetX, offsetY);
			}
		}, BorderLayout.CENTER);
		add(slider = new JSlider(), BorderLayout.SOUTH);
		slider.setMinimum(20);
		slider.setMaximum(400);
		scale = Math.min(width/box.getWidth(), height/box.getHeight())*0.8f;
		slider.setValue((int)(Math.log(scale/2.7)/Math.log(1.5)*40-97.55));
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(slider.getValue() != scale) {
					scale = (int) (2.7*Math.pow(1.5d, 1/40f*(slider.getValue()+97.55)));
					setScale(scale);
				}
			}
		});
	}	
	
	public void createIcon() {
		TeXIcon icon = new TeXFormula(Printer.toLatex(mo)).createTeXIcon(TeXConstants.STYLE_DISPLAY, 50);
		icon.setInsets(new Insets(5,5,5,5));
		box = icon.getBox();
	}
	
	public void setScale(double scale) {
		this.scale = (float)scale;
		canvasPanel.repaint();
	}
}