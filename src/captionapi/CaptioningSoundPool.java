package captionapi;

import net.minecraft.src.*;

import java.io.File;
import java.net.*;
import java.util.*;

import com.wyrdworld.util.ReflectionHelper;

public class CaptioningSoundPool extends SoundPool {
  private static Minecraft mc = Minecraft.getMinecraft();
  private SoundPool parent;
  private static String lastCaption = "";
  private final boolean isGetRandomSound;
  
  public CaptioningSoundPool(SoundPool parent, ResourceManager par1ResourceManager, String par2Str, boolean par3) {
	  super(par1ResourceManager, par2Str, par3);
      this.isGetRandomSound = par3;
	  this.parent = parent;
  }

  public void addSound(String var1) {
	  try {
		  ReflectionHelper.setPrivateValue(SoundPool.class, parent, boolean.class, 0, isGetRandomSound);
	  } catch (Exception ignored) {}
	  parent.addSound(var1);
  }

  public SoundPoolEntry getRandomSoundFromSoundPool(String par1Str) {
//System.out.println("key="+par1Str);	  
    if (CaptionAPI.getCaptioning()) {
      String caption = CaptionAPI.getCaption(par1Str);
      if ((caption!=null) && (caption!=lastCaption)) {
//System.out.println("caption="+caption);	  
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
