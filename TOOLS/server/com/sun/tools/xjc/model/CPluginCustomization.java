/*    */ package com.sun.tools.xjc.model;
/*    */ 
/*    */ import org.w3c.dom.Element;
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
/*    */ public class CPluginCustomization
/*    */ {
/*    */   public final Element element;
/*    */   public final Locator locator;
/*    */   private boolean acknowledged;
/*    */   
/*    */   public void markAsAcknowledged() {
/* 80 */     this.acknowledged = true;
/*    */   }
/*    */   
/*    */   public CPluginCustomization(Element element, Locator locator) {
/* 84 */     this.element = element;
/* 85 */     this.locator = locator;
/*    */   }
/*    */   
/*    */   public boolean isAcknowledged() {
/* 89 */     return this.acknowledged;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\CPluginCustomization.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */