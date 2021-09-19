/*    */ package com.sun.jnlp;
/*    */ 
/*    */ import com.sun.deploy.resources.ResourceManager;
/*    */ import com.sun.deploy.util.Trace;
/*    */ import java.awt.Toolkit;
/*    */ import java.awt.datatransfer.Clipboard;
/*    */ import java.awt.datatransfer.DataFlavor;
/*    */ import java.awt.datatransfer.Transferable;
/*    */ import java.awt.datatransfer.UnsupportedFlavorException;
/*    */ import java.io.IOException;
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ import javax.jnlp.ClipboardService;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ClipboardServiceImpl
/*    */   implements ClipboardService
/*    */ {
/* 21 */   private static ClipboardServiceImpl _sharedInstance = null;
/*    */   
/* 23 */   private Clipboard _sysClipboard = null;
/* 24 */   private SmartSecurityDialog _readDialog = null;
/* 25 */   private SmartSecurityDialog _writeDialog = null;
/*    */ 
/*    */   
/*    */   private ClipboardServiceImpl() {
/* 29 */     this._readDialog = new SmartSecurityDialog(ResourceManager.getString("APIImpl.clipboard.message.read"));
/* 30 */     this._writeDialog = new SmartSecurityDialog(ResourceManager.getString("APIImpl.clipboard.message.write"));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 37 */     Toolkit toolkit = Toolkit.getDefaultToolkit();
/* 38 */     if (toolkit != null) {
/* 39 */       this._sysClipboard = toolkit.getSystemClipboard();
/*    */     }
/*    */   }
/*    */   
/*    */   public static synchronized ClipboardServiceImpl getInstance() {
/* 44 */     if (_sharedInstance == null) {
/* 45 */       _sharedInstance = new ClipboardServiceImpl();
/*    */     }
/* 47 */     return _sharedInstance;
/*    */   }
/*    */   
/*    */   public Transferable getContents() {
/* 51 */     if (!askUser(false)) return null;
/*    */     
/* 53 */     return AccessController.<Transferable>doPrivileged(new PrivilegedAction(this) { private final ClipboardServiceImpl this$0;
/*    */           public Object run() {
/* 55 */             return this.this$0._sysClipboard.getContents(null);
/*    */           } }
/*    */       );
/*    */   }
/*    */   
/*    */   public void setContents(Transferable paramTransferable) {
/* 61 */     if (!askUser(true))
/*    */       return; 
/* 63 */     AccessController.doPrivileged(new PrivilegedAction(this, paramTransferable) { private final Transferable val$contents;
/*    */           public Object run() {
/* 65 */             if (this.val$contents != null) {
/* 66 */               DataFlavor[] arrayOfDataFlavor = this.val$contents.getTransferDataFlavors();
/* 67 */               if (arrayOfDataFlavor == null || arrayOfDataFlavor[0] == null) return null; 
/*    */               try {
/* 69 */                 if (this.val$contents.getTransferData(arrayOfDataFlavor[0]) == null) return null; 
/* 70 */               } catch (IOException iOException) {
/* 71 */                 Trace.ignoredException(iOException);
/* 72 */               } catch (UnsupportedFlavorException unsupportedFlavorException) {
/* 73 */                 Trace.ignoredException(unsupportedFlavorException);
/*    */               } 
/*    */             } 
/* 76 */             this.this$0._sysClipboard.setContents(this.val$contents, null);
/* 77 */             return null;
/*    */           }
/*    */           private final ClipboardServiceImpl this$0; }
/*    */       );
/*    */   }
/*    */   private synchronized boolean askUser(boolean paramBoolean) {
/* 83 */     if (!hasClipboard()) return false;
/*    */     
/* 85 */     if (CheckServicePermission.hasClipboardPermissions()) return true;
/*    */     
/* 87 */     return paramBoolean ? this._writeDialog.showDialog() : this._readDialog.showDialog();
/*    */   }
/*    */   private boolean hasClipboard() {
/* 90 */     return (this._sysClipboard != null);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\jnlp\ClipboardServiceImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */