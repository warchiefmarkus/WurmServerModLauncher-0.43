/*     */ package 1.0.com.sun.tools.xjc.grammar;
/*     */ 
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ReferenceExp;
/*     */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*     */ import com.sun.tools.xjc.grammar.FieldUse;
/*     */ import com.sun.tools.xjc.grammar.JavaItemVisitor;
/*     */ import java.util.Vector;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ public final class ClassItem extends TypeItem {
/*     */   private final JDefinedClass type;
/*     */   private String userSpecifiedImplClass;
/*     */   public final AnnotatedGrammar owner;
/*     */   private final Map fields;
/*     */   public final ReferenceExp agm;
/*     */   private final Vector constructors;
/*     */   public boolean hasGetContentMethod;
/*     */   public SuperClassItem superClass;
/*     */   
/*     */   public JType getType() {
/*     */     return (JType)this.type;
/*     */   }
/*     */   
/*     */   public JDefinedClass getTypeAsDefined() {
/*     */     return this.type;
/*     */   }
/*     */   
/*  30 */   protected ClassItem(AnnotatedGrammar _owner, JDefinedClass _type, Expression exp, Locator loc) { super(_type.name(), loc);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  61 */     this.fields = new HashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     this.agm = new ReferenceExp(null);
/*     */ 
/*     */ 
/*     */     
/* 135 */     this.constructors = new Vector();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 152 */     this.hasGetContentMethod = false; this.type = _type; this.owner = _owner; this.exp = exp; }
/*     */   public final FieldUse getDeclaredField(String name) { return (FieldUse)this.fields.get(name); }
/*     */   public final FieldUse getField(String name) { FieldUse fu = getDeclaredField(name);
/*     */     if (fu != null)
/*     */       return fu; 
/*     */     if (this.superClass != null)
/*     */       return getSuperClass().getField(name); 
/* 159 */     return null; } public com.sun.tools.xjc.grammar.ClassItem getSuperClass() { if (this.superClass == null) return null; 
/* 160 */     return this.superClass.definition; } public final FieldUse[] getDeclaredFieldUses() { return (FieldUse[])this.fields.values().toArray((Object[])new FieldUse[this.fields.size()]); }
/*     */   public FieldUse getOrCreateFieldUse(String name) { FieldUse r = (FieldUse)this.fields.get(name);
/*     */     if (r == null)
/*     */       this.fields.put(name, r = new FieldUse(name, this)); 
/*     */     return r; }
/* 165 */   public Object visitJI(JavaItemVisitor visitor) { return visitor.onClass(this); } public void removeDuplicateFieldUses() { com.sun.tools.xjc.grammar.ClassItem superClass = getSuperClass();
/*     */     if (this.superClass == null)
/*     */       return; 
/*     */     FieldUse[] fu = getDeclaredFieldUses();
/*     */     for (int i = 0; i < fu.length; i++) {
/*     */       if (superClass.getField((fu[i]).name) != null)
/*     */         this.fields.remove((fu[i]).name); 
/*     */     }  }
/*     */   public void addConstructor(String[] fieldNames) { this.constructors.add(new Constructor(fieldNames)); }
/*     */   public Iterator iterateConstructors() { return this.constructors.iterator(); }
/* 175 */   protected boolean calcEpsilonReducibility() { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUserSpecifiedImplClass() {
/* 180 */     return this.userSpecifiedImplClass;
/*     */   }
/*     */   
/*     */   public void setUserSpecifiedImplClass(String userSpecifiedImplClass) {
/* 184 */     this.userSpecifiedImplClass = userSpecifiedImplClass;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\ClassItem.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */