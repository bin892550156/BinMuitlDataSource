package bin.framework.multi;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class MulitOutputStream extends OutputStream {

    private List<OutputStream> realOuptStreams;

    public MulitOutputStream(List<OutputStream> realOuptStreams) {
        this.realOuptStreams = realOuptStreams;
    }

    @Override
    public void write(int b) throws IOException {
        for (OutputStream outputStream :
                realOuptStreams) {
            outputStream.write(b);
        }
    }
}
