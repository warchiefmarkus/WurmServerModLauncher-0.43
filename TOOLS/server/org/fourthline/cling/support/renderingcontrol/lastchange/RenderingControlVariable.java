/*     */ package org.fourthline.cling.support.renderingcontrol.lastchange;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.fourthline.cling.model.types.UnsignedIntegerTwoBytes;
/*     */ import org.fourthline.cling.support.lastchange.EventedValue;
/*     */ import org.fourthline.cling.support.lastchange.EventedValueShort;
/*     */ import org.fourthline.cling.support.lastchange.EventedValueString;
/*     */ import org.fourthline.cling.support.lastchange.EventedValueUnsignedIntegerTwoBytes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RenderingControlVariable
/*     */ {
/*  33 */   public static Set<Class<? extends EventedValue>> ALL = new HashSet<Class<? extends EventedValue>>()
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class PresetNameList
/*     */     extends EventedValueString
/*     */   {
/*     */     public PresetNameList(String s) {
/*  55 */       super(s);
/*     */     }
/*     */     
/*     */     public PresetNameList(Map.Entry<String, String>[] attributes) {
/*  59 */       super((Map.Entry[])attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Brightness extends EventedValueUnsignedIntegerTwoBytes {
/*     */     public Brightness(UnsignedIntegerTwoBytes value) {
/*  65 */       super(value);
/*     */     }
/*     */     
/*     */     public Brightness(Map.Entry<String, String>[] attributes) {
/*  69 */       super((Map.Entry[])attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Contrast extends EventedValueUnsignedIntegerTwoBytes {
/*     */     public Contrast(UnsignedIntegerTwoBytes value) {
/*  75 */       super(value);
/*     */     }
/*     */     
/*     */     public Contrast(Map.Entry<String, String>[] attributes) {
/*  79 */       super((Map.Entry[])attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Sharpness extends EventedValueUnsignedIntegerTwoBytes {
/*     */     public Sharpness(UnsignedIntegerTwoBytes value) {
/*  85 */       super(value);
/*     */     }
/*     */     
/*     */     public Sharpness(Map.Entry<String, String>[] attributes) {
/*  89 */       super((Map.Entry[])attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class RedVideoGain extends EventedValueUnsignedIntegerTwoBytes {
/*     */     public RedVideoGain(UnsignedIntegerTwoBytes value) {
/*  95 */       super(value);
/*     */     }
/*     */     
/*     */     public RedVideoGain(Map.Entry<String, String>[] attributes) {
/*  99 */       super((Map.Entry[])attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class BlueVideoGain extends EventedValueUnsignedIntegerTwoBytes {
/*     */     public BlueVideoGain(UnsignedIntegerTwoBytes value) {
/* 105 */       super(value);
/*     */     }
/*     */     
/*     */     public BlueVideoGain(Map.Entry<String, String>[] attributes) {
/* 109 */       super((Map.Entry[])attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class GreenVideoGain extends EventedValueUnsignedIntegerTwoBytes {
/*     */     public GreenVideoGain(UnsignedIntegerTwoBytes value) {
/* 115 */       super(value);
/*     */     }
/*     */     
/*     */     public GreenVideoGain(Map.Entry<String, String>[] attributes) {
/* 119 */       super((Map.Entry[])attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class RedVideoBlackLevel extends EventedValueUnsignedIntegerTwoBytes {
/*     */     public RedVideoBlackLevel(UnsignedIntegerTwoBytes value) {
/* 125 */       super(value);
/*     */     }
/*     */     
/*     */     public RedVideoBlackLevel(Map.Entry<String, String>[] attributes) {
/* 129 */       super((Map.Entry[])attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class BlueVideoBlackLevel extends EventedValueUnsignedIntegerTwoBytes {
/*     */     public BlueVideoBlackLevel(UnsignedIntegerTwoBytes value) {
/* 135 */       super(value);
/*     */     }
/*     */     
/*     */     public BlueVideoBlackLevel(Map.Entry<String, String>[] attributes) {
/* 139 */       super((Map.Entry[])attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class GreenVideoBlackLevel extends EventedValueUnsignedIntegerTwoBytes {
/*     */     public GreenVideoBlackLevel(UnsignedIntegerTwoBytes value) {
/* 145 */       super(value);
/*     */     }
/*     */     
/*     */     public GreenVideoBlackLevel(Map.Entry<String, String>[] attributes) {
/* 149 */       super((Map.Entry[])attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ColorTemperature extends EventedValueUnsignedIntegerTwoBytes {
/*     */     public ColorTemperature(UnsignedIntegerTwoBytes value) {
/* 155 */       super(value);
/*     */     }
/*     */     
/*     */     public ColorTemperature(Map.Entry<String, String>[] attributes) {
/* 159 */       super((Map.Entry[])attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class HorizontalKeystone extends EventedValueShort {
/*     */     public HorizontalKeystone(Short value) {
/* 165 */       super(value);
/*     */     }
/*     */     
/*     */     public HorizontalKeystone(Map.Entry<String, String>[] attributes) {
/* 169 */       super((Map.Entry[])attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class VerticalKeystone extends EventedValueShort {
/*     */     public VerticalKeystone(Short value) {
/* 175 */       super(value);
/*     */     }
/*     */     
/*     */     public VerticalKeystone(Map.Entry<String, String>[] attributes) {
/* 179 */       super((Map.Entry[])attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Mute extends EventedValueChannelMute {
/*     */     public Mute(ChannelMute value) {
/* 185 */       super(value);
/*     */     }
/*     */     
/*     */     public Mute(Map.Entry<String, String>[] attributes) {
/* 189 */       super(attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class VolumeDB extends EventedValueChannelVolumeDB {
/*     */     public VolumeDB(ChannelVolumeDB value) {
/* 195 */       super(value);
/*     */     }
/*     */     
/*     */     public VolumeDB(Map.Entry<String, String>[] attributes) {
/* 199 */       super(attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Volume extends EventedValueChannelVolume {
/*     */     public Volume(ChannelVolume value) {
/* 205 */       super(value);
/*     */     }
/*     */     
/*     */     public Volume(Map.Entry<String, String>[] attributes) {
/* 209 */       super(attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Loudness extends EventedValueChannelLoudness {
/*     */     public Loudness(ChannelLoudness value) {
/* 215 */       super(value);
/*     */     }
/*     */     
/*     */     public Loudness(Map.Entry<String, String>[] attributes) {
/* 219 */       super(attributes);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\renderingcontrol\lastchange\RenderingControlVariable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */