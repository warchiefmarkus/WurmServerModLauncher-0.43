/*    */ package org.kohsuke.rngom.nc;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.kohsuke.rngom.ast.builder.Annotations;
/*    */ import org.kohsuke.rngom.ast.builder.BuildException;
/*    */ import org.kohsuke.rngom.ast.builder.CommentList;
/*    */ import org.kohsuke.rngom.ast.builder.NameClassBuilder;
/*    */ import org.kohsuke.rngom.ast.om.Location;
/*    */ import org.kohsuke.rngom.ast.om.ParsedElementAnnotation;
/*    */ import org.kohsuke.rngom.ast.om.ParsedNameClass;
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
/*    */ public class NameClassBuilderImpl<E extends ParsedElementAnnotation, L extends Location, A extends Annotations<E, L, CL>, CL extends CommentList<L>>
/*    */   implements NameClassBuilder<NameClass, E, L, A, CL>
/*    */ {
/*    */   public NameClass makeChoice(List<NameClass> nameClasses, L loc, A anno) {
/* 25 */     NameClass result = nameClasses.get(0);
/* 26 */     for (int i = 1; i < nameClasses.size(); i++)
/* 27 */       result = new ChoiceNameClass(result, nameClasses.get(i)); 
/* 28 */     return result;
/*    */   }
/*    */   
/*    */   public NameClass makeName(String ns, String localName, String prefix, L loc, A anno) {
/* 32 */     return new SimpleNameClass(ns, localName);
/*    */   }
/*    */   
/*    */   public NameClass makeNsName(String ns, L loc, A anno) {
/* 36 */     return new NsNameClass(ns);
/*    */   }
/*    */   
/*    */   public NameClass makeNsName(String ns, NameClass except, L loc, A anno) {
/* 40 */     return new NsNameExceptNameClass(ns, except);
/*    */   }
/*    */   
/*    */   public NameClass makeAnyName(L loc, A anno) {
/* 44 */     return NameClass.ANY;
/*    */   }
/*    */   
/*    */   public NameClass makeAnyName(NameClass except, L loc, A anno) {
/* 48 */     return new AnyNameExceptNameClass(except);
/*    */   }
/*    */   
/*    */   public NameClass makeErrorNameClass() {
/* 52 */     return NameClass.NULL;
/*    */   }
/*    */   
/*    */   public NameClass annotate(NameClass nc, A anno) throws BuildException {
/* 56 */     return nc;
/*    */   }
/*    */   
/*    */   public NameClass annotateAfter(NameClass nc, E e) throws BuildException {
/* 60 */     return nc;
/*    */   }
/*    */   
/*    */   public NameClass commentAfter(NameClass nc, CL comments) throws BuildException {
/* 64 */     return nc;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\nc\NameClassBuilderImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */