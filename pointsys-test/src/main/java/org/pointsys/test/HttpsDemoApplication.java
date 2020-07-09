package org.pointsys.test;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@Controller
public class HttpsDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(HttpsDemoApplication.class, args);
    }

    @RequestMapping(value = "/getBook", produces = { "application/xml; charset=UTF-8" })
    @ResponseBody
    public Book abc(HttpServletRequest request) {
        //String accept = request.getHeader("accept");
        Book book = new Book(1, "spring-framework", "12345");
        return book;
    }

    @RequestMapping(value = "/json", produces = { "application/json; charset=UTF-8" })
    @ResponseBody
    public Book json() {
        Book book = new Book(1, "spring-framework", "12345");
        return book;
    }

}
