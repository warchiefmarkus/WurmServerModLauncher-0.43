/*    */ package com.sun.javaws.jnl;
/*    */ 
/*    */ import com.sun.deploy.xml.XMLAttributeBuilder;
/*    */ import com.sun.deploy.xml.XMLNode;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PackageDesc
/*    */   implements ResourceType
/*    */ {
/*    */   private String _packageName;
/*    */   private String _part;
/*    */   private boolean _isRecursive;
/*    */   private boolean _isExact;
/*    */   
/*    */   public PackageDesc(String paramString1, String paramString2, boolean paramBoolean) {
/* 21 */     if (paramString1.endsWith(".*")) {
/*    */       
/* 23 */       this._packageName = paramString1.substring(0, paramString1.length() - 1);
/* 24 */       this._isExact = false;
/*    */     } else {
/* 26 */       this._isExact = true;
/* 27 */       this._packageName = paramString1;
/*    */     } 
/* 29 */     this._part = paramString2;
/* 30 */     this._isRecursive = paramBoolean;
/*    */   }
/*    */   
/*    */   String getPackageName() {
/* 34 */     return this._packageName;
/* 35 */   } String getPart() { return this._part; } boolean isRecursive() {
/* 36 */     return this._isRecursive;
/*    */   }
/*    */ 
/*    */   
/*    */   boolean match(String paramString) {
/* 41 */     if (this._isExact)
/*    */     {
/* 43 */       return this._packageName.equals(paramString); } 
/* 44 */     if (this._isRecursive) {
/* 45 */       return paramString.startsWith(this._packageName);
/*    */     }
/*    */     
/* 48 */     int i = paramString.lastIndexOf('.');
/* 49 */     if (i != -1) paramString = paramString.substring(0, i + 1); 
/* 50 */     return paramString.equals(this._packageName);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void visit(ResourceVisitor paramResourceVisitor) {
/* 56 */     paramResourceVisitor.visitPackageDesc(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public XMLNode asXML() {
/* 61 */     XMLAttributeBuilder xMLAttributeBuilder = new XMLAttributeBuilder();
/* 62 */     xMLAttributeBuilder.add("name", getPackageName());
/* 63 */     xMLAttributeBuilder.add("part", getPart());
/* 64 */     xMLAttributeBuilder.add("recursive", isRecursive());
/* 65 */     return new XMLNode("package", xMLAttributeBuilder.getAttributeList());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\jnl\PackageDesc.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */