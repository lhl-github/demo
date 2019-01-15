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
import com.xljy.kwld.dao.FollowText_di;
import com.xljy.kwld.dao.TextType_di;
import com.xljy.util.Const;

@Service
public class FollowText_sm {

	@Autowired
	private FollowText_di ft_di;
	@Autowired
	private TextType_di type_di;
	
	//两个查询，一个是查询全部的；一个是详细的查询
	public List<Map<String, Object>> findByFollowTextAll(Map parameterStringMap){
		
		return ft_di.findByFollowTextAll(parameterStringMap);
	}
	
	public Map<String, Object> findByFtid(Map parameterStringMap){
		List<Map<String, Object>> followText = ft_di.findByFollowTextAll(parameterStringMap);
		List<Map<String, Object>> followTextList = ft_di.findByFtid(parameterStringMap);
		followText.get(0).put("followTextList", followTextList);
		followText.get(0).put("size", followTextList.size());
		return followText.get(0);
	}
	
	//删除
	@Transactional
	public Map<String, Object> delFollowText(Map parameterStringMap){
		HashMap<String, Object> hashMap=new HashMap<>();
		int num1 = ft_di.delFollowText1(parameterStringMap);
		int num2 =ft_di.delFollowText2(parameterStringMap);
		if(num1>0) {
			hashMap.put("flag", "删除跟读课文名称成功");
		}
		if(num2>0) {
			hashMap.put("flag", "删除跟读课文成功");
		}
		
		return hashMap;
	}
	
	//添加  或    修改
	@Transactional
	public Map<String, Object> addFollowText(Map<String, String> parameterStringMap){
		Map<String, Object> paths=new HashMap<String, Object>();
		HashMap<String, Object> hashMap=new HashMap<String, Object>();//将要传给前端的数据
		Map map=null;//从前端得到的数据
		String json=parameterStringMap.get("json");//获取前端提供的json字符串json={jclx;xd,nj;sxc;dy;ftid;ftName;followTextList}
		ObjectMapper mapper=new ObjectMapper();//通过objectMapper把json字符串转成map
		ArrayList<String> files = new ArrayList<>();// 保存文件名的集合
		boolean ftid = false;
		try {
			map=mapper.readValue(json, Map.class);//map中存在前端传的数据
			
			List<Map> followTextList = (List<Map>) map.get("followTextList");
			
			//判断是添加还是修改
			if(map.containsKey("ftid") == true) {//修改
				paths = ft_di.findByFtidByFt(map);
				ftid = true;
			}else {
				paths = map;
			}
			
			//判断   课文单元   是否存在
			List<Map<String, Object>> typeList=type_di.findByFive(paths);
			if(typeList == null || typeList.isEmpty()) {
				hashMap.put("type", UUID.randomUUID().toString());
				map.put("type", hashMap.get("type"));
				//添加  课文单元
				int add_texttype= type_di.addTextType(map);
				hashMap.put("创建课文单元个数", add_texttype);
			}else if(typeList.size() >1) {
				throw new RuntimeException("发现多条课文单元数据 ===>>>无法确定加入到哪个课文单元!");
			}else {
				hashMap.put("type", typeList.get(0).get("type"));
			}
			
			map.put("type", hashMap.get("type"));
			//判断是否存在ftid   存在修改   不存在添加
			if(map.containsKey("ftid") == false) {
				//添加跟读课文
				hashMap.put("ftid", UUID.randomUUID().toString());
				map.put("ftid", hashMap.get("ftid"));
				int addFt = ft_di.addFollowText1(map);
				
			}else {
				//修改跟读课文
				hashMap.put("ftid", map.get("ftid"));
				int num = ft_di.updateFollowText1(map);
				//删除跟读课文下的所有句子课文
				ft_di.delFollowText2(map);
			}
			  //save_path :生成方法 >>>>>> :教材类型代码_学段代码_年级代码_上下册代码_单元代码_学校代码/课文类别
			  StringBuffer Buffer_save_path = new StringBuffer();
			  Buffer_save_path.append(paths.get("jclx") + "_");
			  Buffer_save_path.append(paths.get("xd") + "_");
			  Buffer_save_path.append(paths.get("nj") + "_" );
			  Buffer_save_path.append(paths.get("sxc") + "_");
			  Buffer_save_path.append(paths.get("dy"));
			  hashMap.put("save_path", Buffer_save_path.toString() + "/" + Const.FOLLOWTEXT+hashMap.get("ftid")+"/");
			  
			  for (int i = 0; i < followTextList.size(); i++) {
				Map ftOne = followTextList.get(i);
				
				//加入文件集合
				if(ftOne.containsKey("followText_video") && ftOne.get("followText_video").toString().length()>0) {
					files.add(ftOne.get("followText_video").toString());
				}
				
				ftOne.put("ftid", hashMap.get("ftid"));
				//增加
				hashMap.put("id", UUID.randomUUID().toString());
				ftOne.put("id", hashMap.get("id"));
				ft_di.addFollowText2(ftOne);
			}
			  
				/*
				 * 数据库同步完成 处理多余文件
				 */

			  if(ftid==true) {
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
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} 
		
	}
}
