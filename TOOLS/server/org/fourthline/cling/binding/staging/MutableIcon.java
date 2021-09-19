/*    */ package org.fourthline.cling.binding.staging;
/*    */ 
/*    */ import java.net.URI;
/*    */ import org.fourthline.cling.model.meta.Icon;
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
/*    */ public class MutableIcon
/*    */ {
/*    */   public String mimeType;
/*    */   public int width;
/*    */   public int height;
/*    */   public int depth;
/*    */   public URI uri;
/*    */   
/*    */   public Icon build() {
/* 34 */     return new Icon(this.mimeType, this.width, this.height, this.depth, this.uri);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\binding\staging\MutableIcon.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */