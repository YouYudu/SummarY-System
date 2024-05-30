package action;

import MyToolWindow.MyToolWindowSubmit;
import application.Context;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;

import java.io.IOException;

import static MyToolWindow.Icons.LOGO;

public class GenerateAction extends AnAction {

    private Context context;
    private static final String ToolWindowName = "SummarY";

    private static ToolWindow toolWindow;

    @Override
    public void actionPerformed(AnActionEvent e) {
        //获取当前编辑器对象
        Project project = e.getProject();
        if (project == null)
        {
            return;
        }
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        if(editor == null){
            return;
        }
        context = new Context(project,editor);

        try {
            context.transfer();
        } catch (RuntimeException re) {
            re.printStackTrace();
            return;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //侧边窗口

        if (toolWindow == null) {
            registerToolWindow(project);
        }

        toolWindow.activate(() -> updateContent(toolWindow), true);
    }


    private void updateContent(ToolWindow toolWindow) {
        MyToolWindowSubmit myToolWindowSubmit = new MyToolWindowSubmit(context);
        if (context.getComment() != null) {
            myToolWindowSubmit.setComment(context.getComment());
        }

        toolWindow.setIcon(LOGO);
        ContentManager contentManager = toolWindow.getContentManager();
        Content content = contentManager.getFactory().createContent(myToolWindowSubmit.getContent(), "", true);
        content.setDescription("Description");
        contentManager.addContent(content);

        if (contentManager.getContentCount() != 1) {
            Content toDel = contentManager.getContent(0);
            contentManager.removeContent(toDel, true);
        }

    }


    private void registerToolWindow(Project project) {
        ToolWindowManager toolWindowMgr = ToolWindowManager.getInstance(project);
        if (toolWindow == null) {
            toolWindow = toolWindowMgr.registerToolWindow(ToolWindowName, false, ToolWindowAnchor.RIGHT, true);
        }
    }
}
