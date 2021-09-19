/*     */ package 1.0.com.sun.tools.xjc.generator.unmarshaller;
/*     */ 
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JCatchBlock;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JFieldVar;
/*     */ import com.sun.codemodel.JInvocation;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JSwitch;
/*     */ import com.sun.codemodel.JTryBlock;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.msv.datatype.DatabindableDatatype;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.HandlerMethodGenerator;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.PerClassGenerator;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.TransitionTable;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.automaton.Alphabet;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.automaton.State;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.automaton.Transition;
/*     */ import com.sun.tools.xjc.generator.validator.StringOutputStream;
/*     */ import com.sun.tools.xjc.runtime.ValidationContextAdaptor;
/*     */ import com.sun.xml.bind.unmarshaller.DatatypeDeserializer;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.StringWriter;
/*     */ import org.relaxng.datatype.Datatype;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class TextMethodGenerator
/*     */   extends HandlerMethodGenerator
/*     */ {
/*     */   private JVar $value;
/*     */   private int datatypeId;
/*     */   
/*     */   TextMethodGenerator(PerClassGenerator parent) {
/*  42 */     super(parent, "text", Alphabet.Text.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 121 */     this.datatypeId = 0;
/*     */   } private boolean needsGuard(State state) { int count = 0; TransitionTable.Entry[] e = this.table.list(state); for (int i = 0; i < e.length; i++) {
/*     */       if ((e[i]).alphabet.isText())
/*     */         count++; 
/*     */     } 
/* 126 */     return (count > 1); } protected boolean performTransition(State state, Alphabet alphabet, Transition action) { JBlock block = getCase(state);
/*     */     
/* 128 */     boolean needsGuard = needsGuard(state);
/*     */     
/* 130 */     if (needsGuard) {
/* 131 */       block = block._if(guardClause(alphabet))._then();
/*     */     }
/* 133 */     if (action == Transition.REVERT_TO_PARENT) {
/* 134 */       generateRevertToParent(block);
/* 135 */       return needsGuard;
/*     */     } 
/* 137 */     if (action.alphabet instanceof Alphabet.Reference) {
/* 138 */       generateSpawnChild(block, action);
/* 139 */       return needsGuard;
/*     */     } 
/*     */     
/* 142 */     if (action.alphabet instanceof Alphabet.BoundText) {
/* 143 */       this.parent.eatText(block, action.alphabet.asBoundText(), (JExpression)this.$value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 150 */     generateGoto(block, action.to);
/* 151 */     block._return();
/* 152 */     return needsGuard; }
/*     */   private JExpression guardClause(Alphabet a) { JExpression con; if (a instanceof Alphabet.IgnoredText || a instanceof Alphabet.SuperClass || a instanceof Alphabet.Child || a instanceof Alphabet.EverythingElse)
/*     */       return JExpr.TRUE;  _assert(a instanceof Alphabet.BoundText); DatabindableDatatype guard = ((Alphabet.BoundText)a).item.guard; StringWriter sw = new StringWriter(); try { ObjectOutputStream oos = new ObjectOutputStream((OutputStream)new StringOutputStream(sw)); oos.writeObject(guard); oos.close(); } catch (IOException e) { e.printStackTrace(); throw new InternalError("unserializable datatype:" + guard); }  JFieldVar jFieldVar = this.parent.context.implClass.field(28, Datatype.class, "___dt" + this.datatypeId++, (JExpression)this.codeModel.ref(DatatypeDeserializer.class).staticInvoke("deserialize").arg(JExpr.lit(sw.getBuffer().toString()))); if (guard.isContextDependent()) { JInvocation jInvocation = JExpr._new(this.parent.parent.context.getRuntime(ValidationContextAdaptor.class)).arg((JExpression)this.parent.$context); }
/*     */     else { con = JExpr._null(); }
/* 156 */      return (JExpression)jFieldVar.invoke("isValid").arg((JExpression)this.$value).arg(con); } protected String getNameOfMethodDecl() { return "handleText"; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected JSwitch makeSwitch(JMethod method, JBlock body) {
/* 161 */     this.$value = method.param(8, String.class, "value");
/*     */     
/* 163 */     if (this.trace)
/*     */     {
/* 165 */       body.invoke((JExpression)this.$tracer, "onText").arg((JExpression)this.$value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 171 */     JTryBlock tryBlock = body._try();
/* 172 */     JSwitch s = super.makeSwitch(method, tryBlock.body());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 177 */     JCatchBlock c = tryBlock._catch(this.codeModel.ref(RuntimeException.class));
/* 178 */     JVar $e = c.param("e");
/* 179 */     c.body().invoke("handleUnexpectedTextException").arg((JExpression)this.$value).arg((JExpression)$e);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 196 */     return s;
/*     */   }
/*     */   
/*     */   protected void addParametersToContextSwitch(JInvocation inv) {
/* 200 */     inv.arg((JExpression)this.$value);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generato\\unmarshaller\TextMethodGenerator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */