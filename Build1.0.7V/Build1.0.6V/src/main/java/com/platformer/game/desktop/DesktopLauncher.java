package com.platformer.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.platformer.game.Orchestrator;



public class DesktopLauncher {
	static public int fps = 30;
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setIdleFPS(fps);
		config.setTitle("Lightning Bolt Town");
		config.setWindowIcon(Files.FileType.Internal, "assets/flash.png");
		config.setWindowedMode(1600, 900);
		new Lwjgl3Application(new Orchestrator(), config);
	}
}
