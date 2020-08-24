package com.example;

import com.example.idbsystem.IdbsystemApplication;
import com.example.idbsystem.service.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(value = SpringJUnit4ClassRunner.class)
@SpringBootTest(classes={IdbsystemApplication.class})
public class serviceTest {

    @Autowired
    private UserServiceImpl us;

    @Test
    public void testAdd() {

        System.out.println("----------开始测试----------------");
        String str = us.selectByName("huahua");
        System.out.println(str);

        System.out.println("---------------------------------");
        str = us.selectByName("huahua");
        System.out.println(str);

    }

    @Test
    public void testThreadStart() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("线程run");
            }
        };

        Thread t = new Thread(runnable);

        t.start();

    }
}