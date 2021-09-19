/*    */ package org.apache.http.impl.cookie;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Date;
/*    */ import org.apache.http.annotation.NotThreadSafe;
/*    */ import org.apache.http.cookie.SetCookie2;
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
/*    */ @NotThreadSafe
/*    */ public class BasicClientCookie2
/*    */   extends BasicClientCookie
/*    */   implements SetCookie2, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -7744598295706617057L;
/*    */   private String commentURL;
/*    */   private int[] ports;
/*    */   private boolean discard;
/*    */   
/*    */   public BasicClientCookie2(String name, String value) {
/* 58 */     super(name, value);
/*    */   }
/*    */ 
/*    */   
/*    */   public int[] getPorts() {
/* 63 */     return this.ports;
/*    */   }
/*    */   
/*    */   public void setPorts(int[] ports) {
/* 67 */     this.ports = ports;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommentURL() {
/* 72 */     return this.commentURL;
/*    */   }
/*    */   
/*    */   public void setCommentURL(String commentURL) {
/* 76 */     this.commentURL = commentURL;
/*    */   }
/*    */   
/*    */   public void setDiscard(boolean discard) {
/* 80 */     this.discard = discard;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPersistent() {
/* 85 */     return (!this.discard && super.isPersistent());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isExpired(Date date) {
/* 90 */     return (this.discard || super.isExpired(date));
/*    */   }
/*    */ 
/*    */   
/*    */   public Object clone() throws CloneNotSupportedException {
/* 95 */     BasicClientCookie2 clone = (BasicClientCookie2)super.clone();
/* 96 */     if (this.ports != null) {
/* 97 */       clone.ports = (int[])this.ports.clone();
/*    */     }
/* 99 */     return clone;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\cookie\BasicClientCookie2.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */