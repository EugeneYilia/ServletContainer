package server;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CoyoteOutputStream extends ServletOutputStream {

    OutputStream outputStream = null;

    public CoyoteOutputStream(OutputStream outputStream){
        this.outputStream = outputStream;
    }

    @Override
    public void write(int b) throws IOException {
        outputStream.write(b);
    }
}
