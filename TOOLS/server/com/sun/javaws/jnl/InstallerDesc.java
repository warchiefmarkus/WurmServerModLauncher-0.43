/*    */ package com.sun.javaws.jnl;
/*    */ 
/*    */ import com.sun.deploy.xml.XMLAttributeBuilder;
/*    */ import com.sun.deploy.xml.XMLNode;
/*    */ import com.sun.deploy.xml.XMLNodeBuilder;
/*    */ import com.sun.deploy.xml.XMLable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InstallerDesc
/*    */   implements XMLable
/*    */ {
/*    */   private String _mainClass;
/*    */   
/*    */   public InstallerDesc(String paramString) {
/* 17 */     this._mainClass = paramString;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMainClass() {
/* 23 */     return this._mainClass;
/*    */   }
/*    */   
/*    */   public XMLNode asXML() {
/* 27 */     XMLAttributeBuilder xMLAttributeBuilder = new XMLAttributeBuilder();
/* 28 */     xMLAttributeBuilder.add("main-class", this._mainClass);
/* 29 */     XMLNodeBuilder xMLNodeBuilder = new XMLNodeBuilder("installer-desc", xMLAttributeBuilder.getAttributeList());
/* 30 */     return xMLNodeBuilder.getNode();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\jnl\InstallerDesc.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */