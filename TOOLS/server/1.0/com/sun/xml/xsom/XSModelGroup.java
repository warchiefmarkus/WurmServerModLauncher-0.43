/*    */ package 1.0.com.sun.xml.xsom;
/*    */ 
/*    */ import com.sun.xml.xsom.XSComponent;
/*    */ import com.sun.xml.xsom.XSParticle;
/*    */ import com.sun.xml.xsom.XSTerm;
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
/*    */ 
/*    */ 
/*    */ public interface XSModelGroup
/*    */   extends XSComponent, XSTerm
/*    */ {
/* 42 */   public static final Compositor ALL = new Compositor("all", null);
/*    */ 
/*    */ 
/*    */   
/* 46 */   public static final Compositor SEQUENCE = new Compositor("sequence", null);
/*    */ 
/*    */ 
/*    */   
/* 50 */   public static final Compositor CHOICE = new Compositor("choice", null);
/*    */   
/*    */   Compositor getCompositor();
/*    */   
/*    */   XSParticle getChild(int paramInt);
/*    */   
/*    */   int getSize();
/*    */   
/*    */   XSParticle[] getChildren();
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\XSModelGroup.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */