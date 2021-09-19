/*     */ package org.controlsfx.glyphfont;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Optional;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.scene.control.Label;
/*     */ import javafx.scene.paint.Color;
/*     */ import javafx.scene.paint.CycleMethod;
/*     */ import javafx.scene.paint.LinearGradient;
/*     */ import javafx.scene.paint.Paint;
/*     */ import javafx.scene.paint.Stop;
/*     */ import javafx.scene.text.Font;
/*     */ import org.controlsfx.tools.Duplicatable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Glyph
/*     */   extends Label
/*     */   implements Duplicatable<Glyph>
/*     */ {
/*     */   public static final String DEFAULT_CSS_CLASS = "glyph-font";
/*     */   public static final String STYLE_GRADIENT = "gradient";
/*     */   public static final String STYLE_HOVER_EFFECT = "hover-effect";
/*     */   
/*     */   public static Glyph create(String fontAndGlyph) {
/*  82 */     String[] args = fontAndGlyph.split("\\|");
/*  83 */     return new Glyph(args[0], args[1]);
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
/*  97 */   private final ObjectProperty<Object> icon = (ObjectProperty<Object>)new SimpleObjectProperty();
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
/*     */   public Glyph() {
/* 109 */     getStyleClass().add("glyph-font");
/*     */     
/* 111 */     this.icon.addListener(x -> updateIcon());
/* 112 */     fontProperty().addListener(x -> updateIcon());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Glyph(String fontFamily, char unicode) {
/* 121 */     this();
/* 122 */     setFontFamily(fontFamily);
/* 123 */     setTextUnicode(unicode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Glyph(String fontFamily, Object icon) {
/* 133 */     this();
/* 134 */     setFontFamily(fontFamily);
/* 135 */     setIcon(icon);
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
/*     */   public Glyph fontFamily(String fontFamily) {
/* 150 */     setFontFamily(fontFamily);
/* 151 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Glyph color(Color color) {
/* 160 */     setColor(color);
/* 161 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Glyph size(double size) {
/* 170 */     setFontSize(size);
/* 171 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Glyph sizeFactor(int factor) {
/* 180 */     Optional.<GlyphFont>ofNullable(GlyphFontRegistry.font(getFont().getFamily())).ifPresent(glyphFont -> setFontSize(glyphFont.getDefaultSize() * ((factor < 1) ? true : factor)));
/*     */ 
/*     */     
/* 183 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Glyph useHoverEffect() {
/* 193 */     getStyleClass().add("hover-effect");
/* 194 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Glyph useGradientEffect() {
/* 203 */     if (getTextFill() instanceof Color) {
/* 204 */       Color currentColor = (Color)getTextFill();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 211 */       Stop[] stops = { new Stop(0.0D, Color.BLACK), new Stop(1.0D, currentColor) };
/* 212 */       LinearGradient lg1 = new LinearGradient(0.0D, 0.0D, 1.0D, 0.0D, true, CycleMethod.NO_CYCLE, stops);
/* 213 */       setTextFill((Paint)lg1);
/*     */     } 
/*     */     
/* 216 */     getStyleClass().add("gradient");
/* 217 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Glyph duplicate() {
/* 226 */     final Paint color = getTextFill();
/* 227 */     final Object icon = getIcon();
/* 228 */     final ObservableList<String> classes = getStyleClass();
/* 229 */     return (new Glyph()
/*     */       {
/*     */ 
/*     */ 
/*     */       
/* 234 */       }).fontFamily(getFontFamily())
/* 235 */       .size(getFontSize());
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
/*     */   public void setFontFamily(String family) {
/* 249 */     if (!getFont().getFamily().equals(family)) {
/* 250 */       Optional.<GlyphFont>ofNullable(GlyphFontRegistry.font(family)).ifPresent(glyphFont -> {
/*     */             glyphFont.ensureFontIsLoaded();
/*     */             Font newFont = Font.font(family, glyphFont.getDefaultSize());
/*     */             setFont(newFont);
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFontFamily() {
/* 262 */     return getFont().getFamily();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFontSize(double size) {
/* 269 */     Font newFont = Font.font(getFont().getFamily(), size);
/* 270 */     setFont(newFont);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getFontSize() {
/* 277 */     return getFont().getSize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColor(Color color) {
/* 284 */     setTextFill((Paint)color);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectProperty<Object> iconProperty() {
/* 294 */     return this.icon;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIcon(Object iconValue) {
/* 302 */     this.icon.set(iconValue);
/*     */   }
/*     */   
/*     */   public Object getIcon() {
/* 306 */     return this.icon.get();
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
/*     */   private void updateIcon() {
/* 322 */     Object iconValue = getIcon();
/*     */     
/* 324 */     if (iconValue != null) {
/* 325 */       if (iconValue instanceof Character) {
/* 326 */         setTextUnicode(((Character)iconValue).charValue());
/*     */       } else {
/* 328 */         GlyphFont glyphFont = GlyphFontRegistry.font(getFontFamily());
/* 329 */         if (glyphFont != null) {
/* 330 */           String name = iconValue.toString();
/* 331 */           Character unicode = glyphFont.getCharacter(name);
/* 332 */           if (unicode != null) {
/* 333 */             setTextUnicode(unicode.charValue());
/*     */           } else {
/*     */             
/* 336 */             setText(name);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setTextUnicode(char unicode) {
/* 348 */     setText(String.valueOf(unicode));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\glyphfont\Glyph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */