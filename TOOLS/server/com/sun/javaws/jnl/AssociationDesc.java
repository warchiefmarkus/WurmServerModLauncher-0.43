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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AssociationDesc
/*    */   implements XMLable
/*    */ {
/*    */   private String _extensions;
/*    */   private String _mimeType;
/*    */   
/*    */   public AssociationDesc(String paramString1, String paramString2) {
/* 22 */     this._extensions = paramString1;
/* 23 */     this._mimeType = paramString2;
/*    */   }
/*    */   
/*    */   public String getExtensions() {
/* 27 */     return this._extensions; } public String getMimeType() {
/* 28 */     return this._mimeType;
/*    */   }
/*    */   
/*    */   public XMLNode asXML() {
/* 32 */     XMLAttributeBuilder xMLAttributeBuilder = new XMLAttributeBuilder();
/* 33 */     xMLAttributeBuilder.add("extensions", this._extensions);
/* 34 */     xMLAttributeBuilder.add("mime-type", this._mimeType);
/* 35 */     XMLNodeBuilder xMLNodeBuilder = new XMLNodeBuilder("association", xMLAttributeBuilder.getAttributeList());
/*    */     
/* 37 */     return xMLNodeBuilder.getNode();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\jnl\AssociationDesc.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */