package bin.framework.multi;

import bin.framework.exception.BinMultiDataSourceException;

import javax.xml.transform.Source;
import java.util.List;

public class MultiSource<T extends Source> implements Source {

    private List<Source> realSources;

    public <T extends Source> MultiSource (List<Source> realSources) {
        this.realSources = realSources;
    }

    @Override
    public void setSystemId(String systemId) {
        for (Source source :
                realSources) {
            source.setSystemId(systemId);
        }
    }

    @Override
    public String getSystemId() {
        String firstResult=null;
        for(Source source: realSources){
            String result=source.getSystemId();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new BinMultiDataSourceException(" list of realSources return result is inconsistent on call getSystemId() ");
            }
        }
        return firstResult;
    }
}
