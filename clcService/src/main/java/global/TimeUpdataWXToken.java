package global;

import dao.TokenTicketDao;
import job.TestQutaz;
import util.DbUtil;

import java.sql.Connection;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 用于调用微信js接口的access_token
 */
public class TimeUpdataWXToken {
    private static TimeUpdataWXToken instance = null;

    // 私有化构造方法
    private TimeUpdataWXToken() {
        DbUtil db = new DbUtil();
        TokenTicketDao ttd = new TokenTicketDao();
        try {
            Connection con = db.getCon();
            ttd.updataTicket(con);

            DbUtil.getClose(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TestQutaz.main(null);
    }

    public static void init() {
        if (instance == null) {
            instance = new TimeUpdataWXToken();
        }else{
            DbUtil db = new DbUtil();
            TokenTicketDao ttd = new TokenTicketDao();
            try {
                Connection con = db.getCon();
                String ticket = ttd.getTicket(con);
                if(ticket!=null&&ticket.equals("null")){
                    ttd.updataTicket(con);
                }

                DbUtil.getClose(con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

/*
    private void useTimerImplTimedTask(){

        // 第一个参数是任务，第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间，时间单位是毫秒
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
            }
        },0L,Contacts.HOUR_24);
    }
*/

}
