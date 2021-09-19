/*    */ package com.sun.codemodel.fmt;
/*    */ 
/*    */ import com.sun.codemodel.JResourceFile;
/*    */ import java.io.DataInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
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
/*    */ 
/*    */ 
/*    */ public final class JStaticFile
/*    */   extends JResourceFile
/*    */ {
/*    */   private final ClassLoader classLoader;
/*    */   private final String resourceName;
/*    */   private final boolean isResource;
/*    */   
/*    */   public JStaticFile(String _resourceName) {
/* 41 */     this(_resourceName, !_resourceName.endsWith(".java"));
/*    */   }
/*    */   
/*    */   public JStaticFile(String _resourceName, boolean isResource) {
/* 45 */     this(JStaticFile.class.getClassLoader(), _resourceName, isResource);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JStaticFile(ClassLoader _classLoader, String _resourceName, boolean isResource) {
/* 53 */     super(_resourceName.substring(_resourceName.lastIndexOf('/') + 1));
/* 54 */     this.classLoader = _classLoader;
/* 55 */     this.resourceName = _resourceName;
/* 56 */     this.isResource = isResource;
/*    */   }
/*    */   
/*    */   protected boolean isResource() {
/* 60 */     return this.isResource;
/*    */   }
/*    */   
/*    */   protected void build(OutputStream os) throws IOException {
/* 64 */     DataInputStream dis = new DataInputStream(this.classLoader.getResourceAsStream(this.resourceName));
/*    */     
/* 66 */     byte[] buf = new byte[256];
/*    */     int sz;
/* 68 */     while ((sz = dis.read(buf)) > 0) {
/* 69 */       os.write(buf, 0, sz);
/*    */     }
/* 71 */     dis.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\fmt\JStaticFile.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */