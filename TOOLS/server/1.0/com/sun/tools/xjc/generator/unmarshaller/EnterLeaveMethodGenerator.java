/*     */ package 1.0.com.sun.tools.xjc.generator.unmarshaller;
/*     */ 
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JInvocation;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JSwitch;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.msv.grammar.NameClass;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.HandlerMethodGenerator;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.PerClassGenerator;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.automaton.Alphabet;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.automaton.State;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.automaton.Transition;
/*     */ 
/*     */ 
/*     */ 
/*     */ class EnterLeaveMethodGenerator
/*     */   extends HandlerMethodGenerator
/*     */ {
/*     */   protected JVar $uri;
/*     */   protected JVar $local;
/*     */   protected JVar $qname;
/*     */   
/*     */   EnterLeaveMethodGenerator(PerClassGenerator parent, String _methodName, Class _alphabetType) {
/*  27 */     super(parent, _methodName, _alphabetType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateAction(Alphabet alpha, Transition tr, JBlock $body) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean performTransition(State state, Alphabet alphabet, Transition action) {
/*  45 */     JBlock $body = getCase(state);
/*     */     
/*  47 */     if (alphabet.isNamed()) {
/*  48 */       $body = $body._if(generateNameClassTest((alphabet.asNamed()).name))._then();
/*     */     }
/*  50 */     generateAction(alphabet, action, $body);
/*     */     
/*  52 */     if (action == Transition.REVERT_TO_PARENT) {
/*  53 */       generateRevertToParent($body);
/*     */     }
/*  55 */     else if (action.alphabet instanceof Alphabet.Reference) {
/*  56 */       generateSpawnChild($body, action);
/*     */     } else {
/*  58 */       generateGoto($body, action.to);
/*  59 */       $body._return();
/*     */     } 
/*     */     
/*  62 */     return alphabet.isNamed();
/*     */   }
/*     */ 
/*     */   
/*     */   protected JSwitch makeSwitch(JMethod method, JBlock body) {
/*  67 */     declareParameters(method);
/*     */     
/*  69 */     if (this.trace)
/*     */     {
/*  71 */       body.invoke((JExpression)this.$tracer, "on" + capitalize()).arg((JExpression)this.$uri).arg((JExpression)this.$local);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  76 */     JSwitch s = super.makeSwitch(method, body);
/*     */ 
/*     */ 
/*     */     
/*  80 */     addParametersToContextSwitch(body.invoke((JExpression)JExpr.ref("super"), method));
/*     */ 
/*     */     
/*  83 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void declareParameters(JMethod method) {
/*  89 */     this.$uri = method.param(String.class, "___uri");
/*  90 */     this.$local = method.param(String.class, "___local");
/*  91 */     this.$qname = method.param(String.class, "___qname");
/*     */   }
/*     */   
/*     */   protected void addParametersToContextSwitch(JInvocation inv) {
/*  95 */     inv.arg((JExpression)this.$uri).arg((JExpression)this.$local).arg((JExpression)this.$qname);
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
/*     */   private JExpression generateNameClassTest(NameClass nc) {
/* 108 */     getSwitch();
/*     */     
/* 110 */     return this.parent.parent.generateNameClassTest(nc, this.$uri, this.$local);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generato\\unmarshaller\EnterLeaveMethodGenerator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */