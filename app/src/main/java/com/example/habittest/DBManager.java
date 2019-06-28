package com.example.habittest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

public class DBManager {
    private static MySqliteHelper helper;
    private SQLiteDatabase db;

    public static MySqliteHelper getIntance(Context context) {
        if (helper == null) {
            helper = new MySqliteHelper(context);
        }
        return helper;
    }

    public DBManager(SQLiteDatabase db) {
        this.db = db;
    }


    //数据库创建函数
    public void createTableOrNot() {
        boolean notable = true;
        int count = -1;
        //先判断表是否存在
        String sql = "select count(*) as c from sqlite_master where type ='table' and name ='habits' ";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            count = cursor.getInt(0);
            if (count > 0) {
                notable = false;
            }
        }
        if (notable) {//不存在则创建
            String sql1 = "create table habits (hname text primary key,pic text, total_num integer,finished_num integer,time text, fre text, htext text,days integer, curdays integer, highdays integer, credate text , swit integer)";
            String sql2 = "create table daka (hname text,dakadate date)";
            db.execSQL(sql1);
            db.execSQL(sql2);
            this.insertTestRecord();
        }
        //调试用Log.i("tag",Integer.toString(count));
        //调试用Log.i("tag",Boolean.toString(notable));
    }

    public void insertTestRecord() {
        String sql1 = "insert into habits values ('测试习惯1','habit_1',2,0,'晨间习惯','每天','只有千锤百炼，才能成为好钢。',5,0,3,'20190526',1)";
        String sql2 = "insert into habits values ('测试习惯2','habit_2',3,0,'午间习惯','每天','只有千锤百炼，才能成为好钢。',3,0,2,'20190515',1)";
        String sql3 = "insert into habits values ('测试习惯3','habit_3',1,0,'晚间习惯','每天','只有千锤百炼，才能成为好钢。',4,0,2,'20190522',1)";
        String sql4 = "insert into habits values ('测试习惯4','habit_4',1,0,'任意时间','每天','只有千锤百炼，才能成为好钢。',6,0,3,'20190531',1)";

        String sql5 = "insert into daka values ('测试习惯1','20190601'),('测试习惯1','20190602'),('测试习惯1','20190603'),('测试习惯1','20190610'),('测试习惯1','20190611')";
        String sql6 = "insert into daka values ('测试习惯2','20190603'),('测试习惯2','20190610'),('测试习惯2','20190611')";
        String sql7 = "insert into daka values ('测试习惯3','20190603'),('测试习惯3','20190604'),('测试习惯3','20190610'),('测试习惯3','20190611')";
        String sql8 = "insert into daka values ('测试习惯4','20190601'),('测试习惯4','20190602'),('测试习惯4','20190603'),('测试习惯4','20190609'),('测试习惯4','20190610'),('测试习惯4','20190611')";

        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);
        db.execSQL(sql4);
        db.execSQL(sql5);
        db.execSQL(sql6);
        db.execSQL(sql7);
        db.execSQL(sql8);
    }

    public void clockinUpdateDB(String h) {
        //点击签到键更新数据库

        //非必做操作 筛选更新
        String sql3 = "update habits set days = days+1 where hname='" + h + "' and finished_num=0";
        String sql4 = "update habits set curdays = curdays+1 where hname='" + h + "' and finished_num=0 and curdays=0";
        String sql5 = "update habits set highdays = highdays+1 where hname='" + h + "' and finished_num=0 and highdays=0";
        db.execSQL(sql3);
        db.execSQL(sql4);
        db.execSQL(sql5);
        //必做操作：finished_num++ 、插入打卡记录
        Date date = new Date();     ///获取当前日期
        String date_s = Utils.date2String(date);
        String sql1 = "update habits set finished_num = finished_num+1 where hname ='" + h + "'";
        String sql2 = "insert into daka values ('" + h + "','" + date_s + "')";
        db.execSQL(sql1);
        db.execSQL(sql2);
    }

    //返回给定时间段下的习惯
    //isNotFinished为1时返回未结束的,为0返回已结束的
    public Habit[] getHabit(String time, int isNotFinished) {
        String[] Time = new String[]{time};
        if (time == "任意时间") {//选中的是任意时间habits，返回全部habits
            String sql = "select count(*) from habits where swit=" + isNotFinished;
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToNext();
            int count = cursor.getInt(0);//获取习惯总数
            Habit[] h = new Habit[count];//创建habit数组
            String sq2 = "select * from habits where swit=" + isNotFinished;
            Cursor c = db.rawQuery(sq2, null);
            c.moveToFirst();
            int i = 0;
            while (!c.isAfterLast()) {
                Habit h1 = new Habit(c.getString(0), c.getString(1), c.getInt(2), c.getInt(3), c.getString(4), c.getString(5), c.getString(6), c.getInt(7), c.getInt(8), c.getInt(9), c.getString(10), c.getInt(11));
                h[i++] = h1;
                c.moveToNext();
            }
            ///应当有count == h.length;
            return h;
        } else {//其它情况相似
            String sql1 = "select count(*) from habits where time=? and swit =" + isNotFinished;
            Cursor cursor = db.rawQuery(sql1, Time);
            cursor.moveToNext();
            int count = cursor.getInt(0);//获取习惯总数
            Habit[] h = new Habit[count];//创建habit数组
            String sql2 = "select * from habits where time=? and swit =" + isNotFinished;
            Cursor c = db.rawQuery(sql2, Time);
            c.moveToFirst();
            int i = 0;
            while (!c.isAfterLast()) {
                Habit h1 = new Habit(c.getString(0), c.getString(1), c.getInt(2), c.getInt(3), c.getString(4), c.getString(5), c.getString(6), c.getInt(7), c.getInt(8), c.getInt(9), c.getString(10), c.getInt(11));
                h[i++] = h1;
                c.moveToNext();
            }
            ///应当有count == h.length ==i;实际h数组的下标应该为0至i-1
            //调试用Log.i("tag##",Integer.toString(i));
            return h;
        }
    }

    //用于在添加新的习惯时更新数据库，成功返回ture，失败返回false（表示该习惯已经存在）。
    public boolean insertHabitDB(Habit habit) {

        //第一步先看要添加的habit名称是否已经存在

        String sql1 = "select count(*) from habits where hname = '" + habit.getHname() + "'";    //sql语句查询是否存在该名字
        Cursor cursor = db.rawQuery(sql1, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        if (count == 1)      //若已经存在这个名字的习惯则直接返回false
            return false;
        //否则创建这个习惯。
        String sql2 = "insert into habits values('" + habit.getHname() + "','" + habit.getPic() + "'," + habit.getTotal_num() + "," + habit.getFinished_num() + ",'" + habit.getTime() + "','" + habit.getFre() + "','" + habit.getHtext() + "'," + habit.getDays() + "," + habit.getCurdays() + "," + habit.getHighdays() + ",'" + habit.getCredate() + "'," + habit.getSwit() + ")";
        db.execSQL(sql2);
        return true;    ///添加则返回true
    }

    public void switchHabit(String hname,int swit) {
        String sql = "update habits set swit = "+swit+" where hname='" + hname + "'";
        db.execSQL(sql);
    }

    //获取查看的习惯已经打卡的日期,返回存放已打卡日期的int数组
    public int[] getDates(String hname) {
        int[] dates;//存放结果的int数组
        int count;//数组长度 通过查询记录获得
        String sql1 = "select count(*) from daka where hname = '" + hname + "'";
        Cursor cursor = db.rawQuery(sql1, null);
        cursor.moveToFirst();
        count = cursor.getInt(0);
        dates = new int[count];  ///获取该习惯打卡总数后，实例化int数组，准备存数据

        String sql2 = "select dakadate from daka where hname = '" + hname + "'";
        Cursor c = db.rawQuery(sql2, null);
        c.moveToFirst();
        int i = 0;
        while (!c.isAfterLast()) {//获取日期
            String s1 = c.getString(0);
            dates[i++] = Integer.parseInt(s1.substring(6, 8));
            c.moveToNext();
        }
        //while过后，应当有count==i==h.length，且s[]下标范围0至i-1

        return dates;
    }
}
