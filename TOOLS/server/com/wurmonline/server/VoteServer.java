/*     */ package com.wurmonline.server;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VoteServer
/*     */ {
/*     */   private final int questionId;
/*     */   private final int serverId;
/*     */   private short total;
/*     */   private short count1;
/*     */   private short count2;
/*     */   private short count3;
/*     */   private short count4;
/*     */   
/*     */   public VoteServer(int aQuestionId, int aServerId) {
/*  44 */     this(aQuestionId, aServerId, (short)0, (short)0, (short)0, (short)0, (short)0);
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
/*     */   public VoteServer(int aQuestionId, int aServerId, short aTotal, short aCount1, short aCount2, short aCount3, short aCount4) {
/*  61 */     this.questionId = aQuestionId;
/*  62 */     this.serverId = aServerId;
/*  63 */     this.total = aTotal;
/*  64 */     this.count1 = aCount1;
/*  65 */     this.count2 = aCount2;
/*  66 */     this.count3 = aCount3;
/*  67 */     this.count4 = aCount4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getTotal() {
/*  77 */     return this.total;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTotal(short aTotal) {
/*  88 */     this.total = aTotal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getCount1() {
/*  98 */     return this.count1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCount1(short aCount1) {
/* 109 */     this.count1 = aCount1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getCount2() {
/* 119 */     return this.count2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCount2(short aCount2) {
/* 130 */     this.count2 = aCount2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getCount3() {
/* 140 */     return this.count3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCount3(short aCount3) {
/* 151 */     this.count3 = aCount3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getCount4() {
/* 161 */     return this.count4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCount4(short aCount4) {
/* 172 */     this.count4 = aCount4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getQuestionId() {
/* 182 */     return this.questionId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getServerId() {
/* 192 */     return this.serverId;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\VoteServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */