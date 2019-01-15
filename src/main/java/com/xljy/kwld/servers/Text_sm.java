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
import com.xljy.kwld.dao.TextType_di;
import com.xljy.kwld.dao.Text_di;
import com.xljy.util.Const;

import cn.hutool.core.io.FileUtil;

@Service
public class Text_sm {
	@Autowired
	private Text_di text_di;
	@Autowired
	private TextType_di type_di;

	// 查询
	public List<Map<String, Object>> findAllText(Map parameterStringMap) {

		return text_di.findAllText(parameterStringMap);
	}

	// 删除
	@Transactional
	public Map<String, Object> delText(Map<String, String> parameterStringMap) {
		HashMap<String, Object> ret_map = new HashMap<>();// 要返回的结果集
		String id = parameterStringMap.get("id");
		Map<String, String> save = text_di.get_savepath(id);// 使用id判断是否存在路径
		// 删除课文
		text_di.delText(parameterStringMap);

		if (save == null) {// 如果不存在id，则不存在路径
			ret_map.put("msg", "数据已经删除");
			return ret_map;
		}

		String save_path = save.get("save") + "/" + Const.KEWEN + id + "/";// 保存路径

		String filePath = Const.getFilePath_Real(save_path);// 将此时的路径与之前存放在服务器的路径进行比对

		boolean del = FileUtil.del(filePath);// 如果服务器存在，则进行删除
		if (!del) {
			ret_map.put("msg", "文件路径不存在了");
		} else {
			ret_map.put("msg", "删除文件成功");
		}
		return ret_map;
	}

	// 修改、保存
	@Transactional
	public Map<String, Object> addText(Map<String, String> parameterStringMap) {
		HashMap<String, Object> hashMap = new HashMap<>();// 给前台传的数据
		Map map = null;// 用于接收前台传递给后台的数据
		String json = parameterStringMap.get("json");// json={nj;sxc;jclx;dy;xd;textList}
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<String> files = new ArrayList<>();// 保存文件名的集合
		try {
			map = mapper.readValue(json, Map.class);// 将json字符串转成map
			Map textList = (Map) map.get("textList");// 获取textList={textname;zw;yw;video;photo}
			// 判断是修改 还是 保存 在修改中id存在map中
			if (map.containsKey("id") == true) {
				map = text_di.findByID(map);// map={id;type}
				hashMap.put("id", map.get("id"));// 将id保存
			}

			// 判断是否存在 课文单元
			List<Map<String, Object>> typeList = type_di.findByFive(map);// 保存 map={nj;sxc;jclx;dy;xd;textList}
			if (typeList == null || typeList.isEmpty()) {
				hashMap.put("type", UUID.randomUUID().toString());
				map.put("type", hashMap.get("type"));
				type_di.addTextType(map); // 保存课文单元

			} else if (typeList.size() > 1) {
				throw new RuntimeException("发现多条课文单元数据 ===>>>无法确定加入到哪个课文单元!");
			} else {
				hashMap.put("type", typeList.get(0).get("type"));
			}
			// 取到课文
		
			textList.put("type", hashMap.get("type"));// 将type填写到textList中

			// 加入文件集合
			if (textList.containsKey("video") && textList.get("video").toString().length() > 0) {
				files.add(textList.get("video").toString());
			}
			if (textList.containsKey("photoes") && textList.get("photoes").toString().length() > 0) {
				files.add(textList.get("photoes").toString());
			}
			// 删除课文
			int count = text_di.delText(map);// 删除在map里面的id

			// 添加课文
			if(count>0) {
				hashMap.put("id", map.get("id"));
			}else {
				hashMap.put("id", UUID.randomUUID().toString());
			}
			textList.put("id", hashMap.get("id"));
			text_di.addText(textList);// 添加课文成功textList={textname;zw;yw;video;photo}

			hashMap.put("flag", "课文添加成功");

			// save_path :生成方法 >>>>>> :教材类型代码_学段代码_年级代码_上下册代码_单元代码_学校代码/课文类别
			StringBuffer Buffer_save_path = new StringBuffer();
			Buffer_save_path.append(map.get("jclx") + "_");
			Buffer_save_path.append(map.get("xd") + "_");
			Buffer_save_path.append(map.get("nj") + "_");
			Buffer_save_path.append(map.get("sxc") + "_");
			Buffer_save_path.append(map.get("dy"));
			hashMap.put("save_path", Buffer_save_path.toString() + "/" + Const.KEWEN + hashMap.get("id") + "/");
            hashMap.put("dy_tupian", Buffer_save_path.toString() + "/");
			/*
			 * 数据库同步完成 处理多余文件
			 */
			if (map.containsKey("id") == true) {
				
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
			

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return hashMap;
	}

}
