/*    */ package com.wurmonline.server;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Groups
/*    */ {
/* 33 */   private static final Map<String, Group> groups = new ConcurrentHashMap<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void addGroup(Group group) {
/* 44 */     groups.put(group.getName(), group);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void removeGroup(String name) {
/* 49 */     groups.remove(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void renameGroup(String oldName, String newName) {
/* 54 */     Group g = groups.remove(oldName);
/* 55 */     if (g != null) {
/*    */       
/* 57 */       g.setName(newName);
/* 58 */       groups.put(newName, g);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static Group getGroup(String name) throws NoSuchGroupException {
/* 64 */     Group toReturn = groups.get(name);
/* 65 */     if (toReturn == null)
/* 66 */       throw new NoSuchGroupException(name); 
/* 67 */     return toReturn;
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Team getTeamForOfflineMember(long wurmid) {
/* 72 */     for (Group g : groups.values()) {
/*    */       
/* 74 */       if (g.isTeam() && g.containsOfflineMember(wurmid))
/* 75 */         return (Team)g; 
/*    */     } 
/* 77 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\Groups.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */