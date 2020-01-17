package bin.framework.multi;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MultiInputStream extends InputStream {

    public List<InputStream> realInputStreams;

    public MultiInputStream(List<InputStream> realInputStreams) {
        this.realInputStreams = realInputStreams;
    }

    @Override
    public int read() throws IOException {
        Integer firstResult=null;
        for(InputStream inputStream: realInputStreams){
            int result=inputStream.read();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new IOException(" list of realInputStreams return result is inconsistent on call  read()  ");
            }
        }
        return firstResult;
    }
}
