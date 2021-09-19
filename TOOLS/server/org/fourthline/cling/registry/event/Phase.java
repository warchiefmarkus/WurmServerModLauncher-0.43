/*    */ package org.fourthline.cling.registry.event;
/*    */ 
/*    */ import java.lang.annotation.ElementType;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
/*    */ import javax.enterprise.util.AnnotationLiteral;
/*    */ import javax.inject.Qualifier;
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
/*    */ public interface Phase
/*    */ {
/* 33 */   public static final AnnotationLiteral<Alive> ALIVE = new AnnotationLiteral<Alive>() {
/*    */     
/*    */     };
/* 36 */   public static final AnnotationLiteral<Complete> COMPLETE = new AnnotationLiteral<Complete>() {
/*    */     
/*    */     };
/* 39 */   public static final AnnotationLiteral<Byebye> BYEBYE = new AnnotationLiteral<Byebye>() {
/*    */     
/*    */     };
/* 42 */   public static final AnnotationLiteral<Updated> UPDATED = new AnnotationLiteral<Updated>() {
/*    */     
/*    */     };
/*    */   
/*    */   @Qualifier
/*    */   @Target({ElementType.FIELD, ElementType.PARAMETER})
/*    */   @Retention(RetentionPolicy.RUNTIME)
/*    */   public static @interface Alive {}
/*    */   
/*    */   @Qualifier
/*    */   @Target({ElementType.FIELD, ElementType.PARAMETER})
/*    */   @Retention(RetentionPolicy.RUNTIME)
/*    */   public static @interface Complete {}
/*    */   
/*    */   @Qualifier
/*    */   @Target({ElementType.FIELD, ElementType.PARAMETER})
/*    */   @Retention(RetentionPolicy.RUNTIME)
/*    */   public static @interface Byebye {}
/*    */   
/*    */   @Qualifier
/*    */   @Target({ElementType.FIELD, ElementType.PARAMETER})
/*    */   @Retention(RetentionPolicy.RUNTIME)
/*    */   public static @interface Updated {}
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\registry\event\Phase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */