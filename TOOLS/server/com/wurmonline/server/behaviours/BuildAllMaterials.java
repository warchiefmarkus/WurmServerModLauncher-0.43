/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.items.NoSuchTemplateException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class BuildAllMaterials
/*     */ {
/*  41 */   private final List<BuildStageMaterials> bsms = new ArrayList<>();
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(BuildStageMaterials bms) {
/*  46 */     this.bsms.add(bms);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BuildStageMaterials> getBuildStageMaterials() {
/*  51 */     return this.bsms;
/*     */   }
/*     */ 
/*     */   
/*     */   public BuildStageMaterials getBuildStageMaterials(byte stage) {
/*  56 */     return this.bsms.get(Math.max(0, stage));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStageCount() {
/*  61 */     return this.bsms.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BuildMaterial> getCurrentRequiredMaterials() {
/*  66 */     Iterator<BuildStageMaterials> iterator = this.bsms.iterator(); if (iterator.hasNext()) { BuildStageMaterials bsm = iterator.next();
/*     */       
/*  68 */       return bsm.getRequiredMaterials(); }
/*     */     
/*  70 */     return new ArrayList<>();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getStageCountAsString() {
/*  75 */     switch (this.bsms.size()) {
/*     */       
/*     */       case 1:
/*  78 */         return "one";
/*     */       case 2:
/*  80 */         return "two";
/*     */       case 3:
/*  82 */         return "three";
/*     */       case 4:
/*  84 */         return "four";
/*     */       case 5:
/*  86 */         return "five";
/*     */       case 6:
/*  88 */         return "six";
/*     */       case 7:
/*  90 */         return "seven";
/*     */     } 
/*  92 */     return "" + this.bsms.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNeeded(byte currentStage, int done) {
/* 101 */     for (int stage = 0; stage < this.bsms.size(); stage++) {
/*     */       
/* 103 */       if (currentStage > stage) {
/* 104 */         getBuildStageMaterials((byte)stage).setNoneNeeded();
/* 105 */       } else if (currentStage == stage) {
/* 106 */         getBuildStageMaterials((byte)stage).reduceNeededBy(done);
/*     */       } else {
/* 108 */         getBuildStageMaterials((byte)stage).setMaxNeeded();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<BuildMaterial> getTotalMaterialsNeeded() {
/* 114 */     BuildStageMaterials all = getTotalMaterialsRequired();
/* 115 */     return all.getBuildMaterials();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private BuildStageMaterials getTotalMaterialsRequired() {
/* 121 */     Map<Integer, Integer> mats = new HashMap<>();
/* 122 */     for (BuildStageMaterials bsm : this.bsms) {
/*     */       
/* 124 */       for (BuildMaterial bm : bsm.getBuildMaterials()) {
/*     */         
/* 126 */         int qty = bm.getNeededQuantity();
/* 127 */         if (qty > 0) {
/*     */           
/* 129 */           Integer key = Integer.valueOf(bm.getTemplateId());
/* 130 */           if (mats.containsKey(key))
/*     */           {
/* 132 */             qty += ((Integer)mats.get(key)).intValue();
/*     */           }
/* 134 */           mats.put(key, Integer.valueOf(qty));
/*     */         } 
/*     */       } 
/*     */     } 
/* 138 */     BuildStageMaterials all = new BuildStageMaterials("All");
/* 139 */     for (Map.Entry<Integer, Integer> entry : mats.entrySet()) {
/*     */ 
/*     */       
/*     */       try {
/* 143 */         all.add(((Integer)entry.getKey()).intValue(), ((Integer)entry.getValue()).intValue());
/*     */       }
/* 145 */       catch (NoSuchTemplateException noSuchTemplateException) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 150 */     return all;
/*     */   }
/*     */ 
/*     */   
/*     */   public BuildAllMaterials getRemainingMaterialsNeeded() {
/* 155 */     BuildAllMaterials toReturn = new BuildAllMaterials();
/* 156 */     for (BuildStageMaterials bsm : this.bsms) {
/*     */       
/* 158 */       if (!bsm.isStageComplete())
/*     */       {
/* 160 */         toReturn.add(bsm);
/*     */       }
/*     */     } 
/* 163 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRequiredMaterialString(boolean detailed) {
/* 168 */     BuildStageMaterials all = getTotalMaterialsRequired();
/* 169 */     return all.getRequiredMaterialString(detailed);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTotalQuantityRequired() {
/* 178 */     int count = 0;
/* 179 */     for (BuildStageMaterials bsm : this.bsms)
/*     */     {
/* 181 */       count += bsm.getTotalQuantityRequired();
/*     */     }
/* 183 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTotalQuantityDone() {
/* 192 */     int count = 0;
/* 193 */     for (BuildStageMaterials bsm : this.bsms)
/*     */     {
/* 195 */       count += bsm.getTotalQuantityDone();
/*     */     }
/* 197 */     return count;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\BuildAllMaterials.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */