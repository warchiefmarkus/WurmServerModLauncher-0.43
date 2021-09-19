/*     */ package org.controlsfx.glyphfont;
/*     */ 
/*     */ import com.sun.javafx.css.StyleManager;
/*     */ import java.io.InputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javafx.scene.text.Font;
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
/*     */ public class GlyphFont
/*     */ {
/*     */   static {
/*  57 */     StyleManager.getInstance().addUserAgentStylesheet(GlyphFont.class
/*  58 */         .getResource("glyphfont.css").toExternalForm());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   private final Map<String, Character> namedGlyphs = new HashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private final Runnable fontLoader;
/*     */ 
/*     */ 
/*     */   
/*     */   private final String fontName;
/*     */ 
/*     */ 
/*     */   
/*     */   private final double defaultSize;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean fontLoaded = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public GlyphFont(String fontName, int defaultSize, InputStream in) {
/*  88 */     this(fontName, defaultSize, in, false);
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
/*     */   public GlyphFont(String fontName, int defaultSize, String urlStr) {
/* 102 */     this(fontName, defaultSize, urlStr, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GlyphFont(String fontName, int defaultSize, InputStream in, boolean lazyLoad) {
/* 113 */     this(fontName, defaultSize, () -> Font.loadFont(in, -1.0D), lazyLoad);
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
/*     */   public GlyphFont(String fontName, int defaultSize, String urlStr, boolean lazyLoad) {
/* 130 */     this(fontName, defaultSize, () -> Font.loadFont(urlStr, -1.0D), lazyLoad);
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
/*     */   private GlyphFont(String fontName, int defaultSize, Runnable fontLoader, boolean lazyLoad) {
/* 143 */     this.fontName = fontName;
/* 144 */     this.defaultSize = defaultSize;
/* 145 */     this.fontLoader = fontLoader;
/*     */     
/* 147 */     if (!lazyLoad) {
/* 148 */       ensureFontIsLoaded();
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
/*     */   public String getName() {
/* 163 */     return this.fontName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getDefaultSize() {
/* 171 */     return this.defaultSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Glyph create(char character) {
/* 181 */     return new Glyph(this.fontName, character);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Glyph create(String glyphName) {
/* 190 */     return new Glyph(this.fontName, glyphName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Glyph create(Enum<?> glyph) {
/* 198 */     return new Glyph(this.fontName, glyph);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Character getCharacter(String glyphName) {
/* 207 */     return this.namedGlyphs.get(glyphName.toUpperCase());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerAll(Iterable<? extends INamedCharacter> namedCharacters) {
/* 216 */     for (INamedCharacter e : namedCharacters) {
/* 217 */       register(e.name(), Character.valueOf(e.getChar()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void register(String name, Character character) {
/* 227 */     this.namedGlyphs.put(name.toUpperCase(), character);
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
/*     */   synchronized void ensureFontIsLoaded() {
/* 240 */     if (!this.fontLoaded) {
/* 241 */       this.fontLoader.run();
/* 242 */       this.fontLoaded = true;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\glyphfont\GlyphFont.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */