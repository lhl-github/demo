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
import com.xljy.kwld.dao.Read_di;
import com.xljy.kwld.dao.TextType_di;
import com.xljy.util.Const;

import cn.hutool.core.io.FileUtil;

@Service
public class Read_sm {
	@Autowired
	private Read_di read_di;
	@Autowired
	private TextType_di type_di;
	
	//查询阅读
	public List< Map<String, Object>> findAll(Map parameterStringMap){
		List<Map<String, Object>> list=read_di.findAll(parameterStringMap);
		return list;
	}
	//查询详情
	public Map<String, Object> findRead(Map parameterStringMap){
		List<Map<String, Object>> list=read_di.findAll(parameterStringMap);
		List<Map<String, Object>> questList=read_di.findAllQuest(parameterStringMap);
		for (int i = 0; i < questList.size(); i++) {
			questList.get(i).put("questionList", read_di.findAllXuan(questList.get(i)));
		}
		list.get(0).put("readList", questList);
		list.get(0).put("size", questList.size());
		return list.get(0);
	}
	//删除
	@Transactional
	public Map<String, Object> delRead(Map<String, String> parameterStringMap){
		HashMap<String, Object> ret_map=new HashMap<>();
		String rid = parameterStringMap.get("rid");
		Map<String, String> save = read_di.get_savepath(rid);
		//判断是否存在阅读
		List<Map<String, Object>> read=read_di.findAll(parameterStringMap);
		if(read.isEmpty() || read==null) {
			throw new RuntimeException("删除阅读失败!");
		}else {
			//判断是否存在问题
			List<Map<String, Object>> questList=read_di.findAllQuest(read.get(0));
			read_di.delRead(parameterStringMap);
			if(questList ==null || questList.isEmpty()) {
				ret_map.put("size", questList.size());
			}else {
				for (int i = 0; i < questList.size(); i++) {
					read_di.delQuestByRid(questList.get(i));
					read_di.delXuanByQid(questList.get(i));
				}
				ret_map.put("size", questList.size());
				}
		}
		
		if(save == null) {
			ret_map.put("msg", "数据已经删除");
			return ret_map;
		}
		
		String save_path = save.get("save") + "/" + Const.YUEDU + rid + "/";
		String filePath = Const.getFilePath_Real(save_path);
		boolean del = FileUtil.del(filePath);
		if (!del) {
			ret_map.put("msg", "文件路径不存在了");
		} else {
			ret_map.put("msg", "删除文件成功");
		}
		return ret_map;
	}

	//保存、修改
	@Transactional
	public Map<String, Object> addRead(Map<String, String> parameterStringMap){
		HashMap<String, Object> hashMap = new HashMap<>();//给后台传输数据
		String json=parameterStringMap.get("json");
		Map map=null;//用来接收前台传输的数据
		ObjectMapper mapper=new ObjectMapper();//
		ArrayList<String> files = new ArrayList<>();// 保存文件名的集合
		try {
			map=mapper.readValue(json, Map.class);//将json字符串转化为map：json={nj;xd;jclx;sxc;dy;readList}
			Map readList=(Map) map.get("readList");//readList={rid;readtitle;readtext;readcontent;photo;questList}
			//判断是添加还是修改
			if(map.containsKey("rid")==true) {
				map=read_di.findByID(map);
			}
			
			List<Map<String, Object>> typeList = type_di.findByFive(map);
			if(typeList == null || typeList.isEmpty()) {//添加 课文单元
				hashMap.put("type", UUID.randomUUID().toString());
				map.put("type", hashMap.get("type"));
				int num = type_di.addTextType(map);	
				
			}else if(typeList.size() >1) {
				throw new RuntimeException("发现多条课文单元数据 ===>>>无法确定加入到哪个课文单元!");
			}else {//存在 课文单元
				hashMap.put("type", typeList.get(0).get("type"));
			}		
			//
			if(readList.containsKey("photoes") && readList.get("photoes").toString().length()>0) {
				files.add(readList.get("photoes").toString());
			}
			//判断是否存在  阅读
	    	readList.put("type", hashMap.get("type"));
			if(map.containsKey("rid") == false) {//保存阅读题目
				hashMap.put("rid", UUID.randomUUID().toString());
				readList.put("rid", hashMap.get("rid"));
				int num = read_di.saveRead(readList);
					hashMap.put("flag", "添加成功");
			}else {//修改阅读题目
				hashMap.put("rid", map.get("rid"));
				readList.put("rid", hashMap.get("rid"));
				int num = read_di.updateRead(readList);
					hashMap.put("flag", "修改成功");
					
				 List<Map<String, Object>> list = read_di.findAllQuest(readList);//根据rid查询qid
				// read_di.delRead(readList);
				 for (int i = 0; i < list.size(); i++) {
					read_di.delXuanByQid(list.get(i));//删除选项
					read_di.delQuest(list.get(i));//删除问题
				}
			}
			
			//save_path :生成方法 >>>>>> :教材类型代码_学段代码_年级代码_上下册代码_单元代码_学校代码/课文类别
			StringBuffer Buffer_save_path = new StringBuffer();
			Buffer_save_path.append(map.get("jclx") + "_");
			Buffer_save_path.append(map.get("xd") + "_");
			Buffer_save_path.append(map.get("nj") + "_" );
			Buffer_save_path.append(map.get("sxc") + "_");
			Buffer_save_path.append(map.get("dy"));
			hashMap.put("save_path", Buffer_save_path.toString() + "/" + Const.YUEDU+hashMap.get("rid")+"/");
			hashMap.put("dy_tupian", Buffer_save_path.toString() + "/");
			
			//
			List<Map> questList=(List<Map>) readList.get("questList");//questList={readquest;readanswer;readresolve;xuanList;del=0代表删除}
			for (int i = 0; i < questList.size(); i++) {
				Map quest = questList.get(i);
				quest.put("rid", hashMap.get("rid"));
				//判断是否存在 阅读问题	
				//添加阅读问题
				hashMap.put("qid", UUID.randomUUID().toString());
				quest.put("qid", hashMap.get("qid"));
				read_di.saveQuest(quest);
				hashMap.put("flag", "阅读问题"+i+"添加成功");
				
				//
				if(quest.containsKey("photoes") && quest.get("photoes").toString().length()>0) {
					files.add(quest.get("photoes").toString());
				}
				
				List<Map> xuanList = (List<Map>) quest.get("xuanList");//xuanList={xid;qid;xuanxiang;tupian;xxtext;del=0代表删除}
				for (int j = 0; j < xuanList.size(); j++) {
					Map xuan=xuanList.get(j);
					xuan.put("qid", hashMap.get("qid"));
					//添加
					hashMap.put("xid", UUID.randomUUID().toString());
					xuan.put("xid", hashMap.get("xid"));
					read_di.saveXuan(xuan);
					hashMap.put("flag", "阅读选项"+j+"添加成功");
					
					if(xuan.containsKey("tupian") && xuan.get("tupian").toString().length()>0) {
						files.add(xuan.get("tupian").toString());
					}
				}
			}
			/*
			 * 数据库同步完成 处理多余文件
			 */

			if(map.containsKey("rid")==true) {
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
		} catch (IOException e) {
			e.printStackTrace();
 		   throw new RuntimeException(e.getMessage());
		}
		
		return hashMap;
	}
}
