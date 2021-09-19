/*     */ package com.wurmonline.server.items;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Base64;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class InscriptionData
/*     */ {
/*     */   private String inscription;
/*     */   private final long wurmid;
/*     */   private String inscriber;
/*     */   private int penColour;
/*  53 */   private static final Logger logger = Logger.getLogger(ItemData.class.getName());
/*     */   private static final String legalInscriptionChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'- 1234567890.,+/!() ;:_#";
/*     */   
/*     */   public InscriptionData(long wid, String theData, String theInscriber, int thePenColour) {
/*  57 */     this.wurmid = wid;
/*  58 */     setInscription(theData);
/*  59 */     setInscriber(theInscriber);
/*  60 */     this.penColour = thePenColour;
/*  61 */     Items.addItemInscriptionData(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public InscriptionData(long wid, Recipe recipe, String theInscriber, int thePenColour) {
/*  66 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*  67 */     DataOutputStream dos = new DataOutputStream(bos);
/*     */     
/*     */     try {
/*  70 */       recipe.pack(dos);
/*  71 */       dos.flush();
/*  72 */       dos.close();
/*     */     }
/*  74 */     catch (IOException e) {
/*     */ 
/*     */       
/*  77 */       logger.log(Level.WARNING, e.getMessage(), e);
/*     */     } 
/*  79 */     byte[] data = bos.toByteArray();
/*  80 */     String base64encodedRecipe = Base64.getEncoder().encodeToString(data);
/*     */     
/*  82 */     this.wurmid = wid;
/*  83 */     setInscription(base64encodedRecipe);
/*  84 */     setInscriber(theInscriber);
/*  85 */     this.penColour = thePenColour;
/*  86 */     Items.addItemInscriptionData(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInscription() {
/*  96 */     return this.inscription;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Recipe getRecipe() {
/* 107 */     byte[] bytes = Base64.getDecoder().decode(this.inscription);
/* 108 */     DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
/*     */     
/*     */     try {
/* 111 */       return new Recipe(dis);
/*     */     }
/* 113 */     catch (NoSuchTemplateException e) {
/*     */ 
/*     */       
/* 116 */       logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*     */     }
/* 118 */     catch (IOException e) {
/*     */ 
/*     */       
/* 121 */       logger.log(Level.WARNING, e.getMessage(), e);
/*     */     } 
/* 123 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInscription(String newInscription) {
/* 134 */     this.inscription = newInscription;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInscriber() {
/* 144 */     return this.inscriber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInscriber(String aInscriber) {
/* 155 */     this.inscriber = aInscriber;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPenColour() {
/* 160 */     return this.penColour;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPenColour(int newColour) {
/* 165 */     this.penColour = newColour;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getWurmId() {
/* 175 */     return this.wurmid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasBeenInscribed() {
/* 185 */     return (getInscription() != null && getInscription().length() > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void createInscriptionEntry(Connection dbcon) {
/* 194 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 197 */       ps = dbcon.prepareStatement(ItemDbStrings.getInstance().createInscription());
/* 198 */       ps.setLong(1, getWurmId());
/* 199 */       ps.setString(2, getInscription());
/* 200 */       ps.setString(3, getInscriber());
/* 201 */       ps.setInt(4, getPenColour());
/* 202 */       ps.executeUpdate();
/*     */     }
/* 204 */     catch (SQLException sqx) {
/*     */       
/* 206 */       logger.log(Level.WARNING, "Failed to save inscription data " + getWurmId(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 210 */       DbUtilities.closeDatabaseObjects(ps, null);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean containsIllegalCharacters(String name) {
/* 217 */     char[] chars = name.toCharArray();
/*     */     
/* 219 */     for (int x = 0; x < chars.length; x++) {
/*     */       
/* 221 */       if ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'- 1234567890.,+/!() ;:_#".indexOf(chars[x]) < 0)
/* 222 */         return true; 
/*     */     } 
/* 224 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\InscriptionData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */