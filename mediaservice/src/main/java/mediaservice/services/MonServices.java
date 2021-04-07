package mediaservice.services;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.bson.Document;

import com.mongodb.client.MongoCursor;

import mediaservice.models.DownloadInputModel;
import mediaservice.models.UploadModel;
import mediaservice.utils.ConfigPropertiesUtil;

@Path("/services")
public class MonServices {	
	@Path("/upload")
	@POST
	@Produces({MediaType.APPLICATION_JSON })
    @Consumes(MediaType.APPLICATION_JSON)
	public Response uploadFile(@Context HttpServletRequest request,UploadModel upload) {
		System.out.println(upload);
		if(upload.getContent()==null || upload.getContent().isEmpty()) {
			return Response.status(400)
					.header("Access-Control-Allow-Origin", "*")
				    .entity(new Document("msg","content phải khác null hoặc rỗng"))
				    .build();
		}
		if(upload.getFileName()==null || upload.getFileName().isEmpty()) {
			return Response.status(400)
					.header("Access-Control-Allow-Origin", "*")
				    .entity(new Document("msg","fileName phải khác null hoặc rỗng"))
				    .build();
		}
		String basePath = ConfigPropertiesUtil.getProperty("upload.path");
		String ip = request.getRemoteAddr().replace(":", "-");
		
		String uploadPath = basePath+File.separator+ip;
		
		File folderUpload = new File(uploadPath);
		String fileName = upload.getFileName();
		if(!folderUpload.exists()) {
			folderUpload.mkdir();
		} else {
			File file = new File(uploadPath+File.separator+upload.getFileName());
			
			int num = 1;
			
			while(file.exists()) {
				fileName = upload.getFileName()+"."+num++;
				file = new File(uploadPath+File.separator+fileName);
			}
		}
		
		try {
			byte[] decodedImg = Base64.decodeBase64(upload.getContent().getBytes());
			java.nio.file.Path destinationFile = Paths.get(uploadPath, fileName);
			Files.write(destinationFile, decodedImg);
			
			return Response.status(200)
					.header("Access-Control-Allow-Origin", "*")
				    .entity(new Document("msg","Upload thành công"))
				    .build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(400)
					.header("Access-Control-Allow-Origin", "*")
				    .entity(new Document("msg","Lỗi, vui lòng thử lại"))
				    .build();
		}
	}
	
	@Path("/download/{filename}")
	@GET
	@Produces({MediaType.APPLICATION_JSON })
    @Consumes(MediaType.APPLICATION_JSON)
	public Response downloadFile(@Context HttpServletRequest request,@PathParam("filename")String filename) {
		String basePath = ConfigPropertiesUtil.getProperty("upload.path");
		String ip = request.getRemoteAddr().replace(":", "-");
		
		String uploadPath = basePath+File.separator+ip;
		
		File fileDownload = new File(uploadPath+File.separator+filename);
		
		if(fileDownload.exists()) {
			 byte[] encoded;
			try {
				encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(fileDownload));
				String base64 = new String(encoded, StandardCharsets.US_ASCII);
				return Response.status(200)
						.header("Access-Control-Allow-Origin", "*")
					    .entity(new Document("content",base64))
					    .build();
			} catch (IOException e) {
				e.printStackTrace();
				return Response.status(400)
						.header("Access-Control-Allow-Origin", "*")
					    .entity(new Document("msg","Lỗi, vui lòng thử lại"))
					    .build();
			}
		} else {
			return Response.status(400)
					.header("Access-Control-Allow-Origin", "*")
				    .entity(new Document("msg","File không tồn tại, vui lòng kiểm tra lại!"))
				    .build();
		}
	}
}
