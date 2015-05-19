/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphamods.controlcenter;

import com.alphamods.controlcenter.utils.config;
import com.alphamods.controlcenter.utils.methods;
import com.alphamods.controlcenter.utils.secuences;
import static com.alphamods.controlcenter.utils.secuences.path;
import com.bric.swing.ColorPicker;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import static java.awt.Frame.ICONIFIED;
import static java.awt.Frame.MAXIMIZED_BOTH;
import static java.awt.Frame.NORMAL;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 *
 * @author prada
 */
public class mainGUI extends javax.swing.JFrame {
private int mode = 0;
private methods methods = new methods();
    com.alphamods.controlcenter.utils.secuences s1record;
    com.alphamods.controlcenter.utils.secuences s1play = new com.alphamods.controlcenter.utils.secuences();


private boolean testmode = false;
ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private SerialPortEventListener evento = new SerialPortEventListener() {

    @Override
    public void serialEvent(SerialPortEvent spe) {
        if (methods.getMulti().DataReceptionCompleted()== true){
            Temp1.setText(methods.getMulti().getMessage(0));
            Temp2.setText(methods.getMulti().getMessage(1));
            Temp3.setText(methods.getMulti().getMessage(2));
            Temp4.setText(methods.getMulti().getMessage(3));
            Temp5.setText(methods.getMulti().getMessage(4));
            Temp6.setText(methods.getMulti().getMessage(5));
            methods.getMulti().flushBuffer();
        }
    }
};

    
    public void trayIcon(){
        TrayIcon trayIcon;

        if (SystemTray.isSupported()) {

            try {
                
                SystemTray tray = SystemTray.getSystemTray();
                
                BufferedImage trayIconImage = ImageIO.read(getClass().getResource("res/icon128.png"));
                int trayIconWidth = new TrayIcon(trayIconImage).getSize().width;
                
                
                
                ActionListener exitListener = new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                };
                
                ActionListener openListener = new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        setVisible(true);
                        requestFocusInWindow();
                        
                        
                    }
                };
                
                PopupMenu popup = new PopupMenu();
                
                MenuItem openItem = new MenuItem("Open");
                openItem.addActionListener(openListener);
                popup.add(openItem);
                MenuItem exitItem = new MenuItem("Exit");
                exitItem.addActionListener(exitListener);
                popup.addSeparator();
                popup.add(exitItem);
                
                trayIcon = new TrayIcon(trayIconImage.getScaledInstance(trayIconWidth, -1, Image.SCALE_SMOOTH), "Right click for options", popup);
                trayIcon.setImageAutoSize(true);
                
                try {
                    tray.add(trayIcon);
                } catch (AWTException e) {
                    System.err.println("TrayIcon could not be added.");
                }
                
                addWindowStateListener(new WindowStateListener() {
                    public void windowStateChanged(WindowEvent e) {
                        if(e.getNewState()==ICONIFIED){
                            setVisible(false);
                        }
                        
                        if(e.getNewState()==7){
                            setVisible(false);
                        }
                        
                        if(e.getNewState()==MAXIMIZED_BOTH){
                            setVisible(true);
                        }
                        
                        if(e.getNewState()==NORMAL){
                            setVisible(true);
                        }
                    }
                });
                setDefaultCloseOperation(mainGUI.EXIT_ON_CLOSE);
                
                
            } catch (IOException ex) {
                Logger.getLogger(mainGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            

        } else {
            System.err.println("System tray is currently not supported.");
        }
    }

    
    /**
     * Creates new form mainGUI
     */
    public mainGUI() {
        initComponents();
        trayIcon();
        makeJPanels(bigpanel1);
        methods.initialicePicker(picker);
        methods.initialiceArrays(picker);
        initPanel();
        sliders();
        ports();
        rpmData();
        rpm();
        refreshMode();
        loadsecpreviews();        
        setIcons();
        checkUpdates();

        
        
        
        
        picker.addPropertyChangeListener(picker.SELECTED_COLOR_PROPERTY, new PropertyChangeListener() {
        
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            PickerColorChanged(evt);}}); 
        }
    
    public void checkUpdates(){
        notificationsLabel.setVisible(true);
        notificationsLabel.setText("Checking for updates...");
        notificationsBar.setVisible(true);
        notificationsBar.setIndeterminate(true);
        updater updater = new updater();
        updater.start();
        updater.setIndicators(notificationsLabel, notificationsBar, this);
        
    }
    public void  Flash(ColorPicker picker) {
        int N=500000;
        float gHue = Color.RGBtoHSB(0, 1, 0, null)[0];
        float bHue = Color.RGBtoHSB(0, 0, 1, null)[0];
        for (int i = 0; i < N; i++) {
            picker.setColor(Color.getHSBColor(gHue + (i * (bHue - gHue) / N), 1, 1));
        }
        for (int i = 0; i < N; i++) {
            picker.setColor(Color.getHSBColor(bHue - (i * (bHue - gHue) / N), 1, 1));
        }
    }
    
    public void setIcons(){
    try {
        
        BufferedImage icon16 = ImageIO.read(main.class.getResourceAsStream("res/icon16.png"));
        BufferedImage icon32 = ImageIO.read(main.class.getResourceAsStream("res/icon32.png"));
        BufferedImage icon64 = ImageIO.read(main.class.getResourceAsStream("res/icon64.png"));
        BufferedImage icon128 = ImageIO.read(main.class.getResourceAsStream("res/icon128.png"));

        List<Image> icons = new ArrayList<Image>();
        icons.add(icon16);
        icons.add(icon32);
        icons.add(icon64);
        icons.add(icon128);
        
        this.setIconImages(icons);
    } catch (IOException ex) {
        Logger.getLogger(mainGUI.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    public void loadsecpreviews(){
        loadSecuencePreview(1,this.playButton1, this.cleanButton, bigpanel1);
    }
        public void loadSecuencePreview(int number, JToggleButton play, JButton clear, JPanel bigpanel){
            File file = new File(path + File.separator+"secuence"+number+".properties");
            if (!file.exists() || config.getValue("iFinal",file)==""|| config.getValue("iFinal",file)==null){
                play.setEnabled(false);
                clear.setEnabled(false);
                
            }else{
            String ifinal =config.getValue("iFinal", file);
            
            if (ifinal =="" ||ifinal== null){
                
            }else{
            int period = Integer.parseInt(ifinal)/100;
            for(int u= 0;u<100;u++){
            
            int rd = Integer.parseInt(config.getValue("c"+period*u+"r", file));
            int gd = Integer.parseInt(config.getValue("c"+period*u+"g", file));
            int bd = Integer.parseInt(config.getValue("c"+period*u+"b", file));
                    bigpanel.getComponent(u).setBackground(new Color(rd,gd,bd));
            }
            }
        }
        }
        
    public void sliders(){
        methods.sliders(fan1slider, fan2slider, pump1slider);
    }
    
    public void ports(){
        methods.ports(PortsBox);
    }
    
    public void rpmData(){
        methods.rpmData(rmpLabelFan1, rmpLabelFan2, rmpLabelPump1, fan1max, fan2max, pump1max, fan1slider, fan2slider, pump1slider);
    }
    
    public void rpm(){
        methods.rpm(fan1max, fan2max, pump1max);
    }
    
    public void refreshMode(){
        boolean ub = methods.refreshMode(RefreshCheckBox);
        if (ub){
            refreshSecondsSpinner.setEnabled(true);
            jLabel19.setEnabled(true);
            jLabel18.setEnabled(true);
            executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(refreshTemp, 0, Long.parseLong(refreshSecondsSpinner.getValue().toString()), TimeUnit.SECONDS);
        }
    }
    
    public void write(){
        if (methods.isConnected()){
           methods.write(mode, picker, fan1slider, fan2slider, pump1slider,  LedC1, LedC2, LedC3,LedC4,testmode);
        }else if (testmode){
            methods.write(mode, picker, fan1slider, fan2slider,pump1slider, LedC1, LedC2, LedC3,LedC4,testmode);
        }
    }
    public void makeJPanels(JPanel bigPanel){
       
        JPanel p1 = new JPanel();   
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();
        JPanel p4 = new JPanel();
        JPanel p5 = new JPanel();
        JPanel p6 = new JPanel();
        JPanel p7 = new JPanel();
        JPanel p8 = new JPanel();
        JPanel p9 = new JPanel();
        JPanel p10 = new JPanel();
        JPanel p11 = new JPanel();
        JPanel p12 = new JPanel();
        JPanel p13 = new JPanel();
        JPanel p14 = new JPanel();
        JPanel p15 = new JPanel();
        JPanel p16 = new JPanel();
        JPanel p17 = new JPanel();
        JPanel p18 = new JPanel();
        JPanel p19 = new JPanel();
        JPanel p20 = new JPanel();
        JPanel p21 = new JPanel();
        JPanel p22 = new JPanel();
        JPanel p23 = new JPanel();
        JPanel p24 = new JPanel();
        JPanel p25 = new JPanel();
        JPanel p26 = new JPanel();
        JPanel p27 = new JPanel();
        JPanel p28 = new JPanel();
        JPanel p29 = new JPanel();
        JPanel p30 = new JPanel();
        JPanel p31 = new JPanel();
        JPanel p32 = new JPanel();
        JPanel p33 = new JPanel();
        JPanel p34 = new JPanel();
        JPanel p35 = new JPanel();
        JPanel p36 = new JPanel();
        JPanel p37 = new JPanel();
        JPanel p38 = new JPanel();
        JPanel p39 = new JPanel();
        JPanel p40 = new JPanel();
        JPanel p41 = new JPanel();
        JPanel p42 = new JPanel();
        JPanel p43 = new JPanel();
        JPanel p44 = new JPanel();
        JPanel p45 = new JPanel();
        JPanel p46 = new JPanel();
        JPanel p47 = new JPanel();
        JPanel p48 = new JPanel();
        JPanel p49 = new JPanel();
        JPanel p50 = new JPanel();
        JPanel p51 = new JPanel();   
        JPanel p52 = new JPanel();
        JPanel p53 = new JPanel();
        JPanel p54 = new JPanel();
        JPanel p55 = new JPanel();
        JPanel p56 = new JPanel();
        JPanel p57 = new JPanel();
        JPanel p58 = new JPanel();
        JPanel p59 = new JPanel();
        JPanel p60 = new JPanel();
        JPanel p61 = new JPanel();
        JPanel p62 = new JPanel();
        JPanel p63 = new JPanel();
        JPanel p64 = new JPanel();
        JPanel p65 = new JPanel();
        JPanel p66 = new JPanel();
        JPanel p67 = new JPanel();
        JPanel p68 = new JPanel();
        JPanel p69 = new JPanel();
        JPanel p70 = new JPanel();
        JPanel p71 = new JPanel();
        JPanel p72 = new JPanel();
        JPanel p73 = new JPanel();
        JPanel p74 = new JPanel();
        JPanel p75 = new JPanel();
        JPanel p76 = new JPanel();
        JPanel p77 = new JPanel();
        JPanel p78 = new JPanel();
        JPanel p79 = new JPanel();
        JPanel p80 = new JPanel();
        JPanel p81 = new JPanel();
        JPanel p82 = new JPanel();
        JPanel p83 = new JPanel();
        JPanel p84 = new JPanel();
        JPanel p85 = new JPanel();
        JPanel p86 = new JPanel();
        JPanel p87 = new JPanel();
        JPanel p88 = new JPanel();
        JPanel p89 = new JPanel();
        JPanel p90 = new JPanel();
        JPanel p91 = new JPanel();
        JPanel p92 = new JPanel();
        JPanel p93 = new JPanel();
        JPanel p94 = new JPanel();
        JPanel p95 = new JPanel();
        JPanel p96 = new JPanel();
        JPanel p97 = new JPanel();
        JPanel p98 = new JPanel();
        JPanel p99 = new JPanel();
        JPanel p100 = new JPanel();
        
        
               int i = 2;
               int a = 35;
               p1.setPreferredSize(new Dimension(i,a));
               p2.setPreferredSize(new Dimension(i,a));
               p3.setPreferredSize(new Dimension(i,a));
               p4.setPreferredSize(new Dimension(i,a));
               p5.setPreferredSize(new Dimension(i,a));
               p6.setPreferredSize(new Dimension(i,a));
               p7.setPreferredSize(new Dimension(i,a));
               p8.setPreferredSize(new Dimension(i,a));
               p9.setPreferredSize(new Dimension(i,a));
               p10.setPreferredSize(new Dimension(i,a));
               p11.setPreferredSize(new Dimension(i,a));
               p12.setPreferredSize(new Dimension(i,a));
               p13.setPreferredSize(new Dimension(i,a));
               p14.setPreferredSize(new Dimension(i,a));
               p15.setPreferredSize(new Dimension(i,a));
               p16.setPreferredSize(new Dimension(i,a));
               p17.setPreferredSize(new Dimension(i,a));
               p18.setPreferredSize(new Dimension(i,a));
               p19.setPreferredSize(new Dimension(i,a));
               p20.setPreferredSize(new Dimension(i,a));
               p21.setPreferredSize(new Dimension(i,a));
               p22.setPreferredSize(new Dimension(i,a));
               p23.setPreferredSize(new Dimension(i,a));
               p24.setPreferredSize(new Dimension(i,a));
               p25.setPreferredSize(new Dimension(i,a));
               p26.setPreferredSize(new Dimension(i,a));
               p27.setPreferredSize(new Dimension(i,a));
               p28.setPreferredSize(new Dimension(i,a));
               p29.setPreferredSize(new Dimension(i,a));
               p30.setPreferredSize(new Dimension(i,a));
               p31.setPreferredSize(new Dimension(i,a));
               p32.setPreferredSize(new Dimension(i,a));
               p33.setPreferredSize(new Dimension(i,a));
               p34.setPreferredSize(new Dimension(i,a));
               p35.setPreferredSize(new Dimension(i,a));
               p36.setPreferredSize(new Dimension(i,a));
               p37.setPreferredSize(new Dimension(i,a));
               p38.setPreferredSize(new Dimension(i,a));
               p39.setPreferredSize(new Dimension(i,a));
               p40.setPreferredSize(new Dimension(i,a));
               p41.setPreferredSize(new Dimension(i,a));
               p42.setPreferredSize(new Dimension(i,a));
               p43.setPreferredSize(new Dimension(i,a));
               p44.setPreferredSize(new Dimension(i,a));
               p45.setPreferredSize(new Dimension(i,a));
               p46.setPreferredSize(new Dimension(i,a));
               p47.setPreferredSize(new Dimension(i,a));
               p48.setPreferredSize(new Dimension(i,a));
               p49.setPreferredSize(new Dimension(i,a));
               p50.setPreferredSize(new Dimension(i,a));
               p51.setPreferredSize(new Dimension(i,a));
               p52.setPreferredSize(new Dimension(i,a));
               p53.setPreferredSize(new Dimension(i,a));
               p54.setPreferredSize(new Dimension(i,a));
               p55.setPreferredSize(new Dimension(i,a));
               p56.setPreferredSize(new Dimension(i,a));
               p57.setPreferredSize(new Dimension(i,a));
               p58.setPreferredSize(new Dimension(i,a));
               p59.setPreferredSize(new Dimension(i,a));
               p60.setPreferredSize(new Dimension(i,a));
               p61.setPreferredSize(new Dimension(i,a));
               p62.setPreferredSize(new Dimension(i,a));
               p63.setPreferredSize(new Dimension(i,a));
               p64.setPreferredSize(new Dimension(i,a));
               p65.setPreferredSize(new Dimension(i,a));
               p66.setPreferredSize(new Dimension(i,a));
               p67.setPreferredSize(new Dimension(i,a));
               p68.setPreferredSize(new Dimension(i,a));
               p69.setPreferredSize(new Dimension(i,a));
               p70.setPreferredSize(new Dimension(i,a));
               p71.setPreferredSize(new Dimension(i,a));
               p72.setPreferredSize(new Dimension(i,a));
               p73.setPreferredSize(new Dimension(i,a));
               p74.setPreferredSize(new Dimension(i,a));
               p75.setPreferredSize(new Dimension(i,a));
               p76.setPreferredSize(new Dimension(i,a));
               p77.setPreferredSize(new Dimension(i,a));
               p78.setPreferredSize(new Dimension(i,a));
               p79.setPreferredSize(new Dimension(i,a));
               p80.setPreferredSize(new Dimension(i,a));
               p81.setPreferredSize(new Dimension(i,a));
               p82.setPreferredSize(new Dimension(i,a));
               p83.setPreferredSize(new Dimension(i,a));
               p84.setPreferredSize(new Dimension(i,a));
               p85.setPreferredSize(new Dimension(i,a));
               p86.setPreferredSize(new Dimension(i,a));
               p87.setPreferredSize(new Dimension(i,a));
               p88.setPreferredSize(new Dimension(i,a));
               p89.setPreferredSize(new Dimension(i,a));
               p90.setPreferredSize(new Dimension(i,a));
               p91.setPreferredSize(new Dimension(i,a));
               p92.setPreferredSize(new Dimension(i,a));
               p93.setPreferredSize(new Dimension(i,a));
               p94.setPreferredSize(new Dimension(i,a));
               p95.setPreferredSize(new Dimension(i,a));
               p96.setPreferredSize(new Dimension(i,a));
               p97.setPreferredSize(new Dimension(i,a));
               p98.setPreferredSize(new Dimension(i,a));
               p99.setPreferredSize(new Dimension(i,a));
               p100.setPreferredSize(new Dimension(i,a));
      
               
        bigPanel.add(p1);
        bigPanel.add(p2);
        bigPanel.add(p3);
        bigPanel.add(p4);
        bigPanel.add(p5);
        bigPanel.add(p6);
        bigPanel.add(p7);
        bigPanel.add(p8);
        bigPanel.add(p9);
        bigPanel.add(p10);
        bigPanel.add(p11);
        bigPanel.add(p12);
        bigPanel.add(p13);
        bigPanel.add(p14);
        bigPanel.add(p15);
        bigPanel.add(p16);
        bigPanel.add(p17);
        bigPanel.add(p18);
        bigPanel.add(p19);
        bigPanel.add(p20);
        bigPanel.add(p21);
        bigPanel.add(p22);
        bigPanel.add(p23);
        bigPanel.add(p24);
        bigPanel.add(p25);
        bigPanel.add(p26);
        bigPanel.add(p27);
        bigPanel.add(p28);
        bigPanel.add(p29);
        bigPanel.add(p30);
        bigPanel.add(p31);
        bigPanel.add(p32);
        bigPanel.add(p33);
        bigPanel.add(p34);
        bigPanel.add(p35);
        bigPanel.add(p36);
        bigPanel.add(p37);
        bigPanel.add(p38);
        bigPanel.add(p39);
        bigPanel.add(p40);
        bigPanel.add(p41);
        bigPanel.add(p42);
        bigPanel.add(p43);
        bigPanel.add(p44);
        bigPanel.add(p45);
        bigPanel.add(p46);
        bigPanel.add(p47);
        bigPanel.add(p48);
        bigPanel.add(p49);
        bigPanel.add(p50);
        bigPanel.add(p51);
        bigPanel.add(p52);
        bigPanel.add(p53);
        bigPanel.add(p54);
        bigPanel.add(p55);
        bigPanel.add(p56);
        bigPanel.add(p57);
        bigPanel.add(p58);
        bigPanel.add(p59);
        bigPanel.add(p60);
        bigPanel.add(p61);
        bigPanel.add(p62);
        bigPanel.add(p63);
        bigPanel.add(p64);
        bigPanel.add(p65);
        bigPanel.add(p66);
        bigPanel.add(p67);
        bigPanel.add(p68);
        bigPanel.add(p69);
        bigPanel.add(p70);
        bigPanel.add(p71);
        bigPanel.add(p72);
        bigPanel.add(p73);
        bigPanel.add(p74);
        bigPanel.add(p75);
        bigPanel.add(p76);
        bigPanel.add(p77);
        bigPanel.add(p78);
        bigPanel.add(p79);
        bigPanel.add(p80);
        bigPanel.add(p81);
        bigPanel.add(p82);
        bigPanel.add(p83);
        bigPanel.add(p84);
        bigPanel.add(p85);
        bigPanel.add(p86);
        bigPanel.add(p87);
        bigPanel.add(p88);
        bigPanel.add(p89);
        bigPanel.add(p90);
        bigPanel.add(p91);
        bigPanel.add(p92);
        bigPanel.add(p93);
        bigPanel.add(p94);
        bigPanel.add(p95);
        bigPanel.add(p96);
        bigPanel.add(p97);
        bigPanel.add(p98);
        bigPanel.add(p99);
        bigPanel.add(p100);
        
        
    }
   
    public void loadpreviews(){
        methods.loadpreview(1, panelColor1);
        methods.loadpreview(2, panelColor2);
        methods.loadpreview(3, panelColor3);
        methods.loadpreview(4, panelColor4);
        methods.loadpreview(5, panelColor5);
        methods.loadpreview(6, panelColor6);
        methods.loadpreview(7, panelColor7);
        methods.loadpreview(8, panelColor8);
        methods.loadpreview(9, panelColor9);
        methods.loadpreview(10,panelColor10);
        methods.loadpreview(11,panelColor11);
        methods.loadpreview(12,panelColor12);
    }
    public int refreshTime(){
        if (com.alphamods.controlcenter.utils.config.getValue("refreshTime") == null || com.alphamods.controlcenter.utils.config.getValue("refreshTime") == ""){
          return 5;  
        }else {
            return Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("refreshTime"));
        }
    }
    private void initPanel(){
        loadpreviews();
        
        buttonGroup1.add(fadeRadioButton);
        buttonGroup1.add(normalRadioButton);
        buttonGroup1.add(musicRadioButton);
        normalRadioButton.setSelected(true);
        notificationsLabel.setVisible(false);
        notificationsBar.setVisible(false);
        if (!methods.isConnected()){
            fadeRadioButton.setEnabled(false);
            normalRadioButton.setEnabled(false);
            musicRadioButton.setEnabled(false);
            notConnectedLabel.setVisible(true);
            recordButton1.setEnabled(false);
            playButton1.setEnabled(false);
            Refresh2.setEnabled(false);
            picker.setEnabled(false);
            clearButton1.setEnabled(false);
        }
        refreshSecondsSpinner.setEnabled(false);
        jLabel19.setEnabled(false);
        jLabel18.setEnabled(false);
        
        PortsBox.removeAllItems();
        methods.getArduino().getSerialPorts().forEach(i -> PortsBox.addItem(i));
        this.ledModeLabel.setVisible(false);
        this.fadeRadioButton.setVisible(false);
        this.normalRadioButton.setVisible(false);
        this.musicRadioButton.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        tabPanel = new javax.swing.JTabbedPane();
        colorTab = new javax.swing.JPanel();
        picker = new com.bric.swing.ColorPicker();
        ledModeLabel = new javax.swing.JLabel();
        fadeRadioButton = new javax.swing.JRadioButton();
        normalRadioButton = new javax.swing.JRadioButton();
        musicRadioButton = new javax.swing.JRadioButton();
        colors_secuencesPanel = new javax.swing.JTabbedPane();
        favColorsPanel = new javax.swing.JPanel();
        labelColor1 = new javax.swing.JLabel();
        panelColor1 = new javax.swing.JPanel();
        labelColor6 = new javax.swing.JLabel();
        panelColor6 = new javax.swing.JPanel();
        getButtonColor6 = new javax.swing.JButton();
        setButtonColor6 = new javax.swing.JButton();
        getButtonColor1 = new javax.swing.JButton();
        setButtonColor1 = new javax.swing.JButton();
        labelColor2 = new javax.swing.JLabel();
        panelColor2 = new javax.swing.JPanel();
        setButtonColor2 = new javax.swing.JButton();
        labelColor3 = new javax.swing.JLabel();
        panelColor3 = new javax.swing.JPanel();
        getButtonColor2 = new javax.swing.JButton();
        labelColor7 = new javax.swing.JLabel();
        panelColor7 = new javax.swing.JPanel();
        getButtonColor7 = new javax.swing.JButton();
        setButtonColor7 = new javax.swing.JButton();
        setButtonColor8 = new javax.swing.JButton();
        labelColor8 = new javax.swing.JLabel();
        getButtonColor8 = new javax.swing.JButton();
        panelColor8 = new javax.swing.JPanel();
        getButtonColor3 = new javax.swing.JButton();
        setButtonColor3 = new javax.swing.JButton();
        labelColor4 = new javax.swing.JLabel();
        panelColor4 = new javax.swing.JPanel();
        labelColor9 = new javax.swing.JLabel();
        panelColor9 = new javax.swing.JPanel();
        getButtonColor9 = new javax.swing.JButton();
        setButtonColor9 = new javax.swing.JButton();
        getButtonColor4 = new javax.swing.JButton();
        setButtonColor4 = new javax.swing.JButton();
        labelColor5 = new javax.swing.JLabel();
        panelColor5 = new javax.swing.JPanel();
        getButtonColor5 = new javax.swing.JButton();
        setButtonColor5 = new javax.swing.JButton();
        setButtonColor10 = new javax.swing.JButton();
        labelColor10 = new javax.swing.JLabel();
        panelColor10 = new javax.swing.JPanel();
        getButtonColor10 = new javax.swing.JButton();
        cleanButton = new javax.swing.JButton();
        labelColor11 = new javax.swing.JLabel();
        getButtonColor11 = new javax.swing.JButton();
        panelColor11 = new javax.swing.JPanel();
        setButtonColor11 = new javax.swing.JButton();
        labelColor12 = new javax.swing.JLabel();
        getButtonColor12 = new javax.swing.JButton();
        panelColor12 = new javax.swing.JPanel();
        setButtonColor12 = new javax.swing.JButton();
        SecuencesPanel = new javax.swing.JPanel();
        SecuencesTitle = new javax.swing.JLabel();
        bigpanel1 = new javax.swing.JPanel();
        recordButton1 = new javax.swing.JToggleButton();
        clearButton1 = new javax.swing.JButton();
        sec1label = new javax.swing.JLabel();
        sec2label = new javax.swing.JLabel();
        recordButton2 = new javax.swing.JToggleButton();
        playButton2 = new javax.swing.JButton();
        clearButton2 = new javax.swing.JButton();
        bigpanel2 = new javax.swing.JPanel();
        sec3label = new javax.swing.JLabel();
        recordButton3 = new javax.swing.JToggleButton();
        playButton3 = new javax.swing.JButton();
        clearButton3 = new javax.swing.JButton();
        bigpanel3 = new javax.swing.JPanel();
        sec4label = new javax.swing.JLabel();
        bigpanel4 = new javax.swing.JPanel();
        recordButton4 = new javax.swing.JToggleButton();
        playButton4 = new javax.swing.JButton();
        clearButton4 = new javax.swing.JButton();
        sec5label = new javax.swing.JLabel();
        bigpanel5 = new javax.swing.JPanel();
        recordButton5 = new javax.swing.JToggleButton();
        playButton5 = new javax.swing.JButton();
        clearButton5 = new javax.swing.JButton();
        loopCheckBox = new javax.swing.JCheckBox();
        playButton1 = new javax.swing.JToggleButton();
        jLabel17 = new javax.swing.JLabel();
        LedC1 = new javax.swing.JCheckBox();
        LedC2 = new javax.swing.JCheckBox();
        LedC3 = new javax.swing.JCheckBox();
        LedC4 = new javax.swing.JCheckBox();
        testMode = new javax.swing.JToggleButton();
        FanPumpPanel = new javax.swing.JPanel();
        fan1label = new javax.swing.JLabel();
        fan1slider = new javax.swing.JSlider();
        pump1label = new javax.swing.JLabel();
        pump2label = new javax.swing.JLabel();
        fan2slider = new javax.swing.JSlider();
        pump1slider = new javax.swing.JSlider();
        fansTitleLabel = new javax.swing.JLabel();
        pumpsTitleLabel = new javax.swing.JLabel();
        rpmlabel1 = new javax.swing.JLabel();
        rpmlabel3 = new javax.swing.JLabel();
        rpmlabel4 = new javax.swing.JLabel();
        rmpLabelFan1 = new javax.swing.JTextField();
        rmpLabelPump1 = new javax.swing.JTextField();
        rmpLabelFan2 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        Temp1 = new javax.swing.JTextField();
        Temp2 = new javax.swing.JTextField();
        Temp3 = new javax.swing.JTextField();
        Temp4 = new javax.swing.JTextField();
        Temp5 = new javax.swing.JTextField();
        Temp6 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        Refresh2 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        pump1max = new javax.swing.JTextField();
        fan1max = new javax.swing.JTextField();
        fan2max = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        RefreshCheckBox = new javax.swing.JCheckBox();
        refreshSecondsSpinner = new javax.swing.JSpinner();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        refreshPortsButton = new javax.swing.JButton();
        PortsBox = new javax.swing.JComboBox();
        connectButton = new javax.swing.JButton();
        sendbutton = new javax.swing.JButton();
        notConnectedLabel = new javax.swing.JLabel();
        notificationsLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        notificationsBar = new javax.swing.JProgressBar();
        menubar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        settings = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Alpha Control Center");
        setBackground(new java.awt.Color(66, 66, 66));
        setIconImages(null);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        picker.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                pickerPropertyChange(evt);
            }
        });

        ledModeLabel.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        ledModeLabel.setText("LED Mode");

        fadeRadioButton.setText("Fade");
        fadeRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                fadeRadioButtonItemStateChanged(evt);
            }
        });
        fadeRadioButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                fadeRadioButtonStateChanged(evt);
            }
        });

        normalRadioButton.setText("Normal");
        normalRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                normalRadioButtonItemStateChanged(evt);
            }
        });
        normalRadioButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                normalRadioButtonStateChanged(evt);
            }
        });
        normalRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                normalRadioButtonActionPerformed(evt);
            }
        });

        musicRadioButton.setText("Music");
        musicRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                musicRadioButtonItemStateChanged(evt);
            }
        });
        musicRadioButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                musicRadioButtonStateChanged(evt);
            }
        });

        favColorsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder("Favourite colors")));

        labelColor1.setText("Color 1");

        panelColor1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout panelColor1Layout = new javax.swing.GroupLayout(panelColor1);
        panelColor1.setLayout(panelColor1Layout);
        panelColor1Layout.setHorizontalGroup(
            panelColor1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 49, Short.MAX_VALUE)
        );
        panelColor1Layout.setVerticalGroup(
            panelColor1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );

        labelColor6.setText("Color 6");

        panelColor6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout panelColor6Layout = new javax.swing.GroupLayout(panelColor6);
        panelColor6.setLayout(panelColor6Layout);
        panelColor6Layout.setHorizontalGroup(
            panelColor6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 49, Short.MAX_VALUE)
        );
        panelColor6Layout.setVerticalGroup(
            panelColor6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );

        getButtonColor6.setText("Get");
        getButtonColor6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getButtonColor6ActionPerformed(evt);
            }
        });

        setButtonColor6.setText("Set");
        setButtonColor6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setButtonColor6ActionPerformed(evt);
            }
        });

        getButtonColor1.setText("Get");
        getButtonColor1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getButtonColor1ActionPerformed(evt);
            }
        });

        setButtonColor1.setText("Set");
        setButtonColor1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setButtonColor1ActionPerformed(evt);
            }
        });

        labelColor2.setText("Color 2");

        panelColor2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout panelColor2Layout = new javax.swing.GroupLayout(panelColor2);
        panelColor2.setLayout(panelColor2Layout);
        panelColor2Layout.setHorizontalGroup(
            panelColor2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 49, Short.MAX_VALUE)
        );
        panelColor2Layout.setVerticalGroup(
            panelColor2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );

        setButtonColor2.setText("Set");
        setButtonColor2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setButtonColor2ActionPerformed(evt);
            }
        });

        labelColor3.setText("Color 3");

        panelColor3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout panelColor3Layout = new javax.swing.GroupLayout(panelColor3);
        panelColor3.setLayout(panelColor3Layout);
        panelColor3Layout.setHorizontalGroup(
            panelColor3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 49, Short.MAX_VALUE)
        );
        panelColor3Layout.setVerticalGroup(
            panelColor3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );

        getButtonColor2.setText("Get");
        getButtonColor2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getButtonColor2ActionPerformed(evt);
            }
        });

        labelColor7.setText("Color 7");

        panelColor7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout panelColor7Layout = new javax.swing.GroupLayout(panelColor7);
        panelColor7.setLayout(panelColor7Layout);
        panelColor7Layout.setHorizontalGroup(
            panelColor7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 49, Short.MAX_VALUE)
        );
        panelColor7Layout.setVerticalGroup(
            panelColor7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );

        getButtonColor7.setText("Get");
        getButtonColor7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getButtonColor7ActionPerformed(evt);
            }
        });

        setButtonColor7.setText("Set");
        setButtonColor7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setButtonColor7ActionPerformed(evt);
            }
        });

        setButtonColor8.setText("Set");
        setButtonColor8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setButtonColor8ActionPerformed(evt);
            }
        });

        labelColor8.setText("Color 8");

        getButtonColor8.setText("Get");
        getButtonColor8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getButtonColor8ActionPerformed(evt);
            }
        });

        panelColor8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout panelColor8Layout = new javax.swing.GroupLayout(panelColor8);
        panelColor8.setLayout(panelColor8Layout);
        panelColor8Layout.setHorizontalGroup(
            panelColor8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 49, Short.MAX_VALUE)
        );
        panelColor8Layout.setVerticalGroup(
            panelColor8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );

        getButtonColor3.setText("Get");
        getButtonColor3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getButtonColor3ActionPerformed(evt);
            }
        });

        setButtonColor3.setText("Set");
        setButtonColor3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setButtonColor3ActionPerformed(evt);
            }
        });

        labelColor4.setText("Color 4");

        panelColor4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout panelColor4Layout = new javax.swing.GroupLayout(panelColor4);
        panelColor4.setLayout(panelColor4Layout);
        panelColor4Layout.setHorizontalGroup(
            panelColor4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 49, Short.MAX_VALUE)
        );
        panelColor4Layout.setVerticalGroup(
            panelColor4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );

        labelColor9.setText("Color 9");

        panelColor9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout panelColor9Layout = new javax.swing.GroupLayout(panelColor9);
        panelColor9.setLayout(panelColor9Layout);
        panelColor9Layout.setHorizontalGroup(
            panelColor9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 49, Short.MAX_VALUE)
        );
        panelColor9Layout.setVerticalGroup(
            panelColor9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );

        getButtonColor9.setText("Get");
        getButtonColor9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getButtonColor9ActionPerformed(evt);
            }
        });

        setButtonColor9.setText("Set");
        setButtonColor9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setButtonColor9ActionPerformed(evt);
            }
        });

        getButtonColor4.setText("Get");
        getButtonColor4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getButtonColor4ActionPerformed(evt);
            }
        });

        setButtonColor4.setText("Set");
        setButtonColor4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setButtonColor4ActionPerformed(evt);
            }
        });

        labelColor5.setText("Color 5");

        panelColor5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout panelColor5Layout = new javax.swing.GroupLayout(panelColor5);
        panelColor5.setLayout(panelColor5Layout);
        panelColor5Layout.setHorizontalGroup(
            panelColor5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 49, Short.MAX_VALUE)
        );
        panelColor5Layout.setVerticalGroup(
            panelColor5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );

        getButtonColor5.setText("Get");
        getButtonColor5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getButtonColor5ActionPerformed(evt);
            }
        });

        setButtonColor5.setText("Set");
        setButtonColor5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setButtonColor5ActionPerformed(evt);
            }
        });

        setButtonColor10.setText("Set");
        setButtonColor10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setButtonColor10ActionPerformed(evt);
            }
        });

        labelColor10.setText("Color 10");

        panelColor10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout panelColor10Layout = new javax.swing.GroupLayout(panelColor10);
        panelColor10.setLayout(panelColor10Layout);
        panelColor10Layout.setHorizontalGroup(
            panelColor10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 49, Short.MAX_VALUE)
        );
        panelColor10Layout.setVerticalGroup(
            panelColor10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );

        getButtonColor10.setText("Get");
        getButtonColor10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getButtonColor10ActionPerformed(evt);
            }
        });

        cleanButton.setText("Clean all");
        cleanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cleanButtonActionPerformed(evt);
            }
        });

        labelColor11.setText("Color 11");

        getButtonColor11.setText("Get");
        getButtonColor11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getButtonColor11ActionPerformed(evt);
            }
        });

        panelColor11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout panelColor11Layout = new javax.swing.GroupLayout(panelColor11);
        panelColor11.setLayout(panelColor11Layout);
        panelColor11Layout.setHorizontalGroup(
            panelColor11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 49, Short.MAX_VALUE)
        );
        panelColor11Layout.setVerticalGroup(
            panelColor11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );

        setButtonColor11.setText("Set");
        setButtonColor11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setButtonColor11ActionPerformed(evt);
            }
        });

        labelColor12.setText("Color 12");

        getButtonColor12.setText("Get");
        getButtonColor12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getButtonColor12ActionPerformed(evt);
            }
        });

        panelColor12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout panelColor12Layout = new javax.swing.GroupLayout(panelColor12);
        panelColor12.setLayout(panelColor12Layout);
        panelColor12Layout.setHorizontalGroup(
            panelColor12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 49, Short.MAX_VALUE)
        );
        panelColor12Layout.setVerticalGroup(
            panelColor12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );

        setButtonColor12.setText("Set");
        setButtonColor12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setButtonColor12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout favColorsPanelLayout = new javax.swing.GroupLayout(favColorsPanel);
        favColorsPanel.setLayout(favColorsPanelLayout);
        favColorsPanelLayout.setHorizontalGroup(
            favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(favColorsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(favColorsPanelLayout.createSequentialGroup()
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelColor1)
                            .addComponent(setButtonColor1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelColor1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(getButtonColor1)))
                    .addGroup(favColorsPanelLayout.createSequentialGroup()
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelColor2)
                            .addComponent(labelColor3)
                            .addComponent(labelColor4)
                            .addComponent(labelColor5)
                            .addComponent(setButtonColor3)
                            .addComponent(setButtonColor4)
                            .addComponent(setButtonColor5)
                            .addComponent(setButtonColor2))
                        .addGap(7, 7, 7)
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelColor2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(getButtonColor2)
                            .addComponent(getButtonColor3)
                            .addComponent(panelColor3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(getButtonColor4)
                            .addComponent(panelColor4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(getButtonColor5)
                            .addComponent(panelColor5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, favColorsPanelLayout.createSequentialGroup()
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelColor6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(setButtonColor6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(getButtonColor6)
                            .addComponent(panelColor6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(favColorsPanelLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(favColorsPanelLayout.createSequentialGroup()
                                .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelColor10)
                                    .addComponent(labelColor8)
                                    .addComponent(labelColor9)
                                    .addComponent(setButtonColor7)
                                    .addComponent(setButtonColor8)
                                    .addComponent(setButtonColor9)
                                    .addComponent(setButtonColor10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(getButtonColor9)
                                    .addComponent(panelColor10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(getButtonColor7)
                                    .addComponent(panelColor8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(getButtonColor8)
                                    .addComponent(panelColor9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(getButtonColor10)))
                            .addGroup(favColorsPanelLayout.createSequentialGroup()
                                .addComponent(labelColor7)
                                .addGap(18, 18, 18)
                                .addComponent(panelColor7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(10, 10, 10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, favColorsPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, favColorsPanelLayout.createSequentialGroup()
                                .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelColor11)
                                    .addComponent(setButtonColor11))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panelColor11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(getButtonColor11)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, favColorsPanelLayout.createSequentialGroup()
                                .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelColor12)
                                    .addComponent(setButtonColor12))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panelColor12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(getButtonColor12))))
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, favColorsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cleanButton)
                .addGap(100, 100, 100))
        );
        favColorsPanelLayout.setVerticalGroup(
            favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(favColorsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(favColorsPanelLayout.createSequentialGroup()
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(panelColor1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelColor1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(setButtonColor1)
                            .addComponent(getButtonColor1))
                        .addGap(18, 18, 18)
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(panelColor2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelColor2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(setButtonColor2)
                            .addComponent(getButtonColor2))
                        .addGap(18, 18, 18)
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(panelColor3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelColor3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(setButtonColor3)
                            .addComponent(getButtonColor3))
                        .addGap(18, 18, 18)
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(panelColor4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelColor4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(setButtonColor4)
                            .addComponent(getButtonColor4)))
                    .addGroup(favColorsPanelLayout.createSequentialGroup()
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(panelColor7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelColor7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(setButtonColor7)
                            .addComponent(getButtonColor7))
                        .addGap(18, 18, 18)
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(panelColor8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelColor8, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(setButtonColor8)
                            .addComponent(getButtonColor8))
                        .addGap(18, 18, 18)
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(panelColor9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelColor9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(setButtonColor9)
                            .addComponent(getButtonColor9))
                        .addGap(18, 18, 18)
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(panelColor10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelColor10, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(setButtonColor10)
                            .addComponent(getButtonColor10))))
                .addGap(18, 18, 18)
                .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(favColorsPanelLayout.createSequentialGroup()
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(panelColor5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelColor5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(setButtonColor5)
                            .addComponent(getButtonColor5)))
                    .addGroup(favColorsPanelLayout.createSequentialGroup()
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(panelColor11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelColor11, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(setButtonColor11)
                            .addComponent(getButtonColor11))))
                .addGap(18, 18, 18)
                .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(favColorsPanelLayout.createSequentialGroup()
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(panelColor6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelColor6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(setButtonColor6)
                            .addComponent(getButtonColor6)))
                    .addGroup(favColorsPanelLayout.createSequentialGroup()
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(panelColor12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelColor12, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(favColorsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(setButtonColor12)
                            .addComponent(getButtonColor12))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                .addComponent(cleanButton)
                .addContainerGap())
        );

        colors_secuencesPanel.addTab("Saved colors", favColorsPanel);

        SecuencesTitle.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        SecuencesTitle.setText("Secuences");

        bigpanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        bigpanel1.setPreferredSize(new java.awt.Dimension(100, 37));
        bigpanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        recordButton1.setText("Record");
        recordButton1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                recordButton1ItemStateChanged(evt);
            }
        });

        clearButton1.setText("Clear");
        clearButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButton1ActionPerformed(evt);
            }
        });

        sec1label.setText("Sec. 1");

        sec2label.setText("Sec. 2");

        recordButton2.setText("Record");
        recordButton2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                recordButton2ItemStateChanged(evt);
            }
        });

        playButton2.setText("Play");
        playButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButton2ActionPerformed(evt);
            }
        });

        clearButton2.setText("Clear");
        clearButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButton2ActionPerformed(evt);
            }
        });

        bigpanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        bigpanel2.setPreferredSize(new java.awt.Dimension(100, 37));
        bigpanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        sec3label.setText("Sec. 3");

        recordButton3.setText("Record");
        recordButton3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                recordButton3ItemStateChanged(evt);
            }
        });

        playButton3.setText("Play");
        playButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButton3ActionPerformed(evt);
            }
        });

        clearButton3.setText("Clear");
        clearButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButton3ActionPerformed(evt);
            }
        });

        bigpanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        bigpanel3.setPreferredSize(new java.awt.Dimension(100, 37));
        bigpanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        sec4label.setText("Sec. 4");

        bigpanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        bigpanel4.setPreferredSize(new java.awt.Dimension(100, 37));
        bigpanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        recordButton4.setText("Record");
        recordButton4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                recordButton4ItemStateChanged(evt);
            }
        });

        playButton4.setText("Play");
        playButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButton4ActionPerformed(evt);
            }
        });

        clearButton4.setText("Clear");
        clearButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButton4ActionPerformed(evt);
            }
        });

        sec5label.setText("Sec. 5");

        bigpanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        bigpanel5.setPreferredSize(new java.awt.Dimension(100, 37));
        bigpanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        recordButton5.setText("Record");
        recordButton5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                recordButton5ItemStateChanged(evt);
            }
        });

        playButton5.setText("Play");
        playButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButton5ActionPerformed(evt);
            }
        });

        clearButton5.setText("Clear");
        clearButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButton5ActionPerformed(evt);
            }
        });

        loopCheckBox.setText("Loop");
        loopCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                loopCheckBoxItemStateChanged(evt);
            }
        });

        playButton1.setText("Play");
        playButton1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                playButton1ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout SecuencesPanelLayout = new javax.swing.GroupLayout(SecuencesPanel);
        SecuencesPanel.setLayout(SecuencesPanelLayout);
        SecuencesPanelLayout.setHorizontalGroup(
            SecuencesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SecuencesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SecuencesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(SecuencesPanelLayout.createSequentialGroup()
                        .addComponent(sec2label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(SecuencesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(bigpanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(SecuencesPanelLayout.createSequentialGroup()
                                .addComponent(recordButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(playButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(clearButton2))))
                    .addGroup(SecuencesPanelLayout.createSequentialGroup()
                        .addComponent(loopCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bigpanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(SecuencesPanelLayout.createSequentialGroup()
                        .addComponent(sec1label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(recordButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(playButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearButton1)))
                .addGap(59, 59, 59))
            .addGroup(SecuencesPanelLayout.createSequentialGroup()
                .addGroup(SecuencesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(SecuencesPanelLayout.createSequentialGroup()
                        .addGap(114, 114, 114)
                        .addComponent(SecuencesTitle))
                    .addGroup(SecuencesPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(sec3label)
                        .addGap(24, 24, 24)
                        .addGroup(SecuencesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bigpanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(SecuencesPanelLayout.createSequentialGroup()
                                .addComponent(recordButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(playButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(clearButton3))))
                    .addGroup(SecuencesPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(sec4label)
                        .addGap(24, 24, 24)
                        .addGroup(SecuencesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bigpanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(SecuencesPanelLayout.createSequentialGroup()
                                .addComponent(recordButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(playButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(clearButton4))))
                    .addGroup(SecuencesPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(sec5label)
                        .addGap(24, 24, 24)
                        .addGroup(SecuencesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bigpanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(SecuencesPanelLayout.createSequentialGroup()
                                .addComponent(recordButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(playButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(clearButton5)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        SecuencesPanelLayout.setVerticalGroup(
            SecuencesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SecuencesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SecuencesTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SecuencesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(recordButton1)
                    .addComponent(clearButton1)
                    .addComponent(sec1label)
                    .addComponent(playButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SecuencesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bigpanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(loopCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(SecuencesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(recordButton2)
                    .addComponent(playButton2)
                    .addComponent(clearButton2)
                    .addComponent(sec2label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bigpanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(SecuencesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(recordButton3)
                    .addComponent(playButton3)
                    .addComponent(clearButton3)
                    .addComponent(sec3label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bigpanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(SecuencesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(recordButton4)
                    .addComponent(playButton4)
                    .addComponent(clearButton4)
                    .addComponent(sec4label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bigpanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(SecuencesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(recordButton5)
                    .addComponent(playButton5)
                    .addComponent(clearButton5)
                    .addComponent(sec5label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bigpanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(137, Short.MAX_VALUE))
        );

        colors_secuencesPanel.addTab("Secuences", SecuencesPanel);

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel17.setText("LED Channels");

        LedC1.setText("Channel 1");

        LedC2.setText("Channel 2");

        LedC3.setText("Channel 3");
        LedC3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LedC3ActionPerformed(evt);
            }
        });

        LedC4.setText("Channel 4");

        testMode.setText("Test Mode");
        testMode.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                testModeItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout colorTabLayout = new javax.swing.GroupLayout(colorTab);
        colorTab.setLayout(colorTabLayout);
        colorTabLayout.setHorizontalGroup(
            colorTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(colorTabLayout.createSequentialGroup()
                .addGroup(colorTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(colorTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(picker, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, colorTabLayout.createSequentialGroup()
                            .addGap(74, 74, 74)
                            .addGroup(colorTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, colorTabLayout.createSequentialGroup()
                                    .addComponent(LedC1)
                                    .addGap(18, 18, 18)
                                    .addComponent(LedC2)
                                    .addGap(18, 18, 18)
                                    .addComponent(LedC3)
                                    .addGap(18, 18, 18)
                                    .addComponent(LedC4))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, colorTabLayout.createSequentialGroup()
                                    .addGap(133, 133, 133)
                                    .addComponent(jLabel17)))))
                    .addComponent(testMode)
                    .addGroup(colorTabLayout.createSequentialGroup()
                        .addGap(125, 125, 125)
                        .addGroup(colorTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(ledModeLabel)
                            .addGroup(colorTabLayout.createSequentialGroup()
                                .addComponent(fadeRadioButton)
                                .addGap(35, 35, 35)
                                .addComponent(normalRadioButton)))
                        .addGap(36, 36, 36)
                        .addComponent(musicRadioButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(colors_secuencesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
        );
        colorTabLayout.setVerticalGroup(
            colorTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(colorTabLayout.createSequentialGroup()
                .addComponent(picker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58)
                .addComponent(jLabel17)
                .addGap(18, 18, 18)
                .addGroup(colorTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LedC1)
                    .addComponent(LedC2)
                    .addComponent(LedC3)
                    .addComponent(LedC4))
                .addGap(70, 70, 70)
                .addComponent(ledModeLabel)
                .addGap(18, 18, 18)
                .addGroup(colorTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(normalRadioButton)
                    .addComponent(musicRadioButton)
                    .addComponent(fadeRadioButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(testMode))
            .addGroup(colorTabLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(colors_secuencesPanel)
                .addContainerGap())
        );

        tabPanel.addTab("Color", colorTab);

        fan1label.setText("Channel 1");

        fan1slider.setPaintTicks(true);
        fan1slider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                fan1sliderStateChanged(evt);
            }
        });

        pump1label.setText("Pump 1");

        pump2label.setText("Channel 2");

        fan2slider.setPaintTicks(true);
        fan2slider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                fan2sliderStateChanged(evt);
            }
        });

        pump1slider.setPaintTicks(true);
        pump1slider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                pump1sliderStateChanged(evt);
            }
        });

        fansTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        fansTitleLabel.setText("Fans");

        pumpsTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        pumpsTitleLabel.setText("Pumps");

        rpmlabel1.setText("RPM");

        rpmlabel3.setText("RPM");

        rpmlabel4.setText("RPM");

        rmpLabelFan1.setEditable(false);
        rmpLabelFan1.setText("0000");

        rmpLabelPump1.setEditable(false);
        rmpLabelPump1.setText("0000");

        rmpLabelFan2.setEditable(false);
        rmpLabelFan2.setText("0000");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel1.setText("Temperatures");

        Temp1.setEditable(false);
        Temp1.setText("0");

        Temp2.setEditable(false);
        Temp2.setText("0");

        Temp3.setEditable(false);
        Temp3.setText("0");

        Temp4.setEditable(false);
        Temp4.setText("0");

        Temp5.setEditable(false);
        Temp5.setText("0");

        Temp6.setEditable(false);
        Temp6.setText("0");

        jLabel2.setFont(new java.awt.Font("Ubuntu", 1, 11)); // NOI18N
        jLabel2.setText("Temp 1");

        jLabel3.setFont(new java.awt.Font("Ubuntu", 1, 11)); // NOI18N
        jLabel3.setText("Temp 2");

        jLabel4.setFont(new java.awt.Font("Ubuntu", 1, 11)); // NOI18N
        jLabel4.setText("Temp 3");

        jLabel5.setFont(new java.awt.Font("Ubuntu", 1, 11)); // NOI18N
        jLabel5.setText("Temp 4");

        jLabel6.setFont(new java.awt.Font("Ubuntu", 1, 11)); // NOI18N
        jLabel6.setText("Temp 5");

        jLabel7.setFont(new java.awt.Font("Ubuntu", 1, 11)); // NOI18N
        jLabel7.setText("Temp 6");

        jLabel8.setText("ºC");

        jLabel9.setText("ºC");

        jLabel10.setText("ºC");

        jLabel11.setText("ºC");

        jLabel12.setText("ºC");

        jLabel13.setText("ºC");

        Refresh2.setText("Refresh");
        Refresh2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Refresh2ActionPerformed(evt);
            }
        });

        jLabel14.setText("Max. RPM Channel 1");

        jLabel15.setText("Max. RPM Channel 2");

        jLabel16.setText("Max. RPM");

        pump1max.setText("0");

        fan1max.setText("0");

        fan2max.setText("0");

        jLabel18.setText("Refresh each");

        jLabel19.setText("seconds");

        RefreshCheckBox.setText("Refresh automatically");
        RefreshCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshCheckBoxActionPerformed(evt);
            }
        });

        refreshSecondsSpinner.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(5L), Long.valueOf(2L), null, Long.valueOf(1L)));
        refreshSecondsSpinner.setToolTipText("");
        refreshSecondsSpinner.setValue(refreshTime());
        refreshSecondsSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                refreshSecondsSpinnerStateChanged(evt);
            }
        });

        javax.swing.GroupLayout FanPumpPanelLayout = new javax.swing.GroupLayout(FanPumpPanel);
        FanPumpPanel.setLayout(FanPumpPanelLayout);
        FanPumpPanelLayout.setHorizontalGroup(
            FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FanPumpPanelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(fansTitleLabel)
                    .addGroup(FanPumpPanelLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fan1label)
                            .addComponent(pump2label)
                            .addGroup(FanPumpPanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(fan2slider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rmpLabelFan2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rpmlabel4))
                            .addGroup(FanPumpPanelLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(fan1slider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rmpLabelFan1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rpmlabel1))
                            .addGroup(FanPumpPanelLayout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(fan1max, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(FanPumpPanelLayout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(fan2max, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(FanPumpPanelLayout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pump1max, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(pumpsTitleLabel)
                    .addGroup(FanPumpPanelLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pump1label)
                            .addGroup(FanPumpPanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(pump1slider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rmpLabelPump1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rpmlabel3)))))
                .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(FanPumpPanelLayout.createSequentialGroup()
                        .addGap(197, 197, 197)
                        .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(FanPumpPanelLayout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addGroup(FanPumpPanelLayout.createSequentialGroup()
                                        .addComponent(jLabel13)
                                        .addGap(86, 86, 86)
                                        .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(Temp2)
                                            .addComponent(jLabel3))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel9))))
                            .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(FanPumpPanelLayout.createSequentialGroup()
                                    .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(Temp1))
                                    .addGap(159, 159, 159))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FanPumpPanelLayout.createSequentialGroup()
                                    .addGap(0, 0, Short.MAX_VALUE)
                                    .addComponent(RefreshCheckBox)
                                    .addGap(34, 34, 34))
                                .addGroup(FanPumpPanelLayout.createSequentialGroup()
                                    .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(Temp5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(FanPumpPanelLayout.createSequentialGroup()
                                            .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(Temp3)
                                                .addComponent(jLabel4))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel10)
                                            .addGap(86, 86, 86)
                                            .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(Temp4)
                                                .addComponent(jLabel5))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel12))
                                        .addGroup(FanPumpPanelLayout.createSequentialGroup()
                                            .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(FanPumpPanelLayout.createSequentialGroup()
                                                    .addGap(43, 43, 43)
                                                    .addComponent(jLabel11))
                                                .addComponent(jLabel6))
                                            .addGap(86, 86, 86)
                                            .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(Temp6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel8))
                                        .addGroup(FanPumpPanelLayout.createSequentialGroup()
                                            .addGap(3, 3, 3)
                                            .addComponent(jLabel18)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(refreshSecondsSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jLabel19)))
                                    .addGap(0, 0, Short.MAX_VALUE))))
                        .addGap(91, 91, 91))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FanPumpPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Refresh2)
                        .addGap(160, 160, 160))))
        );
        FanPumpPanelLayout.setVerticalGroup(
            FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FanPumpPanelLayout.createSequentialGroup()
                .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(FanPumpPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(FanPumpPanelLayout.createSequentialGroup()
                                .addGap(318, 318, 318)
                                .addComponent(Refresh2)
                                .addGap(18, 18, 18)
                                .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(FanPumpPanelLayout.createSequentialGroup()
                                        .addComponent(RefreshCheckBox)
                                        .addGap(34, 34, 34))
                                    .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel18)
                                        .addComponent(jLabel19)
                                        .addComponent(refreshSecondsSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(FanPumpPanelLayout.createSequentialGroup()
                                .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(FanPumpPanelLayout.createSequentialGroup()
                                        .addGap(61, 61, 61)
                                        .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel3))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(Temp1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(Temp2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel9)
                                            .addComponent(jLabel13))
                                        .addGap(133, 133, 133)
                                        .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel6)
                                            .addComponent(jLabel7))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(Temp5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(Temp6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel11)
                                            .addComponent(jLabel8)))
                                    .addGroup(FanPumpPanelLayout.createSequentialGroup()
                                        .addGap(148, 148, 148)
                                        .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel5))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(Temp3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(Temp4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel10)
                                            .addComponent(jLabel12))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 192, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(FanPumpPanelLayout.createSequentialGroup()
                        .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(FanPumpPanelLayout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(fansTitleLabel)
                                .addGap(214, 214, 214)
                                .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel14)
                                    .addComponent(fan1max, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(FanPumpPanelLayout.createSequentialGroup()
                                .addGap(81, 81, 81)
                                .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(FanPumpPanelLayout.createSequentialGroup()
                                        .addComponent(fan1label)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(fan1slider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(rmpLabelFan1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rpmlabel1))
                                .addGap(36, 36, 36)
                                .addComponent(pump2label)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(fan2slider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rmpLabelFan2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rpmlabel4))))
                        .addGap(44, 44, 44)
                        .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(fan2max, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(rmpLabelPump1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(FanPumpPanelLayout.createSequentialGroup()
                                .addComponent(pumpsTitleLabel)
                                .addGap(26, 26, 26)
                                .addComponent(pump1label)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pump1slider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(rpmlabel3))
                        .addGap(39, 39, 39)))
                .addGap(24, 24, 24)
                .addGroup(FanPumpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(pump1max, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(53, 53, 53))
        );

        tabPanel.addTab("Fans & Pumps", FanPumpPanel);

        jButton1.setText("Start Ambilight!");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(546, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(444, Short.MAX_VALUE))
        );

        tabPanel.addTab("Ambilight", jPanel2);

        refreshPortsButton.setText("Refresh");
        refreshPortsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshPortsButtonActionPerformed(evt);
            }
        });

        PortsBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        PortsBox.setToolTipText("Avaliable Arduino Ports");

        connectButton.setText("Connect");
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });

        sendbutton.setText("Send");
        sendbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendbuttonActionPerformed(evt);
            }
        });

        notConnectedLabel.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        notConnectedLabel.setForeground(new java.awt.Color(225, 2, 27));
        notConnectedLabel.setText("Not connected!!");

        notificationsLabel.setText("jLabel20");

        jPanel1.setPreferredSize(new java.awt.Dimension(146, 14));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 146, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(notificationsBar, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 14, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(notificationsBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jMenu1.setText("File");

        settings.setText("Settings");
        settings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsActionPerformed(evt);
            }
        });
        jMenu1.add(settings);

        menubar.add(jMenu1);

        jMenu2.setText("Edit");
        menubar.add(jMenu2);

        setJMenuBar(menubar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tabPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 820, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(PortsBox, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshPortsButton)
                        .addGap(196, 196, 196)
                        .addComponent(notConnectedLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(sendbutton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(connectButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(notificationsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(notificationsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 621, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PortsBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshPortsButton)
                    .addComponent(connectButton)
                    .addComponent(sendbutton)
                    .addComponent(notConnectedLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    
    private void getButtonColor6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getButtonColor6ActionPerformed
        int color6R = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color6R"));
        int color6G = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color6G"));
        int color6B = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color6B"));
        Color color6 = new Color(color6R,color6G,color6B);
        picker.setColor(color6);

        // TODO add your handling code here:
    }//GEN-LAST:event_getButtonColor6ActionPerformed

    private void setButtonColor6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setButtonColor6ActionPerformed
        Color color6 = picker.getColor();
        panelColor6.setBackground(color6);
        com.alphamods.controlcenter.utils.config.setValue("color6R", Integer.toString(color6.getRed()));
        com.alphamods.controlcenter.utils.config.setValue("color6G", Integer.toString(color6.getGreen()));
        com.alphamods.controlcenter.utils.config.setValue("color6B", Integer.toString(color6.getBlue()));

        // TODO add your handling code here:
    }//GEN-LAST:event_setButtonColor6ActionPerformed

    private void getButtonColor1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getButtonColor1ActionPerformed
        int color1R = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color1R"));
        int color1G = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color1G"));
        int color1B = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color1B"));
        Color color1 = new Color(color1R,color1G,color1B);
        picker.setColor(color1);

        // TODO add your handling code here:
    }//GEN-LAST:event_getButtonColor1ActionPerformed

    private void setButtonColor1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setButtonColor1ActionPerformed
        Color color1 = picker.getColor();
        panelColor1.setBackground(color1);
        com.alphamods.controlcenter.utils.config.setValue("color1R", Integer.toString(color1.getRed()));
        com.alphamods.controlcenter.utils.config.setValue("color1G", Integer.toString(color1.getGreen()));
        com.alphamods.controlcenter.utils.config.setValue("color1B", Integer.toString(color1.getBlue()));

        // TODO add your handling code here:
    }//GEN-LAST:event_setButtonColor1ActionPerformed

    private void setButtonColor2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setButtonColor2ActionPerformed
        Color color2 = picker.getColor();
        panelColor2.setBackground(color2);
        com.alphamods.controlcenter.utils.config.setValue("color2R", Integer.toString(color2.getRed()));
        com.alphamods.controlcenter.utils.config.setValue("color2G", Integer.toString(color2.getGreen()));
        com.alphamods.controlcenter.utils.config.setValue("color2B", Integer.toString(color2.getBlue()));

        // TODO add your handling code here:
    }//GEN-LAST:event_setButtonColor2ActionPerformed

    private void getButtonColor2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getButtonColor2ActionPerformed
        int color2R = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color2R"));
        int color2G = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color2G"));
        int color2B = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color2B"));
        Color color2 = new Color(color2R,color2G,color2B);
        picker.setColor(color2);

        // TODO add your handling code here:
    }//GEN-LAST:event_getButtonColor2ActionPerformed

    private void getButtonColor7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getButtonColor7ActionPerformed
        int color7R = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color7R"));
        int color7G = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color7G"));
        int color7B = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color7B"));
        Color color7 = new Color(color7R,color7G,color7B);
        picker.setColor(color7);

        // TODO add your handling code here:
    }//GEN-LAST:event_getButtonColor7ActionPerformed

    private void setButtonColor7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setButtonColor7ActionPerformed
        Color color7 = picker.getColor();
        panelColor7.setBackground(color7);
        com.alphamods.controlcenter.utils.config.setValue("color7R", Integer.toString(color7.getRed()));
        com.alphamods.controlcenter.utils.config.setValue("color7G", Integer.toString(color7.getGreen()));
        com.alphamods.controlcenter.utils.config.setValue("color7B", Integer.toString(color7.getBlue()));

        // TODO add your handling code here:
    }//GEN-LAST:event_setButtonColor7ActionPerformed

    private void PickerColorChanged(java.beans.PropertyChangeEvent evt){

        if (methods.isConnected()) {
            write();
        }else if (testmode){
            write();
        }
    }
    
    
    private void setButtonColor8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setButtonColor8ActionPerformed
        Color color8 = picker.getColor();
        panelColor8.setBackground(color8);
        com.alphamods.controlcenter.utils.config.setValue("color8R", Integer.toString(color8.getRed()));
        com.alphamods.controlcenter.utils.config.setValue("color8G", Integer.toString(color8.getGreen()));
        com.alphamods.controlcenter.utils.config.setValue("color8B", Integer.toString(color8.getBlue()));

        // TODO add your handling code here:
    }//GEN-LAST:event_setButtonColor8ActionPerformed

    private void getButtonColor8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getButtonColor8ActionPerformed
        int color8R = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color8R"));
        int color8G = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color8G"));
        int color8B = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color8B"));
        Color color8 = new Color(color8R,color8G,color8B);
        picker.setColor(color8);

        // TODO add your handling code here:
    }//GEN-LAST:event_getButtonColor8ActionPerformed

    private void getButtonColor3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getButtonColor3ActionPerformed
        int color3R = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color3R"));
        int color3G = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color3G"));
        int color3B = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color3B"));
        Color color3 = new Color(color3R,color3G,color3B);
        picker.setColor(color3);

        // TODO add your handling code here:
    }//GEN-LAST:event_getButtonColor3ActionPerformed

    private void setButtonColor3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setButtonColor3ActionPerformed
        Color color3 = picker.getColor();
        panelColor3.setBackground(color3);
        com.alphamods.controlcenter.utils.config.setValue("color3R", Integer.toString(color3.getRed()));
        com.alphamods.controlcenter.utils.config.setValue("color3G", Integer.toString(color3.getGreen()));
        com.alphamods.controlcenter.utils.config.setValue("color3B", Integer.toString(color3.getBlue()));

        // TODO add your handling code here:
    }//GEN-LAST:event_setButtonColor3ActionPerformed

    private void getButtonColor9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getButtonColor9ActionPerformed
        int color9R = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color9R"));
        int color9G = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color9G"));
        int color9B = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color9B"));
        Color color9 = new Color(color9R,color9G,color9B);
        picker.setColor(color9);

        // TODO add your handling code here:
    }//GEN-LAST:event_getButtonColor9ActionPerformed

    private void setButtonColor9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setButtonColor9ActionPerformed
        Color color9 = picker.getColor();
        panelColor9.setBackground(color9);
        com.alphamods.controlcenter.utils.config.setValue("color9R", Integer.toString(color9.getRed()));
        com.alphamods.controlcenter.utils.config.setValue("color9G", Integer.toString(color9.getGreen()));
        com.alphamods.controlcenter.utils.config.setValue("color9B", Integer.toString(color9.getBlue()));

        // TODO add your handling code here:
    }//GEN-LAST:event_setButtonColor9ActionPerformed

    private void getButtonColor4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getButtonColor4ActionPerformed
        int color4R = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color4R"));
        int color4G = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color4G"));
        int color4B = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color4B"));
        Color color4 = new Color(color4R,color4G,color4B);
        picker.setColor(color4);

        // TODO add your handling code here:
    }//GEN-LAST:event_getButtonColor4ActionPerformed

    private void setButtonColor4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setButtonColor4ActionPerformed
        Color color4 = picker.getColor();
        panelColor4.setBackground(color4);
        com.alphamods.controlcenter.utils.config.setValue("color4R", Integer.toString(color4.getRed()));
        com.alphamods.controlcenter.utils.config.setValue("color4G", Integer.toString(color4.getGreen()));
        com.alphamods.controlcenter.utils.config.setValue("color4B", Integer.toString(color4.getBlue()));

        // TODO add your handling code here:
    }//GEN-LAST:event_setButtonColor4ActionPerformed

    private void getButtonColor5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getButtonColor5ActionPerformed
        int color5R = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color5R"));
        int color5G = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color5G"));
        int color5B = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color5B"));
        Color color5 = new Color(color5R,color5G,color5B);
        picker.setColor(color5);

        // TODO add your handling code here:
    }//GEN-LAST:event_getButtonColor5ActionPerformed

    private void setButtonColor5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setButtonColor5ActionPerformed
        Color color5 = picker.getColor();
        panelColor5.setBackground(color5);
        com.alphamods.controlcenter.utils.config.setValue("color5R", Integer.toString(color5.getRed()));
        com.alphamods.controlcenter.utils.config.setValue("color5G", Integer.toString(color5.getGreen()));
        com.alphamods.controlcenter.utils.config.setValue("color5B", Integer.toString(color5.getBlue()));

        // TODO add your handling code here:
    }//GEN-LAST:event_setButtonColor5ActionPerformed

    private void setButtonColor10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setButtonColor10ActionPerformed
        Color color10 = picker.getColor();
        panelColor10.setBackground(color10);
        com.alphamods.controlcenter.utils.config.setValue("color10R", Integer.toString(color10.getRed()));
        com.alphamods.controlcenter.utils.config.setValue("color10G", Integer.toString(color10.getGreen()));
        com.alphamods.controlcenter.utils.config.setValue("color10B", Integer.toString(color10.getBlue()));

        // TODO add your handling code here:
    }//GEN-LAST:event_setButtonColor10ActionPerformed

    private void getButtonColor10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getButtonColor10ActionPerformed
        int color10R = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color10R"));
        int color10G = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color10G"));
        int color10B = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color10B"));
        Color color10 = new Color(color10R,color10G,color10B);
        picker.setColor(color10);

        // TODO add your handling code here:
    }//GEN-LAST:event_getButtonColor10ActionPerformed

    private void cleanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cleanButtonActionPerformed
methods.cleanFavourites();

loadpreviews();// TODO add your handling code here:
    }//GEN-LAST:event_cleanButtonActionPerformed

    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
if (methods.isConnected()) {
    
                methods.disconnect();
                notConnectedLabel.setVisible(true);
                connectButton.setText("Connect");
                picker.setEnabled(false);
                fadeRadioButton.setEnabled(false);
                normalRadioButton.setEnabled(false);
                musicRadioButton.setEnabled(false);
                recordButton1.setEnabled(false);
                playButton1.setEnabled(false);
                Refresh2.setEnabled(false);
                clearButton1.setEnabled(false);
            

        } else {

            try {
                methods.connect(evento, PortsBox);
                connectButton.setText("Disconnect");
                fadeRadioButton.setEnabled(true);
                normalRadioButton.setEnabled(true);
                musicRadioButton.setEnabled(true);
                recordButton1.setEnabled(true);
                playButton1.setEnabled(true);
                picker.setEnabled(true);
                notConnectedLabel.setVisible(false);
                Refresh2.setEnabled(true);
                clearButton1.setEnabled(true);
                loadsecpreviews();

            } catch (Exception ex) {
                Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        // TODO add your handling code here:
    }//GEN-LAST:event_connectButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        
        if (methods.isConnected()){
        }
        com.alphamods.controlcenter.utils.config.setValue("Fan1", Integer.toString(fan1slider.getValue()));
        com.alphamods.controlcenter.utils.config.setValue("Fan2", Integer.toString(fan2slider.getValue()));
        com.alphamods.controlcenter.utils.config.setValue("Pump1", Integer.toString(pump1slider.getValue()));
        com.alphamods.controlcenter.utils.config.setValue("Fan1max", fan1max.getText());
        com.alphamods.controlcenter.utils.config.setValue("Fan2max", fan2max.getText());
        com.alphamods.controlcenter.utils.config.setValue("Pump1max", pump1max.getText());
        
        if (methods.isConnected()){
            com.alphamods.controlcenter.utils.config.setValue("Port", PortsBox.getSelectedItem().toString());
        }

        com.alphamods.controlcenter.utils.config.setValue("refreshTime", refreshSecondsSpinner.getValue().toString());
        if (RefreshCheckBox.isSelected()){
            com.alphamods.controlcenter.utils.config.setValue("refreshMode", "1");
        }
        else if (!RefreshCheckBox.isSelected()){
            com.alphamods.controlcenter.utils.config.setValue("refreshMode", "0");
        }
        
        com.alphamods.controlcenter.utils.config.setValue("channel1R", methods.getChannel(1)[0]);
        com.alphamods.controlcenter.utils.config.setValue("channel1G", methods.getChannel(1)[1]);
        com.alphamods.controlcenter.utils.config.setValue("channel1B", methods.getChannel(1)[2]);
        
        com.alphamods.controlcenter.utils.config.setValue("channel2R", methods.getChannel(2)[0]);
        com.alphamods.controlcenter.utils.config.setValue("channel2G", methods.getChannel(2)[1]);
        com.alphamods.controlcenter.utils.config.setValue("channel2B", methods.getChannel(2)[2]);
        
        com.alphamods.controlcenter.utils.config.setValue("channel3R", methods.getChannel(3)[0]);
        com.alphamods.controlcenter.utils.config.setValue("channel3G", methods.getChannel(3)[1]);
        com.alphamods.controlcenter.utils.config.setValue("channel3B", methods.getChannel(3)[2]);
        
        com.alphamods.controlcenter.utils.config.setValue("channel4R", methods.getChannel(4)[0]);
        com.alphamods.controlcenter.utils.config.setValue("channel4G", methods.getChannel(4)[1]);
        com.alphamods.controlcenter.utils.config.setValue("channel4B", methods.getChannel(4)[2]);
        
        
// TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosing

    private void refreshPortsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshPortsButtonActionPerformed
   PortsBox.removeAllItems();
        methods.getArduino().getSerialPorts().forEach(i -> PortsBox.addItem(i));     // TODO add your handling code here:
    }//GEN-LAST:event_refreshPortsButtonActionPerformed

    private void sendbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendbuttonActionPerformed
    write();        // TODO add your handling code here:
    }//GEN-LAST:event_sendbuttonActionPerformed

    private void musicRadioButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_musicRadioButtonStateChanged
    }//GEN-LAST:event_musicRadioButtonStateChanged

    private void fadeRadioButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_fadeRadioButtonStateChanged
    }//GEN-LAST:event_fadeRadioButtonStateChanged

    private void normalRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_normalRadioButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_normalRadioButtonActionPerformed

    private void normalRadioButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_normalRadioButtonStateChanged
                // TODO add your handling code here:
    }//GEN-LAST:event_normalRadioButtonStateChanged

    private void pickerPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_pickerPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_pickerPropertyChange

    private void normalRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_normalRadioButtonItemStateChanged
write();        // TODO add your handling code here:
    }//GEN-LAST:event_normalRadioButtonItemStateChanged

    private void fadeRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_fadeRadioButtonItemStateChanged
write();        // TODO add your handling code here:
    }//GEN-LAST:event_fadeRadioButtonItemStateChanged

    private void musicRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_musicRadioButtonItemStateChanged
write();        // TODO add your handling code here:
    }//GEN-LAST:event_musicRadioButtonItemStateChanged

    private void fan1sliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_fan1sliderStateChanged
write();
rpmData();
        // TODO add your handling code here:
    }//GEN-LAST:event_fan1sliderStateChanged

    private void fan2sliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_fan2sliderStateChanged
write();
rpmData();
        // TODO add your handling code here:
    }//GEN-LAST:event_fan2sliderStateChanged

    private void pump1sliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_pump1sliderStateChanged
write();
rpmData();
        // TODO add your handling code here:
    }//GEN-LAST:event_pump1sliderStateChanged

    private void Refresh2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Refresh2ActionPerformed
write();
rpmData();
        // TODO add your handling code here:
    }//GEN-LAST:event_Refresh2ActionPerformed

    private void LedC3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LedC3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LedC3ActionPerformed

    private void getButtonColor11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getButtonColor11ActionPerformed
        int color11R = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color11R"));
        int color11G = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color11G"));
        int color11B = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color11B"));
        Color color11 = new Color(color11R,color11G,color11B);
        picker.setColor(color11);
        
        // TODO add your handling code here:
    }//GEN-LAST:event_getButtonColor11ActionPerformed

    private void setButtonColor11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setButtonColor11ActionPerformed
        Color color11 = picker.getColor();
        panelColor11.setBackground(color11);
        com.alphamods.controlcenter.utils.config.setValue("color11R", Integer.toString(color11.getRed()));
        com.alphamods.controlcenter.utils.config.setValue("color11G", Integer.toString(color11.getGreen()));
        com.alphamods.controlcenter.utils.config.setValue("color11B", Integer.toString(color11.getBlue()));

        // TODO add your handling code here:
    }//GEN-LAST:event_setButtonColor11ActionPerformed

    private void getButtonColor12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getButtonColor12ActionPerformed
        int color12R = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color12R"));
        int color12G = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color12G"));
        int color12B = Integer.parseInt(com.alphamods.controlcenter.utils.config.getValue("color12B"));
        Color color12 = new Color(color12R,color12G,color12B);
        picker.setColor(color12);
        
        // TODO add your handling code here:
    }//GEN-LAST:event_getButtonColor12ActionPerformed

    private void setButtonColor12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setButtonColor12ActionPerformed
        Color color12 = picker.getColor();
        panelColor12.setBackground(color12);
        com.alphamods.controlcenter.utils.config.setValue("color12R", Integer.toString(color12.getRed()));
        com.alphamods.controlcenter.utils.config.setValue("color12G", Integer.toString(color12.getGreen()));
        com.alphamods.controlcenter.utils.config.setValue("color12B", Integer.toString(color12.getBlue()));

        // TODO add your handling code here:
    }//GEN-LAST:event_setButtonColor12ActionPerformed

    private void RefreshCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshCheckBoxActionPerformed
        if (RefreshCheckBox.isSelected()){
            refreshSecondsSpinner.setEnabled(true);
            jLabel19.setEnabled(true);
            jLabel18.setEnabled(true);
            executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(refreshTemp, 0, Long.parseLong(refreshSecondsSpinner.getValue().toString()), TimeUnit.SECONDS);
        }
        else{
            refreshSecondsSpinner.setEnabled(false);
            jLabel19.setEnabled(false);
            jLabel18.setEnabled(false);
            executor.shutdown();
        }

// TODO add your handling code here:
    }//GEN-LAST:event_RefreshCheckBoxActionPerformed

    
    
    private void refreshSecondsSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_refreshSecondsSpinnerStateChanged
executor.shutdown();  
executor = Executors.newScheduledThreadPool(1);

            executor.scheduleAtFixedRate(refreshTemp, 0, Long.parseLong(refreshSecondsSpinner.getValue().toString()), TimeUnit.SECONDS);
// TODO add your handling code here:
    }//GEN-LAST:event_refreshSecondsSpinnerStateChanged

    private void testModeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_testModeItemStateChanged

        setIcons();
        testmode = testMode.isSelected();
if (testMode.isSelected()){
    
                connectButton.setText("testing");
                fadeRadioButton.setEnabled(true);
                normalRadioButton.setEnabled(true);
                musicRadioButton.setEnabled(true);
                picker.setEnabled(true);
                notConnectedLabel.setVisible(false);
                Refresh2.setEnabled(true);
                clearButton1.setEnabled(true);
                recordButton1.setEnabled(true);
                playButton1.setEnabled(true);
}else {
                notConnectedLabel.setVisible(true);
                connectButton.setText("Connect");
                picker.setEnabled(false);
                fadeRadioButton.setEnabled(false);
                normalRadioButton.setEnabled(false);
                musicRadioButton.setEnabled(false);
                recordButton1.setEnabled(false);
                playButton1.setEnabled(false);
                Refresh2.setEnabled(false); 
                
}
    }//GEN-LAST:event_testModeItemStateChanged

    private void recordButton1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_recordButton1ItemStateChanged
    if (recordButton1.isSelected()){
        s1record = new com.alphamods.controlcenter.utils.secuences();
        s1record.record(picker, 1);
        recordButton1.setText("Stop");  
    }
    else{
        s1record.recorderStop(bigpanel1); 
        recordButton1.setText("Record");
    }
    // TODO add your handling code here:
    }//GEN-LAST:event_recordButton1ItemStateChanged

    private void clearButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButton1ActionPerformed
        secuences.clean(1);
    }//GEN-LAST:event_clearButton1ActionPerformed

    private void recordButton2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_recordButton2ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_recordButton2ItemStateChanged

    private void playButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_playButton2ActionPerformed

    private void clearButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clearButton2ActionPerformed

    private void recordButton3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_recordButton3ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_recordButton3ItemStateChanged

    private void playButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_playButton3ActionPerformed

    private void clearButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clearButton3ActionPerformed

    private void recordButton4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_recordButton4ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_recordButton4ItemStateChanged

    private void playButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_playButton4ActionPerformed

    private void clearButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clearButton4ActionPerformed

    private void recordButton5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_recordButton5ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_recordButton5ItemStateChanged

    private void playButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_playButton5ActionPerformed

    private void clearButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clearButton5ActionPerformed

    private void loopCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_loopCheckBoxItemStateChanged
if (loopCheckBox.isSelected()){

    s1play.setLoop(true);
    

}else{
    
    s1play.setLoop(false);
    
}


// TODO add your handling code here:
    }//GEN-LAST:event_loopCheckBoxItemStateChanged

    private void playButton1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_playButton1ItemStateChanged
if (playButton1.isSelected()){
    s1play = new com.alphamods.controlcenter.utils.secuences();
    s1play.play(picker, 1, playButton1);
    if (loopCheckBox.isSelected()){
        s1play.setLoop(loopCheckBox.isSelected());
    }
}else {
    s1play.playerStop();
    
}// TODO add your handling code here:
    }//GEN-LAST:event_playButton1ItemStateChanged

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
Adalight light = new Adalight();

light.startUP();// TODO add your handling code here:
    }//GEN-LAST:event_jButton1MouseClicked

    private void settingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsActionPerformed
settings settings = new settings();
settings.setVisible(true);
settings.requestFocusInWindow();
// TODO add your handling code here:
    }//GEN-LAST:event_settingsActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
try {
                String fpathunlockb = path +"alphalight.exe";
                String[] args = {"cmd","/c","start", "tt", fpathunlockb};
                Runtime rt = Runtime.getRuntime();
                ProcessBuilder pb = new ProcessBuilder(args);
                Process pr = pb.start();
                
                
                // TODO add your handling code here:
            } catch (IOException ex) {
                Logger.getLogger(UpdaterGUI.class.getName()).log(Level.SEVERE, null, ex);
            }         // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    
    Runnable refreshTemp = new Runnable() {

    public void run() {
    write();
    rpmData();
        
    }
};
    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel FanPumpPanel;
    private javax.swing.JCheckBox LedC1;
    private javax.swing.JCheckBox LedC2;
    private javax.swing.JCheckBox LedC3;
    private javax.swing.JCheckBox LedC4;
    private javax.swing.JComboBox PortsBox;
    private javax.swing.JButton Refresh2;
    private javax.swing.JCheckBox RefreshCheckBox;
    private javax.swing.JPanel SecuencesPanel;
    private javax.swing.JLabel SecuencesTitle;
    private javax.swing.JTextField Temp1;
    private javax.swing.JTextField Temp2;
    private javax.swing.JTextField Temp3;
    private javax.swing.JTextField Temp4;
    private javax.swing.JTextField Temp5;
    private javax.swing.JTextField Temp6;
    private javax.swing.JPanel bigpanel1;
    private javax.swing.JPanel bigpanel2;
    private javax.swing.JPanel bigpanel3;
    private javax.swing.JPanel bigpanel4;
    private javax.swing.JPanel bigpanel5;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cleanButton;
    private javax.swing.JButton clearButton1;
    private javax.swing.JButton clearButton2;
    private javax.swing.JButton clearButton3;
    private javax.swing.JButton clearButton4;
    private javax.swing.JButton clearButton5;
    private javax.swing.JPanel colorTab;
    private javax.swing.JTabbedPane colors_secuencesPanel;
    private javax.swing.JButton connectButton;
    private javax.swing.JRadioButton fadeRadioButton;
    private javax.swing.JLabel fan1label;
    private javax.swing.JTextField fan1max;
    private javax.swing.JSlider fan1slider;
    private javax.swing.JTextField fan2max;
    private javax.swing.JSlider fan2slider;
    private javax.swing.JLabel fansTitleLabel;
    private javax.swing.JPanel favColorsPanel;
    private javax.swing.JButton getButtonColor1;
    private javax.swing.JButton getButtonColor10;
    private javax.swing.JButton getButtonColor11;
    private javax.swing.JButton getButtonColor12;
    private javax.swing.JButton getButtonColor2;
    private javax.swing.JButton getButtonColor3;
    private javax.swing.JButton getButtonColor4;
    private javax.swing.JButton getButtonColor5;
    private javax.swing.JButton getButtonColor6;
    private javax.swing.JButton getButtonColor7;
    private javax.swing.JButton getButtonColor8;
    private javax.swing.JButton getButtonColor9;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel labelColor1;
    private javax.swing.JLabel labelColor10;
    private javax.swing.JLabel labelColor11;
    private javax.swing.JLabel labelColor12;
    private javax.swing.JLabel labelColor2;
    private javax.swing.JLabel labelColor3;
    private javax.swing.JLabel labelColor4;
    private javax.swing.JLabel labelColor5;
    private javax.swing.JLabel labelColor6;
    private javax.swing.JLabel labelColor7;
    private javax.swing.JLabel labelColor8;
    private javax.swing.JLabel labelColor9;
    private javax.swing.JLabel ledModeLabel;
    private javax.swing.JCheckBox loopCheckBox;
    private javax.swing.JMenuBar menubar;
    private javax.swing.JRadioButton musicRadioButton;
    private javax.swing.JRadioButton normalRadioButton;
    private javax.swing.JLabel notConnectedLabel;
    private javax.swing.JProgressBar notificationsBar;
    private javax.swing.JLabel notificationsLabel;
    private javax.swing.JPanel panelColor1;
    private javax.swing.JPanel panelColor10;
    private javax.swing.JPanel panelColor11;
    private javax.swing.JPanel panelColor12;
    private javax.swing.JPanel panelColor2;
    private javax.swing.JPanel panelColor3;
    private javax.swing.JPanel panelColor4;
    private javax.swing.JPanel panelColor5;
    private javax.swing.JPanel panelColor6;
    private javax.swing.JPanel panelColor7;
    private javax.swing.JPanel panelColor8;
    private javax.swing.JPanel panelColor9;
    private com.bric.swing.ColorPicker picker;
    private javax.swing.JToggleButton playButton1;
    private javax.swing.JButton playButton2;
    private javax.swing.JButton playButton3;
    private javax.swing.JButton playButton4;
    private javax.swing.JButton playButton5;
    private javax.swing.JLabel pump1label;
    private javax.swing.JTextField pump1max;
    private javax.swing.JSlider pump1slider;
    private javax.swing.JLabel pump2label;
    private javax.swing.JLabel pumpsTitleLabel;
    private javax.swing.JToggleButton recordButton1;
    private javax.swing.JToggleButton recordButton2;
    private javax.swing.JToggleButton recordButton3;
    private javax.swing.JToggleButton recordButton4;
    private javax.swing.JToggleButton recordButton5;
    private javax.swing.JButton refreshPortsButton;
    private javax.swing.JSpinner refreshSecondsSpinner;
    private javax.swing.JTextField rmpLabelFan1;
    private javax.swing.JTextField rmpLabelFan2;
    private javax.swing.JTextField rmpLabelPump1;
    private javax.swing.JLabel rpmlabel1;
    private javax.swing.JLabel rpmlabel3;
    private javax.swing.JLabel rpmlabel4;
    private javax.swing.JLabel sec1label;
    private javax.swing.JLabel sec2label;
    private javax.swing.JLabel sec3label;
    private javax.swing.JLabel sec4label;
    private javax.swing.JLabel sec5label;
    private javax.swing.JButton sendbutton;
    private javax.swing.JButton setButtonColor1;
    private javax.swing.JButton setButtonColor10;
    private javax.swing.JButton setButtonColor11;
    private javax.swing.JButton setButtonColor12;
    private javax.swing.JButton setButtonColor2;
    private javax.swing.JButton setButtonColor3;
    private javax.swing.JButton setButtonColor4;
    private javax.swing.JButton setButtonColor5;
    private javax.swing.JButton setButtonColor6;
    private javax.swing.JButton setButtonColor7;
    private javax.swing.JButton setButtonColor8;
    private javax.swing.JButton setButtonColor9;
    private javax.swing.JMenuItem settings;
    private javax.swing.JTabbedPane tabPanel;
    private javax.swing.JToggleButton testMode;
    // End of variables declaration//GEN-END:variables
}
