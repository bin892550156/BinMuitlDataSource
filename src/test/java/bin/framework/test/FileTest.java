package bin.framework.test;

import java.io.File;

public class FileTest {


    public static void main(String[] args){
        File file=new File("D:\\","a.jpg");
        System.out.println(file);
//        Runable,t.join
//        LinkedList
//        run. LinkedList.

        Thread t1=null;
        Thread t2=null;
        Thread t3=null;
        t1.start();
//        t1.join();
        t2.start();
//        t2.join();
        t3.start();
//        t3.join();

    }
}
