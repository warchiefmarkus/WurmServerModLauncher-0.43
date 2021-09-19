/*     */ package 1.0.com.sun.tools.xjc.generator;
/*     */ 
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JFieldVar;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.msv.datatype.xsd.XSDatatype;
/*     */ import com.sun.tools.xjc.generator.GeneratorContext;
/*     */ import com.sun.tools.xjc.generator.LookupTableFactory;
/*     */ import com.sun.tools.xjc.generator.XMLDeserializerContextImpl;
/*     */ import com.sun.tools.xjc.generator.util.BlockReference;
/*     */ import com.sun.tools.xjc.grammar.xducer.BuiltinDatatypeTransducerFactory;
/*     */ import com.sun.tools.xjc.grammar.xducer.DeserializerContext;
/*     */ import com.sun.tools.xjc.grammar.xducer.SerializerContext;
/*     */ import com.sun.tools.xjc.grammar.xducer.Transducer;
/*     */ import com.sun.tools.xjc.runtime.UnmarshallingContext;
/*     */ import com.sun.tools.xjc.runtime.ValidatableObject;
/*     */ import com.sun.xml.bind.ProxyGroup;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LookupTable
/*     */ {
/* 101 */   private final Set entries = new HashSet();
/*     */   
/*     */   private final int id;
/*     */   
/*     */   private final LookupTableFactory owner;
/*     */   
/*     */   private JMethod $lookup;
/*     */   private JMethod $reverseLookup;
/*     */   private JMethod $add;
/*     */   private JFieldVar $map;
/*     */   private JFieldVar $rmap;
/*     */   private Transducer xducer;
/*     */   private GeneratorContext genContext;
/*     */   
/*     */   LookupTable(LookupTableFactory _owner, int _id) {
/* 116 */     this.owner = _owner;
/* 117 */     this.id = _id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConsistentWith(com.sun.tools.xjc.generator.LookupTable rhs) {
/* 126 */     for (Iterator itr = this.entries.iterator(); itr.hasNext(); ) {
/* 127 */       Entry a = itr.next();
/* 128 */       if (!rhs.isConsistentWith(a)) {
/* 129 */         return false;
/*     */       }
/*     */     } 
/* 132 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isConsistentWith(Entry e) {
/* 136 */     for (Iterator itr = this.entries.iterator(); itr.hasNext(); ) {
/* 137 */       Entry a = itr.next();
/* 138 */       if (!a.isConsistentWith(e))
/* 139 */         return false; 
/*     */     } 
/* 141 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Entry e) {
/* 151 */     this.entries.add(e);
/* 152 */     if (this.$lookup != null) generateEntry(e);
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void absorb(com.sun.tools.xjc.generator.LookupTable rhs) {
/* 162 */     for (Iterator itr = rhs.entries.iterator(); itr.hasNext(); ) {
/* 163 */       Entry e = itr.next();
/* 164 */       add(e);
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
/*     */   public JExpression lookup(GeneratorContext context, JExpression literal, JExpression unmContext) {
/* 177 */     if (this.$lookup == null) generateCode(context);
/*     */     
/* 179 */     return (JExpression)this.owner.getTableClass().staticInvoke(this.$lookup).arg(literal).arg(unmContext);
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
/*     */   public JExpression reverseLookup(JExpression obj, SerializerContext serializer) {
/* 191 */     return this.xducer.generateSerializer((JExpression)this.owner.getTableClass().staticInvoke(this.$reverseLookup).arg(obj), serializer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void declareNamespace(BlockReference body, JExpression value, SerializerContext serializer) {
/* 200 */     this.xducer.declareNamespace(body, (JExpression)this.owner.getTableClass().staticInvoke(this.$reverseLookup).arg(value), serializer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void generateCode(GeneratorContext context) {
/* 211 */     this.genContext = context;
/*     */ 
/*     */     
/* 214 */     JDefinedClass table = this.owner.getTableClass();
/* 215 */     JCodeModel codeModel = table.owner();
/*     */     
/* 217 */     this.$map = table.field(28, Map.class, "table" + this.id, (JExpression)JExpr._new(codeModel.ref(HashMap.class)));
/*     */     
/* 219 */     this.$rmap = table.field(28, Map.class, "rtable" + this.id, (JExpression)JExpr._new(codeModel.ref(HashMap.class)));
/*     */ 
/*     */     
/* 222 */     Entry[] e = (Entry[])this.entries.toArray((Object[])new Entry[this.entries.size()]);
/*     */ 
/*     */     
/* 225 */     this.xducer = BuiltinDatatypeTransducerFactory.get(context.getGrammar(), (XSDatatype)(Entry.access$000(e[0])).dt);
/*     */ 
/*     */     
/* 228 */     for (int i = 0; i < e.length; i++) {
/* 229 */       generateEntry(e[i]);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 236 */     this.$lookup = table.method(25, Class.class, "lookup" + this.id);
/* 237 */     JVar $literal = this.$lookup.param(String.class, "literal");
/* 238 */     XMLDeserializerContextImpl xMLDeserializerContextImpl = new XMLDeserializerContextImpl((JExpression)this.$lookup.param((JType)context.getRuntime(UnmarshallingContext.class), "context"));
/*     */ 
/*     */     
/* 241 */     this.$lookup.body()._return((JExpression)JExpr.cast((JType)codeModel.ref(Class.class), (JExpression)this.$map.invoke("get").arg(this.xducer.generateDeserializer((JExpression)$literal, (DeserializerContext)xMLDeserializerContextImpl))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 252 */     this.$reverseLookup = table.method(25, this.xducer.getReturnType(), "reverseLookup" + this.id);
/* 253 */     JVar $o = this.$reverseLookup.param(Object.class, "o");
/*     */     
/* 255 */     this.$reverseLookup.body()._return((JExpression)JExpr.cast(this.xducer.getReturnType(), (JExpression)this.$rmap.invoke("get").arg((JExpression)codeModel.ref(ProxyGroup.class).staticInvoke("blindWrap").arg((JExpression)$o).arg(context.getRuntime(ValidatableObject.class).dotclass()).arg(JExpr._null()).invoke("getClass"))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 271 */     this.$add = table.method(20, (JType)codeModel.VOID, "add" + this.id);
/* 272 */     JVar $key = this.$add.param(Object.class, "key");
/* 273 */     JVar $value = this.$add.param(Object.class, "value");
/*     */     
/* 275 */     this.$add.body().invoke((JExpression)this.$map, "put").arg((JExpression)$key).arg((JExpression)$value);
/* 276 */     this.$add.body().invoke((JExpression)this.$rmap, "put").arg((JExpression)$value).arg((JExpression)$key);
/*     */   }
/*     */ 
/*     */   
/*     */   private void generateEntry(Entry e) {
/* 281 */     this.owner.getTableClass().init().invoke("add" + this.id).arg(this.xducer.generateConstant(Entry.access$000(e))).arg((this.genContext.getClassContext(Entry.access$100(e))).implRef.dotclass());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\LookupTable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */