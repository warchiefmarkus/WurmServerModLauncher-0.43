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
/*    */ public class ClassLocatable<C>
/*    */   implements Locatable
/*    */ {
/*    */   private final Locatable upstream;
/*    */   private final C clazz;
/*    */   private final Navigator<?, C, ?, ?> nav;
/*    */   
/*    */   public ClassLocatable(Locatable upstream, C clazz, Navigator<?, C, ?, ?> nav) {
/* 53 */     this.upstream = upstream;
/* 54 */     this.clazz = clazz;
/* 55 */     this.nav = nav;
/*    */   }
/*    */   
/*    */   public Locatable getUpstream() {
/* 59 */     return this.upstream;
/*    */   }
/*    */   
/*    */   public Location getLocation() {
/* 63 */     return this.nav.getClassLocation(this.clazz);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\annotation\ClassLocatable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */