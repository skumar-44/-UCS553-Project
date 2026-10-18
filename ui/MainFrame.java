
package ui;

import dao.HostDAO;
import dao.PlayerDAO;
import dao.TournamentDAO;
import model.Host;
import model.Player;
import model.Tournament;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class MainFrame extends JFrame {

    private final PlayerDAO playerDAO = new PlayerDAO();
    private final TournamentDAO tournamentDAO = new TournamentDAO();
    private final HostDAO hostDAO = new HostDAO();

    private final Color background = new Color(18, 18, 18);
    private final Color panelColor = new Color(27, 27, 27);
    private final Color tableColor = new Color(34, 34, 34);
    private final Color fieldColor = new Color(43, 43, 43);
    private final Color accent = new Color(235, 99, 79);
    private final Color textColor = new Color(240, 240, 240);
    private final Color mutedColor = new Color(165, 165, 165);
    private final Color borderColor = new Color(65, 65, 65);

    private final DefaultTableModel tournamentModel = createTableModel("ID", "Title", "Game", "Event Date",
            "Prize Pool");

    private final DefaultTableModel playerModel = createTableModel("ID", "Name", "Email", "Game ID");

    private final DefaultTableModel hostModel = createTableModel("ID", "Name", "Email", "Organization", "Contact");

    private final JTable tournamentTable = createTable(tournamentModel);
    private final JTable playerTable = createTable(playerModel);
    private final JTable hostTable = createTable(hostModel);

    public MainFrame() {
        setTitle("Arena | Gaming Tournament Manager");
        setSize(1050, 650);
        setMinimumSize(new Dimension(850, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(background);
        setLayout(new BorderLayout());

        JLabel heading = new JLabel("ARENA");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 27));
        heading.setForeground(accent);

        JLabel subtitle = new JLabel("GAMING TOURNAMENT MANAGER");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subtitle.setForeground(mutedColor);

        JPanel brandPanel = new JPanel();
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setBackground(background);
        brandPanel.setBorder(new EmptyBorder(18, 24, 15, 24));
        brandPanel.add(heading);
        brandPanel.add(Box.createVerticalStrut(3));
        brandPanel.add(subtitle);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabs.setBackground(Color.WHITE);
        tabs.setForeground(Color.BLACK);
        tabs.setOpaque(true);

        tabs.addTab("Tournaments", createTournamentPanel());
        tabs.addTab("Players", createPlayerPanel());
        tabs.addTab("Hosts", createHostPanel());

        // White tabs with black text
        tabs.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected void paintTabBackground(
                    Graphics g, int placement, int index,
                    int x, int y, int w, int h, boolean selected) {
                g.setColor(Color.WHITE);
                g.fillRect(x, y, w, h);
            }

            @Override
            protected void paintTabBorder(
                    Graphics g, int placement, int index,
                    int x, int y, int w, int h, boolean selected) {
                g.setColor(new Color(190, 190, 190));
                g.drawRect(x, y, w, h);
            }
        });

        add(brandPanel, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        refreshTournaments();
        refreshPlayers();
        refreshHosts();
    }

    // ---------------- TABLE STYLING ----------------

    private DefaultTableModel createTableModel(String... columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private JTable createTable(DefaultTableModel model) {
        JTable table = new JTable(model);

        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(32);
        table.setBackground(tableColor);
        table.setForeground(textColor);
        table.setGridColor(borderColor);
        table.setSelectionBackground(new Color(100, 48, 42));
        table.setSelectionForeground(Color.WHITE);
        table.setShowGrid(true);
        table.setFillsViewportHeight(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(45, 45, 45));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 38));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean selected,
                    boolean focused, int row, int column) {

                Component component = super.getTableCellRendererComponent(
                        table, value, selected, focused, row, column);

                if (selected) {
                    component.setBackground(new Color(100, 48, 42));
                    component.setForeground(Color.WHITE);
                } else {
                    component.setBackground(
                            row % 2 == 0
                                    ? tableColor
                                    : new Color(39, 39, 39));
                    component.setForeground(textColor);
                }

                setBorder(new EmptyBorder(0, 8, 0, 8));
                return component;
            }
        };

        renderer.setHorizontalAlignment(SwingConstants.LEFT);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        return table;
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(background);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panel.setBackground(background);
        return panel;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(accent);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(9, 15, 9, 15));
        return button;
    }

    private JScrollPane createScrollPane(JTable table) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(tableColor);
        scrollPane.setBorder(BorderFactory.createLineBorder(borderColor));
        scrollPane.getVerticalScrollBar().setBackground(panelColor);
        scrollPane.getHorizontalScrollBar().setBackground(panelColor);
        return scrollPane;
    }

    // ---------------- TOURNAMENTS ----------------

    private JPanel createTournamentPanel() {
        JPanel panel = createContentPanel();
        JPanel buttons = createButtonPanel();

        JButton add = createButton("Add Tournament");
        JButton edit = createButton("Edit Tournament");
        JButton delete = createButton("Delete Tournament");
        JButton refresh = createButton("Refresh");

        buttons.add(add);
        buttons.add(edit);
        buttons.add(delete);
        buttons.add(refresh);

        panel.add(buttons, BorderLayout.NORTH);
        panel.add(createScrollPane(tournamentTable), BorderLayout.CENTER);

        add.addActionListener(e -> addTournament());
        edit.addActionListener(e -> editTournament());
        delete.addActionListener(e -> deleteTournament());
        refresh.addActionListener(e -> refreshTournaments());

        return panel;
    }

    private void addTournament() {
        JTextField title = createDialogField();
        JTextField game = createDialogField();
        JTextField date = createDialogField();
        JTextField prize = createDialogField();

        Object[] fields = {
                "Tournament Title:", title,
                "Game:", game,
                "Event Date (YYYY-MM-DD):", date,
                "Prize Pool:", prize
        };

        if (!showForm("Add Tournament", fields))
            return;

        String t = title.getText().trim();
        String g = game.getText().trim();
        String d = date.getText().trim();

        try {
            double p = Double.parseDouble(prize.getText().trim());

            if (t.isEmpty() || g.isEmpty() || d.isEmpty() || p < 0) {
                showError("Enter valid tournament details.");
                return;
            }

            tournamentDAO.addTournament(new Tournament(0, t, g, d, p));
            refreshTournaments();

        } catch (NumberFormatException ex) {
            showError("Prize pool must be a valid number.");
        } catch (SQLException ex) {
            showError("Could not add tournament: " + ex.getMessage());
        }
    }

    private void editTournament() {
        int row = tournamentTable.getSelectedRow();
        if (row == -1) {
            showError("Select a tournament to edit.");
            return;
        }

        int id = (int) tournamentModel.getValueAt(row, 0);
        JTextField title = createDialogField(
                tournamentModel.getValueAt(row, 1).toString());
        JTextField game = createDialogField(
                tournamentModel.getValueAt(row, 2).toString());
        JTextField date = createDialogField(
                tournamentModel.getValueAt(row, 3).toString());
        JTextField prize = createDialogField(
                tournamentModel.getValueAt(row, 4).toString());

        Object[] fields = {
                "Tournament Title:", title,
                "Game:", game,
                "Event Date (YYYY-MM-DD):", date,
                "Prize Pool:", prize
        };

        if (!showForm("Edit Tournament", fields))
            return;

        String t = title.getText().trim();
        String g = game.getText().trim();
        String d = date.getText().trim();

        try {
            double p = Double.parseDouble(prize.getText().trim());

            if (t.isEmpty() || g.isEmpty() || d.isEmpty() || p < 0) {
                showError("Enter valid tournament details.");
                return;
            }

            tournamentDAO.updateTournament(new Tournament(id, t, g, d, p));
            refreshTournaments();

        } catch (NumberFormatException ex) {
            showError("Prize pool must be a valid number.");
        } catch (SQLException ex) {
            showError("Could not update tournament: " + ex.getMessage());
        }
    }

    private void deleteTournament() {
        int row = tournamentTable.getSelectedRow();
        if (row == -1) {
            showError("Select a tournament to delete.");
            return;
        }

        int id = (int) tournamentModel.getValueAt(row, 0);

        if (confirmDelete("this tournament")) {
            try {
                tournamentDAO.deleteTournament(id);
                refreshTournaments();
            } catch (SQLException ex) {
                showError("Could not delete tournament: " + ex.getMessage());
            }
        }
    }

    private void refreshTournaments() {
        try {
            tournamentModel.setRowCount(0);

            List<Tournament> list = tournamentDAO.getAllTournaments();
            for (Tournament t : list) {
                tournamentModel.addRow(new Object[] {
                        t.getId(), t.getTitle(), t.getGame(),
                        t.getEventDate(), t.getPrizePool()
                });
            }
        } catch (SQLException ex) {
            showError("Could not load tournaments: " + ex.getMessage());
        }
    }

    // ---------------- PLAYERS ----------------

    private JPanel createPlayerPanel() {
        JPanel panel = createContentPanel();
        JPanel buttons = createButtonPanel();

        JButton add = createButton("Add Player");
        JButton edit = createButton("Edit Player");
        JButton delete = createButton("Delete Player");
        JButton refresh = createButton("Refresh");

        buttons.add(add);
        buttons.add(edit);
        buttons.add(delete);
        buttons.add(refresh);

        panel.add(buttons, BorderLayout.NORTH);
        panel.add(createScrollPane(playerTable), BorderLayout.CENTER);

        add.addActionListener(e -> addPlayer());
        edit.addActionListener(e -> editPlayer());
        delete.addActionListener(e -> deletePlayer());
        refresh.addActionListener(e -> refreshPlayers());

        return panel;
    }

    private void addPlayer() {
        JTextField name = createDialogField();
        JTextField email = createDialogField();
        JTextField gameId = createDialogField();

        Object[] fields = {
                "Name:", name,
                "Email:", email,
                "Game ID:", gameId
        };

        if (!showForm("Add Player", fields))
            return;

        String n = name.getText().trim();
        String e = email.getText().trim();
        String g = gameId.getText().trim();

        if (n.isEmpty() || e.isEmpty() || g.isEmpty()) {
            showError("Fill in all player fields.");
            return;
        }

        try {
            playerDAO.addPlayer(new Player(0, n, e, g));
            refreshPlayers();
        } catch (SQLException ex) {
            showError("Could not add player: " + ex.getMessage());
        }
    }

    private void editPlayer() {
        int row = playerTable.getSelectedRow();
        if (row == -1) {
            showError("Select a player to edit.");
            return;
        }

        int id = (int) playerModel.getValueAt(row, 0);
        JTextField name = createDialogField(
                playerModel.getValueAt(row, 1).toString());
        JTextField email = createDialogField(
                playerModel.getValueAt(row, 2).toString());
        JTextField gameId = createDialogField(
                playerModel.getValueAt(row, 3).toString());

        Object[] fields = {
                "Name:", name,
                "Email:", email,
                "Game ID:", gameId
        };

        if (!showForm("Edit Player", fields))
            return;

        String n = name.getText().trim();
        String e = email.getText().trim();
        String g = gameId.getText().trim();

        if (n.isEmpty() || e.isEmpty() || g.isEmpty()) {
            showError("Fill in all player fields.");
            return;
        }

        try {
            playerDAO.updatePlayer(new Player(id, n, e, g));
            refreshPlayers();
        } catch (SQLException ex) {
            showError("Could not update player: " + ex.getMessage());
        }
    }

    private void deletePlayer() {
        int row = playerTable.getSelectedRow();
        if (row == -1) {
            showError("Select a player to delete.");
            return;
        }

        int id = (int) playerModel.getValueAt(row, 0);

        if (confirmDelete("this player")) {
            try {
                playerDAO.deletePlayer(id);
                refreshPlayers();
            } catch (SQLException ex) {
                showError("Could not delete player: " + ex.getMessage());
            }
        }
    }

    private void refreshPlayers() {
        try {
            playerModel.setRowCount(0);

            List<Player> list = playerDAO.getAllPlayers();
            for (Player p : list) {
                playerModel.addRow(new Object[] {
                        p.getId(), p.getName(), p.getEmail(), p.getGameId()
                });
            }
        } catch (SQLException ex) {
            showError("Could not load players: " + ex.getMessage());
        }
    }

    // ---------------- HOSTS ----------------

    private JPanel createHostPanel() {
        JPanel panel = createContentPanel();
        JPanel buttons = createButtonPanel();

        JButton add = createButton("Add Host");
        JButton edit = createButton("Edit Host");
        JButton delete = createButton("Delete Host");
        JButton refresh = createButton("Refresh");

        buttons.add(add);
        buttons.add(edit);
        buttons.add(delete);
        buttons.add(refresh);

        panel.add(buttons, BorderLayout.NORTH);
        panel.add(createScrollPane(hostTable), BorderLayout.CENTER);

        add.addActionListener(e -> addHost());
        edit.addActionListener(e -> editHost());
        delete.addActionListener(e -> deleteHost());
        refresh.addActionListener(e -> refreshHosts());

        return panel;
    }

    private void addHost() {
        JTextField name = createDialogField();
        JTextField email = createDialogField();
        JTextField organization = createDialogField();
        JTextField contact = createDialogField();

        Object[] fields = {
                "Name:", name,
                "Email:", email,
                "Organization:", organization,
                "Contact Number:", contact
        };

        if (!showForm("Add Host", fields))
            return;

        String n = name.getText().trim();
        String e = email.getText().trim();
        String o = organization.getText().trim();
        String c = contact.getText().trim();

        if (n.isEmpty() || e.isEmpty() || o.isEmpty() || c.isEmpty()) {
            showError("Fill in all host fields.");
            return;
        }

        try {
            hostDAO.addHost(new Host(0, n, e, o, c));
            refreshHosts();
        } catch (SQLException ex) {
            showError("Could not add host: " + ex.getMessage());
        }
    }

    private void editHost() {
        int row = hostTable.getSelectedRow();
        if (row == -1) {
            showError("Select a host to edit.");
            return;
        }

        int id = (int) hostModel.getValueAt(row, 0);
        JTextField name = createDialogField(
                hostModel.getValueAt(row, 1).toString());
        JTextField email = createDialogField(
                hostModel.getValueAt(row, 2).toString());
        JTextField organization = createDialogField(
                hostModel.getValueAt(row, 3).toString());
        JTextField contact = createDialogField(
                hostModel.getValueAt(row, 4).toString());

        Object[] fields = {
                "Name:", name,
                "Email:", email,
                "Organization:", organization,
                "Contact Number:", contact
        };

        if (!showForm("Edit Host", fields))
            return;

        String n = name.getText().trim();
        String e = email.getText().trim();
        String o = organization.getText().trim();
        String c = contact.getText().trim();

        if (n.isEmpty() || e.isEmpty() || o.isEmpty() || c.isEmpty()) {
            showError("Fill in all host fields.");
            return;
        }

        try {
            hostDAO.updateHost(new Host(id, n, e, o, c));
            refreshHosts();
        } catch (SQLException ex) {
            showError("Could not update host: " + ex.getMessage());
        }
    }

    private void deleteHost() {
        int row = hostTable.getSelectedRow();
        if (row == -1) {
            showError("Select a host to delete.");
            return;
        }

        int id = (int) hostModel.getValueAt(row, 0);

        if (confirmDelete("this host")) {
            try {
                hostDAO.deleteHost(id);
                refreshHosts();
            } catch (SQLException ex) {
                showError("Could not delete host: " + ex.getMessage());
            }
        }
    }

    private void refreshHosts() {
        try {
            hostModel.setRowCount(0);

            List<Host> list = hostDAO.getAllHosts();
            for (Host h : list) {
                hostModel.addRow(new Object[] {
                        h.getId(), h.getName(), h.getEmail(),
                        h.getOrganization(), h.getContactNumber()
                });
            }
        } catch (SQLException ex) {
            showError("Could not load hosts: " + ex.getMessage());
        }
    }

    // ---------------- DIALOGS ----------------

    private JTextField createDialogField() {
        return createDialogField("");
    }

    private JTextField createDialogField(String value) {
        JTextField field = new JTextField(value, 18);
        field.setBackground(fieldColor);
        field.setForeground(textColor);
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor),
                new EmptyBorder(5, 7, 5, 7)));
        return field;
    }

    private boolean showForm(String title, Object[] fields) {
        JPanel form = new JPanel(new GridLayout(0, 1, 5, 5));
        form.setBackground(panelColor);
        form.setBorder(new EmptyBorder(10, 10, 10, 10));

        for (Object field : fields) {
            if (field instanceof String) {
                JLabel label = new JLabel((String) field);
                label.setForeground(textColor);
                label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                form.add(label);
            } else if (field instanceof JTextField) {
                form.add((JTextField) field);
            }
        }

        UIManager.put("OptionPane.background", panelColor);
        UIManager.put("Panel.background", panelColor);
        UIManager.put("OptionPane.messageForeground", textColor);
        UIManager.put("Button.background", accent);
        UIManager.put("Button.foreground", Color.WHITE);

        int result = JOptionPane.showConfirmDialog(
                this, form, title,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        return result == JOptionPane.OK_OPTION;
    }

    private boolean confirmDelete(String itemName) {
        UIManager.put("OptionPane.background", panelColor);
        UIManager.put("Panel.background", panelColor);
        UIManager.put("OptionPane.messageForeground", textColor);

        return JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete " + itemName + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    }

    private void showError(String message) {
        UIManager.put("OptionPane.background", panelColor);
        UIManager.put("Panel.background", panelColor);
        UIManager.put("OptionPane.messageForeground", textColor);

        JOptionPane.showMessageDialog(
                this, message, "Arena",
                JOptionPane.ERROR_MESSAGE);
    }
}
