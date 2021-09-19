/*     */ package 1.0.com.sun.tools.xjc.generator.marshaller;
/*     */ 
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ExpressionPool;
/*     */ import com.sun.msv.grammar.ExpressionVisitorVoid;
/*     */ import com.sun.tools.xjc.generator.GeneratorContext;
/*     */ import com.sun.tools.xjc.generator.marshaller.AttributePass;
/*     */ import com.sun.tools.xjc.generator.marshaller.BodyPass;
/*     */ import com.sun.tools.xjc.generator.marshaller.Builder;
/*     */ import com.sun.tools.xjc.generator.marshaller.FieldMarshallerGenerator;
/*     */ import com.sun.tools.xjc.generator.marshaller.Inside;
/*     */ import com.sun.tools.xjc.generator.marshaller.Outside;
/*     */ import com.sun.tools.xjc.generator.marshaller.Pass;
/*     */ import com.sun.tools.xjc.generator.marshaller.Side;
/*     */ import com.sun.tools.xjc.generator.marshaller.SkipPass;
/*     */ import com.sun.tools.xjc.generator.marshaller.URIPass;
/*     */ import com.sun.tools.xjc.generator.util.BlockReference;
/*     */ import com.sun.tools.xjc.generator.util.ExistingBlockReference;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.grammar.FieldItem;
/*     */ import com.sun.tools.xjc.grammar.FieldUse;
/*     */ import com.sun.tools.xjc.grammar.xducer.SerializerContext;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import java.util.Map;
/*     */ import java.util.Stack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class Context
/*     */   implements SerializerContext
/*     */ {
/*     */   Side currentSide;
/*     */   Pass currentPass;
/*     */   public final GeneratorContext genContext;
/*     */   public final JVar $serializer;
/*     */   public final JCodeModel codeModel;
/*     */   protected final ClassItem classItem;
/*     */   public final ExpressionPool pool;
/*     */   boolean inOneOrMore = false;
/*  74 */   private final Builder builder = new Builder(this);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   private FieldUse fu = null;
/*     */ 
/*     */   
/*  83 */   private int iota = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Map fieldMarshallers;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Stack overridedFMGs;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Stack blocks;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Inside inside;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Outside outside;
/*     */ 
/*     */ 
/*     */   
/*     */   final Pass bodyPass;
/*     */ 
/*     */ 
/*     */   
/*     */   final Pass attPass;
/*     */ 
/*     */ 
/*     */   
/*     */   final Pass uriPass;
/*     */ 
/*     */ 
/*     */   
/*     */   final Pass skipPass;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JClass getRuntime(Class clazz) {
/* 127 */     return this.genContext.getRuntime(clazz);
/*     */   }
/*     */   
/*     */   protected final boolean isInside() {
/* 131 */     return (this.currentSide == this.inside);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldMarshallerGenerator getMarshaller(FieldItem fi) {
/* 142 */     return (FieldMarshallerGenerator)this.fieldMarshallers.get(this.classItem.getDeclaredField(fi.name));
/*     */   }
/*     */   
/*     */   public Context(GeneratorContext _genContext, ExpressionPool _pool, ClassItem _class, JBlock codeBlock, JVar _$serializer, Map _fieldMarshallers) {
/* 146 */     this.overridedFMGs = new Stack();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 205 */     this.blocks = new Stack();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 252 */     this.inside = new Inside(this);
/* 253 */     this.outside = new Outside(this);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 259 */     this.bodyPass = (Pass)new BodyPass(this, "Body");
/* 260 */     this.attPass = (Pass)new AttributePass(this);
/* 261 */     this.uriPass = (Pass)new URIPass(this);
/* 262 */     this.skipPass = (Pass)new SkipPass(this); this.genContext = _genContext; this.pool = _pool; this.classItem = _class; this.$serializer = _$serializer; this.fieldMarshallers = _fieldMarshallers; this.codeModel = this.classItem.owner.codeModel; this.currentSide = (Side)this.outside; pushNewBlock((BlockReference)new ExistingBlockReference(codeBlock));
/*     */   }
/*     */   public void pushNewFieldMarshallerMapping(FieldMarshallerGenerator original, FieldMarshallerGenerator _new) { Object old = this.fieldMarshallers.put(original.owner().getFieldUse(), _new); _assert((old == original)); this.overridedFMGs.push(original); }
/*     */   public void popFieldMarshallerMapping() { FieldMarshallerGenerator fmg = this.overridedFMGs.pop(); this.fieldMarshallers.put(fmg.owner().getFieldUse(), fmg); }
/*     */   public void pushFieldItem(FieldItem item) { _assert((this.fu == null)); this.fu = this.classItem.getDeclaredField(item.name); this.currentSide = (Side)this.inside; _assert((this.fu != null)); }
/*     */   public void popFieldItem(FieldItem item) { _assert((this.fu != null && this.fu.name.equals(item.name)));
/*     */     this.fu = null;
/* 269 */     this.currentSide = (Side)this.outside; } public FieldMarshallerGenerator getCurrentFieldMarshaller() { return (FieldMarshallerGenerator)this.fieldMarshallers.get(this.fu); } public JExpression getNamespaceContext() { return (JExpression)this.$serializer.invoke("getNamespaceContext"); }
/*     */   public void pushNewBlock(BlockReference newBlock) { this.blocks.push(newBlock); }
/*     */   public void pushNewBlock(JBlock block) { pushNewBlock((BlockReference)new ExistingBlockReference(block)); }
/*     */   public void popBlock() { this.blocks.pop(); }
/* 273 */   public BlockReference getCurrentBlock() { return this.blocks.peek(); } public String createIdentifier() { return '_' + Integer.toString(this.iota++); } public void build(Expression exp) { exp.visit((ExpressionVisitorVoid)this.builder); } public JExpression onID(JExpression object, JExpression value) { return (JExpression)this.$serializer.invoke("onID").arg(object).arg(value); }
/*     */ 
/*     */   
/*     */   public JExpression onIDREF(JExpression target) {
/* 277 */     return (JExpression)this.$serializer.invoke("onIDREF").arg(target);
/*     */   }
/*     */   
/*     */   public void declareNamespace(JBlock block, JExpression uri, JExpression prefix, JExpression requirePrefix) {
/* 281 */     block.invoke(getNamespaceContext(), "declareNamespace").arg(uri).arg(prefix).arg(requirePrefix);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void _assert(boolean b) {
/* 287 */     if (!b) throw new JAXBAssertionError(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\marshaller\Context.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */