package bin.framework.multi;

import bin.framework.exception.BinMultiDataSourceException;

import javax.xml.transform.Result;
import java.util.List;

public class MultiResult implements Result {

    private List<Result> realResults;

    public  MultiResult (List<Result> Result) {
        this.realResults = realResults;
    }

    @Override
    public void setSystemId(String systemId) {
        for (Result result :
                realResults) {
            result.setSystemId(systemId);
        }
    }

    @Override
    public String getSystemId() {
        String firstResult=null;
        for (Result resultO : realResults) {
            String result=resultO.getSystemId();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new BinMultiDataSourceException(" list of realResults return result is inconsistent on call getSystemId() ");
            }
        }
        return firstResult;
    }
}
