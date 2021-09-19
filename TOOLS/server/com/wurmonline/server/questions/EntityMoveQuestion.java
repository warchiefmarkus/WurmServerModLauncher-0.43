/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.LoginServerWebConnection;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.deities.Deities;
/*     */ import com.wurmonline.server.deities.Deity;
/*     */ import com.wurmonline.server.epic.EpicServerStatus;
/*     */ import com.wurmonline.server.epic.MapHex;
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
/*     */ public final class EntityMoveQuestion
/*     */   extends Question
/*     */ {
/*     */   private Integer[] neighbours;
/*     */   private MapHex currentHex;
/*  41 */   private int deityToGuide = -1;
/*     */   
/*     */   private boolean secondStep = false;
/*  44 */   private static final Logger logger = Logger.getLogger(EntityMoveQuestion.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityMoveQuestion(Creature aResponder) {
/*  55 */     super(aResponder, "Guide the deities", "Whereto will you guide your deity?", 113, -10L);
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
/*  66 */     if (getResponder().getKarma() < 5000) {
/*     */       
/*  68 */       getResponder().getCommunicator().sendNormalServerMessage("You do not have enough karma to commune with " + 
/*  69 */           getResponder().getDeity().getName() + ".");
/*     */       return;
/*     */     } 
/*  72 */     String deityString = answers.getProperty("deityId");
/*  73 */     if (!this.secondStep) {
/*     */       
/*  75 */       if (deityString != null && deityString.length() > 0) {
/*     */ 
/*     */         
/*     */         try {
/*  79 */           int deityId = Integer.parseInt(deityString);
/*  80 */           if (deityId < 0) {
/*     */             
/*  82 */             getResponder().getCommunicator().sendNormalServerMessage("You refrain from disturbing the gods at this time.");
/*     */ 
/*     */             
/*     */             return;
/*     */           } 
/*     */           
/*  88 */           final Deity deity = Deities.getDeity(deityId);
/*     */           
/*  90 */           if (getResponder().getDeity() != null && deity != null) {
/*     */             
/*  92 */             EntityMoveQuestion nem = new EntityMoveQuestion(getResponder());
/*  93 */             nem.secondStep = true;
/*  94 */             nem.deityToGuide = deityId;
/*  95 */             nem.sendHexQuestion();
/*     */             
/*     */             return;
/*     */           } 
/*  99 */           getResponder().getCommunicator().sendAlertServerMessage("You fail to commune with the gods...");
/*     */         
/*     */         }
/* 102 */         catch (NumberFormatException nfre) {
/*     */           
/* 104 */           getResponder().getCommunicator().sendNormalServerMessage("Not a number for the desired deity...");
/* 105 */           logger.log(Level.INFO, "Not a number " + deityString);
/*     */         } 
/*     */       } else {
/*     */         
/* 109 */         getResponder().getCommunicator().sendNormalServerMessage("You refrain from disturbing the gods at this time.");
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 114 */     else if (getResponder().getDeity() != null) {
/*     */       
/* 116 */       final Deity deity = Deities.getDeity(this.deityToGuide);
/* 117 */       if (deity == null) {
/*     */         
/* 119 */         getResponder().getCommunicator().sendNormalServerMessage("Not a number for the desired deity...");
/*     */         
/*     */         return;
/*     */       } 
/* 123 */       String val = answers.getProperty("sethex");
/* 124 */       if (val != null && val.length() > 0) {
/*     */ 
/*     */         
/*     */         try {
/* 128 */           final int hexnum = Integer.parseInt(val);
/* 129 */           if (hexnum < 0) {
/*     */             
/* 131 */             getResponder().getCommunicator().sendNormalServerMessage("You refrain from disturbing the gods at this time.");
/*     */ 
/*     */             
/*     */             return;
/*     */           } 
/*     */ 
/*     */           
/* 138 */           boolean ok = false;
/* 139 */           for (Integer hexes : this.neighbours) {
/*     */             
/* 141 */             if (hexes.intValue() == hexnum) {
/*     */               
/* 143 */               ok = true;
/*     */               break;
/*     */             } 
/*     */           } 
/* 147 */           if (ok)
/*     */           {
/* 149 */             MapHex hex = EpicServerStatus.getValrei().getMapHex(hexnum);
/* 150 */             if (hex != null)
/*     */             {
/* 152 */               getResponder().getCommunicator().sendNormalServerMessage("You attempt to guide your deity..");
/*     */ 
/*     */               
/* 155 */               (new Thread(getResponder().getName() + "-guides-" + deity.getName() + "-Thread")
/*     */                 {
/*     */ 
/*     */                   
/*     */                   public final void run()
/*     */                   {
/* 161 */                     boolean success = (Server.rand.nextFloat() < 0.7F);
/* 162 */                     if (success) {
/*     */                       
/* 164 */                       LoginServerWebConnection lsw = new LoginServerWebConnection();
/*     */                       
/* 166 */                       success = lsw.requestDeityMove(EntityMoveQuestion.this.deityToGuide, hexnum, EntityMoveQuestion.this
/* 167 */                           .getResponder().getName());
/*     */                       
/*     */                       try {
/* 170 */                         Thread.sleep(2000L);
/*     */                       }
/* 172 */                       catch (InterruptedException interruptedException) {}
/*     */ 
/*     */ 
/*     */                       
/* 176 */                       if (success) {
/*     */                         
/* 178 */                         EntityMoveQuestion.logger.log(Level.INFO, EntityMoveQuestion.this.getResponder().getName() + " guides " + deity
/* 179 */                             .getName());
/* 180 */                         EntityMoveQuestion.this.getResponder().getCommunicator().sendSafeServerMessage("... and " + deity
/* 181 */                             .getName() + " heeds your advice!");
/*     */                         
/* 183 */                         EntityMoveQuestion.this.getResponder().modifyKarma(-5000);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                       
/*     */                       }
/*     */                       else {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                         
/* 199 */                         EntityMoveQuestion.this.getResponder().getCommunicator().sendNormalServerMessage("... but fail to penetrate the ether to Valrei.");
/*     */                         
/* 201 */                         EntityMoveQuestion.logger.log(Level.INFO, EntityMoveQuestion.this.getResponder().getName() + " guiding but connection to " + deity
/*     */                             
/* 203 */                             .getName() + " broken.");
/*     */                       } 
/*     */                     } else {
/*     */ 
/*     */                       
/*     */                       try {
/*     */ 
/*     */                         
/* 211 */                         Thread.sleep(3000L);
/*     */                       }
/* 213 */                       catch (InterruptedException interruptedException) {}
/*     */ 
/*     */ 
/*     */                       
/* 217 */                       EntityMoveQuestion.this.getResponder().getCommunicator().sendNormalServerMessage("... but you are ignored.");
/*     */                       
/* 219 */                       EntityMoveQuestion.this.getResponder().modifyKarma(-2500);
/* 220 */                       EntityMoveQuestion.logger.log(Level.INFO, EntityMoveQuestion.this.getResponder().getName() + " guiding ignored by " + deity
/* 221 */                           .getName() + ".");
/*     */                     } 
/*     */                   }
/* 224 */                 }).start();
/*     */             }
/*     */           
/*     */           }
/*     */         
/* 229 */         } catch (NumberFormatException nfre) {
/*     */           
/* 231 */           getResponder().getCommunicator().sendNormalServerMessage("Not a number for the desired position...");
/* 232 */           logger.log(Level.INFO, "Not a number " + val);
/*     */         } 
/*     */       } else {
/*     */         
/* 236 */         getResponder().getCommunicator().sendNormalServerMessage("You refrain from disturbing the gods at this time.");
/*     */       } 
/*     */     } else {
/*     */       
/* 240 */       getResponder().getCommunicator().sendNormalServerMessage("You no longer pray to a deity.");
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
/*     */   public void sendQuestion() {
/* 253 */     StringBuilder buf = new StringBuilder();
/* 254 */     buf.append(getBmlHeader());
/* 255 */     if (getResponder().getDeity() != null) {
/*     */       
/* 257 */       buf.append("text{text=\"You may spend karma in order to envision Valrei and attempt to guide your deity.\"}text{text=\"\"}");
/* 258 */       buf.append("text{text=\"There is 70% chance that you succed in getting your deities attention, and the cost will be 5000 karma if you do.\"}text{text=\"\"}");
/* 259 */       buf.append("text{text=\"If the request fails, you will only lose 2500 karma.\"}text{text=\"\"}");
/*     */       
/* 261 */       buf.append("radio{ group='deityId'; id='0';text='Do not Guide';selected='true'}");
/* 262 */       if (getResponder().getKingdomTemplateId() == 3)
/*     */       {
/* 264 */         buf.append("radio{ group='deityId'; id='4';text='Guide Libila'}");
/*     */       }
/* 266 */       else if (getResponder().getKingdomTemplateId() == 2)
/*     */       {
/* 268 */         buf.append("radio{ group='deityId'; id='2';text='Guide Magranon'}");
/*     */       }
/* 270 */       else if (getResponder().getKingdomTemplateId() == 1)
/*     */       {
/* 272 */         if ((getResponder().getDeity()).number == 3)
/*     */         {
/*     */           
/* 275 */           buf.append("radio{ group='deityId'; id='1';text='Guide Fo'}");
/*     */         
/*     */         }
/*     */         else
/*     */         {
/* 280 */           buf.append("radio{ group='deityId'; id='1';text='Guide Fo'}");
/*     */ 
/*     */         
/*     */         }
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */       
/* 296 */       buf.append("text{text=\"You no longer pray to a deity.\"}text{text=\"\"}");
/* 297 */     }  buf.append(createAnswerButton2());
/*     */     
/* 299 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void sendHexQuestion() {
/* 305 */     StringBuilder buf = new StringBuilder();
/* 306 */     buf.append(getBmlHeader());
/* 307 */     Integer currentInt = Deities.getPosition(this.deityToGuide);
/* 308 */     Deity deity = Deities.getDeity(this.deityToGuide);
/* 309 */     buf.append("text{text=\"Where do you want " + deity.getName() + " to go?\"}text{text=\"\"}");
/* 310 */     buf.append("radio{ group='sethex'; id=\"-1\";text=\"Never mind...\";selected=\"true\"};");
/* 311 */     if (currentInt != null) {
/*     */       
/* 313 */       this.currentHex = EpicServerStatus.getValrei().getMapHex(currentInt.intValue());
/* 314 */       if (this.currentHex != null) {
/*     */         
/* 316 */         this.neighbours = this.currentHex.getNearMapHexes();
/* 317 */         for (Integer i : this.neighbours) {
/*     */           
/* 319 */           MapHex maphex = EpicServerStatus.getValrei().getMapHex(i.intValue());
/* 320 */           if (maphex != null) {
/*     */             
/* 322 */             String trap = maphex.isTrap() ? " (trap)" : "";
/* 323 */             String slow = maphex.isSlow() ? " (slow)" : "";
/* 324 */             String teleport = maphex.isTeleport() ? " (shift)" : "";
/* 325 */             String strength = maphex.isStrength() ? " (strength)" : "";
/* 326 */             String vitality = maphex.isVitality() ? " (vitality)" : "";
/* 327 */             buf.append("radio{ group='sethex'; id=\"" + i.intValue() + "\";text=\"" + maphex.getName() + trap + slow + teleport + strength + vitality + "\"};");
/*     */           }
/*     */           else {
/*     */             
/* 331 */             logger.log(Level.WARNING, "NO HEX ON VALREI FOR " + i.intValue());
/*     */           } 
/* 333 */         }  if (this.neighbours == null || this.neighbours.length == 0)
/*     */         {
/* 335 */           buf.append("text{text=\"" + deity.getName() + " is not available for guidance now.\"}text{text=\"\"}");
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/* 340 */         buf.append("text{text=\"" + deity.getName() + " is not available for guidance now.\"}text{text=\"\"}");
/*     */       } 
/*     */     } else {
/*     */       
/* 344 */       buf.append("text{text=\"" + deity.getName() + " is not available for guidance now.\"}text{text=\"\"}");
/*     */     } 
/* 346 */     buf.append(createAnswerButton2());
/*     */     
/* 348 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\EntityMoveQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */