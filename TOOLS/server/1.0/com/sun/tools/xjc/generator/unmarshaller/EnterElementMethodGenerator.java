/*    */ package 1.0.com.sun.tools.xjc.generator.unmarshaller;
/*    */ 
/*    */ import com.sun.codemodel.JBlock;
/*    */ import com.sun.codemodel.JExpr;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.codemodel.JInvocation;
/*    */ import com.sun.codemodel.JMethod;
/*    */ import com.sun.codemodel.JVar;
/*    */ import com.sun.tools.xjc.generator.XmlNameStoreAlgorithm;
/*    */ import com.sun.tools.xjc.generator.unmarshaller.EnterLeaveMethodGenerator;
/*    */ import com.sun.tools.xjc.generator.unmarshaller.PerClassGenerator;
/*    */ import com.sun.tools.xjc.generator.unmarshaller.automaton.Alphabet;
/*    */ import com.sun.tools.xjc.generator.unmarshaller.automaton.Transition;
/*    */ import org.xml.sax.Attributes;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class EnterElementMethodGenerator
/*    */   extends EnterLeaveMethodGenerator
/*    */ {
/*    */   private JVar $atts;
/*    */   
/*    */   EnterElementMethodGenerator(PerClassGenerator parent) {
/* 26 */     super(parent, "enterElement", Alphabet.EnterElement.class);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void generateAction(Alphabet alpha, Transition tr, JBlock body) {
/* 37 */     if (tr.alphabet == alpha) {
/* 38 */       Alphabet.EnterElement ee = (Alphabet.EnterElement)alpha;
/*    */       
/* 40 */       XmlNameStoreAlgorithm.get(ee.name).onNameUnmarshalled(this.codeModel, body, this.$uri, this.$local);
/*    */       
/* 42 */       body.invoke((JExpression)this.parent.$context, "pushAttributes").arg((JExpression)this.$atts).arg(ee.isDataElement ? JExpr.TRUE : JExpr.FALSE);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void declareParameters(JMethod method) {
/* 48 */     super.declareParameters(method);
/* 49 */     this.$atts = method.param(Attributes.class, "__atts");
/*    */   }
/*    */   
/*    */   protected void addParametersToContextSwitch(JInvocation inv) {
/* 53 */     super.addParametersToContextSwitch(inv);
/* 54 */     inv.arg((JExpression)this.$atts);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void generateSpawnChildFromExternal(JBlock $body, Transition tr, JExpression memento) {
/* 61 */     if (this.trace) {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 69 */       $body.invoke((JExpression)this.$tracer, "onSpawnWildcard");
/* 70 */       $body.invoke((JExpression)this.$tracer, "suspend");
/*    */     } 
/*    */     
/* 73 */     Alphabet.External ae = (Alphabet.External)tr.alphabet;
/*    */     
/* 75 */     JExpression co = ae.owner.generateUnmarshaller(this.parent.parent.context, (JExpression)this.parent.$context, $body, memento, this.$uri, this.$local, this.$qname, this.$atts);
/*    */ 
/*    */ 
/*    */     
/* 79 */     JBlock then = $body._if(co.ne(JExpr._null()))._then();
/*    */     
/* 81 */     ae.field.setter(then, co);
/*    */     
/* 83 */     $body._return();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generato\\unmarshaller\EnterElementMethodGenerator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */