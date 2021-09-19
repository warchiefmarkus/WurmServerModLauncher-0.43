/*     */ package com.wurmonline.server.tutorial;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Timestamp;
/*     */ import java.text.SimpleDateFormat;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Missions
/*     */   implements MiscConstants
/*     */ {
/*  46 */   private static final Map<Integer, Mission> missions = new HashMap<>();
/*     */   
/*     */   private static final String LOADALLMISSIONS = "SELECT * FROM MISSIONS";
/*     */   
/*     */   private static final String DELETEMISSION = "DELETE FROM MISSIONS WHERE ID=?";
/*     */   
/*  52 */   private static Logger logger = Logger.getLogger(Missions.class.getName());
/*     */   
/*     */   public static final byte CREATOR_UNSET = 0;
/*     */   
/*     */   public static final byte CREATOR_GM = 1;
/*     */   
/*     */   public static final byte CREATOR_SYSTEM = 2;
/*     */   
/*     */   public static final byte CREATOR_PLAYER = 3;
/*     */   
/*     */   public static final int SHOW_ALL = 0;
/*     */   
/*     */   public static final int SHOW_WITH = 1;
/*     */   
/*     */   public static final int SHOW_NONE = 2;
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/*  71 */       loadAllMissions();
/*     */     }
/*  73 */     catch (Exception ex) {
/*     */       
/*  75 */       logger.log(Level.WARNING, "Problems loading all Missions", ex);
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
/*     */   public static int getNumMissions() {
/*  88 */     return missions.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public static Mission[] getAllMissions() {
/*  93 */     return (Mission[])missions.values().toArray((Object[])new Mission[missions.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Mission[] getFilteredMissions(Creature creature, int showTriggers, boolean incInactive, boolean dontListMine, boolean listMineOnly, long listForUser, String groupName, boolean onlyCurrent, long currentTargetId) {
/* 100 */     Set<Mission> missionSet = new HashSet<>();
/* 101 */     for (Mission mission : missions.values()) {
/*     */       
/* 103 */       boolean own = (mission.getOwnerId() == creature.getWurmId());
/* 104 */       boolean show = (creature.getPower() > 0 || own);
/* 105 */       boolean userMatch = (mission.getOwnerId() == listForUser);
/* 106 */       if (own) {
/*     */         
/* 108 */         if (dontListMine) {
/* 109 */           show = false;
/*     */         }
/* 111 */       } else if (listMineOnly) {
/*     */         
/* 113 */         show = false;
/* 114 */         if (listForUser != -10L && userMatch) {
/* 115 */           show = true;
/*     */         }
/* 117 */       } else if (listForUser != -10L) {
/*     */         
/* 119 */         show = false;
/* 120 */         if (userMatch)
/* 121 */           show = true; 
/*     */       } 
/* 123 */       if (show && showTriggers == 2 && mission.hasTriggers())
/* 124 */         show = false; 
/* 125 */       if (show && showTriggers == 1 && !mission.hasTriggers())
/* 126 */         show = false; 
/* 127 */       if (show && !incInactive && mission.isInactive())
/* 128 */         show = false; 
/* 129 */       if (show && mission.getCreatorType() == 2 && creature.getPower() < 2)
/* 130 */         show = false; 
/* 131 */       if (show && !groupName.isEmpty() && !mission.getGroupName().equals(groupName))
/* 132 */         show = false; 
/* 133 */       if (show && onlyCurrent && !mission.hasTargetOf(currentTargetId, creature))
/* 134 */         show = false; 
/* 135 */       if (show)
/*     */       {
/* 137 */         if (mission.getCreatorType() != 2 || mission
/* 138 */           .getCreatedDate() > System.currentTimeMillis() - 2419200000L)
/*     */         {
/* 140 */           missionSet.add(mission);
/*     */         }
/*     */       }
/*     */     } 
/* 144 */     return missionSet.<Mission>toArray(new Mission[missionSet.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addMission(Mission m) {
/* 149 */     missions.put(Integer.valueOf(m.getId()), m);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Mission getMissionWithId(int mid) {
/* 154 */     return missions.get(Integer.valueOf(mid));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Mission[] getMissionsWithTargetId(long tid, Creature performer) {
/* 159 */     MissionTrigger[] triggers = MissionTriggers.getAllTriggers();
/* 160 */     Set<Mission> toReturn = new HashSet<>();
/* 161 */     for (MissionTrigger mt : triggers) {
/*     */       
/* 163 */       if (mt.getTarget() == tid) {
/*     */         
/* 165 */         Mission m = getMissionWithId(mt.getMissionRequired());
/*     */         
/* 167 */         if (m != null)
/*     */         {
/* 169 */           if ((m.getCreatorType() != 2 || performer.getPower() >= 5) && (
/* 170 */             performer.getPower() > 0 || m.getMissionCreatorName().equals(performer.getName()))) {
/* 171 */             toReturn.add(m);
/*     */           }
/*     */         }
/*     */       } 
/*     */     } 
/* 176 */     return toReturn.<Mission>toArray(new Mission[toReturn.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void deleteMission(int misid) {
/* 187 */     removeMission(misid);
/* 188 */     Connection dbcon = null;
/* 189 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 192 */       dbcon = DbConnector.getPlayerDbCon();
/* 193 */       ps = dbcon.prepareStatement("DELETE FROM MISSIONS WHERE ID=?");
/* 194 */       ps.setInt(1, misid);
/* 195 */       ps.executeUpdate();
/*     */     }
/* 197 */     catch (SQLException sqx) {
/*     */       
/* 199 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 203 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 204 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void loadAllMissions() {
/* 210 */     Connection dbcon = null;
/* 211 */     PreparedStatement ps = null;
/* 212 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 215 */       dbcon = DbConnector.getPlayerDbCon();
/* 216 */       ps = dbcon.prepareStatement("SELECT * FROM MISSIONS");
/* 217 */       rs = ps.executeQuery();
/* 218 */       while (rs.next())
/*     */       {
/* 220 */         Timestamp st = new Timestamp(System.currentTimeMillis());
/*     */         
/*     */         try {
/* 223 */           String lastModified = rs.getString("LASTMODIFIEDDATE");
/* 224 */           if (lastModified != null) {
/* 225 */             st = new Timestamp((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(lastModified).getTime());
/*     */           }
/*     */         }
/* 228 */         catch (Exception ex) {
/*     */           
/* 230 */           logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 235 */         Mission m = new Mission(rs.getInt("ID"), rs.getString("NAME"), rs.getString("INSTRUCTION"), rs.getBoolean("INACTIVE"), rs.getString("CREATOR"), rs.getString("CREATEDDATE"), rs.getString("LASTMODIFIER"), st, rs.getInt("MAXTIMESECS"), rs.getBoolean("MAYBERESTARTED"));
/* 236 */         m.setCreatorType(rs.getByte("CREATORTYPE"));
/* 237 */         m.setOwnerId(rs.getLong("CREATORID"));
/* 238 */         m.setSecondChance(rs.getBoolean("SECONDCHANCE"));
/* 239 */         m.setFailOnDeath(rs.getBoolean("FAILONDEATH"));
/* 240 */         m.setGroupName(rs.getString("GROUP_NAME"));
/* 241 */         m.setIsHidden(rs.getBoolean("HIDDEN"));
/* 242 */         addMission(m);
/*     */       }
/*     */     
/* 245 */     } catch (SQLException sqx) {
/*     */       
/* 247 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 251 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 252 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void removeMission(int mid) {
/* 258 */     missions.remove(Integer.valueOf(mid));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\Missions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */