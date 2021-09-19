/*    */ package 1.0.com.sun.relaxng.javadt;
/*    */ 
/*    */ import com.sun.relaxng.javadt.JavaIdentifierDatatype;
/*    */ import com.sun.relaxng.javadt.JavaPackageDatatype;
/*    */ import org.relaxng.datatype.Datatype;
/*    */ import org.relaxng.datatype.DatatypeBuilder;
/*    */ import org.relaxng.datatype.DatatypeException;
/*    */ import org.relaxng.datatype.DatatypeLibrary;
/*    */ import org.relaxng.datatype.DatatypeLibraryFactory;
/*    */ import org.relaxng.datatype.helpers.ParameterlessDatatypeBuilder;
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
/*    */ public class DatatypeLibraryImpl
/*    */   implements DatatypeLibrary, DatatypeLibraryFactory
/*    */ {
/*    */   public static final String NAMESPACE_URI = "http://java.sun.com/xml/ns/relaxng/java-datatypes";
/*    */   
/*    */   public DatatypeBuilder createDatatypeBuilder(String name) throws DatatypeException {
/* 38 */     return (DatatypeBuilder)new ParameterlessDatatypeBuilder(createDatatype(name));
/*    */   }
/*    */   
/*    */   public Datatype createDatatype(String name) throws DatatypeException {
/* 42 */     if ("identifier".equals(name))
/* 43 */       return JavaIdentifierDatatype.theInstance; 
/* 44 */     if ("package".equals(name)) {
/* 45 */       return JavaPackageDatatype.theInstance;
/*    */     }
/* 47 */     throw new DatatypeException();
/*    */   }
/*    */   
/*    */   public DatatypeLibrary createDatatypeLibrary(String namespaceUri) {
/* 51 */     if ("http://java.sun.com/xml/ns/relaxng/java-datatypes".equals(namespaceUri)) {
/* 52 */       return this;
/*    */     }
/* 54 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\relaxng\javadt\DatatypeLibraryImpl.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */