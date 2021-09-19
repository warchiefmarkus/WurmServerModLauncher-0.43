/*    */ package com.sun.javaws.jnl;
/*    */ 
/*    */ import com.sun.deploy.xml.XMLAttribute;
/*    */ import com.sun.deploy.xml.XMLNode;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PropertyDesc
/*    */   implements ResourceType
/*    */ {
/*    */   private String _key;
/*    */   private String _value;
/*    */   
/*    */   public PropertyDesc(String paramString1, String paramString2) {
/* 17 */     this._key = paramString1;
/* 18 */     this._value = paramString2;
/*    */   }
/*    */   
/*    */   String getKey() {
/* 22 */     return this._key; } String getValue() {
/* 23 */     return this._value;
/*    */   }
/*    */   
/*    */   public void visit(ResourceVisitor paramResourceVisitor) {
/* 27 */     paramResourceVisitor.visitPropertyDesc(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public XMLNode asXML() {
/* 32 */     return new XMLNode("property", new XMLAttribute("name", getKey(), new XMLAttribute("value", getValue())), null, null);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\jnl\PropertyDesc.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */