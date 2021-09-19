/*     */ package org.controlsfx.glyphfont;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.ServiceLoader;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class GlyphFontRegistry
/*     */ {
/*  56 */   private static Map<String, GlyphFont> fontMap = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  66 */     ServiceLoader<GlyphFont> loader = ServiceLoader.load(GlyphFont.class);
/*  67 */     for (GlyphFont font : loader) {
/*  68 */       register(font);
/*     */     }
/*     */   }
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
/*     */   public static void register(String familyName, String uri, int defaultSize) {
/*  92 */     register(new GlyphFont(familyName, defaultSize, uri));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void register(String familyName, InputStream in, int defaultSize) {
/* 102 */     register(new GlyphFont(familyName, defaultSize, in));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void register(GlyphFont font) {
/* 110 */     if (font != null) {
/* 111 */       fontMap.put(font.getName(), font);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static GlyphFont font(String familyName) {
/* 121 */     GlyphFont font = fontMap.get(familyName);
/* 122 */     if (font != null) {
/* 123 */       font.ensureFontIsLoaded();
/*     */     }
/* 125 */     return font;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\glyphfont\GlyphFontRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */