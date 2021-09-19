/*     */ package 1.0.com.sun.tools.xjc.generator.unmarshaller;
/*     */ 
/*     */ import com.sun.codemodel.JAssignmentTarget;
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JCatchBlock;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.JClassContainer;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JConditional;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JFieldRef;
/*     */ import com.sun.codemodel.JInvocation;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JTryBlock;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.msv.grammar.NameClass;
/*     */ import com.sun.tools.xjc.generator.ClassContext;
/*     */ import com.sun.tools.xjc.generator.XMLDeserializerContextImpl;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.EnterAttributeMethodGenerator;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.EnterElementMethodGenerator;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.EnterLeaveMethodGenerator;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.LeaveElementMethodGenerator;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.TextMethodGenerator;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.TransitionTable;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.UnmarshallerGenerator;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.automaton.Alphabet;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.automaton.Automaton;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.automaton.State;
/*     */ import com.sun.tools.xjc.grammar.xducer.DeserializerContext;
/*     */ import com.sun.tools.xjc.grammar.xducer.TypeAdaptedTransducer;
/*     */ import com.sun.tools.xjc.runtime.AbstractUnmarshallingEventHandlerImpl;
/*     */ import com.sun.tools.xjc.runtime.InterleaveDispatcher;
/*     */ import com.sun.tools.xjc.runtime.UnmarshallableObject;
/*     */ import com.sun.tools.xjc.runtime.UnmarshallingContext;
/*     */ import com.sun.tools.xjc.runtime.UnmarshallingEventHandler;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import com.sun.xml.bind.unmarshaller.Tracer;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
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
/*     */ class PerClassGenerator
/*     */ {
/*     */   final UnmarshallerGenerator parent;
/*     */   private final JCodeModel codeModel;
/*     */   final ClassContext context;
/*     */   final Automaton automaton;
/*     */   final JDefinedClass unmarshaller;
/*     */   final JFieldRef $state;
/*     */   final JFieldRef $context;
/*     */   private final DeserializerContext dc;
/*     */   final TransitionTable transitionTable;
/*     */   JVar $tracer;
/*  78 */   private int idGen = 0;
/*     */   
/*     */   public int createId() {
/*  81 */     return ++this.idGen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   private final Map interleaveDispatcherImpls = new HashMap();
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
/*     */   private final Map eatTextFunctions;
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
/*     */   private final Map dispatchLookupFunctions;
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
/*     */   protected void generate() {
/* 161 */     JMethod con = this.unmarshaller.constructor(1);
/* 162 */     JVar $context = con.param((JType)getRuntime(UnmarshallingContext.class), "context");
/* 163 */     con.body().invoke("super").arg((JExpression)$context).arg(JExpr.lit(generateEncodedTextType()));
/*     */ 
/*     */ 
/*     */     
/* 167 */     if (this.parent.trace) {
/* 168 */       this.$tracer = (JVar)this.unmarshaller.field(4, Tracer.class, "tracer");
/* 169 */       con.body().assign((JAssignmentTarget)this.$tracer, (JExpression)$context.invoke("getTracer"));
/*     */     } 
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
/* 181 */     con = this.unmarshaller.constructor(2);
/* 182 */     $context = con.param((JType)getRuntime(UnmarshallingContext.class), "context");
/* 183 */     JVar $init = con.param((JType)this.codeModel.INT, "startState");
/* 184 */     con.body().invoke("this").arg((JExpression)$context);
/* 185 */     con.body().assign((JAssignmentTarget)this.$state, (JExpression)$init);
/*     */ 
/*     */     
/* 188 */     (new EnterElementMethodGenerator(this)).generate();
/* 189 */     (new LeaveElementMethodGenerator(this)).generate();
/* 190 */     (new EnterAttributeMethodGenerator(this)).generate();
/* 191 */     (new EnterLeaveMethodGenerator(this, "leaveAttribute", Alphabet.LeaveAttribute.class)).generate();
/* 192 */     (new TextMethodGenerator(this)).generate();
/*     */     
/* 194 */     generateLeaveChild();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JClass getRuntime(Class clazz) {
/* 204 */     return this.parent.context.getRuntime(clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JClass getInterleaveDispatcher(Alphabet.Interleave a) {
/* 212 */     JClass cls = (JClass)this.interleaveDispatcherImpls.get(a);
/* 213 */     if (cls != null) return cls;
/*     */ 
/*     */     
/* 216 */     JDefinedClass impl = null;
/*     */     try {
/* 218 */       impl = this.unmarshaller._class(4, "Interleave" + createId());
/* 219 */     } catch (JClassAlreadyExistsException e) {
/* 220 */       e.printStackTrace();
/* 221 */       _assert(false);
/*     */     } 
/* 223 */     impl._extends(getRuntime(InterleaveDispatcher.class));
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
/* 235 */     JMethod cstr = impl.constructor(4);
/*     */     
/* 237 */     JInvocation arrayInit = JExpr._new(getRuntime(UnmarshallingEventHandler.class).array());
/* 238 */     for (int i = 0; i < a.branches.length; i++) {
/* 239 */       arrayInit.arg((JExpression)JExpr._new((JClass)this.unmarshaller).arg((JExpression)JExpr._super().ref("sites").component(JExpr.lit(i))).arg(getStateNumber((a.branches[i]).initialState)));
/*     */     }
/*     */ 
/*     */     
/* 243 */     cstr.body().invoke("super").arg((JExpression)this.$context).arg(JExpr.lit(a.branches.length));
/* 244 */     cstr.body().invoke("init").arg((JExpression)arrayInit);
/*     */ 
/*     */ 
/*     */     
/* 248 */     generateGetBranchForXXX(impl, a, "Element", 0);
/* 249 */     generateGetBranchForXXX(impl, a, "Attribute", 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 256 */     JMethod m = impl.method(2, (JType)this.codeModel.INT, "getBranchForText");
/* 257 */     m.body()._return(JExpr.lit(a.getTextBranchIndex()));
/*     */ 
/*     */     
/* 260 */     this.interleaveDispatcherImpls.put(a, impl);
/* 261 */     return (JClass)impl;
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
/*     */   private void generateGetBranchForXXX(JDefinedClass clazz, Alphabet.Interleave a, String methodSuffix, int nameIdx) {
/* 273 */     JMethod method = clazz.method(2, (JType)this.codeModel.INT, "getBranchFor" + methodSuffix);
/* 274 */     JVar $uri = method.param((JType)this.codeModel.ref(String.class), "uri");
/* 275 */     JVar $local = method.param((JType)this.codeModel.ref(String.class), "local");
/*     */     
/* 277 */     for (int i = 0; i < a.branches.length; i++) {
/* 278 */       Alphabet.Interleave.Branch br = a.branches[i];
/* 279 */       NameClass nc = br.getName(nameIdx);
/*     */       
/* 281 */       if (!nc.isNull())
/*     */       {
/* 283 */         method.body()._if(this.parent.generateNameClassTest(nc, $uri, $local))._then()._return(JExpr.lit(i));
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 288 */     method.body()._return(JExpr.lit(-1));
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
/*     */   protected final void generateGoto(JBlock $body, State target) {
/* 300 */     generateGoto($body, getStateNumber(target));
/*     */   }
/*     */   
/*     */   private JExpression getStateNumber(State state) {
/* 304 */     return JExpr.lit(this.automaton.getStateNumber(state));
/*     */   }
/*     */   
/*     */   private void generateGoto(JBlock $body, JExpression nextState) {
/* 308 */     if (this.parent.trace)
/*     */     {
/* 310 */       $body.invoke((JExpression)this.$tracer, "nextState").arg(nextState);
/*     */     }
/*     */     
/* 313 */     $body.assign((JAssignmentTarget)this.$state, nextState);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateLeaveChild() {
/* 322 */     if (!this.parent.trace) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 330 */     JMethod method = this.unmarshaller.method(1, (JType)this.codeModel.VOID, "leaveChild");
/*     */     
/* 332 */     method._throws(SAXException.class);
/*     */     
/* 334 */     JVar $nextState = method.param((JType)this.codeModel.INT, "nextState");
/*     */     
/* 336 */     method.body().invoke((JExpression)this.$tracer, "nextState").arg((JExpression)$nextState);
/* 337 */     method.body().invoke(JExpr._super(), "leaveChild").arg((JExpression)$nextState);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String generateEncodedTextType() {
/* 348 */     StringBuffer buf = new StringBuffer(this.automaton.getStateSize());
/*     */     
/* 350 */     for (int i = this.automaton.getStateSize() - 1; i >= 0; i--) {
/* 351 */       buf.append('-');
/*     */     }
/* 353 */     Iterator itr = this.automaton.states();
/* 354 */     while (itr.hasNext()) {
/* 355 */       State s = itr.next();
/*     */ 
/*     */       
/* 358 */       buf.setCharAt(this.automaton.getStateNumber(s), s.isListState ? 76 : 45);
/*     */     } 
/*     */ 
/*     */     
/* 362 */     return buf.toString();
/*     */   }
/*     */   protected final void eatText(JBlock block, Alphabet.BoundText ta, JExpression $attValue) { JMethod method = (JMethod)this.eatTextFunctions.get(ta);
/*     */     if (method == null) {
/*     */       method = generateEatTextFunction(ta);
/*     */       this.eatTextFunctions.put(ta, method);
/*     */     } 
/* 369 */     block.invoke(method).arg($attValue); } PerClassGenerator(UnmarshallerGenerator _parent, Automaton a) { this.eatTextFunctions = new HashMap();
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 466 */     this.dispatchLookupFunctions = new HashMap(); this.parent = _parent; this.codeModel = this.parent.codeModel; this.context = a.getOwner(); this.automaton = a; JDefinedClass impl = this.context.implClass; impl._implements(getRuntime(UnmarshallableObject.class)); this.unmarshaller = this.parent.context.getClassFactory().createClass((JClassContainer)impl, "Unmarshaller", null); this.unmarshaller._extends(getRuntime(AbstractUnmarshallingEventHandlerImpl.class)); JMethod method = this.unmarshaller.method(1, Object.class, "owner");
/*     */     method.body()._return((JExpression)impl.staticRef("this"));
/*     */     this.$state = JExpr.ref("state");
/*     */     this.$context = JExpr.ref("context");
/*     */     this.dc = (DeserializerContext)new XMLDeserializerContextImpl((JExpression)this.$context);
/*     */     method = impl.method(1, (JType)getRuntime(UnmarshallingEventHandler.class), "createUnmarshaller");
/*     */     JVar $context = method.param((JType)getRuntime(UnmarshallingContext.class), "context");
/*     */     method.body()._return((JExpression)JExpr._new((JClass)this.unmarshaller).arg((JExpression)$context));
/* 474 */     this.transitionTable = new TransitionTable(this.automaton); } protected final JExpression invokeLookup(Alphabet.Dispatch da, TransitionTable.Entry tte) { JMethod lookup = (JMethod)this.dispatchLookupFunctions.get(da);
/* 475 */     if (lookup == null) {
/* 476 */       this.dispatchLookupFunctions.put(da, lookup = generateDispatchFunction(da, tte));
/*     */     }
/* 478 */     return (JExpression)JExpr.invoke(lookup); } private JMethod generateEatTextFunction(Alphabet.BoundText ta) { JMethod method = this.unmarshaller.method(4, (JType)this.codeModel.VOID, "eatText" + createId()); method._throws(SAXException.class); JVar $value = method.param(8, String.class, "value"); JTryBlock $try = method.body()._try(); JCatchBlock $catch = $try._catch(this.codeModel.ref(Exception.class));
/*     */     $catch.body().invoke("handleParseConversionException").arg((JExpression)$catch.param("e"));
/*     */     if (this.parent.trace)
/*     */       $try.body().invoke((JExpression)this.$tracer, "onConvertValue").arg((JExpression)$value).arg(JExpr.lit((ta.field.getFieldUse()).name)); 
/*     */     if (!ta.item.xducer.needsDelayedDeserialization()) {
/*     */       ta.field.setter($try.body(), TypeAdaptedTransducer.adapt(ta.item.xducer, ta.field).generateDeserializer((JExpression)$value, this.dc));
/*     */     } else {
/*     */       JDefinedClass patcher = this.codeModel.newAnonymousClass(this.codeModel.ref(Runnable.class));
/*     */       JMethod run = patcher.method(1, (JType)this.codeModel.VOID, "run");
/*     */       ta.field.setter(run.body(), ta.item.xducer.generateDeserializer((JExpression)$value, this.dc));
/*     */       $try.body().invoke((JExpression)this.$context, "addPatcher").arg((JExpression)JExpr._new((JClass)patcher));
/*     */     } 
/*     */     return method; }
/* 491 */   protected final JMethod generateDispatchFunction(Alphabet.Dispatch da, TransitionTable.Entry tte) { JMethod lookup = this.unmarshaller.method(4, Class.class, "lookup" + createId());
/* 492 */     lookup._throws(SAXException.class);
/* 493 */     JBlock body = lookup.body();
/* 494 */     JFieldRef jFieldRef = JExpr.ref("context");
/*     */     
/* 496 */     JVar $idx = body.decl((JType)this.codeModel.INT, "idx", (JExpression)jFieldRef.invoke("getAttribute").arg(JExpr.lit(da.attName.namespaceURI)).arg(JExpr.lit(da.attName.localName)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 501 */     JConditional cond = body._if($idx.gte(JExpr.lit(0)));
/* 502 */     cond._then()._return(da.table.lookup(this.parent.context, (JExpression)jFieldRef.invoke("eatAttribute").arg((JExpression)$idx), (JExpression)jFieldRef));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 508 */     cond._else()._return(JExpr._null());
/*     */     
/* 510 */     return lookup; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final void _assert(boolean b) {
/* 516 */     if (!b)
/* 517 */       throw new JAXBAssertionError(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generato\\unmarshaller\PerClassGenerator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */