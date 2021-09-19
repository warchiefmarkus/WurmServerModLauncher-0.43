/*    */ package org.kohsuke.rngom.dt.builtin;
/*    */ 
/*    */ import org.relaxng.datatype.DatatypeLibrary;
/*    */ import org.relaxng.datatype.DatatypeLibraryFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BuiltinDatatypeLibraryFactory
/*    */   implements DatatypeLibraryFactory
/*    */ {
/*    */   private final DatatypeLibrary builtinDatatypeLibrary;
/*    */   private final DatatypeLibrary compatibilityDatatypeLibrary;
/*    */   private final DatatypeLibraryFactory core;
/*    */   
/*    */   public BuiltinDatatypeLibraryFactory(DatatypeLibraryFactory coreFactory) {
/* 21 */     this.builtinDatatypeLibrary = new BuiltinDatatypeLibrary(coreFactory);
/* 22 */     this.compatibilityDatatypeLibrary = new CompatibilityDatatypeLibrary(coreFactory);
/* 23 */     this.core = coreFactory;
/*    */   }
/*    */   
/*    */   public DatatypeLibrary createDatatypeLibrary(String uri) {
/* 27 */     if (uri.equals(""))
/* 28 */       return this.builtinDatatypeLibrary; 
/* 29 */     if (uri.equals("http://relaxng.org/ns/compatibility/datatypes/1.0"))
/* 30 */       return this.compatibilityDatatypeLibrary; 
/* 31 */     return this.core.createDatatypeLibrary(uri);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\dt\builtin\BuiltinDatatypeLibraryFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */