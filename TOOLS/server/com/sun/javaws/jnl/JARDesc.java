/*    */ package com.sun.javaws.jnl;
/*    */ 
/*    */ import com.sun.deploy.xml.XMLAttributeBuilder;
/*    */ import com.sun.deploy.xml.XMLNode;
/*    */ import com.sun.javaws.Globals;
/*    */ import java.net.URL;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JARDesc
/*    */   implements ResourceType
/*    */ {
/*    */   private URL _location;
/*    */   private String _version;
/*    */   private int _size;
/*    */   private boolean _isNativeLib;
/*    */   private boolean _isLazyDownload;
/*    */   private boolean _isMainFile;
/*    */   private String _part;
/*    */   private ResourcesDesc _parent;
/*    */   
/*    */   public JARDesc(URL paramURL, String paramString1, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, String paramString2, int paramInt, ResourcesDesc paramResourcesDesc) {
/* 28 */     this._location = paramURL;
/* 29 */     this._version = paramString1;
/* 30 */     this._isLazyDownload = (paramBoolean1 && !this._isMainFile && !Globals.isImportMode());
/*    */ 
/*    */     
/* 33 */     this._isNativeLib = paramBoolean3;
/* 34 */     this._isMainFile = paramBoolean2;
/* 35 */     this._part = paramString2;
/* 36 */     this._size = paramInt;
/* 37 */     this._parent = paramResourcesDesc;
/*    */   }
/*    */   
/*    */   public boolean isNativeLib() {
/* 41 */     return this._isNativeLib; } public boolean isJavaFile() {
/* 42 */     return !this._isNativeLib;
/*    */   }
/*    */   
/* 45 */   public URL getLocation() { return this._location; } public String getVersion() {
/* 46 */     return this._version;
/*    */   }
/*    */   public boolean isLazyDownload() {
/* 49 */     return this._isLazyDownload;
/*    */   }
/*    */   public boolean isMainJarFile() {
/* 52 */     return this._isMainFile;
/*    */   }
/*    */   public String getPartName() {
/* 55 */     return this._part;
/*    */   }
/*    */   public int getSize() {
/* 58 */     return this._size;
/*    */   }
/*    */   public ResourcesDesc getParent() {
/* 61 */     return this._parent;
/*    */   }
/*    */   
/*    */   public void visit(ResourceVisitor paramResourceVisitor) {
/* 65 */     paramResourceVisitor.visitJARDesc(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public XMLNode asXML() {
/* 70 */     XMLAttributeBuilder xMLAttributeBuilder = new XMLAttributeBuilder();
/* 71 */     xMLAttributeBuilder.add("href", this._location);
/* 72 */     xMLAttributeBuilder.add("version", this._version);
/* 73 */     xMLAttributeBuilder.add("part", this._part);
/* 74 */     xMLAttributeBuilder.add("download", isLazyDownload() ? "lazy" : "eager");
/* 75 */     xMLAttributeBuilder.add("main", isMainJarFile() ? "true" : "false");
/* 76 */     String str = this._isNativeLib ? "nativelib" : "jar";
/* 77 */     return new XMLNode("jar", xMLAttributeBuilder.getAttributeList());
/*    */   }
/*    */   public String toString() {
/* 80 */     return "JARDesc[" + this._location + ":" + this._version;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\jnl\JARDesc.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */