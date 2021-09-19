/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema.parser;
/*    */ 
/*    */ import java.util.StringTokenizer;
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
/*    */ public class VersionNumber
/*    */   implements Comparable
/*    */ {
/*    */   private final int[] digits;
/*    */   
/*    */   public VersionNumber(String num) {
/* 28 */     StringTokenizer tokens = new StringTokenizer(num, ".");
/* 29 */     this.digits = new int[tokens.countTokens()];
/* 30 */     if (this.digits.length < 2) {
/* 31 */       throw new IllegalArgumentException();
/*    */     }
/* 33 */     int i = 0;
/* 34 */     while (tokens.hasMoreTokens())
/* 35 */       this.digits[i++] = Integer.parseInt(tokens.nextToken()); 
/*    */   }
/*    */   
/*    */   public String toString() {
/* 39 */     StringBuffer buf = new StringBuffer();
/* 40 */     for (int i = 0; i < this.digits.length; i++) {
/* 41 */       if (i != 0) buf.append('.'); 
/* 42 */       buf.append(Integer.toString(this.digits[i]));
/*    */     } 
/* 44 */     return buf.toString();
/*    */   }
/*    */   
/*    */   public boolean isOlderThan(com.sun.tools.xjc.reader.xmlschema.parser.VersionNumber rhs) {
/* 48 */     return (compareTo(rhs) < 0);
/*    */   }
/*    */   
/*    */   public boolean isNewerThan(com.sun.tools.xjc.reader.xmlschema.parser.VersionNumber rhs) {
/* 52 */     return (compareTo(rhs) > 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 57 */     return (compareTo(o) == 0);
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 61 */     int x = 0;
/* 62 */     for (int i = 0; i < this.digits.length; i++) {
/* 63 */       x = x << 1 | this.digits[i];
/*    */     }
/* 65 */     return x;
/*    */   }
/*    */   
/*    */   public int compareTo(Object o) {
/* 69 */     com.sun.tools.xjc.reader.xmlschema.parser.VersionNumber rhs = (com.sun.tools.xjc.reader.xmlschema.parser.VersionNumber)o;
/*    */     
/* 71 */     for (int i = 0;; i++) {
/* 72 */       if (i == this.digits.length && i == rhs.digits.length)
/* 73 */         return 0; 
/* 74 */       if (i == this.digits.length)
/* 75 */         return -1; 
/* 76 */       if (i == rhs.digits.length) {
/* 77 */         return 1;
/*    */       }
/* 79 */       int r = this.digits[i] - rhs.digits[i];
/* 80 */       if (r != 0) return r; 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\parser\VersionNumber.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */