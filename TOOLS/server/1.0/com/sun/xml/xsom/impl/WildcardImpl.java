/*     */ package 1.0.com.sun.xml.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSTerm;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.ComponentImpl;
/*     */ import com.sun.xml.xsom.impl.Ref;
/*     */ import com.sun.xml.xsom.impl.SchemaImpl;
/*     */ import com.sun.xml.xsom.visitor.XSFunction;
/*     */ import com.sun.xml.xsom.visitor.XSTermFunction;
/*     */ import com.sun.xml.xsom.visitor.XSTermVisitor;
/*     */ import com.sun.xml.xsom.visitor.XSVisitor;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class WildcardImpl
/*     */   extends ComponentImpl
/*     */   implements XSWildcard, Ref.Term
/*     */ {
/*     */   private final int mode;
/*     */   
/*     */   protected WildcardImpl(SchemaImpl owner, AnnotationImpl _annon, Locator _loc, int _mode) {
/*  32 */     super(owner, _annon, _loc);
/*  33 */     this.mode = _mode;
/*     */   }
/*     */   
/*     */   public int getMode() {
/*  37 */     return this.mode;
/*     */   } public com.sun.xml.xsom.impl.WildcardImpl union(SchemaImpl owner, com.sun.xml.xsom.impl.WildcardImpl rhs) {
/*     */     Other o;
/*     */     Finite f;
/*  41 */     if (this instanceof Any || rhs instanceof Any) {
/*  42 */       return (com.sun.xml.xsom.impl.WildcardImpl)new Any(owner, null, null, this.mode);
/*     */     }
/*  44 */     if (this instanceof Finite && rhs instanceof Finite) {
/*  45 */       Set values = new HashSet();
/*  46 */       values.addAll(Finite.access$000((Finite)this));
/*  47 */       values.addAll(Finite.access$000((Finite)rhs));
/*  48 */       return (com.sun.xml.xsom.impl.WildcardImpl)new Finite(owner, null, null, values, this.mode);
/*     */     } 
/*     */     
/*  51 */     if (this instanceof Other && rhs instanceof Other) {
/*  52 */       if (Other.access$100((Other)this).equals(Other.access$100((Other)rhs)))
/*     */       {
/*  54 */         return (com.sun.xml.xsom.impl.WildcardImpl)new Other(owner, null, null, Other.access$100((Other)this), this.mode);
/*     */       }
/*  56 */       throw new UnsupportedOperationException("union is not expressible");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  63 */     if (this instanceof Other) {
/*  64 */       o = (Other)this; f = (Finite)rhs;
/*     */     } else {
/*  66 */       o = (Other)rhs; f = (Finite)this;
/*     */     } 
/*     */     
/*  69 */     if (Finite.access$000(f).contains(Other.access$100(o))) {
/*  70 */       return (com.sun.xml.xsom.impl.WildcardImpl)new Any(owner, null, null, this.mode);
/*     */     }
/*  72 */     return (com.sun.xml.xsom.impl.WildcardImpl)new Other(owner, null, null, Other.access$100(o), this.mode);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void visit(XSVisitor visitor) {
/* 142 */     visitor.wildcard(this);
/*     */   }
/*     */   public final void visit(XSTermVisitor visitor) {
/* 145 */     visitor.wildcard(this);
/*     */   }
/*     */   public Object apply(XSTermFunction function) {
/* 148 */     return function.wildcard(this);
/*     */   }
/*     */   public Object apply(XSFunction function) {
/* 151 */     return function.wildcard(this);
/*     */   }
/*     */   
/*     */   public boolean isWildcard() {
/* 155 */     return true;
/* 156 */   } public boolean isModelGroupDecl() { return false; }
/* 157 */   public boolean isModelGroup() { return false; } public boolean isElementDecl() {
/* 158 */     return false;
/*     */   }
/* 160 */   public XSWildcard asWildcard() { return this; }
/* 161 */   public XSModelGroupDecl asModelGroupDecl() { return null; }
/* 162 */   public XSModelGroup asModelGroup() { return null; } public XSElementDecl asElementDecl() {
/* 163 */     return null;
/*     */   }
/*     */   
/*     */   public XSTerm getTerm() {
/* 167 */     return (XSTerm)this;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\WildcardImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */