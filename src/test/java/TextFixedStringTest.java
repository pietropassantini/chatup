import sample.chatup.utils.FixedStringUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TextFixedStringTest {

    @org.junit.jupiter.api.Test
    void test(){

        TestFixedBean b1 = new TestFixedBean(1,"ciao gente",System.currentTimeMillis()+"");
        TestFixedBean b2 = new TestFixedBean(2,"ciao a tutti",System.currentTimeMillis()+"");
        TestFixedBean b3 = new TestFixedBean(3,"che bella chat",System.currentTimeMillis()+"");
        TestFixedBean b4 = new TestFixedBean(4,"ci conosciamo ?",System.currentTimeMillis()+"");

        String total =  b1.toString()+
                        b2.toString()+
                        b3.toString()+
                        b4.toString();


        List<TestFixedBean> list = (List<TestFixedBean>) FixedStringUtils.convertFixedLengthStringToList(total,TestFixedBean.class);
        assertFalse(list.isEmpty());
        assertTrue("ciao a tutti".equals(list.get(1).getMsg()));
    }
}
