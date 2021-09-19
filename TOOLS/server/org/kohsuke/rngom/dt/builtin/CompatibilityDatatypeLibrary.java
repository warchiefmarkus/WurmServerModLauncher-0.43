/*    */ package org.kohsuke.rngom.dt.builtin;
/*    */ 
/*    */ import org.relaxng.datatype.Datatype;
/*    */ import org.relaxng.datatype.DatatypeBuilder;
/*    */ import org.relaxng.datatype.DatatypeException;
/*    */ import org.relaxng.datatype.DatatypeLibrary;
/*    */ import org.relaxng.datatype.DatatypeLibraryFactory;
/*    */ 
/*    */ class CompatibilityDatatypeLibrary
/*    */   implements DatatypeLibrary {
/*    */   private final DatatypeLibraryFactory factory;
/* 12 */   private DatatypeLibrary xsdDatatypeLibrary = null;
/*    */   
/*    */   CompatibilityDatatypeLibrary(DatatypeLibraryFactory factory) {
/* 15 */     this.factory = factory;
/*    */   }
/*    */ 
/*    */   
/*    */   public DatatypeBuilder createDatatypeBuilder(String type) throws DatatypeException {
/* 20 */     if (type.equals("ID") || type.equals("IDREF") || type.equals("IDREFS")) {
/*    */ 
/*    */       
/* 23 */       if (this.xsdDatatypeLibrary == null) {
/* 24 */         this.xsdDatatypeLibrary = this.factory.createDatatypeLibrary("http://www.w3.org/2001/XMLSchema-datatypes");
/*    */ 
/*    */         
/* 27 */         if (this.xsdDatatypeLibrary == null)
/* 28 */           throw new DatatypeException(); 
/*    */       } 
/* 30 */       return this.xsdDatatypeLibrary.createDatatypeBuilder(type);
/*    */     } 
/* 32 */     throw new DatatypeException();
/*    */   }
/*    */   
/*    */   public Datatype createDatatype(String type) throws DatatypeException {
/* 36 */     return createDatatypeBuilder(type).createDatatype();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\dt\builtin\CompatibilityDatatypeLibrary.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */