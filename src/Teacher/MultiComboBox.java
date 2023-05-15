package Teacher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicArrowButton;

/**
 * 下拉复选框组件
 *
 */
public class MultiComboBox extends JComponent implements ActionListener {

    private Object[] values;
    private MultiPopup popup;
    private JTextField editor;
    protected JButton arrowButton;

    public MultiComboBox(Object[] value) {
        values = value;
        initComponent();
    }

    private void initComponent() {
        this.setLayout(new BorderLayout());
        popup = new MultiPopup(values);
        editor = new JTextField();
        editor.setBackground(Color.WHITE);
        editor.setEditable(false);
        editor.setPreferredSize(new Dimension(140, 22));
        editor.addActionListener(this);
        arrowButton = createArrowButton();
        arrowButton.addActionListener(this);
        add(editor, BorderLayout.WEST);
        add(arrowButton, BorderLayout.CENTER);
    }

    public void readdItem(Object[] value)
    {
        popup.readdItem(value);
    }

   //获取选中的数据
    public Object[] getSelectedValues() {
        return popup.getSelectedValues();
    }

    //设置需要选中的值
    public void setSelectValues(Object[] selectvalues) {
        popup.setSelectValues(selectvalues);
        setText(selectvalues);
    }

    private void setText(Object[] values) {
        if (values.length > 0) {
            String value = Arrays.toString(values);
            value = value.replace("[", "");
            value = value.replace("]", "");
            editor.setText(value);
        }else {
            editor.setText("");
        }
    }


    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
        if (!popup.isVisible()) {
            popup.show(this, 0, getHeight());
        }
    }

    protected JButton createArrowButton() {
        JButton button = new BasicArrowButton(BasicArrowButton.SOUTH, UIManager.getColor("ComboBox.buttonBackground"),
                UIManager.getColor("ComboBox.buttonShadow"), UIManager.getColor("ComboBox.buttonDarkShadow"),
                UIManager.getColor("ComboBox.buttonHighlight"));
        button.setName("ComboBox.arrowButton");
        return button;
    }


    //内部类MultiPopup

    public class MultiPopup extends JPopupMenu implements ActionListener {
        private Object[] values;
        private List<JCheckBox> checkBoxList = new ArrayList<JCheckBox>();
        private JButton commitButton;
        private JButton cancelButton;

        private JPanel checkboxPane = new JPanel();
        private JPanel buttonPane = new JPanel();


        public MultiPopup(Object[] value) {
            super();

            values = value;
            initComponent();
        }

        private void initComponent() {
            this.setLayout(new BorderLayout());
            for (Object v : values) {
                JCheckBox temp = new JCheckBox(v.toString());
                checkBoxList.add(temp);
            }

            if (checkBoxList.get(0).getText().equals("全选")) {
                checkBoxList.get(0).addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent e) {
                        if (checkBoxList.get(0).isSelected()) {
                            for (int i = 1; i < checkBoxList.size(); i++) {
                                if (!checkBoxList.get(i).isSelected()) {
                                    checkBoxList.get(i).setSelected(true);
                                }
                            }
                        } else {
                            for (int i = 1; i < checkBoxList.size(); i++) {
                                if (checkBoxList.get(i).isSelected()) {
                                    checkBoxList.get(i).setSelected(false);
                                }
                            }
                        }
                    }
                });
            }

            checkboxPane.setLayout(new GridLayout(checkBoxList.size(), 1, 3, 3));
            for (JCheckBox box : checkBoxList) {
                checkboxPane.add(box);
            }

            commitButton = new JButton("确定");
            commitButton.addActionListener(this);

            cancelButton = new JButton("取消");
            cancelButton.addActionListener(this);

            buttonPane.add(commitButton);
            buttonPane.add(cancelButton);
            this.add(checkboxPane, BorderLayout.CENTER);
            this.add(buttonPane, BorderLayout.SOUTH);
        }

        public void readdItem(Object[] values)
        {
            checkboxPane.removeAll();

            checkBoxList = new ArrayList<JCheckBox>();
            this.values = values;

            for (Object v : values) {
                JCheckBox temp = new JCheckBox(v.toString());
                checkBoxList.add(temp);
            }

            if (checkBoxList.get(0).getText().equals("全选")) {
                checkBoxList.get(0).addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent e) {
                        if (checkBoxList.get(0).isSelected()) {
                            for (int i = 1; i < checkBoxList.size(); i++) {
                                if (!checkBoxList.get(i).isSelected()) {
                                    checkBoxList.get(i).setSelected(true);
                                }
                            }
                        } else {
                            for (int i = 1; i < checkBoxList.size(); i++) {
                                if (checkBoxList.get(i).isSelected()) {
                                    checkBoxList.get(i).setSelected(false);
                                }
                            }
                        }
                    }
                });
            }

            checkboxPane.setLayout(new GridLayout(checkBoxList.size(), 1, 3, 3));
            for (JCheckBox box : checkBoxList) {
                checkboxPane.add(box);
            }
        }

        public void setSelectValues(Object[] values) {
            if (values.length > 0) {
                for (int i = 0; i < values.length; i++) {
                    for (int j = 0; j < checkBoxList.size(); j++) {
                        if (values[i].equals(checkBoxList.get(j).getText())) {
                            checkBoxList.get(j).setSelected(true);
                        }
                    }
                }
                setText(getSelectedValues());
            }
        }


        public Object[] getSelectedValues() {
            List<Object> selectedValues = new ArrayList<Object>();

            if (checkBoxList.get(0).getText().equals("全选")) {
                if (checkBoxList.get(0).isSelected()) {
                    for (int i = 1; i < checkBoxList.size(); i++) {
                        selectedValues.add(values[i]);
                    }
                } else {
                    for (int i = 1; i < checkBoxList.size(); i++) {
                        if (checkBoxList.get(i).isSelected()) {
                            selectedValues.add(values[i]);
                        }
                    }
                }
            } else {
                for (int i = 0; i < checkBoxList.size(); i++) {
                    if (checkBoxList.get(i).isSelected()) {
                        selectedValues.add(values[i]);
                    }
                }
            }

            return selectedValues.toArray(new Object[selectedValues.size()]);
        }



        @Override
        public void actionPerformed(ActionEvent arg0) {
            // TODO Auto-generated method stub
            Object source = arg0.getSource();
            if (source instanceof JButton) {
                JButton button = (JButton) source;
                if (button.equals(commitButton)) {
                    setText(getSelectedValues());
                    popup.setVisible(false);
                } else if (button.equals(cancelButton)) {
                    popup.setVisible(false);
                }
            }
        }

    }
}