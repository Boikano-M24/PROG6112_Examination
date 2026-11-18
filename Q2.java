package q2;

import java.awt.GridLayout;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 * Product App - captures a mattress sale, calculates VAT, commission and
 * total, shows the result and logs it to data.txt.
 */
public class Q2 extends JFrame {

    private static final String DATA_FILE = "data.txt";
    private static final String STARS = "*********************************";

    // Q.2.2 - input controls
    private final JTextField txtCustomerName = new JTextField(20);
    private final JComboBox<String> cmbProduct = new JComboBox<>(new String[]{
        "Cozy Nights Monarch",
        "Restonic Barbados",
        "Sealy Phuket"
    });
    private final JTextField txtPrice = new JTextField(20);
    private final JButton btnProcess = new JButton("PROCESS");

    public Q2() {
        super("Product App");
        buildMenu();
        buildForm();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    // Q.2.1 / Q.2.2 - form layout
    private void buildForm() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 15));
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        panel.add(new JLabel("Customer Name:"));
        panel.add(txtCustomerName);
        panel.add(new JLabel("Select Product:"));
        panel.add(cmbProduct);
        panel.add(new JLabel("Enter Price:"));
        panel.add(txtPrice);
        panel.add(new JLabel(""));
        panel.add(btnProcess);

        btnProcess.addActionListener(e -> processSale());

        setContentPane(panel);
    }

    // Q.2.4 - menu system
    private void buildMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        JMenu toolsMenu = new JMenu("Tools");
        JMenuItem processItem = new JMenuItem("Process");
        processItem.addActionListener(e -> processSale()); // same logic as the button
        JMenuItem clearItem = new JMenuItem("Clear");
        clearItem.addActionListener(e -> clearForm());
        toolsMenu.add(processItem);
        toolsMenu.add(clearItem);

        menuBar.add(fileMenu);
        menuBar.add(toolsMenu);
        setJMenuBar(menuBar);
    }

    // Q.2.3 - shared by the Process button and the Tools > Process menu item
    private void processSale() {
        String customerName = txtCustomerName.getText().trim();
        String product = (String) cmbProduct.getSelectedItem();

        if (customerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the customer's name.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            txtCustomerName.requestFocus();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(txtPrice.getText().trim());
            if (price <= 0) {
                throw new NumberFormatException("Price must be positive");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid price greater than 0.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            txtPrice.requestFocus();
            return;
        }

        IProductCalculations calc = new ProductCalculations(price);
        double vat = calc.GetVat();
        double commission = calc.GetCommission();
        double total = calc.GetTotal();

        String message = "Customer Name: " + customerName + "\n"
                + "Product: " + product + "\n"
                + "VAT: R " + vat + "\n"
                + "COMMISSION: R " + commission + "\n"
                + "TOTAL: R " + total;

        JOptionPane.showMessageDialog(this, message, "Message", JOptionPane.INFORMATION_MESSAGE);

        saveToFile(customerName, product, vat, commission, total);
    }

    // Q.2.3 - write the data log (appends so earlier sales are kept)
    private void saveToFile(String customerName, String product,
                            double vat, double commission, double total) {
        try (PrintWriter out = new PrintWriter(new FileWriter(DATA_FILE, true))) {
            out.println("DATA LOG");
            out.println(STARS);
            out.println("Customer Name: " + customerName);
            out.println("Product: " + product);
            out.println("VAT: R " + vat);
            out.println("COMMISSION: R " + commission);
            out.println("TOTAL: R " + total);
            out.println(STARS);
            out.println();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not save to " + DATA_FILE + ": " + ex.getMessage(),
                    "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Q.2.4 - Tools > Clear
    private void clearForm() {
        txtCustomerName.setText("");
        txtPrice.setText("");
        cmbProduct.setSelectedIndex(0);
        txtCustomerName.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Q2().setVisible(true));
    }
}

// Q.2.5 - interface
interface IProductCalculations {
    double GetVat();

    double GetCommission();

    double GetTotal();
}

// Q.2.5 - class implementing the interface
class ProductCalculations implements IProductCalculations {

    private static final double VAT_RATE = 0.15;
    private static final double COMMISSION_RATE = 0.05;

    private final double price;

    public ProductCalculations(double price) {
        this.price = price;
    }

    @Override
    public double GetVat() {
        return round(price * VAT_RATE);
    }

    @Override
    public double GetCommission() {
        return round(price * COMMISSION_RATE);
    }

    @Override
    public double GetTotal() {
        return round(price + GetVat() + GetCommission());
    }

    // Rounds to 2 decimal places to avoid floating point noise (e.g. 524.9999999)
    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}