package ru.geekbrains.client.data;

public class UserFile {
    private String filename;
    private String size;
    private String date;

    public UserFile(String filename, String size, String date) {
        this.filename = filename;
        this.size = size;
        this.date = date;
    }

    public String getFileName() {
        return filename;
    }

    public void setFileName(String filename) {
        this.filename = filename;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
