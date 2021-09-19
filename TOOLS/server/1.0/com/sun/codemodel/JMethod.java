/*     */ package 1.0.com.sun.codemodel;
/*     */ 
/*     */ import com.sun.codemodel.JBlock;
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JDeclaration;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.codemodel.JDocComment;
/*     */ import com.sun.codemodel.JFormatter;
/*     */ import com.sun.codemodel.JGenerable;
/*     */ import com.sun.codemodel.JMods;
/*     */ import com.sun.codemodel.JStatement;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.codemodel.util.ClassNameComparator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JMethod
/*     */   implements JDeclaration
/*     */ {
/*     */   private JMods mods;
/*  31 */   private JType type = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  36 */   private String name = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  41 */   private final List params = new ArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   private final Set _throws = new TreeSet(ClassNameComparator.theInstance);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   private JBlock body = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private JDefinedClass outer;
/*     */ 
/*     */   
/*  58 */   private JDocComment jdoc = null;
/*     */   
/*     */   private boolean isConstructor() {
/*  61 */     return (this.type == null);
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
/*     */   JMethod(JDefinedClass outer, int mods, JType type, String name) {
/*  77 */     this.mods = JMods.forMethod(mods);
/*  78 */     this.type = type;
/*  79 */     this.name = name;
/*  80 */     this.outer = outer;
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
/*     */   JMethod(int mods, JDefinedClass _class) {
/*  93 */     this.mods = JMods.forMethod(mods);
/*  94 */     this.type = null;
/*  95 */     this.name = _class.name();
/*  96 */     this.outer = _class;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public com.sun.codemodel.JMethod _throws(JClass exception) {
/* 107 */     this._throws.add(exception);
/* 108 */     return this;
/*     */   }
/*     */   
/*     */   public com.sun.codemodel.JMethod _throws(Class exception) {
/* 112 */     return _throws(this.outer.owner().ref(exception));
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
/*     */   public JVar param(int mods, JType type, String name) {
/* 128 */     JVar v = new JVar(JMods.forVar(mods), type, name, null);
/* 129 */     this.params.add(v);
/* 130 */     return v;
/*     */   }
/*     */   
/*     */   public JVar param(JType type, String name) {
/* 134 */     return param(0, type, name);
/*     */   }
/*     */   
/*     */   public JVar param(int mods, Class type, String name) {
/* 138 */     return param(mods, (JType)this.outer.owner().ref(type), name);
/*     */   }
/*     */   
/*     */   public JVar param(Class type, String name) {
/* 142 */     return param((JType)this.outer.owner().ref(type), name);
/*     */   }
/*     */   
/*     */   public String name() {
/* 146 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JType type() {
/* 154 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JType[] listParamTypes() {
/* 163 */     JType[] r = new JType[this.params.size()];
/* 164 */     for (int i = 0; i < r.length; i++)
/* 165 */       r[i] = ((JVar)this.params.get(i)).type(); 
/* 166 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JVar[] listParams() {
/* 175 */     return (JVar[])this.params.toArray((Object[])new JVar[this.params.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasSignature(JType[] argTypes) {
/* 182 */     JVar[] p = listParams();
/* 183 */     if (p.length != argTypes.length) {
/* 184 */       return false;
/*     */     }
/* 186 */     for (int i = 0; i < p.length; i++) {
/* 187 */       if (!(p[i]).type.equals(argTypes[i]))
/* 188 */         return false; 
/*     */     } 
/* 190 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JBlock body() {
/* 200 */     if (this.body == null)
/* 201 */       this.body = new JBlock(); 
/* 202 */     return this.body;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDocComment javadoc() {
/* 212 */     if (this.jdoc == null)
/* 213 */       this.jdoc = new JDocComment(); 
/* 214 */     return this.jdoc;
/*     */   }
/*     */   
/*     */   public void declare(JFormatter f) {
/* 218 */     if (this.jdoc != null) {
/* 219 */       f.g((JGenerable)this.jdoc);
/*     */     }
/* 221 */     f.g((JGenerable)this.mods);
/* 222 */     if (!isConstructor())
/* 223 */       f.g((JGenerable)this.type); 
/* 224 */     f.p(this.name).p('(');
/* 225 */     boolean first = true;
/* 226 */     for (Iterator i = this.params.iterator(); i.hasNext(); ) {
/* 227 */       if (!first)
/* 228 */         f.p(','); 
/* 229 */       f.b(i.next());
/* 230 */       first = false;
/*     */     } 
/* 232 */     f.p(')');
/* 233 */     if (!this._throws.isEmpty()) {
/* 234 */       f.nl().i().p("throws");
/* 235 */       first = true;
/* 236 */       for (Iterator iterator = this._throws.iterator(); iterator.hasNext(); ) {
/* 237 */         if (!first)
/* 238 */           f.p(','); 
/* 239 */         f.g((JGenerable)iterator.next());
/* 240 */         first = false;
/*     */       } 
/* 242 */       f.nl().o();
/*     */     } 
/* 244 */     if (this.body != null) {
/* 245 */       f.s((JStatement)this.body);
/* 246 */     } else if (!this.outer.isInterface() && !this.mods.isAbstract() && !this.mods.isNative()) {
/*     */       
/* 248 */       f.s((JStatement)new JBlock());
/*     */     } else {
/* 250 */       f.p(';').nl();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JMods getMods() {
/* 260 */     return this.mods;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JMethod.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */