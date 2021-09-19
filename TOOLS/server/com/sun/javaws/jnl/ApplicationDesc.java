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
/*    */ public class ApplicationDesc
/*    */   implements XMLable
/*    */ {
/*    */   private String _mainClass;
/*    */   private String[] _arguments;
/*    */   
/*    */   public ApplicationDesc(String paramString, String[] paramArrayOfString) {
/* 19 */     this._mainClass = paramString;
/* 20 */     this._arguments = paramArrayOfString;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMainClass() {
/* 26 */     return this._mainClass;
/*    */   }
/*    */   public String[] getArguments() {
/* 29 */     return this._arguments;
/*    */   }
/*    */   public void setArguments(String[] paramArrayOfString) {
/* 32 */     this._arguments = paramArrayOfString;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public XMLNode asXML() {
/* 38 */     XMLAttributeBuilder xMLAttributeBuilder = new XMLAttributeBuilder();
/* 39 */     xMLAttributeBuilder.add("main-class", this._mainClass);
/* 40 */     XMLNodeBuilder xMLNodeBuilder = new XMLNodeBuilder("application-desc", xMLAttributeBuilder.getAttributeList());
/* 41 */     if (this._arguments != null) {
/* 42 */       for (byte b = 0; b < this._arguments.length; b++) {
/* 43 */         xMLNodeBuilder.add(new XMLNode("argument", null, new XMLNode(this._arguments[b]), null));
/*    */       }
/*    */     }
/* 46 */     return xMLNodeBuilder.getNode();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\jnl\ApplicationDesc.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */