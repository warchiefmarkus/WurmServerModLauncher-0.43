/*     */ package com.wurmonline.server.items;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CoinDbStrings
/*     */   implements DbStrings
/*     */ {
/*     */   private static CoinDbStrings instance;
/*     */   
/*     */   public String createItem() {
/*  36 */     return "insert into COINS (WURMID, TEMPLATEID, NAME,QUALITYLEVEL,ORIGINALQUALITYLEVEL, LASTMAINTAINED, OWNERID, SIZEX, SIZEY, SIZEZ, ZONEID, DAMAGE, ROTATION, PARENTID, WEIGHT, MATERIAL, LOCKID,DESCRIPTION,CREATIONDATE,RARITY,CREATOR,ONBRIDGE,SETTINGS) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String transferItem() {
/*  42 */     return "insert into COINS (WURMID, TEMPLATEID, NAME,QUALITYLEVEL,ORIGINALQUALITYLEVEL, LASTMAINTAINED, OWNERID, SIZEX, SIZEY, SIZEZ, ZONEID, DAMAGE, ROTATION, PARENTID, WEIGHT, MATERIAL, LOCKID,DESCRIPTION,BLESS,ENCHANT,TEMPERATURE, PRICE,BANKED,AUXDATA,CREATIONDATE,CREATIONSTATE,REALTEMPLATE,WORNARMOUR,COLOR,COLOR2,PLACE,POSX,POSY,POSZ,CREATOR,FEMALE,MAILED,MAILTIMES,RARITY,ONBRIDGE,LASTOWNERID,SETTINGS) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String loadItem() {
/*  48 */     return "select * from COINS where WURMID=?";
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
/*  94 */     return "UPDATE COINS SET ZONEID=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getZoneId() {
/* 100 */     return "SELECT ZONEID FROM COINS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setParentId() {
/* 106 */     return "UPDATE COINS SET PARENTID=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParentId() {
/* 112 */     return "SELECT PARENTID FROM COINS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setTemplateId() {
/* 118 */     return "UPDATE COINS SET TEMPLATEID=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTemplateId() {
/* 124 */     return "SELECT TEMPLATEID FROM COINS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setName() {
/* 130 */     return "UPDATE COINS SET NAME=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 136 */     return "SELECT NAME FROM COINS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setDescription() {
/* 142 */     return "UPDATE COINS SET DESCRIPTION=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInscription() {
/* 148 */     return "SELECT INSCRIPTION FROM INSCRIPTIONS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setInscription() {
/* 154 */     return "UPDATE INSCRIPTIONS SET INSCRIPTION = ? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String createInscription() {
/* 160 */     return "insert into INSCRIPTIONS (WURMID, INSCRIPTION, INSCRIBER) VALUES (?,?,?)";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setRarity() {
/* 166 */     return "UPDATE COINS SET RARITY=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 172 */     return "SELECT DESCRIPTION FROM COINS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setPlace() {
/* 178 */     return "UPDATE COINS SET PLACE=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPlace() {
/* 184 */     return "SELECT PLACE FROM COINS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setQualityLevel() {
/* 190 */     return "UPDATE COINS SET QUALITYLEVEL=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getQualityLevel() {
/* 196 */     return "SELECT QUALITYLEVEL FROM COINS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setOriginalQualityLevel() {
/* 202 */     return "UPDATE COINS SET ORIGINALQUALITYLEVEL=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOriginalQualityLevel() {
/* 208 */     return "SELECT ORIGINALQUALITYLEVEL FROM COINS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setLastMaintained() {
/* 214 */     return "INSERT INTO COINS (LASTMAINTAINED, WURMID) VALUES (?, ?) ON DUPLICATE KEY UPDATE LASTMAINTAINED=VALUES(LASTMAINTAINED)";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setLastMaintainedOld() {
/* 220 */     return "UPDATE COINS SET LASTMAINTAINED=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLastMaintained() {
/* 226 */     return "SELECT LASTMAINTAINED FROM COINS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setOwnerId() {
/* 232 */     return "UPDATE COINS SET OWNERID=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setLastOwnerId() {
/* 238 */     return "UPDATE COINS SET LASTOWNERID=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOwnerId() {
/* 244 */     return "SELECT OWNERID FROM COINS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setPosXYZRotation() {
/* 250 */     return "UPDATE COINS SET POSX=?, POSY=?, POSZ=?, ROTATION=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPosXYZRotation() {
/* 256 */     return "SELECT POSX, POSY, POSZ, ROTATION FROM COINS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setPosXYZ() {
/* 262 */     return "UPDATE COINS SET POSX=?, POSY=?, POSZ=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPosXYZ() {
/* 268 */     return "SELECT POSX, POSY, POSZ FROM COINS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setPosXY() {
/* 274 */     return "UPDATE COINS SET POSX=?, POSY=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPosXY() {
/* 280 */     return "SELECT POSX, POSY FROM COINS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setPosX() {
/* 286 */     return "UPDATE COINS SET POSX=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPosX() {
/* 292 */     return "SELECT POSX FROM COINS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setWeight() {
/* 298 */     return "UPDATE COINS SET WEIGHT=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getWeight() {
/* 304 */     return "SELECT WEIGHT FROM COINS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setPosY() {
/* 310 */     return "UPDATE COINS SET POSY=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPosY() {
/* 316 */     return "SELECT POSY FROM COINS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setPosZ() {
/* 322 */     return "UPDATE COINS SET POSZ=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPosZ() {
/* 328 */     return "SELECT POSZ FROM COINS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setRotation() {
/* 334 */     return "UPDATE COINS SET ROTATION=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRotation() {
/* 340 */     return "SELECT ROTATION FROM COINS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String savePos() {
/* 346 */     return "UPDATE COINS SET POSX=?,POSY=?,POSZ=?,ROTATION=?,ONBRIDGE=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String clearItem() {
/* 352 */     return "UPDATE COINS SET NAME=?,DESCRIPTION=?,QUALITYLEVEL=?,ORIGINALQUALITYLEVEL=?,LASTMAINTAINED=?,ENCHANT=?,BANKED=?,SIZEX=?,SIZEY=?,SIZEZ=?,ZONEID=?,DAMAGE=?,PARENTID=?, ROTATION=?,WEIGHT=?,POSX=?,POSY=?,POSZ=?,CREATOR=?,AUXDATA=?,COLOR=?,COLOR2=?,TEMPERATURE=?,CREATIONDATE=?,CREATIONSTATE=0,MATERIAL=?, BLESS=?, MAILED=0, MAILTIMES=0,RARITY=?,CREATIONSTATE=?, OWNERID=-10, LASTOWNERID=-10 WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setDamage() {
/* 358 */     return "INSERT INTO COINS (DAMAGE, LASTMAINTAINED, WURMID) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE LASTMAINTAINED=VALUES(LASTMAINTAINED)";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setDamageOld() {
/* 364 */     return "UPDATE COINS SET DAMAGE=?, LASTMAINTAINED=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDamage() {
/* 370 */     return "SELECT DAMAGE FROM COINS WHERE WURMID=?";
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
/* 388 */     return "UPDATE COINS SET TRANSFERRED=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAllItems() {
/* 394 */     return "SELECT * from COINS where PARENTID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getItem() {
/* 400 */     return "SELECT * from COINS where WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String setBless() {
/* 410 */     return "UPDATE COINS SET BLESS=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setSizeX() {
/* 416 */     return "UPDATE COINS SET SIZEX=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSizeX() {
/* 422 */     return "SELECT SIZEX FROM COINS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setSizeY() {
/* 428 */     return "UPDATE COINS SET SIZEY=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSizeY() {
/* 434 */     return "SELECT SIZEY FROM COINS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setSizeZ() {
/* 440 */     return "UPDATE COINS SET SIZEZ=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSizeZ() {
/* 446 */     return "SELECT SIZEZ FROM COINS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setLockId() {
/* 452 */     return "UPDATE COINS SET LOCKID=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setPrice() {
/* 458 */     return "UPDATE COINS SET PRICE=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setAuxData() {
/* 464 */     return "UPDATE COINS SET AUXDATA=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setCreationState() {
/* 470 */     return "UPDATE COINS SET CREATIONSTATE=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setRealTemplate() {
/* 476 */     return "UPDATE COINS SET REALTEMPLATE=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setColor() {
/* 482 */     return "UPDATE COINS SET COLOR=?,COLOR2=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setEnchant() {
/* 488 */     return "UPDATE COINS SET ENCHANT=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setBanked() {
/* 494 */     return "UPDATE COINS SET BANKED=? WHERE WURMID=?";
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
/*     */   public String createData() {
/* 506 */     return "insert into ITEMDATA ( DATA1, DATA2, EXTRA1, EXTRA2, WURMID) values(?,?,?,?,?)";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String updateData1() {
/* 512 */     return "update ITEMDATA set DATA1=? where WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String updateData2() {
/* 518 */     return "update ITEMDATA set DATA2=? where WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String updateExtra1() {
/* 524 */     return "update ITEMDATA set EXTRA1=? where WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String updateExtra2() {
/* 530 */     return "update ITEMDATA set EXTRA2=? where WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String updateAllData() {
/* 536 */     return "update ITEMDATA set DATA1=?, DATA2=?, EXTRA1=?, EXTRA2=? where WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setTemperature() {
/* 542 */     return "UPDATE COINS SET TEMPERATURE=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTemperature() {
/* 548 */     return "SELECT TEMPERATURE FROM COINS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setMaterial() {
/* 554 */     return "UPDATE COINS SET MATERIAL=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setWornAsArmour() {
/* 560 */     return "UPDATE COINS SET WORNARMOUR=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setFemale() {
/* 566 */     return "UPDATE COINS SET FEMALE=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setMailed() {
/* 572 */     return "UPDATE COINS SET MAILED=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setCreator() {
/* 578 */     return "UPDATE COINS SET CREATOR=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getZoneItems() {
/* 584 */     return "SELECT * FROM COINS WHERE OWNERID=-10";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCreatureItems() {
/* 590 */     return "SELECT * FROM COINS WHERE OWNERID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPreloadedItems() {
/* 596 */     return "SELECT * FROM COINS WHERE TEMPLATEID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCreatureItemsNonTransferred() {
/* 602 */     return "SELECT WURMID FROM COINS WHERE OWNERID=? AND TRANSFERRED=0";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String updateLastMaintainedBankItem() {
/* 608 */     return "UPDATE COINS SET LASTMAINTAINED=? WHERE BANKED=1";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getItemWeights() {
/* 614 */     return "SELECT WURMID, WEIGHT,SIZEX,SIZEY,SIZEZ, TEMPLATEID FROM COINS";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOwnedItems() {
/* 620 */     return "SELECT OWNERID FROM COINS WHERE OWNERID>0 GROUP BY OWNERID";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String deleteByOwnerId() {
/* 626 */     return "UPDATE COINS SET OWNERID=-10 WHERE OWNERID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String deleteTransferedItem() {
/* 632 */     return "DELETE FROM COINS WHERE WURMID=? AND TRANSFERRED=0";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String deleteItem() {
/* 638 */     return "delete from COINS where WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRecycledItems() {
/* 644 */     return "SELECT * FROM COINS WHERE TEMPLATEID=? AND BANKED=1";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getItemsForZone() {
/* 650 */     return "Select WURMID from COINS where ZONEID=? AND BANKED=0";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setHidden() {
/* 656 */     return "UPDATE COINS SET HIDDEN=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setSettings() {
/* 662 */     return "UPDATE COINS SET SETTINGS=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setMailTimes() {
/* 668 */     return "UPDATE COINS SET MAILTIMES=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String freeze() {
/* 674 */     return "INSERT INTO FROZENITEMS SELECT * FROM ITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String thaw() {
/* 680 */     return "INSERT INTO ITEMS SELECT * FROM FROZENITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CoinDbStrings getInstance() {
/* 691 */     if (instance == null)
/* 692 */       instance = new CoinDbStrings(); 
/* 693 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getDbStringsType() {
/* 699 */     return "CoinDbStrings";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\CoinDbStrings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */