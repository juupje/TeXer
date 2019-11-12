package com.github.juupje.texer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.github.juupje.calculator.mathobjects.MathObject;

public class TeXFrame extends JFrame{ 

	private static final long serialVersionUID = -4062363590281487424L;
	TeXPane pane;
	public TeXFrame(String title, MathObject mo) {
		setTitle(title);
		setSize(500, 400);
		setContentPane(pane = new TeXPane(mo, 500, 400, this));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		TeXer.instance.addFrame(this);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				TeXer.instance.removeFrame((TeXFrame) e.getSource());
			}
		});
	}	
}