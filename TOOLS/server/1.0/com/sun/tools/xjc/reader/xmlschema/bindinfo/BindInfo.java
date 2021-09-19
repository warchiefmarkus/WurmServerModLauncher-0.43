/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*     */ 
/*     */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIDeclaration;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import java.util.Vector;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BindInfo
/*     */ {
/*     */   private final Locator location;
/*     */   private String documentation;
/*     */   private boolean _hasTitleInDocumentation;
/*     */   private XSComponent owner;
/*     */   private BGMBuilder builder;
/*     */   private final Vector decls;
/*     */   
/*     */   public Locator getSourceLocation() {
/*     */     return this.location;
/*     */   }
/*     */   
/*     */   public void setOwner(BGMBuilder _builder, XSComponent _owner) {
/*     */     this.owner = _owner;
/*     */     this.builder = _builder;
/*     */   }
/*     */   
/*     */   public BindInfo(Locator loc) {
/*  34 */     this._hasTitleInDocumentation = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  70 */     this.decls = new Vector();
/*     */     this.location = loc;
/*     */   }
/*     */   public void addDecl(BIDeclaration decl) {
/*  74 */     if (decl == null) throw new IllegalArgumentException(); 
/*  75 */     decl.setParent(this);
/*  76 */     this.decls.add(decl);
/*     */   }
/*     */   
/*     */   public XSComponent getOwner() {
/*     */     return this.owner;
/*     */   }
/*     */   
/*     */   public BIDeclaration get(QName name) {
/*  84 */     int len = this.decls.size();
/*  85 */     for (int i = 0; i < len; i++) {
/*  86 */       BIDeclaration decl = this.decls.get(i);
/*  87 */       if (decl.getName().equals(name))
/*  88 */         return decl; 
/*     */     } 
/*  90 */     return null;
/*     */   }
/*     */   public BGMBuilder getBuilder() {
/*     */     return this.builder;
/*     */   }
/*     */   
/*     */   public BIDeclaration[] getDecls() {
/*  97 */     return (BIDeclaration[])this.decls.toArray((Object[])new BIDeclaration[this.decls.size()]);
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
/*     */   public boolean hasTitleInDocumentation() {
/* 113 */     return this._hasTitleInDocumentation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDocumentation() {
/* 121 */     return this.documentation;
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
/*     */   public void appendDocumentation(String fragment, boolean hasTitleInDocumentation) {
/* 137 */     if (this.documentation == null) {
/* 138 */       this.documentation = fragment;
/* 139 */       this._hasTitleInDocumentation = hasTitleInDocumentation;
/*     */     } else {
/* 141 */       this.documentation += "\n\n" + fragment;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void absorb(com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo bi) {
/* 150 */     for (int i = 0; i < bi.decls.size(); i++)
/* 151 */       ((BIDeclaration)bi.decls.get(i)).setParent(this); 
/* 152 */     this.decls.addAll(bi.decls);
/* 153 */     appendDocumentation(bi.documentation, bi.hasTitleInDocumentation());
/*     */   }
/*     */   
/*     */   public int size() {
/* 157 */     return this.decls.size();
/*     */   } public BIDeclaration get(int idx) {
/* 159 */     return this.decls.get(idx);
/*     */   }
/*     */ 
/*     */   
/* 163 */   public static final com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo empty = new com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo(null);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\BindInfo.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */