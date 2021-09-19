/*     */ package javax.activation;
/*     */ 
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import java.awt.datatransfer.UnsupportedFlavorException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PipedInputStream;
/*     */ import java.io.PipedOutputStream;
/*     */ import java.net.URL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DataHandler
/*     */   implements Transferable
/*     */ {
/*  94 */   private DataSource dataSource = null;
/*  95 */   private DataSource objDataSource = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   private Object object = null;
/* 101 */   private String objectMimeType = null;
/*     */ 
/*     */   
/* 104 */   private CommandMap currentCommandMap = null;
/*     */ 
/*     */   
/* 107 */   private static final DataFlavor[] emptyFlavors = new DataFlavor[0];
/* 108 */   private DataFlavor[] transferFlavors = emptyFlavors;
/*     */ 
/*     */   
/* 111 */   private DataContentHandler dataContentHandler = null;
/* 112 */   private DataContentHandler factoryDCH = null;
/*     */ 
/*     */   
/* 115 */   private static DataContentHandlerFactory factory = null;
/* 116 */   private DataContentHandlerFactory oldFactory = null;
/*     */   
/* 118 */   private String shortType = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DataHandler(DataSource ds) {
/* 129 */     this.dataSource = ds;
/* 130 */     this.oldFactory = factory;
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
/*     */   public DataHandler(Object obj, String mimeType) {
/* 143 */     this.object = obj;
/* 144 */     this.objectMimeType = mimeType;
/* 145 */     this.oldFactory = factory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DataHandler(URL url) {
/* 156 */     this.dataSource = new URLDataSource(url);
/* 157 */     this.oldFactory = factory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized CommandMap getCommandMap() {
/* 164 */     if (this.currentCommandMap != null) {
/* 165 */       return this.currentCommandMap;
/*     */     }
/* 167 */     return CommandMap.getDefaultCommandMap();
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
/*     */   public DataSource getDataSource() {
/* 185 */     if (this.dataSource == null) {
/*     */       
/* 187 */       if (this.objDataSource == null)
/* 188 */         this.objDataSource = new DataHandlerDataSource(this); 
/* 189 */       return this.objDataSource;
/*     */     } 
/* 191 */     return this.dataSource;
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
/*     */   public String getName() {
/* 203 */     if (this.dataSource != null) {
/* 204 */       return this.dataSource.getName();
/*     */     }
/* 206 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContentType() {
/* 217 */     if (this.dataSource != null) {
/* 218 */       return this.dataSource.getContentType();
/*     */     }
/* 220 */     return this.objectMimeType;
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
/*     */   public InputStream getInputStream() throws IOException {
/* 248 */     InputStream ins = null;
/*     */     
/* 250 */     if (this.dataSource != null) {
/* 251 */       ins = this.dataSource.getInputStream();
/*     */     } else {
/* 253 */       DataContentHandler dch = getDataContentHandler();
/*     */       
/* 255 */       if (dch == null) {
/* 256 */         throw new UnsupportedDataTypeException("no DCH for MIME type " + getBaseType());
/*     */       }
/*     */       
/* 259 */       if (dch instanceof ObjectDataContentHandler && (
/* 260 */         (ObjectDataContentHandler)dch).getDCH() == null) {
/* 261 */         throw new UnsupportedDataTypeException("no object DCH for MIME type " + getBaseType());
/*     */       }
/*     */ 
/*     */       
/* 265 */       final DataContentHandler fdch = dch;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 273 */       final PipedOutputStream pos = new PipedOutputStream();
/* 274 */       PipedInputStream pin = new PipedInputStream(pos);
/* 275 */       (new Thread(new Runnable() { private final DataContentHandler val$fdch; private final PipedOutputStream val$pos; private final DataHandler this$0;
/*     */             
/*     */             public void run() {
/*     */               
/* 279 */               try { fdch.writeTo(DataHandler.this.object, DataHandler.this.objectMimeType, pos); }
/* 280 */               catch (IOException e)
/*     */               
/*     */               { 
/*     */                 try {
/* 284 */                   pos.close();
/* 285 */                 } catch (IOException ie) {} } finally { try { pos.close(); } catch (IOException ie) {} }
/*     */             
/*     */             } }
/*     */           "DataHandler.getInputStream")).start();
/*     */       
/* 290 */       ins = pin;
/*     */     } 
/*     */     
/* 293 */     return ins;
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
/*     */   public void writeTo(OutputStream os) throws IOException {
/* 313 */     if (this.dataSource != null) {
/* 314 */       InputStream is = null;
/* 315 */       byte[] data = new byte[8192];
/*     */ 
/*     */       
/* 318 */       is = this.dataSource.getInputStream();
/*     */       try {
/*     */         int bytes_read;
/* 321 */         while ((bytes_read = is.read(data)) > 0) {
/* 322 */           os.write(data, 0, bytes_read);
/*     */         }
/*     */       } finally {
/* 325 */         is.close();
/* 326 */         is = null;
/*     */       } 
/*     */     } else {
/* 329 */       DataContentHandler dch = getDataContentHandler();
/* 330 */       dch.writeTo(this.object, this.objectMimeType, os);
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
/*     */   public OutputStream getOutputStream() throws IOException {
/* 347 */     if (this.dataSource != null) {
/* 348 */       return this.dataSource.getOutputStream();
/*     */     }
/* 350 */     return null;
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
/*     */   public synchronized DataFlavor[] getTransferDataFlavors() {
/* 378 */     if (factory != this.oldFactory) {
/* 379 */       this.transferFlavors = emptyFlavors;
/*     */     }
/*     */     
/* 382 */     if (this.transferFlavors == emptyFlavors)
/* 383 */       this.transferFlavors = getDataContentHandler().getTransferDataFlavors(); 
/* 384 */     return this.transferFlavors;
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
/*     */   public boolean isDataFlavorSupported(DataFlavor flavor) {
/* 400 */     DataFlavor[] lFlavors = getTransferDataFlavors();
/*     */     
/* 402 */     for (int i = 0; i < lFlavors.length; i++) {
/* 403 */       if (lFlavors[i].equals(flavor))
/* 404 */         return true; 
/*     */     } 
/* 406 */     return false;
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
/*     */   public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
/* 444 */     return getDataContentHandler().getTransferData(flavor, this.dataSource);
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
/*     */   public synchronized void setCommandMap(CommandMap commandMap) {
/* 460 */     if (commandMap != this.currentCommandMap || commandMap == null) {
/*     */       
/* 462 */       this.transferFlavors = emptyFlavors;
/* 463 */       this.dataContentHandler = null;
/*     */       
/* 465 */       this.currentCommandMap = commandMap;
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
/*     */   public CommandInfo[] getPreferredCommands() {
/* 483 */     if (this.dataSource != null) {
/* 484 */       return getCommandMap().getPreferredCommands(getBaseType(), this.dataSource);
/*     */     }
/*     */     
/* 487 */     return getCommandMap().getPreferredCommands(getBaseType());
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
/*     */   public CommandInfo[] getAllCommands() {
/* 503 */     if (this.dataSource != null) {
/* 504 */       return getCommandMap().getAllCommands(getBaseType(), this.dataSource);
/*     */     }
/* 506 */     return getCommandMap().getAllCommands(getBaseType());
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
/*     */   public CommandInfo getCommand(String cmdName) {
/* 522 */     if (this.dataSource != null) {
/* 523 */       return getCommandMap().getCommand(getBaseType(), cmdName, this.dataSource);
/*     */     }
/*     */     
/* 526 */     return getCommandMap().getCommand(getBaseType(), cmdName);
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
/*     */   public Object getContent() throws IOException {
/* 547 */     if (this.object != null) {
/* 548 */       return this.object;
/*     */     }
/* 550 */     return getDataContentHandler().getContent(getDataSource());
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
/*     */   public Object getBean(CommandInfo cmdinfo) {
/* 566 */     Object bean = null;
/*     */ 
/*     */ 
/*     */     
/* 570 */     try { ClassLoader cld = null;
/*     */       
/* 572 */       cld = SecuritySupport.getContextClassLoader();
/* 573 */       if (cld == null)
/* 574 */         cld = getClass().getClassLoader(); 
/* 575 */       bean = cmdinfo.getCommandObject(this, cld); }
/* 576 */     catch (IOException e) {  }
/* 577 */     catch (ClassNotFoundException e) {}
/*     */     
/* 579 */     return bean;
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
/*     */   private synchronized DataContentHandler getDataContentHandler() {
/* 602 */     if (factory != this.oldFactory) {
/* 603 */       this.oldFactory = factory;
/* 604 */       this.factoryDCH = null;
/* 605 */       this.dataContentHandler = null;
/* 606 */       this.transferFlavors = emptyFlavors;
/*     */     } 
/*     */     
/* 609 */     if (this.dataContentHandler != null) {
/* 610 */       return this.dataContentHandler;
/*     */     }
/* 612 */     String simpleMT = getBaseType();
/*     */     
/* 614 */     if (this.factoryDCH == null && factory != null) {
/* 615 */       this.factoryDCH = factory.createDataContentHandler(simpleMT);
/*     */     }
/* 617 */     if (this.factoryDCH != null) {
/* 618 */       this.dataContentHandler = this.factoryDCH;
/*     */     }
/* 620 */     if (this.dataContentHandler == null) {
/* 621 */       if (this.dataSource != null) {
/* 622 */         this.dataContentHandler = getCommandMap().createDataContentHandler(simpleMT, this.dataSource);
/*     */       } else {
/*     */         
/* 625 */         this.dataContentHandler = getCommandMap().createDataContentHandler(simpleMT);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 631 */     if (this.dataSource != null) {
/* 632 */       this.dataContentHandler = new DataSourceDataContentHandler(this.dataContentHandler, this.dataSource);
/*     */     }
/*     */     else {
/*     */       
/* 636 */       this.dataContentHandler = new ObjectDataContentHandler(this.dataContentHandler, this.object, this.objectMimeType);
/*     */     } 
/*     */ 
/*     */     
/* 640 */     return this.dataContentHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized String getBaseType() {
/* 648 */     if (this.shortType == null) {
/* 649 */       String ct = getContentType();
/*     */       try {
/* 651 */         MimeType mt = new MimeType(ct);
/* 652 */         this.shortType = mt.getBaseType();
/* 653 */       } catch (MimeTypeParseException e) {
/* 654 */         this.shortType = ct;
/*     */       } 
/*     */     } 
/* 657 */     return this.shortType;
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
/*     */   public static synchronized void setDataContentHandlerFactory(DataContentHandlerFactory newFactory) {
/* 675 */     if (factory != null) {
/* 676 */       throw new Error("DataContentHandlerFactory already defined");
/*     */     }
/* 678 */     SecurityManager security = System.getSecurityManager();
/* 679 */     if (security != null)
/*     */       
/*     */       try {
/* 682 */         security.checkSetFactory();
/* 683 */       } catch (SecurityException ex) {
/*     */ 
/*     */ 
/*     */         
/* 687 */         if (DataHandler.class.getClassLoader() != newFactory.getClass().getClassLoader())
/*     */         {
/* 689 */           throw ex;
/*     */         }
/*     */       }  
/* 692 */     factory = newFactory;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\activation\DataHandler.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */