package com.cheroliv.network

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class WhoisGUI extends JFrame {

    JTextField searchString = new JTextField(30)
    JTextArea names = new JTextArea(15, 80)
    JButton findButton = new JButton("Find")
    ButtonGroup searchIn = new ButtonGroup()
    ButtonGroup searchFor = new ButtonGroup()
    JCheckBox exactMatch = new JCheckBox("Exact Match", true)
    JTextField chosenServer = new JTextField()
    Whois server

    WhoisGUI(Whois whois) {
        super("com.cheroliv.network.Whois")
        this.server = whois
        Container pane = this.contentPane

        Font f = new Font("Monospaced", Font.PLAIN, 12)
        names.font = f
        names.editable = false

        JPanel centerPanel = new JPanel()
        centerPanel.layout = new GridLayout(
                1, 1, 10, 10)
        JScrollPane jsp = new JScrollPane(names)
        centerPanel.add(jsp)
        pane.add("Center", centerPanel)

        // You don't want the buttons in the south and north
        // to fill the entire sections so add Panels there
        // and use FlowLayouts in the Panel
        JPanel northPanel = new JPanel()
        JPanel northPanelTop = new JPanel()
        northPanelTop.setLayout(new FlowLayout(FlowLayout.LEFT))
        northPanelTop.add(new JLabel("com.cheroliv.network.Whois: "))
        northPanelTop.add("North", searchString)
        northPanelTop.add(exactMatch)
        northPanelTop.add(findButton)
        northPanel.layout = new BorderLayout(2, 1)
        northPanel.add("North", northPanelTop)
        JPanel northPanelBottom = new JPanel()
        northPanelBottom.layout = new GridLayout(1, 3, 5, 5)
        northPanelBottom.add(initRecordType())
        northPanelBottom.add(initSearchFields())
        northPanelBottom.add(initServerChoice())
        northPanel.add("Center", northPanelBottom)

        pane.add("North", northPanel)

        ActionListener al = new LookupNames()
        findButton.addActionListener(al)
        searchString.addActionListener(al)
    }

    private JPanel initRecordType() {
        JPanel p = new JPanel()
        p.layout = new GridLayout(6, 2, 5, 2)
        p.add(new JLabel("Search for:"))
        p.add(new JLabel(""))

        JRadioButton any = new JRadioButton("Any", true)
        any.actionCommand = "Any"
        searchFor.add(any)
        p.add(any)

        p.add(this.makeRadioButton("Network"))
        p.add(this.makeRadioButton("Person"))
        p.add(this.makeRadioButton("Host"))
        p.add(this.makeRadioButton("Domain"))
        p.add(this.makeRadioButton("Organization"))
        p.add(this.makeRadioButton("Group"))
        p.add(this.makeRadioButton("Gateway"))
        p.add(this.makeRadioButton("ASN"))

        return p
    }

    private JRadioButton makeRadioButton(String label) {
        JRadioButton button = new JRadioButton(label, false)
        button.actionCommand = label
        searchFor.add(button)
        return button
    }

    private JRadioButton makeSearchInRadioButton(String label) {
        JRadioButton button = new JRadioButton(label, false)
        button.actionCommand = label
        searchIn.add(button)
        return button
    }

    private JPanel initSearchFields() {
        JPanel p = new JPanel()
        p.layout = new GridLayout(6, 1, 5, 2)
        p.add(new JLabel("Search In: "))

        JRadioButton all = new JRadioButton("All", true)
        all.actionCommand = "All"
        searchIn.add(all)
        p.add(all)

        p.add(this.makeSearchInRadioButton("Name"))
        p.add(this.makeSearchInRadioButton("Mailbox"))
        p.add(this.makeSearchInRadioButton("Handle"))

        return p
    }

    private JPanel initServerChoice() {
        final JPanel p = new JPanel()
        p.layout = new GridLayout(6, 1, 5, 2)
        p.add(new JLabel("Search At: "))

        chosenServer.text = server.host.hostName
        p.add(chosenServer)
        chosenServer.addActionListener(new ActionListener() {
            @Override
            void actionPerformed(ActionEvent event) {
                try {
                    server = new Whois(chosenServer.getText())
                } catch (UnknownHostException ex) {
                    JOptionPane.showMessageDialog(p,
                            ex.message, "Alert", JOptionPane.ERROR_MESSAGE)
                }
            }
        })

        return p
    }

    private class LookupNames implements ActionListener {

        @Override
        void actionPerformed(ActionEvent event) {
            names.text = ""
            SwingWorker<String, Object> worker = new Lookup()
            worker.execute()
        }
    }

    private class Lookup extends SwingWorker<String, Object> {

        @Override
        protected String doInBackground() throws Exception {
            Whois.SearchIn group = Whois.SearchIn.ALL
            Whois.SearchFor category = Whois.SearchFor.ANY

            String searchForLabel = searchFor.selection.actionCommand
            String searchInLabel = searchIn.selection.actionCommand

            if (searchInLabel.equals("Name")) group = Whois.SearchIn.NAME
            else if (searchInLabel.equals("Mailbox")) {
                group = Whois.SearchIn.MAILBOX
            } else if (searchInLabel.equals("Handle")) {
                group = Whois.SearchIn.HANDLE
            }

            if (searchForLabel.equals("Network")) {
                category = Whois.SearchFor.NETWORK
            } else if (searchForLabel.equals("Person")) {
                category = Whois.SearchFor.PERSON
            } else if (searchForLabel.equals("Host")) {
                category = Whois.SearchFor.HOST
            } else if (searchForLabel.equals("Domain")) {
                category = Whois.SearchFor.DOMAIN
            } else if (searchForLabel.equals("Organization")) {
                category = Whois.SearchFor.ORGANIZATION
            } else if (searchForLabel.equals("Group")) {
                category = Whois.SearchFor.GROUP
            } else if (searchForLabel.equals("Gateway")) {
                category = Whois.SearchFor.GATEWAY
            } else if (searchForLabel.equals("ASN")) {
                category = Whois.SearchFor.ASN
            }

            server.setHost(chosenServer.text)
            return server.lookUpNames(searchString.text,
                    category, group, exactMatch.selected)
        }

        @Override
        protected void done() {
            try {
                names.text = get()
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(WhoisGUI.this,
                        ex.message, "Lookup Failed", JOptionPane.ERROR_MESSAGE)
            }
        }
    }

    static void main(String[] args) {
        try {
            Whois server = new Whois()
            WhoisGUI a = new WhoisGUI(server)
            a.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
            a.pack()
            EventQueue.invokeLater(new FrameShower(a))
        } catch (UnknownHostException ex) {
            JOptionPane.showMessageDialog(null, "Could not locate default host "
                    + Whois.DEFAULT_HOST, "Error", JOptionPane.ERROR_MESSAGE)
        }
    }

    private static class FrameShower implements Runnable {

        private final Frame frame

        FrameShower(Frame frame) {
            this.frame = frame
        }

        @Override
        void run() {
            frame.visible = true
        }
    }
}
