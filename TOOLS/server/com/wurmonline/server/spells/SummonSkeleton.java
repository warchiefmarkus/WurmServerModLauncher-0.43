/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Server;
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
/*     */ public class SummonSkeleton
/*     */   extends KarmaSpell
/*     */   implements CreatureTemplateIds
/*     */ {
/*  44 */   private static final Logger logger = Logger.getLogger(SummonSkeleton.class.getName());
/*     */ 
/*     */   
/*     */   public static final int RANGE = 24;
/*     */ 
/*     */   
/*     */   public SummonSkeleton() {
/*  51 */     super("Summon Skeleton", 631, 30, 500, 30, 1, 180000L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, int tilex, int tiley, int layer) {
/*  58 */     if ((performer.getFollowers()).length > 0) {
/*     */       
/*  60 */       performer.getCommunicator().sendNormalServerMessage("You are too busy leading other creatures and can not focus on summoning.", (byte)3);
/*     */ 
/*     */       
/*  63 */       return false;
/*     */     } 
/*     */     
/*  66 */     if (layer < 0)
/*     */     {
/*  68 */       if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(tilex, tiley)))) {
/*     */         
/*  70 */         performer.getCommunicator().sendNormalServerMessage("You can not summon there.", (byte)3);
/*  71 */         return false;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/*  77 */       if (Zones.calculateHeight(((tilex << 2) + 2), ((tiley << 2) + 2), performer.isOnSurface()) < 0.0F)
/*     */       {
/*  79 */         performer.getCommunicator().sendNormalServerMessage("You can not summon there.", (byte)3);
/*  80 */         return false;
/*     */       }
/*     */     
/*  83 */     } catch (Exception ex) {
/*     */       
/*  85 */       performer.getCommunicator().sendNormalServerMessage("You can not summon there.", (byte)3);
/*  86 */       return false;
/*     */     } 
/*  88 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/*  95 */     if (performer.knowsKarmaSpell(631))
/*     */     {
/*  97 */       for (int nums = 0; nums < Math.max(2.0D, power / 10.0D); nums++) {
/*  98 */         spawnCreature(87, performer);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private final void spawnCreature(int templateId, Creature performer) {
/*     */     try {
/* 106 */       CreatureTemplate ct = CreatureTemplateFactory.getInstance().getTemplate(templateId);
/* 107 */       byte sex = 0;
/* 108 */       if (Server.rand.nextInt(2) == 0)
/* 109 */         sex = 1; 
/* 110 */       byte ctype = (byte)Math.max(0, Server.rand.nextInt(22) - 10);
/* 111 */       if (Server.rand.nextInt(20) == 0)
/* 112 */         ctype = 99; 
/* 113 */       Creature c = Creature.doNew(templateId, true, performer.getPosX() - 4.0F + Server.rand.nextFloat() * 9.0F, performer
/* 114 */           .getPosY() - 4.0F + Server.rand
/* 115 */           .nextFloat() * 9.0F, Server.rand
/* 116 */           .nextFloat() * 360.0F, performer.getLayer(), ct.getName(), sex, performer.getKingdomId(), ctype, true);
/* 117 */       c.setLoyalty(100.0F);
/* 118 */       c.setDominator(performer.getWurmId());
/* 119 */       performer.setPet(c.getWurmId());
/* 120 */       c.setLeader(performer);
/*     */     }
/* 122 */     catch (NoSuchCreatureTemplateException nst) {
/*     */       
/* 124 */       logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*     */     }
/* 126 */     catch (Exception ex) {
/*     */       
/* 128 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\SummonSkeleton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */