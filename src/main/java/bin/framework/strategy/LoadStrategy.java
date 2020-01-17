package bin.framework.strategy;

import bin.framework.BinMetaConnection;

import java.util.List;

public interface LoadStrategy {

     BinMetaConnection choose(String gropId, List<BinMetaConnection> binMetaConnectionList);
}
