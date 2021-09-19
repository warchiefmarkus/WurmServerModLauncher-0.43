/*    */ package org.apache.http.auth;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.security.Principal;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.util.LangUtils;
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
/*    */ @Immutable
/*    */ public final class BasicUserPrincipal
/*    */   implements Principal, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -2266305184969850467L;
/*    */   private final String username;
/*    */   
/*    */   public BasicUserPrincipal(String username) {
/* 50 */     if (username == null) {
/* 51 */       throw new IllegalArgumentException("User name may not be null");
/*    */     }
/* 53 */     this.username = username;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 57 */     return this.username;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 62 */     int hash = 17;
/* 63 */     hash = LangUtils.hashCode(hash, this.username);
/* 64 */     return hash;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 69 */     if (this == o) return true; 
/* 70 */     if (o instanceof BasicUserPrincipal) {
/* 71 */       BasicUserPrincipal that = (BasicUserPrincipal)o;
/* 72 */       if (LangUtils.equals(this.username, that.username)) {
/* 73 */         return true;
/*    */       }
/*    */     } 
/* 76 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 81 */     StringBuilder buffer = new StringBuilder();
/* 82 */     buffer.append("[principal: ");
/* 83 */     buffer.append(this.username);
/* 84 */     buffer.append("]");
/* 85 */     return buffer.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\auth\BasicUserPrincipal.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */