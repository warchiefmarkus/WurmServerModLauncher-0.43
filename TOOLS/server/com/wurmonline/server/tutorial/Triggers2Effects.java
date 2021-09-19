/*     */ package com.wurmonline.server.tutorial;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public class Triggers2Effects
/*     */ {
/*  42 */   private static Logger logger = Logger.getLogger(Triggers2Effects.class.getName());
/*     */   
/*     */   private static final String LOAD_ALL_LINKS = "SELECT * FROM TRIGGERS2EFFECTS";
/*     */   
/*     */   private static final String CREATE_LINK = "INSERT INTO TRIGGERS2EFFECTS (TRIGGERID, EFFECTID) VALUES(?,?)";
/*     */   private static final String DELETE_LINK = "DELETE FROM TRIGGERS2EFFECTS WHERE TRIGGERID=? AND EFFECTID=?";
/*     */   private static final String DELETE_TRIGGER = "DELETE FROM TRIGGERS2EFFECTS WHERE TRIGGERID=?";
/*     */   private static final String DELETE_EFFECT = "DELETE FROM TRIGGERS2EFFECTS WHERE EFFECTID=?";
/*  50 */   private static final Map<Integer, HashSet<Integer>> triggers2Effects = new ConcurrentHashMap<>();
/*  51 */   private static final Map<Integer, HashSet<Integer>> effects2Triggers = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/*  57 */       dbLoadAllTriggers2Effects();
/*     */     }
/*  59 */     catch (Exception ex) {
/*     */       
/*  61 */       logger.log(Level.WARNING, "Problems loading all Triggers 2 Effects", ex);
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
/*     */   public static TriggerEffect[] getEffectsForTrigger(int triggerId, boolean incInactive) {
/*  75 */     Set<TriggerEffect> effs = new HashSet<>();
/*  76 */     HashSet<Integer> effects = triggers2Effects.get(Integer.valueOf(triggerId));
/*  77 */     if (effects != null)
/*     */     {
/*  79 */       for (Integer effectId : effects) {
/*     */ 
/*     */         
/*  82 */         TriggerEffect eff = TriggerEffects.getTriggerEffect(effectId.intValue());
/*     */         
/*  84 */         if (eff != null)
/*     */         {
/*  86 */           if (incInactive || (!incInactive && !eff.isInactive()))
/*  87 */             effs.add(eff); 
/*     */         }
/*     */       } 
/*     */     }
/*  91 */     return effs.<TriggerEffect>toArray(new TriggerEffect[effs.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static MissionTrigger[] getTriggersForEffect(int effectId, boolean incInactive) {
/*  96 */     Set<MissionTrigger> trgs = new HashSet<>();
/*  97 */     HashSet<Integer> triggers = effects2Triggers.get(Integer.valueOf(effectId));
/*  98 */     if (triggers != null)
/*     */     {
/* 100 */       for (Integer triggerId : triggers) {
/*     */ 
/*     */         
/* 103 */         MissionTrigger trg = MissionTriggers.getTriggerWithId(triggerId.intValue());
/*     */         
/* 105 */         if (trg != null)
/*     */         {
/* 107 */           if (incInactive || (!incInactive && !trg.isInactive()))
/* 108 */             trgs.add(trg); 
/*     */         }
/*     */       } 
/*     */     }
/* 112 */     return trgs.<MissionTrigger>toArray(new MissionTrigger[trgs.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean hasLink(int triggerId, int effectId) {
/* 117 */     HashSet<Integer> effects = triggers2Effects.get(Integer.valueOf(triggerId));
/* 118 */     if (effects != null)
/*     */     {
/* 120 */       return effects.contains(Integer.valueOf(effectId));
/*     */     }
/* 122 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean hasEffect(int triggerId) {
/* 127 */     HashSet<Integer> effects = triggers2Effects.get(Integer.valueOf(triggerId));
/* 128 */     if (effects != null)
/*     */     {
/*     */       
/* 131 */       return !effects.isEmpty();
/*     */     }
/* 133 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean hasTrigger(int effectId) {
/* 138 */     HashSet<Integer> triggers = effects2Triggers.get(Integer.valueOf(effectId));
/* 139 */     if (triggers != null)
/*     */     {
/*     */       
/* 142 */       return !triggers.isEmpty();
/*     */     }
/* 144 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addLink(int triggerId, int effectId, boolean loading) {
/* 149 */     if (triggerId <= 0 || effectId <= 0) {
/*     */       return;
/*     */     }
/* 152 */     HashSet<Integer> effects = triggers2Effects.get(Integer.valueOf(triggerId));
/*     */     
/* 154 */     if (effects == null)
/* 155 */       effects = new HashSet<>(); 
/* 156 */     boolean effAdded = effects.add(Integer.valueOf(effectId));
/*     */     
/* 158 */     if (!effects.isEmpty()) {
/* 159 */       triggers2Effects.put(Integer.valueOf(triggerId), effects);
/*     */     }
/* 161 */     HashSet<Integer> triggers = effects2Triggers.get(Integer.valueOf(effectId));
/*     */     
/* 163 */     if (triggers == null)
/* 164 */       triggers = new HashSet<>(); 
/* 165 */     boolean trgAdded = triggers.add(Integer.valueOf(triggerId));
/*     */     
/* 167 */     if (!triggers.isEmpty()) {
/* 168 */       effects2Triggers.put(Integer.valueOf(effectId), triggers);
/*     */     }
/* 170 */     if (!loading)
/*     */     {
/* 172 */       if (effAdded || trgAdded) {
/* 173 */         dbCreateLink(triggerId, effectId);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void deleteLink(int triggerId, int effectId) {
/* 179 */     HashSet<Integer> effects = triggers2Effects.remove(Integer.valueOf(triggerId));
/* 180 */     if (effects != null) {
/*     */ 
/*     */       
/* 183 */       effects.remove(Integer.valueOf(effectId));
/* 184 */       if (!effects.isEmpty())
/* 185 */         triggers2Effects.put(Integer.valueOf(triggerId), effects); 
/*     */     } 
/* 187 */     HashSet<Integer> triggers = effects2Triggers.remove(Integer.valueOf(effectId));
/* 188 */     if (triggers != null) {
/*     */       
/* 190 */       triggers.remove(Integer.valueOf(triggerId));
/* 191 */       if (!triggers.isEmpty())
/* 192 */         effects2Triggers.put(Integer.valueOf(effectId), triggers); 
/*     */     } 
/* 194 */     dbDeleteLink(triggerId, effectId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void deleteTrigger(int triggerId) {
/* 201 */     HashSet<Integer> effects = triggers2Effects.remove(Integer.valueOf(triggerId));
/* 202 */     if (effects != null)
/*     */     {
/*     */       
/* 205 */       for (Integer effectId : effects) {
/*     */ 
/*     */         
/* 208 */         HashSet<Integer> triggers = effects2Triggers.remove(effectId);
/* 209 */         if (triggers != null) {
/*     */ 
/*     */           
/* 212 */           triggers.remove(Integer.valueOf(triggerId));
/*     */           
/* 214 */           if (!triggers.isEmpty())
/* 215 */             effects2Triggers.put(Integer.valueOf(effectId.intValue()), triggers); 
/*     */         } 
/*     */       } 
/*     */     }
/* 219 */     dbDeleteTrigger(triggerId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void deleteEffect(int effectId) {
/* 226 */     HashSet<Integer> triggers = effects2Triggers.remove(Integer.valueOf(effectId));
/* 227 */     if (triggers != null)
/*     */     {
/*     */       
/* 230 */       for (Integer triggerId : triggers) {
/*     */ 
/*     */         
/* 233 */         HashSet<Integer> effects = effects2Triggers.remove(triggerId);
/* 234 */         if (effects != null) {
/*     */ 
/*     */           
/* 237 */           effects.remove(Integer.valueOf(effectId));
/*     */           
/* 239 */           if (!effects.isEmpty())
/* 240 */             effects2Triggers.put(Integer.valueOf(effectId), triggers); 
/*     */         } 
/*     */       } 
/*     */     }
/* 244 */     dbDeleteEffect(effectId);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void dbCreateLink(int triggerId, int effectId) {
/* 249 */     Connection dbcon = null;
/* 250 */     PreparedStatement ps = null;
/* 251 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 254 */       dbcon = DbConnector.getPlayerDbCon();
/* 255 */       ps = dbcon.prepareStatement("INSERT INTO TRIGGERS2EFFECTS (TRIGGERID, EFFECTID) VALUES(?,?)");
/* 256 */       ps.setInt(1, triggerId);
/* 257 */       ps.setInt(2, effectId);
/* 258 */       ps.executeUpdate();
/*     */     }
/* 260 */     catch (SQLException sqx) {
/*     */       
/* 262 */       logger.log(Level.WARNING, sqx.getMessage());
/*     */     }
/*     */     finally {
/*     */       
/* 266 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 267 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void dbDeleteLink(int triggerId, int effectId) {
/* 273 */     Connection dbcon = null;
/* 274 */     PreparedStatement ps = null;
/* 275 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 278 */       dbcon = DbConnector.getPlayerDbCon();
/* 279 */       ps = dbcon.prepareStatement("DELETE FROM TRIGGERS2EFFECTS WHERE TRIGGERID=? AND EFFECTID=?");
/* 280 */       ps.setInt(1, triggerId);
/* 281 */       ps.setInt(2, effectId);
/* 282 */       ps.executeUpdate();
/*     */     }
/* 284 */     catch (SQLException sqx) {
/*     */       
/* 286 */       logger.log(Level.WARNING, sqx.getMessage());
/*     */     }
/*     */     finally {
/*     */       
/* 290 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 291 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void dbDeleteTrigger(int triggerId) {
/* 297 */     Connection dbcon = null;
/* 298 */     PreparedStatement ps = null;
/* 299 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 302 */       dbcon = DbConnector.getPlayerDbCon();
/* 303 */       ps = dbcon.prepareStatement("DELETE FROM TRIGGERS2EFFECTS WHERE TRIGGERID=?");
/* 304 */       ps.setInt(1, triggerId);
/* 305 */       ps.executeUpdate();
/*     */     }
/* 307 */     catch (SQLException sqx) {
/*     */       
/* 309 */       logger.log(Level.WARNING, sqx.getMessage());
/*     */     }
/*     */     finally {
/*     */       
/* 313 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 314 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void dbDeleteEffect(int effectId) {
/* 320 */     Connection dbcon = null;
/* 321 */     PreparedStatement ps = null;
/* 322 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 325 */       dbcon = DbConnector.getPlayerDbCon();
/* 326 */       ps = dbcon.prepareStatement("DELETE FROM TRIGGERS2EFFECTS WHERE EFFECTID=?");
/* 327 */       ps.setInt(1, effectId);
/* 328 */       ps.executeUpdate();
/*     */     }
/* 330 */     catch (SQLException sqx) {
/*     */       
/* 332 */       logger.log(Level.WARNING, sqx.getMessage());
/*     */     }
/*     */     finally {
/*     */       
/* 336 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 337 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void dbLoadAllTriggers2Effects() {
/* 343 */     Connection dbcon = null;
/* 344 */     PreparedStatement ps = null;
/* 345 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 348 */       dbcon = DbConnector.getPlayerDbCon();
/* 349 */       ps = dbcon.prepareStatement("SELECT * FROM TRIGGERS2EFFECTS");
/* 350 */       rs = ps.executeQuery();
/* 351 */       while (rs.next())
/*     */       {
/* 353 */         int triggerId = rs.getInt("TRIGGERID");
/* 354 */         int effectId = rs.getInt("EFFECTID");
/* 355 */         addLink(triggerId, effectId, true);
/*     */       }
/*     */     
/* 358 */     } catch (SQLException sqx) {
/*     */       
/* 360 */       logger.log(Level.WARNING, sqx.getMessage());
/*     */     }
/*     */     finally {
/*     */       
/* 364 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 365 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\Triggers2Effects.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */