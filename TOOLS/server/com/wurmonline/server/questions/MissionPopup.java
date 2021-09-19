/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MissionPopup
/*     */   extends Question
/*     */ {
/*  25 */   private String top = "";
/*     */   
/*  27 */   private String toSend = "";
/*     */   
/*  29 */   private MissionManager root = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MissionPopup(Creature _responder, String _title, String _question) {
/*  40 */     super(_responder, _title, _question, 92, -10L);
/*  41 */     this.windowSizeX = 300;
/*  42 */     this.windowSizeY = 300;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties aAnswers) {
/*  53 */     if (this.root != null)
/*     */     {
/*  55 */       this.root.cloneAndSendManageEffect(null);
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
/*     */   public void sendQuestion() {
/*  67 */     StringBuilder buf = new StringBuilder();
/*     */     
/*  69 */     if (this.top.length() == 0) {
/*  70 */       buf.append("border{null;");
/*  71 */     } else if (this.top.contains("{") && this.toSend.contains("}")) {
/*     */       
/*  73 */       buf.append("border{" + this.top);
/*  74 */       if (!this.top.endsWith(";")) {
/*  75 */         buf.append(";");
/*     */       }
/*     */     } else {
/*  78 */       buf.append("border{center{text{type='bold';text=\"" + this.top + "\"}};");
/*  79 */     }  buf.append("null;scroll{vertical='true';horizontal='true';varray{rescale='false';passthrough{id='id';text='" + getId() + "'}");
/*     */ 
/*     */     
/*  82 */     buf.append("closebutton{id=\"submit\"};");
/*  83 */     buf.append("text{text=\"\"}");
/*  84 */     if (this.toSend.length() > 0) {
/*     */       
/*  86 */       if (this.toSend.contains("{") && this.toSend.contains("}")) {
/*  87 */         buf.append(this.toSend);
/*     */       }
/*     */       else {
/*     */         
/*  91 */         buf.append("text{size=\"" + this.windowSizeX + ",10\";text=\"" + this.toSend + "\"}");
/*     */       } 
/*  93 */       buf.append("text{text=\"\"}");
/*     */     } 
/*     */ 
/*     */     
/*  97 */     buf.append("harray{button{text='Ok';id='submit'}}}};null;null;}");
/*     */     
/*  99 */     getResponder().getCommunicator().sendBml(this.windowSizeX, this.windowSizeY, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setRoot(MissionManager aRoot) {
/* 109 */     this.root = aRoot;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getToSend() {
/* 119 */     return this.toSend;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setToSend(String aToSend) {
/* 130 */     this.toSend = aToSend;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTop() {
/* 140 */     return this.top;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTop(String topString) {
/* 151 */     this.top = topString;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\MissionPopup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */