package MyToolWindow;


import entity.CommentFeedBackDTO;
import entity.R;
import utils.Tools;
import application.Context;
import application.Comment;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;

import static utils.GlobalSetting.LATEST_CODE_TRANSFER_KEY;

public class MyToolWindowSubmit {
    private static int feedback_id=-1;
    private int[] score = new int[3];
    private Context context;

    private JTextArea comment;
    private JRadioButton a1;
    private JRadioButton a2;
    private JRadioButton a3;
    private JRadioButton a4;
    private JRadioButton a5;
    private JRadioButton b1;
    private JRadioButton b2;
    private JRadioButton b3;
    private JRadioButton b4;
    private JRadioButton b5;
    private JRadioButton c1;
    private JRadioButton c2;
    private JRadioButton c3;
    private JRadioButton c4;
    private JRadioButton c5;
    private JTextArea commentOfUser;
    private JScrollPane scrollOfComment;
    private JPanel window;
    private JTextArea tips;
    private JButton submit;
    private JButton feedback;
    private JScrollPane usrComment;
    private JPanel Q1Ans;
    private JButton copyButton;
    private JButton activate;

    //获取注释内容
    public void setComment(Comment fc) {
        comment.setText(fc.getComment());
    }

    public MyToolWindowSubmit(Context c) {
//        tips.setText("Please indicate the level to which you agree with the following statements:"+"\n"+"(1: Strongly disagree 2: Disagree 3: Neutral 4: Agree 5: Strongly Agree)");
//        Q1.setText("1.Independent of other factors, I feel that the summary is accurate.");
//        Q2.setText("2.The summary is missing important information, and that can hinder the understanding of the method.");
//        Q3.setText("3.The summary contains a lot of unnecessary information.");
        comment.setBorder(null);
        feedback.setContentAreaFilled(false);
        feedback.setBorderPainted(false);
        feedback.setPreferredSize(new Dimension(20,2));
//        feedback.setBorder(null);
//        feedback.setTransferHandler(null);
        this.context = c;
        TextBorderUtlis Border = new TextBorderUtlis(new Color(71,71,71),1,false);
        scrollOfComment.setBorder(Border);
//        scrollOfFeedback.setBorder(Border);
        a1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[0]=1;
            }
        });
        a2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[0]=2;
            }
        });
        a3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[0]=3;
            }
        });
        a4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[0]=4;
            }
        });
        a5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[0]=5;
            }
        });
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[1]=1;
            }
        });
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[1]=2;
            }
        });
        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[1]=3;
            }
        });
        b4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[1]=4;
            }
        });
        b5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[1]=5;
            }
        });
        c1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[2]=1;
            }
        });
        c2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[2]=2;
            }
        });
        c3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[2]=3;
            }
        });
        c4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[2]=4;
            }
        });
        c5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                score[2]=5;
            }
        });
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                for(int i = 0;i<3;i++){
                    if(score[i] == 0){
                        JOptionPane.showMessageDialog(window,"You have not scored yet, please score for us.");
                        return;
                    }
                }

                String UserComment = commentOfUser.getText();

                try {
                    String taskId = Tools.getLocalProperties(LATEST_CODE_TRANSFER_KEY);
                    CommentFeedBackDTO fb = new CommentFeedBackDTO(score, UserComment, taskId);
                    R r = Tools.httpPostHelper(Tools.object2Json(fb), "commentFeedback");
                    Tools.showMessage(r, window);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

//                JOptionPane.showMessageDialog(window,"Success", "Message", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        feedback.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Suggestions.main(null);
            }
        });
        activate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Activate.main(null);
            }
        });

        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                copyToClipboard(comment.getText());
            }
        });
//        commentOfUser.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusGained(FocusEvent e) {
//                super.focusGained(e);
//                if(!Objects.equals(commentOfUser.getText(), "Please write your comment here.")){
//                    commentOfUser.setText("");
//                }
//            }
//            @Override
//            public void focusLost(FocusEvent e) {
//                super.focusLost(e);
//                if(!Objects.equals(commentOfUser.getText(), "")){
//                    commentOfUser.setText("Please write your comment here.");
//                }
//            }
//        });


        commentOfUser.addFocusListener(new JTextAreaHintListener(commentOfUser,"Please write your suggestions here."));
    }

    public JComponent getContent() {
        return window;
    }

    public static void copyToClipboard(String content) {
        // 获取系统剪切板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        // 创建一个StringSelection对象，用于包装要复制的文本内容
        StringSelection stringSelection = new StringSelection(content);

        // 将StringSelection对象设置为剪切板的内容
        clipboard.setContents(stringSelection, null);
    }
}
