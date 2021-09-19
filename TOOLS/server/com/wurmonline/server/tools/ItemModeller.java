/*     */ package com.wurmonline.server.tools;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.TextArea;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.awt.event.WindowListener;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JProgressBar;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.JToggleButton;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ItemModeller
/*     */   extends JFrame
/*     */   implements KeyListener, WindowListener
/*     */ {
/*     */   private static final long serialVersionUID = 1008389608509176516L;
/*  50 */   private JPanel southHackPanel = new JPanel();
/*  51 */   private JLabel ipAdressLabel = new JLabel();
/*  52 */   private JProgressBar hackProgressBar = new JProgressBar();
/*  53 */   private FlowLayout flowLayout1 = new FlowLayout();
/*  54 */   private JPanel textAreaPanel = new JPanel();
/*  55 */   private JTextField ipAdressTextField = new JTextField();
/*  56 */   private BorderLayout borderLayout1 = new BorderLayout();
/*     */   
/*  58 */   private JComboBox<?> portComboBox = new JComboBox();
/*  59 */   private JToggleButton hackButton = new JToggleButton();
/*  60 */   private JComboBox<?> hackComboBox = new JComboBox();
/*  61 */   private JTextField inputTextField = new JTextField();
/*     */   
/*  63 */   private JPanel ipAdressPanel = new JPanel();
/*  64 */   private JButton pingButton = new JButton();
/*  65 */   private JButton scanButton = new JButton();
/*  66 */   private TextArea messageTextArea = new TextArea();
/*  67 */   private TextArea codeTextArea = new TextArea();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemModeller() {
/*  74 */     super("Wurm Item Modeller");
/*  75 */     addMessage("Welcome to wurm item modeller.");
/*     */     
/*     */     try {
/*  78 */       jbInit();
/*  79 */       setBounds(0, 0, 1000, 700);
/*  80 */       setVisible(true);
/*     */     }
/*  82 */     catch (Exception e) {
/*     */       
/*  84 */       e.printStackTrace();
/*     */     } 
/*  86 */     addWindowListener(this);
/*     */   }
/*     */ 
/*     */   
/*     */   private void jbInit() throws Exception {
/*  91 */     this.ipAdressLabel.setText("Create new:");
/*  92 */     this.ipAdressLabel.setVerticalAlignment(1);
/*  93 */     this.ipAdressLabel.setVerticalTextPosition(1);
/*  94 */     this.southHackPanel.setLayout(this.flowLayout1);
/*  95 */     this.ipAdressTextField.setMinimumSize(new Dimension(70, 21));
/*  96 */     this.ipAdressTextField.setPreferredSize(new Dimension(170, 21));
/*  97 */     this.ipAdressTextField.setText("");
/*  98 */     this.ipAdressTextField.addActionListener(new ActionListener()
/*     */         {
/*     */           
/*     */           public void actionPerformed(ActionEvent e)
/*     */           {
/* 103 */             ItemModeller.this.ipAdressTextField_actionPerformed(e);
/*     */           }
/*     */         });
/* 106 */     this.textAreaPanel.setLayout(this.borderLayout1);
/* 107 */     this.hackButton.setText("Load");
/* 108 */     this.hackButton.addActionListener(new ActionListener()
/*     */         {
/*     */           
/*     */           public void actionPerformed(ActionEvent e)
/*     */           {
/* 113 */             ItemModeller.this.hackButton_actionPerformed(e);
/*     */           }
/*     */         });
/* 116 */     this.hackProgressBar.setMaximum(100);
/* 117 */     this.hackProgressBar.setMinimum(0);
/* 118 */     this.textAreaPanel.setMinimumSize(new Dimension(200, 500));
/* 119 */     this.textAreaPanel.setPreferredSize(new Dimension(250, 500));
/* 120 */     this.textAreaPanel.setToolTipText("");
/* 121 */     this.inputTextField.setText("");
/* 122 */     this.inputTextField.setHorizontalAlignment(0);
/* 123 */     this.inputTextField.addActionListener(new ActionListener()
/*     */         {
/*     */           
/*     */           public void actionPerformed(ActionEvent e)
/*     */           {
/* 128 */             ItemModeller.this.inputTextField_actionPerformed(e);
/*     */           }
/*     */         });
/* 131 */     this.pingButton.setText("Save");
/* 132 */     this.pingButton.addActionListener(new ActionListener()
/*     */         {
/*     */           
/*     */           public void actionPerformed(ActionEvent e)
/*     */           {
/* 137 */             ItemModeller.this.pingButton_actionPerformed(e);
/*     */           }
/*     */         });
/* 140 */     this.scanButton.setToolTipText("");
/* 141 */     this.scanButton.setText("Load");
/* 142 */     this.scanButton.addActionListener(new ActionListener()
/*     */         {
/*     */           
/*     */           public void actionPerformed(ActionEvent e)
/*     */           {
/* 147 */             ItemModeller.this.scanButton_actionPerformed(e);
/*     */           }
/*     */         });
/*     */     
/* 151 */     this.hackComboBox.addActionListener(new ActionListener()
/*     */         {
/*     */           
/*     */           public void actionPerformed(ActionEvent e)
/*     */           {
/* 156 */             ItemModeller.this.hackComboBox_actionPerformed(e);
/*     */           }
/*     */         });
/* 159 */     this.portComboBox.addActionListener(new ActionListener()
/*     */         {
/*     */           
/*     */           public void actionPerformed(ActionEvent e)
/*     */           {
/* 164 */             ItemModeller.this.portComboBox_actionPerformed(e);
/*     */           }
/*     */         });
/* 167 */     this.textAreaPanel.add(this.inputTextField, "South");
/* 168 */     this.textAreaPanel.add(this.messageTextArea, "Center");
/* 169 */     getContentPane().add(this.ipAdressPanel, "North");
/* 170 */     this.ipAdressPanel.add(this.ipAdressLabel, (Object)null);
/* 171 */     this.ipAdressPanel.add(this.ipAdressTextField, (Object)null);
/* 172 */     this.ipAdressPanel.add(this.pingButton, (Object)null);
/* 173 */     this.ipAdressPanel.add(this.scanButton, (Object)null);
/* 174 */     getContentPane().add(this.southHackPanel, "South");
/* 175 */     String[] data = { "" };
/*     */     
/* 177 */     this.portComboBox = new JComboBox((Object[])data);
/* 178 */     this.southHackPanel.add(this.hackComboBox, (Object)null);
/* 179 */     this.southHackPanel.add(this.portComboBox, (Object)null);
/* 180 */     this.southHackPanel.add(this.hackButton, (Object)null);
/* 181 */     this.southHackPanel.add(this.hackProgressBar, (Object)null);
/* 182 */     getContentPane().add(this.codeTextArea, "Center");
/* 183 */     getContentPane().add(this.textAreaPanel, "East");
/* 184 */     addMessage("Read all about it here.");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void hackButton_actionPerformed(ActionEvent e) {}
/*     */ 
/*     */   
/*     */   void inputTextField_actionPerformed(ActionEvent e) {
/* 193 */     this.inputTextField.setText("");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void ipAdressTextField_actionPerformed(ActionEvent e) {}
/*     */ 
/*     */   
/*     */   void pingButton_actionPerformed(ActionEvent e) {
/* 202 */     this.ipAdressTextField.setBackground(Color.white);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void scanButton_actionPerformed(ActionEvent e) {}
/*     */ 
/*     */ 
/*     */   
/*     */   void remoteFileSystem_actionPerformed(ActionEvent e) {}
/*     */ 
/*     */   
/*     */   void localFileSystem_actionPerformed(ActionEvent e) {
/* 215 */     addMessage("Doing something with the local window.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void hackComboBox_actionPerformed(ActionEvent e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void portComboBox_actionPerformed(ActionEvent e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void windowDeactivated(WindowEvent e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void windowActivated(WindowEvent e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void windowDeiconified(WindowEvent e) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void windowIconified(WindowEvent e) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void windowClosed(WindowEvent e) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void windowClosing(WindowEvent e) {
/* 254 */     shutDown();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void windowOpened(WindowEvent e) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMessage(String message) {
/* 264 */     if (message.endsWith("\n")) {
/* 265 */       this.messageTextArea.append(message);
/*     */     } else {
/* 267 */       this.messageTextArea.append(message + "\n");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void shutDown() {
/* 272 */     System.exit(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void keyReleased(KeyEvent e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void keyTyped(KeyEvent e) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void keyPressed(KeyEvent e) {
/* 288 */     if (e.getKeyCode() == 27)
/*     */     {
/* 290 */       shutDown();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tools\ItemModeller.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */