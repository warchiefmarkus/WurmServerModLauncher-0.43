/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.HistoryManager;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.deities.Deities;
/*     */ import com.wurmonline.server.deities.Deity;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.skills.Skill;
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
/*     */ public class RiteSpring
/*     */   extends ReligiousSpell
/*     */ {
/*     */   public static final int RANGE = 4;
/*     */   
/*     */   public RiteSpring() {
/*  41 */     super("Rite of Spring", 403, 100, 300, 60, 50, 43200000L);
/*  42 */     this.isRitual = true;
/*  43 */     this.targetItem = true;
/*  44 */     this.description = "followers of your god receives a permanent blessing";
/*  45 */     this.type = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, Item target) {
/*  56 */     if (performer.getDeity() != null) {
/*     */       
/*  58 */       Deity deity = performer.getDeity();
/*  59 */       Deity templateDeity = Deities.getDeity(deity.getTemplateDeity());
/*  60 */       if (templateDeity.getFavor() < 100000 && !Servers.isThisATestServer()) {
/*     */         
/*  62 */         performer.getCommunicator().sendNormalServerMessage(deity
/*  63 */             .getName() + " can not grant that power right now.", (byte)3);
/*     */         
/*  65 */         return false;
/*     */       } 
/*  67 */       if (target.getBless() == deity)
/*     */       {
/*  69 */         if (target.isDomainItem())
/*     */         {
/*  71 */           return true;
/*     */         }
/*     */       }
/*  74 */       performer.getCommunicator().sendNormalServerMessage(
/*  75 */           String.format("You need to cast this spell at an altar of %s.", new Object[] { deity.getName() }), (byte)3);
/*     */     } 
/*     */     
/*  78 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, Item target) {
/*  89 */     Deity deity = performer.getDeity();
/*  90 */     Deity templateDeity = Deities.getDeity(deity.getTemplateDeity());
/*     */     
/*  92 */     performer.getCommunicator().sendNormalServerMessage("Followers of " + deity.getName() + " receive a blessing!", (byte)2);
/*  93 */     Server.getInstance().broadCastSafe("As the Rite of Spring is completed, followers of " + deity.getName() + " may now receive a blessing!");
/*  94 */     HistoryManager.addHistory(performer.getName(), "casts " + this.name + ". Followers of " + deity.getName() + " receive a blessing!");
/*  95 */     templateDeity.setFavor(templateDeity.getFavor() - 100000);
/*     */     
/*  97 */     performer.achievement(635);
/*  98 */     for (Creature c : performer.getLinks()) {
/*  99 */       c.achievement(635);
/*     */     }
/*     */     
/* 102 */     new RiteEvent.RiteOfSpringEvent(-10, performer.getWurmId(), getNumber(), deity.getNumber(), System.currentTimeMillis(), 86400000L);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\RiteSpring.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */