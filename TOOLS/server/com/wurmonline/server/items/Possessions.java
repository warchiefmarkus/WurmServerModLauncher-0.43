/*     */ package com.wurmonline.server.items;
/*     */ 
/*     */ import com.wurmonline.server.FailedException;
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.shared.constants.CounterTypes;
/*     */ import edu.umd.cs.findbugs.annotations.Nullable;
/*     */ import java.io.IOException;
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
/*     */ public final class Possessions
/*     */   implements CounterTypes
/*     */ {
/*     */   private final Item inventory;
/*     */   @Nullable
/*     */   private Creature owner;
/*  42 */   private static final Logger logger = Logger.getLogger(Possessions.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Possessions(Creature aOwner) throws NoSuchTemplateException, FailedException, NoSuchPlayerException, NoSuchCreatureException {
/*  50 */     this.owner = aOwner;
/*  51 */     if (this.owner.isPlayer()) {
/*  52 */       this.inventory = ItemFactory.createItem(0, 100.0F, null);
/*     */     } else {
/*  54 */       this.inventory = ItemFactory.createInventory(this.owner.getWurmId(), (short)48, 100.0F);
/*  55 */     }  assert this.inventory != null;
/*  56 */     this.inventory.setOwner(aOwner.getWurmId(), true);
/*     */   }
/*     */ 
/*     */   
/*     */   public Possessions(Creature aOwner, long aInventoryId) throws Exception {
/*  61 */     this.owner = aOwner;
/*  62 */     if (!this.owner.isPlayer()) {
/*     */       
/*  64 */       if (WurmId.getType(aInventoryId) == 19) {
/*     */         
/*  66 */         this.inventory = ItemFactory.createInventory(this.owner.getWurmId(), (short)48, 100.0F);
/*  67 */         this.inventory.setOwner(this.owner.getWurmId(), true);
/*  68 */         this.inventory.getContainedItems();
/*     */       }
/*     */       else {
/*     */         
/*  72 */         Item invent = Items.getItem(aInventoryId);
/*     */         
/*  74 */         this.inventory = ItemFactory.createInventory(this.owner.getWurmId(), (short)48, 100.0F);
/*     */         
/*  76 */         this.inventory.setOwner(this.owner.getWurmId(), true);
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
/*  89 */         Items.destroyItem(invent.getWurmId());
/*     */ 
/*     */         
/*  92 */         aOwner.getStatus().setInventoryId(this.inventory.getWurmId());
/*     */       } 
/*     */     } else {
/*     */       
/*  96 */       this.inventory = Items.getItem(aInventoryId);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Item getInventory() {
/* 105 */     return this.inventory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearOwner() {
/* 113 */     this.owner = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void save() throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sleep(boolean epicServer) throws IOException {
/* 127 */     this.inventory.sleep(this.owner, epicServer);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\Possessions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */