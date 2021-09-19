/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*    */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIDeclaration;
/*    */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo;
/*    */ import com.sun.xml.bind.JAXBAssertionError;
/*    */ import com.sun.xml.xsom.XSComponent;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class AbstractDeclarationImpl
/*    */   implements BIDeclaration
/*    */ {
/*    */   private final Locator loc;
/*    */   protected BindInfo parent;
/*    */   private boolean isAcknowledged;
/*    */   
/*    */   public Locator getLocation() {
/*    */     return this.loc;
/*    */   }
/*    */   
/*    */   public void setParent(BindInfo p) {
/*    */     this.parent = p;
/*    */   }
/*    */   
/*    */   protected final XSComponent getOwner() {
/*    */     return this.parent.getOwner();
/*    */   }
/*    */   
/*    */   protected final BGMBuilder getBuilder() {
/*    */     return this.parent.getBuilder();
/*    */   }
/*    */   
/*    */   protected AbstractDeclarationImpl(Locator _loc) {
/* 38 */     this.isAcknowledged = false;
/*    */     this.loc = _loc; } public final boolean isAcknowledged() {
/* 40 */     return this.isAcknowledged;
/*    */   } public void markAsAcknowledged() {
/* 42 */     this.isAcknowledged = true;
/*    */   }
/*    */   
/*    */   protected static final void _assert(boolean b) {
/* 46 */     if (!b)
/* 47 */       throw new JAXBAssertionError(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\AbstractDeclarationImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */