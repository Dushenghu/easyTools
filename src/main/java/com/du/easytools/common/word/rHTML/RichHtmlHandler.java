package com.du.easytools.common.word.rHTML;

import com.du.easytools.common.file.FileUtils;
import com.du.easytools.common.word.RequestResponseContext.RequestResponseContext;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * @Description:富文本Html处理器，主要处理图片及编码
 * @author:LiaoFei
 * @date :2016-3-28 下午4:13:21
 * @version V1.0
 *
 */
public class RichHtmlHandler {

	private Document doc = null;
	private String html;

	private String docSrcParent = "";
	private String docSrcLocationPrex = "";
	private String nextPartId;
	private String shapeidPrex;
	private String spidPrex;
	private String typeid;

	private String handledDocBodyBlock;
	private List<String> docBase64BlockResults = new ArrayList<String>();
	private List<String> xmlImgRefs = new ArrayList<String>();

	public String getDocSrcLocationPrex() {
		return docSrcLocationPrex;
	}

	public void setDocSrcLocationPrex(String docSrcLocationPrex) {
		this.docSrcLocationPrex = docSrcLocationPrex;
	}

	public String getNextPartId() {
		return nextPartId;
	}

	public void setNextPartId(String nextPartId) {
		this.nextPartId = nextPartId;
	}

	public String getHandledDocBodyBlock() {
		String raw=   WordHtmlGeneratorHelper.string2Ascii(doc.getElementsByTag("body").html());
		return raw.replace("=3D", "=").replace("=", "=3D");
	}

	public String getRawHandledDocBodyBlock() {
		String raw=  doc.getElementsByTag("body").html();
		return raw.replace("=3D", "=").replace("=", "=3D");
	}
	public List<String> getDocBase64BlockResults() {
		return docBase64BlockResults;
	}

	public List<String> getXmlImgRefs() {
		return xmlImgRefs;
	}

	public String getShapeidPrex() {
		return shapeidPrex;
	}

	public void setShapeidPrex(String shapeidPrex) {
		this.shapeidPrex = shapeidPrex;
	}

	public String getSpidPrex() {
		return spidPrex;
	}

	public void setSpidPrex(String spidPrex) {
		this.spidPrex = spidPrex;
	}

	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	public String getDocSrcParent() {
		return docSrcParent;
	}

	public void setDocSrcParent(String docSrcParent) {
		this.docSrcParent = docSrcParent;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public RichHtmlHandler(String html) {
		doc = Jsoup.parse(	wrappHtml(html));
	}

	public void re_init(String html){
		doc=null;
		doc = Jsoup.parse(wrappHtml(html));
		docBase64BlockResults.clear();
		xmlImgRefs.clear();
	}

	/**
	 * @Description: 获得已经处理过的HTML文件
	 * @param @return
	 * @return String
	 */
	public void handledHtml(boolean isWebApplication)
			throws IOException {
		Elements imags = doc.getElementsByTag("img");

		if (imags == null || imags.size() == 0) {
			// 返回编码后字符串
			return;
			//handledDocBodyBlock = WordHtmlGeneratorHelper.string2Ascii(html);
		}

		// 转换成word mht 能识别图片标签内容，去替换html中的图片标签

		for (Element item : imags) {
			// 把文件取出来
			String src = item.attr("src");
			String srcRealPath = src;

			if (isWebApplication) {
				String contentPath= RequestResponseContext.getRequest().getContextPath();
				if(!StringUtils.isEmpty(contentPath)){
					if(src.startsWith(contentPath)){
						src=src.substring(contentPath.length());
					}
				}

				srcRealPath = RequestResponseContext.getRequest().getSession()
						.getServletContext().getRealPath(src);

			}

			File imageFile = new File(srcRealPath);
			String imageFielShortName = imageFile.getName();
			String fileTypeName = FileUtils.getFileSuffix(srcRealPath);

			String docFileName = "image" + UUID.randomUUID() + "."
					+ fileTypeName;
			String srcLocationShortName = docSrcParent + "/" + docFileName;

			String styleAttr = item.attr("style"); // 样式
			//高度
			String imagHeightStr=item.attr("height");;
			if(StringUtils.isEmpty(imagHeightStr)){
				imagHeightStr = getStyleAttrValue(styleAttr, "height");
			}
			//宽度
			String imagWidthStr=item.attr("width");;
			if(StringUtils.isEmpty(imagHeightStr)){
				imagHeightStr = getStyleAttrValue(styleAttr, "width");
			}

			imagHeightStr = imagHeightStr.replace("px", "");
			imagWidthStr = imagWidthStr.replace("px", "");
			if(StringUtils.isEmpty(imagHeightStr)){
				//去得到默认的文件高度
				imagHeightStr="0";
			}
			if(StringUtils.isEmpty(imagWidthStr)){
				imagWidthStr="0";
			}
			int imageHeight = Integer.parseInt(imagHeightStr);
			int imageWidth = Integer.parseInt(imagWidthStr);

			// 得到文件的word mht的body块
			String handledDocBodyBlock = WordImageConvertor.toDocBodyBlock(srcRealPath,
					imageFielShortName, imageHeight, imageWidth,styleAttr,
					srcLocationShortName, shapeidPrex, spidPrex, typeid);

			item.parent().append(handledDocBodyBlock);
			item.remove();
			// 去替换原生的html中的imag

			String base64Content = WordImageConvertor
					.imageToBase64(srcRealPath);
			String contextLoacation = docSrcLocationPrex + "/" + docSrcParent
					+ "/" + docFileName;

			String docBase64BlockResult = WordImageConvertor
					.generateImageBase64Block(nextPartId, contextLoacation,
							fileTypeName, base64Content);
			docBase64BlockResults.add(docBase64BlockResult);

			String imagXMLHref = "<o:File HRef=3D\"" + docFileName + "\"/>";
			xmlImgRefs.add(imagXMLHref);

		}

	}

	private String getStyleAttrValue(String style, String attributeKey) {
		if (StringUtils.isEmpty(style)) {
			return "";
		}

		// 以";"分割
		String[] styleAttrValues = style.split(";");
		for (String item : styleAttrValues) {
			// 在以 ":"分割
			String[] keyValuePairs = item.split(":");
			if (attributeKey.equals(keyValuePairs[0])) {
				return keyValuePairs[1];
			}
		}

		return "";
	}

	private String wrappHtml(String html){
		// 因为传递过来都是不完整的doc
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<body>");
		sb.append(html);

		sb.append("</body>");
		sb.append("</html>");
		return sb.toString();
	}
	/**
	 * @Description: 测试
	 * @param @param args
	 * @return void
	 * @throws IOException
	 * @throws
	 * @author:LiaoFei
	 * @date:2016-3-29 上午9:39:12
	 */
	public static void main(String[] args) throws IOException {

		StringBuilder sb = new StringBuilder();
		sb.append("<div>");

		sb.append("<img style='height:100px;width:200px;display:block;' src='F:\\725017921264249223.jpg' />");
		sb.append("<img style='height:100px;width:200px;display:block;' src='F:\\身份证\\身份证-廖飞.jpg' />");
		sb.append("<span>中国梦，幸福梦！</span>");
		sb.append("</div>");

		RichHtmlHandler handler = new RichHtmlHandler(sb.toString());

		handler.setDocSrcLocationPrex("file:///C:/70ED9946");
		handler.setDocSrcParent("file9462.files");
		handler.setNextPartId("01D189BB.30229F00");
		handler.setShapeidPrex("_x56fe__x7247__x0020");
		handler.setSpidPrex("_x0000_i");
		handler.setTypeid("#_x0000_t75");

		//写入文件中，
		try {
			handler.handledHtml(false);

			String logFile="D:\\log.txt";

			File file=new File(logFile);
			//FileOutputStream out=new FileOutputStream(file);
			FileWriter fw=new FileWriter(file);


			fw.write("======handledDocBody block==========\n");
			fw.write(handler.getHandledDocBodyBlock());

			fw.write("======handledBase64Block==========\n");
			if (handler.getDocBase64BlockResults() != null
					&& handler.getDocBase64BlockResults().size() > 0) {
				for (String item : handler.getDocBase64BlockResults()) {
					fw.write(item + "\n");
				}
			}
			if (handler.getXmlImgRefs() != null
					&& handler.getXmlImgRefs().size() > 0) {
				fw.write("======xmlimaHref==========\n");
				for (String item : handler.getXmlImgRefs()) {
					fw.write(item + "\n");
				}
			}

			fw.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

	}



}
