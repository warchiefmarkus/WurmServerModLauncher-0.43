/*    */ package org.flywaydb.core.internal.util.scanner.classpath.android;
/*    */ 
/*    */ import android.content.res.AssetManager;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import org.flywaydb.core.api.FlywayException;
/*    */ import org.flywaydb.core.internal.util.FileCopyUtils;
/*    */ import org.flywaydb.core.internal.util.scanner.Resource;
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
/*    */ public class AndroidResource
/*    */   implements Resource
/*    */ {
/*    */   private final AssetManager assetManager;
/*    */   private final String path;
/*    */   private final String name;
/*    */   
/*    */   public AndroidResource(AssetManager assetManager, String path, String name) {
/* 35 */     this.assetManager = assetManager;
/* 36 */     this.path = path;
/* 37 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getLocation() {
/* 42 */     return this.path + "/" + this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getLocationOnDisk() {
/* 47 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String loadAsString(String encoding) {
/*    */     try {
/* 53 */       return FileCopyUtils.copyToString(new InputStreamReader(this.assetManager.open(getLocation()), encoding));
/* 54 */     } catch (IOException e) {
/* 55 */       throw new FlywayException("Unable to load asset: " + getLocation(), e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] loadAsBytes() {
/*    */     try {
/* 62 */       return FileCopyUtils.copyToByteArray(this.assetManager.open(getLocation()));
/* 63 */     } catch (IOException e) {
/* 64 */       throw new FlywayException("Unable to load asset: " + getLocation(), e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getFilename() {
/* 70 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\scanner\classpath\android\AndroidResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */