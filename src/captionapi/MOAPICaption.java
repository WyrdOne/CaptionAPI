package captionapi;

import net.minecraft.src.*;
import moapi.*;

public class MOAPICaption extends ModOptionCallback {
	private static MOAPICaption instance = new MOAPICaption();
	private static ModOptions captionOptions;
	
	public static void setup() {
		captionOptions = ModOptionsAPI.addMod("captionapi", "Caption API").setClientMode();
		captionOptions.addBooleanOption("Captioning").setValue(CaptionAPI.getCaptioning()).setCallback(instance);
	}
	
	public void onChange(ModOption option) {
		CaptionAPI.setCaptioning(((ModOptionBoolean)option).getValue());
		mod_CaptionAPI.saveOptions();
	}
}
