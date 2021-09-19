/*    */ package org.kohsuke.rngom.dt.builtin;
/*    */ 
/*    */ import org.relaxng.datatype.Datatype;
/*    */ import org.relaxng.datatype.DatatypeBuilder;
/*    */ import org.relaxng.datatype.DatatypeException;
/*    */ import org.relaxng.datatype.DatatypeLibrary;
/*    */ import org.relaxng.datatype.DatatypeLibraryFactory;
/*    */ 
/*    */ public class BuiltinDatatypeLibrary
/*    */   implements DatatypeLibrary
/*    */ {
/*    */   private final DatatypeLibraryFactory factory;
/* 13 */   private DatatypeLibrary xsdDatatypeLibrary = null;
/*    */   
/*    */   BuiltinDatatypeLibrary(DatatypeLibraryFactory factory) {
/* 16 */     this.factory = factory;
/*    */   }
/*    */ 
/*    */   
/*    */   public DatatypeBuilder createDatatypeBuilder(String type) throws DatatypeException {
/* 21 */     this.xsdDatatypeLibrary = this.factory.createDatatypeLibrary("http://www.w3.org/2001/XMLSchema-datatypes");
/*    */ 
/*    */     
/* 24 */     if (this.xsdDatatypeLibrary == null) {
/* 25 */       throw new DatatypeException();
/*    */     }
/* 27 */     if (type.equals("string") || type.equals("token")) {
/* 28 */       return new BuiltinDatatypeBuilder(this.xsdDatatypeLibrary.createDatatype(type));
/*    */     }
/*    */     
/* 31 */     throw new DatatypeException();
/*    */   }
/*    */   public Datatype createDatatype(String type) throws DatatypeException {
/* 34 */     return createDatatypeBuilder(type).createDatatype();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\dt\builtin\BuiltinDatatypeLibrary.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */