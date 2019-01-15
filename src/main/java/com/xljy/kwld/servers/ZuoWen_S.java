package com.xljy.kwld.servers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xljy.kwld.dao.ZuoWen_D;
import com.xljy.util.Const;

import cn.hutool.core.io.FileUtil;

@Service
public class ZuoWen_S {
	@Autowired
	ZuoWen_D zuowen_d;

	public List<Map<String, Object>> findall(Map<String, String> parameterStringMap) {

		return zuowen_d.findall(parameterStringMap);
	}

	@Transactional
	public Map<String, Object> add(Map<String, String> parameterStringMap) throws RuntimeException {
		HashMap<String, Object> hashMap = new HashMap<>();// 要返回的结果集
		String json = parameterStringMap.get("json");
		ObjectMapper mapper = new ObjectMapper();
		Map map = null;// 获取到的数据
		try {
			map = mapper.readValue(json, Map.class);
		} catch (JsonParseException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		} catch (IOException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		}

		// System.out.println(map);

		// 检查该听力的 课文单元 是否存在
		List<Map<String, Object>> find_kewendanyuan = zuowen_d.find_kewendanyuan(map);
		// System.out.println(find_kewendanyuan);

		if (find_kewendanyuan.size() == 0) {
			// 不存先创建
			hashMap.put("uuid_kewendanyuan", UUID.randomUUID().toString());// 获取课文单元uuid
			int add_kewendanyuan = zuowen_d.add_kewendanyuan(hashMap.get("uuid_kewendanyuan"), map);

			hashMap.put("uuid_zuowen", UUID.randomUUID().toString());// 生成这个作文的uuid
			hashMap.put("创建课文单元个数", add_kewendanyuan);
		} else if (find_kewendanyuan.size() > 1) {
			throw new RuntimeException("发现多条课文单元数据 ===>>>无法确定加入到哪个课文单元!");
		} else {
			hashMap.put("uuid_kewendanyuan", find_kewendanyuan.get(0).get("kewen_id"));// 获取课文单元uuid
			hashMap.put("uuid_zuowen", UUID.randomUUID().toString());// 生成这个作文的uuid

		}

		// save_path :生成方法 >>>>>> :教材类型代码_学段代码_年级代码_上下册代码/课文类别/作文uuid/
		StringBuffer Buffer_save_path = new StringBuffer();
		Buffer_save_path.append(map.get("jiaocaileixing_d") + "_");
		Buffer_save_path.append(map.get("xueduan_d") + "_");
		Buffer_save_path.append(map.get("nianji_d") + "_");
		Buffer_save_path.append(map.get("shangxiace_d") + "_");
		Buffer_save_path.append(map.get("danyuan_d"));

		// Buffer_save_path.append(hashMap.get("uuid_kewendanyuan"));
		hashMap.put("save_path", Buffer_save_path.toString() + "/" + Const.ZUOWEN + hashMap.get("uuid_zuowen") + "/");

		Map zuowen = map;// 取到作文

		// do add_作文
		int add_zuowen = zuowen_d.add_zuowen(hashMap.get("uuid_zuowen"), hashMap.get("uuid_kewendanyuan"), zuowen);//

		hashMap.put("创建作文个数", add_zuowen);

		List<Map> list_yaodian = (List<Map>) zuowen.get("list_yaodian");// 获取这个作文的要点列表
		List<Map> list_zhuyidian = (List<Map>) zuowen.get("list_zhuyidian");// 获取这个作文的要点列表

		// 增加作文要点
		hashMap.put("创建作文要点个数", list_yaodian.size());
		for (int j = 0; j < list_yaodian.size(); j++) {

			Map zuowen_yaodian = list_yaodian.get(j);// 取到作文要点
			zuowen_d.add_yaodian(hashMap.get("uuid_zuowen"), zuowen_yaodian);

		} // end for yaodian

		// 增加作文注意点
		hashMap.put("创建作文注意点个数", list_zhuyidian.size());
		for (int j = 0; j < list_zhuyidian.size(); j++) {

			Map zuowen_zhuyidian = list_zhuyidian.get(j);// 取到作文注意点
			// do add_zhuyidian (作文id,注意点)
			zuowen_d.add_zhuyidian(hashMap.get("uuid_zuowen"), zuowen_zhuyidian);

		} // end for yaodian
		return hashMap;
	}

	@Transactional
	public Map<String, Object> xiugai(Map<String, String> parameterStringMap) throws RuntimeException {
		HashMap<String, Object> hashMap = new HashMap<>();
		String json = parameterStringMap.get("json");
		ObjectMapper mapper = new ObjectMapper();
		Map map = null;
		try {
			map = mapper.readValue(json, Map.class);
		} catch (JsonParseException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		} catch (IOException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1.getMessage());
		}

		// System.out.println(map);
		List<Map<String, Object>> find_kewendanyuan = null;
		if (map.containsKey("zuowen_id")) {//
			// 检查该听力的 课文单元 是否存在
			find_kewendanyuan = zuowen_d.find_kewendanyuan_uuid(map);// 查作文表
		} else {
			// 检查该听力的 课文单元 是否存在
			find_kewendanyuan = zuowen_d.find_kewendanyuan(map);// 课文单元表
			// System.out.println(find_kewendanyuan);
		}

		if (find_kewendanyuan.size() == 0) {// 没找到
			throw new RuntimeException("未发现课文单元数据 ===>>>无法确定修改哪个课文单元!");
		} else if (find_kewendanyuan.size() > 1) {
			throw new RuntimeException("发现多条课文单元数据 ===>>>无法确定修改哪个课文单元!");
		} else {// 正常
			Map<String, Object> map2 = find_kewendanyuan.get(0);
			hashMap.put("uuid_kewendanyuan", map2.get("kewen_id"));// 获取课文单元uuid
			hashMap.put("uuid_zuowen", map.get("zuowen_id"));// 获取作文uuid
			// save_path :生成方法 >>>>>> :教材类型代码_学段代码_年级代码_上下册代码_单元代码_学校代码/课文类别
			StringBuffer Buffer_save_path = new StringBuffer();
			Buffer_save_path.append(map2.get("jiaocaileixing_d") + "_");
			Buffer_save_path.append(map2.get("xueduan_d") + "_");
			Buffer_save_path.append(map2.get("nianji_d") + "_");
			Buffer_save_path.append(map2.get("shangxiace_d") + "_");
			Buffer_save_path.append(map2.get("danyuan_d"));
			// Buffer_save_path.append(hashMap.get("uuid_kewendanyuan"));
			hashMap.put("save_path",
					Buffer_save_path.toString() + "/" + Const.ZUOWEN + hashMap.get("uuid_zuowen") + "/");
		}

		// 修改作文

		Map zuowen = map;// 取到作文

		// do xiugai_tingli
		zuowen_d.xiugai_zuowen(zuowen);//
		ArrayList<String> files = new ArrayList<>();// 保存文件名的集合

		// 加入文件集合
		if (zuowen.containsKey("zuowen_tupian") && zuowen.get("zuowen_tupian").toString().length() > 0) {
			files.add(zuowen.get("zuowen_tupian").toString());
		}

		List<Map> list_yaodian = (List<Map>) zuowen.get("list_yaodian");// 获取这个作文的要点的列表
		List<Map> list_zhuyidian = (List<Map>) zuowen.get("list_zhuyidian");// 获取这个作文的注意点的列表

		zuowen_d.del_yaodian_zuowenid(zuowen.get("zuowen_id").toString());
		zuowen_d.del_zhuyidian_zuowenid(zuowen.get("zuowen_id").toString());

		// 修改作文要点
		/*
		 * // hashMap.put("修改作文要点个数", list_tingliwenti.size()); for (int j = 0; j <
		 * list_yaodian.size(); j++) {
		 * 
		 * Map zuowen_yaodian = list_yaodian.get(j);// 取到作文要点
		 * 
		 * // 判断要点的修改方式 if (zuowen_yaodian.get("yaodian_id") == null ||
		 * zuowen_yaodian.get("yaodian_id").toString().isEmpty()) {// 增加
		 * zuowen_d.add_yaodian(zuowen.get("zuowen_id"), zuowen_yaodian);
		 * 
		 * } else if
		 * (StringUtils.isNotEmpty(zuowen_yaodian.get("yaodian_id").toString()) &&
		 * zuowen_yaodian.size() > 1) {// 修改 yaodian_id
		 * zuowen_d.xiugai_yaodian(zuowen_yaodian);
		 * 
		 * } else if
		 * (StringUtils.isNotEmpty(zuowen_yaodian.get("yaodian_id").toString()) &&
		 * zuowen_yaodian.size() == 1) {// 删除
		 * zuowen_d.del_yaodian(zuowen_yaodian.get("yaodian_id"));
		 * 
		 * }
		 * 
		 * } // end for 要点
		 * 
		 * // 修改作文注意点 for (int k = 0; k < list_zhuyidian.size(); k++) {
		 * 
		 * Map zuowen_zhuyidian = list_zhuyidian.get(k);// 取到作文注意点
		 * 
		 * // 判断注意点的修改方式 if (zuowen_zhuyidian.get("zhuyidian_id") == null ||
		 * zuowen_zhuyidian.get("zhuyidian_id").toString().isEmpty()) {// 增加
		 * zuowen_d.add_zhuyidian(zuowen.get("zuowen_id"), zuowen_zhuyidian); } else if
		 * (StringUtils.isNotEmpty(zuowen_zhuyidian.get("zhuyidian_id").toString()) &&
		 * zuowen_zhuyidian.size() > 1) {// 修改zhuyidian_id
		 * zuowen_d.xiugai_zhuyidian(zuowen_zhuyidian); } else if
		 * (StringUtils.isNotEmpty(zuowen_zhuyidian.get("zhuyidian_id").toString()) &&
		 * zuowen_zhuyidian.size() == 1) {// 删除
		 * zuowen_d.del_zhuyidian(zuowen_zhuyidian.get("zhuyidian_id")); }
		 * 
		 * } // end for 注意点
		 */

		// 增加作文要点
		hashMap.put("创建作文要点个数", list_yaodian.size());
		for (int j = 0; j < list_yaodian.size(); j++) {

			Map zuowen_yaodian = list_yaodian.get(j);// 取到作文要点
			zuowen_d.add_yaodian(hashMap.get("uuid_zuowen"), zuowen_yaodian);

		} // end for yaodian

		// 增加作文注意点
		hashMap.put("创建作文注意点个数", list_zhuyidian.size());
		for (int j = 0; j < list_zhuyidian.size(); j++) {

			Map zuowen_zhuyidian = list_zhuyidian.get(j);// 取到作文注意点
			// do add_zhuyidian (作文id,注意点)
			zuowen_d.add_zhuyidian(hashMap.get("uuid_zuowen"), zuowen_zhuyidian);

		} // end for yaodian

		String[] arr_files = (String[]) files.toArray();

		// String[] arr_yinpin = Const.getyinpin(arr_files);// 得到音频的文件名集合
		String[] arr_tupian = Const.gettupian(arr_files);// 得到图片的文件名集合
		// String[] arr_shipin = Const.getshipin(arr_files);// 得到视频的文件名集合

		// String yinpinfilePath =
		// Const.getFilePath_Real(hashMap.get("save_path").toString(), "2");// 2音频
		String tupianfilePath = Const.getFilePath_Real(hashMap.get("save_path").toString(), "1");// 1图片
		// String shipinfilePath =
		// Const.getFilePath_Real(hashMap.get("save_path").toString(), "3");// 3视频

		// Const.delfile_lujingxiabubaohan(hashMap, arr_yinpin, yinpinfilePath, "音频");
		Const.delfile_lujingxiabubaohan(hashMap, arr_tupian, tupianfilePath, "图片");
		// Const.delfile_lujingxiabubaohan(hashMap, arr_shipin, shipinfilePath, "视频");

		return hashMap;
	}

	@Transactional
	public Map<String, Object> del(Map<String, String> parameterStringMap) {
		String ti_id = parameterStringMap.get("zuowen_id");
		HashMap<String, Object> ret_map = new HashMap<>();// 要返回的结果集
		Map<String, String> save = zuowen_d.get_savepath(ti_id);//

		zuowen_d.del_yaodian_zuowenid(ti_id);
		zuowen_d.del_zhuyidian_zuowenid(ti_id);
		zuowen_d.del_zuowenid(ti_id);

		if (save == null) {
			ret_map.put("msg", "数据已经删除");
			return ret_map;
		}

		String save_path = save.get("save") + "/" + Const.ZUOWEN + ti_id + "/";

		String filePath = Const.getFilePath_Real(save_path);
		ret_map.put("save_path", filePath);
		boolean del = FileUtil.del(filePath);
		if (!del) {
			ret_map.put("msg", "文件路径不存在了");
		} else {
			ret_map.put("msg", "删除文件成功");
		}
		return ret_map;

	}

	public List<Map<String, Object>> Find_id(Map<String, String> parameterStringMap) {

		List<Map<String, Object>> findall = zuowen_d.findall(parameterStringMap);
		if (findall.size() == 0) {
			return null;
		}
		// findall.get(0).put(key, value)
		List<Map<String, Object>> list_yaodian = zuowen_d.find_allyaodian(parameterStringMap);
		List<Map<String, Object>> list_zhuyidian = zuowen_d.find_allzhuyidian(parameterStringMap);

		findall.get(0).put("list_yaodian", list_yaodian);
		findall.get(0).put("list_zhuyidian", list_zhuyidian);
		return findall;
	}
}
