package com.github.juupje.texer;

import java.util.ArrayList;

import com.github.juupje.calculator.helpers.ErrorHandler;
import com.github.juupje.calculator.helpers.IOHandler;
import com.github.juupje.calculator.helpers.SettingsHandler;
import com.github.juupje.calculator.main.Calculator;
import com.github.juupje.calculator.main.Command;
import com.github.juupje.calculator.main.Variables;
import com.github.juupje.calculator.main.plugins.Plugin;
import com.github.juupje.calculator.mathobjects.MathObject;

import javafx.scene.text.Font;

public class TeXer implements Plugin {

	boolean initialized = false;
	private ArrayList<TeXFrame> openFrames;
	public static TeXer instance;
	
	@Override
	public String getName() {
		return "TeXer";
	}

	@Override
	public void run() {
		Command.insertCommand("disp", new Command() {
			@Override
			public void process(String arg) {
				if(Variables.exists(arg))
					display(Variables.get(arg));
				else
					throw new IllegalArgumentException("Variable " + arg + " not found.");
			}
		});
	}

	@Override
	public void exit() {
		for(TeXFrame frame : openFrames)
			frame.dispose();
	}
	
	private void initialize() {
		instance = this;
		openFrames = new ArrayList<>();
		Font.loadFont(TeXer.class.getResourceAsStream("/org/scilab/forge/jlatexmath/fonts/base/jlm_cmmi10.ttf"), 1);
		Font.loadFont(TeXer.class.getResourceAsStream("/org/scilab/forge/jlatexmath/fonts/maths/jlm_cmsy10.ttf"), 1);
        Font.loadFont(TeXer.class.getResourceAsStream("/org/scilab/forge/jlatexmath/fonts/latin/jlm_cmr10.ttf"), 1);
        Font.loadFont(TeXer.class.getResourceAsStream("/org/scilab/forge/jlatexmath/fonts/base/jlm_cmex10.ttf"), 1);
        initialized = true;
	}
	
	public void display(MathObject mo) {
		if(!initialized)
			initialize();
		TeXFrame frame = new TeXFrame("LaTeX view", mo);
		frame.setVisible(true);
	}
	
	public void addFrame(TeXFrame frame) {
		openFrames.add(frame);
	}
	
	public boolean removeFrame(TeXFrame frame) {
		return openFrames.remove(frame);
	}
	
	public static void main(String[] args) {
		Calculator.setIOHandler(new IOHandler());
		Calculator.setErrorHandler(new ErrorHandler());
		Calculator.setSettingsHandler(new SettingsHandler());
		new TeXer().run();
		Calculator.start(args);
		new Calculator();
	}
}