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
/*     */ public class SummonWorg
/*     */   extends KarmaSpell
/*     */   implements CreatureTemplateIds
/*     */ {
/*  43 */   private static final Logger logger = Logger.getLogger(SummonWorg.class.getName());
/*     */ 
/*     */   
/*     */   public static final int RANGE = 24;
/*     */ 
/*     */   
/*     */   public SummonWorg() {
/*  50 */     super("Summon Worg", 629, 30, 500, 30, 1, 180000L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, int tilex, int tiley, int layer) {
/*  57 */     if ((performer.getFollowers()).length > 0) {
/*     */       
/*  59 */       performer.getCommunicator().sendNormalServerMessage("You are too busy leading other creatures and can not focus on summoning.", (byte)3);
/*     */ 
/*     */       
/*  62 */       return false;
/*     */     } 
/*     */     
/*  65 */     if (layer < 0)
/*     */     {
/*  67 */       if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(tilex, tiley)))) {
/*     */         
/*  69 */         performer.getCommunicator().sendNormalServerMessage("You can not summon there.", (byte)3);
/*     */         
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
/*  95 */     if (performer.knowsKarmaSpell(629)) {
/*  96 */       spawnCreature(86, performer);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private final void spawnCreature(int templateId, Creature performer) {
/*     */     try {
/* 103 */       CreatureTemplate ct = CreatureTemplateFactory.getInstance().getTemplate(templateId);
/* 104 */       byte sex = 0;
/* 105 */       if (Server.rand.nextInt(2) == 0)
/* 106 */         sex = 1; 
/* 107 */       byte ctype = (byte)Math.max(0, Server.rand.nextInt(22) - 10);
/* 108 */       if (Server.rand.nextInt(20) == 0)
/* 109 */         ctype = 99; 
/* 110 */       Creature c = Creature.doNew(templateId, true, performer.getPosX() - 4.0F + Server.rand.nextFloat() * 9.0F, performer
/* 111 */           .getPosY() - 4.0F + Server.rand
/* 112 */           .nextFloat() * 9.0F, Server.rand
/* 113 */           .nextFloat() * 360.0F, performer.getLayer(), ct.getName(), sex, performer.getKingdomId(), ctype, true);
/* 114 */       if (performer.getPet() != null) {
/*     */         
/* 116 */         performer.getCommunicator().sendNormalServerMessage("You can't keep control over " + performer
/* 117 */             .getPet().getNameWithGenus() + " as well.", (byte)2);
/*     */         
/* 119 */         performer.getPet().setDominator(-10L);
/*     */       } 
/* 121 */       c.setLoyalty(100.0F);
/* 122 */       c.setDominator(performer.getWurmId());
/* 123 */       performer.setPet(c.getWurmId());
/* 124 */       c.setLeader(performer);
/*     */     }
/* 126 */     catch (NoSuchCreatureTemplateException nst) {
/*     */       
/* 128 */       logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*     */     }
/* 130 */     catch (Exception ex) {
/*     */       
/* 132 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\SummonWorg.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */