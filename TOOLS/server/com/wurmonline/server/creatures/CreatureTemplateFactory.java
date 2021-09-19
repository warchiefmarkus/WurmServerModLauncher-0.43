/*     */ package com.wurmonline.server.creatures;
/*     */ 
/*     */ import com.wurmonline.server.skills.Skills;
/*     */ import com.wurmonline.shared.exceptions.WurmServerException;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
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
/*     */ public final class CreatureTemplateFactory
/*     */   implements CreatureTemplateIds
/*     */ {
/*     */   private static CreatureTemplateFactory instance;
/*  34 */   private static Map<Integer, CreatureTemplate> templates = new HashMap<>();
/*  35 */   private static Map<String, CreatureTemplate> templatesByName = new HashMap<>();
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
/*     */   public static CreatureTemplateFactory getInstance() {
/*  48 */     if (instance == null)
/*  49 */       instance = new CreatureTemplateFactory(); 
/*  50 */     return instance;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean isNameOkay(String aName) {
/*  55 */     String lName = aName.toLowerCase();
/*  56 */     if (lName.startsWith("wurm"))
/*  57 */       return false; 
/*  58 */     for (CreatureTemplate template : templates.values()) {
/*     */       
/*  60 */       if (template.getName().toLowerCase().equals(lName)) {
/*  61 */         return false;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  66 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final CreatureTemplate getTemplate(int id) throws NoSuchCreatureTemplateException {
/*  76 */     CreatureTemplate toReturn = templates.get(Integer.valueOf(id));
/*     */     
/*  78 */     if (toReturn == null)
/*  79 */       throw new NoSuchCreatureTemplateException("No Creature template with id " + id); 
/*  80 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CreatureTemplate getTemplate(String name) throws Exception {
/*  90 */     CreatureTemplate toReturn = templatesByName.get(name.toLowerCase());
/*     */     
/*  92 */     if (toReturn == null) {
/*  93 */       throw new WurmServerException("No Creature template with name " + name);
/*     */     }
/*     */ 
/*     */     
/*  97 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CreatureTemplate[] getTemplates() {
/* 106 */     CreatureTemplate[] toReturn = new CreatureTemplate[templates.size()];
/* 107 */     return (CreatureTemplate[])templates.values().toArray((Object[])toReturn);
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
/*     */   public CreatureTemplate createCreatureTemplate(int id, String name, String plural, String longDesc, String modelName, int[] types, byte bodyType, Skills skills, short vision, byte sex, short centimetersHigh, short centimetersLong, short centimetersWide, String deathSndMale, String deathSndFemale, String hitSndMale, String hitSndFemale, float naturalArmour, float handDam, float kickDam, float biteDam, float headDam, float breathDam, float speed, int moveRate, int[] itemsButchered, int maxHuntDist, int aggress, byte meatMaterial) throws IOException {
/* 119 */     CreatureTemplate toReturn = new DbCreatureTemplate(id, name, plural, longDesc, modelName, types, bodyType, skills, vision, sex, centimetersHigh, centimetersLong, centimetersWide, deathSndMale, deathSndFemale, hitSndMale, hitSndFemale, naturalArmour, handDam, kickDam, biteDam, headDam, breathDam, speed, moveRate, itemsButchered, maxHuntDist, aggress, meatMaterial);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 124 */     templates.put(Integer.valueOf(id), toReturn);
/* 125 */     templatesByName.put(name.toLowerCase(), toReturn);
/* 126 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getModelNameOrNull(String templateName) {
/* 131 */     CreatureTemplate t = templatesByName.get(templateName);
/* 132 */     if (t == null)
/* 133 */       return null; 
/* 134 */     return t.getModelName();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\CreatureTemplateFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */