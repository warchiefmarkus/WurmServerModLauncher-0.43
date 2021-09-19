/*     */ package 1.0.com.sun.tools.xjc.generator.unmarshaller.automaton;
/*     */ 
/*     */ import com.sun.tools.xjc.generator.unmarshaller.automaton.Alphabet;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.automaton.Transition;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.automaton.TransitionVisitor;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class State
/*     */ {
/*     */   public boolean isListState = false;
/*     */   private boolean isFinalState = false;
/*  32 */   private final Set transitions = new HashSet();
/*     */ 
/*     */   
/*     */   private com.sun.tools.xjc.generator.unmarshaller.automaton.State delegatedState;
/*     */ 
/*     */   
/*     */   public com.sun.tools.xjc.generator.unmarshaller.automaton.State getDelegatedState() {
/*  39 */     return this.delegatedState;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDelegatedState(com.sun.tools.xjc.generator.unmarshaller.automaton.State _delegatedState) {
/*  47 */     com.sun.tools.xjc.generator.unmarshaller.automaton.State s = _delegatedState;
/*  48 */     while (s != null) {
/*  49 */       if (s == this) {
/*  50 */         absorb(_delegatedState);
/*     */         return;
/*     */       } 
/*  53 */       s = s.delegatedState;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  58 */     if (this.isFinalState && !_delegatedState.isFinalState) {
/*  59 */       absorb(_delegatedState);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  64 */     if (this.delegatedState == null) {
/*  65 */       this.delegatedState = _delegatedState;
/*  66 */       this.isListState |= this.delegatedState.isListState;
/*     */     }
/*     */     else {
/*     */       
/*  70 */       absorb(_delegatedState);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addTransition(Transition t) {
/*  75 */     this.transitions.add(t);
/*     */   }
/*     */   public Iterator transitions() {
/*  78 */     return this.transitions.iterator();
/*     */   }
/*     */   public Transition[] listTransitions() {
/*  81 */     return (Transition[])this.transitions.toArray((Object[])new Transition[this.transitions.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void acceptForEachTransition(TransitionVisitor visitor) {
/*  88 */     for (Iterator itr = this.transitions.iterator(); itr.hasNext();) {
/*  89 */       ((Transition)itr.next()).accept(visitor);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void absorb(com.sun.tools.xjc.generator.unmarshaller.automaton.State rhs) {
/* 130 */     this.transitions.addAll(rhs.transitions);
/* 131 */     this.isListState |= rhs.isListState;
/* 132 */     if (rhs.isFinalState) {
/* 133 */       markAsFinalState();
/*     */     }
/* 135 */     if (rhs.delegatedState != null) {
/* 136 */       setDelegatedState(rhs.delegatedState);
/*     */     }
/*     */   }
/*     */   
/*     */   public Set head() {
/* 141 */     HashSet s = new HashSet();
/* 142 */     head(s, new HashSet(), true);
/* 143 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void head(Set result, Set visitedStates, boolean includeEE) {
/* 152 */     if (!visitedStates.add(this)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 157 */     if (this.isFinalState && includeEE) {
/* 158 */       result.add(Alphabet.EverythingElse.theInstance);
/*     */     }
/* 160 */     for (Iterator itr = this.transitions.iterator(); itr.hasNext(); ) {
/* 161 */       Transition t = itr.next();
/* 162 */       t.head(result, visitedStates, includeEE);
/*     */     } 
/*     */     
/* 165 */     if (this.delegatedState != null) {
/* 166 */       this.delegatedState.head(result, visitedStates, includeEE);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasTransition() {
/* 173 */     return !this.transitions.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFinalState() {
/* 178 */     return this.isFinalState;
/*     */   }
/*     */   
/*     */   public void markAsFinalState() {
/* 182 */     this.isFinalState = true;
/*     */ 
/*     */ 
/*     */     
/* 186 */     if (this.delegatedState != null && !this.delegatedState.isFinalState) {
/* 187 */       com.sun.tools.xjc.generator.unmarshaller.automaton.State p = this.delegatedState;
/* 188 */       this.delegatedState = null;
/* 189 */       absorb(p);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generato\\unmarshaller\automaton\State.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */