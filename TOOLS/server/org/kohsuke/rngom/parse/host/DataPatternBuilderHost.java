/*    */ package org.kohsuke.rngom.parse.host;
/*    */ 
/*    */ import org.kohsuke.rngom.ast.builder.Annotations;
/*    */ import org.kohsuke.rngom.ast.builder.BuildException;
/*    */ import org.kohsuke.rngom.ast.builder.DataPatternBuilder;
/*    */ import org.kohsuke.rngom.ast.om.Location;
/*    */ import org.kohsuke.rngom.ast.om.ParsedElementAnnotation;
/*    */ import org.kohsuke.rngom.ast.om.ParsedPattern;
/*    */ import org.kohsuke.rngom.parse.Context;
/*    */ 
/*    */ 
/*    */ 
/*    */ final class DataPatternBuilderHost
/*    */   extends Base
/*    */   implements DataPatternBuilder
/*    */ {
/*    */   final DataPatternBuilder lhs;
/*    */   final DataPatternBuilder rhs;
/*    */   
/*    */   DataPatternBuilderHost(DataPatternBuilder lhs, DataPatternBuilder rhs) {
/* 21 */     this.lhs = lhs;
/* 22 */     this.rhs = rhs;
/*    */   }
/*    */   
/*    */   public void addParam(String name, String value, Context context, String ns, Location _loc, Annotations _anno) throws BuildException {
/* 26 */     LocationHost loc = cast(_loc);
/* 27 */     AnnotationsHost anno = cast(_anno);
/*    */     
/* 29 */     this.lhs.addParam(name, value, context, ns, loc.lhs, anno.lhs);
/* 30 */     this.rhs.addParam(name, value, context, ns, loc.rhs, anno.rhs);
/*    */   }
/*    */   
/*    */   public void annotation(ParsedElementAnnotation _ea) {
/* 34 */     ParsedElementAnnotationHost ea = (ParsedElementAnnotationHost)_ea;
/*    */     
/* 36 */     this.lhs.annotation(ea.lhs);
/* 37 */     this.rhs.annotation(ea.rhs);
/*    */   }
/*    */   
/*    */   public ParsedPattern makePattern(Location _loc, Annotations _anno) throws BuildException {
/* 41 */     LocationHost loc = cast(_loc);
/* 42 */     AnnotationsHost anno = cast(_anno);
/*    */     
/* 44 */     return new ParsedPatternHost(this.lhs.makePattern(loc.lhs, anno.lhs), this.rhs.makePattern(loc.rhs, anno.rhs));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ParsedPattern makePattern(ParsedPattern _except, Location _loc, Annotations _anno) throws BuildException {
/* 50 */     ParsedPatternHost except = (ParsedPatternHost)_except;
/* 51 */     LocationHost loc = cast(_loc);
/* 52 */     AnnotationsHost anno = cast(_anno);
/*    */     
/* 54 */     return new ParsedPatternHost(this.lhs.makePattern(except.lhs, loc.lhs, anno.lhs), this.rhs.makePattern(except.rhs, loc.rhs, anno.rhs));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\parse\host\DataPatternBuilderHost.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */