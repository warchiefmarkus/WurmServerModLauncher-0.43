/*     */ package impl.org.controlsfx.i18n;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
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
/*     */ public class Localization
/*     */ {
/*     */   public static final String KEY_PREFIX = "@@";
/*     */   private static final String LOCALE_BUNDLE_NAME = "controlsfx";
/*  41 */   private static Locale locale = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Locale getLocale() {
/*  50 */     return (locale == null) ? Locale.getDefault() : locale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void setLocale(Locale newLocale) {
/*  60 */     locale = newLocale;
/*     */   }
/*     */   
/*  63 */   private static Locale resourceBundleLocale = null;
/*  64 */   private static ResourceBundle resourceBundle = null;
/*     */ 
/*     */   
/*     */   private static final synchronized ResourceBundle getLocaleBundle() {
/*  68 */     Locale currentLocale = getLocale();
/*  69 */     if (!currentLocale.equals(resourceBundleLocale)) {
/*  70 */       resourceBundleLocale = currentLocale;
/*  71 */       resourceBundle = ResourceBundle.getBundle("controlsfx", resourceBundleLocale, Localization.class
/*  72 */           .getClassLoader());
/*     */     } 
/*  74 */     return resourceBundle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getString(String key) {
/*     */     try {
/*  86 */       return getLocaleBundle().getString(key);
/*  87 */     } catch (MissingResourceException ex) {
/*  88 */       return String.format("<%s>", new Object[] { key });
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
/*     */   public static final String asKey(String text) {
/* 100 */     return "@@" + text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean isKey(String text) {
/* 110 */     return (text != null && text.startsWith("@@"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String localize(String text) {
/* 121 */     return isKey(text) ? getString(text.substring("@@".length())
/* 122 */         .trim()) : text;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\i18n\Localization.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */