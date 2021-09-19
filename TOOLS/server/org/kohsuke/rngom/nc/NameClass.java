/*    */ package org.kohsuke.rngom.nc;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import javax.xml.namespace.QName;
/*    */ import org.kohsuke.rngom.ast.om.ParsedNameClass;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class NameClass
/*    */   implements ParsedNameClass, Serializable
/*    */ {
/*    */   static final int SPECIFICITY_NONE = -1;
/*    */   static final int SPECIFICITY_ANY_NAME = 0;
/*    */   static final int SPECIFICITY_NS_NAME = 1;
/*    */   static final int SPECIFICITY_NAME = 2;
/*    */   
/*    */   public abstract boolean contains(QName paramQName);
/*    */   
/*    */   public abstract int containsSpecificity(QName paramQName);
/*    */   
/*    */   public abstract <V> V accept(NameClassVisitor<V> paramNameClassVisitor);
/*    */   
/*    */   public abstract boolean isOpen();
/*    */   
/*    */   public Set<QName> listNames() {
/* 48 */     final Set<QName> names = new HashSet<QName>();
/* 49 */     accept(new NameClassWalker() {
/*    */           public Void visitName(QName name) {
/* 51 */             names.add(name);
/* 52 */             return null;
/*    */           }
/*    */         });
/* 55 */     return names;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean hasOverlapWith(NameClass nc2) {
/* 63 */     return OverlapDetector.overlap(this, nc2);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 68 */   public static final NameClass ANY = new AnyNameClass();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 85 */   public static final NameClass NULL = new NullNameClass();
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\nc\NameClass.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */