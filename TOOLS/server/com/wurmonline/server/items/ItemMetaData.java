/*     */ package com.wurmonline.server.items;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.io.IOException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.logging.Level;
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
/*     */ public final class ItemMetaData
/*     */   implements MiscConstants
/*     */ {
/*  40 */   private static final Logger logger = Logger.getLogger(ItemMetaData.class.getName());
/*     */   
/*  42 */   private static final String INSERT_ITEMKEYS = DbConnector.isUseSqlite() ? "INSERT OR IGNORE INTO ITEMKEYS (LOCKID,KEYID)VALUES(?,?)" : "INSERT IGNORE INTO ITEMKEYS (LOCKID,KEYID)VALUES(?,?)";
/*     */   
/*     */   public final long itemId;
/*     */   
/*     */   public final boolean locked;
/*     */   
/*     */   public final long lockid;
/*     */   
/*     */   public final long[] keys;
/*     */   
/*     */   public final long lastowner;
/*     */   
/*     */   public final int data1;
/*     */   
/*     */   public final int data2;
/*     */   
/*     */   public final int extra1;
/*     */   
/*     */   public final int extra2;
/*     */   
/*     */   public final String itname;
/*     */   
/*     */   public final String desc;
/*     */   
/*     */   public final long ownerId;
/*     */   
/*     */   public final long parentId;
/*     */   
/*     */   public final long lastmaintained;
/*     */   public final float ql;
/*     */   public final float itemdam;
/*     */   public final float origQl;
/*     */   public final int itemtemplateId;
/*     */   public final int weight;
/*     */   public final int sizex;
/*     */   public final int sizey;
/*     */   public final int sizez;
/*     */   public final byte bless;
/*     */   public final byte enchantment;
/*     */   public final byte material;
/*     */   public final int price;
/*     */   public final short temp;
/*     */   public final boolean banked;
/*     */   public final byte auxbyte;
/*     */   public final long creationDate;
/*     */   public final byte creationState;
/*     */   public final int realTemplate;
/*     */   public final boolean wornAsArmour;
/*     */   public final int color;
/*     */   public final int color2;
/*     */   public final short place;
/*     */   public final float posx;
/*     */   public final float posy;
/*     */   public final float posz;
/*     */   public final String creator;
/*     */   public DbStrings instance;
/*     */   public final boolean female;
/*     */   public final boolean mailed;
/*     */   public final byte mailTimes;
/*     */   public final byte rarity;
/*     */   public final long onBridge;
/*     */   public final int settings;
/*     */   public final boolean hasInscription;
/*     */   
/*     */   public ItemMetaData(boolean aLocked, long aLockId, long aItemId, long[] aKeyIds, long aLastOwner, int aData1, int aData2, int aExtra1, int aExtra2, String aName, String aDescription, long aOwnerId, long aParentId, long aLastMaintained, float aQualityLevel, float aDamage, float aOriginalQualityLevel, int aTemplateId, int aWeight, int aSizeX, int aSizeY, int aSizeZ, int aBless, byte aEnchantment, byte aMaterial, int aPrice, short aTemp, boolean aBanked, byte aAuxData, long aCreated, byte aCreationState, int aRealTemplate, boolean aWornArmour, int aColor, int aColor2, short aPlace, float aPosX, float aPosY, float aPosZ, String aCreator, boolean aFemale, boolean aMailed, byte mailedTimes, byte rarebyte, long bridgeId, boolean inscriptionFlag, int aSettings, boolean frozen) {
/* 107 */     this.itemId = aItemId;
/* 108 */     this.locked = (aLocked || aLockId != -10L);
/* 109 */     this.lockid = aLockId;
/* 110 */     this.keys = aKeyIds;
/* 111 */     this.lastowner = aLastOwner;
/* 112 */     this.data1 = aData1;
/* 113 */     this.data2 = aData2;
/* 114 */     this.extra1 = aExtra1;
/* 115 */     this.extra2 = aExtra2;
/* 116 */     this.desc = aDescription.substring(0, Math.min(99, aDescription.length()));
/* 117 */     this.ownerId = aOwnerId;
/* 118 */     this.parentId = aParentId;
/* 119 */     this.lastmaintained = aLastMaintained;
/* 120 */     this.ql = aQualityLevel;
/* 121 */     this.itemdam = aDamage;
/* 122 */     this.origQl = aOriginalQualityLevel;
/* 123 */     this.itemtemplateId = aTemplateId;
/* 124 */     this.instance = Item.getDbStrings(this.itemtemplateId);
/* 125 */     if (frozen && this.instance == ItemDbStrings.getInstance())
/* 126 */       this.instance = FrozenItemDbStrings.getInstance(); 
/* 127 */     this.weight = aWeight;
/* 128 */     this.sizex = aSizeX;
/* 129 */     this.sizey = aSizeY;
/* 130 */     this.sizez = aSizeZ;
/* 131 */     this.bless = (byte)aBless;
/* 132 */     this.enchantment = aEnchantment;
/* 133 */     this.material = aMaterial;
/* 134 */     this.price = aPrice;
/* 135 */     this.temp = aTemp;
/* 136 */     this.banked = aBanked;
/* 137 */     this.auxbyte = aAuxData;
/* 138 */     this.rarity = rarebyte;
/* 139 */     this.onBridge = bridgeId;
/* 140 */     this.settings = aSettings;
/* 141 */     this.hasInscription = inscriptionFlag;
/* 142 */     this.creationDate = aCreated;
/* 143 */     this.creationState = aCreationState;
/* 144 */     this.realTemplate = aRealTemplate;
/* 145 */     this.wornAsArmour = aWornArmour;
/* 146 */     this.color = aColor;
/* 147 */     this.color2 = aColor2;
/* 148 */     this.place = aPlace;
/* 149 */     this.posx = aPosX;
/* 150 */     this.posy = aPosY;
/* 151 */     this.posz = aPosZ;
/* 152 */     this.creator = aCreator;
/* 153 */     this.female = aFemale;
/* 154 */     this.mailed = aMailed;
/* 155 */     this.mailTimes = mailedTimes;
/*     */     
/* 157 */     String itsName = aName.substring(0, Math.min(39, aName.length()));
/*     */ 
/*     */     
/*     */     try {
/* 161 */       ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(aTemplateId);
/*     */       
/* 163 */       if (template.isVehicle()) {
/* 164 */         itsName = ItemFactory.generateName(template, this.material);
/*     */       }
/* 166 */     } catch (NoSuchTemplateException noSuchTemplateException) {}
/*     */ 
/*     */ 
/*     */     
/* 170 */     this.itname = itsName;
/*     */   }
/*     */ 
/*     */   
/*     */   public void save() throws IOException {
/* 175 */     Connection dbcon = null;
/* 176 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 179 */       dbcon = DbConnector.getItemDbCon();
/* 180 */       ps = dbcon.prepareStatement(this.instance.transferItem());
/* 181 */       ps.setLong(1, this.itemId);
/* 182 */       ps.setInt(2, this.itemtemplateId);
/* 183 */       ps.setString(3, this.itname);
/* 184 */       ps.setFloat(4, this.ql);
/* 185 */       ps.setFloat(5, this.origQl);
/* 186 */       ps.setLong(6, this.lastmaintained);
/* 187 */       ps.setLong(7, this.ownerId);
/* 188 */       ps.setInt(8, this.sizex);
/* 189 */       ps.setInt(9, this.sizey);
/* 190 */       ps.setInt(10, this.sizez);
/* 191 */       ps.setInt(11, -10);
/* 192 */       ps.setFloat(12, this.itemdam);
/* 193 */       ps.setFloat(13, 1.0F);
/* 194 */       ps.setLong(14, this.parentId);
/* 195 */       ps.setInt(15, this.weight);
/* 196 */       ps.setByte(16, this.material);
/* 197 */       ps.setLong(17, this.lockid);
/* 198 */       ps.setString(18, this.desc);
/* 199 */       ps.setByte(19, this.bless);
/* 200 */       ps.setByte(20, this.enchantment);
/* 201 */       ps.setShort(21, this.temp);
/* 202 */       ps.setInt(22, this.price);
/* 203 */       ps.setBoolean(23, this.banked);
/* 204 */       ps.setByte(24, this.auxbyte);
/* 205 */       ps.setLong(25, this.creationDate);
/* 206 */       ps.setByte(26, this.creationState);
/* 207 */       ps.setInt(27, this.realTemplate);
/* 208 */       ps.setBoolean(28, this.wornAsArmour);
/* 209 */       ps.setInt(29, this.color);
/* 210 */       ps.setInt(30, this.color2);
/* 211 */       ps.setShort(31, this.place);
/* 212 */       ps.setFloat(32, this.posx);
/* 213 */       ps.setFloat(33, this.posy);
/* 214 */       ps.setFloat(34, this.posz);
/* 215 */       ps.setString(35, this.creator);
/* 216 */       ps.setBoolean(36, this.female);
/* 217 */       ps.setBoolean(37, this.mailed);
/* 218 */       ps.setByte(38, this.mailTimes);
/* 219 */       ps.setByte(39, this.rarity);
/* 220 */       ps.setLong(40, this.onBridge);
/* 221 */       ps.setLong(41, this.lastowner);
/* 222 */       ps.setInt(42, this.settings);
/* 223 */       ps.executeUpdate();
/* 224 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 225 */       if (this.keys != null && this.keys.length > 0)
/* 226 */         saveKeys(this.locked); 
/* 227 */       if (Servers.isThisATestServer()) {
/* 228 */         logger.log(Level.INFO, "Saving " + this.itname + ", " + this.itemId);
/*     */       }
/* 230 */     } catch (SQLException sqex) {
/*     */       
/* 232 */       throw new IOException(this.itemId + " " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 236 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 237 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void saveKeys(boolean aLocked) throws IOException {
/* 243 */     Connection dbcon = null;
/* 244 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 247 */       dbcon = DbConnector.getItemDbCon();
/*     */       
/* 249 */       if (!lockExists(dbcon)) {
/*     */         
/* 251 */         String string = this.instance.createLock();
/* 252 */         ps = dbcon.prepareStatement(string);
/*     */         
/* 254 */         ps.setLong(1, this.itemId);
/* 255 */         ps.setBoolean(2, aLocked);
/* 256 */         ps.executeUpdate();
/* 257 */         DbUtilities.closeDatabaseObjects(ps, null);
/*     */       } 
/*     */       
/* 260 */       ps = dbcon.prepareStatement(this.instance.setLocked());
/*     */       
/* 262 */       ps.setBoolean(1, aLocked);
/* 263 */       ps.setLong(2, this.itemId);
/* 264 */       ps.executeUpdate();
/* 265 */       DbUtilities.closeDatabaseObjects(ps, null);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 270 */       DbUtilities.closeDatabaseObjects(ps, null);
/*     */       
/* 272 */       for (int x = 0; x < this.keys.length; x++) {
/*     */         
/*     */         try
/*     */         {
/*     */           
/* 277 */           ps = dbcon.prepareStatement(INSERT_ITEMKEYS);
/* 278 */           ps.setLong(1, this.itemId);
/* 279 */           ps.setLong(2, this.keys[x]);
/* 280 */           ps.executeUpdate();
/* 281 */           DbUtilities.closeDatabaseObjects(ps, null);
/*     */         }
/* 283 */         catch (SQLException ex)
/*     */         {
/* 285 */           logger.log(Level.INFO, "Failed to insert key id " + this.keys[x] + ": " + ex.getMessage(), ex);
/*     */         }
/*     */       
/*     */       } 
/* 289 */     } catch (SQLException ex) {
/*     */       
/* 291 */       throw new IOException(this.itemId + " " + ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 295 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 296 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean lockExists(Connection dbcon) {
/* 302 */     PreparedStatement ps = null;
/* 303 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 306 */       ps = dbcon.prepareStatement(this.instance.getLock());
/* 307 */       ps.setLong(1, this.itemId);
/* 308 */       rs = ps.executeQuery();
/* 309 */       return rs.next();
/*     */     }
/* 311 */     catch (SQLException ex) {
/*     */       
/* 313 */       logger.log(Level.WARNING, "Failed to check if lock exists:", ex);
/*     */     }
/*     */     finally {
/*     */       
/* 317 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*     */     } 
/* 319 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\ItemMetaData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */