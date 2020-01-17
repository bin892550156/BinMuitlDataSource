package bin.framework.multi;

import java.sql.SQLException;
import java.sql.Struct;
import java.util.List;
import java.util.Map;

public class MultiStruct implements Struct {

    private List<Struct> realStructs;

    public MultiStruct(List<Struct> realStructs) {
        this.realStructs = realStructs;
    }

    @Override
    public String getSQLTypeName() throws SQLException {
        String firstResult=null;
        for(Struct struct: realStructs){
            String result=struct.getSQLTypeName();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realStructs return result is inconsistent on call  getSQLTypeName()  ");
            }
        }
        return firstResult;
    }

    @Override
    public Object[] getAttributes() throws SQLException {
        Object[] firstResult=null;
        for(Struct struct: realStructs){
            Object[] result=struct.getAttributes();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realStructs return result is inconsistent on call  getAttributes()  ");
            }
        }
        return firstResult;
    }

    @Override
    public Object[] getAttributes(Map<String, Class<?>> map) throws SQLException {
        Object[] firstResult=null;
        for(Struct struct: realStructs){
            Object[] result=struct.getAttributes(map);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realStructs return result is inconsistent on call  getAttributes(Map<String, Class<?>> map) ");
            }
        }
        return firstResult;
    }
}
