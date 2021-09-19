/*    */ package org.kohsuke.rngom.ast.builder;
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
/*    */ public interface GrammarSection<P extends org.kohsuke.rngom.ast.om.ParsedPattern, E extends org.kohsuke.rngom.ast.om.ParsedElementAnnotation, L extends org.kohsuke.rngom.ast.om.Location, A extends Annotations<E, L, CL>, CL extends CommentList<L>>
/*    */ {
/*    */   public static final class Combine
/*    */   {
/*    */     private final String name;
/*    */     
/*    */     private Combine(String name) {
/* 22 */       this.name = name;
/*    */     }
/*    */     public final String toString() {
/* 25 */       return this.name;
/*    */     }
/*    */   }
/*    */   
/* 29 */   public static final Combine COMBINE_CHOICE = new Combine("choice");
/* 30 */   public static final Combine COMBINE_INTERLEAVE = new Combine("interleave");
/*    */   public static final String START = "\000#start\000";
/*    */   
/*    */   void define(String paramString, Combine paramCombine, P paramP, L paramL, A paramA) throws BuildException;
/*    */   
/*    */   void topLevelAnnotation(E paramE) throws BuildException;
/*    */   
/*    */   void topLevelComment(CL paramCL) throws BuildException;
/*    */   
/*    */   Div<P, E, L, A, CL> makeDiv();
/*    */   
/*    */   Include<P, E, L, A, CL> makeInclude();
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\ast\builder\GrammarSection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */