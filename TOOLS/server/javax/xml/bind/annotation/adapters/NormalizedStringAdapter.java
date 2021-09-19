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
/*    */ public final class NormalizedStringAdapter
/*    */   extends XmlAdapter<String, String>
/*    */ {
/*    */   public String unmarshal(String text) {
/* 26 */     if (text == null) return null;
/*    */     
/* 28 */     int i = text.length() - 1;
/*    */ 
/*    */     
/* 31 */     while (i >= 0 && !isWhiteSpaceExceptSpace(text.charAt(i))) {
/* 32 */       i--;
/*    */     }
/* 34 */     if (i < 0)
/*    */     {
/* 36 */       return text;
/*    */     }
/*    */ 
/*    */     
/* 40 */     char[] buf = text.toCharArray();
/*    */     
/* 42 */     buf[i--] = ' ';
/* 43 */     for (; i >= 0; i--) {
/* 44 */       if (isWhiteSpaceExceptSpace(buf[i]))
/* 45 */         buf[i] = ' '; 
/*    */     } 
/* 47 */     return new String(buf);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String marshal(String s) {
/* 56 */     return s;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected static boolean isWhiteSpaceExceptSpace(char ch) {
/* 67 */     if (ch >= ' ') return false;
/*    */ 
/*    */     
/* 70 */     return (ch == '\t' || ch == '\n' || ch == '\r');
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\annotation\adapters\NormalizedStringAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */