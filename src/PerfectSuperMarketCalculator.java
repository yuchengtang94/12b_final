import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.*;
import java.util.List;

/*


For the final project you are to implement a sophisticated application with a Graphical User Interface which demonstrates that you have mastered the learning objectives for the course.

The Final Project should meet the following criteria:
it should have a Graphical User Interface with at least four new features that haven't been covered in class
it should be able to save data to a file and load it back
it should use a HashMap to  read data from a file and provide access to the data in the app

You will need to write a reflection in which you explain
what you personally did for the app, and what every one else in the team did
what grade you would give yourself and everyone else (and why)
what you learned in creating this app
what you did well
what you would do if you had more time...
 */

public class PerfectSuperMarketCalculator {

    public static void writeMapToFile(Map<String,String>d,String filename){
        try {
            PrintWriter writer = new PrintWriter(filename, "UTF-8");
            Set<String> keys = d.keySet();
            for(String name: keys){
                writer.println(name+"|"+d.get(name));
            }
            writer.close();
        } catch (Exception e){
            System.out.println("Problem writing to file: "+e);
        }
    }

    public static Map<String,String> readMapFromFile(String filename){
        Map<String,String> d = new TreeMap<String,String>();
        try{
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()){
                String line = scanner.nextLine();
                int delimiter = line.indexOf("|");
                String key = line.substring(0,delimiter);
                String value = line.substring(delimiter+1);
                d.put(key,value);
            }
            scanner.close();
        } catch (FileNotFoundException e){
            System.out.println("Problem reading map from file "+e);
        }
        return d;
    }

    public static void main(String[] args){

        Map<String,String> map = readMapFromFile("src/data.txt");


        Vector<String> columnNames = new Vector<>();
        columnNames.add("Product");
        columnNames.add("Price");
        columnNames.add("Amount");
        columnNames.add("Discount");
        columnNames.add("Total");

        Vector<Vector<String>> currentRecords = new Vector<>();

//        String[] columnNames = {"Product",
//                "Price",
//                "Amount",
//                "Total"};

//        String[][] currentRecords = new String[4][0];
//        double sum_price = 0;
//        List<List<String>> currentRecords = new ArrayList<>();

        JFrame frame = new JFrame("Perfect Calculator for SuperMarket");
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel header = new JLabel("<html><h1>Perfect Calculator for SuperMarket</html></h1>");
        header.setHorizontalAlignment(SwingConstants.CENTER);

//        JTextArea left_text = new JTextArea("pickup fee: $5 \ndistance fee $3/mile \ntime fee: $0.20/minute");

        JPanel center_panel = new JPanel();
        center_panel.setLayout(new GridLayout(0,2));
        center_panel.setBackground(Color.YELLOW);

        JTextField name = new JTextField("cola");
        JTextField price = new JTextField("3");

        JTextField amount = new JTextField("13");

        JCheckBox checkBox = new JCheckBox("on sale");
        JSlider sale = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);

        JLabel current_saleprice = new JLabel("100%");



        center_panel.add(new JLabel("Product Name:"));
        center_panel.add(name);
        center_panel.add(new JLabel("Price:"));
        center_panel.add(price);
        center_panel.add(new JLabel("Amount"));
        center_panel.add(amount);
        center_panel.add(checkBox);
        center_panel.add(sale);
        center_panel.add(new JLabel("Sale Price(0 ~ 100%):"));
        center_panel.add(current_saleprice);
//        center_panel

        JPanel button_panel = new JPanel();

        button_panel.setLayout(new GridLayout(3,0));



        JButton cal_button = new JButton("<html><h1>Calculate Pay</html></h1>");

        JButton reset_button = new JButton("<html><h1>Reset</html></h1>");

        JButton save_data_button = new JButton("<html><h1>Save Product/Price to DataBase</html></h1>");




        button_panel.add(cal_button);
        button_panel.add(reset_button);
        button_panel.add(save_data_button);

        // result table

        JPanel table_panel = new JPanel();
        table_panel.setLayout(new BorderLayout());


        DefaultTableModel model = new DefaultTableModel(currentRecords, columnNames);


        JLabel result_area = new JLabel("Total: 0$");

        JTable result_table = new JTable(model);

        JScrollPane scroll = new JScrollPane(result_table);

        table_panel.add(scroll, BorderLayout.CENTER);
        table_panel.add(result_area, BorderLayout.PAGE_END);




        panel.add(header, BorderLayout.PAGE_START);
//        panel.add(left_text, BorderLayout.LINE_START);
        panel.add(center_panel, BorderLayout.CENTER);
        panel.add(button_panel, BorderLayout.LINE_END);
        panel.add(table_panel, BorderLayout.PAGE_END);

        frame.setContentPane(panel);
        frame.setSize(800,1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

//        sale slider listener
        sale.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                current_saleprice.setText(sale.getValue()+ "%");
//                System.out.println(sale.getValue());
            }
        });

        name.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                System.out.println("get change");
                String product_name = name.getText().trim();
                if (map.containsKey(product_name)) {
                    price.setText(map.get(product_name));
                }
            }
        });


        cal_button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String product_name = name.getText().trim();
                double product_price = Double.parseDouble(price.getText().trim());
                double product_amount = Double.parseDouble(amount.getText().trim());
                int sale_percent = 100;
                boolean onSale = checkBox.isSelected();

                if (onSale) {
                    sale_percent = sale.getValue();
                }
                Vector<String> newRecord = new Vector<>();
                newRecord.add(product_name);
                newRecord.add(product_price + "");
                newRecord.add(product_amount + "");
                newRecord.add(sale_percent + "%");
                newRecord.add(product_price * product_amount * sale_percent / 100 + "");

                currentRecords.add(newRecord);
                String temp = result_area.getText().trim().replaceAll("Total:", "").trim();
                String result = temp.substring(0, temp.length() - 1);

                double result_num = Double.parseDouble(result) + product_price * product_amount * sale_percent / 100;
                result_area.setText("Total: " + String.format("%.2f", result_num) + "$");
                model.fireTableDataChanged();
                result_table.setModel(model);
            }
        });

        save_data_button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String product_name = name.getText().trim();
                double product_price = Double.parseDouble(price.getText().trim());
                map.put(product_name, product_price + "");
                writeMapToFile(map, "src/data.txt");
            }
        });

        reset_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentRecords.clear();
                model.fireTableDataChanged();
                result_table.setModel(model);
                result_area.setText("Total: 0$");
            }
        });
    }
}
