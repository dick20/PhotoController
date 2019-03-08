package upload;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

@WebServlet("/uploadimage")
public class UploadImageServlet extends HttpServlet {
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		����ͼƬ
//		uploadImage(request, response);
//		����ͼƬ���û�Id
		changeUserImage(request, response);
	}

	// �ϴ�ͼƬ�ļ�
	private void uploadImage(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String message = "";
		try{
			DiskFileItemFactory dff = new DiskFileItemFactory();
			ServletFileUpload sfu = new ServletFileUpload(dff);
			List<FileItem> items = sfu.parseRequest(request);
			// ��ȡ�ϴ��ֶ�
			FileItem fileItem = items.get(0);
			// �����ļ���ΪΨһ��
			String filename = fileItem.getName();
			if (filename != null) {
				filename = IdGenertor.generateGUID() + "." + FilenameUtils.getExtension(filename);
			}
			// ���ɴ洢·��
			String storeDirectory = getServletContext().getRealPath("/files/images");
			File file = new File(storeDirectory);
			if (!file.exists()) {
				file.mkdir();
			}
			String path = genericPath(filename, storeDirectory);
			// �����ļ����ϴ�
			try {
				fileItem.write(new File(storeDirectory + path, filename));
				
				String filePath = "/files/images" + path + "/" + filename;
				message = filePath;
			} catch (Exception e) {
				message = "�ϴ�ͼƬʧ��";
			}
		} catch (Exception e) {
			message = "�ϴ�ͼƬʧ��";
		} finally {
			System.out.println(message);
			response.getWriter().write(message);
		}
	}
	
	// �޸��û���ͼƬ
		private void changeUserImage(HttpServletRequest request, HttpServletResponse response) 
				throws ServletException, IOException {
			String message = "";
			String fieldName = "", fieldValue = "", filePath = "";
            
			try{
	            
				DiskFileItemFactory dff = new DiskFileItemFactory();
				ServletFileUpload sfu = new ServletFileUpload(dff);
				List<FileItem> items = sfu.parseRequest(request);
				for(FileItem item:items){
					if(item.isFormField()){
						//��ͨ����
						fieldName = item.getFieldName();
						fieldValue = item.getString();
						System.out.println("name="+fieldName + ", value="+ fieldValue);
						
					} else {// ��ȡ�ϴ��ֶ�
						// �����ļ���ΪΨһ��
						String filename = item.getName();
						if (filename != null) {
							filename = IdGenertor.generateGUID() + "." + FilenameUtils.getExtension(filename);
						}
						// ���ɴ洢·��
						String storeDirectory = getServletContext().getRealPath("/files/images");
						File file = new File(storeDirectory);
						if (!file.exists()) {
							file.mkdir();
						}
						String path = genericPath(filename, storeDirectory);
						// �����ļ����ϴ�
						try {
							item.write(new File(storeDirectory + path, filename));
							
							filePath = "/files/images" + path + "/" + filename;
							System.out.println("filePath="+filePath);
							message = filePath;
							
						} catch (Exception e) {
							message = "�ϴ�ͼƬʧ��";
						}
					}
				}
				
	            String str = "name="+fieldName + ", value="+ fieldValue;
	            str += "\r\n";
                str += "filePath="+filePath;
                str += "\r\n";
        		
                String storeDirectory = getServletContext().getRealPath("");
				File file = new File(storeDirectory);
				if (!file.exists()) {
					file.mkdir();
				}
				File dict = new File(storeDirectory,"data.txt");
				if(!dict.exists()){
					dict.mkdirs();
				}
				
				Writer addWord = new FileWriter(dict,true);
				BufferedWriter addword = new BufferedWriter(addWord);
				addword.write(str);
				addword.newLine();
				addword.close();
				
				
			} catch (Exception e) {
				e.printStackTrace();
				message = "�ϴ�ͼƬʧ��";
			} finally {
				response.getWriter().write(message);
			}
		}
	
	//�����ļ��Ĵ��Ŀ¼
	private String genericPath(String filename, String storeDirectory) {
		String dir = "";
		
		File file = new File(storeDirectory,dir);
		if(!file.exists()){
			file.mkdirs();
		}
		return dir;
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}