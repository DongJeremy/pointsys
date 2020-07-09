package org.pointsys.test.spring;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AppApplication {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);){
            Person person = ctx.getBean(BussinessPerson.class);
            person.service();
        }
    }

}
