package com.wurmonline.server.webinterface;

import com.wurmonline.server.players.Ban;
import com.wurmonline.shared.exceptions.WurmServerException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface WebInterface extends Remote {
  public static final int DEFAULT_RMI_PORT = 7220;
  
  public static final int DEFAULT_REGISTRATION_PORT = 7221;
  
  int getPower(String paramString, long paramLong) throws RemoteException;
  
  boolean isRunning(String paramString) throws RemoteException;
  
  int getPlayerCount(String paramString) throws RemoteException;
  
  int getPremiumPlayerCount(String paramString) throws RemoteException;
  
  String getTestMessage(String paramString) throws RemoteException;
  
  void broadcastMessage(String paramString1, String paramString2) throws RemoteException;
  
  long getAccountStatusForPlayer(String paramString1, String paramString2) throws RemoteException;
  
  long chargeMoney(String paramString1, String paramString2, long paramLong) throws RemoteException;
  
  String getServerStatus(String paramString) throws RemoteException;
  
  Map<String, Integer> getBattleRanks(String paramString, int paramInt) throws RemoteException;
  
  Map<String, Long> getFriends(String paramString, long paramLong) throws RemoteException;
  
  Map<String, String> getInventory(String paramString, long paramLong) throws RemoteException;
  
  Map<Long, Long> getBodyItems(String paramString, long paramLong) throws RemoteException;
  
  Map<String, Float> getSkills(String paramString, long paramLong) throws RemoteException;
  
  Map<String, ?> getPlayerSummary(String paramString, long paramLong) throws RemoteException;
  
  long getLocalCreationTime(String paramString) throws RemoteException;
  
  String ban(String paramString1, String paramString2, String paramString3, int paramInt) throws RemoteException;
  
  String pardonban(String paramString1, String paramString2) throws RemoteException;
  
  String addBannedIp(String paramString1, String paramString2, String paramString3, int paramInt) throws RemoteException;
  
  Ban[] getPlayersBanned(String paramString) throws RemoteException;
  
  Ban[] getIpsBanned(String paramString) throws RemoteException;
  
  String removeBannedIp(String paramString1, String paramString2) throws RemoteException;
  
  Map<Integer, String> getKingdoms(String paramString) throws RemoteException;
  
  Map<Long, String> getPlayersForKingdom(String paramString, int paramInt) throws RemoteException;
  
  long getPlayerId(String paramString1, String paramString2) throws RemoteException;
  
  Map<String, ?> createPlayer(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, byte paramByte1, byte paramByte2, long paramLong, byte paramByte3) throws RemoteException;
  
  Map<String, String> createPlayerPhaseOne(String paramString1, String paramString2, String paramString3) throws RemoteException;
  
  Map<String, ?> createPlayerPhaseTwo(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, byte paramByte1, byte paramByte2, long paramLong, byte paramByte3, String paramString7) throws RemoteException;
  
  Map<String, ?> createPlayerPhaseTwo(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, byte paramByte1, byte paramByte2, long paramLong, byte paramByte3, String paramString7, int paramInt) throws RemoteException;
  
  Map<String, ?> createPlayerPhaseTwo(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, byte paramByte1, byte paramByte2, long paramLong, byte paramByte3, String paramString7, int paramInt, boolean paramBoolean) throws RemoteException;
  
  byte[] createAndReturnPlayer(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, byte paramByte1, byte paramByte2, long paramLong, byte paramByte3, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) throws RemoteException;
  
  Map<String, String> addMoneyToBank(String paramString1, String paramString2, long paramLong, String paramString3) throws RemoteException;
  
  long getMoney(String paramString1, long paramLong, String paramString2) throws RemoteException;
  
  Map<String, String> reversePayment(String paramString1, long paramLong, int paramInt1, int paramInt2, String paramString2, String paramString3, String paramString4) throws RemoteException;
  
  Map<String, String> addMoneyToBank(String paramString1, String paramString2, long paramLong, String paramString3, boolean paramBoolean) throws RemoteException;
  
  Map<String, String> addMoneyToBank(String paramString1, String paramString2, long paramLong1, long paramLong2, String paramString3, boolean paramBoolean) throws RemoteException;
  
  Map<String, String> addPlayingTime(String paramString1, String paramString2, int paramInt1, int paramInt2, String paramString3, boolean paramBoolean) throws RemoteException;
  
  Map<String, String> addPlayingTime(String paramString1, String paramString2, int paramInt1, int paramInt2, String paramString3) throws RemoteException;
  
  Map<Integer, String> getDeeds(String paramString) throws RemoteException;
  
  Map<String, ?> getDeedSummary(String paramString, int paramInt) throws RemoteException;
  
  Map<String, Long> getPlayersForDeed(String paramString, int paramInt) throws RemoteException;
  
  Map<String, Integer> getAlliesForDeed(String paramString, int paramInt) throws RemoteException;
  
  String[] getHistoryForDeed(String paramString, int paramInt1, int paramInt2) throws RemoteException;
  
  String[] getAreaHistory(String paramString, int paramInt) throws RemoteException;
  
  Map<String, ?> getItemSummary(String paramString, long paramLong) throws RemoteException;
  
  Map<String, String> getPlayerIPAddresses(String paramString) throws RemoteException;
  
  Map<String, String> getNameBans(String paramString) throws RemoteException;
  
  Map<String, String> getIPBans(String paramString) throws RemoteException;
  
  Map<String, String> getWarnings(String paramString) throws RemoteException;
  
  String getWurmTime(String paramString) throws RemoteException;
  
  String getUptime(String paramString) throws RemoteException;
  
  String getNews(String paramString) throws RemoteException;
  
  String getGameInfo(String paramString) throws RemoteException;
  
  Map<String, String> getKingdomInfluence(String paramString) throws RemoteException;
  
  Map<String, ?> getMerchantSummary(String paramString, long paramLong) throws RemoteException;
  
  Map<String, ?> getBankAccount(String paramString, long paramLong) throws RemoteException;
  
  Map<String, ?> authenticateUser(String paramString1, String paramString2, String paramString3, String paramString4, Map paramMap) throws RemoteException;
  
  Map<String, ?> authenticateUser(String paramString1, String paramString2, String paramString3, String paramString4) throws RemoteException;
  
  Map<String, String> changePassword(String paramString1, String paramString2, String paramString3, String paramString4) throws RemoteException;
  
  Map<String, String> changePassword(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5) throws RemoteException;
  
  boolean changePassword(String paramString1, long paramLong, String paramString2) throws RemoteException;
  
  Map<String, String> changeEmail(String paramString1, String paramString2, String paramString3, String paramString4) throws RemoteException;
  
  String getChallengePhrase(String paramString1, String paramString2) throws RemoteException;
  
  String[] getPlayerNamesForEmail(String paramString1, String paramString2) throws RemoteException;
  
  String getEmailAddress(String paramString1, String paramString2) throws RemoteException;
  
  Map<String, String> requestPasswordReset(String paramString1, String paramString2, String paramString3) throws RemoteException;
  
  Map<Integer, String> getAllServers(String paramString) throws RemoteException;
  
  Map<Integer, String> getAllServerInternalAddresses(String paramString) throws RemoteException;
  
  boolean sendMail(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5) throws RemoteException;
  
  Map<String, String> getPendingAccounts(String paramString) throws RemoteException;
  
  void shutDown(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt) throws RemoteException;
  
  Map<String, Byte> getReferrers(String paramString, long paramLong) throws RemoteException;
  
  String addReferrer(String paramString1, String paramString2, long paramLong) throws RemoteException;
  
  String acceptReferrer(String paramString1, long paramLong, String paramString2, boolean paramBoolean) throws RemoteException;
  
  Map<String, Double> getSkillStats(String paramString, int paramInt) throws RemoteException;
  
  Map<Integer, String> getSkills(String paramString) throws RemoteException;
  
  Map<String, ?> getStructureSummary(String paramString, long paramLong) throws RemoteException;
  
  long getStructureIdFromWrit(String paramString, long paramLong) throws RemoteException;
  
  Map<String, ?> getTileSummary(String paramString, int paramInt1, int paramInt2, boolean paramBoolean) throws RemoteException;
  
  String getReimbursementInfo(String paramString1, String paramString2) throws RemoteException;
  
  boolean withDraw(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3) throws RemoteException;
  
  boolean transferPlayer(String paramString1, String paramString2, int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3, byte[] paramArrayOfbyte) throws RemoteException;
  
  boolean setCurrentServer(String paramString1, String paramString2, int paramInt) throws RemoteException;
  
  boolean addDraggedItem(String paramString, long paramLong1, byte[] paramArrayOfbyte, long paramLong2, int paramInt1, int paramInt2) throws RemoteException;
  
  String rename(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt) throws RemoteException;
  
  String changePassword(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt) throws RemoteException;
  
  String changeEmail(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, int paramInt, String paramString6, String paramString7) throws RemoteException;
  
  String addReimb(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) throws RemoteException;
  
  long[] getCurrentServerAndWurmid(String paramString1, String paramString2, long paramLong) throws RemoteException;
  
  Map<Long, byte[]> getPlayerStates(String paramString, long[] paramArrayOflong) throws RemoteException, WurmServerException;
  
  void manageFeature(String paramString, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) throws RemoteException;
  
  void startShutdown(String paramString1, String paramString2, int paramInt, String paramString3) throws RemoteException;
  
  String sendMail(String paramString, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, long paramLong1, long paramLong2, int paramInt) throws RemoteException;
  
  String setPlayerPremiumTime(String paramString1, long paramLong1, long paramLong2, int paramInt1, int paramInt2, String paramString2) throws RemoteException;
  
  String setPlayerMoney(String paramString1, long paramLong1, long paramLong2, long paramLong3, String paramString2) throws RemoteException;
  
  Map<String, String> doesPlayerExist(String paramString1, String paramString2) throws RemoteException;
  
  void setWeather(String paramString, float paramFloat1, float paramFloat2, float paramFloat3) throws RemoteException;
  
  String sendVehicle(String paramString, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat) throws RemoteException;
  
  void requestDemigod(String paramString1, byte paramByte, String paramString2) throws RemoteException;
  
  String ascend(String paramString1, int paramInt, String paramString2, long paramLong, byte paramByte1, byte paramByte2, byte paramByte3, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7) throws RemoteException;
  
  boolean requestDeityMove(String paramString1, int paramInt1, int paramInt2, String paramString2) throws RemoteException;
  
  void setKingdomInfo(String paramString1, int paramInt, byte paramByte1, byte paramByte2, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, boolean paramBoolean) throws RemoteException;
  
  boolean kingdomExists(String paramString, int paramInt, byte paramByte, boolean paramBoolean) throws RemoteException;
  
  void genericWebCommand(String paramString, short paramShort, long paramLong, byte[] paramArrayOfbyte) throws RemoteException;
  
  int[] getPremTimeSilvers(String paramString, long paramLong) throws RemoteException;
  
  void awardPlayer(String paramString1, long paramLong, String paramString2, int paramInt1, int paramInt2) throws RemoteException;
  
  boolean isFeatureEnabled(String paramString, int paramInt) throws RemoteException;
  
  boolean setPlayerFlag(String paramString, long paramLong, int paramInt, boolean paramBoolean) throws RemoteException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WebInterface.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */