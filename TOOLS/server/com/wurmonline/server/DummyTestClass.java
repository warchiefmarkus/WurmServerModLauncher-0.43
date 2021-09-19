/*     */ package com.wurmonline.server;
/*     */ 
/*     */ import java.io.File;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DummyTestClass
/*     */   extends XMLSerializer
/*     */ {
/*     */   @Saved
/*  28 */   long test = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getTest() {
/*  38 */     return this.test;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTest(long aTest) {
/*  49 */     this.test = aTest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMyClass() {
/*  59 */     return this.myClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMyClass(String aMyClass) {
/*  70 */     this.myClass = aMyClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getDontSave() {
/*  80 */     return 0.9333222F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSaveThis(float aSaveThis) {
/*  91 */     this.saveThis = aSaveThis;
/*     */   }
/*     */   @Saved
/*  94 */   String myClass = "my Class is dummy";
/*     */   @Saved
/*  96 */   float saveThis = 3.24324E-4F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getSaveThis() {
/* 106 */     return this.saveThis;
/*     */   }
/*     */   
/* 109 */   final float dontSave = 0.9333222F;
/*     */ 
/*     */   
/*     */   public final DummyTestClass createInstanceAndCallLoadXML(File file) {
/* 113 */     loadXML(file);
/* 114 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\DummyTestClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */