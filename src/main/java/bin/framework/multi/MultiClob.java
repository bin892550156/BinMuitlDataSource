package bin.framework.multi;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class MultiClob implements Clob {

    public List<Clob> realClobs;

    public MultiClob(List<Clob> realClobs) {
        this.realClobs = realClobs;
    }

    @Override
    public long length() throws SQLException {
        Long firstResult=null;
        for(Clob clob: realClobs){
            long result=clob.length();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realClobs return result is inconsistent on call  length()  ");
            }
        }
        return firstResult;
    }

    @Override
    public String getSubString(long pos, int length) throws SQLException {
        String firstResult=null;
        for(Clob clob: realClobs){
            String result=clob.getSubString(pos,length);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realClobs return result is inconsistent on call getSubString(long pos, int length)  ");
            }
        }
        return firstResult;
    }

    @Override
    public Reader getCharacterStream() throws SQLException {
        List<Reader> readers=new LinkedList<>();
        for(Clob clob: realClobs){
            readers.add(clob.getCharacterStream());
        }
        return new MultiReader(readers);
    }

    @Override
    public InputStream getAsciiStream() throws SQLException {
        List<InputStream> inputStreams=new LinkedList<>();
        for(Clob clob: realClobs){
            inputStreams.add(clob.getAsciiStream());
        }
        return new MultiInputStream(inputStreams);
    }

    @Override
    public long position(String searchstr, long start) throws SQLException {
        Long firstResult=null;
        for(Clob clob: realClobs){
            long result=clob.position(searchstr,start);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realClobs return result is inconsistent on call position(String searchstr, long start) ");
            }
        }
        return firstResult;
    }

    @Override
    public long position(Clob searchstr, long start) throws SQLException {
        Long firstResult=null;
        for(Clob clob: realClobs){
            long result=clob.position(searchstr,start);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realClobs return result is inconsistent on call position(Clob searchstr, long start) ");
            }
        }
        return firstResult;
    }

    @Override
    public int setString(long pos, String str) throws SQLException {
        Integer firstResult=null;
        for(Clob clob: realClobs){
            int result=clob.setString(pos,str);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realClobs return result is inconsistent on call setString(long pos, String str) ");
            }
        }
        return firstResult;
    }

    @Override
    public int setString(long pos, String str, int offset, int len) throws SQLException {
        Integer firstResult=null;
        for(Clob clob: realClobs){
            int result=clob.setString(pos,str,offset,len);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realClobs return result is inconsistent on call setString(long pos, String str, int offset, int len) ");
            }
        }
        return firstResult;
    }

    @Override
    public OutputStream setAsciiStream(long pos) throws SQLException {
        List<OutputStream> outputStreams=new LinkedList<>();
        for(Clob clob: realClobs){
            outputStreams.add(clob.setAsciiStream(pos));
        }
        return new MulitOutputStream(outputStreams);
    }

    @Override
    public Writer setCharacterStream(long pos) throws SQLException {
        List<Writer> writers=new LinkedList<>();
        for(Clob clob: realClobs){
            writers.add(clob.setCharacterStream(pos));
        }
        return new MultiWriter(writers);
    }

    @Override
    public void truncate(long len) throws SQLException {
        for(Clob clob: realClobs){
            clob.truncate(len);
        }
    }

    @Override
    public void free() throws SQLException {
        for(Clob clob: realClobs){
            clob.free();
        }
    }

    @Override
    public Reader getCharacterStream(long pos, long length) throws SQLException {
        List<Reader> readers=new LinkedList<>();
        for(Clob clob: realClobs){
            readers.add(clob.getCharacterStream(pos,length));
        }
        return new MultiReader(readers);
    }
}
