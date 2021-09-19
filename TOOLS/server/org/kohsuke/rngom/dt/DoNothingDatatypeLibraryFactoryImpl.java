/*    */ package org.kohsuke.rngom.dt;
/*    */ 
/*    */ import org.relaxng.datatype.Datatype;
/*    */ import org.relaxng.datatype.DatatypeBuilder;
/*    */ import org.relaxng.datatype.DatatypeException;
/*    */ import org.relaxng.datatype.DatatypeLibrary;
/*    */ import org.relaxng.datatype.DatatypeLibraryFactory;
/*    */ import org.relaxng.datatype.DatatypeStreamingValidator;
/*    */ import org.relaxng.datatype.ValidationContext;
/*    */ import org.relaxng.datatype.helpers.StreamingValidatorImpl;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class DoNothingDatatypeLibraryFactoryImpl
/*    */   implements DatatypeLibraryFactory
/*    */ {
/*    */   public DatatypeLibrary createDatatypeLibrary(String s) {
/* 20 */     return new DatatypeLibrary()
/*    */       {
/*    */         public Datatype createDatatype(String s) throws DatatypeException {
/* 23 */           return createDatatypeBuilder(s).createDatatype();
/*    */         }
/*    */         
/*    */         public DatatypeBuilder createDatatypeBuilder(String s) throws DatatypeException {
/* 27 */           return new DatatypeBuilder()
/*    */             {
/*    */               public void addParameter(String s, String s1, ValidationContext validationContext) throws DatatypeException {}
/*    */               
/*    */               public Datatype createDatatype() throws DatatypeException {
/* 32 */                 return new Datatype()
/*    */                   {
/*    */                     public boolean isValid(String s, ValidationContext validationContext) {
/* 35 */                       return false;
/*    */                     }
/*    */ 
/*    */                     
/*    */                     public void checkValid(String s, ValidationContext validationContext) throws DatatypeException {}
/*    */                     
/*    */                     public DatatypeStreamingValidator createStreamingValidator(ValidationContext validationContext) {
/* 42 */                       return (DatatypeStreamingValidator)new StreamingValidatorImpl(this, validationContext);
/*    */                     }
/*    */                     
/*    */                     public Object createValue(String s, ValidationContext validationContext) {
/* 46 */                       return null;
/*    */                     }
/*    */                     
/*    */                     public boolean sameValue(Object o, Object o1) {
/* 50 */                       return false;
/*    */                     }
/*    */                     
/*    */                     public int valueHashCode(Object o) {
/* 54 */                       return 0;
/*    */                     }
/*    */                     
/*    */                     public int getIdType() {
/* 58 */                       return 0;
/*    */                     }
/*    */                     
/*    */                     public boolean isContextDependent() {
/* 62 */                       return false;
/*    */                     }
/*    */                   };
/*    */               }
/*    */             };
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\dt\DoNothingDatatypeLibraryFactoryImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */