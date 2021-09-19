/*    */ package 1.0.com.sun.relaxng.javadt;
/*    */ 
/*    */ import com.sun.relaxng.javadt.AbstractDatatypeImpl;
/*    */ import com.sun.relaxng.javadt.Name;
/*    */ import org.relaxng.datatype.Datatype;
/*    */ import org.relaxng.datatype.ValidationContext;
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
/*    */ public class JavaPackageDatatype
/*    */   extends AbstractDatatypeImpl
/*    */ {
/* 24 */   public static final Datatype theInstance = (Datatype)new com.sun.relaxng.javadt.JavaPackageDatatype();
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isValid(String token, ValidationContext context) {
/* 29 */     return Name.isJavaPackageName(token.trim());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\relaxng\javadt\JavaPackageDatatype.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */