/*    */ package com.sun.javaws.jnl;
/*    */ 
/*    */ import com.sun.deploy.xml.XMLAttribute;
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
/*    */ public class ShortcutDesc
/*    */   implements XMLable
/*    */ {
/*    */   private boolean _online;
/*    */   private boolean _desktop;
/*    */   private boolean _menu;
/*    */   private String _submenu;
/*    */   
/*    */   public ShortcutDesc(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, String paramString) {
/* 25 */     this._online = paramBoolean1;
/* 26 */     this._desktop = paramBoolean2;
/* 27 */     this._menu = paramBoolean3;
/* 28 */     this._submenu = paramString;
/*    */   }
/*    */   
/* 31 */   public boolean getOnline() { return this._online; }
/* 32 */   public boolean getDesktop() { return this._desktop; }
/* 33 */   public boolean getMenu() { return this._menu; } public String getSubmenu() {
/* 34 */     return this._submenu;
/*    */   }
/*    */ 
/*    */   
/*    */   public XMLNode asXML() {
/* 39 */     XMLAttributeBuilder xMLAttributeBuilder = new XMLAttributeBuilder();
/* 40 */     xMLAttributeBuilder.add("online", this._online);
/* 41 */     XMLNodeBuilder xMLNodeBuilder = new XMLNodeBuilder("shortcut", xMLAttributeBuilder.getAttributeList());
/*    */ 
/*    */     
/* 44 */     if (this._desktop) {
/* 45 */       xMLNodeBuilder.add("desktop", null);
/*    */     }
/* 47 */     if (this._menu) {
/* 48 */       if (this._submenu == null) {
/* 49 */         xMLNodeBuilder.add("menu", null);
/*    */       } else {
/* 51 */         xMLNodeBuilder.add(new XMLNode("menu", new XMLAttribute("submenu", this._submenu)));
/*    */       } 
/*    */     }
/* 54 */     return xMLNodeBuilder.getNode();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\jnl\ShortcutDesc.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */