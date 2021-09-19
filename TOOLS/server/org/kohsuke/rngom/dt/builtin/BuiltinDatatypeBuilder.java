/*    */ package org.kohsuke.rngom.dt.builtin;
/*    */ 
/*    */ import org.kohsuke.rngom.util.Localizer;
/*    */ import org.relaxng.datatype.Datatype;
/*    */ import org.relaxng.datatype.DatatypeBuilder;
/*    */ import org.relaxng.datatype.DatatypeException;
/*    */ import org.relaxng.datatype.ValidationContext;
/*    */ 
/*    */ class BuiltinDatatypeBuilder
/*    */   implements DatatypeBuilder
/*    */ {
/*    */   private final Datatype dt;
/* 13 */   private static final Localizer localizer = new Localizer(BuiltinDatatypeBuilder.class);
/*    */   
/*    */   BuiltinDatatypeBuilder(Datatype dt) {
/* 16 */     this.dt = dt;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addParameter(String name, String value, ValidationContext context) throws DatatypeException {
/* 22 */     throw new DatatypeException(localizer.message("builtin_param"));
/*    */   }
/*    */   
/*    */   public Datatype createDatatype() {
/* 26 */     return this.dt;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\dt\builtin\BuiltinDatatypeBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */