/*    */ package 1.0.com.sun.tools.xjc.generator.unmarshaller.automaton;
/*    */ 
/*    */ import com.sun.tools.xjc.generator.unmarshaller.automaton.Alphabet;
/*    */ import com.sun.tools.xjc.generator.unmarshaller.automaton.State;
/*    */ import com.sun.tools.xjc.generator.unmarshaller.automaton.TransitionVisitor;
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
/*    */ public class AbstractTransitionVisitorImpl
/*    */   implements TransitionVisitor
/*    */ {
/*    */   public void onEnterElement(Alphabet.EnterElement a, State to) {
/* 28 */     onNamed((Alphabet.Named)a, to);
/*    */   }
/*    */   
/*    */   public void onLeaveElement(Alphabet.LeaveElement a, State to) {
/* 32 */     onNamed((Alphabet.Named)a, to);
/*    */   }
/*    */   
/*    */   public void onEnterAttribute(Alphabet.EnterAttribute a, State to) {
/* 36 */     onNamed((Alphabet.Named)a, to);
/*    */   }
/*    */   
/*    */   public void onLeaveAttribute(Alphabet.LeaveAttribute a, State to) {
/* 40 */     onNamed((Alphabet.Named)a, to);
/*    */   }
/*    */   
/*    */   protected void onNamed(Alphabet.Named a, State to) {
/* 44 */     onAlphabet((Alphabet)a, to);
/*    */   }
/*    */   
/*    */   public void onInterleave(Alphabet.Interleave a, State to) {
/* 48 */     onRef((Alphabet.Reference)a, to);
/*    */   }
/*    */   
/*    */   public void onChild(Alphabet.Child a, State to) {
/* 52 */     onRef((Alphabet.Reference)a, to);
/*    */   }
/*    */   
/*    */   public void onDispatch(Alphabet.Dispatch a, State to) {
/* 56 */     onAlphabet((Alphabet)a, to);
/*    */   }
/*    */   
/*    */   public void onSuper(Alphabet.SuperClass a, State to) {
/* 60 */     onRef((Alphabet.Reference)a, to);
/*    */   }
/*    */   
/*    */   public void onExternal(Alphabet.External a, State to) {
/* 64 */     onRef((Alphabet.Reference)a, to);
/*    */   }
/*    */   
/*    */   protected void onRef(Alphabet.Reference a, State to) {
/* 68 */     onAlphabet((Alphabet)a, to);
/*    */   }
/*    */   
/*    */   public void onBoundText(Alphabet.BoundText a, State to) {
/* 72 */     onText((Alphabet.Text)a, to);
/*    */   }
/*    */   
/*    */   public void onIgnoredText(Alphabet.IgnoredText a, State to) {
/* 76 */     onText((Alphabet.Text)a, to);
/*    */   }
/*    */   
/*    */   protected void onText(Alphabet.Text a, State to) {
/* 80 */     onAlphabet((Alphabet)a, to);
/*    */   }
/*    */   
/*    */   public void onEverythingElse(Alphabet.EverythingElse a, State to) {
/* 84 */     onAlphabet((Alphabet)a, to);
/*    */   }
/*    */   
/*    */   protected void onAlphabet(Alphabet a, State to) {}
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generato\\unmarshaller\automaton\AbstractTransitionVisitorImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */