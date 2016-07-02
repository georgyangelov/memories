package net.gangelov.memories.rest;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.spi.container.ResourceFilters;
import net.gangelov.memories.models.Image;
import net.gangelov.memories.models.User;
import net.gangelov.memories.filestores.ImageFileStore;
import net.gangelov.memories.rest.filters.AuthenticationFilter;
import net.gangelov.memories.rest.responses.ApiError;
import net.gangelov.validation.ValidationException;
import org.apache.commons.imaging.ImageReadException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

@Path("/images")
public class Images {
    @GET
    public List<Image> index() throws SQLException {
        return Image.query().order("created_at", "desc").include("user").results();
    }

    @GET
    @Path("/{id}/image")
    public Response getImage(@PathParam("id") int imageId) throws SQLException {
        Image image = Image.query().where("id", imageId).first();

        if (image == null) {
            throw new ApiError(404, "image_not_found", "No image with this id");
        }

        File imageFile = ImageFileStore.get(image);

        return Response.ok(imageFile, ImageFileStore.getMediaType(imageFile))
                .header("Cache-Control", "public, max-age=31536000")
                .build();
    }

    @PUT
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @ResourceFilters({AuthenticationFilter.class})
    public Image create(
            @HeaderParam("Authorization") String accessToken,
            @FormDataParam("name") String name,
            @FormDataParam("image") InputStream fileInputStream,
            @FormDataParam("image") FormDataContentDisposition contentDisposition
    ) throws SQLException, ValidationException, IOException, ImageReadException {
        User user = User.findByAccessToken(accessToken);
        Image image = new Image();
        image.userId = user.id;
        image.name = name;

        ImageFileStore store = new ImageFileStore(contentDisposition.getFileName(), fileInputStream);
        store.ensureValid();
        store.upload(image);

        image.ensureValid();
        image.save();

        return image;
    }

    @DELETE
    @Path("/{id}")
    @ResourceFilters({AuthenticationFilter.class})
    public void delete(@HeaderParam("Authorization") String accessToken, @PathParam("id") int id) throws SQLException {
        User user = User.findByAccessToken(accessToken);
        Image image = Image.query().where("id", id).first();

        if (image == null) {
            throw new ApiError(404, "image_not_found", "No image with this id");
        }

        if (!user.id.equals(image.userId)) {
            throw new ApiError(401, "no_permission", "You cannot remove someone else's image");
        }

        Image.query().delete().where("id", id).execute();
    }
}
