/*    */ package 1.0.com.sun.codemodel.util;
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
/*    */ public class ClassNameComparator
/*    */   implements Comparator
/*    */ {
/*    */   public int compare(Object l, Object r) {
/* 22 */     return ((JClass)l).fullName().compareTo(((JClass)r).fullName());
/*    */   }
/*    */ 
/*    */   
/* 26 */   public static final Comparator theInstance = new com.sun.codemodel.util.ClassNameComparator();
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemode\\util\ClassNameComparator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */