/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Until;
import dal.NotificationDAO;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
/**
 *
 * @author THIS PC
 */
public class AutoNotification implements Job{
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        NotificationDAO dao = new NotificationDAO();
        dao.checkExpiringUserPackagesAndNotify();
        System.out.println("AutoNotifyJob ran at " + new java.util.Date());
    }
}
