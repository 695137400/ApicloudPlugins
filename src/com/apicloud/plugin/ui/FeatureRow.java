package com.apicloud.plugin.ui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.<br/>
 * User: lizhichao<br/>
 * Date: 2018-6-8-0008<br/>
 * Time: 21:45:25<br/>
 * Author:lizhichao<br/>
 * Description: <span style="color:#63D3E9"></span><br/>
 */
public class FeatureRow extends JPanel {


  //  private JPanel jp1;//this
    private JScrollPane js1;
    private JPanel jpp1;

    public FeatureRow(String name) {
        setName(name);
        GridLayoutManager layoutManagerJp1 = new GridLayoutManager(2, 1);
        layoutManagerJp1.setSameSizeHorizontally(false);
        layoutManagerJp1.setSameSizeVertically(false);
        layoutManagerJp1.setHGap(-1);
        layoutManagerJp1.setVGap(-1);
        layoutManagerJp1.setMargin(new Insets(0, 0, 0, 0));
        setLayout(layoutManagerJp1);
        createTitle();

        GridConstraints keyConstraints = new GridConstraints();
        keyConstraints.setColumn(0);
        keyConstraints.setRow(1);
        keyConstraints.setRowSpan(1);
        keyConstraints.setColSpan(1);
        keyConstraints.setVSizePolicy(7);
        keyConstraints.setHSizePolicy(7);
        keyConstraints.setAnchor(0);
        keyConstraints.setFill(3);
        keyConstraints.setIndent(0);
        keyConstraints.setUseParentLayout(false);
        GridLayoutManager pl = new GridLayoutManager(1000, 1);
        pl.setSameSizeHorizontally(false);
        pl.setSameSizeVertically(false);
        pl.setHGap(-1);
        pl.setVGap(-1);
        pl.setMargin(new Insets(0, 0, 0, 0));
        jpp1 = new JPanel();
        jpp1.setLayout(pl);
        jpp1.setPreferredSize(new Dimension(1000, 1000));
        js1 = new JScrollPane();
        GridConstraints scrolConstraints = new GridConstraints();
        scrolConstraints.setColumn(0);
        scrolConstraints.setRow(0);
        scrolConstraints.setRowSpan(1);
        scrolConstraints.setColSpan(1);
        scrolConstraints.setVSizePolicy(0);
        scrolConstraints.setHSizePolicy(0);
        scrolConstraints.setAnchor(0);
        scrolConstraints.setFill(3);
        scrolConstraints.setIndent(0);
        scrolConstraints.setUseParentLayout(false);

        js1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        js1.setViewportView(jpp1);
        add(js1, keyConstraints);
         createRow(getRowCount(), null, null);
    }

    public void createRow(int index, String k, String v) {

        JPanel panel = new JPanel();
        panel.setName("row" + index);
        GridLayoutManager pl = new GridLayoutManager(1, 4);
        pl.setSameSizeHorizontally(false);
        pl.setSameSizeVertically(false);
        pl.setHGap(-1);
        pl.setVGap(-1);
        pl.setMargin(new Insets(0, 0, 0, 0));

        panel.setLayout(pl);
        JTextField key = new JTextField();
        key.setName("key" + index);
        if (null != k && "" != k) {
            key.setText(k);
        }
        GridConstraints keyConstraints = new GridConstraints();
        keyConstraints.setColumn(0);
        keyConstraints.setRow(0);
        keyConstraints.setRowSpan(1);
        keyConstraints.setColSpan(1);
        keyConstraints.setVSizePolicy(0);
        keyConstraints.setHSizePolicy(6);
        keyConstraints.setAnchor(8);
        keyConstraints.setFill(1);
        keyConstraints.setIndent(0);
        keyConstraints.setUseParentLayout(false);
        Dimension textDimension = new Dimension(200, 37);
        key.setMinimumSize(textDimension);
        panel.add(key, keyConstraints);

        JTextField value = new JTextField();
        value.setName("value" + index);
        if (null != v && "" != v) {
            value.setText(v);
        }
        GridConstraints valueConstraints = new GridConstraints();
        valueConstraints.setColumn(1);
        valueConstraints.setRow(0);
        valueConstraints.setRowSpan(1);
        valueConstraints.setColSpan(1);
        valueConstraints.setVSizePolicy(0);
        valueConstraints.setHSizePolicy(6);
        valueConstraints.setAnchor(8);
        valueConstraints.setFill(1);
        valueConstraints.setIndent(0);
        valueConstraints.setUseParentLayout(false);
        value.setMinimumSize(textDimension);
        panel.add(value, valueConstraints);
        JButton delete = new JButton("删除");
        Dimension buttonDimension = new Dimension(100, 37);
        JButton add = new JButton("添加");
        if (getRowCount() == 0) {
            delete.setEnabled(false);
        }
        add.putClientProperty("parent", this);
        add.setEnabled(false);
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("添加");
                JButton temp = (JButton) e.getSource();
                JPanel par = (JPanel) temp.getClientProperty("parent");
                jpp1 = ((FeatureRow) par).jpp1;
                temp.setEnabled(false);
                createRow(getRowCount(), null, null);
            }
        });
        delete.putClientProperty("parent", this);
        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JButton temp = (JButton) e.getSource();
                JPanel par = (JPanel) temp.getClientProperty("parent");
                jpp1 = ((FeatureRow) par).jpp1;
                jpp1.remove(temp.getParent());
                ArrayList<Map<String, String>> l = new ArrayList<>();
                for (int i = 0; i < jpp1.getComponents().length; i++) {
                    JPanel jp = (JPanel) jpp1.getComponent(i);
                    Map<String, String> rrr = new HashMap<>();
                    String key = ((JTextField) jp.getComponent(0)).getText();
                    String value = ((JTextField) jp.getComponent(1)).getText();
                    rrr.put("key", key);
                    rrr.put("value", value);
                    l.add(rrr);
                }
                jpp1.removeAll();
                for (int i = 0; i < l.size(); i++) {
                    createRow(i, l.get(i).get("key"), l.get(i).get("value"));
                }
                setRowCount(getRowCount() - 1);
                par.updateUI();
            }
        });
        GridConstraints addConstraints = new GridConstraints();
        addConstraints.setColumn(2);
        addConstraints.setRow(0);
        addConstraints.setRowSpan(1);
        addConstraints.setColSpan(1);
        addConstraints.setVSizePolicy(0);
        addConstraints.setHSizePolicy(3);
        addConstraints.setAnchor(0);
        addConstraints.setFill(1);
        addConstraints.setIndent(0);
        addConstraints.setUseParentLayout(false);
        panel.add(add, addConstraints);
        add.setMinimumSize(buttonDimension);


        GridConstraints deleteConstraints = new GridConstraints();
        deleteConstraints.setColumn(3);
        deleteConstraints.setRow(0);
        deleteConstraints.setRowSpan(1);
        deleteConstraints.setColSpan(1);
        deleteConstraints.setVSizePolicy(0);
        deleteConstraints.setHSizePolicy(3);
        deleteConstraints.setAnchor(0);
        deleteConstraints.setFill(1);
        deleteConstraints.setIndent(0);
        deleteConstraints.setUseParentLayout(false);
        panel.add(delete, deleteConstraints);
        delete.setMinimumSize(buttonDimension);

        GridConstraints jpConstraints = new GridConstraints();
        jpConstraints.setColumn(0);
        jpConstraints.setRow(jpp1.getComponentCount() + 1);
        jpConstraints.setRowSpan(1);
        jpConstraints.setColSpan(1);
        jpConstraints.setVSizePolicy(3);
        jpConstraints.setHSizePolicy(3);
        jpConstraints.setAnchor(0);
        jpConstraints.setFill(3);
        jpConstraints.setIndent(0);
        jpConstraints.setUseParentLayout(false);

        jpp1.add(panel, jpConstraints);

        for (int i = 0; i < jpp1.getComponentCount(); i++) {
            JPanel row = (JPanel) jpp1.getComponents()[i];
            if (i == jpp1.getComponentCount() - 1) {
                row.getComponents()[2].setEnabled(true);
            } else {
                row.getComponents()[2].setEnabled(false);
            }
            if (i == 0) {
                row.getComponents()[3].setEnabled(false);
            }
        }
        jpp1.updateUI();
        setRowCount(getRowCount() + 1);
        System.out.println(rowCount);
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public void createTitle() {

        JPanel panel = new JPanel();
        GridLayoutManager pl = new GridLayoutManager(1, 4);
        pl.setSameSizeHorizontally(false);
        pl.setSameSizeVertically(false);
        pl.setHGap(-1);
        pl.setVGap(-1);
        pl.setMargin(new Insets(0, 0, 0, 0));
        panel.setLayout(pl);
        Dimension dimension = new Dimension(600, 37);
        panel.setPreferredSize(dimension);
        JLabel key = new JLabel("key");
        key.setHorizontalAlignment(JTextField.CENTER);
        GridConstraints keyConstraints = new GridConstraints();
        keyConstraints.setColumn(0);
        keyConstraints.setRow(0);
        keyConstraints.setRowSpan(1);
        keyConstraints.setColSpan(1);
        keyConstraints.setVSizePolicy(0);
        keyConstraints.setHSizePolicy(6);
        keyConstraints.setAnchor(8);
        keyConstraints.setFill(1);
        keyConstraints.setIndent(0);
        keyConstraints.setUseParentLayout(false);
        Dimension textDimension = new Dimension(200, 37);
        key.setMinimumSize(textDimension);
        panel.add(key, keyConstraints);

        JLabel value = new JLabel("value");
        value.setHorizontalAlignment(JTextField.CENTER);
        GridConstraints valueConstraints = new GridConstraints();
        valueConstraints.setColumn(1);
        valueConstraints.setRow(0);
        valueConstraints.setRowSpan(1);
        valueConstraints.setColSpan(1);
        valueConstraints.setVSizePolicy(0);
        valueConstraints.setHSizePolicy(6);
        valueConstraints.setAnchor(8);
        valueConstraints.setFill(1);
        valueConstraints.setIndent(0);
        valueConstraints.setUseParentLayout(false);
        value.setMinimumSize(textDimension);
        panel.add(value, valueConstraints);

        Dimension buttonDimension = new Dimension(100, 37);
        JLabel add = new JLabel("操作");
        add.setHorizontalAlignment(JTextField.CENTER);
        GridConstraints addConstraints = new GridConstraints();
        addConstraints.setColumn(2);
        addConstraints.setRow(0);
        addConstraints.setRowSpan(1);
        addConstraints.setColSpan(1);
        addConstraints.setVSizePolicy(0);
        addConstraints.setHSizePolicy(3);
        addConstraints.setAnchor(0);
        addConstraints.setFill(1);
        addConstraints.setIndent(0);
        addConstraints.setUseParentLayout(false);
        add.setMinimumSize(buttonDimension);
        panel.add(add, addConstraints);

        JCheckBox forceBind = new JCheckBox();
        forceBind.setText("强制绑定");
        forceBind.setSelected(true);
        forceBind.setHorizontalAlignment(JTextField.CENTER);
        GridConstraints forceBindConstraints = new GridConstraints();
        forceBindConstraints.setColumn(3);
        forceBindConstraints.setRow(0);
        forceBindConstraints.setRowSpan(1);
        forceBindConstraints.setColSpan(1);
        forceBindConstraints.setVSizePolicy(0);
        forceBindConstraints.setHSizePolicy(3);
        forceBindConstraints.setAnchor(0);
        forceBindConstraints.setFill(1);
        forceBindConstraints.setIndent(0);
        forceBindConstraints.setUseParentLayout(false);
        //add.setMinimumSize(buttonDimension);
        panel.add(forceBind, forceBindConstraints);


        GridConstraints jpConstraints = new GridConstraints();
        jpConstraints.setColumn(0);
        jpConstraints.setRow(0);
        jpConstraints.setRowSpan(1);
        jpConstraints.setColSpan(1);
        jpConstraints.setVSizePolicy(3);
        jpConstraints.setHSizePolicy(3);
        jpConstraints.setAnchor(0);
        jpConstraints.setFill(1);
        jpConstraints.setIndent(0);
        jpConstraints.setUseParentLayout(false);

        add(panel, jpConstraints);
        updateUI();
    }

    private int rowCount = 0;
    static JFrame frame = new JFrame("FeatureRow");

    public static void main(String[] args) {

        frame.setContentPane(new FeatureRow("1"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setBounds(500, 400, 600, 500);
    }
}
