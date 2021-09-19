/*     */ package com.wurmonline.server.items;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public final class ItemRequirement
/*     */ {
/*  37 */   private static final Logger logger = Logger.getLogger(ItemRequirement.class.getName());
/*     */   private static final String loadItemRequirements = "SELECT * FROM ITEMREQUIREMENTS";
/*     */   private static final String deleteItemRequirements = "DELETE FROM ITEMREQUIREMENTS WHERE WURMID=?";
/*     */   private static final String updateItemRequirements = "UPDATE ITEMREQUIREMENTS SET ITEMSDONE=? WHERE WURMID=? AND TEMPLATEID=?";
/*     */   private static final String createItemRequirements = "INSERT INTO ITEMREQUIREMENTS (ITEMSDONE, WURMID, TEMPLATEID) VALUES(?,?,?)";
/*     */   private final int templateId;
/*     */   private int numsDone;
/*  44 */   private static final Map<Long, Set<ItemRequirement>> requirements = new HashMap<>();
/*     */   
/*     */   private static boolean found = false;
/*     */   
/*     */   private ItemRequirement(int aItemTemplateId, int aNumbersDone) {
/*  49 */     this.templateId = aItemTemplateId;
/*  50 */     this.numsDone = aNumbersDone;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void loadAllItemRequirements() {
/*  55 */     Connection dbcon = null;
/*  56 */     PreparedStatement ps = null;
/*  57 */     ResultSet rs = null;
/*     */     
/*     */     try {
/*  60 */       dbcon = DbConnector.getItemDbCon();
/*  61 */       ps = dbcon.prepareStatement("SELECT * FROM ITEMREQUIREMENTS");
/*  62 */       rs = ps.executeQuery();
/*  63 */       while (rs.next())
/*     */       {
/*  65 */         setRequirements(rs.getLong("WURMID"), rs.getInt("TEMPLATEID"), rs.getInt("ITEMSDONE"), false, false);
/*     */       }
/*     */     }
/*  68 */     catch (SQLException ex) {
/*     */       
/*  70 */       logger.log(Level.WARNING, "Failed loading item reqs " + ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/*  74 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  75 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setRequirements(long _wurmid, int _templateId, int _numsDone, boolean save, boolean create) {
/*  82 */     found = false;
/*  83 */     Set<ItemRequirement> doneset = requirements.get(Long.valueOf(_wurmid));
/*  84 */     if (doneset == null) {
/*     */       
/*  86 */       doneset = new HashSet<>();
/*  87 */       requirements.put(Long.valueOf(_wurmid), doneset);
/*     */     } 
/*  89 */     for (ItemRequirement next : doneset) {
/*     */       
/*  91 */       if (next.templateId == _templateId) {
/*     */ 
/*     */         
/*  94 */         next.numsDone = _numsDone;
/*  95 */         found = true;
/*     */       } 
/*     */     } 
/*  98 */     if (!found) {
/*     */       
/* 100 */       ItemRequirement newreq = new ItemRequirement(_templateId, _numsDone);
/* 101 */       doneset.add(newreq);
/*     */     } 
/*     */     
/* 104 */     if (save) {
/* 105 */       updateDatabaseRequirements(_wurmid, _templateId, _numsDone, create);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getTemplateId() {
/* 111 */     return this.templateId;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getNumsDone() {
/* 116 */     return this.numsDone;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void deleteRequirements(long _wurmid) {
/* 121 */     requirements.remove(Long.valueOf(_wurmid));
/* 122 */     Connection dbcon = null;
/* 123 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 126 */       dbcon = DbConnector.getItemDbCon();
/* 127 */       ps = dbcon.prepareStatement("DELETE FROM ITEMREQUIREMENTS WHERE WURMID=?");
/* 128 */       ps.setLong(1, _wurmid);
/* 129 */       ps.executeUpdate();
/*     */     }
/* 131 */     catch (SQLException ex) {
/*     */       
/* 133 */       logger.log(Level.WARNING, "Failed to delete reqs " + _wurmid, ex);
/*     */     }
/*     */     finally {
/*     */       
/* 137 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 138 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Set<ItemRequirement> getRequirements(long wurmid) {
/* 144 */     return requirements.get(Long.valueOf(wurmid));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void updateDatabaseRequirements(long _wurmid, int _templateId, int numsDone, boolean create) {
/* 150 */     Connection dbcon = null;
/* 151 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 154 */       dbcon = DbConnector.getItemDbCon();
/* 155 */       if (numsDone == 1 || create) {
/* 156 */         ps = dbcon.prepareStatement("INSERT INTO ITEMREQUIREMENTS (ITEMSDONE, WURMID, TEMPLATEID) VALUES(?,?,?)");
/*     */       } else {
/* 158 */         ps = dbcon.prepareStatement("UPDATE ITEMREQUIREMENTS SET ITEMSDONE=? WHERE WURMID=? AND TEMPLATEID=?");
/*     */       } 
/* 160 */       ps.setInt(1, numsDone);
/* 161 */       ps.setLong(2, _wurmid);
/* 162 */       ps.setInt(3, _templateId);
/* 163 */       ps.executeUpdate();
/*     */     }
/* 165 */     catch (SQLException ex) {
/*     */       
/* 167 */       logger.log(Level.WARNING, "Failed to update reqs " + _wurmid + ",tid=" + _templateId + ", nums=" + numsDone, ex);
/*     */     }
/*     */     finally {
/*     */       
/* 171 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 172 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static int getStateForRequirement(int _templateId, long _wurmId) {
/* 178 */     Set<ItemRequirement> doneSet = requirements.get(Long.valueOf(_wurmId));
/* 179 */     if (doneSet != null)
/*     */     {
/* 181 */       for (ItemRequirement next : doneSet) {
/*     */         
/* 183 */         if (next.templateId == _templateId)
/*     */         {
/*     */ 
/*     */           
/* 187 */           return next.numsDone;
/*     */         }
/*     */       } 
/*     */     }
/* 191 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\ItemRequirement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */