/*    */ package com.sun.javaws.jnl;
/*    */ 
/*    */ import com.sun.deploy.xml.XMLAttributeBuilder;
/*    */ import com.sun.deploy.xml.XMLNode;
/*    */ import com.sun.deploy.xml.XMLable;
/*    */ import com.sun.javaws.Globals;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExtDownloadDesc
/*    */   implements XMLable
/*    */ {
/*    */   private String _extensionPart;
/*    */   private String _part;
/*    */   private boolean _isLazy;
/*    */   
/*    */   public ExtDownloadDesc(String paramString1, String paramString2, boolean paramBoolean) {
/* 20 */     this._extensionPart = paramString1;
/* 21 */     this._part = paramString2;
/* 22 */     this._isLazy = (paramBoolean && !Globals.isImportMode());
/*    */   }
/*    */   
/* 25 */   public String getExtensionPart() { return this._extensionPart; }
/* 26 */   public String getPart() { return this._part; } public boolean isLazy() {
/* 27 */     return this._isLazy;
/*    */   }
/*    */   
/*    */   public XMLNode asXML() {
/* 31 */     XMLAttributeBuilder xMLAttributeBuilder = new XMLAttributeBuilder();
/* 32 */     xMLAttributeBuilder.add("ext-part", this._extensionPart);
/* 33 */     xMLAttributeBuilder.add("part", this._part);
/* 34 */     xMLAttributeBuilder.add("download", this._isLazy ? "lazy" : "eager");
/* 35 */     return new XMLNode("ext-download", xMLAttributeBuilder.getAttributeList());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\jnl\ExtDownloadDesc.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */