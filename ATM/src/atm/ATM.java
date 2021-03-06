package atm; //User Defined package named atm

import javax.swing.table.DefaultTableModel; //import DefaultTableModel class for creating a table
import atm.Txn.*;//import Txn class
import javax.swing.*;//import java swing library
import java.awt.*;//import Font and Color class
import java.awt.event.*;//import ActionListener class
import javax.swing.border.Border;//import Border class
import java.sql.*;//import Connection, Statement, ResultSet, DriverManager, SQLException class for connecting to the database

class ATMUI {
   //variables declaration
    private JFrame frm;
    private JPanel panel, panel1, panel2, panel3, panel4, panel5, panel6;
    private JTabbedPane tp;
    public int accountnumber;
    public String name;
    public int pin;
    double balance_global = 0;
    public JPanel loading;
    Timer timer;
    public int check;
    JPasswordField pf1, pf2, pf22;
    JPanel _panel1, _panel2, _panel3;

    //display atm menu
    void displayATMMenu() {
        createATMMenu();
        panel.setVisible(false);
        tp.setVisible(true);
    }
  
      //display login page
    void displayAuth() {
        panel.setVisible(true);
        tp.setVisible(false);
        tp = null;
        frm.setJMenuBar(null);
    }

    //create authentication screen
    void createAuth() {
        panel = new JPanel();
        panel.setBounds(0, 0, 800, 800);
        panel.setBackground(new Color(0, 102, 255));
        panel.setLayout(null);
        
        //login avatar
        ImageIcon img = new ImageIcon("E:\\MiniProject\\ATM\\src\\atm\\uscc1.png");
        JLabel imglbl = new JLabel(img); //login avatar image
        imglbl.setBounds(150, 215, 200, 200);
        //imglbl.setSize(200, 200);
        panel.add(imglbl);
        frm.add(panel);

        JLabel label = new JLabel();
        label.setText("WELCOME TO ATM");
        label.setBackground(Color.CYAN);
        label.setOpaque(true);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(175, 20, 450, 100);
        label.setFont(new Font("Osward", Font.BOLD, 28));
        Border blackline = BorderFactory.createLineBorder(Color.black);//set border for label 
        label.setBorder(blackline);
        panel.add(label);

        JLabel label2 = new JLabel("ACCOUNT NUMBER:");
        label2.setBounds(350, 226, 200, 30);
        label2.setFont(new Font("Osward", Font.BOLD, 18));
        label2.setForeground(Color.white);
        panel.add(label2);

        JTextField text = new JTextField();
        text.setBounds(350, 260, 200, 30);
        panel.add(text);

        JLabel label3 = new JLabel("PIN:");
        label3.setBounds(350, 305, 250, 30);
        label3.setFont(new Font("Osward", Font.BOLD, 18));
        label3.setForeground(Color.white);
        panel.add(label3);

        JPasswordField pf1 = new JPasswordField(15);
        pf1.setBounds(350, 345, 200, 30);
        panel.add(pf1);

        JButton b1 = new JButton("LOGIN");
        b1.setBounds(350, 450, 100, 30);
        frm.getRootPane().setDefaultButton(b1);
        
        //Mouse hover feature 
         b1.addMouseListener(new java.awt.event.MouseAdapter() {
             
            public void mouseEntered(java.awt.event.MouseEvent evt) { 
                b1.setBackground(Color.CYAN);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                b1.setBackground(UIManager.getColor("control"));
            }
        });
         //action listener for login button
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int pass, accno;

                try {
                    pass = Integer.parseInt(new String(pf1.getPassword()));
                    accno = Integer.parseInt(text.getText());
                    
                    //sql query for authenticating user
                    String sql = "SELECT * FROM ATMLOGIN.ACCHOLDER WHERE accountnumber = " + accno + " AND pin = " + pass;
                    System.out.println(sql);
                    //try with resources block for connecting to the db and returning the ResultSet
                    try (Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/ATMDB", "atmlogin", "loginatm"); Statement statement = connection.createStatement(); ResultSet result = statement.executeQuery(sql);) {
                        if (result.next()) {
                            //Read accholder info
                            accountnumber = result.getInt("ACCOUNTNUMBER");
                            pin = result.getInt("PIN");
                            name = result.getString("NAME");
                            double balance = result.getInt("BALANCE");
                            balance_global = balance;
                            System.out.println("ACCOUNTNUMBER: " + accountnumber + " NAME: " + name + " PIN: " + pin + " BALANCE: " + balance);
                            displayATMMenu(); //display ATM Menu

                        } else {
                            //User not authenticated 
                            System.out.println("Data is not validated");
                            JOptionPane.showMessageDialog(null, "Invalid Credentials", "Error", JOptionPane.WARNING_MESSAGE);
                        }

                    } catch (SQLException se) {
                        //Handle could not connect to db
                        System.out.println(se);
                        JOptionPane.showMessageDialog(null, "Please connect the database and login again!!", "Connection Error", JOptionPane.WARNING_MESSAGE);
                        System.exit(0);
                        
                    }
                    text.setText("");
                    pf1.setText("");
                    //displayATMMenu();
                } catch (Exception e) {
                     //Handle non integer input exception
                    JOptionPane.showMessageDialog(null, "Please enter valid Account number and pin!!", "Warning", JOptionPane.WARNING_MESSAGE);
                    text.setText("");
                    pf1.setText("");
                }

            }
        });

        panel.add(b1);
    }
//create Atm menu
    void createATMMenu() {
        panel1 = createHome();//create Home panel
        panel2 = createBalance();//create Balance panel
        panel3 = createWithdraw();//create Withdraw panel
        panel4 = createMiniStatement();//create MiniStatement panel
        panel5 = createPinChange();//create Pinchange panel
        panel6 = createExit();//create Exit panel

        //Tabbed Pane to include all Atm menu panels
        tp = new JTabbedPane();
        tp.setBounds(0, 0, 800, 800);
        tp.addTab("Home", panel1);
        frm.add(tp);
        
        //menu
        JMenu menu = new JMenu("Quick Navigate");
        JMenuBar m_bar = new JMenuBar();
        JMenuItem home_item = new JMenuItem(new AbstractAction("Home") {
            public void actionPerformed(ActionEvent e) {
                tp.addTab("Home", panel1);
                tp.setSelectedIndex(0);
                tp.remove(panel2);
                tp.remove(panel3);
                tp.remove(panel4);
                tp.remove(panel5);
                tp.remove(panel6);
            }
        });
        JMenuItem balance_item = new JMenuItem(new AbstractAction("Balance") {
            public void actionPerformed(ActionEvent e) {
                tp.addTab("Balance", panel2);
                tp.setSelectedIndex(0);
                tp.remove(panel1);
                tp.remove(panel3);
                tp.remove(panel4);
                tp.remove(panel5);
                tp.remove(panel6);
            }
        });
        JMenuItem pin_item = new JMenuItem(new AbstractAction("Pin Change") {
            public void actionPerformed(ActionEvent e) {
                tp.addTab("Pin change", panel5);
                tp.setSelectedIndex(0);
                tp.remove(panel1);
                tp.remove(panel2);
                tp.remove(panel3);
                tp.remove(panel4);
                tp.remove(panel6);
            }
        });
        JMenuItem withdraw_item = new JMenuItem(new AbstractAction("Withdraw") {
            public void actionPerformed(ActionEvent e) {
                tp.addTab("Withdraw", panel3);
                tp.setSelectedIndex(0);
                tp.remove(panel1);
                tp.remove(panel2);
                tp.remove(panel4);
                tp.remove(panel5);
                tp.remove(panel6);
            }
        });
        JMenuItem ministatement_item = new JMenuItem(new AbstractAction("MiniStatement") {
            public void actionPerformed(ActionEvent e) {
                tp.addTab("Ministament", panel4);
                tp.setSelectedIndex(0);
                tp.remove(panel1);
                tp.remove(panel2);
                tp.remove(panel3);
                tp.remove(panel5);
                tp.remove(panel6);
            }
        });
        JMenuItem exit_item = new JMenuItem(new AbstractAction("Exit") {
            public void actionPerformed(ActionEvent e) {
                tp.addTab("Exit", panel6);
                tp.setSelectedIndex(0);
                tp.remove(panel1);
                tp.remove(panel2);
                tp.remove(panel3);
                tp.remove(panel4);
                tp.remove(panel5);
            }
        });
        menu.add(home_item);
        menu.add(balance_item);
        menu.add(pin_item);
        menu.add(withdraw_item);
        menu.add(ministatement_item);
        menu.add(exit_item);

        m_bar.add(menu);
        frm.setJMenuBar(m_bar);
    }

    JPanel createHome() {
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 800, 800);
        panel.setLayout(null);
        panel.setBackground(new Color(0, 102, 255));
        JLabel lbl = new JLabel("Welcome " + name + "\n to PMOJ Bank", SwingConstants.CENTER);
        lbl.setFont(new Font("Calibri", Font.BOLD, 20));
        lbl.setBackground(Color.CYAN);
        lbl.setOpaque(true);
        lbl.setBounds(0, 50, 800, 70);

        panel.add(lbl);
        JButton b1 = new JButton("Balance");
        b1.setBounds(150, 250, 150, 50);
        b1.setBackground(Color.cyan);
        JButton b2 = new JButton("Withdraw");
        b2.setBounds(500, 250, 150, 50);
        b2.setBackground(Color.cyan);
        JButton b3 = new JButton("Ministatement");
        b3.setBounds(150, 400, 150, 50);
        b3.setBackground(Color.cyan);
        JButton b4 = new JButton("Pin change");
        b4.setBounds(500, 400, 150, 50);
        b4.setBackground(Color.cyan);
        JButton b5 = new JButton("Exit");
        b5.setBounds(320, 580, 150, 50);
        b5.setBackground(Color.cyan);
        panel.add(b1);
        panel.add(b2);
        panel.add(b3);
        panel.add(b4);
        panel.add(b5);

        //button listeners
        b1.addActionListener(new ActionListener() { //balance
            public void actionPerformed(ActionEvent ae) {
                //sets balance tab visible and jumps to balance tab 
                tp.addTab("Balance", panel2);
                tp.setSelectedIndex(0);
                //rest all tabs are removed
                tp.remove(panel1);
                tp.remove(panel3);
                tp.remove(panel4);
                tp.remove(panel5);
                tp.remove(panel6);
            }
        });

        b2.addActionListener(new ActionListener() { //withdraw
            public void actionPerformed(ActionEvent ae) {
                tp.addTab("Withdraw", panel3);
                tp.setSelectedIndex(0);
                tp.remove(panel1);
                tp.remove(panel2);
                tp.remove(panel4);
                tp.remove(panel5);
                tp.remove(panel6);
            }
        });

        b3.addActionListener(new ActionListener() { //ministatement
            public void actionPerformed(ActionEvent ae) {
                tp.addTab("Ministament", panel4);
                tp.setSelectedIndex(0);
                tp.remove(panel1);
                tp.remove(panel2);
                tp.remove(panel3);
                tp.remove(panel5);
                tp.remove(panel6);
                if (check == 0) {
                    JOptionPane.showMessageDialog(null, "No transactions found...", "Message", JOptionPane.WARNING_MESSAGE);
                }

            }
        });

        b4.addActionListener(new ActionListener() { //pinchange
            public void actionPerformed(ActionEvent ae) {
                tp.addTab("Pin change", panel5);
                tp.setSelectedIndex(0);
                tp.remove(panel1);
                tp.remove(panel2);
                tp.remove(panel3);
                tp.remove(panel4);
                tp.remove(panel6);
            }
        });

        b5.addActionListener(new ActionListener() { //exit
            public void actionPerformed(ActionEvent ae) {
                  displayAuth();
            }
            });
        
        return panel;
        }

    JPanel createBalance() {
        JPanel panel = new JPanel();
        JButton exbal = new JButton("BACK ");
        exbal.setBounds(320, 600, 100, 30);
        panel.add(exbal);
        //exit button
         exbal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exbal.setBackground(Color.CYAN);
            }
            //hover effect
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exbal.setBackground(UIManager.getColor("control"));
            }
        });
         
        exbal.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent ae) {
                tp.addTab("Home", panel1);
                tp.setSelectedIndex(0);
                tp.remove(panel2);
                tp.remove(panel3);
                tp.remove(panel4);
                tp.remove(panel5);
                tp.remove(panel6);
            }
        });

        JLabel label3 = new JLabel("AVAILABLE BALANCE:");
        panel.setLayout(null);
        panel.setBackground(new Color(0, 102, 255));
        label3.setForeground(Color.white);
//        JLabel lb1 = new JLabel("Withdraw", SwingConstants.CENTER);
//        lb1.setBounds(0, 140, 800, 70);
//        lb1.setBackground(Color.cyan);
//        lb1.setOpaque(true);
        label3.setBounds(250, 200, 250, 40);
        label3.setFont(new Font("Osward", Font.BOLD, 18));
        panel.add(label3);

        JLabel label4 = new JLabel("label");
        label4.setText(Double.toString(balance_global));
        label4.setBounds(250, 250, 250, 40);
        label4.setBackground(Color.cyan);
        label4.setOpaque(true);
        label4.setFont(new Font("Osward", Font.BOLD, 18));
        Border blackline1 = BorderFactory.createLineBorder(Color.black);
        label4.setBorder(blackline1);
        panel.add(label4);
        return panel;
    }

    JPanel createWithdraw() {
        //loading panel
        loading = new JPanel();
        loading.setVisible(true);
        loading.setLayout(null);
        loading.setBackground(new Color(0, 102, 255));
        JLabel lla = new JLabel("Your transaction is being processed.", SwingConstants.CENTER);
        lla.setBounds(0, 140, 800, 70);
        lla.setBackground(Color.cyan);
        lla.setOpaque(true);
        lla.setFont(new Font("Calibri", Font.BOLD, 20));
        loading.add(lla);

        JLabel llb = new JLabel("Please Wait...", SwingConstants.CENTER);
        llb.setBounds(0, 195, 800, 50);
        llb.setBackground(Color.cyan);
        llb.setOpaque(true);
        llb.setFont(new Font("Calibri", Font.BOLD, 20));
        loading.add(llb);

        JPanel panel = new JPanel(); //main withdraw panel
        panel.setLayout(null);
        panel.setBackground(new Color(0, 102, 255));
        JLabel lb1 = new JLabel("Withdraw", SwingConstants.CENTER);
        lb1.setBounds(0, 180, 800, 70);
        lb1.setBackground(Color.cyan);
        lb1.setOpaque(true);
        lb1.setFont(new Font("Calibri", Font.BOLD, 30));
        panel.add(lb1);

        JLabel lb3 = new JLabel("Rs.");
        lb3.setBounds(280, 283, 20, 30);
        lb3.setFont(new Font("Calibri", Font.BOLD, 15));
        panel.add(lb3);

        JTextField text = new JTextField(10);
        text.setBounds(310, 280, 200, 30);
        text.setOpaque(true);

        panel.add(text);

        JButton btn = new JButton("Yes");
        btn.setBounds(360, 330, 100, 30);
        //Mouse Hover button Color Change
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(Color.CYAN);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(UIManager.getColor("control"));
            }
        });
        //Action listener for yes button to initiate withdraw
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                double wthdr=0;
                try {
                  wthdr = Integer.parseInt(text.getText());
                }
                catch (Exception e) {
                  //handle non integer input
                  wthdr=0; 
                }
                
                 if (wthdr <= 0) {
                 //handle withdraw amount invalid
                 JOptionPane.showMessageDialog(null, "Invalid amount", "Warning", JOptionPane.WARNING_MESSAGE);
                 text.setText("");
                } else
                if (wthdr > balance_global) {
                    //handle withdraw amount greater than balance
                    JOptionPane.showMessageDialog(null, "Insuffient Balance", "Warning", JOptionPane.WARNING_MESSAGE);
                    text.setText("");
                }else {
                    //code to withdraw amount
                    double nbalance = balance_global - wthdr;

                    //Create Transaction
                    Txn t = new Txn(wthdr, accountnumber);

                    //Update balance in database
                    String sql = "UPDATE ATMLOGIN.ACCHOLDER SET BALANCE=" + nbalance + "WHERE accountnumber = " + accountnumber;
                    System.out.println(sql);
                    try (Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/ATMDB", "atmlogin", "loginatm");
                            Statement statement = connection.createStatement();) {
                        statement.executeUpdate(sql);
                        System.out.println("ACCOUNTNUMBER: " + accountnumber + " BALANCE: " + nbalance);
                        tp.addTab("Loading", loading);
                        tp.setSelectedIndex(0);
                        tp.remove(panel3);
                        
                        ActionListener actionListener = new ActionListener() {
                            public void actionPerformed(ActionEvent actionEvent) {
                                timer.stop();
                                JOptionPane.showMessageDialog(null, "Transaction Successfull", "Message", JOptionPane.WARNING_MESSAGE);
                                displayAuth();                              
                            }
                        };
                        //adds 2 second delay between withdraw panel and loading panel
                        timer = new Timer(2000, actionListener);
                        timer.start();

                    } catch (SQLException se) {
                        System.out.println(se);
                    }
                }
            }

        });
        panel.add(btn);

        JButton btnNo = new JButton("No");
        btnNo.setBounds(360, 370, 100, 30);
        //No button hover effect
        btnNo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnNo.setBackground(Color.CYAN);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnNo.setBackground(UIManager.getColor("control"));
            }
        });
        //NO button action listener
        btnNo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                tp.addTab("Home", panel1);
                tp.setSelectedIndex(0);
                tp.remove(panel2);
                tp.remove(panel3);
                tp.remove(panel4);
                tp.remove(panel5);
                tp.remove(panel6);
            }
        });

        panel.add(btnNo);
        return panel;
    }
    //Ministatement
    JPanel createMiniStatement() {
        Date d;
        Time t;
        int accno, id;
        double amount;     
        String CRDR;

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(0, 102, 255));

        JLabel lb1 = new JLabel("Mini Statement", SwingConstants.CENTER);
        lb1.setBounds(0, 90, 800, 70);
        lb1.setBackground(Color.cyan);
        lb1.setOpaque(true);
        lb1.setFont(new Font("Calibri", Font.BOLD, 20));
        panel.add(lb1);
        
        
        //query string to get all transanctions done by the user
        String sql = "SELECT * FROM ATMLOGIN.ATM_Txn WHERE accountnumber = " + accountnumber;

        try (Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/ATMDB", "atmlogin", "loginatm"); Statement statement = connection.createStatement(); ResultSet rs = statement.executeQuery(sql);) {

            if (rs.next() == false) {
                //Handle no txns
                check = 0;
            } else {
                check = 1;

                //Radio Buttons for displaying recent and all transactions
                JPanel radioP = new JPanel();
                radioP.setLayout(null);
                radioP.setBounds(0, 205, 800, 50);
                radioP.setBackground(Color.cyan);

                JRadioButton r1 = new JRadioButton("Recent Transactions"); //display only recent transactions
                r1.setBounds(180, 20, 250, 15);
                r1.setBackground(Color.cyan);
                radioP.add(r1);

                JRadioButton r2 = new JRadioButton("All Transactions", true);  //display all transactions
                r2.setBounds(450, 20, 250, 15);
                r2.setBackground(Color.cyan);
                radioP.add(r2);
                
                //ButtonGroup for grouping the radio buttons
                ButtonGroup group = new ButtonGroup();
                group.add(r1);
                group.add(r2);

                panel.add(radioP);
                
                //TableModel for all transactions
                DefaultTableModel txnModel = new DefaultTableModel();
                txnModel.addColumn("TXN ID"); //add column to table model
                txnModel.addColumn("DATE");
                txnModel.addColumn("TIME");
                txnModel.addColumn("ACCOUNT NUMBER");
                txnModel.addColumn("CRDR");
                txnModel.addColumn("AMOUNT");

                //Getting row data from result set and Adding rows to all transactions table model
                do {
                    id = rs.getInt("Txn_ID"); //get transaction id
                    accno = rs.getInt("ACCOUNTNUMBER"); //get account number
                    amount = rs.getFloat("Amount"); //get withdraw amount
                    d = rs.getDate("Txn_Date"); //get date when withdraw was done
                    t = rs.getTime("Txn_Time"); //get time when withdraw was done
                    CRDR = "DR";

                    txnModel.addRow(new Object[]{id, d, t, accno, CRDR, amount}); //add all values to a new row
                } while (rs.next());

                //Creating all transactions table
                JTable txnTable = new JTable(txnModel);
                txnTable.setEnabled(false); //disable edit table by user
                txnTable.setFont(new Font("Calibri", Font.PLAIN, 14));
                txnTable.setRowHeight(20); //set table height
                panel.add(txnTable);

                //Adding all transactions table to a scrollpane 
                JScrollPane scroll_table = new JScrollPane(txnTable);
                //set table height according to number of transactions
                int height = 0;
                if (20 + (txnModel.getRowCount() * 15) <= 255)
                    height = 20 + (txnModel.getRowCount() * 15);
                else 
                    height = 255;
                scroll_table.setBounds(0, 300, 800, height); 
                scroll_table.setVisible(true);
                panel.add(scroll_table);

                //TableModel for recent transactions
                DefaultTableModel txnRModel = new DefaultTableModel();
                txnRModel.addColumn("TXN ID");
                txnRModel.addColumn("DATE");
                txnRModel.addColumn("TIME");
                txnRModel.addColumn("ACCOUNT NUMBER");
                txnRModel.addColumn("CRDR");
                txnRModel.addColumn("AMOUNT");
                
                //Get last 3 rows from all transactions table model and add to recent transactions table model
                int i=txnModel.getRowCount()-1;
                int count = 0;

                while(i>=0) {
                    if (count == 3) 
                        break;
                    else {
                        txnRModel.addRow(new Object[]{txnModel.getValueAt(i, 0), txnModel.getValueAt(i, 1), txnModel.getValueAt(i, 2), txnModel.getValueAt(i, 3), txnModel.getValueAt(i, 4), txnModel.getValueAt(i, 5)});
                        i--;
                        count++;
                    }
                }

                //Create recent transactions table using recent transactions table model
                JTable txnRTable = new JTable(txnRModel);
                txnRTable.setFont(new Font("Calibri", Font.PLAIN, 14));
                txnRTable.setRowHeight(20);
                txnRTable.setEnabled(false); //disable edit table by user
                txnRTable.setBounds(0, 300, 800, 275);
                txnRTable.setVisible(false);
                panel.add(txnRTable);

                //Add recent transactions table to a scrollpane
                JScrollPane scroll_table_r = new JScrollPane(txnRTable);
                scroll_table_r.setBounds(0, 300, 800, 85);
                scroll_table_r.setVisible(true);
                panel.add(scroll_table_r);

                //Add action listener to Recent Txs radio button to display recent transactions table
                r1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        txnRTable.setVisible(true);
                        scroll_table_r.setVisible(true);
                        txnTable.setVisible(false);
                        scroll_table.setVisible(false);
                    }
                });

                //Add action listener to All Txs radio button to display all transactions table
                r2.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        txnTable.setVisible(true);
                        scroll_table.setVisible(true);
                        txnRTable.setVisible(false);
                        scroll_table_r.setVisible(false);
                    }
                });
            }
        } catch (SQLException se) {
            System.out.println(se);
        }

        JButton exmini = new JButton("EXIT ");
        exmini.setBounds(340, 600, 100, 30);
        panel.add(exmini);
        
        exmini.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exmini.setBackground(Color.CYAN);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                exmini.setBackground(UIManager.getColor("control"));
            }
        });
        
        
        exmini.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent ae) {
                tp.addTab("Home", panel1);
                tp.setSelectedIndex(0);
                tp.remove(panel2);
                tp.remove(panel3);
                tp.remove(panel4);
                tp.remove(panel5);
                tp.remove(panel6);
            }
        });
        return panel;
    }
    //create pin change
    JPanel createPinChange() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(0, 102, 255));
        //panel.setBackground(Color.blue);
        JLabel lb1 = new JLabel("Pin Change", SwingConstants.CENTER);
        lb1.setBounds(0, 140, 800, 70);
        lb1.setBackground(Color.cyan);
        lb1.setOpaque(true);
        lb1.setFont(new Font("Calibri", Font.BOLD, 20));
        panel.add(lb1);

        JButton expin = new JButton("BACK ");
        expin.setBounds(320, 600, 100, 30);
        panel.add(expin);
        
        expin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                expin.setBackground(Color.CYAN);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                expin.setBackground(UIManager.getColor("control"));
            }
        });
        expin.addActionListener(new ActionListener() { //balance
            public void actionPerformed(ActionEvent ae) {
                tp.addTab("Home", panel1);
                tp.setSelectedIndex(0);
                tp.remove(panel2);
                tp.remove(panel3);
                tp.remove(panel4);
                tp.remove(panel5);
                tp.remove(panel6);
                _panel2.setVisible(false);
                _panel1.setVisible(true);
                pf1.setText("");
                pf2.setText("");
                pf22.setText("");
            }
        });

        //Old Pin Validation panel
        _panel1 = new JPanel();
        _panel2 = new JPanel();
        _panel1.setLayout(null);
        _panel1.setBounds(0, 283, 800, 150);
        _panel1.setBackground(Color.cyan);
      
        JLabel lb3 = new JLabel("Enter old pin: ");
        lb3.setBounds(225, 23, 200, 30);
        lb3.setFont(new Font("Calibri", Font.BOLD, 20));
        _panel1.add(lb3);
        //password field for old pin
        pf1 = new JPasswordField(15);
        pf1.setBounds(380, 20, 200, 30);
        _panel1.add(pf1);

        JButton btn = new JButton("Submit");
        btn.setBounds(345, 110, 100, 30);
        btn.setFont(new Font("Calibri", Font.BOLD, 15));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(Color.CYAN);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(UIManager.getColor("control"));
            }
        });
        
        
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try{
                int upin = Integer.parseInt(new String(pf1.getPassword()));
                if (pin == upin) {
                    //correct pin entered
                    _panel1.setVisible(false);
                    _panel2.setVisible(true);
                } else {
                    //invalid pin entered
                    JOptionPane.showMessageDialog(null, "Invalid Pin", "Error", JOptionPane.WARNING_MESSAGE);
                    pf1.setText("");//clear password field
                }
                }
                catch(Exception e){
                    //Handle non-integer input
                    JOptionPane.showMessageDialog(null, "Please enter a valid pin!!", "Error", JOptionPane.WARNING_MESSAGE);
                    pf1.setText("");
                }
            }
        });

        _panel1.add(btn);

        panel.add(_panel1);

        //New Pin Validation panel
        _panel2.setLayout(null);
        _panel2.setVisible(false);
        _panel2.setBounds(0, 283, 800, 150);
        _panel2.setBackground(Color.cyan);

        JLabel lbn1 = new JLabel("Enter new pin: ");
        lbn1.setBounds(225, 23, 200, 30);
        lbn1.setFont(new Font("Calibri", Font.BOLD, 20));
        _panel2.add(lbn1);

        pf2 = new JPasswordField(15);//password field for new pin
        pf2.setBounds(380, 20, 200, 30);
        _panel2.add(pf2);

        JLabel lbn2 = new JLabel("Confirm new pin: ");
        lbn2.setBounds(225, 63, 200, 30);
        lbn2.setFont(new Font("Calibri", Font.BOLD, 20));
        _panel2.add(lbn2);

        pf22 = new JPasswordField(15);//password field for confirm pin
        pf22.setBounds(380, 60, 200, 30);
        _panel2.add(pf22);

        JButton btn2 = new JButton("Submit");
        //panel2.getRootPane().setDefaultButton(btn2);
        btn2.setBounds(345, 115, 100, 30);
        btn2.setFont(new Font("Calibri", Font.BOLD, 15));
        btn2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                boolean confirm ;
                int npin,ncpin;
                try {
                    npin = Integer.parseInt(new String(pf2.getPassword()));
                    ncpin = Integer.parseInt(new String(pf22.getPassword()));
                    String s_pin = new String(pf2.getPassword());
                    if(npin<0){
                        //Handle invalid new pin
                        JOptionPane.showMessageDialog(null, "Please enter a valid pin!!", "Error", JOptionPane.WARNING_MESSAGE);
                        pf2.setText("");
                        pf22.setText("");
                    }
                    else if(s_pin.length() < 4){
                        //Handle incorrect pin length
                      JOptionPane.showMessageDialog(null, "Minimum pin length should be 4!!", "Error", JOptionPane.WARNING_MESSAGE); 
                      pf2.setText("");
                        pf22.setText("");
                    }
                    else if (npin == ncpin) {
                        //Update pin in database
                        String sql = "UPDATE ATMLOGIN.ACCHOLDER SET PIN=" + ncpin + "WHERE accountnumber = " + accountnumber;
                        System.out.println(sql);
                        try (Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/ATMDB", "atmlogin", "loginatm");
                                Statement statement = connection.createStatement();) {
                            statement.executeUpdate(sql);//Execute update query
                            System.out.println("ACCOUNTNUMBER: " + accountnumber + " NAME: " + name + " PIN: " + ncpin);
                            JOptionPane.showMessageDialog(null, "Pin changed successfully...", "Congratulations", JOptionPane.WARNING_MESSAGE);
                            displayAuth();//display login menu
                        } catch (SQLException se) {
                            System.out.println(se);
                        }
                    } else {
                        //new pin and confirm new pin dont match
                        JOptionPane.showMessageDialog(null, "Pins Don't match!!", "Error", JOptionPane.WARNING_MESSAGE);
                        pf2.setText("");
                        pf22.setText("");
                    }       
                } catch (Exception e) {
                    //Handle non-integer input 
                    JOptionPane.showMessageDialog(null, "Please enter a valid pin!!", "Error", JOptionPane.WARNING_MESSAGE);
                    pf2.setText("");
                    pf22.setText("");
                }

            }
        });

        _panel2.add(btn2);

        panel.add(_panel2);

        return panel;

    }

    JPanel createExit() {
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(0, 102, 255));
        
        JLabel exitlbl = new JLabel("Are you sure you want to exit?", SwingConstants.CENTER);
        exitlbl.setBounds(0, 140, 800, 70);
        exitlbl.setBackground(Color.cyan);
        exitlbl.setOpaque(true);
        exitlbl.setFont(new Font("Calibri", Font.BOLD, 20));
        panel.add(exitlbl);
        
        JButton btn = new JButton("Yes");
        btn.setBounds(280, 280, 100, 40);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(Color.CYAN);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(UIManager.getColor("control"));
            }
        });
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                  System.exit(0);//exit application menu
            }
        });
        panel.add(btn);

        JButton btnNo = new JButton("No");
        btnNo.setBounds(400, 280, 100, 40);
        
        btnNo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnNo.setBackground(Color.CYAN);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnNo.setBackground(UIManager.getColor("control"));
            }
        });
        
        btnNo.addActionListener(new ActionListener() {
            //go back to home
            public void actionPerformed(ActionEvent ae) {
                tp.addTab("Home", panel1);
                tp.setSelectedIndex(0);
                tp.remove(panel2);
                tp.remove(panel3);
                tp.remove(panel4);
                tp.remove(panel5);
                tp.remove(panel6);
            }
        });

        panel.add(btnNo);
        return panel;
    }

    ATMUI() {
        //Constructor for ATMUI class
        //Create JFrame 
        frm = new JFrame("ATM");
        frm.setSize(800, 800); //set frame width and height
        frm.setResizable(false); //disable window resizing
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //set close button operation to exit application
        frm.setLayout(null); 
        frm.setLocationRelativeTo(null); //set window to display at centre of screen
        //Create Authentication Page
        createAuth();
        
        frm.setVisible(true); //set frame visibility
    }
}

public class ATM {
    public static void main(String[] args) {
            //Test Database connection at startup
            try {

            Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/ATMDB", "atmlogin", "loginatm");

            if (conn != null) {
                System.out.println("Connected");
               
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //Create object of class ATMUI
        ATMUI atm = new ATMUI();
    }

}
