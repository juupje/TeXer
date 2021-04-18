package com.github.juupje.texer;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;

import com.github.juupje.calculator.commands.Commands;
import com.github.juupje.calculator.helpers.ErrorHandler;
import com.github.juupje.calculator.helpers.io.IOHandler;
import com.github.juupje.calculator.helpers.io.JSONReader;
import com.github.juupje.calculator.main.Calculator;
import com.github.juupje.calculator.main.Variables;
import com.github.juupje.calculator.main.plugins.Plugin;
import com.github.juupje.calculator.mathobjects.MathObject;
import com.github.juupje.calculator.settings.SettingsHandler;

public class TeXer implements Plugin {

	private static final int VERSION_ID = 5;
	
	static boolean initialized = false;
	private ArrayList<TeXFrame> openFrames;
	public static TeXer instance;
	
	@Override
	public String getName() {
		return "TeXer";
	}

	@Override
	public void run() {
		openFrames = new ArrayList<>();
		Commands.insertCommand("disp", arg -> {
				if(Variables.exists(arg))
					display(Variables.get(arg));
				else
					throw new IllegalArgumentException("Variable " + arg + " not found.");
			});
	}
	
	@Override
	public JSONObject initHelp() {
		try {
			return JSONReader.parse(TeXer.class.getResourceAsStream("/com/github/juupje/texer/files/help.json"));
		} catch (IOException e) {
			Calculator.errorHandler.handle("Couldn't load help file of TeXer.", e);
		}
		return null;
	}
	
	@Override
	public int version() {
		return VERSION_ID;
	}

	@Override
	public void exit() {
		for(TeXFrame frame : openFrames)
			frame.dispose();
	}
	
	private void initialize() {
		instance = this;
		/*Font.loadFont(TeXer.class.getResourceAsStream("/org/scilab/forge/jlatexmath/fonts/base/jlm_cmmi10.ttf"), 1);
		Font.loadFont(TeXer.class.getResourceAsStream("/org/scilab/forge/jlatexmath/fonts/maths/jlm_cmsy10.ttf"), 1);
        Font.loadFont(TeXer.class.getResourceAsStream("/org/scilab/forge/jlatexmath/fonts/latin/jlm_cmr10.ttf"), 1);
        Font.loadFont(TeXer.class.getResourceAsStream("/org/scilab/forge/jlatexmath/fonts/base/jlm_cmex10.ttf"), 1);
        */initialized = true;
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
		Calculator.parseArgs(args);
		new TeXer().run();
		Calculator.start();
		new Calculator();
	}
}