package com.example.nomorekickout_teacher;

public class MyItem {
    private String title="";
    private boolean checked=false;
    private Integer id;

    public MyItem(String title, boolean checked, Integer id) {
        this.title=title;
        this.checked=checked;
        this.id=id;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getID() {
        return id;
    }
}
