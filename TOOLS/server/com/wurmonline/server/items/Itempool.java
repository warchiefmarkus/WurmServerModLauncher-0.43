/*     */ package com.wurmonline.server.items;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
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
/*     */ public final class Itempool
/*     */   implements MiscConstants
/*     */ {
/*  31 */   private static final Logger logger = Logger.getLogger(Itempool.class.getName());
/*     */   
/*  33 */   private static final ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Item>> recycleds = new ConcurrentHashMap<>();
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
/*     */   private static final int MAX_ITEMS_IN_POOL = 200;
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
/*     */   public static void checkRecycledItems() {}
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
/*     */   static void addRecycledItem(Item item) {
/*  68 */     ConcurrentLinkedQueue<Item> dset = getRecycledItemForTemplateID(item.getTemplateId());
/*  69 */     if (dset == null) {
/*     */       
/*  71 */       dset = new ConcurrentLinkedQueue<>();
/*  72 */       recycleds.put(Integer.valueOf(item.getTemplateId()), dset);
/*     */     } 
/*  74 */     if (dset.size() >= 200) {
/*  75 */       Items.decay(item.getWurmId(), item.getDbStrings());
/*     */     } else {
/*  77 */       dset.add(item);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ConcurrentLinkedQueue<Item> getRecycledItemForTemplateID(int aTemplateId) {
/*  88 */     ConcurrentLinkedQueue<Item> dset = recycleds.get(Integer.valueOf(aTemplateId));
/*  89 */     return dset;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static void returnRecycledItem(Item item) {
/* 110 */     item.setZoneId(-10, true);
/*     */ 
/*     */     
/* 113 */     item.setBanked(true);
/*     */     
/* 115 */     item.data = null;
/* 116 */     item.ownerId = -10L;
/* 117 */     item.lastOwner = -10L;
/* 118 */     item.parentId = -10L;
/*     */     
/* 120 */     ItemFactory.clearData(item.id);
/* 121 */     ConcurrentLinkedQueue<Item> dset = getRecycledItemForTemplateID(item.getTemplateId());
/* 122 */     if (dset == null) {
/*     */       
/* 124 */       dset = new ConcurrentLinkedQueue<>();
/* 125 */       recycleds.put(Integer.valueOf(item.getTemplateId()), dset);
/*     */     } 
/*     */ 
/*     */     
/* 129 */     if (dset.size() >= 200) {
/* 130 */       Items.decay(item.getWurmId(), item.getDbStrings());
/*     */     }
/*     */     else {
/*     */       
/* 134 */       item.setSettings(0);
/* 135 */       item.setRealTemplate(-10);
/* 136 */       dset.add(item);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static Item getRecycledItem(int templateId, float qualityLevel) {
/* 142 */     Item toReturn = null;
/* 143 */     ConcurrentLinkedQueue<Item> dset = getRecycledItemForTemplateID(templateId);
/* 144 */     if (dset == null)
/* 145 */       return null; 
/* 146 */     toReturn = dset.poll();
/* 147 */     if (toReturn == null)
/*     */     {
/* 149 */       return toReturn;
/*     */     }
/* 151 */     toReturn.setBanked(false);
/* 152 */     toReturn.deleted = false;
/* 153 */     toReturn.setSettings(0);
/* 154 */     toReturn.setRealTemplate(-10);
/*     */     
/* 156 */     Items.putItem(toReturn);
/* 157 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void deleteItem(int templateId, long wurmid) {
/* 162 */     ConcurrentLinkedQueue<Item> dset = getRecycledItemForTemplateID(templateId);
/* 163 */     if (dset == null || dset.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 167 */     Item[] items = dset.<Item>toArray(new Item[0]);
/*     */     
/* 169 */     for (Item i : items) {
/*     */ 
/*     */       
/* 172 */       if (i.getWurmId() == wurmid) {
/*     */ 
/*     */         
/* 175 */         dset.remove(i);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\Itempool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */