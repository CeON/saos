package pl.edu.icm.saos.batch.admin;

import java.util.Collection;

import org.springframework.batch.admin.service.NoSuchStepExecutionException;
import org.springframework.batch.admin.service.SimpleJobService;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.launch.NoSuchJobInstanceException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.NoSuchStepException;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author Łukasz Dumiszewski
 */

public class SaosJobServiceAdapter implements SaosJobService {

    private SimpleJobService simpleJobService;
    
    public SaosJobServiceAdapter(SimpleJobService simpleJobService) {
        this.simpleJobService = simpleJobService;
        
    }

    public int hashCode() {
        return simpleJobService.hashCode();
    }

    public void setShutdownTimeout(int shutdownTimeout) {
        simpleJobService.setShutdownTimeout(shutdownTimeout);
    }

    public boolean equals(Object obj) {
        return simpleJobService.equals(obj);
    }

    public Collection<StepExecution> getStepExecutions(Long jobExecutionId)
            throws NoSuchJobExecutionException {
        return simpleJobService.getStepExecutions(jobExecutionId);
    }

    public boolean isLaunchable(String jobName) {
        return simpleJobService.isLaunchable(jobName);
    }

    public boolean isIncrementable(String jobName) {
        return simpleJobService.isIncrementable(jobName);
    }

    public JobExecution restart(Long jobExecutionId)
            throws NoSuchJobExecutionException,
            JobExecutionAlreadyRunningException, JobRestartException,
            JobInstanceAlreadyCompleteException, NoSuchJobException,
            JobParametersInvalidException {
        return simpleJobService.restart(jobExecutionId);
    }

    public JobExecution launch(String jobName, JobParameters jobParameters)
            throws NoSuchJobException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException,
            JobParametersInvalidException {
        return simpleJobService.launch(jobName, jobParameters);
    }

    public JobParameters getLastJobParameters(String jobName)
            throws NoSuchJobException {
        return simpleJobService.getLastJobParameters(jobName);
    }

    public Collection<JobExecution> listJobExecutions(int start, int count) {
        return simpleJobService.listJobExecutions(start, count);
    }

    public int countJobExecutions() {
        return simpleJobService.countJobExecutions();
    }

    public Collection<String> listJobs(int start, int count) {
        return simpleJobService.listJobs(start, count);
    }

    public int countJobs() {
        return simpleJobService.countJobs();
    }

    public String toString() {
        return simpleJobService.toString();
    }

    public int stopAll() {
        return simpleJobService.stopAll();
    }

    public JobExecution stop(Long jobExecutionId)
            throws NoSuchJobExecutionException, JobExecutionNotRunningException {
        return simpleJobService.stop(jobExecutionId);
    }

    public JobExecution abandon(Long jobExecutionId)
            throws NoSuchJobExecutionException,
            JobExecutionAlreadyRunningException {
        return simpleJobService.abandon(jobExecutionId);
    }

    public int countJobExecutionsForJob(String name) throws NoSuchJobException {
        return simpleJobService.countJobExecutionsForJob(name);
    }

    public int countJobInstances(String name) throws NoSuchJobException {
        return simpleJobService.countJobInstances(name);
    }

    public JobExecution getJobExecution(Long jobExecutionId)
            throws NoSuchJobExecutionException {
        return simpleJobService.getJobExecution(jobExecutionId);
    }

    public Collection<JobExecution> getJobExecutionsForJobInstance(String name,
            Long jobInstanceId) throws NoSuchJobException {
        return simpleJobService.getJobExecutionsForJobInstance(name,
                jobInstanceId);
    }

    public StepExecution getStepExecution(Long jobExecutionId,
            Long stepExecutionId) throws NoSuchJobExecutionException,
            NoSuchStepExecutionException {
        return simpleJobService.getStepExecution(jobExecutionId,
                stepExecutionId);
    }

    public Collection<JobExecution> listJobExecutionsForJob(String jobName,
            int start, int count) throws NoSuchJobException {
        return simpleJobService.listJobExecutionsForJob(jobName, start, count);
    }

    public Collection<StepExecution> listStepExecutionsForStep(String jobName,
            String stepName, int start, int count) throws NoSuchStepException {
        return simpleJobService.listStepExecutionsForStep(jobName, stepName,
                start, count);
    }

    public int countStepExecutionsForStep(String jobName, String stepName)
            throws NoSuchStepException {
        return simpleJobService.countStepExecutionsForStep(jobName, stepName);
    }

    public JobInstance getJobInstance(long jobInstanceId)
            throws NoSuchJobInstanceException {
        return simpleJobService.getJobInstance(jobInstanceId);
    }

    public Collection<JobInstance> listJobInstances(String jobName, int start,
            int count) throws NoSuchJobException {
        return simpleJobService.listJobInstances(jobName, start, count);
    }

    public Collection<String> getStepNamesForJob(String jobName)
            throws NoSuchJobException {
        return simpleJobService.getStepNamesForJob(jobName);
    }

    public void destroy() throws Exception {
        simpleJobService.destroy();
    }

    @Scheduled(fixedDelay = 60000)
    public void removeInactiveExecutions() {
        simpleJobService.removeInactiveExecutions();
    }

}
