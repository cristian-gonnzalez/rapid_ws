package  com.meli.backend.rapid.common;

public class CMutext {
    private static volatile CMutext instance;

    private CMutext() {
    }

    public static CMutext getInstance() {
        CMutext result = instance;
        if (result != null) {
            return result;
        }
        synchronized(CMutext.class) {
            if (instance == null) {
                instance = new CMutext();
            }
            return instance;
        }
    }
}
