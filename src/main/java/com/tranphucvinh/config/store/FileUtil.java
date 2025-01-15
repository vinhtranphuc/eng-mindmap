package com.tranphucvinh.config.store;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    private final static String IMAGE_TYPE = "image/";
    private final static String AUDIO_TYPE = "audio/";
    private final static String VIDEO_TYPE = "video/";
    private final static String APPLICATION_TYPE = "application/";
    private final static String TXT_TYPE = "text/";
    private final static String APPLICATION_DEFAULT = "octet-stream";

    /**
     * check and create content type
     * @param multipartFile
     * @return
     */
    public static String getContentType(MultipartFile multipartFile) {

        String extType = "";

        extType = getExtensionByStringHandling(multipartFile.getOriginalFilename()).get();
        LOGGER.info("FileUtil | getFileType | type : " + extType);

        if (isImage(extType)) {

            LOGGER.info("FileUtil | getFileType | IMAGE_TYPE+type : " + IMAGE_TYPE+extType);
            return IMAGE_TYPE+extType;
        }
        if (extType.equalsIgnoreCase("mp3") || extType.equalsIgnoreCase("OGG")
                || extType.equalsIgnoreCase("WAV") || extType.equalsIgnoreCase("REAL")
                || extType.equalsIgnoreCase("APE") || extType.equalsIgnoreCase("MODULE")
                || extType.equalsIgnoreCase("MIDI") || extType.equalsIgnoreCase("VQF")
                || extType.equalsIgnoreCase("CD")) {

            LOGGER.info("FileUtil | getFileType | AUDIO_TYPE+type : " + AUDIO_TYPE+extType);
            return AUDIO_TYPE+extType;
        }
        if (extType.equalsIgnoreCase("mp4") || extType.equalsIgnoreCase("avi")
                || extType.equalsIgnoreCase("MPEG-1") || extType.equalsIgnoreCase("RM")
                || extType.equalsIgnoreCase("ASF") || extType.equalsIgnoreCase("WMV")
                || extType.equalsIgnoreCase("qlv") || extType.equalsIgnoreCase("MPEG-2")
                || extType.equalsIgnoreCase("MPEG4") || extType.equalsIgnoreCase("mov")
                || extType.equalsIgnoreCase("3gp")) {

            LOGGER.info("FileUtil | getFileType | VIDEO_TYPE+type : " + VIDEO_TYPE+extType);
            return VIDEO_TYPE+extType;
        }
        if (extType.equalsIgnoreCase("doc") || extType.equalsIgnoreCase("docx")
                || extType.equalsIgnoreCase("ppt") || extType.equalsIgnoreCase("pptx")
                || extType.equalsIgnoreCase("xls") || extType.equalsIgnoreCase("xlsx")
                || extType.equalsIgnoreCase("zip")||extType.equalsIgnoreCase("jar")) {

            LOGGER.info("FileUtil | getFileType | APPLICATION_TYPE+type : " + APPLICATION_TYPE+extType);
            return APPLICATION_TYPE+extType;
        }
        if (extType.equalsIgnoreCase("txt")) {

            LOGGER.info("FileUtil | getFileType | TXT_TYPE+type : " + TXT_TYPE+extType);
            return TXT_TYPE+extType;
        }

        LOGGER.info("FileUtil | getFileType | APPLICATION_DEFAULT : " + APPLICATION_TYPE+APPLICATION_DEFAULT);
        return APPLICATION_TYPE+APPLICATION_DEFAULT;
    }

    /**
     * get extension from filename
     * @param filename
     * @return
     */
    public static Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
          .filter(f -> f.contains("."))
          .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    /**
     * check extension is image
     * @param extType
     * @return
     */
    public static boolean isImage(String extType) {
        return (extType.equalsIgnoreCase("JPG") || extType.equalsIgnoreCase("JPEG")
                || extType.equalsIgnoreCase("GIF") || extType.equalsIgnoreCase("PNG")
                || extType.equalsIgnoreCase("BMP") || extType.equalsIgnoreCase("PCX")
                || extType.equalsIgnoreCase("TGA") || extType.equalsIgnoreCase("PSD")
                || extType.equalsIgnoreCase("TIFF"));
    }

    /**
     * get image dimension
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public static String getImageDimension(MultipartFile multipartFile) throws IOException{
        try {
            BufferedImage image = ImageIO.read(multipartFile.getInputStream());
            if(Objects.isNull(image)) {
                LOGGER.info("FileUtil | getImageDimension : Cannot detect BufferedImage from " + multipartFile.getName());
                return null;
            }
            int width = image.getWidth();
            int height = image.getHeight();
            return width + "x" + height;
        } catch (Exception e) {
            LOGGER.error("Cannot get image dimension from file "+e);
        }
        return null;
    }
}
