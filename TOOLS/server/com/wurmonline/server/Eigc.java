/*     */ package com.wurmonline.server;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Communicator;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.server.utils.HttpResponseStatus;
/*     */ import com.wurmonline.shared.util.IoUtilities;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.StringReader;
/*     */ import java.io.Writer;
/*     */ import java.net.URL;
/*     */ import java.net.URLEncoder;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.LinkedList;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.HttpsURLConnection;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.InputSource;
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
/*     */ 
/*     */ public final class Eigc
/*     */   implements MiscConstants
/*     */ {
/*  65 */   private static final Logger logger = Logger.getLogger(Eigc.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String LOAD_ALL_EIGC = "SELECT * FROM EIGC";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String INSERT_EIGC_ACCOUNT = "INSERT INTO EIGC(USERNAME,PASSWORD,SERVICEBUNDLE,EXPIRATION,EMAIL) VALUES (?,?,?,?,?)";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String DELETE_EIGC_ACCOUNT = "DELETE FROM EIGC WHERE USERNAME=?";
/*     */ 
/*     */ 
/*     */   
/*  81 */   private static final LinkedList<EigcClient> EIGC_CLIENTS = new LinkedList<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String HTTP_CHARACTER_ENCODING = "UTF-8";
/*     */ 
/*     */   
/*  88 */   private static final int initialAccountsToProvision = Servers.isThisATestServer() ? 5 : 25;
/*     */   
/*  90 */   private static final int accountsToProvision = Servers.isThisATestServer() ? 5 : 25;
/*     */ 
/*     */   
/*     */   private static boolean isProvisioning = false;
/*     */ 
/*     */   
/*     */   public static final String SERVICE_PROXIMITY = "proximity";
/*     */   
/*     */   public static final String SERVICE_P2P = "p2p";
/*     */   
/*     */   public static final String SERVICE_TEAM = "team";
/*     */   
/*     */   public static final String SERVICE_LECTURE = "lecture";
/*     */   
/*     */   public static final String SERVICE_HIFI = "hifi";
/*     */   
/*     */   public static final String SERVICES_FREE = "proximity";
/*     */   
/*     */   public static final String SERVICES_BUNDLE = "proximity,team,p2p,hifi";
/*     */   
/*     */   public static final String PROTOCOL_PROVISIONING = "https://";
/*     */   
/* 112 */   private static String HOST_PROVISIONING = "bla";
/* 113 */   public static String URL_PROXIMITY = "bla";
/* 114 */   public static String URL_SIP_REGISTRAR = "bla";
/* 115 */   public static String URL_SIP_PROXY = "bla";
/* 116 */   private static String EIGC_REALM = "bla";
/*     */   private static final int PORT_PROVISIONING = 5002;
/* 118 */   private static String URL_PROVISIONING = "https://" + HOST_PROVISIONING + ":" + 'ᎊ' + "/";
/*     */   
/* 120 */   private static String CREATE_URL = URL_PROVISIONING + "userprovisioning/v1/create/" + EIGC_REALM + "/";
/*     */   
/* 122 */   private static String MODIFY_URL = URL_PROVISIONING + "userprovisioning/v1/modify/" + EIGC_REALM + "/";
/*     */   
/* 124 */   private static String VIEW_URL = URL_PROVISIONING + "userprovisioning/v1/view/" + EIGC_REALM + "/";
/*     */   
/* 126 */   private static String DELETE_URL = URL_PROVISIONING + "userprovisioning/v1/delete/" + EIGC_REALM + "/";
/*     */   
/* 128 */   private static String EIGC_PASSWORD = "tL4PDKim";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final void setEigcHttpsOverride() {
/* 136 */     HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
/*     */         {
/*     */           
/*     */           public boolean verify(String hostname, SSLSession sslSession)
/*     */           {
/* 141 */             if (hostname.equals(Eigc.HOST_PROVISIONING))
/*     */             {
/* 143 */               return true;
/*     */             }
/* 145 */             return false;
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void loadAllAccounts() {
/* 152 */     Connection dbcon = null;
/* 153 */     PreparedStatement ps = null;
/* 154 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 157 */       dbcon = DbConnector.getLoginDbCon();
/* 158 */       ps = dbcon.prepareStatement("SELECT * FROM EIGC");
/* 159 */       rs = ps.executeQuery();
/* 160 */       while (rs.next()) {
/*     */         
/* 162 */         String eigcUserId = rs.getString("USERNAME");
/*     */         
/* 164 */         EigcClient eigclient = new EigcClient(eigcUserId, rs.getString("PASSWORD"), rs.getString("SERVICEBUNDLE"), rs.getLong("EXPIRATION"), rs.getString("EMAIL"));
/*     */         
/* 166 */         EIGC_CLIENTS.add(eigclient);
/*     */       } 
/* 168 */       logger.log(Level.INFO, "Loaded " + EIGC_CLIENTS.size() + " eigc accounts.");
/*     */     }
/* 170 */     catch (SQLException sqx) {
/*     */       
/* 172 */       logger.log(Level.WARNING, "Problem loading eigc clients for server due to " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 176 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 177 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 179 */     if (Constants.isEigcEnabled)
/*     */     {
/* 181 */       if (EIGC_CLIENTS.size() < initialAccountsToProvision) {
/*     */         
/*     */         try {
/*     */           
/* 185 */           installCert();
/* 186 */           provisionAccounts(initialAccountsToProvision, false);
/*     */         }
/* 188 */         catch (Exception exc) {
/*     */           
/* 190 */           logger.log(Level.WARNING, exc.getMessage(), exc);
/*     */         } 
/*     */       }
/*     */     }
/* 194 */     if (Servers.localServer.testServer) {
/*     */       
/* 196 */       HOST_PROVISIONING = "provisioning.eigctestnw.com";
/* 197 */       URL_PROXIMITY = "sip:wurmonline.eigctestnw.com";
/* 198 */       URL_SIP_REGISTRAR = "wurmonline.eigctestnw.com";
/* 199 */       URL_SIP_PROXY = "sip:gateway.eigctestnw.com:35060";
/* 200 */       EIGC_REALM = "wurmonline.eigctestnw.com";
/* 201 */       EIGC_PASSWORD = "admin";
/*     */       
/* 203 */       changeEigcUrls();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final void changeEigcUrls() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void installCert() throws Exception {}
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void deleteAccounts(final int nums) {
/* 218 */     if (Constants.isEigcEnabled)
/*     */     {
/* 220 */       if (!isProvisioning) {
/*     */         
/* 222 */         isProvisioning = true;
/* 223 */         (new Thread()
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             public void run()
/*     */             {
/* 233 */               for (int x = 0; x < nums; x++) {
/*     */                 
/* 235 */                 String userName = "Wurmpool" + (Servers.localServer.id * 20000) + x + '\001';
/* 236 */                 Eigc.logger.log(Level.INFO, Eigc.deleteUser(userName));
/*     */               } 
/* 238 */               Eigc.isProvisioning = false;
/*     */             }
/* 240 */           }).start();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void deleteAccounts() {
/* 249 */     if (!isProvisioning) {
/*     */       
/* 251 */       final EigcClient[] clients = EIGC_CLIENTS.<EigcClient>toArray(new EigcClient[EIGC_CLIENTS.size()]);
/* 252 */       isProvisioning = true;
/* 253 */       (new Thread()
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void run()
/*     */           {
/* 263 */             for (EigcClient client : clients) {
/*     */               
/* 265 */               String userName = client.getClientId();
/* 266 */               Eigc.logger.log(Level.INFO, Eigc.deleteUser(userName));
/*     */             } 
/* 268 */             Eigc.isProvisioning = false;
/*     */           }
/* 270 */         }).start();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void provisionAccounts(final int numberToProvision, final boolean overRide) {
/* 277 */     if (Constants.isEigcEnabled || overRide)
/*     */     {
/* 279 */       if (!isProvisioning) {
/*     */         
/* 281 */         isProvisioning = true;
/* 282 */         (new Thread()
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             public void run()
/*     */             {
/* 292 */               String[] paramNames = { "servicebundle" };
/*     */               
/* 294 */               String[] paramVals = { "proximity" };
/*     */               
/* 296 */               String userName = "Wurmpool" + (Servers.localServer.id * 20000 + Eigc.EIGC_CLIENTS.size() + 1);
/* 297 */               int failed = 0;
/* 298 */               for (int x = 0; x < numberToProvision; x++) {
/*     */ 
/*     */                 
/*     */                 try {
/*     */                   
/* 303 */                   userName = "Wurmpool" + (Servers.localServer.id * 20000 + Eigc.EIGC_CLIENTS.size() + failed + 1);
/* 304 */                   String response = Eigc.httpPost(Eigc.CREATE_URL, paramNames, paramVals, userName);
/* 305 */                   if (Eigc.logger.isLoggable(Level.INFO))
/*     */                   {
/* 307 */                     Eigc.logger.info("Called " + Eigc.CREATE_URL + " with user name " + userName + " and received response " + response);
/*     */                   }
/*     */                   
/* 310 */                   boolean created = false;
/*     */                   
/*     */                   try {
/* 313 */                     InputSource inStream = new InputSource();
/*     */                     
/* 315 */                     inStream.setCharacterStream(new StringReader(response));
/* 316 */                     DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
/* 317 */                     DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
/*     */                     
/* 319 */                     Document doc = dBuilder.parse(inStream);
/* 320 */                     doc.getDocumentElement().normalize();
/*     */                     
/* 322 */                     Eigc.logger.log(Level.INFO, "Root element :" + doc.getDocumentElement().getNodeName());
/* 323 */                     NodeList nList = doc.getElementsByTagName("user");
/* 324 */                     for (int temp = 0; temp < nList.getLength(); temp++) {
/*     */                       
/* 326 */                       Node nNode = nList.item(temp);
/* 327 */                       if (nNode.getNodeType() == 1)
/*     */                       {
/* 329 */                         Element eElement = (Element)nNode;
/* 330 */                         String uname = Eigc.getTagValue("username", eElement);
/* 331 */                         Eigc.logger.log(Level.INFO, "UserName : " + uname);
/* 332 */                         String authid = Eigc.getTagValue("authid", eElement);
/* 333 */                         Eigc.logger.log(Level.INFO, "Auth Id : " + authid);
/* 334 */                         String password = Eigc.getTagValue("passwd", eElement);
/* 335 */                         Eigc.logger.log(Level.INFO, "Password : " + password);
/* 336 */                         String services = Eigc.getTagValue("servicebundle", eElement);
/* 337 */                         Eigc.logger.log(Level.INFO, "Service bundle : " + services);
/* 338 */                         Eigc.createAccount(uname, password, services, Long.MAX_VALUE, "");
/* 339 */                         created = true;
/*     */                       }
/*     */                     
/*     */                     } 
/* 343 */                   } catch (Exception e) {
/*     */                     
/* 345 */                     Eigc.logger.log(Level.WARNING, e.getMessage());
/*     */                   } 
/* 347 */                   if (!created) {
/* 348 */                     failed++;
/*     */                   }
/* 350 */                 } catch (Exception e) {
/*     */                   
/* 352 */                   Eigc.logger.log(Level.WARNING, "Problem calling " + Eigc.CREATE_URL + " with user name " + userName, e);
/*     */                 } 
/*     */               } 
/* 355 */               Eigc.isProvisioning = false;
/* 356 */               if (overRide)
/* 357 */                 Constants.isEigcEnabled = true; 
/*     */             }
/* 359 */           }).start();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getTagValue(String sTag, Element eElement) {
/* 366 */     NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
/* 367 */     Node nValue = nlList.item(0);
/*     */     
/* 369 */     return nValue.getNodeValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getEigcInfo(String eigcId) {
/*     */     try {
/* 376 */       String answer = httpGet(VIEW_URL, eigcId);
/*     */       
/* 378 */       return answer;
/*     */     }
/* 380 */     catch (IOException iox) {
/*     */       
/* 382 */       logger.log(Level.INFO, iox.getMessage(), iox);
/*     */       
/* 384 */       return "Failed to retrieve information about " + eigcId;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final String deleteUser(String userId) {
/*     */     try {
/* 391 */       String[] paramNames = new String[0];
/* 392 */       String[] paramVals = new String[0];
/* 393 */       String answer = httpDelete(DELETE_URL, paramNames, paramVals, userId);
/* 394 */       logger.log(Level.INFO, "Called " + DELETE_URL + " with userId=" + userId);
/* 395 */       if (answer.toLowerCase().contains("<rsp stat=\"ok\">")) {
/*     */         
/* 397 */         logger.log(Level.INFO, "Deleting " + userId + " from database.");
/* 398 */         deleteAccount(userId);
/*     */       } 
/* 400 */       return answer;
/*     */     }
/* 402 */     catch (Exception iox) {
/*     */       
/* 404 */       logger.log(Level.INFO, iox.getMessage(), iox);
/*     */       
/* 406 */       return "Failed to delete " + userId;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String modifyUser(String userId, String servicesAsCommaSeparatedString, long expiration) {
/*     */     try {
/* 414 */       String[] paramNames = { "servicebundle" };
/*     */       
/* 416 */       String[] paramVals = { servicesAsCommaSeparatedString };
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 422 */         String response = httpPost(MODIFY_URL, paramNames, paramVals, userId);
/* 423 */         if (logger.isLoggable(Level.INFO))
/*     */         {
/* 425 */           logger.info("Called " + MODIFY_URL + " with user name " + userId + " and received response " + response);
/*     */         }
/*     */         
/*     */         try {
/* 429 */           InputSource inStream = new InputSource();
/*     */           
/* 431 */           inStream.setCharacterStream(new StringReader(response));
/* 432 */           DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
/* 433 */           DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
/*     */           
/* 435 */           Document doc = dBuilder.parse(inStream);
/* 436 */           doc.getDocumentElement().normalize();
/*     */           
/* 438 */           logger.log(Level.INFO, "Root element :" + doc.getDocumentElement().getNodeName());
/* 439 */           NodeList nList = doc.getElementsByTagName("user");
/* 440 */           for (int temp = 0; temp < nList.getLength(); temp++) {
/*     */             
/* 442 */             Node nNode = nList.item(temp);
/* 443 */             if (nNode.getNodeType() == 1) {
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 448 */               Element eElement = (Element)nNode;
/* 449 */               String uname = getTagValue("username", eElement);
/* 450 */               logger.log(Level.INFO, "UserName : " + uname);
/* 451 */               String authid = getTagValue("authid", eElement);
/* 452 */               logger.log(Level.INFO, "Auth Id : " + authid);
/* 453 */               String password = getTagValue("passwd", eElement);
/* 454 */               logger.log(Level.INFO, "Password : " + password);
/* 455 */               String services = getTagValue("servicebundle", eElement);
/* 456 */               logger.log(Level.INFO, "Service bundle : " + services);
/*     */               
/* 458 */               EigcClient old = getClientWithId(uname);
/* 459 */               if (old != null) {
/* 460 */                 updateAccount(uname, password, services, expiration, old.getAccountName());
/*     */               } else {
/* 462 */                 updateAccount(uname, password, services, expiration, "");
/*     */               } 
/*     */             } 
/*     */           } 
/* 466 */         } catch (Exception e) {
/*     */           
/* 468 */           logger.log(Level.WARNING, e.getMessage());
/*     */         }
/*     */       
/* 471 */       } catch (Exception e) {
/*     */         
/* 473 */         logger.log(Level.WARNING, "Problem calling " + CREATE_URL + " with user name " + userId, e);
/*     */       }
/*     */     
/* 476 */     } catch (Exception iox) {
/*     */       
/* 478 */       logger.log(Level.INFO, iox.getMessage(), iox);
/*     */     } 
/* 480 */     return "Failed to modify " + userId;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String httpPost(String urlStr, String[] paramName, String[] paramVal, String userName) throws Exception {
/*     */     StringBuilder sb;
/* 486 */     URL url = new URL(urlStr + userName);
/* 487 */     setEigcHttpsOverride();
/* 488 */     HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
/*     */ 
/*     */     
/*     */     try {
/* 492 */       conn.setRequestMethod("POST");
/* 493 */       conn.setDoOutput(true);
/* 494 */       conn.setDoInput(true);
/* 495 */       conn.setUseCaches(false);
/* 496 */       conn.setAllowUserInteraction(false);
/* 497 */       conn.setRequestProperty("Authorization", "Digest " + EIGC_PASSWORD);
/* 498 */       conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
/*     */       
/* 500 */       OutputStream out = conn.getOutputStream();
/* 501 */       Writer writer = new OutputStreamWriter(out, "UTF-8");
/*     */       
/*     */       try {
/* 504 */         for (int i = 0; i < paramName.length; i++)
/*     */         {
/* 506 */           writer.write(paramName[i]);
/* 507 */           writer.write("=");
/* 508 */           logger.log(Level.INFO, "Sending " + paramName[i] + "=" + paramVal[i]);
/* 509 */           writer.write(URLEncoder.encode(paramVal[i], "UTF-8"));
/*     */           
/* 511 */           writer.write("&");
/*     */         }
/*     */       
/*     */       } finally {
/*     */         
/* 516 */         IoUtilities.closeClosable(writer);
/* 517 */         IoUtilities.closeClosable(out);
/*     */       } 
/* 519 */       if (conn.getResponseCode() != HttpResponseStatus.OK.getStatusCode())
/*     */       {
/* 521 */         throw new IOException(conn.getResponseMessage());
/*     */       }
/*     */       
/* 524 */       BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
/*     */       
/*     */       try {
/* 527 */         sb = new StringBuilder();
/*     */         String line;
/* 529 */         while ((line = rd.readLine()) != null)
/*     */         {
/* 531 */           sb.append(line);
/*     */         }
/*     */       }
/*     */       finally {
/*     */         
/* 536 */         IoUtilities.closeClosable(rd);
/*     */       }
/*     */     
/*     */     } finally {
/*     */       
/* 541 */       IoUtilities.closeHttpURLConnection(conn);
/*     */     } 
/* 543 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String httpDelete(String urlStr, String[] paramName, String[] paramVal, String userName) throws Exception {
/*     */     StringBuilder sb;
/* 549 */     URL url = new URL(urlStr + userName);
/* 550 */     setEigcHttpsOverride();
/* 551 */     HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
/*     */ 
/*     */     
/*     */     try {
/* 555 */       conn.setRequestMethod("DELETE");
/* 556 */       conn.setDoOutput(true);
/* 557 */       conn.setDoInput(true);
/* 558 */       conn.setUseCaches(false);
/* 559 */       conn.setAllowUserInteraction(false);
/* 560 */       conn.setRequestProperty("Authorization", "Digest " + EIGC_PASSWORD);
/* 561 */       conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
/*     */       
/* 563 */       OutputStream out = conn.getOutputStream();
/* 564 */       Writer writer = new OutputStreamWriter(out, "UTF-8");
/*     */       
/*     */       try {
/* 567 */         for (int i = 0; i < paramName.length; i++)
/*     */         {
/* 569 */           writer.write(paramName[i]);
/* 570 */           writer.write("=");
/* 571 */           logger.log(Level.INFO, "Sending " + paramName[i] + "=" + paramVal[i]);
/* 572 */           writer.write(URLEncoder.encode(paramVal[i], "UTF-8"));
/*     */           
/* 574 */           writer.write("&");
/*     */         }
/*     */       
/*     */       } finally {
/*     */         
/* 579 */         IoUtilities.closeClosable(writer);
/* 580 */         IoUtilities.closeClosable(out);
/*     */       } 
/* 582 */       if (conn.getResponseCode() != HttpResponseStatus.OK.getStatusCode())
/*     */       {
/* 584 */         throw new IOException(conn.getResponseMessage());
/*     */       }
/*     */       
/* 587 */       BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
/*     */       
/*     */       try {
/* 590 */         sb = new StringBuilder();
/*     */         String line;
/* 592 */         while ((line = rd.readLine()) != null)
/*     */         {
/* 594 */           sb.append(line);
/*     */         }
/*     */       }
/*     */       finally {
/*     */         
/* 599 */         IoUtilities.closeClosable(rd);
/*     */       }
/*     */     
/*     */     } finally {
/*     */       
/* 604 */       IoUtilities.closeHttpURLConnection(conn);
/*     */     } 
/* 606 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String httpGet(String urlStr, String userName) throws IOException {
/* 611 */     URL url = new URL(urlStr + userName);
/* 612 */     setEigcHttpsOverride();
/* 613 */     HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
/* 614 */     conn.setRequestProperty("Authorization", EIGC_PASSWORD);
/* 615 */     if (conn.getResponseCode() != HttpResponseStatus.OK.getStatusCode())
/*     */     {
/* 617 */       throw new IOException(conn.getResponseMessage());
/*     */     }
/*     */     
/* 620 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 622 */     try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
/*     */       String line;
/*     */       
/* 625 */       while ((line = rd.readLine()) != null)
/*     */       {
/* 627 */         sb.append(line);
/*     */       }
/* 629 */       rd.close();
/*     */     } 
/*     */     
/* 632 */     conn.disconnect();
/* 633 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String addPlayer(String playerName) {
/* 640 */     EigcClient found = getClientForPlayer(playerName);
/* 641 */     if (found != null) {
/*     */       
/* 643 */       logger.log(Level.INFO, playerName + " already in use: " + found.getClientId());
/* 644 */       return found.getClientId();
/*     */     } 
/*     */     
/* 647 */     for (EigcClient gcc : EIGC_CLIENTS) {
/*     */       
/* 649 */       if (gcc.getAccountName().equalsIgnoreCase(playerName))
/*     */       {
/* 651 */         return setClientUsed(gcc, playerName, "found unused reserved client");
/*     */       }
/*     */     } 
/*     */     
/* 655 */     EigcClient client = null;
/* 656 */     for (EigcClient gcc : EIGC_CLIENTS) {
/*     */       
/* 658 */       if (!gcc.isUsed())
/*     */       {
/* 660 */         if (gcc.getAccountName().length() <= 0) {
/*     */           
/* 662 */           client = gcc;
/*     */           break;
/*     */         } 
/*     */       }
/*     */     } 
/* 667 */     if (client != null) {
/*     */ 
/*     */       
/* 670 */       EIGC_CLIENTS.remove(client);
/* 671 */       EIGC_CLIENTS.add(client);
/* 672 */       return setClientUsed(client, playerName, "found unused free client");
/*     */     } 
/*     */     
/* 675 */     provisionAccounts(accountsToProvision, false);
/* 676 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   private static final String setClientUsed(EigcClient client, String playerName, String reason) {
/* 681 */     client.setPlayerName(playerName.toLowerCase(), reason);
/* 682 */     client.setAccountName(playerName.toLowerCase());
/* 683 */     return client.getClientId();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final EigcClient getClientForPlayer(String playerName) {
/* 688 */     String nameSearched = LoginHandler.raiseFirstLetter(playerName);
/* 689 */     boolean mustTrim = (playerName.indexOf(" ") > 0);
/* 690 */     if (mustTrim) {
/*     */       
/* 692 */       nameSearched = playerName.substring(0, playerName.indexOf(" "));
/* 693 */       logger.log(Level.INFO, "Trimmed " + playerName + " to " + nameSearched);
/*     */     } 
/* 695 */     for (EigcClient client : EIGC_CLIENTS) {
/*     */       
/* 697 */       if (client.getPlayerName().equalsIgnoreCase(nameSearched))
/* 698 */         return client; 
/*     */     } 
/* 700 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final EigcClient getReservedClientForPlayer(String playerName) {
/* 705 */     String nameSearched = LoginHandler.raiseFirstLetter(playerName);
/* 706 */     boolean mustTrim = (playerName.indexOf(" ") > 0);
/* 707 */     if (mustTrim) {
/*     */       
/* 709 */       nameSearched = playerName.substring(0, playerName.indexOf(" "));
/* 710 */       logger.log(Level.INFO, "Trimmed " + playerName + " to " + nameSearched);
/*     */     } 
/* 712 */     for (EigcClient client : EIGC_CLIENTS) {
/*     */       
/* 714 */       if (client.getAccountName().equalsIgnoreCase(nameSearched))
/* 715 */         return client; 
/*     */     } 
/* 717 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final EigcClient removePlayer(String playerName) {
/* 722 */     EigcClient client = getClientForPlayer(playerName);
/* 723 */     if (client != null) {
/*     */       
/* 725 */       client.setPlayerName("", "removed");
/* 726 */       if (client.getExpiration() == Long.MAX_VALUE || client.getExpiration() < System.currentTimeMillis())
/*     */       {
/* 728 */         client.setAccountName("");
/*     */       }
/*     */     } 
/* 731 */     return client;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void sendAllClientInfo(Communicator comm) {
/* 736 */     for (EigcClient entry : EIGC_CLIENTS)
/*     */     {
/* 738 */       comm.sendNormalServerMessage("ClientId: " + entry.getClientId() + ": user: " + entry.getPlayerName() + " occupied=" + entry
/* 739 */           .isUsed() + " accountname=" + entry.getAccountName() + " secs since last use=" + (
/* 740 */           entry.isUsed() ? entry.timeSinceLastUse() : "N/A"));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final EigcClient transferPlayer(String playerName) {
/* 746 */     final EigcClient client = getReservedClientForPlayer(playerName);
/* 747 */     if (client != null) {
/*     */       
/* 749 */       if (client.getExpiration() < Long.MAX_VALUE && client.getExpiration() > System.currentTimeMillis()) {
/* 750 */         return client;
/*     */       }
/*     */       
/* 753 */       logger.log(Level.INFO, "Setting expired reserved client to unused at server transfer. This should be detected earlier.");
/*     */       
/* 755 */       client.setAccountName("");
/* 756 */       (new Thread()
/*     */         {
/*     */           
/*     */           public void run()
/*     */           {
/* 761 */             Eigc.modifyUser(client.getClientId(), "proximity", Long.MAX_VALUE);
/*     */           }
/* 763 */         }).start();
/*     */     } 
/*     */     
/* 766 */     return null;
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
/*     */ 
/*     */   
/*     */   public static final void updateAccount(String eigcUserId, String clientPass, String services, long expirationTime, String accountName) {
/* 781 */     EigcClient oldClient = getClientWithId(eigcUserId);
/* 782 */     if (oldClient == null) {
/* 783 */       createAccount(eigcUserId, clientPass, services, expirationTime, accountName.toLowerCase());
/*     */     } else {
/*     */       
/* 786 */       oldClient.setPassword(clientPass);
/* 787 */       oldClient.setServiceBundle(services);
/* 788 */       oldClient.setExpiration(expirationTime);
/* 789 */       oldClient.setAccountName(accountName.toLowerCase());
/* 790 */       oldClient.updateAccount();
/* 791 */       Players.getInstance().updateEigcInfo(oldClient);
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
/*     */   
/*     */   public static final void createAccount(String eigcUserId, String clientPass, String services, long expirationTime, String accountName) {
/* 806 */     EigcClient eigclient = new EigcClient(eigcUserId, clientPass, services, expirationTime, accountName.toLowerCase());
/* 807 */     EIGC_CLIENTS.add(eigclient);
/*     */     
/* 809 */     Connection dbcon = null;
/* 810 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 813 */       dbcon = DbConnector.getLoginDbCon();
/* 814 */       ps = dbcon.prepareStatement("INSERT INTO EIGC(USERNAME,PASSWORD,SERVICEBUNDLE,EXPIRATION,EMAIL) VALUES (?,?,?,?,?)");
/* 815 */       ps.setString(1, eigcUserId);
/* 816 */       ps.setString(2, clientPass);
/* 817 */       ps.setString(3, services);
/* 818 */       ps.setLong(4, expirationTime);
/* 819 */       ps.setString(5, accountName.toLowerCase());
/* 820 */       ps.executeUpdate();
/* 821 */       logger.log(Level.INFO, "Successfully saved " + eigcUserId);
/*     */     }
/* 823 */     catch (SQLException sqx) {
/*     */       
/* 825 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 829 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 830 */       DbConnector.returnConnection(dbcon);
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
/*     */   public static final EigcClient getClientWithId(String eigcUserId) {
/* 843 */     for (EigcClient entry : EIGC_CLIENTS) {
/*     */       
/* 845 */       if (entry.getClientId().equalsIgnoreCase(eigcUserId))
/* 846 */         return entry; 
/*     */     } 
/* 848 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void removeClientWithId(String eigcUserId) {
/* 859 */     EigcClient toRemove = null;
/* 860 */     for (EigcClient c : EIGC_CLIENTS) {
/*     */       
/* 862 */       if (c.getClientId().equalsIgnoreCase(eigcUserId)) {
/*     */         
/* 864 */         toRemove = c;
/*     */         break;
/*     */       } 
/*     */     } 
/* 868 */     if (toRemove != null) {
/* 869 */       EIGC_CLIENTS.remove(toRemove);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void deleteAccount(String eigcUserId) {
/* 878 */     removeClientWithId(eigcUserId);
/* 879 */     Connection dbcon = null;
/* 880 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 883 */       dbcon = DbConnector.getLoginDbCon();
/* 884 */       ps = dbcon.prepareStatement("DELETE FROM EIGC WHERE USERNAME=?");
/* 885 */       ps.setString(1, eigcUserId);
/* 886 */       ps.executeUpdate();
/*     */     }
/* 888 */     catch (SQLException sqx) {
/*     */       
/* 890 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 894 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 895 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\Eigc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */