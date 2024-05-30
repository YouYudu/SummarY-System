package entity;

public class CommentGenerationDTO {
    String code;

    String comment;

    String taskUuid;

    public CommentGenerationDTO(String code) {
        this.code = code;
        this.comment = null;
    }

    public CommentGenerationDTO() {}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean emptyComment() {
        return this.comment == null;
    }

    public String getTaskUuid() {
        return taskUuid;
    }

    public void setTaskUuid(String taskUuid) {
        this.taskUuid = taskUuid;
    }
}
