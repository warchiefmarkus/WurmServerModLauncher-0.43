/*    */ package com.sun.javaws.jnl;
/*    */ 
/*    */ import com.sun.deploy.xml.XMLAttribute;
/*    */ import com.sun.deploy.xml.XMLAttributeBuilder;
/*    */ import com.sun.deploy.xml.XMLNode;
/*    */ import com.sun.deploy.xml.XMLNodeBuilder;
/*    */ import com.sun.deploy.xml.XMLable;
/*    */ import java.net.URL;
/*    */ import java.util.Enumeration;
/*    */ import java.util.Properties;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AppletDesc
/*    */   implements XMLable
/*    */ {
/*    */   private String _name;
/*    */   private String _appletClass;
/*    */   private URL _documentBase;
/*    */   private int _width;
/*    */   private int _height;
/*    */   private Properties _params;
/*    */   
/*    */   public AppletDesc(String paramString1, String paramString2, URL paramURL, int paramInt1, int paramInt2, Properties paramProperties) {
/* 26 */     this._name = paramString1;
/* 27 */     this._appletClass = paramString2;
/* 28 */     this._documentBase = paramURL;
/* 29 */     this._width = paramInt1;
/* 30 */     this._height = paramInt2;
/* 31 */     this._params = paramProperties;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 35 */     return this._name;
/*    */   }
/*    */   public String getAppletClass() {
/* 38 */     return this._appletClass;
/*    */   }
/*    */   public URL getDocumentBase() {
/* 41 */     return this._documentBase;
/*    */   }
/*    */   public int getWidth() {
/* 44 */     return this._width;
/*    */   }
/*    */   public int getHeight() {
/* 47 */     return this._height;
/*    */   }
/*    */   public Properties getParameters() {
/* 50 */     return this._params;
/*    */   }
/*    */   
/*    */   public XMLNode asXML() {
/* 54 */     XMLAttributeBuilder xMLAttributeBuilder = new XMLAttributeBuilder();
/* 55 */     xMLAttributeBuilder.add("name", this._name);
/* 56 */     xMLAttributeBuilder.add("code", this._appletClass);
/* 57 */     xMLAttributeBuilder.add("documentbase", this._documentBase);
/* 58 */     xMLAttributeBuilder.add("width", this._width);
/* 59 */     xMLAttributeBuilder.add("height", this._height);
/* 60 */     XMLNodeBuilder xMLNodeBuilder = new XMLNodeBuilder("applet-desc", xMLAttributeBuilder.getAttributeList());
/* 61 */     if (this._params != null) {
/* 62 */       Enumeration enumeration = this._params.keys();
/* 63 */       while (enumeration.hasMoreElements()) {
/* 64 */         String str1 = (String)enumeration.nextElement();
/* 65 */         String str2 = this._params.getProperty(str1);
/* 66 */         xMLNodeBuilder.add(new XMLNode("param", new XMLAttribute("name", str1, new XMLAttribute("value", str2)), null, null));
/*    */       } 
/*    */     } 
/* 69 */     return xMLNodeBuilder.getNode();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\jnl\AppletDesc.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */