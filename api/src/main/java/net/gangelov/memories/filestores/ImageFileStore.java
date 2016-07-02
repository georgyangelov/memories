package net.gangelov.memories.filestores;

import net.gangelov.memories.Config;
import net.gangelov.memories.models.Image;
import net.gangelov.utils.Strings;
import net.gangelov.validation.ValidationErrors;
import net.gangelov.validation.ValidationException;
import net.gangelov.validation.Validator;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.io.IOUtils;
import org.postgresql.geometric.PGpoint;

import javax.management.RuntimeErrorException;
import javax.ws.rs.core.MediaType;
import java.awt.*;
import java.io.*;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class ImageFileStore {
    private String originalFileName;
    private InputStream fileInputStream;
    private int width, height;

    public ImageFileStore(String originalFileName, InputStream fileInputStream) {
        this.originalFileName = originalFileName;
        this.fileInputStream = fileInputStream;
    }

    private final ImageExtensionValidator validator = new ImageExtensionValidator();

    public ValidationErrors validate() {
        return validator.validate(originalFileName);
    }

    public void ensureValid() throws ValidationException {
        validator.ensureValid(originalFileName);
    }

    private static final SecureRandom random = new SecureRandom();

    public String uploadFile() throws IOException {
        String extension = Strings.getExtension(originalFileName);
        String fileName = "images/" + DigestUtils.sha1Hex(Instant.now().toString() + "-" + random.nextInt()) + "." + extension;

        File outFile = new File(Config.FILE_UPLOAD_PATH + "/" + fileName);

        if (outFile.exists()) {
            throw new RuntimeException("File name not unique. SHA1 has been broken!!!");
        }
        outFile.createNewFile();

        FileOutputStream fileOutputStream = new FileOutputStream(outFile);
        IOUtils.copy(fileInputStream, fileOutputStream);
        fileOutputStream.close();

        return fileName;
    }

    public void upload(Image image) throws IOException, ImageReadException {
        image.filePath = uploadFile();

        File imageFile = new File(Config.FILE_UPLOAD_PATH + "/" + image.filePath);

        Dimension dimensions = Imaging.getImageSize(imageFile);

        image.width = (int)dimensions.getWidth();
        image.height = (int)dimensions.getHeight();

        ImageMetadata metadata = Imaging.getMetadata(imageFile);

        if (metadata instanceof JpegImageMetadata) {
            JpegImageMetadata jpegMetadata = (JpegImageMetadata)metadata;

            TiffField takenAtField = jpegMetadata.findEXIFValueWithExactMatch(TiffTagConstants.TIFF_TAG_DATE_TIME);

            if (takenAtField != null) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");
                try {
                    Date date = format.parse(takenAtField.getStringValue());
                    image.takenAt = date.toInstant();
                } catch (ParseException e) {
                    e.printStackTrace();
                    image.takenAt = null;
                }
            }

            TiffImageMetadata exifMetadata = jpegMetadata.getExif();
            if (exifMetadata != null) {
                TiffImageMetadata.GPSInfo gpsInfo = exifMetadata.getGPS();

                if (gpsInfo != null) {
                    String gpsDescription = gpsInfo.toString();
                    double longitude = gpsInfo.getLongitudeAsDegreesEast();
                    double latitude = gpsInfo.getLatitudeAsDegreesNorth();

                    image.coordinates = new PGpoint(longitude, latitude);
                }
            }
        }
    }

    public static File get(Image image) {
        if (image.filePath == null) {
            return null;
        }

        return new File(Config.FILE_UPLOAD_PATH + "/" + image.filePath);
    }

    public static MediaType getMediaType(File image) {
        String extension = Strings.getExtension(image.getName());

        switch (extension) {
            case "jpg":
            case "jpeg":
                return new MediaType("image", "jpeg");
            case "png":
                return new MediaType("image", "png");
            default:
                throw new RuntimeException("Unknown file type");
        }
    }

    private static class ImageExtensionValidator extends Validator<String> {
        @Override
        public ValidationErrors validate(String object) {
            ValidationErrors errors = new ValidationErrors();

            if (!object.endsWith(".jpg") && !object.endsWith(".png")) {
                errors.add("file", "is not an acceptable image");
            }

            return errors;
        }
    }
}
