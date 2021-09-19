/*     */ package com.wurmonline.server.items;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.FailedException;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.bodys.Body;
/*     */ import com.wurmonline.server.effects.Effect;
/*     */ import com.wurmonline.server.effects.EffectFactory;
/*     */ import com.wurmonline.server.epic.HexMap;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
/*     */ import com.wurmonline.server.zones.Zone;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import com.wurmonline.shared.constants.ItemMaterials;
/*     */ import java.io.IOException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Optional;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
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
/*     */ public final class ItemFactory
/*     */   implements MiscConstants, ItemTypes, ItemMaterials
/*     */ {
/*  54 */   private static final Logger logger = Logger.getLogger(ItemFactory.class.getName());
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
/*     */   private static final String deleteItemData = "delete from ITEMDATA where WURMID=?";
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
/*     */   private static DbStrings dbstrings;
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
/*     */   @Nonnull
/*     */   public static Item createItem(int templateId, float qualityLevel, byte material, byte aRarity, @Nullable String creator) throws FailedException, NoSuchTemplateException {
/*  90 */     return createItem(templateId, qualityLevel, material, aRarity, -10L, creator);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Optional<Item> createItemOptional(int templateId, float qualityLevel, byte material, byte aRarity, @Nullable String creator) {
/*     */     try {
/*  96 */       return Optional.of(createItem(templateId, qualityLevel, material, aRarity, creator));
/*     */     }
/*  98 */     catch (Exception e) {
/*     */       
/* 100 */       e.printStackTrace();
/* 101 */       return Optional.empty();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void createContainerRestrictions(Item item) {
/* 112 */     ItemTemplate template = item.getTemplate();
/* 113 */     if (template.getContainerRestrictions() != null && !template.isNoPut())
/*     */     {
/* 115 */       for (ContainerRestriction cRest : template.getContainerRestrictions()) {
/*     */         
/* 117 */         boolean skipAdd = false;
/* 118 */         for (Item i : item.getItems()) {
/* 119 */           if (i.getTemplateId() == 1392 && cRest.contains(i.getRealTemplateId())) {
/* 120 */             skipAdd = true; continue;
/* 121 */           }  if (cRest.contains(i.getTemplateId()))
/* 122 */             skipAdd = true; 
/*     */         } 
/* 124 */         if (!skipAdd) {
/*     */           
/*     */           try {
/*     */             
/* 128 */             Item tempSlotItem = createItem(1392, 100.0F, item.getCreatorName());
/* 129 */             tempSlotItem.setRealTemplate(cRest.getEmptySlotTemplateId());
/* 130 */             tempSlotItem.setName(cRest.getEmptySlotName());
/*     */             
/* 132 */             item.insertItem(tempSlotItem, true);
/*     */           }
/* 134 */           catch (FailedException|NoSuchTemplateException failedException) {}
/*     */         }
/*     */       } 
/*     */     }
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
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static Item createItem(int templateId, float qualityLevel, byte material, byte aRarity, long bridgeId, @Nullable String creator) throws FailedException, NoSuchTemplateException {
/*     */     Item toReturn;
/* 162 */     ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(templateId);
/*     */ 
/*     */     
/* 165 */     if (material == 0)
/* 166 */       material = template.getMaterial(); 
/* 167 */     String name = generateName(template, material);
/* 168 */     if (template.isTemporary()) {
/*     */ 
/*     */       
/*     */       try {
/* 172 */         toReturn = new TempItem(name, template, qualityLevel, creator);
/* 173 */         if (logger.isLoggable(Level.FINEST))
/*     */         {
/* 175 */           logger.finest("Creating tempitem: " + toReturn);
/*     */         }
/*     */       }
/* 178 */       catch (IOException ex) {
/*     */         
/* 180 */         throw new FailedException(ex);
/*     */       } 
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/* 187 */         if (template.isRecycled) {
/*     */           
/* 189 */           Item item = Itempool.getRecycledItem(templateId, qualityLevel);
/* 190 */           if (item != null) {
/*     */             
/* 192 */             if (item.isTemporary()) {
/* 193 */               item.clear(WurmId.getNextTempItemId(), creator, 0.0F, 0.0F, 0.0F, 1.0F, "", name, qualityLevel, material, aRarity, bridgeId);
/*     */             } else {
/*     */               
/* 196 */               item.clear(item.id, creator, 0.0F, 0.0F, 0.0F, 1.0F, "", name, qualityLevel, material, aRarity, bridgeId);
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/* 201 */             return item;
/*     */           } 
/*     */         } 
/*     */         
/* 205 */         toReturn = new DbItem(-10L, name, template, qualityLevel, material, aRarity, bridgeId, creator);
/* 206 */         if (template.isCoin()) {
/* 207 */           Server.getInstance().transaction(toReturn.getWurmId(), -10L, bridgeId, "new " + toReturn.getName(), template
/* 208 */               .getValue());
/*     */         }
/* 210 */       } catch (IOException iox) {
/*     */         
/* 212 */         throw new FailedException(iox);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 218 */     if (template.getInitialContainers() != null)
/*     */     {
/* 220 */       for (InitialContainer ic : template.getInitialContainers()) {
/*     */         
/* 222 */         byte icMaterial = (ic.getMaterial() == 0) ? material : ic.getMaterial();
/* 223 */         Item subItem = createItem(ic.getTemplateId(), Math.max(1.0F, qualityLevel), icMaterial, aRarity, creator);
/*     */         
/* 225 */         subItem.setName(ic.getName());
/* 226 */         toReturn.insertItem(subItem, true);
/*     */       } 
/*     */     }
/*     */     
/* 230 */     if (toReturn != null) {
/* 231 */       createContainerRestrictions(toReturn);
/*     */     }
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
/* 259 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Item createItem(int templateId, float qualityLevel, byte aRarity, @Nullable String creator) throws FailedException, NoSuchTemplateException {
/* 266 */     return createItem(templateId, qualityLevel, (byte)0, aRarity, creator);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Optional<Item> createItemOptional(int templateId, float qualityLevel, byte aRarity, @Nullable String creator) {
/* 271 */     return createItemOptional(templateId, qualityLevel, (byte)0, aRarity, creator);
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
/*     */   @Nonnull
/*     */   public static Item createItem(int templateId, float qualityLevel, @Nullable String creator) throws FailedException, NoSuchTemplateException {
/* 291 */     return createItem(templateId, qualityLevel, (byte)0, (byte)0, creator);
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
/*     */   public static String generateName(ItemTemplate template, byte material) {
/* 304 */     String name = template.sizeString + template.getName();
/* 305 */     if (template.getTemplateId() == 683)
/* 306 */       name = HexMap.generateFirstName() + " " + HexMap.generateSecondName(); 
/* 307 */     if (template.unique)
/* 308 */       name = template.getName(); 
/* 309 */     return name;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Item createBodyPart(Body body, short place, int templateId, String name, float qualityLevel) throws FailedException, NoSuchTemplateException {
/* 335 */     ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(templateId);
/* 336 */     Item toReturn = null;
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
/*     */     try {
/* 349 */       long wurmId = WurmId.getNextBodyPartId(body.getOwnerId(), (byte)place, 
/* 350 */           (WurmId.getType(body.getOwnerId()) == 0));
/* 351 */       if (template.isRecycled) {
/*     */         
/* 353 */         toReturn = Itempool.getRecycledItem(templateId, qualityLevel);
/* 354 */         if (toReturn != null) {
/*     */           
/* 356 */           toReturn.clear(-10L, "", 0.0F, 0.0F, 0.0F, 0.0F, "", name, qualityLevel, template.getMaterial(), (byte)0, -10L);
/*     */           
/* 358 */           toReturn.setPlace(place);
/*     */         } 
/*     */       } 
/* 361 */       if (toReturn == null) {
/* 362 */         toReturn = new TempItem(wurmId, place, name, template, qualityLevel, "");
/*     */       }
/*     */     }
/* 365 */     catch (IOException ex) {
/*     */       
/* 367 */       throw new FailedException(ex);
/*     */     } 
/*     */     
/* 370 */     return toReturn;
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
/*     */   @Nullable
/*     */   public static Item createInventory(long ownerId, short place, float qualityLevel) throws FailedException, NoSuchTemplateException {
/* 391 */     ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(0);
/* 392 */     Item toReturn = null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 397 */       long wurmId = WurmId.getNextBodyPartId(ownerId, (byte)place, 
/* 398 */           (WurmId.getType(ownerId) == 0));
/* 399 */       if (template.isRecycled) {
/*     */         
/* 401 */         toReturn = Itempool.getRecycledItem(0, qualityLevel);
/* 402 */         if (toReturn != null)
/*     */         {
/* 404 */           toReturn.clear(wurmId, "", 0.0F, 0.0F, 0.0F, 0.0F, "", "inventory", qualityLevel, template
/* 405 */               .getMaterial(), (byte)0, -10L);
/*     */         }
/*     */       } 
/* 408 */       if (toReturn == null) {
/* 409 */         toReturn = new TempItem(wurmId, place, "inventory", template, qualityLevel, "");
/*     */       
/*     */       }
/*     */     }
/* 413 */     catch (IOException ex) {
/*     */       
/* 415 */       throw new FailedException(ex);
/*     */     } 
/*     */     
/* 418 */     return toReturn;
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
/*     */   public static Item loadItem(long id) throws NoSuchItemException, Exception {
/* 433 */     Item item = null;
/* 434 */     if (WurmId.getType(id) == 2 || WurmId.getType(id) == 19 || 
/* 435 */       WurmId.getType(id) == 20) {
/*     */ 
/*     */       
/* 438 */       item = new DbItem(id);
/*     */     } else {
/*     */       
/* 441 */       throw new NoSuchItemException("Temporary item.");
/* 442 */     }  return item;
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
/*     */   public static void decay(long id, @Nullable DbStrings dbStrings) {
/* 457 */     dbstrings = dbStrings;
/* 458 */     if (dbstrings == null)
/* 459 */       dbstrings = Item.getDbStringsByWurmId(id); 
/* 460 */     Connection dbcon = null;
/* 461 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 464 */       dbcon = DbConnector.getItemDbCon();
/* 465 */       ps = dbcon.prepareStatement(dbstrings.deleteItem());
/* 466 */       ps.setLong(1, id);
/* 467 */       ps.executeUpdate();
/* 468 */       DbUtilities.closeDatabaseObjects(ps, null);
/*     */       
/* 470 */       ps = dbcon.prepareStatement("delete from ITEMDATA where WURMID=?");
/* 471 */       ps.setLong(1, id);
/* 472 */       ps.executeUpdate();
/* 473 */       DbUtilities.closeDatabaseObjects(ps, null);
/*     */       
/* 475 */       ps = dbcon.prepareStatement("DELETE FROM ITEMKEYS WHERE LOCKID=?");
/* 476 */       ps.setLong(1, id);
/* 477 */       ps.executeUpdate();
/*     */     }
/* 479 */     catch (SQLException ex) {
/*     */       
/* 481 */       logger.log(Level.WARNING, "Failed to decay item with id " + id, ex);
/*     */     }
/*     */     finally {
/*     */       
/* 485 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 486 */       DbConnector.returnConnection(dbcon);
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clearData(long id) {
/* 516 */     Connection dbcon = null;
/* 517 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 520 */       dbcon = DbConnector.getItemDbCon();
/* 521 */       ps = dbcon.prepareStatement("delete from ITEMDATA where WURMID=?");
/* 522 */       ps.setLong(1, id);
/* 523 */       ps.executeUpdate();
/* 524 */       DbUtilities.closeDatabaseObjects(ps, null);
/*     */       
/* 526 */       ps = dbcon.prepareStatement("DELETE FROM ITEMKEYS WHERE LOCKID=?");
/* 527 */       ps.setLong(1, id);
/* 528 */       ps.executeUpdate();
/*     */     }
/* 530 */     catch (SQLException ex) {
/*     */       
/* 532 */       logger.log(Level.WARNING, "Failed to decay item with id " + id, ex);
/*     */     }
/*     */     finally {
/*     */       
/* 536 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 537 */       DbConnector.returnConnection(dbcon);
/*     */     } 
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
/*     */   public static Item createItem(int templateId, float qualityLevel, float posX, float posY, float rot, boolean onSurface, byte rarity, long bridgeId, @Nullable String creator) throws NoSuchTemplateException, FailedException {
/* 582 */     return createItem(templateId, qualityLevel, posX, posY, rot, onSurface, (byte)0, rarity, bridgeId, creator);
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
/*     */   public static Item createItem(int templateId, float qualityLevel, float posX, float posY, float rot, boolean onSurface, byte material, byte aRarity, long bridgeId, @Nullable String creator) throws NoSuchTemplateException, FailedException {
/* 613 */     return createItem(templateId, qualityLevel, posX, posY, rot, onSurface, material, aRarity, bridgeId, creator, (byte)0);
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
/*     */   public static Item createItem(int templateId, float qualityLevel, float posX, float posY, float rot, boolean onSurface, byte material, byte aRarity, long bridgeId, @Nullable String creator, byte initialAuxData) throws NoSuchTemplateException, FailedException {
/* 644 */     float height = 0.0F;
/*     */     
/*     */     try {
/* 647 */       height = Zones.calculateHeight(posX, posY, onSurface);
/*     */     }
/* 649 */     catch (NoSuchZoneException nsz) {
/*     */       
/* 651 */       logger.log(Level.WARNING, "Could not calculate height for position: " + posX + ", " + posY + ", surfaced: " + onSurface + " due to " + nsz
/* 652 */           .getMessage(), (Throwable)nsz);
/*     */     } 
/*     */     
/* 655 */     if (logger.isLoggable(Level.FINER))
/*     */     {
/* 657 */       logger.finer("Factory trying to create item with id " + templateId + " at " + posX + ", " + posY + ", " + height + ".");
/*     */     }
/*     */     
/* 660 */     ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(templateId);
/*     */     
/* 662 */     if (material == 0)
/* 663 */       material = template.getMaterial(); 
/* 664 */     String name = generateName(template, material);
/* 665 */     Item toReturn = null;
/*     */     
/*     */     try {
/* 668 */       if (template.isRecycled) {
/*     */         
/* 670 */         toReturn = Itempool.getRecycledItem(templateId, qualityLevel);
/* 671 */         if (toReturn != null)
/*     */         {
/* 673 */           if (toReturn.isTemporary()) {
/* 674 */             toReturn.clear(WurmId.getNextTempItemId(), creator, posX, posY, height, rot, "", name, qualityLevel, material, aRarity, bridgeId);
/*     */           } else {
/*     */             
/* 677 */             toReturn.clear(toReturn.id, creator, posX, posY, height, rot, "", name, qualityLevel, material, aRarity, bridgeId);
/*     */           } 
/*     */         }
/*     */       } 
/* 681 */       if (toReturn == null)
/*     */       {
/* 683 */         if (template.isTemporary()) {
/* 684 */           toReturn = new TempItem(name, template, qualityLevel, posX, posY, height, rot, bridgeId, creator);
/*     */         } else {
/*     */           
/* 687 */           toReturn = new DbItem(name, template, qualityLevel, posX, posY, height, rot, material, aRarity, bridgeId, creator);
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 695 */         if (toReturn.getTemplateId() == 385 || toReturn.getTemplateId() == 731)
/* 696 */           toReturn.setAuxData((byte)(100 + initialAuxData)); 
/* 697 */         Zone zone = Zones.getZone((int)posX >> 2, (int)posY >> 2, onSurface);
/* 698 */         zone.addItem(toReturn);
/*     */         
/* 700 */         if (toReturn.getTemplateId() == 385 || toReturn.getTemplateId() == 731) {
/* 701 */           toReturn.setAuxData(initialAuxData);
/*     */         }
/* 703 */       } catch (NoSuchZoneException sex) {
/*     */         
/* 705 */         logger.log(Level.WARNING, "Could not get Zone for position: " + posX + ", " + posY + ", surfaced: " + onSurface + " due to " + sex
/* 706 */             .getMessage(), (Throwable)sex);
/*     */       }
/*     */     
/* 709 */     } catch (IOException ex) {
/*     */       
/* 711 */       throw new FailedException(ex);
/*     */     } 
/* 713 */     toReturn.setOwner(-10L, true);
/* 714 */     if (toReturn.isFire()) {
/*     */       
/* 716 */       toReturn.setTemperature((short)20000);
/* 717 */       Effect effect = EffectFactory.getInstance().createFire(toReturn.getWurmId(), toReturn.getPosX(), toReturn
/* 718 */           .getPosY(), toReturn.getPosZ(), toReturn.isOnSurface());
/* 719 */       toReturn.addEffect(effect);
/*     */     } 
/* 721 */     return toReturn;
/*     */   }
/*     */   
/* 724 */   public static int[] metalLumpList = new int[] { 46, 221, 223, 205, 47, 220, 49, 44, 45, 48, 837, 698, 694, 1411 };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isMetalLump(int itemTemplateId) {
/* 730 */     for (int lumpId : metalLumpList) {
/* 731 */       if (lumpId == itemTemplateId)
/* 732 */         return true; 
/*     */     } 
/* 734 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Optional<Item> createItemOptional(int itemTemplateId, float qualityLevel, String creator) {
/*     */     try {
/* 740 */       return Optional.of(createItem(itemTemplateId, qualityLevel, creator));
/*     */     }
/* 742 */     catch (Exception e) {
/*     */       
/* 744 */       e.printStackTrace();
/* 745 */       return Optional.empty();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\ItemFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */