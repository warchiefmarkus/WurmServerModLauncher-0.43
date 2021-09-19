/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema.ct;
/*    */ 
/*    */ import com.sun.msv.grammar.Expression;
/*    */ import com.sun.tools.xjc.reader.xmlschema.ct.AbstractCTBuilder;
/*    */ import com.sun.tools.xjc.reader.xmlschema.ct.ComplexTypeFieldBuilder;
/*    */ import com.sun.xml.xsom.XSAttContainer;
/*    */ import com.sun.xml.xsom.XSComplexType;
/*    */ import com.sun.xml.xsom.XSComponent;
/*    */ import com.sun.xml.xsom.XSModelGroup;
/*    */ import com.sun.xml.xsom.XSParticle;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChoiceComplexTypeBuilder
/*    */   extends AbstractCTBuilder
/*    */ {
/*    */   public ChoiceComplexTypeBuilder(ComplexTypeFieldBuilder _builder) {
/* 22 */     super(_builder);
/*    */   }
/*    */   
/*    */   public boolean isApplicable(XSComplexType ct) {
/* 26 */     if (!this.bgmBuilder.getGlobalBinding().isModelGroupBinding()) {
/* 27 */       return false;
/*    */     }
/* 29 */     if (ct.getBaseType() != this.bgmBuilder.schemas.getAnyType())
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 35 */       return false;
/*    */     }
/* 37 */     XSParticle p = ct.getContentType().asParticle();
/* 38 */     if (p == null) {
/* 39 */       return false;
/*    */     }
/* 41 */     XSModelGroup mg = getTopLevelModelGroup(p);
/*    */     
/* 43 */     if (mg.getCompositor() != XSModelGroup.CHOICE) {
/* 44 */       return false;
/*    */     }
/* 46 */     if (p.getMaxOccurs() > 1 || p.getMaxOccurs() == -1) {
/* 47 */       return false;
/*    */     }
/* 49 */     return true;
/*    */   }
/*    */   
/*    */   private XSModelGroup getTopLevelModelGroup(XSParticle p) {
/* 53 */     XSModelGroup mg = p.getTerm().asModelGroup();
/* 54 */     if (p.getTerm().isModelGroupDecl())
/* 55 */       mg = p.getTerm().asModelGroupDecl().getModelGroup(); 
/* 56 */     return mg;
/*    */   }
/*    */   
/*    */   public Expression build(XSComplexType ct) {
/* 60 */     XSModelGroup choice = getTopLevelModelGroup(ct.getContentType().asParticle());
/*    */     
/* 62 */     Expression body = this.bgmBuilder.fieldBuilder.build((XSComponent)choice);
/*    */ 
/*    */     
/* 65 */     return this.pool.createSequence(this.bgmBuilder.fieldBuilder.attributeContainer((XSAttContainer)ct), body);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\ct\ChoiceComplexTypeBuilder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */