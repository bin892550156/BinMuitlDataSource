package bin.framework.multi;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.util.LinkedList;
import java.util.List;

public class MultiSQLXML implements SQLXML {

    private List<SQLXML> realSQLXMLs;

    public MultiSQLXML(List<SQLXML> realSQLXMLs) {
        this.realSQLXMLs = realSQLXMLs;
    }

    @Override
    public void free() throws SQLException {
        for (SQLXML sqlxml:
                realSQLXMLs) {
            sqlxml.free();
        }
    }

    @Override
    public InputStream getBinaryStream() throws SQLException {
        List<InputStream> inputStreams=new LinkedList<>();
        for(SQLXML sqlxml: realSQLXMLs){
            inputStreams.add(sqlxml.getBinaryStream());
        }
        return new MultiInputStream(inputStreams);
    }

    @Override
    public OutputStream setBinaryStream() throws SQLException {
        List<OutputStream> outputStreams=new LinkedList<>();
        for(SQLXML sqlxml: realSQLXMLs){
            outputStreams.add(sqlxml.setBinaryStream());
        }
        return new MulitOutputStream(outputStreams);
    }

    @Override
    public Reader getCharacterStream() throws SQLException {
        List<Reader> readers=new LinkedList<>();
        for(SQLXML sqlxml: realSQLXMLs){
            readers.add(sqlxml.getCharacterStream());
        }
        return new MultiReader(readers);
    }

    @Override
    public Writer setCharacterStream() throws SQLException {
        List<Writer> writers=new LinkedList<>();
        for(SQLXML sqlxml: realSQLXMLs){
            writers.add(sqlxml.setCharacterStream());
        }
        return new MultiWriter(writers);
    }

    @Override
    public String getString() throws SQLException {
        String firstResult=null;
        for(SQLXML sqlxml: realSQLXMLs){
            String result=sqlxml.getString();
            if(firstResult==null) {
                firstResult=result;
                continue;
            }
            if(!firstResult.equals(result)){
                throw new SQLException(" list of realSQLXMLs return result is inconsistent on call getString() ");
            }
        }
        return firstResult;
    }

    @Override
    public void setString(String value) throws SQLException {
        for(SQLXML sqlxml: realSQLXMLs){
          sqlxml.setString(value);
        }
    }

    @Override
    public <T extends Source> T getSource(Class<T> sourceClass) throws SQLException {
        List<Source> writers=new LinkedList<>();
        for(SQLXML sqlxml: realSQLXMLs){
            writers.add(sqlxml.getSource(sourceClass));
        }
        return (T) new MultiSource(writers);
    }

    @Override
    public <T extends Result> T setResult(Class<T> resultClass) throws SQLException {
        List<Result> results=new LinkedList<>();
        for(SQLXML sqlxml: realSQLXMLs){
            results.add(sqlxml.setResult(resultClass));
        }
        return (T) new MultiResult(results);
    }
}
