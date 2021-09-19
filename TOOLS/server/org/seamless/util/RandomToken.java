/*    */ package org.seamless.util;
/*    */ 
/*    */ import java.security.SecureRandom;
/*    */ import java.util.Random;
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
/*    */ public class RandomToken
/*    */ {
/*    */   protected final Random random;
/*    */   
/*    */   public RandomToken() {
/*    */     try {
/* 29 */       this.random = SecureRandom.getInstance("SHA1PRNG", "SUN");
/* 30 */     } catch (Exception ex) {
/* 31 */       throw new RuntimeException(ex);
/*    */     } 
/*    */     
/* 34 */     this.random.nextBytes(new byte[1]);
/*    */   }
/*    */   
/*    */   public String generate() {
/* 38 */     String token = null;
/* 39 */     while (token == null || token.length() == 0) {
/* 40 */       long r0 = this.random.nextLong();
/* 41 */       if (r0 < 0L)
/* 42 */         r0 = -r0; 
/* 43 */       long r1 = this.random.nextLong();
/* 44 */       if (r1 < 0L)
/* 45 */         r1 = -r1; 
/* 46 */       token = Long.toString(r0, 36) + Long.toString(r1, 36);
/*    */     } 
/* 48 */     return token;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\RandomToken.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */