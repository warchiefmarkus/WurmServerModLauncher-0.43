/*    */ package 1.0.com.sun.tools.xjc.generator.unmarshaller;
/*    */ 
/*    */ import com.sun.codemodel.JBlock;
/*    */ import com.sun.tools.xjc.generator.XmlNameStoreAlgorithm;
/*    */ import com.sun.tools.xjc.generator.unmarshaller.EnterLeaveMethodGenerator;
/*    */ import com.sun.tools.xjc.generator.unmarshaller.PerClassGenerator;
/*    */ import com.sun.tools.xjc.generator.unmarshaller.automaton.Alphabet;
/*    */ import com.sun.tools.xjc.generator.unmarshaller.automaton.Transition;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class EnterAttributeMethodGenerator
/*    */   extends EnterLeaveMethodGenerator
/*    */ {
/*    */   public EnterAttributeMethodGenerator(PerClassGenerator parent) {
/* 19 */     super(parent, "enterAttribute", Alphabet.EnterAttribute.class);
/*    */   }
/*    */   
/*    */   protected void generateAction(Alphabet alpha, Transition tr, JBlock $body) {
/* 23 */     if (tr.alphabet == alpha) {
/* 24 */       Alphabet.EnterAttribute ea = (Alphabet.EnterAttribute)alpha;
/*    */ 
/*    */       
/* 27 */       XmlNameStoreAlgorithm.get(ea.name).onNameUnmarshalled(this.codeModel, $body, this.$uri, this.$local);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generato\\unmarshaller\EnterAttributeMethodGenerator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */