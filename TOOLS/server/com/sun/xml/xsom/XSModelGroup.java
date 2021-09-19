/*    */ package com.sun.xml.xsom;
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
/*    */   extends XSComponent, XSTerm, Iterable<XSParticle>
/*    */ {
/*    */   public enum Compositor
/*    */   {
/* 35 */     ALL("all"), CHOICE("choice"), SEQUENCE("sequence"); private final String value;
/*    */     
/*    */     Compositor(String _value) {
/* 38 */       this.value = _value;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public String toString() {
/* 49 */       return this.value;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 55 */   public static final Compositor ALL = Compositor.ALL;
/*    */ 
/*    */ 
/*    */   
/* 59 */   public static final Compositor SEQUENCE = Compositor.SEQUENCE;
/*    */ 
/*    */ 
/*    */   
/* 63 */   public static final Compositor CHOICE = Compositor.CHOICE;
/*    */   
/*    */   Compositor getCompositor();
/*    */   
/*    */   XSParticle getChild(int paramInt);
/*    */   
/*    */   int getSize();
/*    */   
/*    */   XSParticle[] getChildren();
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\XSModelGroup.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */