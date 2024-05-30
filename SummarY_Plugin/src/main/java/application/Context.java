package application;

import entity.CommentGenerationDTO;
import entity.R;
import utils.Tools;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.IOException;

import static utils.GlobalSetting.LATEST_CODE_TRANSFER_KEY;
import static utils.GlobalSetting.SUCCESS_CODE;


public class Context {
    /** 选中的代码文本 **/
    private static String code;
    private static String str_comment;
    private static String corrected_comment;
    /** 工程对象 **/
    private Project project;
    /** 文件 **/
    private Document document;
    /** 编辑器 **/
    private Editor editor;
    /** 相关数据 **/
    private int function_begin;
    private int function_end;
    private String name;
    private int comment_begin;
    private int comment_end;//function_end - 1

    /** 缩进量 */
    private int indent;

    /** 是否已有代码注释 **/
    private boolean hasCodeComment;
    private int commentType;

    /** 返回的代码注释 **/
    public Comment comment;

//    private final MessageBusConnection connection;


    private static final int DocComment = 0;
    private static final int SingleLineComment = 1;
    private static final int MultiLineComment = 2;

    public Context(Project p,Editor e){
        project = p;
        editor = e;
        document = e.getDocument();

        code = null;
        function_begin = function_end = -1;
        comment_begin = comment_end = -1;
        hasCodeComment = false;
        commentType = -1;
        comment = null;
        indent = 0;
        name = null;
//        connection = project.getMessageBus().connect();
//        myClient = null;
//        connection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER,
//                new FileEditorManagerListener() {
//                    @Override
//                    public void selectionChanged(@NotNull FileEditorManagerEvent event) {
//                        //System.out.println("selection change");
//                        try {
//                            check();
//                        } catch (IOException ex) {
//                            ex.printStackTrace();
//                        }
//                    }
//                    @Override
//                    public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file){
//                        //System.out.println("closed");
//                        try {
//                            check();
//                        } catch (IOException ex) {
//                            ex.printStackTrace();
//                        }
//                    }
//
//                });
    }



    private boolean isCommentLine(String s){
        String check_s = s.trim();
        if(check_s.startsWith("\"\"\"")){
            this.commentType = DocComment;
            return true;
        }else if(check_s.startsWith("#")){
            this.commentType = SingleLineComment;
            return true;
        }else if(check_s.startsWith("'''")){
            this.commentType = MultiLineComment;
            return true;
        }
        return false;
    }

    public void checkComment(){
        String[] text = document.getText().split("\n");
        int begin = function_begin + 1;
        for(;begin < function_end && text[begin].equals(""); begin++);
        if(!isCommentLine(text[begin])){
            hasCodeComment = false;
            return;
        }
        hasCodeComment = true;
        comment_begin = begin;
        comment_end = comment_begin;
        for(int i = comment_begin + 1; i <= function_end; i++){
            if(text[i].equals("")){
                continue;
            }
            if(commentType == 1){
                if(!text[i].contains("#")){
                    return;
                }
                comment_end = i;
            }else if(text[i].contains("'''") | text[i].contains("\"\"\"")){
                comment_end = i;
                return;
            }
        }
        return;
    }

    /*
    return comments from different models
    */
    public void transfer() throws RuntimeException, IOException {
        code = editor.getSelectionModel().getSelectedText();
        // 如果只跑前端的话，把下面注释掉
//        comment = new Comment("this is code comment",indent + 4);

        ProgressManager.getInstance().run(
                new Task.Modal(project, "Generating Comment", true) {
                    @Override
                    public void run(@NotNull ProgressIndicator indicator) {
                        indicator.setIndeterminate(true);

                        try {
                            String json = Tools.object2Json(new CommentGenerationDTO(code));
                            R r = Tools.httpPostHelper(json, "generateComment");
                            if (r != null) {
                                CommentGenerationDTO res = Tools.json2Obj(r.getData(), CommentGenerationDTO.class);
                                Tools.saveLocalProperties(LATEST_CODE_TRANSFER_KEY, res.getTaskUuid());
                                comment = new Comment(res.getComment());
                            } else {
                                SwingUtilities.invokeLater(() -> {
                                    Messages.showErrorDialog(project, "Please activate the plugin first","Error");
                                });
                            }

                        } catch (Exception e) {
                            SwingUtilities.invokeLater(() -> {
                                Messages.showErrorDialog(project, e.getMessage(),"Error");
                            });
                            throw new RuntimeException();
                        }

                        if(indicator.isCanceled()){
                            throw new RuntimeException();
                        }
                    }
                });

    }



//    private void check() throws IOException {
//        String[] s = document.getText().split("\n");
//        for(int i = 0; i < s.length; i++){
//            if(Objects.equals(s[i], "")){
//                continue;
//            }
//            if(s[i].contains(name)){
//                hasCodeComment = false;
//                function_begin = i;
//                comment_begin = comment_end = -1;
//                this.checkComment();
//                String change = document.getText(new TextRange(document.getLineStartOffset(comment_begin),document.getLineEndOffset(comment_end)));
//
//                corrected_comment = change;
//                MyClient myClient1 = new MyClient("127.0.0.1",6666);
//                try {
//                    myClient1.sendRequest("change:"+ MyToolWindowSubmit.getID()+"ehqpeui@!#!!#DQWW1"+change);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                String res = null;
//                try {
//                    res = myClient1.receive();
//                    System.out.println(res);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                myClient1.close();
//                connection.disconnect();
//            }
//        }
//    }

    public Comment getComment() {
        return comment;
    }
}
