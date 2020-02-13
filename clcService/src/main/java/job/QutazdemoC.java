package job;

import dao.TokenTicketDao;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import util.DbUtil;

import java.sql.Connection;

public class QutazdemoC implements Job {
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("----------------------------------------更新了一次token_ticket数据库");
        DbUtil db = new DbUtil();
        TokenTicketDao ttd = new TokenTicketDao();
        try {
            Connection con = db.getCon();
            ttd.updataTicket(con);

            DbUtil.getClose(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
