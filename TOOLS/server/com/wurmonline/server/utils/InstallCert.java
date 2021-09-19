/*     */ package com.wurmonline.server.utils;
/*     */ 
/*     */ import com.wurmonline.shared.util.StringUtilities;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import javax.net.ssl.X509TrustManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class InstallCert
/*     */ {
/*  73 */   private static final Logger logger = Logger.getLogger(InstallCert.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void installCert(String host, int port, String password, String keystoreName) throws Exception {
/*  87 */     char[] passphrase = password.toCharArray();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  92 */     char SEP = File.separatorChar;
/*  93 */     File dir = new File(System.getProperty("java.home") + SEP + "lib" + SEP + "security");
/*  94 */     File file = new File(dir, keystoreName);
/*  95 */     if (!file.isFile())
/*     */     {
/*  97 */       file = new File(dir, "cacerts");
/*     */     }
/*     */     
/* 100 */     logger.log(Level.INFO, "Loading KeyStore " + file + "...");
/* 101 */     InputStream in = new FileInputStream(file);
/* 102 */     KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
/* 103 */     ks.load(in, passphrase);
/* 104 */     in.close();
/*     */     
/*     */     try {
/* 107 */       logger.log(Level.INFO, "Loaded Keystore size: " + ks.size());
/*     */     }
/* 109 */     catch (KeyStoreException kse) {
/*     */       
/* 111 */       logger.log(Level.INFO, "Keystore has not been initalized");
/*     */     } 
/* 113 */     SSLContext context = SSLContext.getInstance("TLS");
/* 114 */     TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
/* 115 */     tmf.init(ks);
/* 116 */     X509TrustManager defaultTrustManager = (X509TrustManager)tmf.getTrustManagers()[0];
/* 117 */     SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
/* 118 */     context.init(null, new TrustManager[] { tm }, null);
/*     */     
/* 120 */     SSLSocketFactory factory = context.getSocketFactory();
/*     */     
/* 122 */     logger.log(Level.INFO, "Opening connection to " + host + ":" + port + "...");
/* 123 */     SSLSocket socket = (SSLSocket)factory.createSocket(host, port);
/* 124 */     socket.setSoTimeout(10000);
/*     */     
/*     */     try {
/* 127 */       logger.log(Level.INFO, "Starting SSL handshake...");
/* 128 */       socket.startHandshake();
/* 129 */       socket.close();
/* 130 */       logger.log(Level.INFO, "No errors, certificate is already trusted");
/*     */       
/*     */       return;
/* 133 */     } catch (SSLException e) {
/*     */       
/* 135 */       logger.log(Level.INFO, "Received SSLException. Untrusted cert. Installing.");
/*     */ 
/*     */       
/* 138 */       X509Certificate[] chain = tm.chain;
/* 139 */       if (chain == null) {
/*     */         
/* 141 */         logger.log(Level.INFO, "Could not obtain server certificate chain");
/*     */         
/*     */         return;
/*     */       } 
/* 145 */       logger.log(Level.INFO, "Server sent " + chain.length + " certificate(s):");
/* 146 */       MessageDigest sha1 = MessageDigest.getInstance("SHA1");
/* 147 */       MessageDigest md5 = MessageDigest.getInstance("MD5");
/* 148 */       for (int i = 0; i < chain.length; i++) {
/*     */         
/* 150 */         X509Certificate x509Certificate = chain[i];
/* 151 */         logger.log(Level.INFO, " " + (i + 1) + " Subject " + x509Certificate.getSubjectDN());
/* 152 */         logger.log(Level.INFO, "   Issuer  " + x509Certificate.getIssuerDN());
/* 153 */         sha1.update(x509Certificate.getEncoded());
/* 154 */         logger.log(Level.INFO, "   sha1    " + StringUtilities.toHexString(sha1.digest()));
/* 155 */         md5.update(x509Certificate.getEncoded());
/* 156 */         logger.log(Level.INFO, "   md5     " + StringUtilities.toHexString(md5.digest()));
/*     */       } 
/*     */       
/* 159 */       int k = chain.length - 1;
/*     */       
/* 161 */       X509Certificate cert = chain[k];
/* 162 */       String alias = host + "-" + (k + 1);
/* 163 */       ks.setCertificateEntry(alias, cert);
/*     */       
/* 165 */       OutputStream out = new FileOutputStream(file);
/* 166 */       ks.store(out, passphrase);
/* 167 */       out.close();
/*     */       
/* 169 */       logger.log(Level.INFO, cert.toString());
/* 170 */       logger.log(Level.INFO, "Added certificate to keystore '" + file.getAbsolutePath() + "' using alias '" + alias + "'");
/*     */       return;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class SavingTrustManager
/*     */     implements X509TrustManager {
/*     */     private final X509TrustManager tm;
/*     */     private X509Certificate[] chain;
/*     */     
/*     */     SavingTrustManager(X509TrustManager aTm) {
/* 181 */       this.tm = aTm;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public X509Certificate[] getAcceptedIssuers() {
/* 192 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void checkClientTrusted(X509Certificate[] aChain, String authType) throws CertificateException {
/* 203 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void checkServerTrusted(X509Certificate[] aChain, String authType) throws CertificateException {
/* 214 */       this.chain = aChain;
/* 215 */       this.tm.checkServerTrusted(aChain, authType);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\serve\\utils\InstallCert.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */