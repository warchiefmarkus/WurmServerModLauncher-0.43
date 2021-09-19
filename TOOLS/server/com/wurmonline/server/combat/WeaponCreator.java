/*     */ package com.wurmonline.server.combat;
/*     */ 
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
/*     */ public final class WeaponCreator
/*     */ {
/*  28 */   private static final Logger logger = Logger.getLogger(WeaponCreator.class.getName());
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
/*     */   public static final void createWeapons() {
/*  40 */     logger.info("Creating weapons");
/*  41 */     long start = System.nanoTime();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  46 */     Weapon awl = new Weapon(390, 1.0F, 3.0F, 0.0F, 1, 1, 0.0F, 2.0D);
/*  47 */     Weapon knifele = new Weapon(392, 0.5F, 2.0F, 0.0F, 1, 1, 0.0F, 2.0D);
/*  48 */     Weapon knifeca = new Weapon(8, 1.0F, 2.0F, 0.0F, 1, 1, 1.0F, 2.0D);
/*  49 */     Weapon knifebu = new Weapon(93, 1.5F, 2.0F, 0.0F, 1, 1, 1.0F, 1.0D);
/*  50 */     Weapon knifesa = new Weapon(792, 1.5F, 2.0F, 0.03F, 1, 1, 1.0F, 1.0D);
/*  51 */     Weapon knifecru = new Weapon(685, 1.0F, 4.0F, 0.0F, 1, 1, 0.0F, 3.0D);
/*  52 */     Weapon pickaxecru = new Weapon(687, 1.0F, 6.0F, 0.0F, 1, 1, 0.0F, 5.0D);
/*  53 */     Weapon shaftcru = new Weapon(691, 1.0F, 3.0F, 0.0F, 1, 1, 0.0F, 5.0D);
/*  54 */     Weapon shovelcru = new Weapon(690, 1.0F, 6.0F, 0.0F, 1, 1, 0.0F, 5.0D);
/*  55 */     Weapon branch = new Weapon(688, 1.0F, 6.0F, 0.0F, 1, 1, 0.0F, 3.0D);
/*  56 */     Weapon axecru = new Weapon(1011, 1.0F, 5.0F, 0.0F, 1, 1, 0.0F, 5.0D);
/*  57 */     Weapon scissor = new Weapon(394, 0.5F, 2.0F, 0.0F, 1, 1, 0.0F, 2.0D);
/*  58 */     Weapon swoshort = new Weapon(80, 4.0F, 3.0F, 0.1F, 2, 1, 1.0F, 0.0D);
/*     */     
/*  60 */     Weapon bowL = new Weapon(449, 0.0F, 5.0F, 0.0F, 1, 5, 1.0F, 9.0D);
/*  61 */     bowL.setDamagedByMetal(true);
/*  62 */     Weapon bowM = new Weapon(448, 0.0F, 5.0F, 0.0F, 1, 5, 1.0F, 9.0D);
/*  63 */     bowM.setDamagedByMetal(true);
/*  64 */     Weapon bowS = new Weapon(447, 0.0F, 5.0F, 0.0F, 1, 5, 1.0F, 9.0D);
/*  65 */     bowS.setDamagedByMetal(true);
/*  66 */     Weapon bowLN = new Weapon(461, 0.0F, 5.0F, 0.0F, 1, 5, 1.0F, 9.0D);
/*  67 */     bowLN.setDamagedByMetal(true);
/*  68 */     Weapon bowSN = new Weapon(459, 0.0F, 5.0F, 0.0F, 1, 5, 1.0F, 9.0D);
/*  69 */     bowSN.setDamagedByMetal(true);
/*  70 */     Weapon bowMN = new Weapon(460, 0.0F, 5.0F, 0.0F, 1, 5, 1.0F, 9.0D);
/*  71 */     bowMN.setDamagedByMetal(true);
/*     */     
/*  73 */     Weapon hatchet = new Weapon(7, 1.0F, 5.0F, 0.0F, 2, 2, 0.0F, 3.0D);
/*  74 */     hatchet.setDamagedByMetal(true);
/*  75 */     Weapon pickax = new Weapon(20, 1.5F, 5.0F, 0.0F, 3, 3, 0.1F, 3.0D);
/*  76 */     pickax.setDamagedByMetal(true);
/*  77 */     Weapon shovel = new Weapon(25, 1.0F, 5.0F, 0.0F, 4, 3, 1.0F, 3.0D);
/*  78 */     shovel.setDamagedByMetal(true);
/*  79 */     Weapon rake = new Weapon(27, 0.5F, 5.0F, 0.0F, 5, 2, 1.0F, 3.0D);
/*  80 */     rake.setDamagedByMetal(true);
/*     */     
/*  82 */     Weapon saw = new Weapon(24, 0.5F, 5.0F, 0.01F, 2, 3, 0.0F, 3.0D);
/*  83 */     Weapon sickle = new Weapon(267, 6.0F, 3.0F, 0.02F, 2, 3, 0.2F, 2.0D);
/*  84 */     Weapon scythe = new Weapon(268, 9.0F, 5.0F, 0.08F, 5, 4, 0.2F, 2.0D);
/*  85 */     scythe.setDamagedByMetal(true);
/*     */     
/*  87 */     Weapon longswo = new Weapon(21, 5.5F, 4.0F, 0.01F, 3, 3, 1.0F, 0.0D);
/*  88 */     Weapon twoswo = new Weapon(81, 9.0F, 5.0F, 0.05F, 4, 5, 1.0F, 0.0D);
/*  89 */     Weapon smallax = new Weapon(3, 5.0F, 3.0F, 0.0F, 2, 2, 0.3F, 0.0D);
/*  90 */     Weapon batax = new Weapon(90, 6.5F, 4.0F, 0.03F, 4, 5, 0.3F, 0.0D);
/*  91 */     Weapon twoaxe = new Weapon(87, 12.0F, 6.0F, 0.05F, 5, 5, 0.2F, 0.0D);
/*  92 */     Weapon swoMag = new Weapon(336, 15.0F, 5.0F, 0.08F, 4, 3, 1.0F, 0.0D);
/*  93 */     Weapon whipOne = new Weapon(514, 6.0F, 2.0F, 0.0F, 5, 1, 0.1F, 0.0D);
/*     */     
/*  95 */     Weapon spearLong = new Weapon(705, 8.0F, 5.0F, 0.06F, 7, 3, 1.0F, 0.0D);
/*  96 */     Weapon halberd = new Weapon(706, 9.0F, 5.0F, 0.06F, 6, 8, 1.0F, 0.0D);
/*  97 */     Weapon steelSpear = new Weapon(707, 9.0F, 5.0F, 0.06F, 7, 4, 1.0F, 0.0D);
/*  98 */     Weapon staffSteel = new Weapon(710, 8.0F, 4.0F, 0.0F, 3, 3, 1.0F, 0.0D);
/*  99 */     Weapon staffLand = new Weapon(986, 8.0F, 4.0F, 0.0F, 3, 3, 1.0F, 0.0D);
/*     */     
/* 101 */     Weapon fist = new Weapon(14, 1.0F, 1.0F, 0.0F, 1, 1, 0.0F, 2.0D);
/* 102 */     Weapon foot = new Weapon(19, 1.0F, 2.0F, 0.0F, 1, 1, 0.0F, 3.0D);
/* 103 */     Weapon plank = new Weapon(22, 0.5F, 4.0F, 0.0F, 2, 1, 1.0F, 3.0D);
/* 104 */     plank.setDamagedByMetal(true);
/* 105 */     Weapon shaft = new Weapon(23, 0.5F, 4.0F, 0.0F, 2, 2, 1.0F, 3.0D);
/* 106 */     shaft.setDamagedByMetal(true);
/* 107 */     Weapon staff = new Weapon(711, 2.0F, 3.0F, 0.0F, 2, 3, 1.0F, 0.0D);
/* 108 */     staff.setDamagedByMetal(true);
/*     */     
/* 110 */     Weapon metalh = new Weapon(62, 0.5F, 3.0F, 0.0F, 1, 1, 0.1F, 3.0D);
/* 111 */     metalh.setDamagedByMetal(true);
/* 112 */     Weapon woodenh = new Weapon(63, 0.3F, 3.0F, 0.0F, 1, 1, 0.1F, 3.0D);
/* 113 */     woodenh.setDamagedByMetal(true);
/* 114 */     Weapon maulL = new Weapon(290, 11.0F, 6.0F, 0.03F, 4, 5, 1.0F, 0.0D);
/* 115 */     Weapon maulM = new Weapon(292, 8.0F, 5.0F, 0.03F, 3, 2, 1.0F, 0.0D);
/*     */     
/* 117 */     Weapon maulS = new Weapon(291, 4.5F, 3.0F, 0.01F, 2, 2, 1.0F, 0.0D);
/*     */     
/* 119 */     Weapon belaying = new Weapon(567, 2.0F, 3.0F, 0.0F, 1, 1, 1.0F, 2.0D);
/* 120 */     belaying.setDamagedByMetal(true);
/* 121 */     Weapon clubH = new Weapon(314, 8.0F, 6.0F, 0.01F, 4, 6, 1.0F, 2.0D);
/* 122 */     clubH.setDamagedByMetal(true);
/* 123 */     Weapon hamMag = new Weapon(337, 18.0F, 6.0F, 0.08F, 4, 4, 1.0F, 0.0D);
/* 124 */     Weapon sceptre = new Weapon(340, 17.0F, 6.0F, 0.08F, 3, 3, 1.0F, 0.0D);
/*     */     
/* 126 */     Weapon steelCrowbar = new Weapon(1115, 4.5F, 3.0F, 0.01F, 2, 2, 1.0F, 0.0D);
/*     */     
/* 128 */     long end = System.nanoTime();
/* 129 */     logger.info("Creating weapons took " + ((float)(end - start) / 1000000.0F) + " ms");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\combat\WeaponCreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */