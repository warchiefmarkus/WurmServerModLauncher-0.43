/*    */ package com.sun.javaws.jnl;
/*    */ 
/*    */ import com.sun.deploy.xml.XMLAttributeBuilder;
/*    */ import com.sun.deploy.xml.XMLNode;
/*    */ import java.net.URL;
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
/*    */ 
/*    */ public class JREDesc
/*    */   implements ResourceType
/*    */ {
/*    */   private String _version;
/*    */   private long _maxHeap;
/*    */   private long _minHeap;
/*    */   private String _vmargs;
/*    */   private URL _href;
/*    */   private boolean _isSelected;
/*    */   private ResourcesDesc _resourceDesc;
/*    */   private LaunchDesc _extensioDesc;
/*    */   
/*    */   public JREDesc(String paramString1, long paramLong1, long paramLong2, String paramString2, URL paramURL, ResourcesDesc paramResourcesDesc) {
/* 31 */     this._version = paramString1;
/* 32 */     this._maxHeap = paramLong2;
/* 33 */     this._minHeap = paramLong1;
/* 34 */     this._vmargs = paramString2;
/* 35 */     this._href = paramURL;
/* 36 */     this._isSelected = false;
/* 37 */     this._resourceDesc = paramResourcesDesc;
/* 38 */     this._extensioDesc = null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getVersion() {
/* 44 */     return this._version; } public URL getHref() {
/* 45 */     return this._href;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getMinHeap() {
/* 50 */     return this._minHeap; }
/* 51 */   public long getMaxHeap() { return this._maxHeap; } public String getVmArgs() {
/* 52 */     return this._vmargs;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSelected() {
/* 57 */     return this._isSelected; } public void markAsSelected() {
/* 58 */     this._isSelected = true;
/*    */   }
/*    */   public ResourcesDesc getNestedResources() {
/* 61 */     return this._resourceDesc;
/*    */   }
/*    */ 
/*    */   
/*    */   public LaunchDesc getExtensionDesc() {
/* 66 */     return this._extensioDesc; } public void setExtensionDesc(LaunchDesc paramLaunchDesc) {
/* 67 */     this._extensioDesc = paramLaunchDesc;
/*    */   }
/*    */   
/*    */   public void visit(ResourceVisitor paramResourceVisitor) {
/* 71 */     paramResourceVisitor.visitJREDesc(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public XMLNode asXML() {
/* 76 */     XMLAttributeBuilder xMLAttributeBuilder = new XMLAttributeBuilder();
/* 77 */     if (this._minHeap > 0L) {
/* 78 */       xMLAttributeBuilder.add("initial-heap-size", this._minHeap);
/*    */     }
/* 80 */     if (this._maxHeap > 0L) {
/* 81 */       xMLAttributeBuilder.add("max-heap-size", this._maxHeap);
/*    */     }
/* 83 */     if (this._vmargs != null) {
/* 84 */       xMLAttributeBuilder.add("java-vm-args", this._vmargs);
/*    */     }
/* 86 */     xMLAttributeBuilder.add("href", this._href);
/* 87 */     if (this._version != null) {
/* 88 */       xMLAttributeBuilder.add("version", this._version);
/*    */     }
/* 90 */     XMLNode xMLNode = (this._extensioDesc != null) ? this._extensioDesc.asXML() : null;
/* 91 */     if (this._resourceDesc != null) {
/* 92 */       xMLNode = this._resourceDesc.asXML();
/*    */     }
/* 94 */     return new XMLNode("j2se", xMLAttributeBuilder.getAttributeList(), xMLNode, null);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\jnl\JREDesc.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */