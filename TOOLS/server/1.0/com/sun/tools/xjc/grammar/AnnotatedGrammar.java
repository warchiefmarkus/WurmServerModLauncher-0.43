/*     */ package 1.0.com.sun.tools.xjc.grammar;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.msv.datatype.DatabindableDatatype;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ExpressionPool;
/*     */ import com.sun.msv.grammar.Grammar;
/*     */ import com.sun.msv.grammar.ReferenceExp;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.grammar.InterfaceItem;
/*     */ import com.sun.tools.xjc.grammar.PrimitiveItem;
/*     */ import com.sun.tools.xjc.grammar.id.SymbolSpace;
/*     */ import com.sun.tools.xjc.grammar.xducer.DatabindableXducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.Transducer;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class AnnotatedGrammar
/*     */   extends ReferenceExp
/*     */   implements Grammar
/*     */ {
/*     */   private final ExpressionPool pool;
/*     */   public final JCodeModel codeModel;
/*     */   public final SymbolSpace defaultSymbolSpace;
/*  64 */   private final Map symbolSpaces = new HashMap();
/*     */ 
/*     */   
/*  67 */   private final Map classes = new HashMap();
/*     */ 
/*     */   
/*  70 */   private final Map interfaces = new HashMap();
/*     */ 
/*     */   
/*  73 */   private final Set primitives = new HashSet();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JClass rootClass;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   public Long serialVersionUID = null;
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotatedGrammar(ExpressionPool pool) {
/*  95 */     this(null, pool, new JCodeModel());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotatedGrammar(Grammar source, JCodeModel _codeModel) {
/* 102 */     this(source.getTopLevel(), source.getPool(), _codeModel);
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotatedGrammar(Expression topLevel, ExpressionPool pool, JCodeModel _codeModel) {
/* 107 */     super("");
/* 108 */     this.exp = topLevel;
/* 109 */     this.pool = pool;
/* 110 */     this.codeModel = _codeModel;
/* 111 */     this.defaultSymbolSpace = new SymbolSpace(this.codeModel);
/* 112 */     this.defaultSymbolSpace.setType((JType)this.codeModel.ref(Object.class));
/*     */   }
/*     */   public Expression getTopLevel() {
/* 115 */     return this.exp;
/*     */   } public ExpressionPool getPool() {
/* 117 */     return this.pool;
/*     */   }
/*     */   public PrimitiveItem[] getPrimitives() {
/* 120 */     return (PrimitiveItem[])this.primitives.toArray((Object[])new PrimitiveItem[this.primitives.size()]);
/*     */   }
/*     */   
/*     */   public ClassItem[] getClasses() {
/* 124 */     return (ClassItem[])this.classes.values().toArray((Object[])new ClassItem[this.classes.size()]);
/*     */   }
/*     */   public Iterator iterateClasses() {
/* 127 */     return this.classes.values().iterator();
/*     */   }
/*     */   
/*     */   public InterfaceItem[] getInterfaces() {
/* 131 */     return (InterfaceItem[])this.interfaces.values().toArray((Object[])new InterfaceItem[this.interfaces.size()]);
/*     */   }
/*     */   public Iterator iterateInterfaces() {
/* 134 */     return this.interfaces.values().iterator();
/*     */   }
/*     */   
/*     */   public SymbolSpace getSymbolSpace(String name) {
/* 138 */     SymbolSpace ss = (SymbolSpace)this.symbolSpaces.get(name);
/* 139 */     if (ss == null)
/* 140 */       this.symbolSpaces.put(name, ss = new SymbolSpace(this.codeModel)); 
/* 141 */     return ss;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrimitiveItem createPrimitiveItem(Transducer _xducer, DatabindableDatatype _guard, Expression _exp, Locator loc) {
/* 148 */     PrimitiveItem pi = new PrimitiveItem(_xducer, _guard, _exp, loc);
/* 149 */     this.primitives.add(pi);
/* 150 */     return pi;
/*     */   }
/*     */ 
/*     */   
/*     */   public PrimitiveItem createPrimitiveItem(JCodeModel writer, DatabindableDatatype dt, Expression exp, Locator loc) {
/* 155 */     return new PrimitiveItem((Transducer)new DatabindableXducer(writer, dt), dt, exp, loc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassItem getClassItem(JDefinedClass type) {
/* 162 */     return (ClassItem)this.classes.get(type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassItem createClassItem(JDefinedClass type, Expression body, Locator loc) {
/* 168 */     if (this.classes.containsKey(type)) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 173 */       System.err.println("class name " + type.fullName() + " is already defined");
/* 174 */       Iterator itr = this.classes.keySet().iterator();
/* 175 */       while (itr.hasNext()) {
/* 176 */         JDefinedClass cls = itr.next();
/* 177 */         System.err.println(cls.fullName());
/*     */       } 
/* 179 */       _assert(false);
/*     */     } 
/*     */     
/* 182 */     ClassItem o = new ClassItem(this, type, body, loc);
/* 183 */     this.classes.put(type, o);
/* 184 */     return o;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InterfaceItem getInterfaceItem(JDefinedClass type) {
/* 191 */     return (InterfaceItem)this.interfaces.get(type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InterfaceItem createInterfaceItem(JClass type, Expression body, Locator loc) {
/* 197 */     _assert(!this.interfaces.containsKey(type));
/*     */     
/* 199 */     InterfaceItem o = new InterfaceItem(type, body, loc);
/* 200 */     this.interfaces.put(type, o);
/* 201 */     return o;
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
/*     */   public JPackage[] getUsedPackages() {
/* 228 */     Set s = new TreeSet(packageComparator);
/*     */ 
/*     */     
/* 231 */     Iterator itr = iterateClasses();
/* 232 */     while (itr.hasNext()) {
/* 233 */       s.add(((ClassItem)itr.next()).getTypeAsDefined()._package());
/*     */     }
/* 235 */     itr = iterateInterfaces();
/* 236 */     while (itr.hasNext()) {
/* 237 */       s.add(((InterfaceItem)itr.next()).getTypeAsClass()._package());
/*     */     }
/* 239 */     return s.<JPackage>toArray(new JPackage[s.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final void _assert(boolean b) {
/* 244 */     if (!b) throw new JAXBAssertionError();
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 250 */   private static final Comparator packageComparator = (Comparator)new Object();
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\AnnotatedGrammar.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */