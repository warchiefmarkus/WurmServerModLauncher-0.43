/*    */ package com.sun.javaws.jnl;
/*    */ 
/*    */ import com.sun.deploy.xml.XMLAttributeBuilder;
/*    */ import com.sun.deploy.xml.XMLNode;
/*    */ import com.sun.deploy.xml.XMLNodeBuilder;
/*    */ import java.net.URL;
/*    */ import java.util.HashSet;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExtensionDesc
/*    */   implements ResourceType
/*    */ {
/*    */   private String _name;
/*    */   private URL _location;
/*    */   private String _version;
/*    */   private boolean _isInstaller;
/*    */   private ExtDownloadDesc[] _extDownloadDescs;
/*    */   private LaunchDesc _extensionDefDesc;
/*    */   
/*    */   public ExtensionDesc(String paramString1, URL paramURL, String paramString2, ExtDownloadDesc[] paramArrayOfExtDownloadDesc) {
/* 30 */     this._location = paramURL;
/* 31 */     this._version = paramString2;
/* 32 */     this._name = paramString1;
/* 33 */     if (paramArrayOfExtDownloadDesc == null) paramArrayOfExtDownloadDesc = new ExtDownloadDesc[0]; 
/* 34 */     this._extDownloadDescs = paramArrayOfExtDownloadDesc;
/* 35 */     this._extensionDefDesc = null;
/* 36 */     this._isInstaller = false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setInstaller(boolean paramBoolean) {
/* 42 */     this._isInstaller = paramBoolean;
/* 43 */   } public boolean isInstaller() { return this._isInstaller; }
/* 44 */   public String getVersion() { return this._version; }
/* 45 */   public URL getLocation() { return this._location; }
/* 46 */   public String getName() { return this._name; } ExtDownloadDesc[] getExtDownloadDescs() {
/* 47 */     return this._extDownloadDescs;
/*    */   }
/*    */ 
/*    */   
/*    */   public LaunchDesc getExtensionDesc() {
/* 52 */     return this._extensionDefDesc; } public void setExtensionDesc(LaunchDesc paramLaunchDesc) {
/* 53 */     this._extensionDefDesc = paramLaunchDesc;
/*    */   }
/*    */   ResourcesDesc getExtensionResources() {
/* 56 */     return this._extensionDefDesc.getResources();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   HashSet getExtensionPackages(HashSet paramHashSet, boolean paramBoolean) {
/* 64 */     HashSet hashSet = new HashSet();
/* 65 */     for (byte b = 0; b < this._extDownloadDescs.length; b++) {
/* 66 */       ExtDownloadDesc extDownloadDesc = this._extDownloadDescs[b];
/*    */ 
/*    */ 
/*    */       
/* 70 */       boolean bool = (paramBoolean && !extDownloadDesc.isLazy()) ? true : false;
/* 71 */       if (bool || (paramHashSet != null && paramHashSet.contains(extDownloadDesc.getPart()))) {
/* 72 */         hashSet.add(extDownloadDesc.getExtensionPart());
/*    */       }
/*    */     } 
/* 75 */     return hashSet;
/*    */   }
/*    */ 
/*    */   
/*    */   public void visit(ResourceVisitor paramResourceVisitor) {
/* 80 */     paramResourceVisitor.visitExtensionDesc(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public XMLNode asXML() {
/* 85 */     XMLAttributeBuilder xMLAttributeBuilder = new XMLAttributeBuilder();
/* 86 */     xMLAttributeBuilder.add("href", this._location);
/* 87 */     xMLAttributeBuilder.add("version", this._version);
/* 88 */     xMLAttributeBuilder.add("name", this._name);
/* 89 */     XMLNodeBuilder xMLNodeBuilder = new XMLNodeBuilder("extension", xMLAttributeBuilder.getAttributeList());
/* 90 */     for (byte b = 0; b < this._extDownloadDescs.length; b++) {
/* 91 */       xMLNodeBuilder.add(this._extDownloadDescs[b]);
/*    */     }
/* 93 */     return xMLNodeBuilder.getNode();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\jnl\ExtensionDesc.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */