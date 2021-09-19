/*    */ package com.sun.xml.bind.v2.model.annotation;
/*    */ 
/*    */ import com.sun.xml.bind.v2.model.nav.Navigator;
/*    */ import com.sun.xml.bind.v2.runtime.Location;
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
/*    */ public class MethodLocatable<M>
/*    */   implements Locatable
/*    */ {
/*    */   private final Locatable upstream;
/*    */   private final M method;
/*    */   private final Navigator<?, ?, ?, M> nav;
/*    */   
/*    */   public MethodLocatable(Locatable upstream, M method, Navigator<?, ?, ?, M> nav) {
/* 53 */     this.upstream = upstream;
/* 54 */     this.method = method;
/* 55 */     this.nav = nav;
/*    */   }
/*    */   
/*    */   public Locatable getUpstream() {
/* 59 */     return this.upstream;
/*    */   }
/*    */   
/*    */   public Location getLocation() {
/* 63 */     return this.nav.getMethodLocation(this.method);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\annotation\MethodLocatable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */