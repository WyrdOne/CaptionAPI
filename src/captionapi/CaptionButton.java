package captionapi;

import net.minecraft.src.*;

public class CaptionButton extends GuiButton {
  public static String buttonText = "Captioning";
  
	public CaptionButton(int par1, int par2, int par3, int par4, int par5, String par6Str) {
		super(par1, par2, par3, par4, par5, par6Str);
    buttonText = par6Str;
    updateButton();
	}

  private void updateButton() {
    displayString = buttonText + ": " + CaptionAPI.getCaption((CaptionAPI.getCaptioning()) ? "button.on" : "button.off");
  }

	@Override
	public boolean mousePressed(Minecraft mc, int par2, int par3) {
    if (enabled && drawButton && par2>=xPosition && par3>=yPosition && par2<xPosition+width && par3<yPosition+height) {
      CaptionAPI.setCaptioning(!CaptionAPI.getCaptioning());
      mod_CaptionAPI.saveOptions();
      updateButton();
     	return true;
    }
    return false;
  }
}
