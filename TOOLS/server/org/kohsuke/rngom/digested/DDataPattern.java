/*     */ package org.kohsuke.rngom.digested;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.kohsuke.rngom.ast.om.Location;
/*     */ import org.kohsuke.rngom.parse.Context;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DDataPattern
/*     */   extends DPattern
/*     */ {
/*     */   DPattern except;
/*     */   String datatypeLibrary;
/*     */   String type;
/*  18 */   final List<Param> params = new ArrayList<Param>();
/*     */ 
/*     */   
/*     */   public final class Param
/*     */   {
/*     */     String name;
/*     */     
/*     */     String value;
/*     */     Context context;
/*     */     String ns;
/*     */     Location loc;
/*     */     Annotation anno;
/*     */     
/*     */     public Param(String name, String value, Context context, String ns, Location loc, Annotation anno) {
/*  32 */       this.name = name;
/*  33 */       this.value = value;
/*  34 */       this.context = context;
/*  35 */       this.ns = ns;
/*  36 */       this.loc = loc;
/*  37 */       this.anno = anno;
/*     */     }
/*     */     
/*     */     public String getName() {
/*  41 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getValue() {
/*  45 */       return this.value;
/*     */     }
/*     */     
/*     */     public Context getContext() {
/*  49 */       return this.context;
/*     */     }
/*     */     
/*     */     public String getNs() {
/*  53 */       return this.ns;
/*     */     }
/*     */     
/*     */     public Location getLoc() {
/*  57 */       return this.loc;
/*     */     }
/*     */     
/*     */     public Annotation getAnno() {
/*  61 */       return this.anno;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDatatypeLibrary() {
/*  72 */     return this.datatypeLibrary;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/*  82 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Param> getParams() {
/*  92 */     return this.params;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DPattern getExcept() {
/* 101 */     return this.except;
/*     */   }
/*     */   
/*     */   public boolean isNullable() {
/* 105 */     return false;
/*     */   }
/*     */   
/*     */   public Object accept(DPatternVisitor visitor) {
/* 109 */     return visitor.onData(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\DDataPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */