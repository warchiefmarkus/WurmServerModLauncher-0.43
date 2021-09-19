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
/*     */ public final class FrozenItemDbStrings
/*     */   implements DbStrings
/*     */ {
/*     */   private static FrozenItemDbStrings instance;
/*     */   
/*     */   public String createItem() {
/*  35 */     return "insert into FROZENITEMS (WURMID, TEMPLATEID, NAME,QUALITYLEVEL,ORIGINALQUALITYLEVEL, LASTMAINTAINED, OWNERID, SIZEX, SIZEY, SIZEZ, ZONEID, DAMAGE, ROTATION, PARENTID, WEIGHT, MATERIAL, LOCKID,DESCRIPTION,CREATIONDATE,RARITY,CREATOR,ONBRIDGE,SETTINGS) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String transferItem() {
/*  41 */     return "insert into FROZENITEMS (WURMID, TEMPLATEID, NAME,QUALITYLEVEL,ORIGINALQUALITYLEVEL, LASTMAINTAINED, OWNERID, SIZEX, SIZEY, SIZEZ, ZONEID, DAMAGE, ROTATION, PARENTID, WEIGHT, MATERIAL, LOCKID,DESCRIPTION,BLESS,ENCHANT,TEMPERATURE, PRICE,BANKED,AUXDATA,CREATIONDATE,CREATIONSTATE,REALTEMPLATE,WORNARMOUR,COLOR,COLOR2,PLACE,POSX,POSY,POSZ,CREATOR,FEMALE,MAILED,MAILTIMES,RARITY,ONBRIDGE,LASTOWNERID,SETTINGS) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String loadItem() {
/*  47 */     return "select * from FROZENITEMS where WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String loadEffects() {
/*  55 */     return "select * from EFFECTS where OWNER=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLock() {
/*  61 */     return "select * from LOCKS where WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getKeys() {
/*  67 */     return "select KEYID from ITEMKEYS where LOCKID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String addKey() {
/*  73 */     return "INSERT INTO ITEMKEYS (LOCKID,KEYID) VALUES(?,?)";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String removeKey() {
/*  79 */     return "DELETE FROM ITEMKEYS WHERE KEYID=? AND LOCKID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String createLock() {
/*  85 */     return "insert into LOCKS ( WURMID, LOCKED) values(?,?)";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String setZoneId() {
/*  93 */     return "UPDATE FROZENITEMS SET ZONEID=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getZoneId() {
/*  99 */     return "SELECT ZONEID FROM FROZENITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setParentId() {
/* 105 */     return "UPDATE FROZENITEMS SET PARENTID=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParentId() {
/* 111 */     return "SELECT PARENTID FROM FROZENITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setTemplateId() {
/* 117 */     return "UPDATE FROZENITEMS SET TEMPLATEID=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTemplateId() {
/* 123 */     return "SELECT TEMPLATEID FROM FROZENITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setName() {
/* 129 */     return "UPDATE FROZENITEMS SET NAME=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 135 */     return "SELECT NAME FROM FROZENITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInscription() {
/* 141 */     return "SELECT INSCRIPTION FROM INSCRIPTIONS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setInscription() {
/* 147 */     return "UPDATE INSCRIPTIONS SET INSCRIPTION = ? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setRarity() {
/* 153 */     return "UPDATE FROZENITEMS SET RARITY=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setDescription() {
/* 159 */     return "UPDATE FROZENITEMS SET DESCRIPTION=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String createInscription() {
/* 165 */     return "insert into INSCRIPTIONS (WURMID, INSCRIPTION, INSCRIBER) VALUES (?,?,?)";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 171 */     return "SELECT DESCRIPTION FROM FROZENITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setPlace() {
/* 177 */     return "UPDATE FROZENITEMS SET PLACE=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPlace() {
/* 183 */     return "SELECT PLACE FROM FROZENITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setQualityLevel() {
/* 189 */     return "UPDATE FROZENITEMS SET QUALITYLEVEL=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getQualityLevel() {
/* 195 */     return "SELECT QUALITYLEVEL FROM FROZENITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setOriginalQualityLevel() {
/* 201 */     return "UPDATE FROZENITEMS SET ORIGINALQUALITYLEVEL=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOriginalQualityLevel() {
/* 207 */     return "SELECT ORIGINALQUALITYLEVEL FROM FROZENITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setLastMaintained() {
/* 213 */     return "INSERT INTO FROZENITEMS (LASTMAINTAINED, WURMID) VALUES (?, ?) ON DUPLICATE KEY UPDATE LASTMAINTAINED=VALUES(LASTMAINTAINED)";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setLastMaintainedOld() {
/* 219 */     return "UPDATE FROZENITEMS SET LASTMAINTAINED=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLastMaintained() {
/* 225 */     return "SELECT LASTMAINTAINED FROM FROZENITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setOwnerId() {
/* 231 */     return "UPDATE FROZENITEMS SET OWNERID=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setLastOwnerId() {
/* 237 */     return "UPDATE FROZENITEMS SET LASTOWNERID=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOwnerId() {
/* 243 */     return "SELECT OWNERID FROM FROZENITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setPosXYZRotation() {
/* 249 */     return "UPDATE FROZENITEMS SET POSX=?, POSY=?, POSZ=?, ROTATION=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPosXYZRotation() {
/* 255 */     return "SELECT POSX, POSY, POSZ, ROTATION FROM FROZENITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setPosXYZ() {
/* 261 */     return "UPDATE FROZENITEMS SET POSX=?, POSY=?, POSZ=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPosXYZ() {
/* 267 */     return "SELECT POSX, POSY, POSZ FROM FROZENITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setPosXY() {
/* 273 */     return "UPDATE FROZENITEMS SET POSX=?, POSY=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPosXY() {
/* 279 */     return "SELECT POSX, POSY FROM FROZENITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setPosX() {
/* 285 */     return "UPDATE FROZENITEMS SET POSX=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPosX() {
/* 291 */     return "SELECT POSX FROM FROZENITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setWeight() {
/* 297 */     return "UPDATE FROZENITEMS SET WEIGHT=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getWeight() {
/* 303 */     return "SELECT WEIGHT FROM FROZENITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setPosY() {
/* 309 */     return "UPDATE FROZENITEMS SET POSY=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPosY() {
/* 315 */     return "SELECT POSY FROM FROZENITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setPosZ() {
/* 321 */     return "UPDATE FROZENITEMS SET POSZ=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPosZ() {
/* 327 */     return "SELECT POSZ FROM FROZENITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setRotation() {
/* 333 */     return "UPDATE FROZENITEMS SET ROTATION=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRotation() {
/* 339 */     return "SELECT ROTATION FROM FROZENITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String savePos() {
/* 345 */     return "UPDATE FROZENITEMS SET POSX=?,POSY=?,POSZ=?,ROTATION=?,ONBRIDGE=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String clearItem() {
/* 351 */     return "UPDATE FROZENITEMS SET NAME=?,DESCRIPTION=?,QUALITYLEVEL=?,ORIGINALQUALITYLEVEL=?,LASTMAINTAINED=?,ENCHANT=?,BANKED=?,SIZEX=?,SIZEY=?,SIZEZ=?,ZONEID=?,DAMAGE=?,PARENTID=?, ROTATION=?,WEIGHT=?,POSX=?,POSY=?,POSZ=?,CREATOR=?,AUXDATA=?,COLOR=?,COLOR2=?,TEMPERATURE=?,CREATIONDATE=?,CREATIONSTATE=0,MATERIAL=?, BLESS=?, MAILED=0, MAILTIMES=0,RARITY=?, OWNERID=-10, LASTOWNERID=-10 WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setDamage() {
/* 357 */     return "INSERT INTO FROZENITEMS (DAMAGE, LASTMAINTAINED, WURMID) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE LASTMAINTAINED=VALUES(LASTMAINTAINED)";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setDamageOld() {
/* 363 */     return "UPDATE FROZENITEMS SET DAMAGE=?,LASTMAINTAINED=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDamage() {
/* 369 */     return "SELECT DAMAGE FROM FROZENITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setLocked() {
/* 375 */     return "UPDATE LOCKS SET LOCKED=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLocked() {
/* 381 */     return "SELECT LOCKED FROM LOCKS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setTransferred() {
/* 387 */     return "UPDATE FROZENITEMS SET TRANSFERRED=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAllItems() {
/* 393 */     return "SELECT * from FROZENITEMS where PARENTID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getItem() {
/* 399 */     return "SELECT * from FROZENITEMS where WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String setBless() {
/* 409 */     return "UPDATE FROZENITEMS SET BLESS=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setSizeX() {
/* 415 */     return "UPDATE FROZENITEMS SET SIZEX=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSizeX() {
/* 421 */     return "SELECT SIZEX FROM FROZENITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setSizeY() {
/* 427 */     return "UPDATE FROZENITEMS SET SIZEY=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSizeY() {
/* 433 */     return "SELECT SIZEY FROM FROZENITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setSizeZ() {
/* 439 */     return "UPDATE FROZENITEMS SET SIZEZ=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSizeZ() {
/* 445 */     return "SELECT SIZEZ FROM FROZENITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setLockId() {
/* 451 */     return "UPDATE FROZENITEMS SET LOCKID=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setPrice() {
/* 457 */     return "UPDATE FROZENITEMS SET PRICE=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setAuxData() {
/* 463 */     return "UPDATE FROZENITEMS SET AUXDATA=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setCreationState() {
/* 469 */     return "UPDATE FROZENITEMS SET CREATIONSTATE=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setRealTemplate() {
/* 475 */     return "UPDATE FROZENITEMS SET REALTEMPLATE=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setColor() {
/* 481 */     return "UPDATE FROZENITEMS SET COLOR=?,COLOR2=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setEnchant() {
/* 487 */     return "UPDATE FROZENITEMS SET ENCHANT=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setBanked() {
/* 493 */     return "UPDATE FROZENITEMS SET BANKED=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getData() {
/* 499 */     return "select * from ITEMDATA where WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String createData() {
/* 505 */     return "insert into ITEMDATA ( DATA1, DATA2, WURMID) values(?,?,?)";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String updateData1() {
/* 511 */     return "update ITEMDATA set DATA1=? where WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String updateData2() {
/* 517 */     return "update ITEMDATA set DATA2=? where WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String updateExtra1() {
/* 523 */     return "update ITEMDATA set EXTRA1=? where WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String updateExtra2() {
/* 529 */     return "update ITEMDATA set EXTRA2=? where WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String updateAllData() {
/* 535 */     return "update ITEMDATA set DATA1=?, DATA2=?, EXTRA1=?, EXTRA2=? where WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setTemperature() {
/* 541 */     return "UPDATE FROZENITEMS SET TEMPERATURE=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTemperature() {
/* 547 */     return "SELECT TEMPERATURE FROM FROZENITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setMaterial() {
/* 553 */     return "UPDATE FROZENITEMS SET MATERIAL=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setWornAsArmour() {
/* 559 */     return "UPDATE FROZENITEMS SET WORNARMOUR=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setFemale() {
/* 565 */     return "UPDATE FROZENITEMS SET FEMALE=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setMailed() {
/* 571 */     return "UPDATE FROZENITEMS SET MAILED=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setCreator() {
/* 577 */     return "UPDATE FROZENITEMS SET CREATOR=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getZoneItems() {
/* 583 */     return "SELECT * FROM FROZENITEMS WHERE OWNERID=-10";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCreatureItems() {
/* 589 */     return "SELECT * FROM FROZENITEMS WHERE OWNERID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPreloadedItems() {
/* 595 */     return "SELECT * FROM FROZENITEMS WHERE TEMPLATEID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCreatureItemsNonTransferred() {
/* 601 */     return "SELECT WURMID FROM FROZENITEMS WHERE OWNERID=? AND TRANSFERRED=0";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String updateLastMaintainedBankItem() {
/* 607 */     return "UPDATE FROZENITEMS SET LASTMAINTAINED=? WHERE BANKED=1";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getItemWeights() {
/* 613 */     return "SELECT WURMID, WEIGHT,SIZEX,SIZEY,SIZEZ, TEMPLATEID FROM FROZENITEMS";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOwnedItems() {
/* 619 */     return "SELECT OWNERID FROM FROZENITEMS WHERE OWNERID>0 GROUP BY OWNERID";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String deleteByOwnerId() {
/* 625 */     return "UPDATE FROZENITEMS SET OWNERID=-10 WHERE OWNERID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String deleteTransferedItem() {
/* 631 */     return "DELETE FROM FROZENITEMS WHERE WURMID=? AND TRANSFERRED=0";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String deleteItem() {
/* 637 */     return "delete from FROZENITEMS where WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRecycledItems() {
/* 643 */     return "SELECT * FROM FROZENITEMS WHERE TEMPLATEID=? AND BANKED=1";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getItemsForZone() {
/* 649 */     return "Select WURMID from FROZENITEMS where ZONEID=? AND BANKED=0";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setHidden() {
/* 655 */     return "UPDATE FROZENITEMS SET HIDDEN=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setSettings() {
/* 661 */     return "UPDATE FROZENITEMS SET SETTINGS=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String setMailTimes() {
/* 667 */     return "UPDATE FROZENITEMS SET MAILTIMES=? WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String freeze() {
/* 673 */     return "INSERT INTO FROZENITEMS SELECT * FROM ITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String thaw() {
/* 679 */     return "INSERT INTO ITEMS SELECT * FROM FROZENITEMS WHERE WURMID=?";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FrozenItemDbStrings getInstance() {
/* 690 */     if (instance == null)
/* 691 */       instance = new FrozenItemDbStrings(); 
/* 692 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getDbStringsType() {
/* 698 */     return "FrozenItemDbStrings";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\FrozenItemDbStrings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */