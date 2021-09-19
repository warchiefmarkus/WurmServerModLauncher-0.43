/*    */ package org.kohsuke.rngom.dt;
/*    */ 
/*    */ import org.relaxng.datatype.DatatypeLibrary;
/*    */ import org.relaxng.datatype.DatatypeLibraryFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CascadingDatatypeLibraryFactory
/*    */   implements DatatypeLibraryFactory
/*    */ {
/*    */   private final DatatypeLibraryFactory factory1;
/*    */   private final DatatypeLibraryFactory factory2;
/*    */   
/*    */   public CascadingDatatypeLibraryFactory(DatatypeLibraryFactory factory1, DatatypeLibraryFactory factory2) {
/* 17 */     this.factory1 = factory1;
/* 18 */     this.factory2 = factory2;
/*    */   }
/*    */   
/*    */   public DatatypeLibrary createDatatypeLibrary(String namespaceURI) {
/* 22 */     DatatypeLibrary lib = this.factory1.createDatatypeLibrary(namespaceURI);
/* 23 */     if (lib == null)
/* 24 */       lib = this.factory2.createDatatypeLibrary(namespaceURI); 
/* 25 */     return lib;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\dt\CascadingDatatypeLibraryFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */