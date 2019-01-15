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
import com.xljy.bean.Result;
import com.xljy.kwld.dao.Talk_di;
import com.xljy.kwld.dao.TextType_di;
import com.xljy.util.Const;

import cn.hutool.core.io.FileUtil;

@Service
public class Talk_sm {
	@Autowired
    private Talk_di talk_di;
	@Autowired
	private TextType_di type_di;
	
   //查询
	@Transactional
	public List< Map<String, Object>> findByTalkByTid(Map<String, String> parameterStringMap){
		
		return talk_di.findByTalkByTid(parameterStringMap);
	}
	
	//
	public Map<String, Object> findByTk(Map<String, String> parameterStringMap){
		List< Map<String, Object>> findAll =talk_di.findByTalkByTid(parameterStringMap);
		 List<Map<String, Object>> list=talk_di.findByTk(parameterStringMap);
		 
		 findAll.get(0).put("talk", list);
		 findAll.get(0).put("size", list.size());
		 return findAll.get(0);
	}
	//删除角色对话
	@Transactional
	public int delByJsTalk(Map<String, String> parameterStringMap) {
		
		return talk_di.delTalkById(parameterStringMap);
	}
	//删除对话dhid
	@Transactional
	public Map<String, Object> delByTalk(Map<String, String> parameterStringMap){
		HashMap<String, Object> ret_map=new HashMap<>();//将要返回给前端
		String dhid=parameterStringMap.get("dhid");
		Map<String, String> save = talk_di.get_savepath(dhid);
		//删除
		talk_di.delTalkName(parameterStringMap);
		talk_di.delTalk(parameterStringMap);
		
		if(save == null) {
			ret_map.put("msg", "数据已经删除");
			return ret_map;
		}
		String save_path = save.get("save") + "/" +Const.DUIHUA + dhid + "/";
		String filePath = Const.getFilePath_Real(save_path);
	
		boolean del = FileUtil.del(filePath);
		if(!del) {
			ret_map.put("msg", "文件路径不存在了");
		}else {
			ret_map.put("msg", "删除文件成功");
		}
		
		return ret_map;
		}
	
	//添加或修改对话
	@Transactional
	public Map<String, Object> addTalk(Map<String, String> parameterStringMap){
		HashMap<String, Object> hashMap=new HashMap<>();//将要传给前端的数据
		Map map=null;//从前端得到的数据
		String json=parameterStringMap.get("json");//获取前端提供的json字符串json={jclx;xd,nj;sxc;dy;talkList}
		ObjectMapper mapper=new ObjectMapper();//通过objectMapper把json字符串转成map
		ArrayList<String> files = new ArrayList<>();// 保存文件名的集合
		
		try {
			map=mapper.readValue(json, Map.class);//map中存在前端传的数据
			 Map talkList = (Map) map.get("talkList");//map中的talkList=｛dhid,dhname,photo,jsList｝
			List<Map> jsList=(List<Map>) talkList.get("jsList");//talkList中的jsList={jsname;zw;yw;video}
		
			//判断是添加还是修改
			if(map.containsKey("dhid")==true) {
				 map=talk_di.findByID(map);
			}
			//加入文件集合
			if(map.containsKey("photo") && map.get("photo").toString().length()>0) {
				files.add(map.get("photo").toString());
			}
			//判断  课文单元  是否存在
			 List<Map<String, Object>> typeList=type_di.findByFive(map);
			 if(typeList == null || typeList.isEmpty()) {
				 hashMap.put("type", UUID.randomUUID().toString());
				 map.put("type", hashMap.get("type"));
				 //添加 课文单元
				 int add_texttype= type_di.addTextType(map);
				 hashMap.put("创建课文单元个数", add_texttype); 		
			 }else if(typeList.size() >1) {
				 throw new RuntimeException("发现多条课文单元数据 ===>>>无法确定加入到哪个课文单元!");
			 }else {
				 hashMap.put("type", typeList.get(0).get("type"));
				 System.out.println(typeList.get(0).get("type"));
			 }
			 
             talkList.put("type", hashMap.get("type"));
			 //判断是否存在dhid 存在修改；不存在添加
			 if(map.containsKey("dhid") == false) {
				 //添加对话名称
				 hashMap.put("dhid", UUID.randomUUID().toString());
				 talkList.put("dhid", hashMap.get("dhid"));
				 int num= talk_di.addTalkName(talkList);
				 hashMap.put("flag", "对话名称添加成功");
			 }else {
				 //修改对话名称
				 hashMap.put("dhid", map.get("dhid"));
				 talkList.put("dhid", hashMap.get("dhid"));
				 int num = talk_di.updateTalkName(talkList);
				 //删除对话id下的所有角色
				 int count = talk_di.delTalk(talkList);
				 hashMap.put("flag", "对话名称修改成功");
			  }
			  
			  //save_path :生成方法 >>>>>> :教材类型代码_学段代码_年级代码_上下册代码_单元代码_学校代码/课文类别
			  StringBuffer Buffer_save_path = new StringBuffer();
			  Buffer_save_path.append(map.get("jclx") + "_");
			  Buffer_save_path.append(map.get("xd") + "_");
			  Buffer_save_path.append(map.get("nj") + "_" );
			  Buffer_save_path.append(map.get("sxc") + "_");
			  Buffer_save_path.append(map.get("dy"));
			  hashMap.put("save_path", Buffer_save_path.toString() + "/" + Const.DUIHUA+hashMap.get("dhid")+"/");
			  hashMap.put("dy_tupian", Buffer_save_path.toString() + "/");
			  
			 
			  for (int i = 0; i < jsList.size(); i++) {
				 Map js=jsList.get(i);
				 //加入文件集合
				 if(js.containsKey("video") && js.get("video").toString().length()>0) {
					 files.add(js.get("video").toString());
				 }
				 
				//判断是否存在此相关所有内容
				 js.put("type", hashMap.get("type"));
				 js.put("dhid", hashMap.get("dhid"));
                 //增加
				 hashMap.put("id", UUID.randomUUID().toString());
				 js.put("id", hashMap.get("id"));
			     int num=talk_di.addTalk(js);
				 hashMap.put("flag", "角色"+i+"添加成功");
			  }
			  
				/*
				 * 数据库同步完成 处理多余文件
				 */

			  if(map.containsKey("dhid")==true) {
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
				
			return hashMap;
		} catch (Exception e1) {
			e1.printStackTrace();
	   		throw new RuntimeException(e1.getMessage());
		}
		
	}
	
	}
	
