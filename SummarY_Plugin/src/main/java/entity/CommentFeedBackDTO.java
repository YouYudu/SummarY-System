package entity;

public class CommentFeedBackDTO {
    int[] scores;

    String message;

    String taskUuid;

    public CommentFeedBackDTO(int[] scores, String message, String taskUuid) {
        this.scores = scores;
        this.message = message;
        this.taskUuid = taskUuid;
    }

    public CommentFeedBackDTO(){}

    public int[] getScores() {
        return scores;
    }

    public void setScores(int[] scores) {
        this.scores = scores;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTaskUuid() {
        return taskUuid;
    }

    public void setTaskUuid(String taskUuid) {
        this.taskUuid = taskUuid;
    }
}
