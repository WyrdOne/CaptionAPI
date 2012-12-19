package net.minecraft.src;

import java.io.*;
import java.net.*;
import java.util.*;

public class CaptionAPI {
  private static CaptionAPI instance = new CaptionAPI(StringTranslate.getInstance().getCurrentLanguage());
  private static boolean captioning = false;
  private static Properties captions;
  private static String currentLanguage;

  private CaptionAPI(String lang) {
    if (!setLanguage(lang)) {
      setLanguage("en_US"); // Default to english if language not found
    }
  }

  public boolean setLanguage(String lang) {
    if (lang==currentLanguage) {
      return true;
    }
    try {
      BufferedReader captionFile = new BufferedReader(new InputStreamReader(CaptionAPI.class.getResourceAsStream("/captions/" + lang + ".captions"), "UTF-8"));
      captions = new Properties(); // After file in case invalid language is set
      for (String line=captionFile.readLine(); line!=null; line=captionFile.readLine()) {
        line = line.trim();
        if (!line.startsWith("#")) {
          String[] tokens = line.split("=");
          if (tokens!=null && tokens.length == 2) {
            captions.setProperty(tokens[0], tokens[1]);
          }
        }
      }
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  public static boolean getCaptioning() {
    return captioning;
  }

  public static void setCaptioning(boolean value) {
    captioning = value;
  }

  public static void addCaption(String key, String output) {
    captions.setProperty(key, output);
  }
  
  public static String getCaption(String key) {
    return captions.getProperty(key);
  } 

  public static CaptionAPI getInstance() {
    return instance;
  }
}