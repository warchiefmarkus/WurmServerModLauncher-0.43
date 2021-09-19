/*    */ package org.seamless.util.time;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public class DateFormat
/*    */   implements Serializable
/*    */ {
/*    */   protected String label;
/*    */   protected String pattern;
/*    */   
/*    */   public enum Preset
/*    */   {
/* 27 */     DD_MM_YYYY_DOT((String)new DateFormat("31.12.2010", "dd.MM.yyyy")),
/* 28 */     MM_DD_YYYY_DOT((String)new DateFormat("12.31.2010", "MM.dd.yyyy")),
/* 29 */     YYYY_MM_DD_DOT((String)new DateFormat("2010.12.31", "yyyy.MM.dd")),
/* 30 */     YYYY_DD_MM_DOT((String)new DateFormat("2010.31.12", "yyyy.dd.MM")),
/* 31 */     DD_MM_YYYY_SLASH((String)new DateFormat("31/12/2010", "dd/MM/yyyy")),
/* 32 */     MM_DD_YYYY_SLASH((String)new DateFormat("12/31/2010", "MM/dd/yyyy")),
/* 33 */     YYYY_MM_DD_SLASH((String)new DateFormat("2010/12/31", "yyyy/MM/dd")),
/* 34 */     YYYY_DD_MM_SLASH((String)new DateFormat("2010/31/12", "yyyy/dd/MM")),
/* 35 */     YYYY_MMM_DD((String)new DateFormat("2010 Dec 31", "yyyy MMM dd")),
/* 36 */     DD_MMM_YYYY((String)new DateFormat("31 Dec 2010", "dd MMM yyyy")),
/* 37 */     MMM_DD_YYYY((String)new DateFormat("Dec 31 2010", "MMM dd yyyy"));
/*    */     
/*    */     protected DateFormat dateFormat;
/*    */     
/*    */     Preset(DateFormat dateFormat) {
/* 42 */       this.dateFormat = dateFormat;
/*    */     }
/*    */     
/*    */     public DateFormat getDateFormat() {
/* 46 */       return this.dateFormat;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public DateFormat() {}
/*    */ 
/*    */ 
/*    */   
/*    */   DateFormat(String label, String pattern) {
/* 57 */     this.label = label;
/* 58 */     this.pattern = pattern;
/*    */   }
/*    */   
/*    */   public DateFormat(String pattern) {
/* 62 */     this.label = pattern;
/* 63 */     this.pattern = pattern;
/*    */   }
/*    */   
/*    */   public String getLabel() {
/* 67 */     return this.label;
/*    */   }
/*    */   
/*    */   public String getPattern() {
/* 71 */     return this.pattern;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 76 */     if (this == o) return true; 
/* 77 */     if (o == null || getClass() != o.getClass()) return false;
/*    */     
/* 79 */     DateFormat that = (DateFormat)o;
/*    */     
/* 81 */     if ((this.pattern != null) ? !this.pattern.equals(that.pattern) : (that.pattern != null)) return false;
/*    */     
/* 83 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 88 */     return (this.pattern != null) ? this.pattern.hashCode() : 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 93 */     return getLabel() + ", Pattern: " + getPattern();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\time\DateFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */