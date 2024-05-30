package MyToolWindow;

import entity.ActivationDTO;
import entity.R;
import utils.Tools;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static utils.GlobalSetting.ACTIVATE_CODE;
import static utils.GlobalSetting.SUCCESS_CODE;

public class Activate {
    private JPanel panel1;
    private JButton confirmButton;
    private JTextArea activationCodeText;

    final private String hintText = "Please enter your activation code here.";

    static  JFrame frame = new JFrame("Activation");

    public Activate() {
        activationCodeText.addFocusListener(new JTextAreaHintListener(activationCodeText, hintText));

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String activationCode = activationCodeText.getText();


                if(activationCode == null || activationCode.equals(hintText)){
                    JOptionPane.showMessageDialog(panel1,"You have not entered yet.", "Message", JOptionPane.ERROR_MESSAGE);
                    return;
                }


                try {
                    String json = Tools.object2Json(new ActivationDTO(activationCode));
                    R r = Tools.httpPostHelper(json, "activate");
                    if (r.getCode() == SUCCESS_CODE) {
                        Tools.saveLocalProperties(ACTIVATE_CODE, activationCode);
                    }
                    Tools.showMessage(r, panel1);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
        });
    }


    public static void main(String[] args) {
        frame.setContentPane(new Activate().panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400,200);
        frame.setLocation(500,200);
        frame.setVisible(true);
    }
}
