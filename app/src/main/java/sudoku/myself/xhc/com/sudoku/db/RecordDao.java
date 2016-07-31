package sudoku.myself.xhc.com.sudoku.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

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
            e.printStackTrace();
        }
    }

    /**
     * 增加一个用户
     *
     * @param user
     */
    public void add(Record user)
    {
        try
        {
            recordDaoOpe.create(user);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public Record get(int id)
    {
        try
        {
            return recordDaoOpe.queryForId(id);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public Record getRecordFromTypeAndLevel(String levelType , int level){

        QueryBuilder builder = recordDaoOpe.queryBuilder() ;
        return null;
    }

}






















