package sample.chatup.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class FixedStringUtils {

    public static List<?> convertFixedLengthStringToList(String sreamString,Class<?> bean) {
        List beanList = new ArrayList<>();


        Field[] fields = bean.getDeclaredFields();


        int totalRecord = 0;
        int totField = 0;

        for(Field field:fields){
            FixedField fixedfiel = field.getAnnotation(FixedField.class);
            if(fixedfiel!=null) {
                totField++;
                totalRecord += fixedfiel.size();
            }
        }

        for(int i=0;i<sreamString.length();i+=totalRecord){
            String record = sreamString.substring(i,i+totalRecord);
            Object[] valConstr = new Object[totField];
            Class<?>[] typesContructor = new Class[totField];
            int k = 0;
            int cont = 0;
            for(Field field:fields){
                FixedField fixedann = field.getAnnotation(FixedField.class);
                if(fixedann!=null){

                    typesContructor[cont] = field.getType();
                    if(field.getType()==int.class){
                        valConstr[cont] =  Integer.valueOf(record.substring(k,k+fixedann.size()).trim());
                    }
                    else {
                        valConstr[cont] = record.substring(k,k+fixedann.size()).trim();
                    }
                    k+=fixedann.size();
                    cont++;
                }
            }

            try {
              Constructor<?>  constr = bean.getConstructor(typesContructor);
              Object instBean = constr.newInstance(valConstr);
              beanList.add(instBean);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }


        }






        return beanList;
    }
}
