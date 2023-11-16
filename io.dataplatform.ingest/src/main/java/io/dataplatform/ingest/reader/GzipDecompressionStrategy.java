package io.dataplatform.ingest.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

/**
 * GZIP을 압축 해제할 때 텍스트 데이터는 용량이 10배까지 증가할 수 있음에 주의해서 한줄씩 읽어야 한다
 */
public class GzipDecompressionStrategy implements DecompressionStrategy{
    @Override
    public BufferedReader getBufferedReader(InputStream inputStream) throws IOException {
        return new BufferedReader(new InputStreamReader(new GZIPInputStream(inputStream)));
    }
}
