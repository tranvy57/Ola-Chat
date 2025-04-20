package vn.edu.iuh.fit.olachatbackend.utils;

import java.util.HashMap;
import java.util.Map;

public class FileUtils {
    private static final Map<String, String> MIME_TYPE_TO_EXTENSION = new HashMap<>();
    
    static {
        // Documents
        MIME_TYPE_TO_EXTENSION.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx");
        MIME_TYPE_TO_EXTENSION.put("application/msword", "doc");
        MIME_TYPE_TO_EXTENSION.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx");
        MIME_TYPE_TO_EXTENSION.put("application/vnd.ms-excel", "xls");
        MIME_TYPE_TO_EXTENSION.put("application/vnd.openxmlformats-officedocument.presentationml.presentation", "pptx");
        MIME_TYPE_TO_EXTENSION.put("application/vnd.ms-powerpoint", "ppt");
        MIME_TYPE_TO_EXTENSION.put("application/pdf", "pdf");
        MIME_TYPE_TO_EXTENSION.put("text/plain", "txt");
        MIME_TYPE_TO_EXTENSION.put("text/csv", "csv");
        
        // Images
        MIME_TYPE_TO_EXTENSION.put("image/jpeg", "jpg");
        MIME_TYPE_TO_EXTENSION.put("image/png", "png");
        MIME_TYPE_TO_EXTENSION.put("image/gif", "gif");
        MIME_TYPE_TO_EXTENSION.put("image/svg+xml", "svg");
        MIME_TYPE_TO_EXTENSION.put("image/webp", "webp");
        
        // Videos
        MIME_TYPE_TO_EXTENSION.put("video/mp4", "mp4");
        MIME_TYPE_TO_EXTENSION.put("video/webm", "webm");
        MIME_TYPE_TO_EXTENSION.put("video/ogg", "ogv");
        
        // Audio
        MIME_TYPE_TO_EXTENSION.put("audio/mpeg", "mp3");
        MIME_TYPE_TO_EXTENSION.put("audio/ogg", "ogg");
        MIME_TYPE_TO_EXTENSION.put("audio/wav", "wav");
    }
    
    /**
     * Get file extension from MIME type
     * @param mimeType the MIME type
     * @return the corresponding file extension or a default extension
     */
    public static String getExtensionFromMimeType(String mimeType) {
        if (mimeType == null) {
            return "bin";
        }
        
        String extension = MIME_TYPE_TO_EXTENSION.get(mimeType.toLowerCase());
        if (extension != null) {
            return extension;
        }
        
        // Fallback: try to extract extension from MIME type
        int slashIndex = mimeType.lastIndexOf('/');
        if (slashIndex != -1 && slashIndex < mimeType.length() - 1) {
            return mimeType.substring(slashIndex + 1);
        }
        
        return "bin"; // Default binary extension
    }
}
