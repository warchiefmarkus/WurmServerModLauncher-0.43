/*     */ package com.wurmonline.server.items;
/*     */ 
/*     */ import com.wurmonline.server.NoSuchEntryException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public final class CreationMatrix
/*     */ {
/*  36 */   private static Map<Integer, List<CreationEntry>> matrix = new HashMap<>();
/*     */ 
/*     */   
/*     */   private static CreationMatrix instance;
/*     */ 
/*     */   
/*  42 */   private static Map<Integer, List<CreationEntry>> advancedEntries = new HashMap<>();
/*     */   
/*  44 */   private static Map<Integer, List<CreationEntry>> simpleEntries = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CreationMatrix getInstance() {
/*  52 */     if (instance == null)
/*  53 */       instance = new CreationMatrix(); 
/*  54 */     return instance;
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
/*     */   public void addCreationEntry(CreationEntry entry) {
/*  66 */     Integer space = Integer.valueOf(entry.getObjectTarget());
/*  67 */     List<CreationEntry> entrys = matrix.get(space);
/*  68 */     if (entrys == null)
/*  69 */       entrys = new LinkedList<>(); 
/*  70 */     entrys.add(entry);
/*  71 */     matrix.put(space, entrys);
/*     */     
/*  73 */     space = Integer.valueOf(entry.getObjectCreated());
/*  74 */     if (entry instanceof AdvancedCreationEntry) {
/*     */       
/*  76 */       entrys = advancedEntries.get(space);
/*  77 */       if (entrys == null)
/*  78 */         entrys = new LinkedList<>(); 
/*  79 */       entrys.add(entry);
/*  80 */       advancedEntries.put(space, entrys);
/*     */     } 
/*  82 */     entrys = simpleEntries.get(space);
/*  83 */     if (entrys == null)
/*  84 */       entrys = new LinkedList<>(); 
/*  85 */     entrys.add(entry);
/*  86 */     simpleEntries.put(space, entrys);
/*     */   }
/*     */ 
/*     */   
/*     */   public CreationEntry[] getCreationOptionsFor(int sourceTemplateId, int targetTemplateId) {
/*  91 */     List<CreationEntry> entrys = matrix.get(Integer.valueOf(targetTemplateId));
/*  92 */     List<CreationEntry> options = new LinkedList<>();
/*  93 */     if (entrys != null)
/*     */     {
/*  95 */       for (CreationEntry entry : entrys) {
/*     */         
/*  97 */         if (entry.getObjectSource() != sourceTemplateId)
/*     */           continue; 
/*  99 */         if (entry.getObjectTarget() != targetTemplateId) {
/*     */           continue;
/*     */         }
/* 102 */         options.add(entry);
/*     */       } 
/*     */     }
/*     */     
/* 106 */     entrys = matrix.get(Integer.valueOf(sourceTemplateId));
/* 107 */     if (entrys != null)
/*     */     {
/* 109 */       for (CreationEntry entry : entrys) {
/*     */         
/* 111 */         if (entry.getObjectSource() != targetTemplateId)
/*     */           continue; 
/* 113 */         if (entry.getObjectTarget() != sourceTemplateId) {
/*     */           continue;
/*     */         }
/* 116 */         options.add(entry);
/*     */       } 
/*     */     }
/*     */     
/* 120 */     CreationEntry[] toReturn = new CreationEntry[options.size()];
/* 121 */     return options.<CreationEntry>toArray(toReturn);
/*     */   }
/*     */ 
/*     */   
/*     */   public CreationEntry[] getCreationOptionsFor(Item source, Item target) {
/* 126 */     Integer space = Integer.valueOf(target.getTemplateId());
/* 127 */     List<CreationEntry> entrys = matrix.get(space);
/* 128 */     List<CreationEntry> options = new LinkedList<>();
/* 129 */     if (entrys != null)
/*     */     {
/* 131 */       for (CreationEntry entry : entrys) {
/*     */         
/* 133 */         if (entry.getObjectSource() != source.getTemplateId())
/*     */           continue; 
/* 135 */         if (entry.getObjectSourceMaterial() != 0 && entry
/* 136 */           .getObjectSourceMaterial() != source.getMaterial())
/*     */           continue; 
/* 138 */         if (entry.getObjectTarget() != target.getTemplateId())
/*     */           continue; 
/* 140 */         if (entry.getObjectTargetMaterial() != 0 && entry
/* 141 */           .getObjectTargetMaterial() != target.getMaterial()) {
/*     */           continue;
/*     */         }
/* 144 */         options.add(entry);
/*     */       } 
/*     */     }
/*     */     
/* 148 */     entrys = matrix.get(Integer.valueOf(source.getTemplateId()));
/* 149 */     if (entrys != null)
/*     */     {
/* 151 */       for (CreationEntry entry : entrys) {
/*     */         
/* 153 */         if (entry.getObjectSource() != target.getTemplateId())
/*     */           continue; 
/* 155 */         if (entry.getObjectSourceMaterial() != 0 && entry
/* 156 */           .getObjectSourceMaterial() != target.getMaterial())
/*     */           continue; 
/* 158 */         if (entry.getObjectTarget() != source.getTemplateId())
/*     */           continue; 
/* 160 */         if (entry.getObjectTargetMaterial() != 0 && entry
/* 161 */           .getObjectTargetMaterial() != source.getMaterial()) {
/*     */           continue;
/*     */         }
/* 164 */         options.add(entry);
/*     */       } 
/*     */     }
/*     */     
/* 168 */     CreationEntry[] toReturn = new CreationEntry[options.size()];
/* 169 */     return options.<CreationEntry>toArray(toReturn);
/*     */   }
/*     */ 
/*     */   
/*     */   public AdvancedCreationEntry getAdvancedCreationEntry(int objectCreated) throws NoSuchEntryException {
/* 174 */     if (advancedEntries != null) {
/*     */       
/* 176 */       LinkedList<CreationEntry> alist = (LinkedList<CreationEntry>)advancedEntries.get(Integer.valueOf(objectCreated));
/* 177 */       if (alist == null)
/* 178 */         throw new NoSuchEntryException("No entry with id " + objectCreated); 
/* 179 */       AdvancedCreationEntry toReturn = (AdvancedCreationEntry)alist.getFirst();
/* 180 */       return toReturn;
/*     */     } 
/*     */     
/* 183 */     throw new NoSuchEntryException("No entry with id " + objectCreated);
/*     */   }
/*     */ 
/*     */   
/*     */   public final CreationEntry[] getAdvancedEntries() {
/* 188 */     List<CreationEntry> list = new ArrayList<>();
/* 189 */     for (Integer in : advancedEntries.keySet()) {
/*     */       
/* 191 */       List<CreationEntry> entrys = advancedEntries.get(in);
/* 192 */       for (CreationEntry ee : entrys) {
/*     */         
/* 194 */         if (!list.contains(ee))
/*     */         {
/* 196 */           list.addAll(entrys);
/*     */         }
/*     */       } 
/*     */     } 
/* 200 */     return list.<CreationEntry>toArray(new CreationEntry[list.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Map<Integer, List<CreationEntry>> getAdvancedEntriesMap() {
/* 205 */     return advancedEntries;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final CreationEntry[] getAdvancedEntriesNotEpicMission() {
/* 212 */     Set<CreationEntry> advanced = new HashSet<>();
/* 213 */     for (List<CreationEntry> entrys : advancedEntries.values()) {
/*     */       
/* 215 */       for (CreationEntry entry : entrys) {
/*     */         
/* 217 */         if (!entry.isOnlyCreateEpicTargetMission && entry.isCreateEpicTargetMission)
/* 218 */           advanced.add(entry); 
/*     */       } 
/*     */     } 
/* 221 */     return advanced.<CreationEntry>toArray(new CreationEntry[advanced.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public final CreationEntry[] getSimpleEntries() {
/* 226 */     List<CreationEntry> list = new ArrayList<>();
/* 227 */     for (Integer in : simpleEntries.keySet()) {
/*     */       
/* 229 */       List<CreationEntry> entrys = simpleEntries.get(in);
/* 230 */       for (CreationEntry ee : entrys) {
/*     */         
/* 232 */         if (!list.contains(ee))
/*     */         {
/* 234 */           list.addAll(entrys);
/*     */         }
/*     */       } 
/*     */     } 
/* 238 */     return list.<CreationEntry>toArray(new CreationEntry[list.size()]);
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
/*     */   public CreationEntry getCreationEntry(int objectCreated) {
/* 251 */     CreationEntry toReturn = null;
/*     */     
/*     */     try {
/* 254 */       toReturn = getAdvancedCreationEntry(objectCreated);
/*     */     }
/* 256 */     catch (NoSuchEntryException nse) {
/*     */       
/* 258 */       Integer space = Integer.valueOf(objectCreated);
/* 259 */       List<CreationEntry> entrys = simpleEntries.get(space);
/* 260 */       if (entrys == null)
/* 261 */         return toReturn; 
/* 262 */       Iterator<CreationEntry> iterator = entrys.iterator(); if (iterator.hasNext()) { CreationEntry entry = iterator.next();
/*     */         
/* 264 */         return entry; }
/*     */     
/*     */     } 
/* 267 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CreationEntry getCreationEntry(int objectSource, int objectTarget, int objectCreated) throws NoSuchEntryException {
/* 273 */     CreationEntry toReturn = null;
/* 274 */     Integer space = Integer.valueOf(objectTarget);
/* 275 */     List<CreationEntry> entrys = matrix.get(space);
/* 276 */     if (entrys != null)
/*     */     {
/* 278 */       for (CreationEntry entry : entrys) {
/*     */         
/* 280 */         if (((entry.getObjectSource() == objectSource && entry.getObjectTarget() == objectTarget) || (entry
/* 281 */           .getObjectSource() == objectTarget && entry.getObjectTarget() == objectSource)) && entry
/* 282 */           .getObjectCreated() == objectCreated)
/* 283 */           toReturn = entry; 
/*     */       } 
/*     */     }
/* 286 */     if (toReturn == null) {
/*     */       
/* 288 */       entrys = matrix.get(Integer.valueOf(objectSource));
/* 289 */       if (entrys != null)
/*     */       {
/* 291 */         for (CreationEntry entry : entrys) {
/*     */           
/* 293 */           if (((entry.getObjectSource() == objectSource && entry.getObjectTarget() == objectTarget) || (entry
/* 294 */             .getObjectSource() == objectTarget && entry.getObjectTarget() == objectSource)) && entry
/* 295 */             .getObjectCreated() == objectCreated)
/* 296 */             toReturn = entry; 
/*     */         } 
/*     */       }
/*     */     } 
/* 300 */     if (toReturn == null) {
/* 301 */       throw new NoSuchEntryException("No creation entry found for objectSource=" + objectSource + ", objectTarget=" + objectTarget + ", objectCreated=" + objectCreated);
/*     */     }
/* 303 */     return toReturn;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\CreationMatrix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */