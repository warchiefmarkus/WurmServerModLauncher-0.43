/*    */ package 1.0.com.sun.xml.xsom;
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
/*    */ public final class XSVariety
/*    */ {
/* 19 */   public static final com.sun.xml.xsom.XSVariety ATOMIC = new com.sun.xml.xsom.XSVariety("atomic");
/* 20 */   public static final com.sun.xml.xsom.XSVariety UNION = new com.sun.xml.xsom.XSVariety("union"); private final String name;
/* 21 */   public static final com.sun.xml.xsom.XSVariety LIST = new com.sun.xml.xsom.XSVariety("list");
/*    */   private XSVariety(String _name) {
/* 23 */     this.name = _name;
/*    */   } public String toString() {
/* 25 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\XSVariety.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */