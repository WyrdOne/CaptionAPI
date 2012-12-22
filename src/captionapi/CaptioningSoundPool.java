package net.minecraft.src;

import java.io.File;
import java.net.*;
import java.util.*;
import net.minecraft.client.Minecraft;

public class CaptioningSoundPool extends SoundPool {
  private static Minecraft mc = Minecraft.getMinecraft();
  private SoundPool parent;
  private static String lastCaption = "";
  
  public CaptioningSoundPool(SoundPool parent) {
    this.parent = parent;
  }

  public SoundPoolEntry addSound(String par1Str, File par2File) {
    parent.isGetRandomSound = isGetRandomSound;
    SoundPoolEntry rval = parent.addSound(par1Str, par2File);
    numberOfSoundPoolEntries = parent.numberOfSoundPoolEntries;
    return rval;
  }

  public SoundPoolEntry addSound(String var1, URL var2) {
    parent.isGetRandomSound = isGetRandomSound;
    SoundPoolEntry rval = parent.addSound(var1, var2);
    numberOfSoundPoolEntries = parent.numberOfSoundPoolEntries;
    return rval;
  }

  public SoundPoolEntry getRandomSoundFromSoundPool(String par1Str) {
    if (CaptionAPI.getCaptioning()) {
      String caption = CaptionAPI.getCaption(par1Str);
      if ((caption!=null) && (caption!=lastCaption)) {
        lastCaption = caption;
        mc.ingameGUI.getChatGUI().printChatMessage(caption);
      }
    }
    return parent.getRandomSoundFromSoundPool(par1Str);
  }

  public SoundPoolEntry getRandomSound() {
    return parent.getRandomSound(); 
  }
}
