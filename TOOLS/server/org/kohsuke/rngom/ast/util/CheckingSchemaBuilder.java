/*    */ package org.kohsuke.rngom.ast.util;
/*    */ 
/*    */ import org.kohsuke.rngom.ast.builder.BuildException;
/*    */ import org.kohsuke.rngom.ast.builder.SchemaBuilder;
/*    */ import org.kohsuke.rngom.ast.om.ParsedPattern;
/*    */ import org.kohsuke.rngom.binary.SchemaBuilderImpl;
/*    */ import org.kohsuke.rngom.binary.SchemaPatternBuilder;
/*    */ import org.kohsuke.rngom.parse.IllegalSchemaException;
/*    */ import org.kohsuke.rngom.parse.host.ParsedPatternHost;
/*    */ import org.kohsuke.rngom.parse.host.SchemaBuilderHost;
/*    */ import org.relaxng.datatype.DatatypeLibraryFactory;
/*    */ import org.xml.sax.ErrorHandler;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CheckingSchemaBuilder
/*    */   extends SchemaBuilderHost
/*    */ {
/*    */   public CheckingSchemaBuilder(SchemaBuilder sb, ErrorHandler eh) {
/* 45 */     super((SchemaBuilder)new SchemaBuilderImpl(eh), sb);
/*    */   }
/*    */   public CheckingSchemaBuilder(SchemaBuilder sb, ErrorHandler eh, DatatypeLibraryFactory dlf) {
/* 48 */     super((SchemaBuilder)new SchemaBuilderImpl(eh, dlf, new SchemaPatternBuilder()), sb);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ParsedPattern expandPattern(ParsedPattern p) throws BuildException, IllegalSchemaException {
/* 55 */     ParsedPatternHost r = (ParsedPatternHost)super.expandPattern(p);
/* 56 */     return r.rhs;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\as\\util\CheckingSchemaBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */