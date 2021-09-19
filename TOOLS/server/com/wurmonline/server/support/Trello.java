/*      */ package com.wurmonline.server.support;
/*      */ 
/*      */ import com.wurmonline.server.Constants;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.VoteServer;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.net.URL;
/*      */ import java.net.URLEncoder;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import java.util.concurrent.ConcurrentLinkedDeque;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import java.util.zip.GZIPInputStream;
/*      */ import javax.net.ssl.HttpsURLConnection;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Trello
/*      */ {
/*   51 */   private static Logger logger = Logger.getLogger(Trello.class.getName());
/*      */   
/*   53 */   private static final TrelloThread trelloThread = new TrelloThread();
/*   54 */   private static final ConcurrentLinkedDeque<TrelloCard> trelloCardQueue = new ConcurrentLinkedDeque<>();
/*   55 */   private static int tickerCount = 0;
/*   56 */   private static final String[] trelloLists = new String[] { "None", "Waiting GM Calls", "Waiting ARCH or Dev action", "Resolved/Cancelled", "Watching", "Feedback" };
/*   57 */   private static String[] trelloListIds = new String[] { "", "", "", "", "", "" };
/*   58 */   private static String trelloFeedbackTemplateCardId = "";
/*      */   
/*      */   private static final String trelloMutes = "Mutes";
/*      */   private static final String trelloMutewarns = "Mutewarns";
/*      */   private static final String trelloVotings = "Voting";
/*      */   private static final String trelloHighways = "Highways";
/*      */   private static final String trelloDeaths = "Deaths";
/*   65 */   private static String trelloMuteIds = "";
/*   66 */   private static String trelloMutewarnIds = "";
/*   67 */   private static String trelloVotingIds = "";
/*   68 */   private static String trelloHighwaysIds = "";
/*   69 */   private static String trelloDeathsIds = "";
/*      */   
/*      */   private static boolean trelloMuteStorage = false;
/*      */   
/*      */   public static final byte LIST_NONE = 0;
/*      */   
/*      */   public static final byte LIST_WAITING_GM = 1;
/*      */   
/*      */   public static final byte LIST_WAITING_ARCH = 2;
/*      */   
/*      */   public static final byte LIST_CLOSED = 3;
/*      */   
/*      */   public static final byte LIST_WATCHING = 4;
/*      */   
/*      */   public static final byte LIST_FEEDBACK = 5;
/*      */   
/*      */   private static final String METHOD_GET = "GET";
/*      */   
/*      */   private static final String METHOD_POST = "POST";
/*      */   
/*      */   private static final String METHOD_PUT = "PUT";
/*      */   
/*      */   private static final String GZIP_ENCODING = "gzip";
/*      */   
/*      */   private static final String HTTP_CHARACTER_ENCODING = "UTF-8";
/*      */   
/*      */   public static final TrelloThread getTrelloThread() {
/*   96 */     return trelloThread;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class TrelloThread
/*      */     implements Runnable
/*      */   {
/*      */     public void run() {
/*  115 */       if (Trello.logger.isLoggable(Level.FINEST))
/*      */       {
/*  117 */         Trello.logger.finest("Running newSingleThreadScheduledExecutor for calling Tickets.ticker()");
/*      */       }
/*      */       
/*      */       try {
/*  121 */         long now = System.nanoTime();
/*      */ 
/*      */         
/*  124 */         if (Servers.isThisLoginServer() && Constants.trelloApiKey.length() > 0) {
/*      */ 
/*      */           
/*  127 */           Trello.updateTicketsInTrello();
/*      */           
/*  129 */           Trello.updateMuteVoteInTrello();
/*      */         } 
/*      */         
/*  132 */         float lElapsedTime = (float)(System.nanoTime() - now) / 1000000.0F;
/*  133 */         if (lElapsedTime > (float)Constants.lagThreshold)
/*      */         {
/*  135 */           Trello.logger.info("Finished calling Tickets.ticker(), which took " + lElapsedTime + " millis.");
/*      */         
/*      */         }
/*      */       }
/*  139 */       catch (RuntimeException e) {
/*      */         
/*  141 */         Trello.logger.log(Level.WARNING, "Caught exception in ScheduledExecutorService while calling Tickets.ticker()", e);
/*      */         
/*  143 */         throw e;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void addHighwayMessage(String server, String title, String description) {
/*  150 */     if (Constants.trelloMVBoardId.length() == 0) {
/*      */       return;
/*      */     }
/*      */     
/*  154 */     StringBuilder buf = new StringBuilder();
/*  155 */     buf.append(Tickets.convertTime(System.currentTimeMillis()));
/*  156 */     buf.append(" (");
/*  157 */     buf.append(server);
/*  158 */     buf.append(") ");
/*  159 */     buf.append(title);
/*  160 */     String tList = trelloHighwaysIds;
/*      */     
/*  162 */     TrelloCard tc = new TrelloCard(Constants.trelloMVBoardId, tList, buf.toString(), description, "");
/*  163 */     trelloCardQueue.add(tc);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void addImportantDeathsMessage(String server, String title, String description) {
/*  168 */     if (Constants.trelloMVBoardId.length() == 0) {
/*      */       return;
/*      */     }
/*      */     
/*  172 */     StringBuilder buf = new StringBuilder();
/*  173 */     buf.append(Tickets.convertTime(System.currentTimeMillis()));
/*  174 */     buf.append(" (");
/*  175 */     buf.append(server);
/*  176 */     buf.append(") ");
/*  177 */     buf.append(title);
/*  178 */     String tList = trelloDeathsIds;
/*      */     
/*  180 */     TrelloCard tc = new TrelloCard(Constants.trelloMVBoardId, tList, buf.toString(), description, "");
/*  181 */     trelloCardQueue.add(tc);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void addMessage(String sender, String playerName, String reason, int hours) {
/*  187 */     if (Constants.trelloMVBoardId.length() == 0) {
/*      */       return;
/*      */     }
/*      */     
/*  191 */     StringBuilder buf = new StringBuilder();
/*  192 */     buf.append(Tickets.convertTime(System.currentTimeMillis()) + " " + sender + " ");
/*      */ 
/*      */     
/*  195 */     String tList = "";
/*  196 */     String tLabel = "";
/*      */     
/*  198 */     if (hours == 0) {
/*      */       
/*  200 */       tList = trelloMutewarnIds;
/*  201 */       buf.append("Mutewarn " + playerName);
/*      */       
/*  203 */       tLabel = "Orange";
/*      */     }
/*  205 */     else if (reason.length() == 0) {
/*      */       
/*  207 */       tList = trelloMuteIds;
/*  208 */       buf.append("Unmute " + playerName);
/*  209 */       tLabel = "Unmute";
/*      */     }
/*      */     else {
/*      */       
/*  213 */       tList = trelloMuteIds;
/*  214 */       buf.append("Mute " + playerName + " for " + hours + " hour" + ((hours == 1) ? " " : "s "));
/*  215 */       tLabel = "Mute";
/*      */     } 
/*  217 */     TrelloCard tc = new TrelloCard(Constants.trelloMVBoardId, tList, buf.toString(), reason, tLabel);
/*  218 */     trelloCardQueue.add(tc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void updateTicketsInTrello() {
/*  228 */     if (trelloListIds[1].length() == 0) {
/*      */       
/*  230 */       obtainListIds();
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*      */     try {
/*  237 */       for (Ticket ticket : Tickets.getDirtyTickets()) {
/*      */ 
/*      */         
/*  240 */         if (ticket.getTrelloCardId().length() == 0) {
/*  241 */           createCard(ticket);
/*      */         } else {
/*      */           
/*  244 */           updateCard(ticket);
/*      */         } 
/*      */         
/*  247 */         for (TicketAction ta : ticket.getDirtyTicketActions()) {
/*  248 */           addAction(ticket, ta);
/*      */         }
/*      */         
/*  251 */         if (ticket.hasFeedback() && ticket.getTrelloFeedbackCardId().length() == 0) {
/*  252 */           createFeedbackCard(ticket);
/*      */         }
/*      */       } 
/*  255 */       for (Ticket ticket : Tickets.getArchiveTickets()) {
/*      */ 
/*      */         
/*  258 */         if (ticket.getTrelloCardId().length() == 0)
/*  259 */           createCard(ticket); 
/*  260 */         archiveCard(ticket);
/*      */       } 
/*  262 */       tickerCount = 0;
/*      */     }
/*  264 */     catch (RuntimeException e) {
/*      */ 
/*      */       
/*  267 */       if (tickerCount % 10 == 0)
/*  268 */         logger.log(Level.INFO, "Problem communicating with Trello " + (tickerCount + 1) + " times.", e); 
/*  269 */       if (tickerCount >= 1000)
/*  270 */         throw e; 
/*  271 */       tickerCount++;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void updateMuteVoteInTrello() {
/*  282 */     if (!trelloMuteStorage) {
/*      */       
/*  284 */       obtainMVListIds();
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*      */     try {
/*  291 */       for (VoteQuestion vq : VoteQuestions.getFinishedQuestions()) {
/*      */ 
/*      */         
/*  294 */         if (vq.getTrelloCardId().length() == 0)
/*      */         {
/*  296 */           createCard(vq);
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  304 */       for (VoteQuestion vq : VoteQuestions.getArchiveVoteQuestions()) {
/*      */ 
/*      */         
/*  307 */         if (vq.getTrelloCardId().length() == 0)
/*  308 */           createCard(vq); 
/*  309 */         archiveCard(vq);
/*      */       } 
/*      */       
/*  312 */       TrelloCard card = trelloCardQueue.pollFirst();
/*  313 */       while (card != null) {
/*      */         
/*  315 */         createCard(card);
/*  316 */         card = trelloCardQueue.pollFirst();
/*      */       } 
/*  318 */       tickerCount = 0;
/*      */     }
/*  320 */     catch (RuntimeException e) {
/*      */ 
/*      */       
/*  323 */       if (tickerCount % 10 == 0)
/*  324 */         logger.log(Level.INFO, "Problem communicating with Trello " + (tickerCount + 1) + " times.", e); 
/*  325 */       if (tickerCount >= 1000)
/*  326 */         throw e; 
/*  327 */       tickerCount++;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void archiveCardsInList(String trelloList, String listName) {
/*  340 */     long archiveTime = System.currentTimeMillis() - (Servers.isThisATestServer() ? 604800000L : 2419200000L);
/*  341 */     int archived = 0;
/*      */     
/*  343 */     String lurl = TrelloURL.make("https://api.trello.com/1/lists/{0}/cards", new String[] { trelloList });
/*  344 */     Map<String, String> argumentsMap = new HashMap<>();
/*  345 */     argumentsMap.put("fields", "id");
/*  346 */     JSONArray lja = doGetArray(lurl, argumentsMap);
/*  347 */     for (int x = 0; x < lja.length(); x++) {
/*      */       
/*  349 */       JSONObject jo = lja.getJSONObject(x);
/*  350 */       String id = jo.getString("id");
/*      */ 
/*      */       
/*  353 */       String createdDateHex = id.substring(0, 8);
/*  354 */       long dms = Long.parseLong(createdDateHex, 16) * 1000L;
/*  355 */       if (archiveTime > dms)
/*      */       {
/*  357 */         if (archiveCard(id))
/*  358 */           archived++; 
/*      */       }
/*      */     } 
/*  361 */     if (archived > 0) {
/*  362 */       logger.log(Level.INFO, "Archived " + archived + " " + listName + " cards in Trello");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addAction(Ticket ticket, TicketAction ta) {
/*      */     try {
/*  375 */       String url = TrelloURL.make("https://api.trello.com/1/cards/{0}/actions/comments", new String[] { ticket.getTrelloCardId() });
/*  376 */       ta.setTrelloCommentId(addComment(url, ta.getTrelloComment()));
/*      */     }
/*  378 */     catch (TrelloCardNotFoundException tcnfe) {
/*      */ 
/*      */       
/*  381 */       ta.setTrelloCommentId("Failed");
/*      */       
/*  383 */       logger.log(Level.WARNING, tcnfe.getMessage(), (Throwable)tcnfe);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static String addComment(String url, String text) throws TrelloCardNotFoundException {
/*  389 */     Map<String, String> keyValueMap = new HashMap<>();
/*  390 */     keyValueMap.put("text", text);
/*      */     
/*  392 */     JSONObject jo = doPost(url, keyValueMap);
/*  393 */     return jo.get("id").toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void updateCard(Ticket ticket) {
/*      */     try {
/*  405 */       String cardId = ticket.getTrelloCardId();
/*  406 */       if (ticket.hasSummaryChanged())
/*  407 */         updateCard(TrelloURL.make("https://api.trello.com/1/cards/{0}/name", new String[] { cardId }), ticket.getTrelloName()); 
/*  408 */       if (ticket.hasDescriptionChanged())
/*  409 */         updateCard(TrelloURL.make("https://api.trello.com/1/cards/{0}/desc", new String[] { cardId }), ticket.getDescription()); 
/*  410 */       if (ticket.hasListChanged()) {
/*  411 */         updateCard(TrelloURL.make("https://api.trello.com/1/cards/{0}/idList", new String[] { cardId }), trelloListIds[ticket.getTrelloListCode()]);
/*      */       }
/*  413 */       Tickets.setTicketIsDirty(ticket, false);
/*      */     }
/*  415 */     catch (TrelloCardNotFoundException tcnfe) {
/*      */ 
/*      */       
/*  418 */       Tickets.setTicketIsDirty(ticket, false);
/*      */       
/*  420 */       logger.log(Level.WARNING, tcnfe.getMessage(), (Throwable)tcnfe);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean archiveCard(String cardId) {
/*      */     try {
/*  434 */       updateCard(TrelloURL.make("https://api.trello.com/1/cards/{0}/closed", new String[] { cardId }), "true");
/*  435 */       return true;
/*      */     }
/*  437 */     catch (TrelloCardNotFoundException tcnfe) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  442 */       logger.log(Level.WARNING, tcnfe.getMessage(), (Throwable)tcnfe);
/*  443 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void archiveCard(VoteQuestion voteQuestion) {
/*      */     try {
/*  452 */       updateCard(TrelloURL.make("https://api.trello.com/1/cards/{0}/closed", new String[] { voteQuestion.getTrelloCardId() }), "true");
/*      */     }
/*  454 */     catch (TrelloCardNotFoundException tcnfe) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  459 */       logger.log(Level.WARNING, tcnfe.getMessage(), (Throwable)tcnfe);
/*      */     } 
/*  461 */     VoteQuestions.queueSetArchiveState(voteQuestion.getQuestionId(), (byte)3);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void archiveCard(Ticket ticket) {
/*      */     try {
/*  475 */       updateCard(TrelloURL.make("https://api.trello.com/1/cards/{0}/closed", new String[] { ticket.getTrelloCardId() }), "true");
/*      */       
/*  477 */       if (ticket.hasFeedback() && ticket.getTrelloFeedbackCardId().length() > 0) {
/*  478 */         updateCard(TrelloURL.make("https://api.trello.com/1/cards/{0}/closed", new String[] { ticket.getTrelloFeedbackCardId() }), "true");
/*      */       }
/*  480 */     } catch (TrelloCardNotFoundException tcnfe) {
/*      */ 
/*      */ 
/*      */       
/*  484 */       logger.log(Level.WARNING, tcnfe.getMessage(), (Throwable)tcnfe);
/*      */     } 
/*  486 */     Tickets.setTicketArchiveState(ticket, (byte)3);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void updateCard(String url, String value) throws TrelloCardNotFoundException {
/*  497 */     Map<String, String> keyValueMap = new HashMap<>();
/*  498 */     keyValueMap.put("value", value);
/*  499 */     doPut(url, keyValueMap);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createCard(Ticket ticket) {
/*      */     try {
/*  511 */       String url = TrelloURL.make("https://api.trello.com/1/cards", new String[0]);
/*  512 */       Map<String, String> keyValueMap = new HashMap<>();
/*  513 */       keyValueMap.put("name", ticket.getTrelloName());
/*  514 */       keyValueMap.put("desc", ticket.getDescription());
/*  515 */       keyValueMap.put("idList", trelloListIds[ticket.getTrelloListCode()]);
/*      */       
/*  517 */       JSONObject jo = doPost(url, keyValueMap);
/*      */ 
/*      */       
/*  520 */       String shortLink = getShortLink(jo.getString("shortUrl"));
/*  521 */       Tickets.setTicketTrelloCardId(ticket, shortLink);
/*      */     }
/*  523 */     catch (TrelloCardNotFoundException tcnfe) {
/*      */ 
/*      */       
/*  526 */       logger.log(Level.WARNING, tcnfe.getMessage(), (Throwable)tcnfe);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createCard(TrelloCard card) {
/*      */     try {
/*  534 */       String url = TrelloURL.make("https://api.trello.com/1/cards", new String[0]);
/*  535 */       Map<String, String> keyValueMap = new HashMap<>();
/*  536 */       keyValueMap.put("name", card.getTitle());
/*  537 */       keyValueMap.put("desc", card.getDescription());
/*      */       
/*  539 */       keyValueMap.put("idList", card.getListId());
/*      */ 
/*      */       
/*  542 */       JSONObject jSONObject = doPost(url, keyValueMap);
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  547 */     catch (TrelloCardNotFoundException tcnfe) {
/*      */ 
/*      */       
/*  550 */       logger.log(Level.WARNING, tcnfe.getMessage(), (Throwable)tcnfe);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createCard(VoteQuestion question) {
/*      */     try {
/*  561 */       String name = Tickets.convertTime(question.getVoteStart()) + " " + question.getQuestionTitle() + " (" + Tickets.convertTime(question.getVoteEnd()) + ")";
/*  562 */       String desc = question.getQuestionText();
/*  563 */       String url = TrelloURL.make("https://api.trello.com/1/cards", new String[0]);
/*  564 */       Map<String, String> keyValueMap = new HashMap<>();
/*  565 */       keyValueMap.put("name", name);
/*  566 */       keyValueMap.put("desc", desc);
/*  567 */       keyValueMap.put("idList", trelloVotingIds);
/*      */       
/*  569 */       JSONObject jo = doPost(url, keyValueMap);
/*  570 */       String shortLink = getShortLink(jo.getString("shortUrl"));
/*      */ 
/*      */       
/*  573 */       addVoteQuestionDetails(question, shortLink);
/*      */       
/*  575 */       VoteQuestions.queueSetTrelloCardId(question.getQuestionId(), shortLink);
/*      */     }
/*  577 */     catch (TrelloCardNotFoundException tcnfe) {
/*      */ 
/*      */       
/*  580 */       logger.log(Level.WARNING, tcnfe.getMessage(), (Throwable)tcnfe);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addVoteQuestionDetails(VoteQuestion question, String shortLink) {
/*      */     try {
/*  588 */       String url = TrelloURL.make("https://api.trello.com/1/cards/{0}/actions/comments", new String[] { shortLink });
/*  589 */       StringBuilder buf = new StringBuilder();
/*  590 */       buf.append("SUMMARY\n\n");
/*  591 */       buf.append(getOptionSummary(question.getOption1Text(), question.getOption1Count(), question.getVoteCount()));
/*  592 */       buf.append(getOptionSummary(question.getOption2Text(), question.getOption2Count(), question.getVoteCount()));
/*  593 */       buf.append(getOptionSummary(question.getOption3Text(), question.getOption3Count(), question.getVoteCount()));
/*  594 */       buf.append(getOptionSummary(question.getOption4Text(), question.getOption4Count(), question.getVoteCount()));
/*  595 */       buf.append("\nTotal Players Voted: " + question.getVoteCount());
/*      */ 
/*      */       
/*  598 */       String reply = addComment(url, buf.toString());
/*      */ 
/*      */       
/*  601 */       buf = new StringBuilder();
/*  602 */       buf.append("OPTIONS\n");
/*  603 */       buf.append("\nVote Start: " + Tickets.convertTime(question.getVoteStart()));
/*  604 */       buf.append("\nVote End: " + Tickets.convertTime(question.getVoteEnd()));
/*  605 */       buf.append("\nAllow Multiple: " + question.isAllowMultiple());
/*  606 */       buf.append("\nPrem Only: " + question.isPremOnly());
/*  607 */       buf.append("\nJK: " + question.isJK());
/*  608 */       buf.append("\nMR: " + question.isMR());
/*  609 */       buf.append("\nHots: " + question.isHots());
/*  610 */       buf.append("\nFreedom: " + question.isFreedom());
/*  611 */       reply = addComment(url, buf.toString());
/*      */ 
/*      */       
/*  614 */       buf = new StringBuilder();
/*  615 */       buf.append("SERVERS\n");
/*  616 */       for (VoteServer vs : question.getServers())
/*  617 */         buf.append("\n" + (Servers.getServerWithId(vs.getServerId())).name); 
/*  618 */       reply = addComment(url, buf.toString());
/*      */     }
/*  620 */     catch (TrelloCardNotFoundException tcnfe) {
/*      */ 
/*      */       
/*  623 */       logger.log(Level.WARNING, tcnfe.getMessage(), (Throwable)tcnfe);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static String getOptionSummary(String text, int count, int total) {
/*  629 */     if (text.length() == 0)
/*  630 */       return ""; 
/*  631 */     int perc = -1;
/*  632 */     String percText = " (Nan%)";
/*  633 */     if (total > 0) {
/*      */       
/*  635 */       perc = count * 100 / total;
/*  636 */       percText = " (" + perc + "%)";
/*      */     } 
/*      */     
/*  639 */     StringBuilder buf = new StringBuilder();
/*  640 */     buf.append(text + " [" + count + percText + "]\n");
/*  641 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void createFeedbackCard(Ticket ticket) {
/*      */     try {
/*  654 */       String url = TrelloURL.make("https://api.trello.com/1/cards", new String[0]);
/*  655 */       Map<String, String> keyValueMap = new HashMap<>();
/*  656 */       keyValueMap.put("name", ticket.getTrelloFeedbackTitle());
/*  657 */       keyValueMap.put("idList", trelloListIds[5]);
/*  658 */       keyValueMap.put("idCardSource", trelloFeedbackTemplateCardId);
/*  659 */       JSONObject fjo = doPost(url, keyValueMap);
/*      */ 
/*      */       
/*  662 */       String shortLink = getShortLink(fjo.getString("shortUrl"));
/*      */ 
/*      */       
/*  665 */       updateCard(TrelloURL.make("https://api.trello.com/1/cards/{0}/desc", new String[] { shortLink }), ticket.getFeedbackText());
/*  666 */       tickSelected(ticket, shortLink);
/*      */       
/*  668 */       Tickets.setTicketTrelloFeedbackCardId(ticket, shortLink);
/*      */     }
/*  670 */     catch (TrelloCardNotFoundException tcnfe) {
/*      */ 
/*      */       
/*  673 */       logger.log(Level.WARNING, tcnfe.getMessage(), (Throwable)tcnfe);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void tickSelected(Ticket ticket, String cardId) {
/*      */     try {
/*  683 */       String curl = TrelloURL.make("https://api.trello.com/1/cards/{0}/checklists", new String[] { cardId });
/*  684 */       Map<String, String> argumentsMap = new HashMap<>();
/*  685 */       argumentsMap.put("card_fields", "checkItemStates,idChecklists,name");
/*  686 */       argumentsMap.put("checkItem_fields", "name");
/*  687 */       argumentsMap.put("fields", "name");
/*  688 */       JSONArray ja = doGetArray(curl, argumentsMap);
/*      */ 
/*      */ 
/*      */       
/*  692 */       String[] nService = { "Quality Of Service", "Superior", "Good", "Average", "Fair", "Poor" };
/*  693 */       String[] nCourteous = { "Courteous", "Strongly Agree", "Somewhat Agree", "Neutral", "Somewhat Disagree", "Strongly Disagree" };
/*  694 */       String[] nKnowledgeable = { "Knowledgeable", "Strongly Agree", "Somewhat Agree", "Neutral", "Somewhat Disagree", "Strongly Disagree" };
/*  695 */       String[] nGeneral = { "General", "Wrong Info", "No Understand", "Unclear", "No Solve", "Disorganized", "Other", "Fine" };
/*  696 */       String[] nQuality = { "Quality", "Patient", "Enthusiastic", "Listened", "Friendly", "Responsive", "Nothing" };
/*  697 */       String[] nIrked = { "Irked", "Patient", "Enthusiastic", "Listened", "Friendly", "Responsive", "Nothing" };
/*      */       
/*  699 */       String[] idService = { "", "", "", "", "", "" };
/*  700 */       String[] idCourteous = { "", "", "", "", "", "" };
/*  701 */       String[] idKnowledgeable = { "", "", "", "", "", "" };
/*  702 */       String[] idGeneral = { "", "", "", "", "", "", "", "" };
/*  703 */       String[] idQuality = { "", "", "", "", "", "", "" };
/*  704 */       String[] idIrked = { "", "", "", "", "", "", "" };
/*      */       
/*  706 */       idService[0] = ja.getJSONObject(nService[0]).getString("id"); int i;
/*  707 */       for (i = 1; i < idService.length; i++) {
/*  708 */         idService[i] = ja.getJSONObject(nService[0]).getJSONArray("checkItems").getJSONObject(nService[i]).getString("id");
/*      */       }
/*  710 */       idCourteous[0] = ja.getJSONObject(nCourteous[0]).getString("id");
/*  711 */       for (i = 1; i < idCourteous.length; i++) {
/*  712 */         idCourteous[i] = ja.getJSONObject(nCourteous[0]).getJSONArray("checkItems").getJSONObject(nCourteous[i]).getString("id");
/*      */       }
/*  714 */       idKnowledgeable[0] = ja.getJSONObject(nKnowledgeable[0]).getString("id");
/*  715 */       for (i = 1; i < idKnowledgeable.length; i++) {
/*  716 */         idKnowledgeable[i] = ja.getJSONObject(nKnowledgeable[0]).getJSONArray("checkItems").getJSONObject(nKnowledgeable[i]).getString("id");
/*      */       }
/*  718 */       idGeneral[0] = ja.getJSONObject(nGeneral[0]).getString("id");
/*  719 */       for (i = 1; i < idGeneral.length; i++) {
/*  720 */         idGeneral[i] = ja.getJSONObject(nGeneral[0]).getJSONArray("checkItems").getJSONObject(nGeneral[i]).getString("id");
/*      */       }
/*  722 */       idQuality[0] = ja.getJSONObject(nQuality[0]).getString("id");
/*  723 */       for (i = 1; i < idQuality.length; i++) {
/*  724 */         idQuality[i] = ja.getJSONObject(nQuality[0]).getJSONArray("checkItems").getJSONObject(nQuality[i]).getString("id");
/*      */       }
/*  726 */       idIrked[0] = ja.getJSONObject(nIrked[0]).getString("id");
/*  727 */       for (i = 1; i < idIrked.length; i++) {
/*  728 */         idIrked[i] = ja.getJSONObject(nIrked[0]).getJSONArray("checkItems").getJSONObject(nIrked[i]).getString("id");
/*      */       }
/*      */       
/*  731 */       TicketAction ta = ticket.getFeedback();
/*      */       
/*  733 */       if (ta.wasServiceSuperior()) tick(cardId, idService[0], idService[1]); 
/*  734 */       if (ta.wasServiceGood()) tick(cardId, idService[0], idService[2]); 
/*  735 */       if (ta.wasServiceAverage()) tick(cardId, idService[0], idService[3]); 
/*  736 */       if (ta.wasServiceFair()) tick(cardId, idService[0], idService[4]); 
/*  737 */       if (ta.wasServicePoor()) tick(cardId, idService[0], idService[5]);
/*      */       
/*  739 */       if (ta.wasCourteousStronglyAgree()) tick(cardId, idCourteous[0], idCourteous[1]); 
/*  740 */       if (ta.wasCourteousSomewhatAgree()) tick(cardId, idCourteous[0], idCourteous[2]); 
/*  741 */       if (ta.wasCourteousNeutral()) tick(cardId, idCourteous[0], idCourteous[3]); 
/*  742 */       if (ta.wasCourteousSomewhatDisagree()) tick(cardId, idCourteous[0], idCourteous[4]); 
/*  743 */       if (ta.wasCourteousStronglyDisagree()) tick(cardId, idCourteous[0], idCourteous[5]);
/*      */       
/*  745 */       if (ta.wasKnowledgeableStronglyAgree()) tick(cardId, idKnowledgeable[0], idKnowledgeable[1]); 
/*  746 */       if (ta.wasKnowledgeableSomewhatAgree()) tick(cardId, idKnowledgeable[0], idKnowledgeable[2]); 
/*  747 */       if (ta.wasKnowledgeableNeutral()) tick(cardId, idKnowledgeable[0], idKnowledgeable[3]); 
/*  748 */       if (ta.wasKnowledgeableSomewhatDisagree()) tick(cardId, idKnowledgeable[0], idKnowledgeable[4]); 
/*  749 */       if (ta.wasKnowledgeableStronglyDisagree()) tick(cardId, idKnowledgeable[0], idKnowledgeable[5]);
/*      */       
/*  751 */       if (ta.wasGeneralWrongInfo()) tick(cardId, idGeneral[0], idGeneral[1]); 
/*  752 */       if (ta.wasGeneralNoUnderstand()) tick(cardId, idGeneral[0], idGeneral[2]); 
/*  753 */       if (ta.wasGeneralUnclear()) tick(cardId, idGeneral[0], idGeneral[3]); 
/*  754 */       if (ta.wasGeneralNoSolve()) tick(cardId, idGeneral[0], idGeneral[4]); 
/*  755 */       if (ta.wasGeneralDisorganized()) tick(cardId, idGeneral[0], idGeneral[5]); 
/*  756 */       if (ta.wasGeneralOther()) tick(cardId, idGeneral[0], idGeneral[6]); 
/*  757 */       if (ta.wasGeneralFine()) tick(cardId, idGeneral[0], idGeneral[7]);
/*      */       
/*  759 */       if (ta.wasQualityPatient()) tick(cardId, idQuality[0], idQuality[1]); 
/*  760 */       if (ta.wasQualityEnthusiastic()) tick(cardId, idQuality[0], idQuality[2]); 
/*  761 */       if (ta.wasQualityListened()) tick(cardId, idQuality[0], idQuality[3]); 
/*  762 */       if (ta.wasQualityFriendly()) tick(cardId, idQuality[0], idQuality[4]); 
/*  763 */       if (ta.wasQualityResponsive()) tick(cardId, idQuality[0], idQuality[5]); 
/*  764 */       if (ta.wasQualityNothing()) tick(cardId, idQuality[0], idQuality[6]);
/*      */       
/*  766 */       if (ta.wasIrkedPatient()) tick(cardId, idIrked[0], idIrked[1]); 
/*  767 */       if (ta.wasIrkedEnthusiastic()) tick(cardId, idIrked[0], idIrked[2]); 
/*  768 */       if (ta.wasIrkedListened()) tick(cardId, idIrked[0], idIrked[3]); 
/*  769 */       if (ta.wasIrkedFriendly()) tick(cardId, idIrked[0], idIrked[4]); 
/*  770 */       if (ta.wasIrkedResponsive()) tick(cardId, idIrked[0], idIrked[5]); 
/*  771 */       if (ta.wasIrkedNothing()) tick(cardId, idIrked[0], idIrked[6]);
/*      */     
/*  773 */     } catch (TrelloCardNotFoundException tcnfe) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  778 */       logger.log(Level.WARNING, tcnfe.getMessage(), (Throwable)tcnfe);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void tick(String cardId, String checkListId, String checkItemId) throws TrelloCardNotFoundException {
/*  784 */     String url = TrelloURL.make("https://api.trello.com/1/cards/{0}/checklist/{1}/checkItem/{2}/state", new String[] { cardId, checkListId, checkItemId });
/*      */     
/*  786 */     Map<String, String> keyValueMap = new HashMap<>();
/*  787 */     keyValueMap.put("idCheckList", checkListId);
/*  788 */     keyValueMap.put("idCheckItem", checkItemId);
/*  789 */     keyValueMap.put("value", "true");
/*      */     
/*  791 */     JSONObject jo = doPut(url, keyValueMap);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void obtainListIds() {
/*  800 */     String url = TrelloURL.make("https://api.trello.com/1/boards/{0}/lists", new String[] { Constants.trelloBoardid });
/*  801 */     JSONArray ja = doGetArray(url);
/*      */     
/*  803 */     int count = 0;
/*  804 */     for (int x = 0; x < ja.length(); x++) {
/*      */       
/*  806 */       JSONObject jo = ja.getJSONObject(x);
/*  807 */       String name = jo.getString("name");
/*  808 */       for (int y = 1; y <= 5; y++) {
/*      */         
/*  810 */         if (name.equalsIgnoreCase(trelloLists[y])) {
/*      */           
/*  812 */           trelloListIds[y] = jo.getString("id");
/*  813 */           count++;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  820 */     if (count != trelloListIds.length - 1)
/*      */     {
/*      */       
/*  823 */       throw new JSONException("Not all the required lists found on Trello Ticket board.");
/*      */     }
/*      */ 
/*      */     
/*  827 */     String lurl = TrelloURL.make("https://api.trello.com/1/lists/{0}/cards", new String[] { trelloListIds[5] });
/*  828 */     Map<String, String> argumentsMap = new HashMap<>();
/*  829 */     argumentsMap.put("fields", "name");
/*  830 */     argumentsMap.put("card_fields", "name");
/*  831 */     JSONArray lja = doGetArray(lurl, argumentsMap);
/*  832 */     for (int i = 0; i < lja.length(); i++) {
/*      */       
/*  834 */       JSONObject jo = lja.getJSONObject(i);
/*  835 */       String name = jo.getString("name");
/*  836 */       if (name.equalsIgnoreCase("Feedback Checklist Template")) {
/*      */         
/*  838 */         trelloFeedbackTemplateCardId = jo.getString("id");
/*      */         break;
/*      */       } 
/*      */     } 
/*  842 */     if (trelloFeedbackTemplateCardId.length() == 0)
/*      */     {
/*  844 */       throw new JSONException("Could not find the Feedback Checklist Template on Trello Ticket board.");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void obtainMVListIds() {
/*  853 */     if (Constants.trelloMVBoardId.length() == 0) {
/*      */       return;
/*      */     }
/*      */     
/*  857 */     String url = TrelloURL.make("https://api.trello.com/1/boards/{0}/lists", new String[] { Constants.trelloMVBoardId });
/*  858 */     JSONArray ja = doGetArray(url);
/*      */     
/*  860 */     for (int x = 0; x < ja.length(); x++) {
/*      */       
/*  862 */       JSONObject jo = ja.getJSONObject(x);
/*  863 */       String name = jo.getString("name");
/*  864 */       if (name.equals("Mutes")) {
/*  865 */         trelloMuteIds = jo.getString("id");
/*  866 */       } else if (name.equals("Mutewarns")) {
/*  867 */         trelloMutewarnIds = jo.getString("id");
/*  868 */       } else if (name.equals("Voting")) {
/*  869 */         trelloVotingIds = jo.getString("id");
/*  870 */       } else if (name.equals("Highways")) {
/*  871 */         trelloHighwaysIds = jo.getString("id");
/*  872 */       } else if (name.equals("Deaths")) {
/*  873 */         trelloDeathsIds = jo.getString("id");
/*      */       } 
/*      */     } 
/*  876 */     if (trelloMuteIds.length() == 0 || trelloMutewarnIds.length() == 0 || trelloVotingIds.length() == 0 || trelloHighwaysIds.length() == 0)
/*      */     {
/*      */       
/*  879 */       throw new JSONException("Not all the required lists found on Trello Mute Vote board.");
/*      */     }
/*      */     
/*  882 */     trelloMuteStorage = true;
/*      */     
/*  884 */     archiveCardsInList(trelloMuteIds, "Mutes");
/*  885 */     archiveCardsInList(trelloMutewarnIds, "Mutewarns");
/*  886 */     archiveCardsInList(trelloHighwaysIds, "Highways");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String getShortLink(String shortUrl) {
/*  904 */     String[] parts = shortUrl.split("/");
/*  905 */     String shortLink = parts[parts.length - 1];
/*  906 */     return shortLink;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static JSONArray doGetArray(String url) {
/*      */     try {
/*  919 */       InputStream in = doRequest(url, "GET", null);
/*  920 */       if (in == null) {
/*  921 */         throw new JSONException("Failed read permissions for Trello board.");
/*      */       }
/*  923 */       JSONTokener tk = new JSONTokener(in);
/*  924 */       return new JSONArray(tk);
/*      */     }
/*  926 */     catch (TrelloCardNotFoundException tcnfe) {
/*      */       
/*  928 */       throw new JSONException("Cannot find ticket, but were not looking for one");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static JSONArray doGetArray(String url, Map<String, String> map) {
/*  942 */     String lurl = url;
/*      */     
/*      */     try {
/*  945 */       StringBuilder sb = new StringBuilder();
/*  946 */       boolean hasMap = (map != null && !map.isEmpty());
/*  947 */       if (hasMap)
/*      */       {
/*  949 */         for (String key : map.keySet()) {
/*      */           
/*  951 */           sb.append("&");
/*  952 */           sb.append(URLEncoder.encode(key, "UTF-8"));
/*  953 */           sb.append("=");
/*  954 */           sb.append(URLEncoder.encode(map.get(key), "UTF-8"));
/*      */         } 
/*      */         
/*  957 */         lurl = url + sb.toString();
/*      */       }
/*      */     
/*  960 */     } catch (UnsupportedEncodingException e) {
/*      */ 
/*      */       
/*  963 */       logger.log(Level.WARNING, e.getMessage(), e);
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/*  968 */       InputStream in = doRequest(lurl, "GET", null);
/*  969 */       if (in == null) {
/*  970 */         throw new JSONException("Failed read permissions for Trello board.");
/*      */       }
/*  972 */       JSONTokener tk = new JSONTokener(in);
/*  973 */       return new JSONArray(tk);
/*      */     }
/*  975 */     catch (TrelloCardNotFoundException tcnfe) {
/*      */       
/*  977 */       throw new JSONException("Cannot find ticket, but were not looking for one");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static JSONObject doPut(String url, Map<String, String> map) throws TrelloCardNotFoundException {
/* 1023 */     InputStream in = doRequest(url, "PUT", map);
/* 1024 */     if (in == null) {
/* 1025 */       throw new JSONException("Failed read permissions for Trello board.");
/*      */     }
/* 1027 */     JSONTokener tk = new JSONTokener(in);
/* 1028 */     return new JSONObject(tk);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static JSONObject doPost(String url, Map<String, String> map) throws TrelloCardNotFoundException {
/* 1057 */     InputStream in = doRequest(url, "POST", map);
/* 1058 */     if (in == null) {
/* 1059 */       throw new JSONException("Failed read permissions for Trello board.");
/*      */     }
/* 1061 */     JSONTokener tk = new JSONTokener(in);
/* 1062 */     return new JSONObject(tk);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static InputStream doRequest(String url, String requestMethod, Map<String, String> map) throws TrelloCardNotFoundException {
/*      */     try {
/* 1087 */       boolean hasMap = (map != null && !map.isEmpty());
/* 1088 */       HttpsURLConnection conn = (HttpsURLConnection)(new URL(url)).openConnection();
/* 1089 */       conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
/* 1090 */       conn.setDoOutput((requestMethod.equals("POST") || requestMethod.equals("PUT")));
/* 1091 */       conn.setRequestMethod(requestMethod);
/*      */       
/* 1093 */       conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
/*      */       
/* 1095 */       String arguments = "";
/* 1096 */       if (hasMap) {
/*      */         
/* 1098 */         StringBuilder sb = new StringBuilder();
/* 1099 */         for (String key : map.keySet()) {
/*      */           
/* 1101 */           sb.append((sb.length() > 0) ? "&" : "");
/* 1102 */           sb.append(URLEncoder.encode(key, "UTF-8"));
/* 1103 */           sb.append("=");
/* 1104 */           sb.append(URLEncoder.encode(map.get(key), "UTF-8"));
/*      */         } 
/* 1106 */         conn.getOutputStream().write(sb.toString().getBytes());
/* 1107 */         conn.getOutputStream().close();
/* 1108 */         arguments = sb.toString();
/*      */       } 
/* 1110 */       conn.connect();
/*      */       
/* 1112 */       int rc = conn.getResponseCode();
/* 1113 */       String responseMessage = conn.getResponseMessage();
/* 1114 */       if (rc != 200)
/* 1115 */         logger.info("response " + rc + " (" + responseMessage + ") from " + requestMethod + " " + url + " args:" + arguments); 
/* 1116 */       if (rc == 404)
/* 1117 */         throw new TrelloCardNotFoundException("Ticket not found"); 
/* 1118 */       if (rc > 399) {
/*      */         
/* 1120 */         String str = stream2String(conn.getErrorStream());
/* 1121 */         logger.info("error response:" + str);
/* 1122 */         return null;
/*      */       } 
/*      */ 
/*      */       
/* 1126 */       return getWrappedInputStream(conn
/* 1127 */           .getInputStream(), "gzip".equalsIgnoreCase(conn.getContentEncoding()));
/*      */ 
/*      */     
/*      */     }
/* 1131 */     catch (IOException e) {
/*      */       
/* 1133 */       throw new TrelloException(e.getMessage());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static InputStream getWrappedInputStream(InputStream is, boolean gzip) throws IOException {
/* 1150 */     if (gzip)
/*      */     {
/* 1152 */       return new BufferedInputStream(new GZIPInputStream(is));
/*      */     }
/*      */ 
/*      */     
/* 1156 */     return new BufferedInputStream(is);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String stream2String(InputStream in) {
/* 1168 */     InputStreamReader is = new InputStreamReader(in);
/* 1169 */     BufferedReader br = new BufferedReader(is);
/* 1170 */     StringBuilder sb = new StringBuilder();
/*      */     
/*      */     try {
/* 1173 */       String read = br.readLine();
/* 1174 */       while (read != null)
/*      */       {
/*      */         
/* 1177 */         sb.append(read);
/* 1178 */         read = br.readLine();
/*      */       }
/*      */     
/* 1181 */     } catch (IOException e) {
/*      */       
/* 1183 */       return "Error trying to read stream:" + e.getMessage();
/*      */     } 
/*      */     
/* 1186 */     return sb.toString();
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\support\Trello.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */