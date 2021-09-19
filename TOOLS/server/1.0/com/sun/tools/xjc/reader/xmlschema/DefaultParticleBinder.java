/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema;
/*    */ 
/*    */ import com.sun.msv.grammar.Expression;
/*    */ import com.sun.tools.xjc.grammar.ClassItem;
/*    */ import com.sun.tools.xjc.grammar.FieldItem;
/*    */ import com.sun.tools.xjc.grammar.util.Multiplicity;
/*    */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*    */ import com.sun.tools.xjc.reader.xmlschema.Messages;
/*    */ import com.sun.tools.xjc.reader.xmlschema.ParticleBinder;
/*    */ import com.sun.xml.xsom.XSComponent;
/*    */ import com.sun.xml.xsom.XSParticle;
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
/*    */ class DefaultParticleBinder
/*    */   extends ParticleBinder
/*    */ {
/*    */   DefaultParticleBinder(BGMBuilder builder) {
/* 43 */     super(builder);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Expression build(XSParticle p, ClassItem superClass) {
/* 49 */     Checker checker = new Checker(this, null);
/*    */     
/* 51 */     if (superClass != null)
/* 52 */       checker.readSuperClass(superClass); 
/* 53 */     checker.particle(p);
/*    */     
/* 55 */     if (checker.hasNameCollision()) {
/*    */ 
/*    */       
/* 58 */       FieldItem fi = new FieldItem((superClass == null) ? "Content" : "Rest", this.builder.typeBuilder.build((XSComponent)p), p.getLocator());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 64 */       fi.multiplicity = Multiplicity.star;
/* 65 */       fi.collisionExpected = true;
/*    */       
/* 67 */       fi.javadoc = Messages.format("DefaultParticleBinder.FallbackJavadoc", checker.getCollisionInfo().toString());
/*    */ 
/*    */       
/* 70 */       return (Expression)fi;
/*    */     } 
/* 72 */     return (new Builder(this, checker.markedParticles)).build(p);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean checkFallback(XSParticle p, ClassItem superClass) {
/* 78 */     Checker checker = new Checker(this, null);
/*    */     
/* 80 */     if (superClass != null)
/* 81 */       checker.readSuperClass(superClass); 
/* 82 */     checker.particle(p);
/*    */     
/* 84 */     return checker.hasNameCollision();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\DefaultParticleBinder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */