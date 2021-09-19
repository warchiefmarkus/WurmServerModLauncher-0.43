/*    */ package 1.0.com.sun.tools.xjc.generator.unmarshaller.automaton;
/*    */ 
/*    */ import com.sun.tools.xjc.generator.unmarshaller.automaton.Alphabet;
/*    */ import com.sun.tools.xjc.generator.unmarshaller.automaton.State;
/*    */ import com.sun.tools.xjc.generator.unmarshaller.automaton.TransitionVisitor;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Transition
/*    */ {
/* 14 */   public static final com.sun.tools.xjc.generator.unmarshaller.automaton.Transition REVERT_TO_PARENT = new com.sun.tools.xjc.generator.unmarshaller.automaton.Transition(null, null);
/*    */   
/*    */   public final Alphabet alphabet;
/*    */   
/*    */   public final State to;
/*    */ 
/*    */   
/*    */   public Transition(Alphabet _alphabet, State _to) {
/* 22 */     this.alphabet = _alphabet;
/* 23 */     this.to = _to;
/*    */   }
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
/*    */   public Set head(State sourceState) {
/* 36 */     HashSet s = new HashSet();
/*    */     
/* 38 */     HashSet visited = new HashSet();
/* 39 */     visited.add(sourceState);
/*    */     
/* 41 */     head(s, visited, true);
/* 42 */     return s;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void head(Set result, Set visitedStates, boolean includeEE) {
/* 51 */     result.add(this.alphabet);
/*    */     
/* 53 */     if (!(this.alphabet instanceof Alphabet.Reference)) {
/*    */       return;
/*    */     }
/*    */ 
/*    */     
/* 58 */     Alphabet.Reference ref = this.alphabet.asReference();
/*    */     
/* 60 */     if (ref.isNullable())
/* 61 */       this.to.head(result, visitedStates, includeEE); 
/*    */   }
/*    */   
/*    */   public void accept(TransitionVisitor visitor) {
/* 65 */     this.alphabet.accept(visitor, this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generato\\unmarshaller\automaton\Transition.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */