/*    */ package com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSAnnotation;
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.helpers.LocatorImpl;
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
/*    */ public class AnnotationImpl
/*    */   implements XSAnnotation
/*    */ {
/*    */   private Object annotation;
/*    */   private final Locator locator;
/*    */   
/*    */   public Object getAnnotation() {
/* 29 */     return this.annotation;
/*    */   }
/*    */   public Object setAnnotation(Object o) {
/* 32 */     Object r = this.annotation;
/* 33 */     this.annotation = o;
/* 34 */     return r;
/*    */   }
/*    */   
/*    */   public Locator getLocator() {
/* 38 */     return this.locator;
/*    */   }
/*    */   public AnnotationImpl(Object o, Locator _loc) {
/* 41 */     this.annotation = o;
/* 42 */     this.locator = _loc;
/*    */   }
/*    */   
/*    */   public AnnotationImpl() {
/* 46 */     this.locator = NULL_LOCATION;
/*    */   }
/*    */ 
/*    */   
/* 50 */   private static final LocatorImpl NULL_LOCATION = new LocatorImpl();
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\AnnotationImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */