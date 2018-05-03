package sudoku.myself.xhc.com.sudoku.bean;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by xhc on 2016/7/31.
 * 用于保存数据记录到本地。以后也会做到服务器上去
 * level 是有1，2，3，4，5等5个等级
 * leveltype 有normal ，harder , screening三个模式
 * time是游戏所花的时间
 * starttime是运行开始的时间
 */

public class Record {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "nodes")
    private String nodes;//Node[][]转换成的json
    @DatabaseField(columnName = "level")
    private int level;
    @DatabaseField(columnName = "levelType")
    private int levelType;
    //已经所花的时间
    @DatabaseField(columnName = "time")
    private long time;
    //开始的时间
    @DatabaseField(columnName = "startTime")
    private long startTime ;


    public String getNodes() {
        return nodes;
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevelType() {
        return levelType;
    }

    public void setLevelType(int levelType) {
        this.levelType = levelType;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "Record{" +
                "nodes='" + nodes + '\'' +
                ", level='" + level + '\'' +
                ", levelType='" + levelType + '\'' +
                ", time=" + time +
                ", startTime=" + startTime +
                '}';
    }
}
