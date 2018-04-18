package com.ab.worldcup.web.components;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Stream;

@RunWith(SpringRunner.class)
public class EncryptionUtilTest {

    @Test
    public void testEncryption() {
        Stream.of(1, 2, 3, 4, 5, 20, 21, 22, 33, 44, 55).forEach(n -> {

            Long id = Long.valueOf(n);

            String s = EncryptionUtil.encodeId(id);
            System.out.println("number: " + id + " encode to: " + s);
            Long aLong = EncryptionUtil.decodeId(s);
            Assert.assertEquals(id, aLong);

        });
    }

}