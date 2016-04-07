package com.dianrong.common.uniauth.cas.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**.
 * 文件处理相关util.主要用于cas的子系统
 * @author wanglin
 *
 */
public class FileUtil {

	/**.
	 * 返回webApp文件夹所在路径
	 * @return
	 */
	public static String getWebAppLocation(){
		String classesLocation = FileUtil.class.getClassLoader().getResource("").getPath();
		File tclasses = new File(classesLocation);
		
		// ../../classes
		return tclasses.getParentFile().getParent();
	}
	
	/**.
	 * 
	 * @param relativePathForWebApp 相对于webapp的相对路径
	 * @return 读取到的文件流数据
	 * @throws IOException  文件读取io异常
	 */
	public static byte[] readFiles(String relativePathForWebApp) throws IOException{
		if(relativePathForWebApp == null){
			relativePathForWebApp = "";
		}
		if(relativePathForWebApp.startsWith(File.separator)){
			relativePathForWebApp = relativePathForWebApp.substring(1);
		}
		String realFilePath = getWebAppLocation()+File.separator+relativePathForWebApp;
		File file = new File(realFilePath);
		if(file.exists() && file.isFile()){
			byte[] bytes = new byte[(int)file.length()];
			BufferedInputStream bufferedInputStream= null;
			try{
				bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
				int r = bufferedInputStream.read( bytes );
				if (r != file.length())
					throw new IOException("读取文件不正确");
			} catch(IOException ex){
				throw ex;
			} finally {
				if(bufferedInputStream != null){
					bufferedInputStream.close();
				}
			}
			return bytes;
		}
		return null;
	}
}
