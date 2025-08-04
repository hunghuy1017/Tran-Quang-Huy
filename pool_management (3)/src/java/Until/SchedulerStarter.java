/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Until;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class SchedulerStarter {
    public static void start() throws SchedulerException {
        JobDetail job = JobBuilder.newJob(AutoNotification.class)
                .withIdentity("notifyJob", "group1").build();

        // Mỗi ngày lúc 00:00 (nửa đêm)
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("dailyTrigger", "group1")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(23,50 )) // 00:00 AM mỗi ngày
                .build();

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
    }
}
