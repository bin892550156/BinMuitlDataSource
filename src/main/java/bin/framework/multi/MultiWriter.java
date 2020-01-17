package bin.framework.multi;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class MultiWriter extends Writer {

    private List<Writer> realWriter;

    public MultiWriter(List<Writer> realWriter) {
        this.realWriter = realWriter;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        for (Writer writer :
                realWriter) {
            writer.write(cbuf, off, len);
        }
    }

    @Override
    public void flush() throws IOException {
        for (Writer writer :
                realWriter) {
            writer.flush();
        }
    }

    @Override
    public void close() throws IOException {
        for (Writer writer :
                realWriter) {
            writer.close();
        }
    }
}
