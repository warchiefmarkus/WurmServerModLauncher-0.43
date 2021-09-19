/*    */ package org.kohsuke.rngom.dt;
/*    */ 
/*    */ import org.relaxng.datatype.DatatypeLibrary;
/*    */ import org.relaxng.datatype.DatatypeLibraryFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CachedDatatypeLibraryFactory
/*    */   implements DatatypeLibraryFactory
/*    */ {
/*    */   private String lastUri;
/*    */   private DatatypeLibrary lastLib;
/*    */   private final DatatypeLibraryFactory core;
/*    */   
/*    */   public CachedDatatypeLibraryFactory(DatatypeLibraryFactory core) {
/* 19 */     this.core = core;
/*    */   }
/*    */   
/*    */   public DatatypeLibrary createDatatypeLibrary(String namespaceURI) {
/* 23 */     if (this.lastUri == namespaceURI) {
/* 24 */       return this.lastLib;
/*    */     }
/* 26 */     this.lastUri = namespaceURI;
/* 27 */     this.lastLib = this.core.createDatatypeLibrary(namespaceURI);
/* 28 */     return this.lastLib;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\dt\CachedDatatypeLibraryFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */