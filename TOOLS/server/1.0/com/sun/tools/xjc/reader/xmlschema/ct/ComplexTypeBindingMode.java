/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema.ct;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class ComplexTypeBindingMode
/*    */ {
/*    */   private final String name;
/*    */   
/*    */   private ComplexTypeBindingMode(String name) {
/* 14 */     this.name = name;
/*    */   }
/*    */   public String toString() {
/* 17 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 22 */   static final com.sun.tools.xjc.reader.xmlschema.ct.ComplexTypeBindingMode NORMAL = new com.sun.tools.xjc.reader.xmlschema.ct.ComplexTypeBindingMode("normal");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   static final com.sun.tools.xjc.reader.xmlschema.ct.ComplexTypeBindingMode FALLBACK_CONTENT = new com.sun.tools.xjc.reader.xmlschema.ct.ComplexTypeBindingMode("fallback(content)");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   static final com.sun.tools.xjc.reader.xmlschema.ct.ComplexTypeBindingMode FALLBACK_REST = new com.sun.tools.xjc.reader.xmlschema.ct.ComplexTypeBindingMode("fallback(rest)");
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\ct\ComplexTypeBindingMode.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */