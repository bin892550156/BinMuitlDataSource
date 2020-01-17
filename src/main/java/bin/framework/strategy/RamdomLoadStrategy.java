package bin.framework.strategy;

import bin.framework.BinMetaConnection;

import java.util.List;
import java.util.Random;

/**
 * 随机负载策略
 */
public class RamdomLoadStrategy implements LoadStrategy {

    private Random random =new Random();

    @Override
    public BinMetaConnection choose(String groupId, List<BinMetaConnection> activeBinMetaConnections) {
        int pos = random.nextInt(activeBinMetaConnections.size());
        return activeBinMetaConnections.get(pos);
    }
}
