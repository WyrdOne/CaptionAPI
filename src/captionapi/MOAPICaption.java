package captionapi;

import net.minecraft.src.*;
import moapi.*;

public class MOAPICaption extends ModOptionCallback {
	private static MOAPICaption instance = new MOAPICaption();
	private static ModOptions captionOptions;
	
	public static void setup() {
		captionOptions = ModOptionsAPI.addMod("captionapi", "Caption API").setClientMode();
		ModOptionBoolean option = (ModOptionBoolean)captionOptions.addBooleanOption("Captioning", CaptionAPI.getCaption("button.text"), CaptionAPI.getCaptioning()).setCallback(instance);
		option.setLabels(CaptionAPI.getCaption("button.on"), CaptionAPI.getCaption("button.off"));
	}
	
	public void onChange(ModOption option) {
		CaptionAPI.setCaptioning(((ModOptionBoolean)option).getValue());
		mod_CaptionAPI.saveOptions();
	}
}
