package bin.framework.multi;

import java.sql.Clob;
import java.sql.NClob;
import java.util.List;

public class MultiNClob extends MultiClob implements NClob {

    public MultiNClob(List<Clob> realClobs) {
        super(realClobs);
    }
}
