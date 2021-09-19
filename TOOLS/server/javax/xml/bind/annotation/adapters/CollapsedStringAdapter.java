/*    */ package javax.xml.bind.annotation.adapters;
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
/*    */ public class CollapsedStringAdapter
/*    */   extends XmlAdapter<String, String>
/*    */ {
/*    */   public String unmarshal(String text) {
/* 27 */     if (text == null) return null;
/*    */     
/* 29 */     int len = text.length();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 34 */     int s = 0;
/* 35 */     while (s < len && 
/* 36 */       !isWhiteSpace(text.charAt(s)))
/*    */     {
/* 38 */       s++;
/*    */     }
/* 40 */     if (s == len)
/*    */     {
/* 42 */       return text;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 47 */     StringBuffer result = new StringBuffer(len);
/*    */     
/* 49 */     if (s != 0) {
/* 50 */       for (int j = 0; j < s; j++)
/* 51 */         result.append(text.charAt(j)); 
/* 52 */       result.append(' ');
/*    */     } 
/*    */     
/* 55 */     boolean inStripMode = true;
/* 56 */     for (int i = s + 1; i < len; i++) {
/* 57 */       char ch = text.charAt(i);
/* 58 */       boolean b = isWhiteSpace(ch);
/* 59 */       if (!inStripMode || !b) {
/*    */ 
/*    */         
/* 62 */         inStripMode = b;
/* 63 */         if (inStripMode) {
/* 64 */           result.append(' ');
/*    */         } else {
/* 66 */           result.append(ch);
/*    */         } 
/*    */       } 
/*    */     } 
/* 70 */     len = result.length();
/* 71 */     if (len > 0 && result.charAt(len - 1) == ' ') {
/* 72 */       result.setLength(len - 1);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 77 */     return result.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String marshal(String s) {
/* 86 */     return s;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected static boolean isWhiteSpace(char ch) {
/* 94 */     if (ch > ' ') return false;
/*    */ 
/*    */     
/* 97 */     return (ch == '\t' || ch == '\n' || ch == '\r' || ch == ' ');
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\annotation\adapters\CollapsedStringAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */