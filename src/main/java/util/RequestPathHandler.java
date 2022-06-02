package util;

import java.io.*;

public class RequestPathHandler {
    private final String defaultPath = "./webapp";

    public boolean isExistPath(final String path) {
        File file = new File(defaultPath + path);

        return file.exists() && file.isFile();
    }

    public byte[] readData(final String path) {
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(defaultPath + path, "r")) {
            return parseFileToByteArray(randomAccessFile);
        } catch (IOException e) {
            System.out.println(e.toString());

            return new byte[] {};
        }
    }

    private byte[] parseFileToByteArray(final RandomAccessFile randomAccessFile) throws IOException {
        byte[] data = new byte[(int) randomAccessFile.length()];
        randomAccessFile.readFully(data);

        return data;
    }
}
