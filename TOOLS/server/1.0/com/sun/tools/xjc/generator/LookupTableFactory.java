/*     */ package 1.0.com.sun.tools.xjc.generator;
/*     */ 
/*     */ import com.sun.codemodel.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.msv.grammar.ChoiceExp;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.tools.xjc.generator.LookupTable;
/*     */ import com.sun.tools.xjc.generator.LookupTableBuilder;
/*     */ import com.sun.tools.xjc.generator.LookupTableUse;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class LookupTableFactory
/*     */   implements LookupTableBuilder
/*     */ {
/*     */   private JDefinedClass tableClass;
/*     */   private final JPackage pkg;
/*  88 */   private int id = 0;
/*     */ 
/*     */   
/*     */   public LookupTableFactory(JPackage _pkg) {
/*  92 */     this.pkg = _pkg;
/*     */   }
/*     */   
/*     */   JDefinedClass getTableClass() {
/*  96 */     if (this.tableClass == null) {
/*     */       try {
/*  98 */         this.tableClass = this.pkg._class(1, "Table");
/*  99 */       } catch (JClassAlreadyExistsException e) {
/* 100 */         throw new JAXBAssertionError();
/*     */       } 
/*     */     }
/*     */     
/* 104 */     return this.tableClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LookupTableUse buildTable(ChoiceExp exp) {
/*     */     Branch branch;
/* 116 */     Expression[] children = exp.getChildren();
/* 117 */     if (children.length < 3) return null;
/*     */     
/* 119 */     int nullBranchCount = 0;
/* 120 */     Branch[] branches = new Branch[children.length];
/*     */     
/* 122 */     for (int i = 0; i < children.length; i++) {
/* 123 */       branches[i] = Branch.create(children[i]); if (Branch.create(children[i]) == null) {
/* 124 */         nullBranchCount++;
/*     */       }
/*     */     } 
/*     */     
/* 128 */     if (nullBranchCount > 1) return null;
/*     */ 
/*     */     
/* 131 */     int anomaly = -1;
/*     */     
/* 133 */     if (Branch.access$000(branches[0], branches[1])) {
/* 134 */       branch = branches[0];
/*     */     }
/* 136 */     else if (Branch.access$000(branches[0], branches[2])) {
/* 137 */       branch = branches[0];
/* 138 */       anomaly = 1;
/*     */     }
/* 140 */     else if (Branch.access$000(branches[1], branches[2])) {
/* 141 */       branch = branches[1];
/* 142 */       anomaly = 0;
/*     */     } else {
/* 144 */       return null;
/*     */     } 
/*     */     
/* 147 */     for (int j = 2; j < branches.length; j++) {
/* 148 */       if (!Branch.access$000(branch, branches[j])) {
/* 149 */         if (anomaly != -1)
/* 150 */           return null; 
/* 151 */         anomaly = j;
/*     */       } 
/*     */     } 
/*     */     
/* 155 */     if (anomaly != -1) {
/* 156 */       branches[anomaly] = null;
/*     */     }
/* 158 */     LookupTable t = new LookupTable(this, this.id++);
/* 159 */     for (int k = 0; k < branches.length; k++) {
/* 160 */       if (branches[k] != null) {
/*     */         
/* 162 */         LookupTable.Entry e = branches[k].toEntry();
/* 163 */         if (!t.isConsistentWith(e)) {
/* 164 */           return null;
/*     */         }
/* 166 */         t.add(e);
/*     */       } 
/*     */     } 
/* 169 */     return new LookupTableUse(t, (anomaly == -1) ? null : children[anomaly], branch.attName);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generator\LookupTableFactory.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */