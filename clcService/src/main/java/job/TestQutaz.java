package job;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.text.ParseException;

public class TestQutaz {
    //private static final SimpleTrigger CronTrigger = null; // 触发器对象
    //需要执行的方法
    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
        //实例化scherduler 工厂
        SchedulerFactory schedulerFactory=new StdSchedulerFactory();
        Scheduler scheduler;
        try {
            scheduler=schedulerFactory.getScheduler();//创建scheduler
            //创建jobDetail 1.job分组，2.job名，3.job执行类
            JobDetail jobDetail =
                    new JobDetail("jobDetail-s1", "jobDetailGroup-s1", QutazdemoC.class);
            //实例化触发器
            CronTrigger trigger = new CronTrigger("triggerGroup", "triggerGroup-s1");// 触发器名,触发器组
            trigger.setCronExpression("0 0 * ? * * *");// 触发器时间设定 每小时的0分0秒触发一次
            scheduler.scheduleJob(jobDetail, trigger); //添加一个job
            scheduler.start();//开启一个job
        } catch (SchedulerException | ParseException e) {
            e.printStackTrace();
        }
    }

    //添加运行一个job
    public static  void addJob(String jobName, String jobClass, String time){
        //实例化scherduler 工厂
        SchedulerFactory schedulerFactory=new StdSchedulerFactory();
        Scheduler scheduler;
        try {
            scheduler=schedulerFactory.getScheduler();//创建scheduler
            //创建jobDetail 1.job分组，2.job名，3.job执行类
            JobDetail jobDetail =
                    new JobDetail("jobDetail-s1", jobName, Class.forName(jobClass));
            //实例化触发器
            CronTrigger trigger = new CronTrigger("triggerGroup", "triggerGroup-s1");// 触发器名,触发器组
            trigger.setCronExpression(time);// 触发器时间设定
            scheduler.scheduleJob(jobDetail, trigger); //添加一个job
            scheduler.start();//开启一个job
        } catch (SchedulerException | ParseException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //修改时间
    public static void modifyJobTime(String jobName, String time) {
        try {
            SchedulerFactory schedulerFactory=new StdSchedulerFactory();
            Scheduler sched = schedulerFactory.getScheduler();
            CronTrigger trigger = (CronTrigger) sched.getTrigger("jobDetail-s1", "triggerGroup");
            if(trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time)) {
                JobDetail jobDetail = sched.getJobDetail("jobDetail-s1", "triggerGroup");
                Class objJobClass = jobDetail.getJobClass();
                String jobClass = objJobClass.getName();
                removeJob(jobName);

                addJob(jobName, jobClass, time);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    //移除一个job
    public static void removeJob(String jobName) {
        try {
            SchedulerFactory schedulerFactory=new StdSchedulerFactory();
            Scheduler sched = schedulerFactory.getScheduler();
            sched.pauseTrigger(jobName, "triggerGroup");// 停止触发器
            sched.unscheduleJob(jobName, "triggerGroup");// 移除触发器
            sched.deleteJob(jobName, "jobDetail-s1");// 删除任务
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
