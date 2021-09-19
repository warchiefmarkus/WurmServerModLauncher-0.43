/*    */ package com.sun.javaws.jnl;
/*    */ 
/*    */ import com.sun.deploy.xml.XMLAttribute;
/*    */ import com.sun.deploy.xml.XMLAttributeBuilder;
/*    */ import com.sun.deploy.xml.XMLNode;
/*    */ import com.sun.deploy.xml.XMLNodeBuilder;
/*    */ import com.sun.deploy.xml.XMLable;
/*    */ import java.net.URL;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RContentDesc
/*    */   implements XMLable
/*    */ {
/*    */   private URL _href;
/*    */   private String _title;
/*    */   private String _description;
/*    */   private URL _icon;
/*    */   private boolean _isApplication;
/*    */   
/*    */   public RContentDesc(URL paramURL1, String paramString1, String paramString2, URL paramURL2) {
/* 25 */     this._href = paramURL1;
/* 26 */     this._title = paramString1;
/* 27 */     this._description = paramString2;
/* 28 */     this._icon = paramURL2;
/* 29 */     this._isApplication = (paramURL1 != null && paramURL1.toString().endsWith(".jnlp"));
/*    */   }
/*    */   
/* 32 */   public URL getHref() { return this._href; } public URL getIcon() {
/* 33 */     return this._icon;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 39 */     if (this._title == null) {
/* 40 */       String str = this._href.getPath();
/* 41 */       return str.substring(str.lastIndexOf('/') + 1);
/*    */     } 
/* 43 */     return this._title;
/*    */   }
/* 45 */   public String getDescription() { return this._description; } public boolean isApplication() {
/* 46 */     return this._isApplication;
/*    */   }
/*    */   
/*    */   public XMLNode asXML() {
/* 50 */     XMLAttributeBuilder xMLAttributeBuilder = new XMLAttributeBuilder();
/* 51 */     xMLAttributeBuilder.add("href", this._href);
/* 52 */     XMLNodeBuilder xMLNodeBuilder = new XMLNodeBuilder("related-content", xMLAttributeBuilder.getAttributeList());
/*    */     
/* 54 */     if (this._title != null) {
/* 55 */       xMLNodeBuilder.add("title", this._title);
/*    */     }
/* 57 */     if (this._description != null) {
/* 58 */       xMLNodeBuilder.add("description", this._description);
/*    */     }
/* 60 */     if (this._icon != null) {
/* 61 */       xMLNodeBuilder.add(new XMLNode("icon", new XMLAttribute("href", this._icon.toString())));
/*    */     }
/*    */     
/* 64 */     return xMLNodeBuilder.getNode();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\jnl\RContentDesc.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */