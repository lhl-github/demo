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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xljy.kwld.dao.Talk_di;
import com.xljy.kwld.dao.TextType_di;
import com.xljy.kwld.dao.Word_di;
import com.xljy.util.Const;

import cn.hutool.core.io.FileUtil;


@Service
public class Word_sm {
	@Autowired
	private Word_di word_di;
	@Autowired
	private TextType_di type_di;
	
	//查询
	@Transactional
	public Map<String, Object> findAllWord(Map parameterStringMap){
		List< Map<String, Object>> map = null;
		List<Map<String, Object>> find = word_di.findByType(parameterStringMap);//word_phrase:word/phrase
		List<Map<String, Object>> list = word_di.findAllWord(parameterStringMap);
		find.get(0).put("wordList", list);
		find.get(0).put("size", list.size());
		return find.get(0);
	}
	//
	public List< Map<String, Object>> findByType(Map parameterStringMap){
		
		return word_di.findByType(parameterStringMap);
	}
	//根据type进行删除
	@Transactional
	public Map<String, Object> delWordByType(Map<String, String> parameterStringMap) {
		HashMap<String, Object> map=new HashMap<>();//将要返回给前端
		String type = parameterStringMap.get("type");
		String word_phrase = parameterStringMap.get("word_phrase");
		Map<String, String> save = word_di.get_savepath(type);
		//删除
		word_di.delWordByType(parameterStringMap);//根据word_phrase和type删除
		
		if(save == null) {
			map.put("msg", "数据已经删除");
			return map;
		}
		String save_path =null;
		if(word_phrase.equals("word")) {
			save_path = save.get("save") + "/" + Const.DANCI + type + "/";
		}else if(word_phrase.equals("phrase")) {
			save_path = save.get("save") + "/" + Const.DUANYU + type + "/";
		}
		
		String filePath = Const.getFilePath_Real(save_path);
		boolean del = FileUtil.del(filePath);
		if(!del) {
			map.put("msg", "文件路径不存在了");
		}else {
			map.put("msg", "删除文件成功");
		}
		return map;
	}
	//修改/保存
	@Transactional
	public Map<String, Object> addWord(Map<String, String> parameterStringMap){
		HashMap<String, Object> hashMap=new HashMap<>();//传递给后台的
		Map map=null;//得到前台传递的数据
		String json=parameterStringMap.get("json");//接收前端传递的json
		ObjectMapper mapper=new ObjectMapper();//将json转换为map
		ArrayList<String> files = new ArrayList<>();// 保存文件名的集合
		try {
			map=mapper.readValue(json, Map.class);//map={jclx;nj;dy;sxc;xd;wordList;type;word_phrase}
			List<Map> wordList=(List<Map>) map.get("wordList");//wordList={word;zw;phrase;phraseTran;video}
			//判断是修改   还是保存
			hashMap.put("word_phrase", map.get("word_phrase"));
			if(map.containsKey("photo") && map.get("photo").toString().length()>0) {
				hashMap.put("photo", map.get("photo"));
				files.add(map.get("photo").toString());
			}
			if(map.containsKey("type")==true) {//修改
				 List<Map<String,Object>> list = word_di.findByID(map);
				 map = list.get(0);
			}
			
			if(!hashMap.get("word_phrase").equals("word") && !hashMap.get("word_phrase").equals("phrase")) {
				throw new RuntimeException("无法确定添加的数据!");
			}
			//判断 是否存在  课文单元
			List<Map<String, Object>> typeList=type_di.findByFive(map);
			if(typeList==null || typeList.isEmpty()) {
				hashMap.put("type", UUID.randomUUID().toString());
				map.put("type", hashMap.get("type"));
				int add_kewendanyuan=type_di.addTextType(map);//保存修改
				 hashMap.put("创建课文单元个数", add_kewendanyuan);
					
			}else if(typeList.size()>1) {
				throw new RuntimeException("发现多条课文单元数据 ===>>>无法确定加入到哪个课文单元!");
			}else {
				hashMap.put("type", typeList.get(0).get("type"));
				map.put("type", hashMap.get("type"));
				if(hashMap.containsKey("photo")) {
					map.put("photo", hashMap.get("photo"));
					type_di.updatePhoto(map);
				}
				
			}
			//save_path :生成方法 >>>>>> :教材类型代码_学段代码_年级代码_上下册代码_单元代码_学校代码/课文类别
			StringBuffer Buffer_save_path = new StringBuffer();
			Buffer_save_path.append(map.get("jclx") + "_");
			Buffer_save_path.append(map.get("xd") + "_");
			Buffer_save_path.append(map.get("nj") + "_" );
			Buffer_save_path.append(map.get("sxc") + "_");
			Buffer_save_path.append(map.get("dy"));
			hashMap.put("dy_tupian", Buffer_save_path.toString() + "/");
            if(hashMap.get("word_phrase").equals("word")) {
            	hashMap.put("save_path", Buffer_save_path.toString() + "/" + Const.DANCI+hashMap.get("type")+"/");
			}else if(hashMap.get("word_phrase").equals("phrase")) {
				hashMap.put("save_path", Buffer_save_path.toString() + "/" + Const.DUANYU+hashMap.get("type")+"/");
			}
			
			//取单词
			int count = word_di.delWordByType(hashMap);//删除原来数据
		    for (int i = 0; i < wordList.size(); i++) {
				Map word=wordList.get(i);//word={word;zw;phrase;phraseTran;video}
				word.put("word_phrase", hashMap.get("word_phrase"));
				word.put("type", hashMap.get("type"));
				if(hashMap.get("word_phrase").equals("word") && word.get("word") == null) {
					throw new RuntimeException("单词不能为空!");
				}else if(hashMap.get("word_phrase").equals("phrase") && word.get("phrase") == null) {
					throw new RuntimeException("短语不能为空!");
				}
				//增加（不存在id）
				hashMap.put("id", UUID.randomUUID().toString());
				word.put("id", hashMap.get("id"));
				int num=word_di.addWord(word);//保存
				hashMap.put("flag", "单词"+i+"添加成功");   
				
				//加入文件集合
				if(word.containsKey("video") && word.get("video").toString().length()>0) {
					files.add(word.get("video").toString());
				}                                                                                
			}
		    
		    /*
			 * 数据库同步完成 处理多余文件
			 */
		    if(map.containsKey("type")==true) {
		    	    Object[] arr_files1 = files.toArray();
					String[] arr_files = ArrayUtils.toStringArray(arr_files1);
					
					String[] arr_yinpin = Const.getyinpin(arr_files);// 得到音频的文件名集合
					String[] arr_tupian = Const.gettupian(arr_files);// 得到图片的文件名集合
					String[] arr_shipin = Const.getshipin(arr_files);// 得到视频的文件名集合

					String yinpinfilePath = Const.getFilePath_Real(hashMap.get("save_path").toString(), "2");// 2音频
					String tupianfilePath = Const.getFilePath_Real(hashMap.get("dy_tupian").toString(), "1");// 1图片
					String shipinfilePath = Const.getFilePath_Real(hashMap.get("save_path").toString(), "3");// 3视频

					Const.delfile_lujingxiabubaohan(hashMap, arr_yinpin, yinpinfilePath, "音频");
					Const.delfile_lujingxiabubaohan(hashMap, arr_tupian, tupianfilePath, "图片");
					Const.delfile_lujingxiabubaohan(hashMap, arr_shipin, shipinfilePath, "视频");
		    }
		  

		} catch (Exception e) {
			e.printStackTrace();
 		   throw new RuntimeException(e.getMessage());
		}		
		
		return hashMap;
	}

}
