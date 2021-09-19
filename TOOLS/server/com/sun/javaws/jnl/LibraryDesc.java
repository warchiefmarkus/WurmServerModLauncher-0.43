/*    */ package com.sun.javaws.jnl;
/*    */ 
/*    */ import com.sun.deploy.xml.XMLAttribute;
/*    */ import com.sun.deploy.xml.XMLNode;
/*    */ import com.sun.deploy.xml.XMLable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LibraryDesc
/*    */   implements XMLable
/*    */ {
/*    */   private String _uniqueId;
/*    */   
/*    */   public LibraryDesc(String paramString) {
/* 20 */     this._uniqueId = paramString;
/*    */   }
/*    */   
/*    */   public String getUniqueId() {
/* 24 */     return this._uniqueId;
/*    */   }
/*    */ 
/*    */   
/*    */   public XMLNode asXML() {
/* 29 */     return new XMLNode("library-desc", new XMLAttribute("unique-id", this._uniqueId));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\jnl\LibraryDesc.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */