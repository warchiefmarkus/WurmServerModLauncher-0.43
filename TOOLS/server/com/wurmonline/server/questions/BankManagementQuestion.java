/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.banks.Bank;
/*     */ import com.wurmonline.server.banks.BankUnavailableException;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.villages.NoSuchVillageException;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.Villages;
/*     */ import java.util.Properties;
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
/*     */ public final class BankManagementQuestion
/*     */   extends Question
/*     */   implements MiscConstants, TimeConstants
/*     */ {
/*     */   private final Bank bank;
/*     */   
/*     */   public BankManagementQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget, @Nullable Bank aBank) {
/*  60 */     super(aResponder, aTitle, aQuestion, 33, aTarget);
/*  61 */     this.bank = aBank;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  72 */     setAnswer(answers);
/*  73 */     if (!getResponder().isGuest()) {
/*     */ 
/*     */       
/*     */       try {
/*  77 */         Item token = Items.getItem(this.target);
/*  78 */         Village village = Villages.getVillage(token.getData2());
/*  79 */         String key = "open";
/*  80 */         String val = getAnswer().getProperty(key);
/*     */         
/*  82 */         if (val != null && val.equals("true")) {
/*     */           
/*  84 */           ((Player)getResponder()).startBank(village);
/*     */         }
/*  86 */         else if (this.bank != null) {
/*     */           
/*  88 */           if (!this.bank.open) {
/*     */             
/*  90 */             key = "move";
/*  91 */             val = getAnswer().getProperty(key);
/*  92 */             if (val != null && val.equals("true"))
/*     */             {
/*  94 */               if (this.bank.targetVillage > 0) {
/*     */                 
/*     */                 try
/*     */                 {
/*     */                   
/*  99 */                   Village village1 = Villages.getVillage(this.bank.targetVillage);
/*     */                 }
/* 101 */                 catch (NoSuchVillageException nsv)
/*     */                 {
/* 103 */                   this.bank.stopMoving();
/* 104 */                   getResponder().getCommunicator()
/* 105 */                     .sendNormalServerMessage("The bank account has moved here.");
/*     */                 }
/*     */               
/* 108 */               } else if (this.bank.targetVillage != village.getId()) {
/*     */                 
/* 110 */                 boolean disbanded = false;
/*     */                 
/*     */                 try {
/* 113 */                   this.bank.getCurrentVillage();
/*     */                 }
/* 115 */                 catch (BankUnavailableException nub) {
/*     */                   
/* 117 */                   disbanded = true;
/*     */                 } 
/* 119 */                 this.bank.startMoving(village.getId());
/* 120 */                 if (getResponder().getPower() > 0)
/*     */                 {
/* 122 */                   this.bank.stopMoving();
/* 123 */                   getResponder()
/* 124 */                     .getCommunicator()
/* 125 */                     .sendNormalServerMessage("The bank account has moved here because you're a cool person with some extra powers.");
/*     */ 
/*     */ 
/*     */                 
/*     */                 }
/* 130 */                 else if (disbanded)
/*     */                 {
/* 132 */                   this.bank.stopMoving();
/* 133 */                   getResponder().getCommunicator().sendNormalServerMessage("The bank account has moved here.");
/*     */                 }
/*     */               
/*     */               }
/*     */               else {
/*     */                 
/* 139 */                 getResponder().getCommunicator().sendNormalServerMessage("Your bank is already moving here.");
/*     */               } 
/*     */             }
/*     */           } else {
/* 143 */             getResponder().getCommunicator().sendNormalServerMessage("The bank account is open. You cannot manage it now.");
/*     */           }
/*     */         
/*     */         } 
/* 147 */       } catch (NoSuchItemException nsi) {
/*     */         
/* 149 */         getResponder().getCommunicator().sendNormalServerMessage("Failed to localize the village token for that request.");
/*     */       
/*     */       }
/* 152 */       catch (NoSuchVillageException nsv) {
/*     */         
/* 154 */         getResponder().getCommunicator().sendNormalServerMessage("Failed to localize the village for that request.");
/*     */       } 
/*     */     } else {
/*     */       
/* 158 */       getResponder().getCommunicator().sendNormalServerMessage("Guests may not open bank accounts.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 164 */     Village vill = null;
/* 165 */     if (this.bank != null && this.bank.targetVillage >= 0) {
/*     */       
/*     */       try {
/*     */         
/* 169 */         vill = Villages.getVillage(this.bank.targetVillage);
/*     */       }
/* 171 */       catch (NoSuchVillageException noSuchVillageException) {}
/*     */     }
/*     */ 
/*     */     
/* 175 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/* 176 */     if (!getResponder().isGuest())
/*     */     {
/* 178 */       if (this.bank == null) {
/*     */         
/* 180 */         buf.append("text{text='You may open a bank account here.'}");
/* 181 */         buf.append("text{text='You can only have one bank account, but you may move it.'}");
/* 182 */         buf
/* 183 */           .append("text{text='A bank account will currently not move items between servers, but your money will be available if you open a new bank account on another server.'}");
/* 184 */         buf.append("text{text='It will take 24 hours to move it to another settlement.'}");
/* 185 */         buf.append("text{text='You have to start the move from that settlement token.'}");
/* 186 */         buf
/* 187 */           .append("text{text='Note that some items decay and may disappear inside the bank account, although slower than outside.'}");
/* 188 */         buf
/* 189 */           .append("text{text='It will however be possible to rent a stasis spell to be cast upon the item in the future that will prevent decay.'}");
/*     */ 
/*     */         
/* 192 */         buf.append("text{text='Do you wish to open a bank account here?'}");
/*     */         
/* 194 */         buf.append("radio{ group='open'; id='true';text='Yes'}");
/* 195 */         buf.append("radio{ group='open'; id='false';text='No';selected='true'}");
/*     */       }
/*     */       else {
/*     */         
/* 199 */         this.bank.poll(System.currentTimeMillis());
/* 200 */         if (this.bank.startedMoving > 0L) {
/*     */           
/* 202 */           if (vill != null) {
/*     */             
/* 204 */             buf.append("text{text=\"Your bank is currently moving to " + vill.getName() + ".\"}");
/* 205 */             buf.append("text{text='It will arrive in approximately " + 
/* 206 */                 Server.getTimeFor(this.bank.startedMoving + 86400000L - System.currentTimeMillis()) + ".'}");
/* 207 */             if (vill != getResponder().getCurrentVillage()) {
/* 208 */               buf.append("text{text='It will take 24 hours to move your bank account here instead.'}");
/*     */             }
/*     */           } else {
/*     */             
/* 212 */             buf.append("text{text='Do you wish to move your bank account here?'}");
/* 213 */             buf.append("radio{ group='move'; id='true';text='Yes'}");
/* 214 */             buf.append("radio{ group='move'; id='false';text='No';selected='true'}");
/*     */           } 
/*     */         } else {
/*     */ 
/*     */           
/*     */           try {
/*     */             
/* 221 */             Village village = this.bank.getCurrentVillage();
/* 222 */             buf.append("text{text=\"Your bank is currently situated in " + village.getName() + ".\"}");
/*     */             
/* 224 */             buf.append("text{text='It will take 24 hours to move your bank account here.'}");
/*     */           }
/* 226 */           catch (BankUnavailableException bu) {
/*     */             
/* 228 */             buf
/* 229 */               .append("text{text='Your bank is not currently located in a village as its previous location has been disbanded.'}");
/*     */           } 
/*     */           
/* 232 */           buf.append("text{text='Do you wish to move your bank account here?'}");
/* 233 */           buf.append("radio{ group='move'; id='true';text='Yes'}");
/* 234 */           buf.append("radio{ group='move'; id='false';text='No';selected='true'}");
/*     */         } 
/*     */       } 
/*     */     }
/* 238 */     buf.append(createAnswerButton2());
/*     */     
/* 240 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\BankManagementQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */