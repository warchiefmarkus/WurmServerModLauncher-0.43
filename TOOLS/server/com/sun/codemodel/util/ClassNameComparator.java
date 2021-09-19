/*    */ package com.sun.codemodel.util;
/*    */ 
/*    */ import com.sun.codemodel.JClass;
/*    */ import java.util.Comparator;
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
/*    */ public class ClassNameComparator
/*    */   implements Comparator<JClass>
/*    */ {
/*    */   public int compare(JClass l, JClass r) {
/* 36 */     return l.fullName().compareTo(r.fullName());
/*    */   }
/*    */   
/* 39 */   public static final Comparator<JClass> theInstance = new ClassNameComparator();
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemode\\util\ClassNameComparator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */