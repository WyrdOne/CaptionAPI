package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class mod_Captioning extends BaseMod {
  // Copyright/license info
  private static final String Name = "Captioning";
  private static final String Version = "0.1 (For use with Minecraft 1.4.5)";
	private static final String Copyright = "All original code and images (C) 2012, Jonathan \"Wyrd\" Brazell";
	private static final String License = "This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.";
  // Other variables
  private static Minecraft mc = Minecraft.getMinecraft();
  private static CaptioningSoundPool captioningSoundPool;
  private static int tickCount = 0;
      	
  public void load() {
    CaptionAPI.setCaptioning(true);
    setupSoundPool();
    ModLoader.setInGameHook(this, true, false);
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

  public boolean onTickInGame(float f, Minecraft paramMinecraft) {
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
