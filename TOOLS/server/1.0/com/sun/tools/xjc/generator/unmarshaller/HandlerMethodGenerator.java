/*     */ package 1.0.com.sun.tools.xjc.generator.unmarshaller;
/*     */ 
/*     */ import com.sun.codemodel.JAssignmentTarget;
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JFieldRef;
/*     */ import com.sun.codemodel.JForLoop;
/*     */ import com.sun.codemodel.JInvocation;
/*     */ import com.sun.codemodel.JLabel;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JSwitch;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.codemodel.JWhileLoop;
/*     */ import com.sun.msv.grammar.NameClass;
/*     */ import com.sun.msv.grammar.SimpleNameClass;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.PerClassGenerator;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.TransitionTable;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.automaton.Alphabet;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.automaton.State;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.automaton.Transition;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Iterator;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
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
/*     */ abstract class HandlerMethodGenerator
/*     */ {
/*     */   protected final PerClassGenerator parent;
/*     */   protected final JCodeModel codeModel;
/*     */   protected final boolean trace;
/*     */   protected final JVar $tracer;
/*     */   protected final String methodName;
/*     */   private final Class alphabetType;
/*     */   protected final TransitionTable table;
/*     */   private JBlock $case;
/*     */   private JSwitch $switch;
/*     */   private JVar $attIdx;
/*     */   private JLabel outerLabel;
/*     */   
/*     */   protected HandlerMethodGenerator(PerClassGenerator _parent, String _mname, Class _alphabetType) {
/*  67 */     this.parent = _parent;
/*  68 */     this.methodName = _mname;
/*  69 */     this.alphabetType = _alphabetType;
/*  70 */     this.codeModel = this.parent.parent.codeModel;
/*  71 */     this.trace = this.parent.parent.trace;
/*  72 */     this.$tracer = this.parent.$tracer;
/*  73 */     this.table = this.parent.transitionTable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JBlock getCase(State source) {
/*  84 */     if (this.$case != null) return this.$case;
/*     */ 
/*     */ 
/*     */     
/*  88 */     this.$case = getSwitch()._case(JExpr.lit(this.parent.automaton.getStateNumber(source))).body();
/*     */ 
/*     */     
/*  91 */     return this.$case;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasCase(State source) {
/*  96 */     return (this.$case != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getNameOfMethodDecl() {
/* 107 */     return this.methodName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JSwitch getSwitch() {
/* 118 */     if (this.$switch != null) return this.$switch;
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
/* 133 */     JMethod method = this.parent.unmarshaller.method(1, (JType)this.codeModel.VOID, getNameOfMethodDecl());
/*     */     
/* 135 */     method._throws(SAXException.class);
/*     */     
/* 137 */     this.$attIdx = method.body().decl((JType)this.codeModel.INT, "attIdx");
/*     */     
/* 139 */     this.outerLabel = method.body().label("outer");
/* 140 */     JWhileLoop w = method.body()._while(JExpr.TRUE);
/*     */     
/* 142 */     this.$switch = makeSwitch(method, w.body());
/*     */     
/* 144 */     w.body()._break();
/*     */     
/* 146 */     return this.$switch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JSwitch makeSwitch(JMethod method, JBlock parentBody) {
/* 156 */     return parentBody._switch((JExpression)this.parent.$state);
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
/*     */   private void onState(State state, TransitionTable table) {
/* 174 */     TransitionTable.Entry[] row = table.list(state);
/*     */     
/* 176 */     boolean canFallThrough = true;
/* 177 */     TransitionTable.Entry catchAll = null;
/*     */     
/* 179 */     for (int i = 0; i < row.length && canFallThrough; i++) {
/* 180 */       Alphabet a = (row[i]).alphabet;
/*     */       
/* 182 */       if (this.alphabetType.isInstance(a)) {
/*     */         
/* 184 */         canFallThrough = performTransition(state, a, (row[i]).transition);
/*     */       }
/* 186 */       else if (a.isEnterAttribute()) {
/* 187 */         buildAttributeCheckClause(getCase(state), state, (Alphabet.EnterAttribute)a, row[i]);
/*     */       }
/* 189 */       else if (a.isDispatch() && this.alphabetType != Alphabet.EnterAttribute.class && this.alphabetType != Alphabet.LeaveAttribute.class) {
/*     */         
/* 191 */         generateDispatch(getCase(state), a.asDispatch(), row[i]);
/*     */       }
/* 193 */       else if (a == Alphabet.EverythingElse.theInstance) {
/*     */         
/* 195 */         catchAll = row[i];
/*     */       } 
/*     */     } 
/*     */     
/* 199 */     if (canFallThrough && catchAll != null) {
/* 200 */       canFallThrough = performTransition(state, catchAll.alphabet, catchAll.transition);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 205 */     if (canFallThrough) {
/* 206 */       if (state.getDelegatedState() != null) {
/*     */         
/* 208 */         generateGoto(getCase(state), state.getDelegatedState());
/*     */ 
/*     */         
/* 211 */         getCase(state)._continue(this.outerLabel);
/*     */       }
/* 213 */       else if (hasCase(state)) {
/*     */         
/* 215 */         getCase(state)._break();
/*     */       } 
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
/*     */   protected final void generate() {
/* 246 */     Iterator itr = this.parent.automaton.states();
/* 247 */     while (itr.hasNext()) {
/* 248 */       this.$case = null;
/* 249 */       onState(itr.next(), this.table);
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
/*     */   protected final String capitalize() {
/* 261 */     return Character.toUpperCase(this.methodName.charAt(0)) + this.methodName.substring(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void generateRevertToParent(JBlock $body) {
/* 269 */     if (this.trace)
/*     */     {
/*     */       
/* 272 */       $body.invoke((JExpression)this.$tracer, "onRevertToParent");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 278 */     JInvocation inv = $body.invoke("revertToParentFrom" + capitalize());
/*     */ 
/*     */     
/* 281 */     addParametersToContextSwitch(inv);
/*     */     
/* 283 */     $body._return();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateSpawnChildFromExternal(JBlock $body, Transition tr, JExpression memento) {
/* 291 */     _assert(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void generateSpawnChild(JBlock $body, Transition tr) {
/* 298 */     _assert(tr.alphabet instanceof Alphabet.Reference);
/*     */     
/* 300 */     JExpression memento = JExpr.lit(this.parent.automaton.getStateNumber(tr.to));
/*     */ 
/*     */     
/* 303 */     if (tr.alphabet instanceof Alphabet.External) {
/* 304 */       generateSpawnChildFromExternal($body, tr, memento);
/*     */     }
/* 306 */     else if (tr.alphabet instanceof Alphabet.Interleave) {
/*     */ 
/*     */       
/* 309 */       Alphabet.Interleave ia = (Alphabet.Interleave)tr.alphabet;
/*     */       
/* 311 */       JInvocation $inv = $body.invoke("spawnHandlerFrom" + capitalize()).arg((JExpression)JExpr._new(this.parent.getInterleaveDispatcher(ia))).arg(memento);
/*     */ 
/*     */ 
/*     */       
/* 315 */       addParametersToContextSwitch($inv);
/* 316 */       $body._return();
/*     */     }
/* 318 */     else if (!tr.alphabet.isDispatch()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 327 */       Alphabet.StaticReference sr = (Alphabet.StaticReference)tr.alphabet;
/*     */ 
/*     */       
/* 330 */       JClass childType = (sr.target.getOwner()).implRef;
/*     */       
/* 332 */       if (tr.alphabet instanceof Alphabet.SuperClass) {
/*     */         
/* 334 */         if (this.trace) {
/*     */ 
/*     */ 
/*     */           
/* 338 */           $body.invoke((JExpression)this.$tracer, "onSpawnSuper").arg(JExpr.lit(childType.name()));
/*     */           
/* 340 */           $body.invoke((JExpression)this.$tracer, "suspend");
/*     */         } 
/*     */         
/* 343 */         JInvocation $inv = $body.invoke("spawnHandlerFrom" + capitalize()).arg(JExpr.direct(MessageFormat.format("(({0}){1}.this).new Unmarshaller(context)", new Object[] { childType.fullName(), this.parent.context.implClass.fullName() }))).arg(memento);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 353 */         addParametersToContextSwitch($inv);
/* 354 */         $body._return();
/*     */       } else {
/* 356 */         Alphabet.Child c = (Alphabet.Child)tr.alphabet;
/*     */         
/* 358 */         if (this.trace) {
/*     */ 
/*     */ 
/*     */           
/* 362 */           $body.invoke((JExpression)this.$tracer, "onSpawnChild").arg(JExpr.lit(childType.name())).arg(JExpr.lit((c.field.getFieldUse()).name));
/*     */ 
/*     */           
/* 365 */           $body.invoke((JExpression)this.$tracer, "suspend");
/*     */         } 
/*     */         
/* 368 */         JInvocation $childObj = JExpr.invoke("spawnChildFrom" + capitalize()).arg(JExpr.dotclass(childType)).arg(memento);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 373 */         addParametersToContextSwitch($childObj);
/*     */         
/* 375 */         c.field.setter($body, (JExpression)JExpr.cast((JType)childType, (JExpression)$childObj));
/*     */         
/* 377 */         $body._return();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final void generateGoto(JBlock $body, State target) {
/* 383 */     this.parent.generateGoto($body, target);
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
/*     */   private void buildAttributeCheckClause(JBlock body, State current, Alphabet.EnterAttribute alphabet, TransitionTable.Entry tte) {
/* 396 */     NameClass nc = alphabet.name;
/*     */     
/* 398 */     JFieldRef jFieldRef = JExpr.ref("context");
/*     */     
/* 400 */     if (nc instanceof SimpleNameClass) {
/*     */ 
/*     */       
/* 403 */       SimpleNameClass snc = (SimpleNameClass)nc;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 408 */       body.assign((JAssignmentTarget)this.$attIdx, (JExpression)jFieldRef.invoke("getAttribute").arg(JExpr.lit(snc.namespaceURI)).arg(JExpr.lit(snc.localName)));
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 415 */       JBlock b = body.block();
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
/* 427 */       JVar $a = b.decl((JType)this.codeModel.ref(Attributes.class), "a", (JExpression)jFieldRef.invoke("getUnconsumedAttributes"));
/*     */ 
/*     */ 
/*     */       
/* 431 */       JForLoop loop = b._for();
/* 432 */       loop.init(this.$attIdx, JExpr.invoke((JExpression)$a, "getLength").minus(JExpr.lit(1)));
/*     */       
/* 434 */       loop.test(this.$attIdx.gte(JExpr.lit(0)));
/* 435 */       loop.update(this.$attIdx.decr());
/*     */       
/* 437 */       JClass jClass = this.codeModel.ref(String.class);
/* 438 */       JVar $uri = loop.body().decl((JType)jClass, "uri", (JExpression)$a.invoke("getURI").arg((JExpression)this.$attIdx));
/* 439 */       JVar $local = loop.body().decl((JType)jClass, "local", (JExpression)$a.invoke("getLocalName").arg((JExpression)this.$attIdx));
/*     */       
/* 441 */       loop.body()._if(this.parent.parent.generateNameClassTest(nc, $uri, $local))._then()._break();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 449 */     JBlock _then = body._if(this.$attIdx.gte(JExpr.lit(0)))._then();
/*     */ 
/*     */     
/* 452 */     AttOptimizeInfo aoi = calcOptimizableAttribute(tte);
/* 453 */     if (aoi == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 461 */       _then.invoke((JExpression)jFieldRef, "consumeAttribute").arg((JExpression)this.$attIdx);
/*     */       
/* 463 */       addParametersToContextSwitch(_then.invoke((JExpression)jFieldRef.invoke("getCurrentHandler"), this.methodName));
/*     */       
/* 465 */       _then._return();
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */       
/* 474 */       JVar $v = _then.decl(8, (JType)this.codeModel.ref(String.class), "v", (JExpression)jFieldRef.invoke("eatAttribute").arg((JExpression)this.$attIdx));
/*     */       
/* 476 */       this.parent.eatText(_then, aoi.valueHandler, (JExpression)$v);
/* 477 */       generateGoto(_then, aoi.nextState);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 483 */       if ((aoi.nextState.isListState ^ current.isListState) != 0) {
/* 484 */         addParametersToContextSwitch(_then.invoke((JExpression)jFieldRef.invoke("getCurrentHandler"), this.methodName));
/*     */         
/* 486 */         _then._return();
/*     */       } else {
/* 488 */         _then._continue(this.outerLabel);
/*     */       } 
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
/*     */   private void generateDispatch(JBlock $body, Alphabet.Dispatch da, TransitionTable.Entry tte) {
/* 509 */     JBlock block = $body.block();
/*     */     
/* 511 */     JVar $childType = block.decl((JType)this.codeModel.ref(Class.class), "child", this.parent.invokeLookup(da, tte));
/*     */ 
/*     */     
/* 514 */     block = block._if($childType.ne(JExpr._null()))._then();
/*     */     
/* 516 */     if (this.trace) {
/*     */ 
/*     */ 
/*     */       
/* 520 */       block.invoke((JExpression)this.$tracer, "onSpawnChild").arg(JExpr.lit('{' + da.attName.namespaceURI + '}' + da.attName.localName)).arg(JExpr.lit((da.field.getFieldUse()).name));
/*     */ 
/*     */       
/* 523 */       block.invoke((JExpression)this.$tracer, "suspend");
/*     */     } 
/*     */     
/* 526 */     JInvocation $childObj = JExpr.invoke("spawnChildFrom" + capitalize()).arg((JExpression)$childType).arg(JExpr.lit(this.parent.automaton.getStateNumber(tte.transition.to)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 531 */     addParametersToContextSwitch($childObj);
/*     */     
/* 533 */     da.field.setter(block, (JExpression)JExpr.cast((da.field.getFieldUse()).type, (JExpression)$childObj));
/*     */     
/* 535 */     block._return();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AttOptimizeInfo calcOptimizableAttribute(TransitionTable.Entry tte) {
/* 584 */     if (!tte.transition.alphabet.isEnterAttribute()) {
/* 585 */       return null;
/*     */     }
/* 587 */     Transition[] hop1 = tte.transition.to.listTransitions();
/* 588 */     if (hop1.length != 1) {
/* 589 */       return null;
/*     */     }
/* 591 */     Transition t1 = hop1[0];
/* 592 */     if (!t1.alphabet.isBoundText()) {
/* 593 */       return null;
/*     */     }
/* 595 */     Transition[] hop2 = t1.to.listTransitions();
/* 596 */     if (hop2.length != 1) {
/* 597 */       return null;
/*     */     }
/* 599 */     Transition t2 = hop2[0];
/* 600 */     if (!t2.alphabet.isLeaveAttribute()) {
/* 601 */       return null;
/*     */     }
/* 603 */     return new AttOptimizeInfo(t1.alphabet.asBoundText(), t2.to);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static final void _assert(boolean b) {
/* 608 */     if (!b)
/* 609 */       throw new JAXBAssertionError(); 
/*     */   }
/*     */   
/*     */   protected abstract boolean performTransition(State paramState, Alphabet paramAlphabet, Transition paramTransition);
/*     */   
/*     */   protected abstract void addParametersToContextSwitch(JInvocation paramJInvocation);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generato\\unmarshaller\HandlerMethodGenerator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */