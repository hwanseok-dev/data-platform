package io.dataplatform.ingest.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommonDecompressionStrategy implements DecompressionStrategy{
    @Override
    public BufferedReader getBufferedReader(InputStream inputStream) throws IOException {
        return new BufferedReader(new InputStreamReader(inputStream));
    }
}
