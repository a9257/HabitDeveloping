package com.example.habittest;

public class Habit {
    public String hname;  //习惯名
    public String pic;     //习惯图标
    public int total_num;     //习惯每日需完成次数
    public String time; //习惯时间区间：晨间习惯、午间、晚间、任意时间
    public String fre;  //习惯频率：每日、每周、每月
    public String htext; //习惯标注
    public int finished_num;  //习惯今日已完成次数
    public int days;    //坚持天数
    public int curdays; //当前已坚持天数
    public int highdays;    //最高坚持天数
    public String credate;  //创建日期
    public int swit;    //习惯是否开启


    public Habit(String h, String p, int t, int f, String time, String fre, String htext, int d, int c, int high, String cre, int swit) {
        setHname(h);
        setPic(p);
        setTotal_num(t);
        setFinished_num(f);

        setTime(time);
        setFre(fre);
        setHtext(htext);
        setDays(d);

        setCurdays(c);
        setHighdays(high);
        setCredate(cre);
        setSwit(swit);
    }

    public int getSwit() {
        return swit;
    }

    public void setSwit(int swit) {
        this.swit = swit;
    }

    public int getCurdays() {
        return curdays;
    }

    public void setCurdays(int curdays) {
        this.curdays = curdays;
    }

    public int getHighdays() {
        return highdays;
    }

    public void setHighdays(int highdays) {
        this.highdays = highdays;
    }

    public String getCredate() {
        return credate;
    }

    public void setCredate(String credate) {
        this.credate = credate;
    }


    public String getHname() {
        return hname;
    }

    public void setHname(String hname) {
        this.hname = hname;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getTotal_num() {
        return total_num;
    }

    public void setTotal_num(int total_num) {
        this.total_num = total_num;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFre() {
        return fre;
    }

    public void setFre(String fre) {
        this.fre = fre;
    }

    public String getHtext() {
        return htext;
    }

    public void setHtext(String htext) {
        this.htext = htext;
    }

    public int getFinished_num() {
        return finished_num;
    }

    public void setFinished_num(int finished_num) {
        this.finished_num = finished_num;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}