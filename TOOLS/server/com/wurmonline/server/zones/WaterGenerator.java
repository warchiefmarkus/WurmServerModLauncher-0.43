/*     */ package com.wurmonline.server.zones;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemTemplate;
/*     */ import com.wurmonline.server.items.ItemTemplateFactory;
/*     */ import com.wurmonline.server.items.TempItem;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public class WaterGenerator
/*     */   implements Comparable<WaterGenerator>
/*     */ {
/*  40 */   private static final Logger logger = Logger.getLogger(WaterGenerator.class.getName());
/*     */   
/*     */   public final int x;
/*     */   public final int y;
/*     */   public final int layer;
/*     */   private int height;
/*     */   private int lastHeight;
/*     */   private int resetHeight;
/*     */   private boolean changed = true;
/*     */   private boolean spring = false;
/*     */   private Map<Integer, WaterGenerator> waterPointsY;
/*     */   private boolean createItem = true;
/*  52 */   private static final Map<Integer, WaterGenerator> waterPointsX = new HashMap<>();
/*     */   
/*     */   private Item waterMarker;
/*     */   
/*     */   private boolean isReset = false;
/*     */   
/*     */   private static ItemTemplate template;
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/*  63 */       template = ItemTemplateFactory.getInstance().getTemplate(845);
/*     */     }
/*  65 */     catch (Exception exception) {}
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
/*     */   public WaterGenerator(int tx, int ty, boolean isSpring, int tlayer, int waterHeight) {
/*  80 */     this.x = tx;
/*  81 */     this.y = ty;
/*  82 */     this.layer = tlayer;
/*  83 */     this.spring = isSpring;
/*  84 */     this.height = waterHeight;
/*  85 */     if (!this.spring) {
/*  86 */       putInMatrix(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public final void setSpring(boolean isSpring) {
/*  91 */     this.spring = isSpring;
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
/*     */   public WaterGenerator(int tx, int ty, int tlayer, int waterHeight) {
/* 104 */     this.x = tx;
/* 105 */     this.y = ty;
/* 106 */     this.layer = tlayer;
/* 107 */     this.height = waterHeight;
/* 108 */     this.lastHeight = this.height;
/*     */     
/* 110 */     putInMatrix(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean shouldCreateItem() {
/* 115 */     return this.createItem;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void createItem() {
/*     */     try {
/* 122 */       this
/* 123 */         .waterMarker = (Item)new TempItem("" + this.height, template, 99.0F, (this.x * 4 + 2), (this.y * 4 + 2), Zones.calculateHeight((this.x * 4 + 2), (this.y * 4 + 2), true), 1.0F, -10L, "");
/* 124 */       Zones.getZone(this.waterMarker.getTileX(), this.waterMarker.getTileY(), true).addItem(this.waterMarker);
/*     */     }
/* 126 */     catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */     
/* 130 */     this.createItem = false;
/*     */   }
/*     */ 
/*     */   
/*     */   private final void putInMatrix(WaterGenerator wg) {
/* 135 */     if (getXGeneral(wg.x) == null) {
/*     */       
/* 137 */       addXGeneral(wg);
/* 138 */       addY(wg);
/*     */     }
/*     */     else {
/*     */       
/* 142 */       WaterGenerator general = getXGeneral(wg.x);
/* 143 */       general.addY(wg);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final WaterGenerator getXGeneral(int aX) {
/* 149 */     return waterPointsX.get(Integer.valueOf(aX));
/*     */   }
/*     */ 
/*     */   
/*     */   public final WaterGenerator getY(int aY) {
/* 154 */     return this.waterPointsY.get(Integer.valueOf(aY));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void addXGeneral(WaterGenerator wg) {
/* 160 */     waterPointsX.put(Integer.valueOf(wg.x), wg);
/* 161 */     wg.generateXMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void generateXMap() {
/* 166 */     this.waterPointsY = new ConcurrentHashMap<>();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void addY(WaterGenerator wg) {
/* 171 */     this.waterPointsY.put(Integer.valueOf(wg.y), wg);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final WaterGenerator getWG(int x, int y) {
/* 176 */     WaterGenerator xgeneral = getXGeneral(x);
/* 177 */     if (xgeneral == null) {
/* 178 */       return null;
/*     */     }
/* 180 */     return xgeneral.getY(y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getHeight() {
/* 190 */     return this.height;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean changed() {
/* 195 */     return this.changed;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean changedSinceReset() {
/* 200 */     return (this.changed && this.height != this.resetHeight);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setHeight(int aHeight) {
/* 211 */     if (this.lastHeight != aHeight) {
/*     */       
/* 213 */       this.changed = true;
/*     */       
/* 215 */       this.isReset = false;
/* 216 */       this.lastHeight = aHeight;
/* 217 */       this.height = aHeight;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void updateItem() {
/* 223 */     if (this.changed)
/*     */     {
/* 225 */       if (shouldCreateItem()) {
/* 226 */         createItem();
/*     */       
/*     */       }
/* 229 */       else if (this.height == 0 && !this.spring) {
/*     */         
/* 231 */         deleteItem();
/*     */         
/* 233 */         if (this.waterPointsY != null) {
/*     */           
/* 235 */           this.waterPointsY.remove(Integer.valueOf(this.y));
/* 236 */           waterPointsX.remove(Integer.valueOf(this.x));
/* 237 */           if (this.waterPointsY.size() > 0)
/*     */           {
/*     */             
/* 240 */             WaterGenerator[] gens = (WaterGenerator[])this.waterPointsY.values().toArray((Object[])new WaterGenerator[this.waterPointsY.size()]);
/* 241 */             addXGeneral(gens[0]);
/* 242 */             gens[0].addWaterPointsY(this.waterPointsY);
/*     */           }
/*     */         
/*     */         } 
/*     */       } else {
/*     */         
/* 248 */         this.waterMarker.setName("" + this.height);
/* 249 */         this.waterMarker.updateIfGroundItem();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void addWaterPointsY(Map<Integer, WaterGenerator> wpy) {
/* 257 */     this.waterPointsY = wpy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void deleteItem() {
/*     */     try {
/* 268 */       Items.destroyItem(this.waterMarker.getWurmId());
/*     */     
/*     */     }
/* 271 */     catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long getTileId() {
/* 280 */     return Tiles.getTileId(this.x, this.y, 0, (this.layer >= 0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(WaterGenerator o) {
/* 291 */     return o.x + o.y + o.layer + o.height - this.x + this.y + this.layer + this.height;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReset() {
/* 301 */     return this.isReset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReset(boolean aIsReset) {
/* 312 */     this.isReset = aIsReset;
/* 313 */     this.resetHeight = this.height;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\WaterGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */