package com.summarization.dto;

public class PluginFeedBackDTO {
    int[] scores;

    String message;

    public PluginFeedBackDTO(int[] scores, String message) {
        this.scores = scores;
        this.message = message;
    }

    public PluginFeedBackDTO(){}
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

}
