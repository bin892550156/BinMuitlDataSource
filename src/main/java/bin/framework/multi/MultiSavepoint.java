package bin.framework.multi;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;

public class MultiSavepoint implements Savepoint {

    public List<Savepoint> realSavepoints;

    public MultiSavepoint(List<Savepoint> realSavepoints) {
        this.realSavepoints = realSavepoints;
    }

    @Override
    public int getSavepointId() throws SQLException {
        Integer firstResult=null;
        for(Savepoint savepoint: realSavepoints){
            int result=savepoint.getSavepointId();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realSavepoints return result is inconsistent on call  getSavepointId()  ");
            }
        }
        return firstResult;
    }

    @Override
    public String getSavepointName() throws SQLException {
        String firstResult=null;
        for(Savepoint savepoint: realSavepoints){
            String result=savepoint.getSavepointName();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realSavepoints return result is inconsistent on call  getSavepointName()  ");
            }
        }
        return firstResult;
    }
}
