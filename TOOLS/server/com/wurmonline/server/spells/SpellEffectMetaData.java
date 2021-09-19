/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.items.ItemSpellEffects;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.shared.constants.CounterTypes;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SpellEffectMetaData
/*     */   implements MiscConstants, CounterTypes
/*     */ {
/*     */   private static final String CREATE_EFFECT = "INSERT INTO SPELLEFFECTS (WURMID, OWNER,TYPE,POWER,TIMELEFT) VALUES(?,?,?,?,?)";
/*     */   private static final String CREATE_ITEM_EFFECT = "INSERT INTO SPELLEFFECTS (WURMID, ITEMID,TYPE,POWER,TIMELEFT) VALUES(?,?,?,?,?)";
/*  46 */   private static final Logger logger = Logger.getLogger(SpellEffectMetaData.class.getName());
/*     */   
/*     */   private final long id;
/*     */   
/*     */   private final float power;
/*     */   
/*     */   private final int timeleft;
/*     */   
/*     */   private final long owner;
/*     */   
/*     */   private final byte type;
/*     */   
/*     */   public SpellEffectMetaData(long aWurmid, long aOwner, byte aType, float aPower, int aTimeleft, boolean aAddToTables) {
/*  59 */     this.owner = aOwner;
/*  60 */     this.type = aType;
/*  61 */     this.power = aPower;
/*  62 */     this.timeleft = aTimeleft;
/*  63 */     this.id = aWurmid;
/*     */     
/*  65 */     if (aAddToTables)
/*     */     {
/*  67 */       if (WurmId.getType(aOwner) == 2 || WurmId.getType(aOwner) == 19 || 
/*  68 */         WurmId.getType(aOwner) == 20) {
/*     */         
/*  70 */         SpellEffect sp = new SpellEffect(aWurmid, aOwner, aType, aPower, aTimeleft, (byte)9, (byte)0);
/*     */         
/*  72 */         ItemSpellEffects eff = ItemSpellEffects.getSpellEffects(sp.owner);
/*  73 */         if (eff == null)
/*     */         {
/*  75 */           eff = new ItemSpellEffects(sp.owner);
/*     */         }
/*  77 */         eff.addSpellEffect(sp);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void save() {
/*  86 */     if (WurmId.getType(this.owner) == 0) {
/*     */       
/*  88 */       Connection dbcon = null;
/*  89 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/*  92 */         dbcon = DbConnector.getPlayerDbCon();
/*     */         
/*  94 */         ps = dbcon.prepareStatement("INSERT INTO SPELLEFFECTS (WURMID, OWNER,TYPE,POWER,TIMELEFT) VALUES(?,?,?,?,?)");
/*  95 */         ps.setLong(1, this.id);
/*  96 */         ps.setLong(2, this.owner);
/*  97 */         ps.setByte(3, this.type);
/*  98 */         ps.setFloat(4, this.power);
/*  99 */         ps.setInt(5, this.timeleft);
/* 100 */         ps.executeUpdate();
/*     */       }
/* 102 */       catch (SQLException sqex) {
/*     */         
/* 104 */         logger.log(Level.WARNING, sqex.getMessage(), sqex);
/*     */       }
/*     */       finally {
/*     */         
/* 108 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 109 */         DbConnector.returnConnection(dbcon);
/*     */       }
/*     */     
/* 112 */     } else if (WurmId.getType(this.owner) == 2) {
/*     */       
/* 114 */       Connection dbcon = null;
/* 115 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 118 */         dbcon = DbConnector.getItemDbCon();
/*     */         
/* 120 */         ps = dbcon.prepareStatement("INSERT INTO SPELLEFFECTS (WURMID, ITEMID,TYPE,POWER,TIMELEFT) VALUES(?,?,?,?,?)");
/* 121 */         ps.setLong(1, this.id);
/* 122 */         ps.setLong(2, this.owner);
/* 123 */         ps.setByte(3, this.type);
/* 124 */         ps.setFloat(4, this.power);
/* 125 */         ps.setInt(5, this.timeleft);
/* 126 */         ps.executeUpdate();
/*     */       }
/* 128 */       catch (SQLException sqex) {
/*     */         
/* 130 */         logger.log(Level.WARNING, sqex.getMessage(), sqex);
/*     */       }
/*     */       finally {
/*     */         
/* 134 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 135 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\SpellEffectMetaData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */