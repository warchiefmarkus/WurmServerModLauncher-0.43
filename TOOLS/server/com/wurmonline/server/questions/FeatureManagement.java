/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Features;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FeatureManagement
/*     */   extends Question
/*     */   implements TimeConstants
/*     */ {
/*  37 */   private static final Logger logger = Logger.getLogger(FeatureManagement.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean somethingChanged = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FeatureManagement(Creature aResponder, long aTarget) {
/*  48 */     super(aResponder, "Feature Management", "---- Manage Features ----", 102, aTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties aAnswer) {
/*  59 */     setAnswer(aAnswer);
/*  60 */     if (this.type == 0) {
/*     */       
/*  62 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*     */       return;
/*     */     } 
/*  65 */     if (this.type == 102)
/*     */     {
/*  67 */       if (getResponder().getPower() >= 4) {
/*     */ 
/*     */         
/*  70 */         Features.Feature[] features = Features.Feature.values();
/*  71 */         for (int x = 0; x < features.length; x++) {
/*     */           
/*  73 */           if (features[x].isShown()) {
/*     */             
/*  75 */             boolean enabled = aAnswer.getProperty("enable" + x).equals("true");
/*  76 */             boolean override = aAnswer.getProperty("override" + x).equals("true");
/*  77 */             boolean global = aAnswer.getProperty("global" + x).equals("true");
/*     */             
/*  79 */             if (global || features[x].isEnabled() != enabled || features[x].isOverridden() != override) {
/*  80 */               this.somethingChanged = true;
/*     */             }
/*  82 */             int featureId = features[x].getFeatureId();
/*     */             
/*  84 */             Features.Feature.setOverridden(Servers.getLocalServerId(), featureId, override, enabled, global);
/*     */           } 
/*     */         } 
/*  87 */         if (this.somethingChanged) {
/*     */ 
/*     */           
/*  90 */           FeatureManagement fm = new FeatureManagement(getResponder(), this.target);
/*  91 */           fm.sendQuestion();
/*     */         } 
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
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 113 */     StringBuilder buf = new StringBuilder();
/* 114 */     buf.append(getBmlHeader());
/* 115 */     if (getResponder().getPower() >= 4) {
/*     */       
/* 117 */       Features.Feature[] features = Features.Feature.values();
/*     */       
/* 119 */       buf.append("text{text=\"Current version is " + Features.getVerionsNo() + "\"};");
/* 120 */       buf.append("text{text=\"The version number for each feature is used to determine if it is enabled. If on Live then the number needs to be equal or less than the current one above, and if on Test then everything is enable by default. Override will change this behaviour.\"};");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 126 */       buf.append("table{rows=\"" + (features.length + 1) + "\";cols=\"5\";text{type=\"bold\";text=\"Enable  \"};text{type=\"bold\";text=\"Override  \"};text{type=\"bold\";text=\"Global  \"};text{type=\"bold\";text=\"Version\"};text{type=\"bold\";text=\"Name\"};");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 132 */       for (int x = 0; x < features.length; x++) {
/*     */         
/* 134 */         if (features[x].isShown()) {
/*     */           
/* 136 */           String colour = "";
/* 137 */           String bold = "";
/*     */           
/* 139 */           if (features[x].isAvailable() || features[x].isEnabled()) {
/*     */             
/* 141 */             buf.append("checkbox{id=\"enable" + x + "\";selected=\"" + features[x].isEnabled() + "\"};");
/* 142 */             buf.append("checkbox{id=\"override" + x + "\";selected=\"" + features[x].isOverridden() + "\"};");
/* 143 */             buf.append("checkbox{id=\"global" + x + "\";};");
/*     */           
/*     */           }
/*     */           else {
/*     */             
/* 148 */             buf.append("label{text=\"\"};");
/* 149 */             buf.append("label{text=\"\"};");
/* 150 */             buf.append("label{text=\"\"};");
/*     */           } 
/* 152 */           if (features[x].getState() == Features.State.FUTURE) {
/* 153 */             colour = "color=\"255,127,127\"";
/* 154 */           } else if (features[x].getState() == Features.State.INDEV) {
/* 155 */             colour = "color=\"127,127,255\"";
/* 156 */           } else if (features[x].isEnabled()) {
/* 157 */             colour = "color=\"127,255,127\"";
/* 158 */           }  if (features[x].isOverridden())
/* 159 */             bold = "type=\"bold\""; 
/* 160 */           buf.append("label{" + bold + colour + "text=\"" + features[x].getVersion() + "\"};");
/* 161 */           buf.append("label{" + bold + colour + "text=\"" + features[x].getName() + "\"};");
/*     */         } 
/*     */       } 
/* 164 */       buf.append("}");
/*     */       
/* 166 */       buf.append("text{type=\"bold\";text=\"--------------- Help -------------------\"}");
/* 167 */       buf.append("text{text=\"This is a list of the features that can be enabled or disabled by server or for all servers.\"}");
/* 168 */       buf.append("text{text=\"Setting the override will record the enabled state on the database.\"}");
/* 169 */       buf.append("text{text=\"Clearing the override will set the enabled back to it's default state.\"}");
/* 170 */       buf.append("text{type=\"bold\";text=\"Selecting global will attempt to change the same feature on all servers.\"}");
/* 171 */       buf.append("text{text=\"If anything gets altered then this window will be reshown.\"}");
/* 172 */       buf.append("text{type=\"italics\";text=\"Note only completed features are now shown.\"}");
/*     */       
/* 174 */       buf.append(createAnswerButton2());
/* 175 */       getResponder().getCommunicator().sendBml(500, 500, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\FeatureManagement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */