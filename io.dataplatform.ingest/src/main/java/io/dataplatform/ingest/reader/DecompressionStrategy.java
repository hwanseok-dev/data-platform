package io.dataplatform.ingest.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

public interface DecompressionStrategy {
    BufferedReader getBufferedReader(InputStream inputStream) throws IOException;
}
