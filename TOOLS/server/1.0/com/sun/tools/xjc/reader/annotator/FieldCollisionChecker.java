/*     */ package 1.0.com.sun.tools.xjc.reader.annotator;
/*     */ 
/*     */ import com.sun.msv.grammar.BinaryExp;
/*     */ import com.sun.msv.grammar.ChoiceExp;
/*     */ import com.sun.msv.grammar.ExpressionVisitorVoid;
/*     */ import com.sun.msv.grammar.InterleaveExp;
/*     */ import com.sun.msv.grammar.OtherExp;
/*     */ import com.sun.msv.grammar.SequenceExp;
/*     */ import com.sun.msv.grammar.xmlschema.OccurrenceExp;
/*     */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*     */ import com.sun.tools.xjc.grammar.BGMWalker;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.grammar.FieldItem;
/*     */ import com.sun.tools.xjc.grammar.SuperClassItem;
/*     */ import com.sun.tools.xjc.reader.annotator.AnnotatorController;
/*     */ import com.sun.tools.xjc.reader.annotator.Messages;
/*     */ import com.sun.tools.xjc.util.SubList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FieldCollisionChecker
/*     */   extends BGMWalker
/*     */ {
/*     */   private final AnnotatorController controller;
/*     */   private List fields;
/*     */   private final Map class2fields;
/*     */   private int sl;
/*     */   private int sr;
/*     */   
/*     */   public static void check(AnnotatedGrammar grammar, AnnotatorController controller) {
/*  43 */     com.sun.tools.xjc.reader.annotator.FieldCollisionChecker checker = new com.sun.tools.xjc.reader.annotator.FieldCollisionChecker(controller);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  50 */     Set baseClasses = new HashSet();
/*  51 */     ClassItem[] cls = grammar.getClasses(); int i;
/*  52 */     for (i = 0; i < cls.length; i++) {
/*  53 */       baseClasses.add(cls[i].getSuperClass());
/*     */     }
/*  55 */     for (i = 0; i < cls.length; i++) {
/*  56 */       if (!baseClasses.contains(cls[i])) {
/*  57 */         checker.reset();
/*  58 */         cls[i].visit((ExpressionVisitorVoid)checker);
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private FieldCollisionChecker(AnnotatorController _controller) {
/*  77 */     this.fields = new ArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  86 */     this.class2fields = new HashMap();
/*     */     this.controller = _controller;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void reset() {
/*  94 */     this.fields = new ArrayList();
/*  95 */     this.sl = this.sr = -1;
/*     */   }
/*     */   
/*     */   public void onInterleave(InterleaveExp exp) {
/*  99 */     check((BinaryExp)exp);
/*     */   }
/*     */   
/*     */   public void onSequence(SequenceExp exp) {
/* 103 */     check((BinaryExp)exp);
/*     */   }
/*     */   
/*     */   private void check(BinaryExp exp) {
/* 107 */     int l = this.fields.size();
/* 108 */     exp.exp1.visit((ExpressionVisitorVoid)this);
/* 109 */     int r = this.fields.size();
/* 110 */     exp.exp2.visit((ExpressionVisitorVoid)this);
/*     */     
/* 112 */     compare(l, r, r, this.fields.size());
/*     */   }
/*     */   
/*     */   public void onChoice(ChoiceExp exp) {
/* 116 */     int l = this.fields.size();
/* 117 */     exp.exp1.visit((ExpressionVisitorVoid)this);
/* 118 */     int r = this.fields.size();
/* 119 */     exp.exp2.visit((ExpressionVisitorVoid)this);
/*     */     
/* 121 */     if (l <= this.sl && this.sr <= r) {
/*     */ 
/*     */       
/* 124 */       compare(this.sl, this.sr, r, this.fields.size());
/*     */     }
/* 126 */     else if (r <= this.sl && this.sr <= this.fields.size()) {
/*     */ 
/*     */       
/* 129 */       compare(l, r, this.sl, this.sr);
/*     */     } 
/*     */   }
/*     */   public Object onSuper(SuperClassItem sci) {
/* 133 */     this.sl = this.fields.size();
/*     */ 
/*     */ 
/*     */     
/* 137 */     sci.definition.visit((ExpressionVisitorVoid)this);
/*     */     
/* 139 */     this.sr = this.fields.size();
/*     */     
/* 141 */     return null;
/*     */   }
/*     */   
/*     */   public Object onField(FieldItem item) {
/* 145 */     this.fields.add(item);
/*     */ 
/*     */     
/* 148 */     if (item.name.equals("Class")) {
/* 149 */       error(item.locator, "FieldCollisionChecker.ReservedWordCollision", item.name);
/*     */     }
/* 151 */     return null;
/*     */   }
/*     */   
/*     */   public Object onClass(ClassItem item) {
/* 155 */     List subList = (List)this.class2fields.get(item);
/* 156 */     if (subList == null) {
/*     */       
/* 158 */       int s = this.fields.size();
/* 159 */       super.onClass(item);
/* 160 */       int e = this.fields.size();
/*     */ 
/*     */       
/* 163 */       this.class2fields.put(item, new SubList(this.fields, s, e));
/*     */     }
/*     */     else {
/*     */       
/* 167 */       this.fields.addAll(subList);
/*     */     } 
/* 169 */     return null;
/*     */   }
/*     */   
/*     */   public void onOther(OtherExp exp) {
/* 173 */     if (exp instanceof OccurrenceExp) {
/* 174 */       ((OccurrenceExp)exp).itemExp.visit((ExpressionVisitorVoid)this);
/*     */     } else {
/* 176 */       super.onOther(exp);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void compare(int ls, int le, int rs, int re) {
/* 183 */     for (int l = ls; l < le; l++) {
/* 184 */       FieldItem left = this.fields.get(l);
/*     */       
/* 186 */       for (int r = rs; r < re; r++) {
/* 187 */         FieldItem right = this.fields.get(r);
/*     */         
/* 189 */         if (left.name.equals(right.name) && (!left.collisionExpected || !right.collisionExpected)) {
/*     */           Locator locator;
/*     */ 
/*     */           
/* 193 */           if (left.locator != null) { locator = left.locator; }
/* 194 */           else { locator = right.locator; }
/*     */           
/* 196 */           error(locator, "FieldCollisionChecker.PropertyNameCollision", left.name);
/*     */           
/* 198 */           if (left.locator != null && right.locator != null) {
/* 199 */             error(right.locator, "FieldCollisionChecker.PropertyNameCollision.Source", left.name);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void error(Locator loc, String prop, Object arg) {
/* 208 */     this.controller.getErrorReceiver().error(loc, Messages.format(prop, arg));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\annotator\FieldCollisionChecker.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */