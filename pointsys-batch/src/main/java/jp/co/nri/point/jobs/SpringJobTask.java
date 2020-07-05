package jp.co.nri.point.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jp.co.nri.point.annotation.JobTask;

@Component
@JobTask(value = "job1")
public class SpringJobTask {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public void task1() {
        logger.info("Do the job1...");
    }

    public void task2() {
        logger.info("Do the job2...");
    }

    public void task3() {
        logger.info("Do the job3...");
    }

}
