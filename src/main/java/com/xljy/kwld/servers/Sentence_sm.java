package com.xljy.kwld.servers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xljy.kwld.dao.Sentence_di;
import com.xljy.kwld.dao.TextType_di;
import com.xljy.util.Const;

import cn.hutool.core.io.FileUtil;

@Service
public class Sentence_sm {
	@Autowired
    private Sentence_di di;
	@Autowired
	private TextType_di type_di;
 

//删
     public Map<String, Object> delSentence(Map<String, String> parameterStringMap) {
    	 HashMap<String, Object> map=new HashMap<>();//将要返回给前端
    	 String jzid = parameterStringMap.get("jzid");
    	 Map<String, String> save = di.get_savepath(jzid);
    	 //删除
    	 di.delSentName(parameterStringMap);//
         di.del(parameterStringMap);
    	 
         if(save == null) {
        	 map.put("msg", "数据已经删除");
 			 return map;
         }
         
         String save_path = save.get("save") + "/" +Const.JVZI + jzid + "/";
         
         String filePath = Const.getFilePath_Real(save_path);
         boolean del = FileUtil.del(filePath);
         if (!del) {
 			map.put("msg", "文件路径不存在了");
 		} else {
 			map.put("msg", "删除文件成功");
 		}
    	 return map;
    		
    	 
     }

//查
//所有(教材、学段、年级、上下册、单元)
     public List< Map<String, Object>> findAllSentence(Map<String, String> parameterStringMap){
    	 
    	 return di.findAllSentence(parameterStringMap);
     }
     
//根据id查询一个
     public Map<String, Object> findByIdSentence(Map<String, String> parameterStringMap) {
    	 
    	 List<Map<String, Object>> findAll=di.findAllSentence(parameterStringMap);//查所有
    	
    	 List<Map<String, Object>> list=di.findByJz(parameterStringMap);
  
    	 findAll.get(0).put("sentence", list);
    	 findAll.get(0).put("size", list.size());
    
    	 return findAll.get(0);
     }
   
//对句子进行增加
 	@Transactional
     public Map<String, Object> add(Map<String, String> parameterStringMap) throws RuntimeException{
       HashMap<String, Object> hashMap=new HashMap<>();//要返回的结果集
       String json=parameterStringMap.get("json");//前端给的json值
       Map map=null;//从前端获取的数据map(jclx;xd,nj;sxc;dy;sentenceList)
       ObjectMapper mapper=new ObjectMapper();//借助它将json的字符串传递给map
       try {
    	   map=mapper.readValue(json, Map.class);//将json中的数据传给map,将json转成map
		
           //判断 该课文单元 是否存在
           List<Map<String, Object>> typeList= type_di.findByFive(map);
       
           if(typeList==null || typeList.isEmpty()) {
        	   //添加 课文单元
    	       hashMap.put("type", UUID.randomUUID().toString());//将type保存到hashmap
    	       map.put("type", hashMap.get("type"));
    	       int add_texttype= type_di.addTextType(map);
    	       hashMap.put("创建课文单元个数", add_texttype);   
           }else if(typeList.size() > 1) { 
    	      throw new RuntimeException("发现多条课文单元数据 ===>>>无法确定加入到哪个课文单元!");
           }else {
			 hashMap.put("type", typeList.get(0).get("type"));//将type保存到hashmap
		   }
       
           List<Map> sentenceList=(List<Map>) map.get("sentenceList");//获取句子列表sentenceList(jzmc,zw,yw,video，del=0代表删除)
           hashMap.put("创建句子个数", sentenceList.size());
        
           for (int i = 0; i < sentenceList.size(); i++) {//将句子列表进行遍历  
        	   Map sentence=sentenceList.get(i);//获取句子
		       //判断是否存在此句子
		       sentence.put("type",hashMap.get("type"));
		       Map<String, Object> list=di.findByContent( sentence);
		       //添加 句子
		       if (list==null  || list.isEmpty()) {
		    	   hashMap.put("id", UUID.randomUUID().toString());//将数据传递给前台
			       sentence.put("id", hashMap.get("id"));
			       di.addSentence(sentence);//将前台传递的数据保存到数据库
		       }else {
			       throw new RuntimeException("添加句子失败 ===句子已存在>>>!");
		       }
           }
       } catch (Exception e1) {
    	   e1.printStackTrace();
   		   throw new RuntimeException(e1.getMessage());
       }
       return hashMap;
 	}
     
//对句子进行修改或者添加
 	@Transactional
     public Map<String, Object>  updateSentence(Map<String, String> parameterStringMap) {
 		HashMap<String, Object> hashMap=new HashMap<>();//要返回的结果集
 		String json=parameterStringMap.get("json");//获取前台给的json
 		ObjectMapper mapper=new ObjectMapper();//将前端的json转为map
 		Map map=null;//将前端数据写入 map(jclx;xd,nj;sxc;dy;jzList)
 		ArrayList<String> files = new ArrayList<>();// 保存文件名的集合
 		try {
 			
			map=mapper.readValue(json, Map.class);
			Map jzList= (Map) map.get("jzList");//jzList={jzmc;jzid;sentenceList}
			List<Map> sentenceList=(List<Map>) jzList.get("sentenceList");//获取句子列表sentenceList(zw,yw,video)
			if(map.containsKey("jzid")==true) {
				map=di.findByID(map);
			}
			//判断是否存在  课文单元
 		    List<Map<String, Object>> typeList= type_di.findByFive(map);
 		    if(typeList.isEmpty() || typeList==null) {
 		    	//添加课文单元
 		    	hashMap.put("type", UUID.randomUUID().toString());
 			    map.put("type", hashMap.get("type"));
 			    int add_kewendanyuan= type_di.addTextType(map);//保存修改 
 			    hashMap.put("创建课文单元个数", add_kewendanyuan);		
 		    }else if(typeList.size()>1) {
 		    	throw new RuntimeException("发现多条课文单元数据 ===>>>无法确定加入到哪个课文单元!");
 		    }else {
 			    hashMap.put("type", typeList.get(0).get("type"));
 		    }
			
 		   jzList.put("type", hashMap.get("type"));
 		   if(map.containsKey("jzid")==false) {//保存句子名称
 			   hashMap.put("jzid", UUID.randomUUID().toString());
 			   jzList.put("jzid", hashMap.get("jzid"));
 			   int num=di.addSentName(jzList);
				   hashMap.put("flag", "添加成功");
 		   }else {//修改
 			   hashMap.put("jzid", map.get("jzid"));
 			   jzList.put("jzid", hashMap.get("jzid"));
 			   int num=di.updateSentName(jzList);
			   hashMap.put("flag", "修改成功");
 			   int count = di.del(jzList);//删除map里面的jzid
 		   }

		   //save_path :生成方法 >>>>>> :教材类型代码_学段代码_年级代码_上下册代码_单元代码_学校代码/课文类别
			StringBuffer Buffer_save_path = new StringBuffer();
			Buffer_save_path.append(map.get("jclx") + "_");
			Buffer_save_path.append(map.get("xd") + "_");
			Buffer_save_path.append(map.get("nj") + "_" );
			Buffer_save_path.append(map.get("sxc") + "_");
			Buffer_save_path.append(map.get("dy"));
			hashMap.put("save_path", Buffer_save_path.toString() + "/" + Const.JVZI+hashMap.get("jzid")+"/");
			hashMap.put("dy_tupian", Buffer_save_path.toString() + "/");
			
 	        hashMap.put("创建句子个数", sentenceList.size());
 	        for (int i = 0; i < sentenceList.size(); i++) {//将句子列表进行遍历  
 	        	Map sentence=sentenceList.get(i);//获取句子
 			    //判断是否存在此句子(存在只可以删除；不存在的即可以修改也可以保存或者删除)
 	        	sentence.put("type", hashMap.get("type"));
 	        	sentence.put("jzid", hashMap.get("jzid"));

 	        //对句子进行添加
 	        	hashMap.put("id", UUID.randomUUID().toString());
 	        	sentence.put("id", hashMap.get("id"));
 			    int num=di.addSentence(sentence);
 			    hashMap.put("flag","句子"+i+"添加成功");

 			    //加入文件集合
 			    if(sentence.containsKey("video") && sentence.get("video").toString().length()>0) {
 			    	files.add(sentence.get("video").toString());
 			    }
 	        }
 	        
 	       /*
 			 * 数据库同步完成 处理多余文件
 			 */

 	   	if(map.containsKey("jzid")==true) {
 	   	    Object[] arr_files1 = files.toArray();
			String[] arr_files = ArrayUtils.toStringArray(arr_files1);

			String[] arr_yinpin = Const.getyinpin(arr_files);// 得到音频的文件名集合
			String[] arr_tupian = Const.gettupian(arr_files);// 得到图片的文件名集合
			String[] arr_shipin = Const.getshipin(arr_files);// 得到视频的文件名集合

			String yinpinfilePath = Const.getFilePath_Real(hashMap.get("save_path").toString(), "2");// 2音频
			String tupianfilePath = Const.getFilePath_Real(hashMap.get("save_path").toString(), "1");// 1图片
			String shipinfilePath = Const.getFilePath_Real(hashMap.get("save_path").toString(), "3");// 3视频

			Const.delfile_lujingxiabubaohan(hashMap, arr_yinpin, yinpinfilePath, "音频");
			Const.delfile_lujingxiabubaohan(hashMap, arr_tupian, tupianfilePath, "图片");
			Const.delfile_lujingxiabubaohan(hashMap, arr_shipin, shipinfilePath, "视频");

	        
 	   	}
 			
 		}catch (Exception e1) {
     	   e1.printStackTrace();
    		   throw new RuntimeException(e1.getMessage());
        }
 		  
 		 return hashMap;
 	}

}
