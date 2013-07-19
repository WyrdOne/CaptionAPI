package net.minecraft.src;

import java.io.*;
import java.util.*;
import captionapi.*;
import com.wyrdworld.util.*;

public class mod_CaptionAPI extends BaseMod {
	// Copyright/license info
	private static final String Name = "Captioning";
	private static final String Version = "0.4 (For use with Minecraft 1.6.2)";
	private static final String Copyright = "All original code and images (C) 2012-2013, Jonathan \"Wyrd\" Brazell";
	private static final String License = "This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.";
	// Other variables
	private static Minecraft mc = Minecraft.getMinecraft();
	private static File configFile = new File(mc.mcDataDir, "/config/captioning.properties");
	private static int tickCount = 0;
	private boolean cacheOption = false;
	private SoundPool captioningSoundPool;
      	
	public void load() {
		loadOptions();
		setupSoundManager();
		ModLoader.setInGameHook(this, true, false);
		if (ModLoader.isModLoaded("Mod Options API (MOAPI)") || ModLoader.isModLoaded("mod_MOAPI")) {
			MOAPICaption.setup();
		} else {
			ModLoader.setInGUIHook(this, true, false);
		}
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

	private void setupSoundManager() {
		SoundPool parent = null;
		try {
			parent = ReflectionHelper.getPrivateValue(SoundManager.class, mc.sndManager, SoundPool.class, 0);
		} catch (Exception ignored) {}
		if (!(parent instanceof CaptioningSoundPool)) {
			// Replace sound pool
			try {
				ResourceManager resourceManager = ReflectionHelper.getPrivateValue(Minecraft.class, mc, ResourceManager.class, 0);
				captioningSoundPool = new CaptioningSoundPool(parent, resourceManager, "sound", true);
				ReflectionHelper.setPrivateValue(SoundManager.class, mc.sndManager, SoundPool.class, 0, captioningSoundPool);
			} catch (Exception ignored) {}
		}
	}
	
	public boolean onTickInGUI(float ticks, Minecraft mc, GuiScreen screen) {
		if (screen instanceof GuiOptions) {
			if (!cacheOption) {
				cacheOption = true;
				int yPos = screen.height / 6 + 144 - 6;
				for (int idx=0; idx<screen.buttonList.size(); idx++) {
					GuiButton btn = (GuiButton)screen.buttonList.get(idx);
					if (btn.id==EnumOptions.TOUCHSCREEN.returnEnumOrdinal()) {
						yPos = btn.yPosition;
					}
				}
				screen.buttonList.add(new CaptionButton(1064, screen.width / 2 + 5, yPos, 150, 20, CaptionAPI.getCaption("button.text")));
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
			setupSoundManager();
			tickCount = 0;
		}
		return true;
	}

	public String getPriorities() {
		return "after:mod_MOAPI";
	}
	
	public String getName() {
		return Name;
	}
	
	public String getVersion() {
		return Version;
	}
}
