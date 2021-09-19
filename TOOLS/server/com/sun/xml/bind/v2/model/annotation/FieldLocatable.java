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
/*    */ public class FieldLocatable<F>
/*    */   implements Locatable
/*    */ {
/*    */   private final Locatable upstream;
/*    */   private final F field;
/*    */   private final Navigator<?, ?, F, ?> nav;
/*    */   
/*    */   public FieldLocatable(Locatable upstream, F field, Navigator<?, ?, F, ?> nav) {
/* 53 */     this.upstream = upstream;
/* 54 */     this.field = field;
/* 55 */     this.nav = nav;
/*    */   }
/*    */   
/*    */   public Locatable getUpstream() {
/* 59 */     return this.upstream;
/*    */   }
/*    */   
/*    */   public Location getLocation() {
/* 63 */     return this.nav.getFieldLocation(this.field);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\annotation\FieldLocatable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */