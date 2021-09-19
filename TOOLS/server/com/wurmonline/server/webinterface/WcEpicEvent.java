/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.HistoryManager;
/*     */ import com.wurmonline.server.Message;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.CreatureTemplate;
/*     */ import com.wurmonline.server.creatures.CreatureTemplateCreator;
/*     */ import com.wurmonline.server.creatures.CreatureTemplateFactory;
/*     */ import com.wurmonline.server.creatures.CreatureTemplateIds;
/*     */ import com.wurmonline.server.creatures.Creatures;
/*     */ import com.wurmonline.server.creatures.DbCreatureStatus;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureTemplateException;
/*     */ import com.wurmonline.server.deities.Deities;
/*     */ import com.wurmonline.server.deities.Deity;
/*     */ import com.wurmonline.server.epic.Effectuator;
/*     */ import com.wurmonline.server.epic.SynchedEpicEffect;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemFactory;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import com.wurmonline.shared.constants.CounterTypes;
/*     */ import com.wurmonline.shared.util.StreamUtilities;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
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
/*     */ public class WcEpicEvent
/*     */   extends WebCommand
/*     */   implements CreatureTemplateIds, CounterTypes
/*     */ {
/*  64 */   private static final Logger logger = Logger.getLogger(WcEpicEvent.class.getName());
/*     */   private int effectNumber;
/*     */   private long deityNumber;
/*     */   private int templateId;
/*     */   private int bonusEffectNum;
/*  69 */   private String eventString = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean resettingKarma = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WcEpicEvent(long _id, int effectNum, long deityNum, int selectedTemplate, int bonusEffect, String eventDesc, boolean resetKarma) {
/*  83 */     super(_id, (short)9);
/*  84 */     this.effectNumber = effectNum;
/*  85 */     this.deityNumber = deityNum;
/*  86 */     this.templateId = selectedTemplate;
/*  87 */     this.bonusEffectNum = bonusEffect;
/*  88 */     if (this.bonusEffectNum < 0)
/*  89 */       this.bonusEffectNum = Server.rand.nextInt(4); 
/*  90 */     this.eventString = eventDesc;
/*  91 */     this.resettingKarma = resetKarma;
/*  92 */     this.isRestrictedEpic = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WcEpicEvent(long _id, byte[] _data) {
/* 101 */     super(_id, (short)9, _data);
/* 102 */     this.isRestrictedEpic = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean autoForward() {
/* 113 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   byte[] encode() {
/* 124 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 125 */     DataOutputStream dos = null;
/* 126 */     byte[] barr = null;
/*     */     
/*     */     try {
/* 129 */       dos = new DataOutputStream(bos);
/* 130 */       dos.writeInt(this.effectNumber);
/* 131 */       dos.writeLong(this.deityNumber);
/* 132 */       dos.writeInt(this.templateId);
/* 133 */       dos.writeInt(this.bonusEffectNum);
/* 134 */       dos.writeUTF(this.eventString);
/* 135 */       dos.writeBoolean(this.resettingKarma);
/* 136 */       dos.flush();
/* 137 */       dos.close();
/*     */     }
/* 139 */     catch (Exception ex) {
/*     */       
/* 141 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 145 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 146 */       barr = bos.toByteArray();
/* 147 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 148 */       setData(barr);
/*     */     } 
/* 150 */     return barr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/* 161 */     DataInputStream dis = null;
/*     */     
/*     */     try {
/* 164 */       dis = new DataInputStream(new ByteArrayInputStream(getData()));
/* 165 */       this.effectNumber = dis.readInt();
/* 166 */       this.deityNumber = dis.readLong();
/* 167 */       this.templateId = dis.readInt();
/* 168 */       this.bonusEffectNum = dis.readInt();
/* 169 */       this.eventString = dis.readUTF();
/* 170 */       this.resettingKarma = dis.readBoolean();
/* 171 */       if (this.bonusEffectNum > 0) {
/*     */         
/* 173 */         if (this.bonusEffectNum == 4 || this.bonusEffectNum == 5) {
/*     */ 
/*     */           
/* 176 */           String creatureName = "Unknown";
/* 177 */           if (WurmId.getType(this.deityNumber) == 0) {
/*     */ 
/*     */             
/* 180 */             PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(this.deityNumber);
/* 181 */             if (pinf != null) {
/*     */               
/* 183 */               if (!pinf.loaded) {
/*     */                 
/*     */                 try {
/*     */                   
/* 187 */                   pinf.load();
/* 188 */                   creatureName = pinf.getName();
/*     */                 }
/* 190 */                 catch (IOException iox) {
/*     */                   return;
/*     */                 } 
/*     */               }
/*     */ 
/*     */               
/* 196 */               if (pinf.currentServer != Servers.localServer.id) {
/*     */                 return;
/*     */               }
/*     */             } else {
/*     */               return;
/*     */             } 
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/* 209 */             boolean fragment = (this.bonusEffectNum == 5);
/* 210 */             Item i = ItemFactory.createItem(fragment ? 1307 : this.templateId, (80 + Server.rand.nextInt(20)), this.eventString);
/*     */             
/* 212 */             if (fragment) {
/*     */               
/* 214 */               i.setRealTemplate(this.templateId);
/* 215 */               i.setAuxData((byte)1);
/*     */             } 
/*     */             
/* 218 */             if (i.getTemplateId() == 465) {
/*     */ 
/*     */               
/* 221 */               i.setData1(CreatureTemplateCreator.getRandomDragonOrDrakeId());
/*     */             }
/* 223 */             else if (i.getTemplateId() == 794) {
/*     */               
/* 225 */               Deity[] deityArr = Deities.getDeities();
/* 226 */               for (Deity de : deityArr) {
/*     */                 
/* 228 */                 if (de.getName().equalsIgnoreCase(this.eventString))
/*     */                 {
/* 230 */                   i.setAuxData((byte)de.getNumber());
/* 231 */                   i.setData1(577);
/*     */                 }
/*     */               
/*     */               } 
/* 235 */             } else if (i.isAbility()) {
/*     */               
/* 237 */               i.setAuxData((byte)2);
/*     */             } 
/* 239 */             if (WurmId.getType(this.deityNumber) == 1) {
/*     */ 
/*     */               
/*     */               try {
/* 243 */                 Creature c = Creatures.getInstance().getCreature(this.deityNumber);
/* 244 */                 c.getInventory().insertItem(i, true);
/* 245 */                 creatureName = c.getName();
/*     */               }
/* 247 */               catch (NoSuchCreatureException noSuchCreatureException) {}
/*     */             } else {
/*     */ 
/*     */               
/*     */               try {
/*     */ 
/*     */ 
/*     */                 
/* 255 */                 Player p = Players.getInstance().getPlayer(this.deityNumber);
/* 256 */                 p.getInventory().insertItem(i);
/* 257 */                 creatureName = p.getName();
/*     */               }
/* 259 */               catch (NoSuchPlayerException nsp) {
/*     */                 
/* 261 */                 long inventory = DbCreatureStatus.getInventoryIdFor(this.deityNumber);
/* 262 */                 i.setParentId(inventory, true);
/* 263 */                 i.setOwnerId(this.deityNumber);
/*     */               } 
/*     */             } 
/*     */ 
/*     */             
/* 268 */             HistoryManager.addHistory(creatureName, "receives the " + i.getName() + " from " + this.eventString + ".");
/*     */           }
/* 270 */           catch (Exception nsi) {
/*     */             
/* 272 */             logger.log(Level.WARNING, nsi.getMessage(), nsi);
/*     */           }
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 278 */           Message mess = new Message(null, (byte)5, ":Event", this.eventString);
/* 279 */           Server.getInstance().addMessage(mess);
/* 280 */           SynchedEpicEffect eff = new SynchedEpicEffect(3);
/* 281 */           eff.setDeityNumber(this.deityNumber);
/* 282 */           eff.setBonusEffectNum(this.bonusEffectNum);
/* 283 */           eff.setResetKarma(this.resettingKarma);
/* 284 */           eff.setEffectNumber(this.effectNumber);
/* 285 */           Effectuator.addEpicEffect(eff);
/*     */ 
/*     */           
/* 288 */           if (this.templateId > 0) {
/*     */             try
/*     */             {
/*     */               
/* 292 */               CreatureTemplate ct = CreatureTemplateFactory.getInstance().getTemplate(this.templateId);
/* 293 */               String effectDesc = "Some " + ct.getName() + " arrive.";
/* 294 */               Message mess2 = new Message(null, (byte)5, ":Event", effectDesc);
/* 295 */               Server.getInstance().addMessage(mess2);
/* 296 */               SynchedEpicEffect eff2 = new SynchedEpicEffect(1);
/* 297 */               eff2.setDeityNumber(this.deityNumber);
/* 298 */               eff2.setCreatureTemplateId(this.templateId);
/* 299 */               Effectuator.addEpicEffect(eff2);
/*     */             }
/* 301 */             catch (NoSuchCreatureTemplateException nst)
/*     */             {
/* 303 */               logger.log(Level.WARNING, nst.getMessage());
/*     */             }
/*     */           
/*     */           }
/*     */         } 
/* 308 */       } else if (this.effectNumber > 0 && Servers.localServer.PVPSERVER) {
/*     */         
/* 310 */         if (this.effectNumber == 5)
/*     */         {
/* 312 */           Message mess = new Message(null, (byte)5, ":Event", this.eventString);
/* 313 */           Server.getInstance().addMessage(mess);
/* 314 */           SynchedEpicEffect eff = new SynchedEpicEffect(1);
/* 315 */           eff.setDeityNumber(this.deityNumber);
/* 316 */           eff.setCreatureTemplateId(this.templateId);
/* 317 */           eff.setResetKarma(this.resettingKarma);
/* 318 */           Effectuator.addEpicEffect(eff);
/*     */         }
/*     */         else
/*     */         {
/* 322 */           Message mess = new Message(null, (byte)5, ":Event", this.eventString);
/* 323 */           Server.getInstance().addMessage(mess);
/* 324 */           SynchedEpicEffect eff = new SynchedEpicEffect(2);
/* 325 */           eff.setDeityNumber(this.deityNumber);
/* 326 */           eff.setCreatureTemplateId(this.templateId);
/* 327 */           eff.setEffectNumber(this.effectNumber);
/* 328 */           eff.setBonusEffectNum(this.bonusEffectNum);
/* 329 */           eff.setEventString(this.eventString);
/* 330 */           eff.setResetKarma(this.resettingKarma);
/* 331 */           Effectuator.addEpicEffect(eff);
/*     */         }
/*     */       
/*     */       } 
/* 335 */     } catch (IOException ex) {
/*     */       
/* 337 */       logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 341 */       StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcEpicEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */