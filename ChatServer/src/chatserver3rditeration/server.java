/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver3rditeration;

import java.awt.event.KeyEvent;
import java.awt.EventQueue;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import java.io.IOException;
import java.awt.Toolkit;
import javax.swing.LayoutStyle;
import java.awt.LayoutManager;
import javax.swing.GroupLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;
import java.awt.Component;
import java.net.InetAddress;
import javax.swing.JOptionPane;
import java.text.SimpleDateFormat;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.io.DataOutputStream;
import java.util.Date;
import java.nio.file.Path;
import java.text.DateFormat;
import java.io.DataInputStream;
import java.io.File;
import java.net.Socket;
import java.net.ServerSocket;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

public class server extends JFrame implements KeyListener, Runnable
{
    static ServerSocket ssckt;
    static Socket sckt;
    String username;
    File file;
    static DataInputStream dtinpt;
    DateFormat dateFormat;
    Path p;
    Date date;
    static DataOutputStream dtotpt;
    private static JTextArea chat;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JScrollPane jScrollPane1;
    private JTextField message;
    private JButton save;
    private JButton send;
    private JLabel time;
    
    @Override
    public void run() {
        while (true) {
            final Date date = new Date();
            this.time.setText(new SimpleDateFormat("HH:mm:ss").format(date));
            server.chat.getText();
        }
    }
    
    public server() throws UnknownHostException {
        this.dateFormat = new SimpleDateFormat("HH:mm:ss");
        this.date = new Date();
        this.initComponents();
        final Thread t = new Thread(this);
        t.start();
        this.username = JOptionPane.showInputDialog("Enter a Username");
        this.message.addKeyListener(this);
        final InetAddress inetAddress = InetAddress.getLocalHost();
        server.chat.setText("***Chat Host V3***\nThe Chat is opened on " + inetAddress.getHostAddress() + "\nUsing Socket 1201 by default");
        this.jLabel1.setText(this.username + " Chat Session");
        this.setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        this.send = new JButton();
        this.jLabel1 = new JLabel();
        this.message = new JTextField();
        this.jScrollPane1 = new JScrollPane();
        server.chat = new JTextArea();
        this.jLabel2 = new JLabel();
        this.time = new JLabel();
        this.save = new JButton();
        this.setDefaultCloseOperation(3);
        this.send.setText("\u263a");
        this.send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                server.this.sendActionPerformed(evt);
            }
        });
        this.jLabel1.setFont(new Font("Dialog", 1, 18));
        this.jLabel1.setText("Server Side Chat");
        server.chat.setEditable(false);
        server.chat.setColumns(20);
        server.chat.setFont(new Font("Dialog", 0, 18));
        server.chat.setRows(5);
        this.jScrollPane1.setViewportView(server.chat);
        this.jLabel2.setText("Message");
        this.time.setFont(new Font("Dialog", 1, 18));
        this.time.setText("time");
        this.save.setText("Save ");
        this.save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                server.this.saveActionPerformed(evt);
            }
        });
        final GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.jLabel2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.message).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.send)).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(this.time, -1, -1, 32767).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jLabel1).addGap(49, 49, 49).addComponent(this.save).addGap(25, 25, 25)).addComponent(this.jScrollPane1, GroupLayout.Alignment.TRAILING, -1, 399, 32767)).addContainerGap()))));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel1).addComponent(this.time, -2, 37, -2).addComponent(this.save, -1, 36, 32767)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jScrollPane1, -2, 252, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.send).addComponent(this.message, -2, -1, -2).addComponent(this.jLabel2)).addContainerGap()));
        this.pack();
    }
    
    private void sendActionPerformed(final ActionEvent evt) {
        this.message.setText(this.message.getText().replaceAll("<happy>", "\u263a"));
        this.message.setText(this.message.getText().replaceAll("<heart>", "\u2764"));
        this.message.setText(this.message.getText().replaceAll("<wrong>", "\u2715"));
        this.message.setText(this.message.getText().replaceAll("<correct>", "\u2713"));
        Toolkit.getDefaultToolkit().beep();
        if (this.message.getText().equals("")) {
            server.chat.setText(server.chat.getText() + "\n***You May Not Send a Blank Message***");
        }
        else {
            try {
                String msgout = "";
                msgout = this.message.getText().trim();
                server.chat.setText(server.chat.getText() + "\n " + this.username + ": " + msgout);
                server.dtotpt.writeUTF(this.username + ": " + msgout);
                this.message.setText("");
                appendStrToFile("C:\\Users\\donal\\OneDrive\\Desktop\\Oxbridge\\ServerLogs.txt", server.chat.getText() + "\n" + this.username + ": " + msgout);
            }
            catch (IOException ex) {
                server.chat.setText(server.chat.getText() + "\n***Connection Lost/Timed Out***");
            }
        }
    }
    
    public void saveFile() throws IOException {
        final JFileChooser fc = new JFileChooser();
        final int returnVal = fc.showOpenDialog(this);
        if (returnVal == 0) {
            this.file = fc.getSelectedFile();
            final String path = this.file.getAbsolutePath();
            appendStrToFile(path, server.chat.getText());
        }
    }
    
    private void saveActionPerformed(final ActionEvent evt) {
        try {
            this.saveFile();
            JOptionPane.showMessageDialog(this, "The conversation has been saved");
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "An Error has occured the conversation did not save");
            Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void appendStrToFile(final String fileName, final String str) throws IOException {
        final BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
        writer.newLine();
        writer.write(str);
        writer.close();
    }
    
    public static void main(final String[] args) throws InterruptedException {
        try {
            for (final UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex2) {
            Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex2);
        }
        catch (IllegalAccessException ex3) {
            Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex3);
        }
        catch (UnsupportedLookAndFeelException ex4) {
            Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex4);
        }
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new server().setVisible(true);
                }
                catch (UnknownHostException ex) {
                    Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        String msgin = "";
        try {
            server.ssckt = new ServerSocket(1201);
            server.sckt = server.ssckt.accept();
            server.dtinpt = new DataInputStream(server.sckt.getInputStream());
            server.dtotpt = new DataOutputStream(server.sckt.getOutputStream());
            while (!msgin.equals("exit")) {
                msgin = server.dtinpt.readUTF();
                server.chat.setText(server.chat.getText() + "\n" + msgin);
                server.chat.setCaretPosition(server.chat.getDocument().getLength());
            }
        }
        catch (Exception e) {
            server.chat.setText(server.chat.getText() + "\n***Connection Lost/Timed Out***\nProgram ENDING in 5 Seconds");
            Thread.sleep(5000L);
            System.exit(0);
        }
    }
    
    @Override
    public void keyTyped(final KeyEvent e) {
    }
    
    @Override
    public void keyReleased(final KeyEvent e) {
    }
    
    @Override
    public void keyPressed(final KeyEvent e) {
        final int key = e.getKeyCode();
        if (key == 10) {
            this.message.setText(this.message.getText().replaceAll("<happy>", "\u263a"));
            this.message.setText(this.message.getText().replaceAll("<sad>", "\u2639"));
            this.message.setText(this.message.getText().replaceAll("<heart>", "\u2764"));
            this.message.setText(this.message.getText().replaceAll("<wrong>", "\u2715"));
            this.message.setText(this.message.getText().replaceAll("<correct>", "\u2713"));
            Toolkit.getDefaultToolkit().beep();
            if (this.message.getText().equals("")) {
                server.chat.setText(server.chat.getText() + "\n***You May Not Send a Blank Message***");
            }
            else {
                try {
                    String msgout = "";
                    msgout = this.message.getText().trim();
                    server.chat.setText(server.chat.getText() + "\n " + this.username + ": " + msgout);
                    server.dtotpt.writeUTF(this.username + ": " + msgout);
                    this.message.setText("");
                    appendStrToFile("C:\\Users\\donal\\OneDrive\\Desktop\\Oxbridge\\ServerLogs.txt", server.chat.getText() + "\n" + this.username + ": " + msgout);
                }
                catch (IOException ex) {
               
                }
            }
        }
    }
}