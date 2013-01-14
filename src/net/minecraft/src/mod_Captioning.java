package net.minecraft.src;

import java.io.*;
import java.util.*;
import net.minecraft.client.Minecraft;
import captionapi.*;

public class mod_Captioning extends BaseMod {
	// Copyright/license info
	private static final String Name = "Captioning";
	private static final String Version = "0.21 (For use with Minecraft 1.4.7)";
	private static final String Copyright = "All original code and images (C) 2012, Jonathan \"Wyrd\" Brazell";
	private static final String License = "This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.";
	// Other variables
	private static Minecraft mc = Minecraft.getMinecraft();
	private static File configFile = new File(mc.getMinecraftDir(), "/config/captioning.properties");
	private static CaptioningSoundPool captioningSoundPool;
	private static int tickCount = 0;
	private boolean cacheOption = false;
      	
	public void load() {
		loadOptions();
		setupSoundPool();
		ModLoader.setInGUIHook(this, true, false);
		ModLoader.setInGameHook(this, true, false);
	}

	private static void loadOptions() {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(configFile));
		} catch (Exception ignored) {}
		String captioning = properties.getProperty("captioning");
		if (captioning!=null)
			CaptionAPI.setCaptioning(captioning.compareToIgnoreCase("true")==0);
		saveOptions();
	}

	public static void saveOptions() {
		Properties properties = new Properties();
		properties.setProperty("captioning", Boolean.toString(CaptionAPI.getCaptioning()));
		try {
			properties.store(new FileOutputStream(configFile), "Options for CaptionAPI");
		} catch (Exception ignored) {}
	}

	private void setupSoundPool() {
		SoundPool parent;
		try {
			parent = (SoundPool)ModLoader.getPrivateValue(SoundManager.class, mc.sndManager, 1);
		} catch (Exception e) {
			// WTF, we got issues
			return;
		}  
		if (!(parent instanceof CaptioningSoundPool)) {
			// Replace sound pool
			captioningSoundPool = new CaptioningSoundPool(parent);
			try {
				ModLoader.setPrivateValue(SoundManager.class, mc.sndManager, 1, captioningSoundPool);
			} catch (Exception e) {
				// Cannot set the captioning up, it will try again in a second.
			}
		}
	}
	
	public boolean onTickInGUI(float ticks, Minecraft mc, GuiScreen screen) {
		if (screen instanceof GuiOptions) {
			if (!cacheOption) {
				cacheOption = true;
				int yPos = screen.height / 6 + 144 - 6;
				for (int idx=0; idx<screen.controlList.size(); idx++) {
					GuiButton btn = (GuiButton)screen.controlList.get(idx);
					if (btn.id==EnumOptions.TOUCHSCREEN.returnEnumOrdinal()) {
						yPos = btn.yPosition;
					}
				}
				screen.controlList.add(new CaptionButton(1064, screen.width / 2 + 5, yPos, 150, 20, "Captioning"));
			}
		} else {
			cacheOption = false;
		} 
		return true;
	}

	public boolean onTickInGame(float f, Minecraft paramMinecraft) {
		if (!CaptionAPI.getCaptioning())
			return true;
		if (tickCount++>20) {
			if (mc.gameSettings.soundVolume==0.0F) {
				mc.gameSettings.soundVolume = 0.01F;
			}
			setupSoundPool();
			tickCount = 0;
		}
		return true;
	}

	public String getName() {
		return Name;
	}
	
	public String getVersion() {
		return Version;
	}
}
