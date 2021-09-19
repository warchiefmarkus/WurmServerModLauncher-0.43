/*     */ package org.fourthline.cling.model.types;
/*     */ 
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class DLNADoc
/*     */ {
/*     */   private final String devClass;
/*     */   private final String version;
/*  32 */   public static final Pattern PATTERN = Pattern.compile("(.+?)[ -]([0-9].[0-9]{2})");
/*     */   
/*     */   public enum Version {
/*  35 */     V1_0("1.00"),
/*  36 */     V1_5("1.50");
/*     */     
/*     */     String s;
/*     */     
/*     */     Version(String s) {
/*  41 */       this.s = s;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/*  47 */       return this.s;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DLNADoc(String devClass, String version) {
/*  55 */     this.devClass = devClass;
/*  56 */     this.version = version;
/*     */   }
/*     */   
/*     */   public DLNADoc(String devClass, Version version) {
/*  60 */     this.devClass = devClass;
/*  61 */     this.version = version.s;
/*     */   }
/*     */   
/*     */   public String getDevClass() {
/*  65 */     return this.devClass;
/*     */   }
/*     */   
/*     */   public String getVersion() {
/*  69 */     return this.version;
/*     */   }
/*     */   
/*     */   public static DLNADoc valueOf(String s) throws InvalidValueException {
/*  73 */     Matcher matcher = PATTERN.matcher(s);
/*  74 */     if (matcher.matches()) {
/*  75 */       return new DLNADoc(matcher.group(1), matcher.group(2));
/*     */     }
/*  77 */     throw new InvalidValueException("Can't parse DLNADoc: " + s);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  83 */     if (this == o) return true; 
/*  84 */     if (o == null || getClass() != o.getClass()) return false;
/*     */     
/*  86 */     DLNADoc dlnaDoc = (DLNADoc)o;
/*     */     
/*  88 */     if (!this.devClass.equals(dlnaDoc.devClass)) return false; 
/*  89 */     if (!this.version.equals(dlnaDoc.version)) return false;
/*     */     
/*  91 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  96 */     int result = this.devClass.hashCode();
/*  97 */     result = 31 * result + this.version.hashCode();
/*  98 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 103 */     return getDevClass() + "-" + getVersion();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\DLNADoc.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */