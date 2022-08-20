package com.dds.reggie;

import com.dds.reggie.entity.R;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class test {

    @Test
    public void test1(){

        String str = null;
        String str1 = "dds" + str;
        System.out.println(str1.equals("ddsnull"));
        System.out.println(str1);




    }
}
