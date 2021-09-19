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
/*    */ public class JavaIdentifierDatatype
/*    */   extends AbstractDatatypeImpl
/*    */ {
/* 24 */   public static final Datatype theInstance = (Datatype)new com.sun.relaxng.javadt.JavaIdentifierDatatype();
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isValid(String token, ValidationContext context) {
/* 29 */     return Name.isJavaIdentifier(token.trim());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\relaxng\javadt\JavaIdentifierDatatype.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */