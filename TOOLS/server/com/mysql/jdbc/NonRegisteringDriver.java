/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.net.URLDecoder;
/*     */ import java.sql.Connection;
/*     */ import java.sql.Driver;
/*     */ import java.sql.DriverPropertyInfo;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NonRegisteringDriver
/*     */   implements Driver
/*     */ {
/*     */   private static final String REPLICATION_URL_PREFIX = "jdbc:mysql:replication://";
/*     */   private static final String URL_PREFIX = "jdbc:mysql://";
/*     */   private static final String MXJ_URL_PREFIX = "jdbc:mysql:mxj://";
/*     */   private static final String LOADBALANCE_URL_PREFIX = "jdbc:mysql:loadbalance://";
/*     */   public static final String DBNAME_PROPERTY_KEY = "DBNAME";
/*     */   public static final boolean DEBUG = false;
/*     */   public static final int HOST_NAME_INDEX = 0;
/*     */   public static final String HOST_PROPERTY_KEY = "HOST";
/*     */   public static final String NUM_HOSTS_PROPERTY_KEY = "NUM_HOSTS";
/*     */   public static final String PASSWORD_PROPERTY_KEY = "password";
/*     */   public static final int PORT_NUMBER_INDEX = 1;
/*     */   public static final String PORT_PROPERTY_KEY = "PORT";
/*     */   public static final String PROPERTIES_TRANSFORM_KEY = "propertiesTransform";
/*     */   public static final boolean TRACE = false;
/*     */   public static final String USE_CONFIG_PROPERTY_KEY = "useConfigs";
/*     */   public static final String USER_PROPERTY_KEY = "user";
/*     */   
/*     */   static int getMajorVersionInternal() {
/* 130 */     return safeIntParse("5");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int getMinorVersionInternal() {
/* 139 */     return safeIntParse("1");
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static String[] parseHostPortPair(String hostPortPair) throws SQLException {
/* 158 */     int portIndex = hostPortPair.indexOf(":");
/*     */     
/* 160 */     String[] splitValues = new String[2];
/*     */     
/* 162 */     String hostname = null;
/*     */     
/* 164 */     if (portIndex != -1) {
/* 165 */       if (portIndex + 1 < hostPortPair.length()) {
/* 166 */         String portAsString = hostPortPair.substring(portIndex + 1);
/* 167 */         hostname = hostPortPair.substring(0, portIndex);
/*     */         
/* 169 */         splitValues[0] = hostname;
/*     */         
/* 171 */         splitValues[1] = portAsString;
/*     */       } else {
/* 173 */         throw SQLError.createSQLException(Messages.getString("NonRegisteringDriver.37"), "01S00", null);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 178 */       splitValues[0] = hostPortPair;
/* 179 */       splitValues[1] = null;
/*     */     } 
/*     */     
/* 182 */     return splitValues;
/*     */   }
/*     */   
/*     */   private static int safeIntParse(String intAsString) {
/*     */     try {
/* 187 */       return Integer.parseInt(intAsString);
/* 188 */     } catch (NumberFormatException nfe) {
/* 189 */       return 0;
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
/*     */   public boolean acceptsURL(String url) throws SQLException {
/* 219 */     return (parseURL(url, null) != null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection connect(String url, Properties info) throws SQLException {
/* 268 */     if (url != null) {
/* 269 */       if (StringUtils.startsWithIgnoreCase(url, "jdbc:mysql:loadbalance://"))
/* 270 */         return connectLoadBalanced(url, info); 
/* 271 */       if (StringUtils.startsWithIgnoreCase(url, "jdbc:mysql:replication://"))
/*     */       {
/* 273 */         return connectReplicationConnection(url, info);
/*     */       }
/*     */     } 
/*     */     
/* 277 */     Properties props = null;
/*     */     
/* 279 */     if ((props = parseURL(url, info)) == null) {
/* 280 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 284 */       Connection newConn = ConnectionImpl.getInstance(host(props), port(props), props, database(props), url);
/*     */ 
/*     */       
/* 287 */       return newConn;
/* 288 */     } catch (SQLException sqlEx) {
/*     */ 
/*     */       
/* 291 */       throw sqlEx;
/* 292 */     } catch (Exception ex) {
/* 293 */       SQLException sqlEx = SQLError.createSQLException(Messages.getString("NonRegisteringDriver.17") + ex.toString() + Messages.getString("NonRegisteringDriver.18"), "08001", null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 299 */       sqlEx.initCause(ex);
/*     */       
/* 301 */       throw sqlEx;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Connection connectLoadBalanced(String url, Properties info) throws SQLException {
/* 307 */     Properties parsedProps = parseURL(url, info);
/*     */ 
/*     */     
/* 310 */     parsedProps.remove("roundRobinLoadBalance");
/*     */     
/* 312 */     if (parsedProps == null) {
/* 313 */       return null;
/*     */     }
/*     */     
/* 316 */     int numHosts = Integer.parseInt(parsedProps.getProperty("NUM_HOSTS"));
/*     */     
/* 318 */     List hostList = new ArrayList();
/*     */     
/* 320 */     for (int i = 0; i < numHosts; i++) {
/* 321 */       int index = i + 1;
/*     */       
/* 323 */       hostList.add(parsedProps.getProperty("HOST." + index) + ":" + parsedProps.getProperty("PORT." + index));
/*     */     } 
/*     */ 
/*     */     
/* 327 */     LoadBalancingConnectionProxy proxyBal = new LoadBalancingConnectionProxy(hostList, parsedProps);
/*     */ 
/*     */     
/* 330 */     return (Connection)Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { Connection.class }, proxyBal);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Connection connectReplicationConnection(String url, Properties info) throws SQLException {
/* 337 */     Properties parsedProps = parseURL(url, info);
/*     */     
/* 339 */     if (parsedProps == null) {
/* 340 */       return null;
/*     */     }
/*     */     
/* 343 */     Properties masterProps = (Properties)parsedProps.clone();
/* 344 */     Properties slavesProps = (Properties)parsedProps.clone();
/*     */ 
/*     */ 
/*     */     
/* 348 */     slavesProps.setProperty("com.mysql.jdbc.ReplicationConnection.isSlave", "true");
/*     */ 
/*     */     
/* 351 */     int numHosts = Integer.parseInt(parsedProps.getProperty("NUM_HOSTS"));
/*     */     
/* 353 */     if (numHosts < 2) {
/* 354 */       throw SQLError.createSQLException("Must specify at least one slave host to connect to for master/slave replication load-balancing functionality", "01S00", null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 360 */     for (int i = 1; i < numHosts; i++) {
/* 361 */       int index = i + 1;
/*     */       
/* 363 */       masterProps.remove("HOST." + index);
/* 364 */       masterProps.remove("PORT." + index);
/*     */       
/* 366 */       slavesProps.setProperty("HOST." + i, parsedProps.getProperty("HOST." + index));
/* 367 */       slavesProps.setProperty("PORT." + i, parsedProps.getProperty("PORT." + index));
/*     */     } 
/*     */     
/* 370 */     masterProps.setProperty("NUM_HOSTS", "1");
/* 371 */     slavesProps.remove("HOST." + numHosts);
/* 372 */     slavesProps.remove("PORT." + numHosts);
/* 373 */     slavesProps.setProperty("NUM_HOSTS", String.valueOf(numHosts - 1));
/* 374 */     slavesProps.setProperty("HOST", slavesProps.getProperty("HOST.1"));
/* 375 */     slavesProps.setProperty("PORT", slavesProps.getProperty("PORT.1"));
/*     */     
/* 377 */     return new ReplicationConnection(masterProps, slavesProps);
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
/*     */   public String database(Properties props) {
/* 389 */     return props.getProperty("DBNAME");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMajorVersion() {
/* 398 */     return getMajorVersionInternal();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMinorVersion() {
/* 407 */     return getMinorVersionInternal();
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
/*     */   public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
/* 438 */     if (info == null) {
/* 439 */       info = new Properties();
/*     */     }
/*     */     
/* 442 */     if (url != null && url.startsWith("jdbc:mysql://")) {
/* 443 */       info = parseURL(url, info);
/*     */     }
/*     */     
/* 446 */     DriverPropertyInfo hostProp = new DriverPropertyInfo("HOST", info.getProperty("HOST"));
/*     */     
/* 448 */     hostProp.required = true;
/* 449 */     hostProp.description = Messages.getString("NonRegisteringDriver.3");
/*     */     
/* 451 */     DriverPropertyInfo portProp = new DriverPropertyInfo("PORT", info.getProperty("PORT", "3306"));
/*     */     
/* 453 */     portProp.required = false;
/* 454 */     portProp.description = Messages.getString("NonRegisteringDriver.7");
/*     */     
/* 456 */     DriverPropertyInfo dbProp = new DriverPropertyInfo("DBNAME", info.getProperty("DBNAME"));
/*     */     
/* 458 */     dbProp.required = false;
/* 459 */     dbProp.description = "Database name";
/*     */     
/* 461 */     DriverPropertyInfo userProp = new DriverPropertyInfo("user", info.getProperty("user"));
/*     */     
/* 463 */     userProp.required = true;
/* 464 */     userProp.description = Messages.getString("NonRegisteringDriver.13");
/*     */     
/* 466 */     DriverPropertyInfo passwordProp = new DriverPropertyInfo("password", info.getProperty("password"));
/*     */ 
/*     */     
/* 469 */     passwordProp.required = true;
/* 470 */     passwordProp.description = Messages.getString("NonRegisteringDriver.16");
/*     */ 
/*     */     
/* 473 */     DriverPropertyInfo[] dpi = ConnectionPropertiesImpl.exposeAsDriverPropertyInfo(info, 5);
/*     */ 
/*     */     
/* 476 */     dpi[0] = hostProp;
/* 477 */     dpi[1] = portProp;
/* 478 */     dpi[2] = dbProp;
/* 479 */     dpi[3] = userProp;
/* 480 */     dpi[4] = passwordProp;
/*     */     
/* 482 */     return dpi;
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
/*     */ 
/*     */   
/*     */   public String host(Properties props) {
/* 499 */     return props.getProperty("HOST", "localhost");
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
/*     */   
/*     */   public boolean jdbcCompliant() {
/* 515 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Properties parseURL(String url, Properties defaults) throws SQLException {
/* 520 */     Properties urlProps = (defaults != null) ? new Properties(defaults) : new Properties();
/*     */ 
/*     */     
/* 523 */     if (url == null) {
/* 524 */       return null;
/*     */     }
/*     */     
/* 527 */     if (!StringUtils.startsWithIgnoreCase(url, "jdbc:mysql://") && !StringUtils.startsWithIgnoreCase(url, "jdbc:mysql:mxj://") && !StringUtils.startsWithIgnoreCase(url, "jdbc:mysql:loadbalance://") && !StringUtils.startsWithIgnoreCase(url, "jdbc:mysql:replication://"))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 534 */       return null;
/*     */     }
/*     */     
/* 537 */     int beginningOfSlashes = url.indexOf("//");
/*     */     
/* 539 */     if (StringUtils.startsWithIgnoreCase(url, "jdbc:mysql:mxj://"))
/*     */     {
/* 541 */       urlProps.setProperty("socketFactory", "com.mysql.management.driverlaunched.ServerLauncherSocketFactory");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 550 */     int index = url.indexOf("?");
/*     */     
/* 552 */     if (index != -1) {
/* 553 */       String paramString = url.substring(index + 1, url.length());
/* 554 */       url = url.substring(0, index);
/*     */       
/* 556 */       StringTokenizer queryParams = new StringTokenizer(paramString, "&");
/*     */       
/* 558 */       while (queryParams.hasMoreTokens()) {
/* 559 */         String parameterValuePair = queryParams.nextToken();
/*     */         
/* 561 */         int indexOfEquals = StringUtils.indexOfIgnoreCase(0, parameterValuePair, "=");
/*     */ 
/*     */         
/* 564 */         String parameter = null;
/* 565 */         String value = null;
/*     */         
/* 567 */         if (indexOfEquals != -1) {
/* 568 */           parameter = parameterValuePair.substring(0, indexOfEquals);
/*     */           
/* 570 */           if (indexOfEquals + 1 < parameterValuePair.length()) {
/* 571 */             value = parameterValuePair.substring(indexOfEquals + 1);
/*     */           }
/*     */         } 
/*     */         
/* 575 */         if (value != null && value.length() > 0 && parameter != null && parameter.length() > 0) {
/*     */           
/*     */           try {
/* 578 */             urlProps.put(parameter, URLDecoder.decode(value, "UTF-8"));
/*     */           }
/* 580 */           catch (UnsupportedEncodingException badEncoding) {
/*     */             
/* 582 */             urlProps.put(parameter, URLDecoder.decode(value));
/* 583 */           } catch (NoSuchMethodError nsme) {
/*     */             
/* 585 */             urlProps.put(parameter, URLDecoder.decode(value));
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 591 */     url = url.substring(beginningOfSlashes + 2);
/*     */     
/* 593 */     String hostStuff = null;
/*     */     
/* 595 */     int slashIndex = url.indexOf("/");
/*     */     
/* 597 */     if (slashIndex != -1) {
/* 598 */       hostStuff = url.substring(0, slashIndex);
/*     */       
/* 600 */       if (slashIndex + 1 < url.length()) {
/* 601 */         urlProps.put("DBNAME", url.substring(slashIndex + 1, url.length()));
/*     */       }
/*     */     } else {
/*     */       
/* 605 */       hostStuff = url;
/*     */     } 
/*     */     
/* 608 */     int numHosts = 0;
/*     */     
/* 610 */     if (hostStuff != null && hostStuff.trim().length() > 0) {
/* 611 */       StringTokenizer st = new StringTokenizer(hostStuff, ",");
/*     */       
/* 613 */       while (st.hasMoreTokens()) {
/* 614 */         numHosts++;
/*     */         
/* 616 */         String[] hostPortPair = parseHostPortPair(st.nextToken());
/*     */         
/* 618 */         if (hostPortPair[0] != null && hostPortPair[0].trim().length() > 0) {
/* 619 */           urlProps.setProperty("HOST." + numHosts, hostPortPair[0]);
/*     */         } else {
/* 621 */           urlProps.setProperty("HOST." + numHosts, "localhost");
/*     */         } 
/*     */         
/* 624 */         if (hostPortPair[1] != null) {
/* 625 */           urlProps.setProperty("PORT." + numHosts, hostPortPair[1]); continue;
/*     */         } 
/* 627 */         urlProps.setProperty("PORT." + numHosts, "3306");
/*     */       } 
/*     */     } else {
/*     */       
/* 631 */       numHosts = 1;
/* 632 */       urlProps.setProperty("HOST.1", "localhost");
/* 633 */       urlProps.setProperty("PORT.1", "3306");
/*     */     } 
/*     */     
/* 636 */     urlProps.setProperty("NUM_HOSTS", String.valueOf(numHosts));
/* 637 */     urlProps.setProperty("HOST", urlProps.getProperty("HOST.1"));
/* 638 */     urlProps.setProperty("PORT", urlProps.getProperty("PORT.1"));
/*     */     
/* 640 */     String propertiesTransformClassName = urlProps.getProperty("propertiesTransform");
/*     */ 
/*     */     
/* 643 */     if (propertiesTransformClassName != null) {
/*     */       try {
/* 645 */         ConnectionPropertiesTransform propTransformer = (ConnectionPropertiesTransform)Class.forName(propertiesTransformClassName).newInstance();
/*     */ 
/*     */         
/* 648 */         urlProps = propTransformer.transformProperties(urlProps);
/* 649 */       } catch (InstantiationException e) {
/* 650 */         throw SQLError.createSQLException("Unable to create properties transform instance '" + propertiesTransformClassName + "' due to underlying exception: " + e.toString(), "01S00", null);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 656 */       catch (IllegalAccessException e) {
/* 657 */         throw SQLError.createSQLException("Unable to create properties transform instance '" + propertiesTransformClassName + "' due to underlying exception: " + e.toString(), "01S00", null);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 663 */       catch (ClassNotFoundException e) {
/* 664 */         throw SQLError.createSQLException("Unable to create properties transform instance '" + propertiesTransformClassName + "' due to underlying exception: " + e.toString(), "01S00", null);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 673 */     if (Util.isColdFusion() && urlProps.getProperty("autoConfigureForColdFusion", "true").equalsIgnoreCase("true")) {
/*     */       
/* 675 */       String configs = urlProps.getProperty("useConfigs");
/*     */       
/* 677 */       StringBuffer newConfigs = new StringBuffer();
/*     */       
/* 679 */       if (configs != null) {
/* 680 */         newConfigs.append(configs);
/* 681 */         newConfigs.append(",");
/*     */       } 
/*     */       
/* 684 */       newConfigs.append("coldFusion");
/*     */       
/* 686 */       urlProps.setProperty("useConfigs", newConfigs.toString());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 692 */     String configNames = null;
/*     */     
/* 694 */     if (defaults != null) {
/* 695 */       configNames = defaults.getProperty("useConfigs");
/*     */     }
/*     */     
/* 698 */     if (configNames == null) {
/* 699 */       configNames = urlProps.getProperty("useConfigs");
/*     */     }
/*     */     
/* 702 */     if (configNames != null) {
/* 703 */       List splitNames = StringUtils.split(configNames, ",", true);
/*     */       
/* 705 */       Properties configProps = new Properties();
/*     */       
/* 707 */       Iterator namesIter = splitNames.iterator();
/*     */       
/* 709 */       while (namesIter.hasNext()) {
/* 710 */         String configName = namesIter.next();
/*     */         
/*     */         try {
/* 713 */           InputStream configAsStream = getClass().getResourceAsStream("configs/" + configName + ".properties");
/*     */ 
/*     */ 
/*     */           
/* 717 */           if (configAsStream == null) {
/* 718 */             throw SQLError.createSQLException("Can't find configuration template named '" + configName + "'", "01S00", null);
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 724 */           configProps.load(configAsStream);
/* 725 */         } catch (IOException ioEx) {
/* 726 */           SQLException sqlEx = SQLError.createSQLException("Unable to load configuration template '" + configName + "' due to underlying IOException: " + ioEx, "01S00", null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 732 */           sqlEx.initCause(ioEx);
/*     */           
/* 734 */           throw sqlEx;
/*     */         } 
/*     */       } 
/*     */       
/* 738 */       Iterator propsIter = urlProps.keySet().iterator();
/*     */       
/* 740 */       while (propsIter.hasNext()) {
/* 741 */         String key = propsIter.next().toString();
/* 742 */         String property = urlProps.getProperty(key);
/* 743 */         configProps.setProperty(key, property);
/*     */       } 
/*     */       
/* 746 */       urlProps = configProps;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 751 */     if (defaults != null) {
/* 752 */       Iterator propsIter = defaults.keySet().iterator();
/*     */       
/* 754 */       while (propsIter.hasNext()) {
/* 755 */         String key = propsIter.next().toString();
/* 756 */         if (!key.equals("NUM_HOSTS")) {
/* 757 */           String property = defaults.getProperty(key);
/* 758 */           urlProps.setProperty(key, property);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 763 */     return urlProps;
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
/*     */   public int port(Properties props) {
/* 775 */     return Integer.parseInt(props.getProperty("PORT", "3306"));
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
/*     */   public String property(String name, Properties props) {
/* 789 */     return props.getProperty(name);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\NonRegisteringDriver.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */