/*     */ package 1.0.com.sun.tools.xjc.generator.unmarshaller.automaton;
/*     */ 
/*     */ import com.sun.msv.grammar.ExpressionPool;
/*     */ import com.sun.tools.xjc.generator.ClassContext;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.automaton.State;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
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
/*     */ public final class Automaton
/*     */ {
/*     */   private final ClassContext owner;
/*     */   private State initial;
/*  27 */   private Boolean nullable = null;
/*     */ 
/*     */   
/*  30 */   private final Map states = new HashMap();
/*  31 */   private int iota = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Automaton(ClassContext _owner) {
/*  42 */     this.owner = _owner;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInitialState(State _initialState) {
/*  49 */     if (this.initial != null)
/*     */     {
/*  51 */       throw new JAXBAssertionError();
/*     */     }
/*  53 */     this.initial = _initialState;
/*     */ 
/*     */     
/*  56 */     (new StateEnumerator(this, null)).visit(this.initial);
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
/*     */   public State getInitialState() {
/*  82 */     return this.initial;
/*     */   }
/*     */   public int getStateNumber(State s) {
/*  85 */     return ((Integer)this.states.get(s)).intValue();
/*     */   }
/*     */   
/*     */   public int getStateSize() {
/*  89 */     return this.states.size();
/*     */   }
/*     */   public Iterator states() {
/*  92 */     return this.states.keySet().iterator();
/*     */   }
/*     */   public ClassContext getOwner() {
/*  95 */     return this.owner;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isNullable() {
/* 100 */     if (this.nullable == null) {
/*     */       
/* 102 */       ExpressionPool pool = new ExpressionPool();
/* 103 */       if (this.owner.target.exp.getExpandedExp(pool).isEpsilonReducible()) {
/* 104 */         this.nullable = Boolean.TRUE;
/*     */       } else {
/* 106 */         this.nullable = Boolean.FALSE;
/*     */       } 
/*     */     } 
/* 109 */     return this.nullable.booleanValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generato\\unmarshaller\automaton\Automaton.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */