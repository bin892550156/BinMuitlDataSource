package bin.framework;

public class DataSourceProp {

    /**
     * 恢复连接定时器的执行检查的时间，默认为2分钟
     */
    private int intervalPeriodToRestoreConnectionPoolTimer=2*60*1000;

    public int getIntervalPeriodToRestoreConnectionPoolTimer() {
        return intervalPeriodToRestoreConnectionPoolTimer;
    }

    public void setIntervalPeriodToRestoreConnectionPoolTimer(int intervalPeriodToRestoreConnectionPoolTimer) {
        this.intervalPeriodToRestoreConnectionPoolTimer = intervalPeriodToRestoreConnectionPoolTimer;
    }
}
