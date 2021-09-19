/*     */ package com.wurmonline.server.items;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ItemDbStrings
/*     */   implements DbStrings
/*     */ {
/*     */   private static ItemDbStrings instance;
/*     */   
/*     */   public String createItem() {
/*  36 */     return "insert into ITEMS (WURMID, TEMPLATEID, NAME,QUALITYLEVEL,ORIGINALQUALITYLEVEL, LASTMAINTAINED, OWNERID, SIZEX, SIZEY, SIZEZ, ZONEID, DAMAGE, ROTATION, PARENTID, WEIGHT, MATERIAL, LOCKID,DESCRIPTION,CREATIONDATE,RARITY,CREATOR,ONBRIDGE,SETTINGS) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String transferItem() {
/*  42 */     return "insert into ITEMS (WURMID, TEMPLATEID, NAME,QUALITYLEVEL,ORIGINALQUALITYLEVEL, LASTMAINTAINED, OWNERID,SIZEX, SIZEY, SIZEZ, ZONEID, DAMAGE, ROTATION, PARENTID, WEIGHT, MATERIAL, LOCKID,DESCRIPTION,BLESS,ENCHANT,TEMPERATURE, PRICE,BANKED,AUXDATA,CREATIONDATE,CREATIONSTATE,REALTEMPLATE,WORNARMOUR,COLOR,COLOR2,PLACE,POSX,POSY,POSZ,CREATOR,FEMALE,MAILED,MAILTIMES,RARITY,ONBRIDGE,LASTOWNERID,SETTINGS) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String loadItem() {
/*  48 */     return "select * from ITEMS where WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String loadEffects() {
/*  56 */     return "select * from EFFECTS where OWNER=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLock() {
/*  62 */     return "select * from LOCKS where WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getKeys() {
/*  68 */     return "select KEYID from ITEMKEYS where LOCKID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String addKey() {
/*  74 */     return "INSERT INTO ITEMKEYS (LOCKID,KEYID) VALUES(?,?)";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String removeKey() {
/*  80 */     return "DELETE FROM ITEMKEYS WHERE KEYID=? AND LOCKID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String createLock() {
/*  86 */     return "insert into LOCKS ( WURMID, LOCKED) values(?,?)";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String setZoneId() {
/*  94 */     return "UPDATE ITEMS SET ZONEID=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getZoneId() {
/* 100 */     return "SELECT ZONEID FROM ITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setParentId() {
/* 106 */     return "UPDATE ITEMS SET PARENTID=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParentId() {
/* 112 */     return "SELECT PARENTID FROM ITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setTemplateId() {
/* 118 */     return "UPDATE ITEMS SET TEMPLATEID=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTemplateId() {
/* 124 */     return "SELECT TEMPLATEID FROM ITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setInscription() {
/* 130 */     return "UPDATE INSCRIPTIONS SET INSCRIPTION=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInscription() {
/* 136 */     return "SELECT INSCRIPTION FROM INSCRIPTIONS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String createInscription() {
/* 142 */     return "INSERT INTO INSCRIPTIONS (WURMID, INSCRIPTION, INSCRIBER, PENCOLOR) VALUES (?,?,?,?)";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setName() {
/* 148 */     return "UPDATE ITEMS SET NAME=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 154 */     return "SELECT NAME FROM ITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setRarity() {
/* 160 */     return "UPDATE ITEMS SET RARITY=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setDescription() {
/* 166 */     return "UPDATE ITEMS SET DESCRIPTION=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 172 */     return "SELECT DESCRIPTION FROM ITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setPlace() {
/* 178 */     return "UPDATE ITEMS SET PLACE=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPlace() {
/* 184 */     return "SELECT PLACE FROM ITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setQualityLevel() {
/* 190 */     return "UPDATE ITEMS SET QUALITYLEVEL=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getQualityLevel() {
/* 196 */     return "SELECT QUALITYLEVEL FROM ITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setOriginalQualityLevel() {
/* 202 */     return "UPDATE ITEMS SET ORIGINALQUALITYLEVEL=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOriginalQualityLevel() {
/* 208 */     return "SELECT ORIGINALQUALITYLEVEL FROM ITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setLastMaintained() {
/* 214 */     return "INSERT INTO ITEMS (LASTMAINTAINED, WURMID) VALUES (?, ?) ON DUPLICATE KEY UPDATE LASTMAINTAINED=VALUES(LASTMAINTAINED)";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setLastMaintainedOld() {
/* 220 */     return "UPDATE ITEMS SET LASTMAINTAINED=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLastMaintained() {
/* 226 */     return "SELECT LASTMAINTAINED FROM ITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setOwnerId() {
/* 232 */     return "UPDATE ITEMS SET OWNERID=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setLastOwnerId() {
/* 238 */     return "UPDATE ITEMS SET LASTOWNERID=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOwnerId() {
/* 244 */     return "SELECT OWNERID FROM ITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setPosXYZRotation() {
/* 250 */     return "UPDATE ITEMS SET POSX=?, POSY=?, POSZ=?, ROTATION=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPosXYZRotation() {
/* 256 */     return "SELECT POSX, POSY, POSZ, ROTATION FROM ITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setPosXYZ() {
/* 262 */     return "UPDATE ITEMS SET POSX=?, POSY=?, POSZ=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPosXYZ() {
/* 268 */     return "SELECT POSX, POSY, POSZ FROM ITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setPosXY() {
/* 274 */     return "UPDATE ITEMS SET POSX=?, POSY=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPosXY() {
/* 280 */     return "SELECT POSX, POSY FROM ITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setPosX() {
/* 286 */     return "UPDATE ITEMS SET POSX=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPosX() {
/* 292 */     return "SELECT POSX FROM ITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setWeight() {
/* 298 */     return "UPDATE ITEMS SET WEIGHT=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getWeight() {
/* 304 */     return "SELECT WEIGHT FROM ITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setPosY() {
/* 310 */     return "UPDATE ITEMS SET POSY=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPosY() {
/* 316 */     return "SELECT POSY FROM ITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setPosZ() {
/* 322 */     return "UPDATE ITEMS SET POSZ=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPosZ() {
/* 328 */     return "SELECT POSZ FROM ITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setRotation() {
/* 334 */     return "UPDATE ITEMS SET ROTATION=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRotation() {
/* 340 */     return "SELECT ROTATION FROM ITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String savePos() {
/* 346 */     return "UPDATE ITEMS SET POSX=?,POSY=?,POSZ=?,ROTATION=?,ONBRIDGE=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String clearItem() {
/* 352 */     return "UPDATE ITEMS SET NAME=?,DESCRIPTION=?,QUALITYLEVEL=?,ORIGINALQUALITYLEVEL=?,LASTMAINTAINED=?,ENCHANT=?,BANKED=?,SIZEX=?,SIZEY=?,SIZEZ=?,ZONEID=?,DAMAGE=?,PARENTID=?, ROTATION=?,WEIGHT=?,POSX=?,POSY=?,POSZ=?,CREATOR=?,AUXDATA=?,COLOR=?,COLOR2=?,TEMPERATURE=?,CREATIONDATE=?,CREATIONSTATE=0,MATERIAL=?, BLESS=?, MAILED=0, MAILTIMES=0,RARITY=?,CREATIONSTATE=?, OWNERID=-10, LASTOWNERID=-10 WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setDamage() {
/* 358 */     return "INSERT INTO ITEMS (DAMAGE, LASTMAINTAINED, WURMID) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE DAMAGE=VALUES(DAMAGE), LASTMAINTAINED=VALUES(LASTMAINTAINED)";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setDamageOld() {
/* 364 */     return "UPDATE ITEMS SET DAMAGE=?, LASTMAINTAINED=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDamage() {
/* 370 */     return "SELECT DAMAGE FROM ITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setLocked() {
/* 376 */     return "UPDATE LOCKS SET LOCKED=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLocked() {
/* 382 */     return "SELECT LOCKED FROM LOCKS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setTransferred() {
/* 388 */     return "UPDATE ITEMS SET TRANSFERRED=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAllItems() {
/* 394 */     return "SELECT * from ITEMS where PARENTID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getItem() {
/* 400 */     return "SELECT * from ITEMS where WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String setBless() {
/* 410 */     return "UPDATE ITEMS SET BLESS=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setSizeX() {
/* 416 */     return "UPDATE ITEMS SET SIZEX=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSizeX() {
/* 422 */     return "SELECT SIZEX FROM ITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setSizeY() {
/* 428 */     return "UPDATE ITEMS SET SIZEY=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSizeY() {
/* 434 */     return "SELECT SIZEY FROM ITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setSizeZ() {
/* 440 */     return "UPDATE ITEMS SET SIZEZ=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSizeZ() {
/* 446 */     return "SELECT SIZEZ FROM ITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setLockId() {
/* 452 */     return "UPDATE ITEMS SET LOCKID=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setPrice() {
/* 458 */     return "UPDATE ITEMS SET PRICE=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setAuxData() {
/* 464 */     return "UPDATE ITEMS SET AUXDATA=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setCreationState() {
/* 470 */     return "UPDATE ITEMS SET CREATIONSTATE=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setRealTemplate() {
/* 476 */     return "UPDATE ITEMS SET REALTEMPLATE=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setColor() {
/* 482 */     return "UPDATE ITEMS SET COLOR=?,COLOR2=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setEnchant() {
/* 488 */     return "UPDATE ITEMS SET ENCHANT=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setBanked() {
/* 494 */     return "UPDATE ITEMS SET BANKED=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getData() {
/* 500 */     return "select * from ITEMDATA where WURMID=?";
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
/*     */   public String createData() {
/* 515 */     if (DbConnector.isUseSqlite())
/* 516 */       return "insert OR IGNORE into ITEMDATA ( DATA1, DATA2, EXTRA1, EXTRA2, WURMID) values(?,?,?,?,?)"; 
/* 517 */     return "insert IGNORE into ITEMDATA ( DATA1, DATA2, EXTRA1, EXTRA2, WURMID) values(?,?,?,?,?)";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String updateData1() {
/* 523 */     return "update ITEMDATA set DATA1=? where WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String updateData2() {
/* 529 */     return "update ITEMDATA set DATA2=? where WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String updateExtra1() {
/* 535 */     return "update ITEMDATA set EXTRA1=? where WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String updateExtra2() {
/* 541 */     return "update ITEMDATA set EXTRA2=? where WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String updateAllData() {
/* 547 */     return "update ITEMDATA set DATA1=?, DATA2=?, EXTRA1=?, EXTRA2=? where WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setTemperature() {
/* 553 */     return "UPDATE ITEMS SET TEMPERATURE=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTemperature() {
/* 559 */     return "SELECT TEMPERATURE FROM ITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setMaterial() {
/* 565 */     return "UPDATE ITEMS SET MATERIAL=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setWornAsArmour() {
/* 571 */     return "UPDATE ITEMS SET WORNARMOUR=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setFemale() {
/* 577 */     return "UPDATE ITEMS SET FEMALE=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setMailed() {
/* 583 */     return "UPDATE ITEMS SET MAILED=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setCreator() {
/* 589 */     return "UPDATE ITEMS SET CREATOR=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getZoneItems() {
/* 595 */     return "SELECT * FROM ITEMS WHERE OWNERID=-10";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCreatureItems() {
/* 601 */     return "SELECT * FROM ITEMS WHERE OWNERID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPreloadedItems() {
/* 607 */     return "SELECT * FROM ITEMS WHERE TEMPLATEID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCreatureItemsNonTransferred() {
/* 613 */     return "SELECT WURMID FROM ITEMS WHERE OWNERID=? AND TRANSFERRED=0";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String updateLastMaintainedBankItem() {
/* 619 */     return "UPDATE ITEMS SET LASTMAINTAINED=? WHERE BANKED=1";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getItemWeights() {
/* 625 */     return "SELECT WURMID, WEIGHT,SIZEX,SIZEY,SIZEZ, TEMPLATEID FROM ITEMS";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOwnedItems() {
/* 631 */     return "SELECT OWNERID FROM ITEMS WHERE OWNERID>0 GROUP BY OWNERID";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String deleteByOwnerId() {
/* 637 */     return "DELETE FROM ITEMS WHERE OWNERID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String deleteTransferedItem() {
/* 643 */     return "DELETE FROM ITEMS WHERE WURMID=? AND TRANSFERRED=0";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String deleteItem() {
/* 649 */     return "delete from ITEMS where WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRecycledItems() {
/* 655 */     return "SELECT * FROM ITEMS WHERE TEMPLATEID=? AND BANKED=1";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getItemsForZone() {
/* 661 */     return "Select WURMID from ITEMS where ZONEID=? AND BANKED=0";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setHidden() {
/* 667 */     return "UPDATE ITEMS SET HIDDEN=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setSettings() {
/* 673 */     return "UPDATE ITEMS SET SETTINGS=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setMailTimes() {
/* 679 */     return "UPDATE ITEMS SET MAILTIMES=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String freeze() {
/* 685 */     return "INSERT INTO FROZENITEMS SELECT * FROM ITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String thaw() {
/* 691 */     return "INSERT INTO ITEMS SELECT * FROM FROZENITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ItemDbStrings getInstance() {
/* 702 */     if (instance == null)
/* 703 */       instance = new ItemDbStrings(); 
/* 704 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getDbStringsType() {
/* 710 */     return "ItemDbStrings";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\ItemDbStrings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */