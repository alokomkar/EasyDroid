package com.sortedqueue.learnandroid.topic;

/**
 * Created by Alok on 07/06/17.
 */

public class SlideContent {

    private String content;
    private int contentType;

    public SlideContent(String content, int contentType) {
        this.content = content;
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "SlideContent{" +
                "content='" + content + '\'' +
                ", contentType=" + contentType +
                '}';
    }
}
