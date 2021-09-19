/*    */ package 1.0.com.sun.tools.xjc.generator.unmarshaller.automaton;
/*    */ 
/*    */ import com.sun.tools.xjc.generator.unmarshaller.automaton.AlphabetVisitor;
/*    */ import com.sun.tools.xjc.generator.unmarshaller.automaton.Transition;
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
/*    */ public abstract class Alphabet
/*    */ {
/*    */   public final int order;
/*    */   
/*    */   public Alphabet(int _order) {
/* 49 */     this.order = _order;
/*    */   }
/*    */ 
/*    */   
/*    */   public final Named asNamed() {
/* 54 */     return (Named)this; }
/* 55 */   public final Reference asReference() { return (Reference)this; }
/* 56 */   public final StaticReference asStaticReference() { return (StaticReference)this; }
/* 57 */   public final Text asText() { return (Text)this; }
/* 58 */   public final BoundText asBoundText() { return (BoundText)this; } public final Dispatch asDispatch() {
/* 59 */     return (Dispatch)this;
/*    */   }
/* 61 */   public final boolean isReference() { return this instanceof Reference; }
/* 62 */   public final boolean isEnterAttribute() { return this instanceof EnterAttribute; }
/* 63 */   public final boolean isLeaveAttribute() { return this instanceof LeaveAttribute; }
/* 64 */   public final boolean isText() { return this instanceof Text; }
/* 65 */   public final boolean isNamed() { return this instanceof Named; }
/* 66 */   public final boolean isBoundText() { return this instanceof BoundText; } public final boolean isDispatch() {
/* 67 */     return this instanceof Dispatch;
/*    */   }
/*    */   
/*    */   public abstract void accept(AlphabetVisitor paramAlphabetVisitor);
/*    */   
/*    */   protected abstract void accept(TransitionVisitor paramTransitionVisitor, Transition paramTransition);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generato\\unmarshaller\automaton\Alphabet.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */