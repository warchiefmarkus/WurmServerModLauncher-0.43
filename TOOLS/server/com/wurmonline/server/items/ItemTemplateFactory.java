/*     */ package com.wurmonline.server.items;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public final class ItemTemplateFactory
/*     */ {
/*  35 */   private static Logger logger = Logger.getLogger(ItemTemplateFactory.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ItemTemplateFactory instance;
/*     */ 
/*     */ 
/*     */   
/*  44 */   private static Map<Integer, ItemTemplate> templates = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   private static Set<ItemTemplate> missionTemplates = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   private static Set<ItemTemplate> epicMissionTemplates = new HashSet<>();
/*     */   
/*  56 */   private static Map<String, ItemTemplate> templatesByName = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ItemTemplateFactory getInstance() {
/*  67 */     if (instance == null)
/*  68 */       instance = new ItemTemplateFactory(); 
/*  69 */     return instance;
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
/*     */ 
/*     */   
/*     */   public ItemTemplate getTemplateOrNull(int templateId) {
/*  83 */     return templates.get(Integer.valueOf(templateId));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTemplateName(int templateId) {
/*  88 */     ItemTemplate it = getTemplateOrNull(templateId);
/*  89 */     if (it != null)
/*  90 */       return it.getName(); 
/*  91 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemTemplate getTemplate(int templateId) throws NoSuchTemplateException {
/*  96 */     ItemTemplate toReturn = templates.get(Integer.valueOf(templateId));
/*  97 */     if (toReturn == null) {
/*  98 */       throw new NoSuchTemplateException("No item template with id " + templateId);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 104 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemTemplate getTemplate(String name) {
/* 109 */     return templatesByName.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemTemplate[] getTemplates() {
/* 118 */     ItemTemplate[] toReturn = new ItemTemplate[templates.size()];
/* 119 */     return (ItemTemplate[])templates.values().toArray((Object[])toReturn);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemTemplate[] getMissionTemplates() {
/* 128 */     ItemTemplate[] toReturn = new ItemTemplate[missionTemplates.size()];
/* 129 */     return missionTemplates.<ItemTemplate>toArray(toReturn);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemTemplate[] getEpicMissionTemplates() {
/* 138 */     ItemTemplate[] toReturn = new ItemTemplate[epicMissionTemplates.size()];
/* 139 */     return epicMissionTemplates.<ItemTemplate>toArray(toReturn);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemTemplate[] getMostDamageUpdated() {
/* 144 */     ItemTemplate[] temps = getTemplates();
/* 145 */     Arrays.sort(temps, new Comparator<ItemTemplate>()
/*     */         {
/*     */ 
/*     */ 
/*     */           
/*     */           public int compare(ItemTemplate o1, ItemTemplate o2)
/*     */           {
/* 152 */             if (o1.damUpdates == o2.damUpdates)
/* 153 */               return 0; 
/* 154 */             if (o1.damUpdates > o2.damUpdates) {
/* 155 */               return 1;
/*     */             }
/* 157 */             return -1;
/*     */           }
/*     */         });
/* 160 */     return temps;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemTemplate[] getMostMaintenanceUpdated() {
/* 165 */     ItemTemplate[] temps = getTemplates();
/*     */     
/* 167 */     Arrays.sort(temps, new Comparator<ItemTemplate>()
/*     */         {
/*     */           
/*     */           public int compare(ItemTemplate o1, ItemTemplate o2)
/*     */           {
/* 172 */             if (o1.maintUpdates == o2.maintUpdates)
/* 173 */               return 0; 
/* 174 */             if (o1.maintUpdates > o2.maintUpdates) {
/* 175 */               return 1;
/*     */             }
/* 177 */             return -1;
/*     */           }
/*     */         });
/* 180 */     return temps;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemTemplate createItemTemplate(int templateId, int size, String name, String plural, String itemDescriptionSuperb, String itemDescriptionNormal, String itemDescriptionBad, String itemDescriptionRotten, String itemDescriptionLong, short[] itemTypes, short imageNumber, short behaviourType, int combatDamage, long decayTime, int centimetersX, int centimetersY, int centimetersZ, int primarySkill, byte[] bodySpaces, String modelName, float difficulty, int weight, byte material, int value, boolean isTraded, int dyeAmountOverrideGrams) throws IOException {
/* 198 */     ItemTemplate toReturn = new ItemTemplate(templateId, size, name, plural, itemDescriptionSuperb, itemDescriptionNormal, itemDescriptionBad, itemDescriptionRotten, itemDescriptionLong, itemTypes, imageNumber, behaviourType, combatDamage, decayTime, centimetersX, centimetersY, centimetersZ, primarySkill, bodySpaces, modelName, difficulty, weight, material, value, isTraded);
/*     */ 
/*     */ 
/*     */     
/* 202 */     toReturn.setDyeAmountGrams(dyeAmountOverrideGrams);
/*     */     
/* 204 */     ItemTemplate old = templates.put(Integer.valueOf(templateId), toReturn);
/* 205 */     if (old != null)
/* 206 */       logger.warning("Duplicate definition for template " + templateId + " ('" + name + "' overwrites '" + old.getName() + "')."); 
/* 207 */     ItemTemplate it = templatesByName.put(name, toReturn);
/* 208 */     if (it != null && toReturn.isFood())
/* 209 */       logger.warning("Template " + it.getName() + " already being used."); 
/* 210 */     if (toReturn.isMissionItem()) {
/*     */       
/* 212 */       missionTemplates.add(toReturn);
/* 213 */       if (!toReturn.isNoTake() && !toReturn.isNoDrop() && toReturn.getWeightGrams() < 12000 && !toReturn.isRiftLoot() && (
/* 214 */         !toReturn.isFood() || !toReturn.isBulk()) && !toReturn.isLiquid())
/*     */       {
/* 216 */         if (toReturn.getTemplateId() != 652 && toReturn.getTemplateId() != 737 && toReturn
/* 217 */           .getTemplateId() != 1097 && toReturn.getTemplateId() != 1306 && toReturn
/* 218 */           .getTemplateId() != 1414)
/* 219 */           epicMissionTemplates.add(toReturn); 
/*     */       }
/*     */     } 
/* 222 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void logAllTemplates() {
/* 233 */     for (ItemTemplate template : templates.values())
/*     */     {
/* 235 */       logger.info(template.toString());
/*     */     }
/*     */   }
/*     */   
/*     */   public String getModelNameOrNull(String templateName) {
/* 240 */     ItemTemplate i = templatesByName.get(templateName);
/* 241 */     if (i == null)
/* 242 */       return null; 
/* 243 */     return i.getModelName();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\ItemTemplateFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */