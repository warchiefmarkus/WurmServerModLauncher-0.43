/*    */ package impl.org.controlsfx.i18n;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import java.util.Locale;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Translation
/*    */   implements Comparable<Translation>
/*    */ {
/*    */   private final String localeString;
/*    */   private final Locale locale;
/*    */   private final Path path;
/*    */   
/*    */   public Translation(String locale, Path path) {
/* 39 */     this.localeString = locale;
/* 40 */     this.path = path;
/*    */     
/* 42 */     String[] split = this.localeString.split("_");
/* 43 */     if (split.length == 1) {
/* 44 */       this.locale = new Locale(this.localeString);
/* 45 */     } else if (split.length == 2) {
/* 46 */       this.locale = new Locale(split[0], split[1]);
/* 47 */     } else if (split.length == 3) {
/* 48 */       this.locale = new Locale(split[0], split[1], split[2]);
/*    */     } else {
/* 50 */       throw new IllegalArgumentException("Unknown locale string '" + locale + "'");
/*    */     } 
/*    */   }
/*    */   
/*    */   public final String getLocaleString() {
/* 55 */     return this.localeString;
/*    */   }
/*    */   
/*    */   public final Locale getLocale() {
/* 59 */     return this.locale;
/*    */   }
/*    */   
/*    */   public final Path getPath() {
/* 63 */     return this.path;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 67 */     return this.localeString;
/*    */   }
/*    */   
/*    */   public int compareTo(Translation o) {
/* 71 */     if (o == null) return 1; 
/* 72 */     return this.localeString.compareTo(o.localeString);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\i18n\Translation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */