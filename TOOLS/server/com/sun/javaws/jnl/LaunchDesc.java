/*     */ package com.sun.javaws.jnl;
/*     */ 
/*     */ import com.sun.deploy.xml.XMLAttributeBuilder;
/*     */ import com.sun.deploy.xml.XMLNode;
/*     */ import com.sun.deploy.xml.XMLNodeBuilder;
/*     */ import com.sun.deploy.xml.XMLable;
/*     */ import com.sun.javaws.exceptions.JNLPSigningException;
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
/*     */ public class LaunchDesc
/*     */   implements XMLable
/*     */ {
/*     */   private String _specVersion;
/*     */   private String _version;
/*     */   private URL _home;
/*     */   private URL _codebase;
/*     */   private InformationDesc _information;
/*     */   private int _securiyModel;
/*     */   private ResourcesDesc _resources;
/*     */   private int _launchType;
/*     */   private ApplicationDesc _applicationDesc;
/*     */   private AppletDesc _appletDesc;
/*     */   private LibraryDesc _libraryDesc;
/*     */   private InstallerDesc _installerDesc;
/*     */   private String _internalCommand;
/*     */   private String _source;
/*     */   private boolean _propsSet = false;
/*     */   private byte[] _bits;
/*     */   public static final int SANDBOX_SECURITY = 0;
/*     */   public static final int ALLPERMISSIONS_SECURITY = 1;
/*     */   public static final int J2EE_APP_CLIENT_SECURITY = 2;
/*     */   public static final int APPLICATION_DESC_TYPE = 1;
/*     */   public static final int APPLET_DESC_TYPE = 2;
/*     */   public static final int LIBRARY_DESC_TYPE = 3;
/*     */   public static final int INSTALLER_DESC_TYPE = 4;
/*     */   public static final int INTERNAL_TYPE = 5;
/*     */   
/*     */   public LaunchDesc(String paramString1, URL paramURL1, URL paramURL2, String paramString2, InformationDesc paramInformationDesc, int paramInt1, ResourcesDesc paramResourcesDesc, int paramInt2, ApplicationDesc paramApplicationDesc, AppletDesc paramAppletDesc, LibraryDesc paramLibraryDesc, InstallerDesc paramInstallerDesc, String paramString3, String paramString4, byte[] paramArrayOfbyte) {
/*  54 */     this._specVersion = paramString1;
/*  55 */     this._version = paramString2;
/*  56 */     this._codebase = paramURL1;
/*  57 */     this._home = paramURL2;
/*  58 */     this._information = paramInformationDesc;
/*  59 */     this._securiyModel = paramInt1;
/*  60 */     this._resources = paramResourcesDesc;
/*  61 */     this._launchType = paramInt2;
/*  62 */     this._applicationDesc = paramApplicationDesc;
/*  63 */     this._appletDesc = paramAppletDesc;
/*  64 */     this._libraryDesc = paramLibraryDesc;
/*  65 */     this._installerDesc = paramInstallerDesc;
/*  66 */     this._internalCommand = paramString3;
/*  67 */     this._source = paramString4;
/*  68 */     this._bits = paramArrayOfbyte;
/*  69 */     if (this._resources != null) this._resources.setParent(this);
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSpecVersion() {
/*  75 */     return this._specVersion;
/*  76 */   } public synchronized URL getCodebase() { return this._codebase; } public byte[] getBytes() {
/*  77 */     return this._bits;
/*     */   }
/*     */   
/*     */   public synchronized URL getLocation() {
/*  81 */     return this._home;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized URL getCanonicalHome() {
/*  88 */     if (this._home == null && this._resources != null) {
/*  89 */       JARDesc jARDesc = this._resources.getMainJar(true);
/*  90 */       return (jARDesc != null) ? jARDesc.getLocation() : null;
/*     */     } 
/*  92 */     return this._home;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InformationDesc getInformation() {
/*  99 */     return this._information;
/*     */   }
/*     */   
/*     */   public String getInternalCommand() {
/* 103 */     return this._internalCommand;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSecurityModel() {
/* 109 */     return this._securiyModel;
/*     */   }
/*     */   
/*     */   public ResourcesDesc getResources()
/*     */   {
/* 114 */     return this._resources;
/* 115 */   } public boolean arePropsSet() { return this._propsSet; } public void setPropsSet(boolean paramBoolean) {
/* 116 */     this._propsSet = paramBoolean;
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
/*     */   public int getLaunchType() {
/* 128 */     return this._launchType;
/* 129 */   } public ApplicationDesc getApplicationDescriptor() { return this._applicationDesc; }
/* 130 */   public AppletDesc getAppletDescriptor() { return this._appletDesc; } public InstallerDesc getInstallerDescriptor() {
/* 131 */     return this._installerDesc;
/*     */   }
/* 133 */   public boolean isApplication() { return (this._launchType == 1); }
/* 134 */   public boolean isApplet() { return (this._launchType == 2); }
/* 135 */   public boolean isLibrary() { return (this._launchType == 3); }
/* 136 */   public boolean isInstaller() { return (this._launchType == 4); }
/* 137 */   public boolean isApplicationDescriptor() { return (isApplication() || isApplet()); } public boolean isHttps() {
/* 138 */     return this._codebase.getProtocol().equals("https");
/*     */   }
/*     */   public String getSource() {
/* 141 */     return this._source;
/*     */   }
/*     */   
/*     */   public void checkSigning(LaunchDesc paramLaunchDesc) throws JNLPSigningException {
/* 145 */     if (!paramLaunchDesc.getSource().equals(getSource())) {
/* 146 */       throw new JNLPSigningException(this, paramLaunchDesc.getSource());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isJRESpecified() {
/* 152 */     boolean[] arrayOfBoolean1 = new boolean[1];
/* 153 */     boolean[] arrayOfBoolean2 = new boolean[1];
/* 154 */     if (getResources() != null)
/* 155 */       getResources().visit(new ResourceVisitor(this, arrayOfBoolean2, arrayOfBoolean1) { private final boolean[] val$needJre; public void visitJARDesc(JARDesc param1JARDesc) {
/* 156 */               this.val$needJre[0] = true;
/*     */             } private final boolean[] val$hasJre; private final LaunchDesc this$0; public void visitPropertyDesc(PropertyDesc param1PropertyDesc) {} public void visitPackageDesc(PackageDesc param1PackageDesc) {}
/*     */             public void visitExtensionDesc(ExtensionDesc param1ExtensionDesc) {
/* 159 */               this.val$needJre[0] = true; } public void visitJREDesc(JREDesc param1JREDesc) {
/* 160 */               this.val$hasJre[0] = true;
/*     */             } }
/*     */         ); 
/* 163 */     if (this._launchType == 1 || this._launchType == 2) {
/* 164 */       arrayOfBoolean2[0] = true;
/*     */     }
/* 166 */     return (arrayOfBoolean1[0] || !arrayOfBoolean2[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public XMLNode asXML() {
/* 171 */     XMLAttributeBuilder xMLAttributeBuilder = new XMLAttributeBuilder();
/* 172 */     xMLAttributeBuilder.add("spec", this._specVersion);
/* 173 */     xMLAttributeBuilder.add("codebase", this._codebase);
/* 174 */     xMLAttributeBuilder.add("version", this._version);
/* 175 */     xMLAttributeBuilder.add("href", this._home);
/*     */     
/* 177 */     XMLNodeBuilder xMLNodeBuilder = new XMLNodeBuilder("jnlp", xMLAttributeBuilder.getAttributeList());
/* 178 */     xMLNodeBuilder.add(this._information);
/*     */     
/* 180 */     if (this._securiyModel == 1) {
/* 181 */       xMLNodeBuilder.add(new XMLNode("security", null, new XMLNode("all-permissions", null), null));
/* 182 */     } else if (this._securiyModel == 2) {
/* 183 */       xMLNodeBuilder.add(new XMLNode("security", null, new XMLNode("j2ee-application-client-permissions", null), null));
/*     */     } 
/* 185 */     xMLNodeBuilder.add(this._resources);
/* 186 */     xMLNodeBuilder.add(this._applicationDesc);
/* 187 */     xMLNodeBuilder.add(this._appletDesc);
/* 188 */     xMLNodeBuilder.add(this._libraryDesc);
/* 189 */     xMLNodeBuilder.add(this._installerDesc);
/* 190 */     return xMLNodeBuilder.getNode();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 194 */     return asXML().toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\jnl\LaunchDesc.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */