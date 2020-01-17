package bin.framework.strategy;

import bin.framework.BinMetaConnection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 轮询选择策略
 */
public class RoundLoadStrategy implements LoadStrategy {

    /**
     *  groupId - 计数器
     */
    Map<String,Integer> timesMap=new HashMap<>();

    @Override
    public BinMetaConnection choose(String groupId, List<BinMetaConnection> activeBinMetaConnections) {
        Integer times = timesMap.get(groupId);
        if(times==null) times=0;
        BinMetaConnection binMetaConnection = activeBinMetaConnections.get(times % activeBinMetaConnections.size());
        timesMap.put(groupId,++times);
        return binMetaConnection;
    }
}
