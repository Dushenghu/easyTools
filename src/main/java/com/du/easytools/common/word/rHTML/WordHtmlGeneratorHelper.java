package com.du.easytools.common.word.rHTML;

import com.du.easytools.testDemo.User;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;



/**
* @Description:word 网页导出（单文件网页导出，mht文件格式）
*/
public class WordHtmlGeneratorHelper  {

	/**
	* @Description: 将字符换成3Dus-asci，十进制Accsii码
	* @param @param source
	*/
	public static String string2Ascii(String source){
		if(source==null || source==""){
			return null;
		}
		StringBuilder sb=new StringBuilder();

		char[] c=source.toCharArray();
		for(char item : c){
			String itemascii="";
			if(item>=19968 && item<40623){
				itemascii=itemascii="&#"+(item & 0xffff)+";";
			}else{
				itemascii=item+"";
			}
			sb.append(itemascii);
		}

		return sb.toString();

	}

	/**
	* @Description: 将object的所有属性值转成成3Dus-asci编码值
	* @param @param object
	* @param @return
	* @return T
	* @throws
	* @author:LiaoFei
	* @date:2016-3-29 下午2:56:24
	*/
	public static <T extends Object> T handleObject2Ascii(final T toHandleObject){

		class myFieldsCallBack  implements FieldCallback{

			@Override
			public void doWith(Field f) throws IllegalArgumentException,
					IllegalAccessException {
				if(f.getType().equals(String.class)){
					//如果是字符串类型
					f.setAccessible(true);
					String oldValue=(String)f.get(toHandleObject);
					if(!StringUtils.isEmpty(oldValue)){
						f.set(toHandleObject, string2Ascii(oldValue));
					}

					//f.setAccessible(false);
				}
			}
		}

		ReflectionUtils.doWithFields(toHandleObject.getClass(), new myFieldsCallBack());

		return toHandleObject;
	}


	public static <T extends Object> List<T> handleObjectList2Ascii(final List<T> toHandleObjects){

		for (T t : toHandleObjects) {
			handleObject2Ascii(t);
		}

		return toHandleObjects;
	}


	public static void handleAllObject(Map<String, Object> dataMap){

		//去处理数据
    	for (Map.Entry<String, Object> entry : dataMap.entrySet()){
    		Object item=entry.getValue();

    		//判断object是否是primitive type
    		if(isPrimitiveType(item.getClass())){
    			if(item.getClass().equals(String.class)){
    				item=WordHtmlGeneratorHelper.string2Ascii((String)item);
    				entry.setValue(item);
    			}
    		}else if(isCollection(item.getClass())){
    			for (Object itemobject : (Collection)item) {
    				WordHtmlGeneratorHelper.handleObject2Ascii(itemobject);
				}
    		}else{
    			WordHtmlGeneratorHelper.handleObject2Ascii(item);
    		}
    	}

	}

	public static String joinList(List<String> list,String join ){
		StringBuilder sb=new StringBuilder();
		for (String t : list) {
			sb.append(t);
			if(!StringUtils.isEmpty(join)){
				sb.append(join);
			}
		}

		return sb.toString();
	}


	private static boolean isPrimitiveType(Class<?> clazz){
    	return clazz.isEnum() ||
		CharSequence.class.isAssignableFrom(clazz) ||
		Number.class.isAssignableFrom(clazz) ||
		Date.class.isAssignableFrom(clazz);

    }
    private static boolean isCollection(Class<?> clazz){
    	return Collection.class.isAssignableFrom(clazz);
    }

	public static void main(String[] args) {
//		ProjectZrdw object=new ProjectZrdw();
//
//		object.setDwname("湖南省交通科学研究院");
//		object.setDwszd("长沙");
//
//		handleObject2Ascii(object);
//
//		System.out.println(object.getDwname() +"\n");
//		System.out.println(object.getDwszd());

		/*ProjectSbWithBLOBs sb=new ProjectSbWithBLOBs();
		sb.setSbdw("湖南省交通科学研究院");

		handleObject2Ascii(sb);
		System.out.println(sb.getSbdw());*/

		List<User> users =new ArrayList<User>();

		User user=new User();


		handleObjectList2Ascii(users);
		for (User projectUser : users) {
			System.out.println(projectUser.getName());
		}


	}


}
