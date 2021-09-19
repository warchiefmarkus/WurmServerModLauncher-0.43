/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.items.ItemTemplate;
/*     */ import com.wurmonline.server.items.ItemTemplateFactory;
/*     */ import com.wurmonline.server.items.NoSuchTemplateException;
/*     */ import com.wurmonline.server.structures.BridgePart;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ public class BuildStageMaterials
/*     */ {
/*     */   private final List<BuildMaterial> bms;
/*     */   private final String name;
/*  39 */   private int stageNo = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BuildStageMaterials(String newName) {
/*  46 */     this.name = newName;
/*  47 */     this.bms = new ArrayList<>();
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(int templateId, int qty) throws NoSuchTemplateException {
/*  52 */     this.bms.add(new BuildMaterial(templateId, qty));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStageNumber(int numb) {
/*  57 */     this.stageNo = numb;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getStageNumber() {
/*  62 */     if (this.stageNo >= 0) {
/*  63 */       return "Stage " + this.stageNo + " ";
/*     */     }
/*  65 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  70 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getStageName() {
/*  75 */     return getStageNumber() + this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BuildMaterial> getBuildMaterials() {
/*  80 */     return this.bms;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNoneNeeded() {
/*  88 */     for (BuildMaterial mat : this.bms)
/*     */     {
/*  90 */       mat.setNeededQuantity(0);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxNeeded() {
/*  99 */     for (BuildMaterial mat : this.bms)
/*     */     {
/* 101 */       mat.setNeededQuantity(mat.getTotalQuantityRequired());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reduceNeededBy(int qty) {
/* 110 */     for (BuildMaterial mat : this.bms) {
/*     */       
/* 112 */       int newQty = mat.getTotalQuantityRequired() - qty;
/* 113 */       if (newQty < 0)
/* 114 */         newQty = 0; 
/* 115 */       mat.setNeededQuantity(newQty);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStageComplete(BridgePart bridgePart) {
/* 121 */     for (BuildMaterial mat : this.bms) {
/*     */       
/* 123 */       if (mat.getTotalQuantityRequired() > bridgePart.getMaterialCount())
/* 124 */         return false; 
/*     */     } 
/* 126 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStageComplete() {
/* 131 */     for (BuildMaterial bm : getBuildMaterials()) {
/*     */       
/* 133 */       if (bm.getNeededQuantity() > 0)
/*     */       {
/* 135 */         return false;
/*     */       }
/*     */     } 
/* 138 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BuildMaterial> getRequiredMaterials() {
/* 143 */     List<BuildMaterial> mats = new ArrayList<>();
/* 144 */     for (BuildMaterial mat : this.bms) {
/*     */       
/* 146 */       if (mat.getNeededQuantity() > 0)
/*     */       {
/* 148 */         mats.add(mat);
/*     */       }
/*     */     } 
/* 151 */     return mats;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRequiredMaterialString(boolean detailed) {
/* 156 */     Set<String> mats = new HashSet<>();
/* 157 */     for (BuildMaterial mat : this.bms) {
/*     */       
/* 159 */       if (mat.getNeededQuantity() > 0) {
/*     */         
/*     */         try {
/*     */           
/* 163 */           String str = "";
/* 164 */           ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(mat.getTemplateId());
/* 165 */           if (template != null) {
/*     */             
/* 167 */             if (detailed)
/* 168 */               str = str + mat.getNeededQuantity() + " "; 
/* 169 */             if (template.sizeString.length() > 0)
/* 170 */               str = str + template.sizeString; 
/* 171 */             str = str + ((mat.getNeededQuantity() > 1) ? template.getPlural() : template.getName());
/*     */           } 
/*     */           
/* 174 */           if (str.length() == 0)
/* 175 */             str = "unknown quantities of unknown materials"; 
/* 176 */           mats.add(str);
/*     */         }
/* 178 */         catch (NoSuchTemplateException noSuchTemplateException) {}
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 186 */     String description = "";
/* 187 */     int cnt = 0;
/* 188 */     for (String s : mats) {
/*     */       
/* 190 */       cnt++;
/* 191 */       if (cnt == mats.size() && mats.size() > 1) {
/* 192 */         description = description + " and ";
/* 193 */       } else if (cnt > 1) {
/* 194 */         description = description + ", ";
/* 195 */       }  description = description + s;
/*     */     } 
/*     */     
/* 198 */     if (description.length() == 0)
/*     */     {
/* 200 */       description = "no materials";
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 205 */     return description;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTotalQuantityRequired() {
/* 214 */     int count = 0;
/* 215 */     for (BuildMaterial bm : this.bms)
/*     */     {
/* 217 */       count += bm.getTotalQuantityRequired();
/*     */     }
/* 219 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTotalQuantityDone() {
/* 228 */     int count = 0;
/* 229 */     for (BuildMaterial bm : this.bms)
/*     */     {
/* 231 */       count += bm.getTotalQuantityRequired() - bm.getNeededQuantity();
/*     */     }
/* 233 */     return count;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\BuildStageMaterials.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */