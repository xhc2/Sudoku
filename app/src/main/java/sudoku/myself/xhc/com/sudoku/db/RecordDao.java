package sudoku.myself.xhc.com.sudoku.db;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;

import java.util.List;

import sudoku.myself.xhc.com.sudoku.bean.Record;

/**
 * Created by xhc on 2016/7/31.
 */
public class RecordDao {
    private Context context;
    private Dao<Record, Integer> recordDaoOpe;
    private DataBaseHelper helper;

    public RecordDao(Context context)
    {
        this.context = context;

        try
        {
            helper = DataBaseHelper.getHelper(context);
            recordDaoOpe = helper.getDao(Record.class);
        } catch (Exception e)
        {
            Log.e("xhc","创建错误？-->"+e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @param record
     * 同一个leveltype ，level数据库中值保存一份
     */
    public void addOrUpdate(Record record)
    {
        try
        {
            if(record == null){
                return ;
            }

            List<Record> list = getRecordFromTypeAndLevel(record.getLevelType() , record.getLevel());

            if(list != null && list.size() > 0){
                //已经有了。
                Log.e("xhc","已经有了更新-》 "+record.toString());
                update(record);
            }
            else{
                Log.e("xhc","没有直接保存-> "+record.toString());
                recordDaoOpe.create(record);
            }

        } catch (Exception e)
        {
            Log.e("xhc","保存错误-> "+e.getMessage());
            e.printStackTrace();
        }

    }

    public void update(Record record){
        try{
            UpdateBuilder b = recordDaoOpe.updateBuilder();
            b.where().eq("levelType", record.getLevelType()).and().eq("level", record.getLevel());
            b.updateColumnValue("nodes", record.getNodes());
            b.updateColumnValue("time",record.getTime());
            b.updateColumnValue("startTime", record.getStartTime());
            b.update();
        }catch(Exception e){

        }
    }


    public List<Record> getAllRecord(){
        try{
            return recordDaoOpe.queryForAll();
        }catch(Exception e){

        }
            return null;
    }

    public List<Record> getRecordFromTypeAndLevel(int levelType , int level){
        try{
            Log.e("xhc","查询 "+levelType+" level -> "+ level);
            QueryBuilder builder = recordDaoOpe.queryBuilder() ;
            Where<Record , Integer> where = builder.where();
            where.eq("levelType",levelType).and();
            where.eq("level", level);
            return where.query();
        }catch(Exception e){
            Log.e("xhc",e.getMessage());
        }
        return null;
    }

}






















