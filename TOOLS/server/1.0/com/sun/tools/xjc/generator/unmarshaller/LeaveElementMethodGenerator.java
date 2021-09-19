/*    */ package 1.0.com.sun.tools.xjc.generator.unmarshaller;
/*    */ 
/*    */ import com.sun.codemodel.JBlock;
/*    */ import com.sun.codemodel.JExpression;
/*    */ import com.sun.tools.xjc.generator.unmarshaller.EnterLeaveMethodGenerator;
/*    */ import com.sun.tools.xjc.generator.unmarshaller.PerClassGenerator;
/*    */ import com.sun.tools.xjc.generator.unmarshaller.automaton.Alphabet;
/*    */ import com.sun.tools.xjc.generator.unmarshaller.automaton.Transition;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class LeaveElementMethodGenerator
/*    */   extends EnterLeaveMethodGenerator
/*    */ {
/*    */   LeaveElementMethodGenerator(PerClassGenerator parent) {
/* 17 */     super(parent, "leaveElement", Alphabet.LeaveElement.class);
/*    */   }
/*    */   
/*    */   protected void generateAction(Alphabet alpha, Transition tr, JBlock body) {
/* 21 */     if (tr.alphabet == alpha)
/* 22 */       body.invoke((JExpression)this.parent.$context, "popAttributes"); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generato\\unmarshaller\LeaveElementMethodGenerator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */