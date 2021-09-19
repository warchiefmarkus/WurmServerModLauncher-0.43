/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema;
/*    */ 
/*    */ import com.sun.tools.xjc.reader.xmlschema.Messages;
/*    */ import org.xml.sax.Locator;
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
/*    */ final class CollisionInfo
/*    */ {
/*    */   private final String name;
/*    */   private final Locator source1;
/*    */   private final Locator source2;
/*    */   
/*    */   public CollisionInfo(String name, Locator source1, Locator source2) {
/* 25 */     this.name = name;
/* 26 */     this.source1 = source1;
/* 27 */     this.source2 = source2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 34 */     return Messages.format("CollisionInfo.CollisionInfo", this.name, printLocator(this.source1), printLocator(this.source2));
/*    */   }
/*    */ 
/*    */   
/*    */   private String printLocator(Locator loc) {
/* 39 */     if (loc == null) return "";
/*    */     
/* 41 */     int line = loc.getLineNumber();
/* 42 */     String sysId = loc.getSystemId();
/* 43 */     if (sysId == null) sysId = Messages.format("CollisionInfo.UnknownFile");
/*    */     
/* 45 */     if (line != -1) {
/* 46 */       return Messages.format("CollisionInfo.LineXOfY", Integer.toString(line), sysId);
/*    */     }
/*    */     
/* 49 */     return sysId;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\CollisionInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */