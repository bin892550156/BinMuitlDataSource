package bin.framework.multi;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class MultiBlob implements Blob {

    public List<Blob> realBlobs;

    public MultiBlob(List<Blob> realBlobs) {
        this.realBlobs = realBlobs;
    }

    @Override
    public long length() throws SQLException {
        Long firstResult=null;
        for(Blob blob: realBlobs){
            long result=blob.length();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realBlobs return result is inconsistent on call  length()  ");
            }
        }
        return firstResult;
    }

    @Override
    public byte[] getBytes(long pos, int length) throws SQLException {
        byte[] firstResult=null;
        for(Blob blob: realBlobs){
            byte[] result=blob.getBytes(pos,length);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realBlobs return result is inconsistent on call getBytes(long pos, int length)  ");
            }
        }
        return firstResult;
    }

    @Override
    public InputStream getBinaryStream() throws SQLException {
        List<InputStream> inputStreams=new LinkedList<>();
        for(Blob blob: realBlobs){
            inputStreams.add(blob.getBinaryStream());
        }
        return new MultiInputStream(inputStreams);
    }

    @Override
    public long position(byte[] pattern, long start) throws SQLException {
        Long firstResult=null;
        for(Blob blob: realBlobs){
            long result=blob.position(pattern,start);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realClobs return result is inconsistent on call position(byte[] pattern, long start) ");
            }
        }
        return firstResult;
    }

    @Override
    public long position(Blob pattern, long start) throws SQLException {
        Long firstResult=null;
        for(Blob blob: realBlobs){
            long result=blob.position(pattern,start);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realClobs return result is inconsistent on call position(byte[] pattern, long start) ");
            }
        }
        return firstResult;
    }

    @Override
    public int setBytes(long pos, byte[] bytes) throws SQLException {
        Integer firstResult=null;
        for(Blob blob: realBlobs){
            int result=blob.setBytes(pos,bytes);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realClobs return result is inconsistent on call setBytes(long pos, byte[] bytes) ");
            }
        }
        return firstResult;
    }

    @Override
    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
        Integer firstResult=null;
        for(Blob blob: realBlobs){
            int result=blob.setBytes(pos,bytes,offset,len);
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realClobs return result is inconsistent on call setBytes(long pos, byte[] bytes, int offset, int len) ");
            }
        }
        return firstResult;
    }

    @Override
    public OutputStream setBinaryStream(long pos) throws SQLException {
        List<OutputStream> outputStreams=new LinkedList<>();
        for(Blob blob: realBlobs){
            outputStreams.add(blob.setBinaryStream(pos));
        }
        return new MulitOutputStream(outputStreams);
    }

    @Override
    public void truncate(long len) throws SQLException {
        for(Blob blob: realBlobs){
            blob.truncate(len);
        }
    }

    @Override
    public void free() throws SQLException {
        for(Blob blob: realBlobs){
            blob.free();
        }
    }

    @Override
    public InputStream getBinaryStream(long pos, long length) throws SQLException {
        List<InputStream> inputStreams=new LinkedList<>();
        for(Blob blob: realBlobs){
            inputStreams.add(blob.getBinaryStream());
        }
        return new MultiInputStream(inputStreams);
    }
}
