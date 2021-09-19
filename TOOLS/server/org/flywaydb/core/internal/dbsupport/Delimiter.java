/*    */ package org.flywaydb.core.internal.dbsupport;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Delimiter
/*    */ {
/*    */   private final String delimiter;
/*    */   private final boolean aloneOnLine;
/*    */   
/*    */   public Delimiter(String delimiter, boolean aloneOnLine) {
/* 39 */     this.delimiter = delimiter;
/* 40 */     this.aloneOnLine = aloneOnLine;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getDelimiter() {
/* 47 */     return this.delimiter;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isAloneOnLine() {
/* 54 */     return this.aloneOnLine;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 59 */     if (this == o) return true; 
/* 60 */     if (o == null || getClass() != o.getClass()) return false;
/*    */     
/* 62 */     Delimiter delimiter1 = (Delimiter)o;
/*    */     
/* 64 */     return (this.aloneOnLine == delimiter1.aloneOnLine && this.delimiter.equals(delimiter1.delimiter));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 69 */     int result = this.delimiter.hashCode();
/* 70 */     result = 31 * result + (this.aloneOnLine ? 1 : 0);
/* 71 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\Delimiter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */