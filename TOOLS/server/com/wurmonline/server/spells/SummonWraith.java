/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.WurmCalendar;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.CreatureTemplate;
/*     */ import com.wurmonline.server.creatures.CreatureTemplateFactory;
/*     */ import com.wurmonline.server.creatures.CreatureTemplateIds;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureTemplateException;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.zones.Zones;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SummonWraith
/*     */   extends KarmaSpell
/*     */   implements CreatureTemplateIds
/*     */ {
/*  45 */   private static final Logger logger = Logger.getLogger(SummonWraith.class.getName());
/*     */ 
/*     */   
/*     */   public static final int RANGE = 24;
/*     */ 
/*     */   
/*     */   public SummonWraith() {
/*  52 */     super("Summon Wraith", 630, 30, 500, 30, 1, 180000L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, int tilex, int tiley, int layer) {
/*  59 */     if (performer.knowsKarmaSpell(630))
/*     */     {
/*  61 */       if (layer >= 0 || performer.getLayer() >= 0)
/*     */       {
/*  63 */         if (!WurmCalendar.isNight()) {
/*     */           
/*  65 */           performer.getCommunicator().sendNormalServerMessage("You cannot summon this above ground during daytime.", (byte)3);
/*     */           
/*  67 */           return false;
/*     */         } 
/*     */       }
/*     */     }
/*     */     
/*  72 */     if ((performer.getFollowers()).length > 0) {
/*     */       
/*  74 */       performer.getCommunicator().sendNormalServerMessage("You are too busy leading other creatures and can not focus on summoning.", (byte)3);
/*     */       
/*  76 */       return false;
/*     */     } 
/*     */     
/*  79 */     if (layer < 0)
/*     */     {
/*  81 */       if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(tilex, tiley)))) {
/*     */         
/*  83 */         performer.getCommunicator().sendNormalServerMessage("You can not summon there.", (byte)3);
/*  84 */         return false;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/*  90 */       if (Zones.calculateHeight(((tilex << 2) + 2), ((tiley << 2) + 2), performer.isOnSurface()) < 0.0F)
/*     */       {
/*  92 */         performer.getCommunicator().sendNormalServerMessage("You can not summon there.", (byte)3);
/*  93 */         return false;
/*     */       }
/*     */     
/*  96 */     } catch (Exception ex) {
/*     */       
/*  98 */       performer.getCommunicator().sendNormalServerMessage("You can not summon there.", (byte)3);
/*  99 */       return false;
/*     */     } 
/* 101 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/* 108 */     if (performer.knowsKarmaSpell(630))
/*     */     {
/* 110 */       if (WurmCalendar.isNight() || layer < 0 || performer.getLayer() < 0)
/*     */       {
/* 112 */         spawnCreature(88, performer);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final void spawnCreature(int templateId, Creature performer) {
/*     */     try {
/* 121 */       CreatureTemplate ct = CreatureTemplateFactory.getInstance().getTemplate(templateId);
/* 122 */       byte sex = 0;
/* 123 */       if (Server.rand.nextInt(2) == 0)
/* 124 */         sex = 1; 
/* 125 */       byte ctype = (byte)Math.max(0, Server.rand.nextInt(22) - 10);
/* 126 */       if (Server.rand.nextInt(20) == 0)
/* 127 */         ctype = 99; 
/* 128 */       Creature c = Creature.doNew(templateId, true, performer.getPosX() - 4.0F + Server.rand.nextFloat() * 9.0F, performer
/* 129 */           .getPosY() - 4.0F + Server.rand
/* 130 */           .nextFloat() * 9.0F, Server.rand
/* 131 */           .nextFloat() * 360.0F, performer.getLayer(), ct.getName(), sex, performer.getKingdomId(), ctype, true);
/* 132 */       c.setLoyalty(100.0F);
/* 133 */       c.setDominator(performer.getWurmId());
/* 134 */       performer.setPet(c.getWurmId());
/* 135 */       c.setLeader(performer);
/*     */     }
/* 137 */     catch (NoSuchCreatureTemplateException nst) {
/*     */       
/* 139 */       logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*     */     }
/* 141 */     catch (Exception ex) {
/*     */       
/* 143 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\SummonWraith.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */