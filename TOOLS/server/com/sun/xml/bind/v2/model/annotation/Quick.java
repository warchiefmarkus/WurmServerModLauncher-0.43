/*    */ package com.sun.xml.bind.v2.model.annotation;
/*    */ 
/*    */ import com.sun.xml.bind.v2.runtime.Location;
/*    */ import java.lang.annotation.Annotation;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Quick
/*    */   implements Annotation, Locatable, Location
/*    */ {
/*    */   private final Locatable upstream;
/*    */   
/*    */   protected Quick(Locatable upstream) {
/* 56 */     this.upstream = upstream;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract Annotation getAnnotation();
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract Quick newInstance(Locatable paramLocatable, Annotation paramAnnotation);
/*    */ 
/*    */ 
/*    */   
/*    */   public final Location getLocation() {
/* 71 */     return this;
/*    */   }
/*    */   
/*    */   public final Locatable getUpstream() {
/* 75 */     return this.upstream;
/*    */   }
/*    */   
/*    */   public final String toString() {
/* 79 */     return getAnnotation().toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\annotation\Quick.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */