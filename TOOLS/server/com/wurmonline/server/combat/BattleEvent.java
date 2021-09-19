/*     */ package com.wurmonline.server.combat;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.behaviours.Actions;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BattleEvent
/*     */   implements MiscConstants
/*     */ {
/*     */   public static final short BATTLEACTION_ATTACK = -1;
/*     */   public static final short BATTLEACTION_LEAVE = -2;
/*     */   public static final short BATTLEACTION_DIE = -3;
/*  34 */   private final SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
/*     */ 
/*     */   
/*     */   private final short act;
/*     */   
/*     */   private final String perf;
/*     */   
/*     */   private final String rec;
/*     */   
/*     */   private String longString;
/*     */   
/*     */   private final long time;
/*     */ 
/*     */   
/*     */   BattleEvent(short action, String performer) {
/*  49 */     this(action, performer, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public BattleEvent(short action, String performer, @Nullable String receiver) {
/*  54 */     this.act = action;
/*  55 */     this.perf = performer;
/*  56 */     this.rec = receiver;
/*  57 */     this.time = System.currentTimeMillis();
/*     */   }
/*     */ 
/*     */   
/*     */   public BattleEvent(short action, String performer, String receiver, String longDesc) {
/*  62 */     this.act = action;
/*  63 */     this.perf = performer;
/*  64 */     this.rec = receiver;
/*  65 */     this.time = System.currentTimeMillis();
/*  66 */     this.longString = longDesc;
/*     */   }
/*     */ 
/*     */   
/*     */   short getAction() {
/*  71 */     return this.act;
/*     */   }
/*     */ 
/*     */   
/*     */   String getPerformer() {
/*  76 */     return this.perf;
/*     */   }
/*     */ 
/*     */   
/*     */   String getReceiver() {
/*  81 */     return this.rec;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long getTime() {
/*  90 */     return this.time;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  96 */     StringBuilder buf = new StringBuilder();
/*     */     
/*  98 */     buf.append(this.df.format(new Date(this.time)));
/*  99 */     buf.append(" : ");
/* 100 */     if (this.longString != null) {
/* 101 */       buf.append(this.longString);
/* 102 */     } else if (this.act == -1) {
/*     */       
/* 104 */       if (this.rec != null)
/*     */       {
/* 106 */         buf.append(this.perf);
/* 107 */         buf.append(" attacks ");
/* 108 */         buf.append(this.rec);
/* 109 */         buf.append(".");
/*     */       }
/*     */       else
/*     */       {
/* 113 */         buf.append(this.perf);
/* 114 */         buf.append(" joins the fray.");
/*     */       }
/*     */     
/* 117 */     } else if (this.act == -2) {
/*     */       
/* 119 */       buf.append(this.perf);
/* 120 */       buf.append(" leaves the battle.");
/*     */     }
/* 122 */     else if (this.act == -3) {
/*     */       
/* 124 */       if (this.rec != null)
/*     */       {
/* 126 */         buf.append(this.perf);
/* 127 */         buf.append(" dies by the hands of ");
/* 128 */         buf.append(this.rec);
/* 129 */         buf.append(".");
/*     */       }
/*     */       else
/*     */       {
/* 133 */         buf.append(this.perf);
/* 134 */         buf.append(" dies.");
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 139 */       String verb = Actions.actionEntrys[this.act].getActionString();
/* 140 */       if (this.rec != null) {
/*     */         
/* 142 */         buf.append(this.perf);
/* 143 */         buf.append(" ");
/* 144 */         buf.append(verb);
/* 145 */         buf.append("s at ");
/* 146 */         buf.append(this.rec);
/* 147 */         buf.append(".");
/*     */       }
/*     */       else {
/*     */         
/* 151 */         buf.append(this.perf);
/* 152 */         buf.append(" ");
/* 153 */         buf.append(verb);
/* 154 */         buf.append("s ");
/* 155 */         buf.append(".");
/*     */       } 
/*     */     } 
/* 158 */     buf.append("<br>");
/* 159 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\combat\BattleEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */