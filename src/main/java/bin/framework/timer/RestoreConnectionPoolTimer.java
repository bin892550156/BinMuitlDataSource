package bin.framework.timer;

import bin.framework.BinMetaConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 恢复
 */
public class RestoreConnectionPoolTimer extends Thread{

    private List<BinMetaConnection> deprecatedBinMetaConnectionList =new ArrayList<>();

    private Object lock=new Object();

    /**
     * 每次执行的间隔时间：2 分钟
     */
    private long intervalPeriod=2*60*1000;

    public RestoreConnectionPoolTimer(long intervalPeriod) {
        this.intervalPeriod = intervalPeriod;
    }

    @Override
    public void run() {
        try{
            while (true){
                if(deprecatedBinMetaConnectionList.isEmpty()){
                    synchronized (lock){
                        lock.wait();
                    }
                }
                for (BinMetaConnection binMetaConnection :
                        deprecatedBinMetaConnectionList) {
                    try {
                        Connection connection = binMetaConnection.getConnection();
                        if(connection!=null){
                            binMetaConnection.setConnectionDeprecated(false);
                            deprecatedBinMetaConnectionList.remove(connection);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                Thread.sleep(intervalPeriod);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addMetaConnectionInfo(BinMetaConnection binMetaConnection){
        deprecatedBinMetaConnectionList.add(binMetaConnection);
        synchronized (lock){
            lock.notify();
        }
    }

    public long getIntervalPeriod() {
        return intervalPeriod;
    }

    public void setIntervalPeriod(long intervalPeriod) {
        this.intervalPeriod = intervalPeriod;
    }
}
