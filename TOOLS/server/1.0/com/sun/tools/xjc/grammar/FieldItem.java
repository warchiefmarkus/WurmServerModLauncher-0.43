/*     */ package 1.0.com.sun.tools.xjc.grammar;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.tools.xjc.generator.field.FieldRendererFactory;
/*     */ import com.sun.tools.xjc.grammar.DefaultValue;
/*     */ import com.sun.tools.xjc.grammar.JavaItem;
/*     */ import com.sun.tools.xjc.grammar.JavaItemVisitor;
/*     */ import com.sun.tools.xjc.grammar.TypeItem;
/*     */ import com.sun.tools.xjc.grammar.util.Multiplicity;
/*     */ import com.sun.tools.xjc.reader.TypeUtil;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FieldItem
/*     */   extends JavaItem
/*     */ {
/*     */   public FieldRendererFactory realization;
/*     */   public DefaultValue[] defaultValues;
/*     */   public Multiplicity multiplicity;
/*     */   public boolean collisionExpected;
/*     */   public String javadoc;
/*     */   private boolean delegation;
/*     */   private final Set types;
/*     */   public final JType userSpecifiedType;
/*     */   
/*     */   public FieldItem(String name, Locator loc) {
/*  34 */     this(name, null, loc);
/*     */   }
/*     */   public FieldItem(String name, Expression exp, Locator loc) {
/*  37 */     this(name, exp, null, loc);
/*     */   }
/*     */   
/*     */   public FieldItem(String name, Expression _exp, JType _userDefinedType, Locator loc) {
/*  41 */     super(name, loc);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     this.defaultValues = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  99 */     this.collisionExpected = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 105 */     this.javadoc = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 121 */     this.delegation = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 143 */     this.types = new HashSet();
/*     */     this.exp = _exp;
/*     */     this.userSpecifiedType = _userDefinedType;
/*     */   }
/*     */   public void setDelegation(boolean f) {
/*     */     this.delegation = f;
/*     */   }
/*     */   
/*     */   protected boolean isDelegated() {
/*     */     return this.delegation;
/*     */   }
/*     */   
/*     */   public final void addType(TypeItem ti) throws BadTypeException {
/* 156 */     if (this.userSpecifiedType != null)
/*     */     {
/* 158 */       throw new BadTypeException(this.userSpecifiedType, null);
/*     */     }
/*     */     
/* 161 */     this.types.add(ti);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TypeItem[] listTypes() {
/* 168 */     return (TypeItem[])this.types.toArray((Object[])new TypeItem[this.types.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean hasTypes() {
/* 175 */     return !this.types.isEmpty();
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
/*     */   public JType getType(JCodeModel codeModel) {
/* 208 */     if (this.userSpecifiedType != null) return this.userSpecifiedType;
/*     */ 
/*     */     
/* 211 */     JType[] classes = new JType[this.types.size()];
/* 212 */     TypeItem[] types = listTypes();
/*     */     
/* 214 */     for (int i = 0; i < types.length; i++) {
/* 215 */       classes[i] = types[i].getType();
/*     */     }
/* 217 */     return TypeUtil.getCommonBaseType(codeModel, classes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUnboxable(JCodeModel codeModel) {
/* 228 */     TypeItem[] types = listTypes();
/*     */     
/* 230 */     if (!getType(codeModel).isPrimitive()) {
/* 231 */       return false;
/*     */     }
/*     */     
/* 234 */     for (int i = 0; i < types.length; i++) {
/* 235 */       JType t = types[i].getType();
/* 236 */       if (!(t instanceof com.sun.codemodel.JPrimitiveType))
/*     */       {
/*     */         
/* 239 */         if (((JClass)t).getPrimitiveType() == null)
/* 240 */           return false; 
/*     */       }
/*     */     } 
/* 243 */     return true;
/*     */   }
/*     */   
/*     */   public Object visitJI(JavaItemVisitor visitor) {
/* 247 */     return visitor.onField(this);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 251 */     return super.toString() + '[' + this.name + ']';
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\grammar\FieldItem.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */