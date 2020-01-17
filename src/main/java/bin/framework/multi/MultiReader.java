package bin.framework.multi;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class MultiReader extends Reader {

    public List<Reader> realReaders;

    public MultiReader(List<Reader> realReaders) {
        this.realReaders=realReaders;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        Integer firstResult=null;
        for(Reader reader: realReaders){
            int result=reader.read(cbuf,off,len);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new IOException(" list of realReaders return result is inconsistent on call  read(char[] cbuf, int off, int len)  ");
            }
        }
        return firstResult;
    }

    @Override
    public void close() throws IOException {
        for (Reader reader:realReaders){
            reader.close();
        }
    }
}
