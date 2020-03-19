package job;

import dao.AllBuyedDao;
import entiry.AllBuyed;
import global.Contacts;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import util.DbUtil;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 * 定时任务 每隔5秒执行一次
 */
public class QutazdemoC2 implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //获取当前的时间
        long time = new Date().getTime();

        DbUtil db = new DbUtil();
        AllBuyedDao abd = new AllBuyedDao();
        try {
            Connection con = db.getCon();
            List<AllBuyed> allBuyedList = abd.getAllBuyedList(con);
            for (AllBuyed allBuyed : allBuyedList) {
                String device_info = allBuyed.getDevice_info();
                String last_time = allBuyed.getLast_time();
                Long ltime = Long.valueOf(last_time);
                long temp = time - ltime;
                if (temp > Contacts.S_7) {//如果超时了 就将制定设备终端设置为不在线
                    abd.setLastTime(con,device_info,"0");
                }
            }
            DbUtil.getClose(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
