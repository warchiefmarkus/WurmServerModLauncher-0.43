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
/*     */ public class Summon
/*     */   extends KarmaSpell
/*     */   implements CreatureTemplateIds
/*     */ {
/*  45 */   private static final Logger logger = Logger.getLogger(Summon.class.getName());
/*     */ 
/*     */   
/*     */   public static final int RANGE = 24;
/*     */ 
/*     */   
/*     */   public Summon() {
/*  52 */     super("Summon", 559, 60, 500, 30, 1, 180000L);
/*  53 */     this.targetTile = true;
/*  54 */     this.description = "summons a creature to your aid";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, int tilex, int tiley, int layer) {
/*  60 */     if (performer.knowsKarmaSpell(630))
/*     */     {
/*  62 */       if (layer >= 0 || performer.getLayer() >= 0)
/*     */       {
/*  64 */         if (!WurmCalendar.isNight())
/*     */         {
/*  66 */           if (!performer.knowsKarmaSpell(629) && !performer.knowsKarmaSpell(631)) {
/*     */             
/*  68 */             performer.getCommunicator().sendNormalServerMessage("You cannot summon this above ground during daytime.", (byte)3);
/*     */ 
/*     */             
/*  71 */             return false;
/*     */           } 
/*     */         }
/*     */       }
/*     */     }
/*  76 */     if ((performer.getFollowers()).length > 0) {
/*     */       
/*  78 */       performer.getCommunicator().sendNormalServerMessage("You are too busy leading other creatures and can not focus on summoning.", (byte)3);
/*     */ 
/*     */       
/*  81 */       return false;
/*     */     } 
/*  83 */     if (layer < 0)
/*     */     {
/*  85 */       if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(tilex, tiley)))) {
/*     */         
/*  87 */         performer.getCommunicator().sendNormalServerMessage("You can not summon there.", (byte)3);
/*     */         
/*  89 */         return false;
/*     */       } 
/*     */     }
/*     */     
/*     */     try {
/*  94 */       if (Zones.calculateHeight(((tilex << 2) + 2), ((tiley << 2) + 2), performer.isOnSurface()) < 0.0F)
/*     */       {
/*  96 */         performer.getCommunicator().sendNormalServerMessage("You can not summon there.", (byte)3);
/*     */         
/*  98 */         return false;
/*     */       }
/*     */     
/* 101 */     } catch (Exception ex) {
/*     */       
/* 103 */       performer.getCommunicator().sendNormalServerMessage("You can not summon there.", (byte)3);
/*     */       
/* 105 */       return false;
/*     */     } 
/* 107 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/*     */     try {
/* 116 */       if (performer.knowsKarmaSpell(629))
/* 117 */         spawnCreature(86, performer); 
/* 118 */       if (performer.knowsKarmaSpell(631))
/*     */       {
/* 120 */         for (int nums = 0; nums < Math.max(2.0D, power / 10.0D); nums++)
/* 121 */           spawnCreature(87, performer); 
/*     */       }
/* 123 */       if (performer.knowsKarmaSpell(630))
/*     */       {
/*     */         
/* 126 */         if (WurmCalendar.isNight() || layer < 0 || performer.getLayer() < 0)
/*     */         {
/* 128 */           spawnCreature(88, performer);
/*     */         }
/*     */       }
/*     */     }
/* 132 */     catch (Exception ex) {
/*     */       
/* 134 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final void spawnCreature(int templateId, Creature performer) {
/*     */     try {
/* 142 */       CreatureTemplate ct = CreatureTemplateFactory.getInstance().getTemplate(templateId);
/* 143 */       byte sex = 0;
/* 144 */       if (Server.rand.nextInt(2) == 0)
/* 145 */         sex = 1; 
/* 146 */       byte ctype = (byte)Math.max(0, Server.rand.nextInt(22) - 10);
/* 147 */       if (Server.rand.nextInt(20) == 0)
/* 148 */         ctype = 99; 
/* 149 */       Creature creature = Creature.doNew(templateId, true, performer.getPosX() - 4.0F + Server.rand.nextFloat() * 9.0F, performer
/* 150 */           .getPosY() - 4.0F + Server.rand
/* 151 */           .nextFloat() * 9.0F, Server.rand
/* 152 */           .nextFloat() * 360.0F, performer.getLayer(), ct.getName(), sex, performer.getKingdomId(), ctype, true);
/*     */     }
/* 154 */     catch (NoSuchCreatureTemplateException nst) {
/*     */       
/* 156 */       logger.log(Level.WARNING, nst.getMessage(), (Throwable)nst);
/*     */     }
/* 158 */     catch (Exception ex) {
/*     */       
/* 160 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\Summon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */