package com.xljy.kwld.servers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xljy.kwld.dao.TingLi_di;
import com.xljy.util.Const;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Editor;
import cn.hutool.core.util.ArrayUtil;

@Service
public class TingLi_sm {
	@Autowired
	TingLi_di tingli_di;

	public List<Map<String, Object>> findall(Map<String, String> parameterStringMap) {

		return tingli_di.findall(parameterStringMap);
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
		List<Map<String, Object>> find_kewendanyuan = tingli_di.find_kewendanyuan(map);
		// System.out.println(find_kewendanyuan);

		if (find_kewendanyuan.size() == 0) {
			// 不存课文单元先创建
			hashMap.put("uuid_kewendanyuan", UUID.randomUUID().toString());// 获取课文单元uuid
			int add_kewendanyuan = tingli_di.add_kewendanyuan(hashMap.get("uuid_kewendanyuan"), map);

			hashMap.put("uuid_tingli", UUID.randomUUID().toString());// 生成这个听力的uuid
			hashMap.put("创建课文单元个数", add_kewendanyuan);
		} else if (find_kewendanyuan.size() > 1) {
			throw new RuntimeException("发现多条课文单元数据 ===>>>无法确定加入到哪个课文单元!");
		} else {
			// 存在课文单元
			hashMap.put("uuid_kewendanyuan", find_kewendanyuan.get(0).get("kewen_id"));// 获取课文单元uuid
			hashMap.put("uuid_tingli", UUID.randomUUID().toString());// 生成这个听力的uuid

		}

		// save_path :生成方法 >>>>>> :教材类型代码_学段代码_年级代码_上下册代码/课文类别/听力uuid/
		StringBuffer Buffer_save_path = new StringBuffer();
		Buffer_save_path.append(map.get("jiaocaileixing_d") + "_");
		Buffer_save_path.append(map.get("xueduan_d") + "_");
		Buffer_save_path.append(map.get("nianji_d") + "_");
		Buffer_save_path.append(map.get("shangxiace_d") + "_");
		Buffer_save_path.append(map.get("danyuan_d"));

		// Buffer_save_path.append(hashMap.get("uuid_kewendanyuan"));
		hashMap.put("save_path", Buffer_save_path.toString() + "/" + Const.TINGLI + hashMap.get("uuid_tingli") + "/");

		if (map.get("tingli_type_d").equals("1")) {

			// 增加听力 1 //听力选择题
			// Map tingli = (Map) map.get("");//取到听力
			Map tingli = map;// 取到听力

			// do add_tingli
			int add_tingli = tingli_di.add_tingli(hashMap.get("uuid_tingli"), hashMap.get("uuid_kewendanyuan"), tingli);//

			hashMap.put("创建听力个数", add_tingli);
			List<Map> list_tingliwenti = (List<Map>) tingli.get("list_wenti");// 获取这个听力的问题列表

			// 增加听力问题
			hashMap.put("创建听力问题个数", list_tingliwenti.size());
			for (int j = 0; j < list_tingliwenti.size(); j++) {

				Map tingli_wenti = list_tingliwenti.get(j);// 取到听力问题
				hashMap.put("uuid_wenti" + j, UUID.randomUUID().toString());// 生成这个听力的问题的uuid

				// do add_tingli_wenti (听力id,问题id,听力问题)
				tingli_di.add_wenti(hashMap.get("uuid_wenti" + j), hashMap.get("uuid_tingli"), map.get("timudalei"), tingli_wenti);

				List<Map> list_tingliwentixuanxiang = (List<Map>) tingli_wenti.get("list_wentixuanxiang");// 获取这个听力的问题的选项列表

				// 增加问题对应的选项
				// hashMap.put("创建听力问题选项个数", list_tingliwentixuanxiang.size());
				for (int k = 0; k < list_tingliwentixuanxiang.size(); k++) {

					Map tingli_wentixuanxiang = list_tingliwentixuanxiang.get(k);// 取到听力问题的选项
					// hashMap.put("uuid_tingli_wenti_xuanxiang" + k,
					// UUID.randomUUID().toString());// 生成这个听力的问题的选项的uuid

					// do add_tingli_wenti (听力id,问题id,听力问题)
					tingli_di.add_wenti_xuanxiang(hashMap.get("uuid_wenti" + j), tingli_wentixuanxiang);

				}
			}

		} else if (map.get("tingli_type_d").equals("2")) {
			// 听力天空题
			Map tingli = map;// 取到听力
			// hashMap.put("uuid_tingli", UUID.randomUUID().toString());//
			// 生成这个听力的uuid

			// do add_tingli
			int add_tingli = tingli_di.add_tingli(hashMap.get("uuid_tingli"), hashMap.get("uuid_kewendanyuan"), tingli);//
			hashMap.put("创建听力个数", add_tingli);

			List<Map> list_tingliwenti = (List<Map>) tingli.get("list_wenti");// 获取这个听力的问题列表

			// 增加听力问题
			hashMap.put("创建听力问题个数", list_tingliwenti.size());
			for (int j = 0; j < list_tingliwenti.size(); j++) {
				Map tingli_wenti = list_tingliwenti.get(j);// 取到听力问题
				hashMap.put("uuid_wenti" + j, UUID.randomUUID().toString());// 生成这个听力的问题的uuid
				// do add_tingli_wenti (听力id,问题id,听力问题)
				tingli_di.add_wenti(hashMap.get("uuid_wenti" + j), hashMap.get("uuid_tingli"), map.get("timudalei"), tingli_wenti);
			}

		}
		return hashMap;
	}

	@Transactional
	public Map<String, Object> xiugai(Map<String, String> parameterStringMap) throws RuntimeException {
		HashMap<String, Object> hashMap = new HashMap<>();
		String json = parameterStringMap.get("json");
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<String> files = new ArrayList<>();// 保存文件名的集合
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

		// 检查该听力的 课文单元 是否存在
		List<Map<String, Object>> find_kewendanyuan = null;
		if (map.containsKey("tingli_id")) {
			// 检查该听力的 课文单元 是否存在
			find_kewendanyuan = tingli_di.find_kewendanyuan_uuid(map);// 查课文单元表
		} else {
			// 检查该听力的 课文单元 是否存在
			find_kewendanyuan = tingli_di.find_kewendanyuan(map);// 课文单元表
			// System.out.println(find_kewendanyuan);
		}

		if (find_kewendanyuan.size() == 0) {// 没找到
			throw new RuntimeException("未发现课文单元数据 ===>>>无法确定修改哪个课文单元!");
		} else if (find_kewendanyuan.size() > 1) {
			throw new RuntimeException("发现多条课文单元数据 ===>>>无法确定修改哪个课文单元!");
		} else {// 正常
			Map<String, Object> map2 = find_kewendanyuan.get(0);
			hashMap.put("uuid_kewendanyuan", map2.get("kewen_id"));// 获取课文单元uuid
			hashMap.put("uuid_tingli", map.get("tingli_id"));// 获取听力uuid
			// save_path :生成方法 >>>>>> :教材类型代码_学段代码_年级代码_上下册代码_单元代码/课文类别
			StringBuffer Buffer_save_path = new StringBuffer();
			Buffer_save_path.append(map2.get("jiaocaileixing_d") + "_");
			Buffer_save_path.append(map2.get("xueduan_d") + "_");
			Buffer_save_path.append(map2.get("nianji_d") + "_");
			Buffer_save_path.append(map2.get("shangxiace_d") + "_");
			Buffer_save_path.append(map2.get("danyuan_d"));
			// hashMap.put("save_path", Buffer_save_path.toString() + "/" +
			// Const.TINGLI);
			hashMap.put("save_path", Buffer_save_path.toString() + "/" + Const.TINGLI + hashMap.get("uuid_tingli") + "/");
		}

		// List<Map> list_tingli = (List<Map>) map.get("");// 取到听力列表
		// hashMap.put("创建听力个数", list_tingli.size());
		// 修改听力

		Map tingli = map;// 取到听力
		// hashMap.put("uuid_tingli"+i,
		// UUID.randomUUID().toString());//生成这个听力的uuid
		if (map.get("tingli_type_d").equals("1")) {// 听选题
			// do xiugai_tingli

			tingli_di.xiugai_tingli(tingli);//

			// 加入文件集合
			if (tingli.containsKey("tingli_yinpin") && tingli.get("tingli_yinpin").toString().length() > 0) {
				files.add(tingli.get("tingli_yinpin").toString());
			}
			// 2 删除听力下的选项
			tingli_di.del_xuanxuang_all(hashMap.get("uuid_tingli").toString());
			// 3 删除听力下的问题
			tingli_di.del_wenti_all(hashMap.get("uuid_tingli").toString());

			/*
			 * List<Map> list_tingliwenti = (List<Map>)
			 * tingli.get("list_wenti");//
			 * 获取这个听力的问题列表
			 * 
			 * // 修改听力问题 // hashMap.put("修改听力问题个数", list_tingliwenti.size());
			 * for (int j =
			 * 0; j < list_tingliwenti.size(); j++) {
			 * 
			 * Map tingli_wenti = list_tingliwenti.get(j);// 取到听力问题
			 * 
			 * // 判断问题的修改方式 if (tingli_wenti.get("wenti_id") == null ||
			 * tingli_wenti.get("wenti_id").toString().isEmpty()) {// 增加
			 * hashMap.put("uuid_wenti" + j, UUID.randomUUID().toString());//
			 * 生成这个听力的问题的uuid
			 * // do add_tingli_wenti (听力id,问题id,听力问题)
			 * tingli_di.add_wenti(hashMap.get("uuid_wenti" + j),
			 * tingli.get("tingli_id"),
			 * map.get("timudalei"), tingli_wenti); } else if
			 * (StringUtils.isNotEmpty(tingli_wenti.get("wenti_id").toString())
			 * &&
			 * tingli_wenti.size() > 1) {// 修改
			 * tingli_di.xiugai_wenti(tingli_wenti); } else
			 * if
			 * (StringUtils.isNotEmpty(tingli_wenti.get("wenti_id").toString())
			 * &&
			 * tingli_wenti.size() == 1) {// 删除
			 * tingli_di.del_wenti(tingli_wenti); //
			 * 删除问题下所有的选项 tingli_di.del_wenti_xuanxiang_all(tingli_wenti);
			 * 
			 * } // hashMap.put("uuid_tingli_wenti"+j, //
			 * UUID.randomUUID().toString());//生成这个听力的问题的uuid
			 * 
			 * // do add_tingli_wenti (听力id,问题id,听力问题)
			 * 
			 * List<Map> list_wentixuanxiang = (List<Map>)
			 * tingli_wenti.get("list_wentixuanxiang");// 获取这个听力的问题的选项列表
			 * 
			 * // 修改问题对应的选项 // hashMap.put("修改听力问题选项个数",
			 * list_tingliwentixuanxiang.size());
			 * for (int k = 0; k < list_wentixuanxiang.size(); k++) {
			 * 
			 * Map wentixuanxiang = list_wentixuanxiang.get(k);// 取到听力问题的选项
			 * 
			 * // do add_tingli_wenti (听力id,问题id,听力问题) // 判断问题的修改方式 if
			 * (wentixuanxiang.get("xuanxiang_id") == null ||
			 * wentixuanxiang.get("xuanxiang_id").toString().isEmpty()) {// 增加
			 * 
			 * tingli_di.add_wenti_xuanxiang(tingli_wenti.get("wenti_id"),
			 * wentixuanxiang);
			 * } else if
			 * (StringUtils.isNotEmpty(wentixuanxiang.get("xuanxiang_id").
			 * toString()) &&
			 * wentixuanxiang.size() > 1) {// 修改
			 * tingli_di.xiugai_wenti_xuanxiang(wentixuanxiang); } else if
			 * (StringUtils.isNotEmpty(wentixuanxiang.get("xuanxiang_id").
			 * toString()) &&
			 * wentixuanxiang.size() == 1) {// 删除
			 * tingli_di.del_wenti_xuanxiang(wentixuanxiang); }
			 * 
			 * } }
			 */

			List<Map> list_tingliwenti = (List<Map>) tingli.get("list_wenti");// 获取这个听力的问题列表

			// 增加听力问题
			hashMap.put("创建听力问题个数", list_tingliwenti.size());
			for (int j = 0; j < list_tingliwenti.size(); j++) {

				Map tingli_wenti = list_tingliwenti.get(j);// 取到听力问题
				hashMap.put("uuid_wenti" + j, UUID.randomUUID().toString());// 生成这个听力的问题的uuid

				// do add_tingli_wenti (听力id,问题id,听力问题)
				tingli_di.add_wenti(hashMap.get("uuid_wenti" + j), hashMap.get("uuid_tingli"), map.get("timudalei"), tingli_wenti);

				// 加入文件集合
				if (tingli_wenti.containsKey("wenti_yinpin") && tingli_wenti.get("wenti_yinpin").toString().length() > 0) {
					files.add(tingli_wenti.get("wenti_yinpin").toString());
				}

				List<Map> list_tingliwentixuanxiang = (List<Map>) tingli_wenti.get("list_wentixuanxiang");// 获取这个听力的问题的选项列表

				// 增加问题对应的选项
				hashMap.put("创建听力问题选项个数", list_tingliwentixuanxiang.size());
				for (int k = 0; k < list_tingliwentixuanxiang.size(); k++) {

					Map tingli_wentixuanxiang = list_tingliwentixuanxiang.get(k);// 取到听力问题的选项
					// hashMap.put("uuid_tingli_wenti_xuanxiang" + k,
					// UUID.randomUUID().toString());// 生成这个听力的问题的选项的uuid

					// do add_tingli_wenti (听力id,问题id,听力问题)
					tingli_di.add_wenti_xuanxiang(hashMap.get("uuid_wenti" + j), tingli_wentixuanxiang);

					// 加入文件集合
					if (tingli_wentixuanxiang.containsKey("xuanxiang_yinpin") && tingli_wentixuanxiang.get("xuanxiang_yinpin").toString().length() > 0) {
						files.add(tingli_wentixuanxiang.get("xuanxiang_yinpin").toString());
					}
					// 加入文件集合
					if (tingli_wentixuanxiang.containsKey("xuanxiang_tupian") && tingli_wentixuanxiang.get("xuanxiang_tupian").toString().length() > 0) {
						files.add(tingli_wentixuanxiang.get("xuanxiang_tupian").toString());
					}

				}
			}

		} else if (map.get("tingli_type_d").equals("2")) {// 听填题
			// do xiugai_tingli
			// 1 修改听力
			tingli_di.xiugai_tingli(tingli);//

			// 加入文件集合
			if (tingli.containsKey("tingli_yinpin") && tingli.get("tingli_yinpin").toString().length() > 0) {
				files.add(tingli.get("tingli_yinpin").toString());
			}

			// 2 删除听力下的选项
			tingli_di.del_xuanxuang_all(hashMap.get("uuid_tingli").toString());
			// 3 删除听力下的问题
			tingli_di.del_wenti_all(hashMap.get("uuid_tingli").toString());

			/*
			 * List<Map> list_tingliwenti = (List<Map>)
			 * tingli.get("list_wenti");//
			 * 获取这个听力的问题列表
			 * 
			 * // 修改听力问题 // hashMap.put("修改听力问题个数", list_tingliwenti.size());
			 * for (int j =
			 * 0; j < list_tingliwenti.size(); j++) {
			 * 
			 * Map tingli_wenti = list_tingliwenti.get(j);// 取到听力问题
			 * 
			 * // 判断问题的修改方式 if (tingli_wenti.get("wenti_id") == null ||
			 * tingli_wenti.get("wenti_id").toString().isEmpty()) {// 增加
			 * hashMap.put("uuid_wenti" + j, UUID.randomUUID().toString());//
			 * 生成这个听力的问题的uuid
			 * // do add_tingli_wenti (听力id,问题id,听力问题)
			 * tingli_di.add_wenti(hashMap.get("uuid_wenti" + j),
			 * tingli.get("tingli_id"),
			 * map.get("timudalei"), tingli_wenti); } else if
			 * (StringUtils.isNotEmpty(tingli_wenti.get("wenti_id").toString())
			 * &&
			 * tingli_wenti.size() > 1) {// 修改
			 * tingli_di.xiugai_wenti(tingli_wenti); } else
			 * if
			 * (StringUtils.isNotEmpty(tingli_wenti.get("wenti_id").toString())
			 * &&
			 * tingli_wenti.size() == 1) {// 删除
			 * 
			 * tingli_di.del_wenti(tingli_wenti);
			 * 
			 * }
			 * 
			 * }
			 */

			List<Map> list_tingliwenti = (List<Map>) tingli.get("list_wenti");// 获取这个听力的问题列表

			// 增加听力问题
			hashMap.put("创建听力问题个数", list_tingliwenti.size());
			for (int j = 0; j < list_tingliwenti.size(); j++) {
				Map tingli_wenti = list_tingliwenti.get(j);// 取到听力问题
				hashMap.put("uuid_wenti" + j, UUID.randomUUID().toString());// 生成这个听力的问题的uuid
				// do add_tingli_wenti (听力id,问题id,听力问题)
				tingli_di.add_wenti(hashMap.get("uuid_wenti" + j), hashMap.get("uuid_tingli"), map.get("timudalei"), tingli_wenti);

				// 加入文件集合
				if (tingli_wenti.containsKey("wenti_yinpin") && tingli_wenti.get("wenti_yinpin").toString().length() > 0) {
					files.add(tingli_wenti.get("wenti_yinpin").toString());
				}
			}

		}

		/*
		 * 数据库同步完成 处理多余文件
		 */

		Object[] arr_files = files.toArray();

		String[] stringArray = ArrayUtils.toStringArray(arr_files);

		// 对stringArray分组 得到3个
		String[] arr_yinpin = Const.getyinpin(stringArray);// 得到音频的文件名集合
		String[] arr_tupian = Const.gettupian(stringArray);// 得到图片的文件名集合
		String[] arr_shipin = Const.getshipin(stringArray);// 得到视频的文件名集合

		/*System.out.println(ArrayUtils.toString(arr_yinpin));
		System.out.println(ArrayUtils.toString(arr_tupian));
		System.out.println(ArrayUtils.toString(arr_shipin));*/

		String yinpinfilePath = Const.getFilePath_Real(hashMap.get("save_path").toString(), "2");// 2音频
		String tupianfilePath = Const.getFilePath_Real(hashMap.get("save_path").toString(), "1");// 1图片
		String shipinfilePath = Const.getFilePath_Real(hashMap.get("save_path").toString(), "3");// 3视频

		Const.delfile_lujingxiabubaohan(hashMap, arr_yinpin, yinpinfilePath, "音频");
		Const.delfile_lujingxiabubaohan(hashMap, arr_tupian, tupianfilePath, "图片");
		Const.delfile_lujingxiabubaohan(hashMap, arr_shipin, shipinfilePath, "视频");

		return hashMap;
	}

	@Transactional
	public Map<String, Object> del(Map<String, String> parameterStringMap) {
		HashMap<String, Object> ret_map = new HashMap<>();// 要返回的结果集
		String ti_id = parameterStringMap.get("ti_id");
		Map<String, String> save = tingli_di.get_savepath(ti_id);// 路径
		// 先删子项 再删父项
		if (parameterStringMap.get("tingli_type_d").equals("1")) {
			tingli_di.del_xuanxuang_all(ti_id);
			tingli_di.del_wenti_all(ti_id);
			tingli_di.del_ti_tingli(ti_id);
		} else if (parameterStringMap.get("tingli_type_d").equals("2")) {
			tingli_di.del_wenti_all(ti_id);
			tingli_di.del_ti_tingli(ti_id);
		}

		if (save == null) {
			ret_map.put("msg", "数据已经删除");
			return ret_map;
		}

		String save_path = save.get("save") + "/" + Const.TINGLI + ti_id + "/";

		String filePath = Const.getFilePath_Real(save_path);
		// ret_map.put("save_path", filePath);
		boolean del = FileUtil.del(filePath);
		if (!del) {
			ret_map.put("msg", "文件路径不存在了");
		} else {
			ret_map.put("msg", "删除文件成功");
		}
		return ret_map;
	}

	public List<Map<String, Object>> Find_id(Map<String, String> parameterStringMap) {

		List<Map<String, Object>> findall = tingli_di.findall(parameterStringMap);
		if (findall.size() == 0) {
			return null;
		}
		// findall.get(0).put(key, value)
		List<Map<String, Object>> list_wenti = tingli_di.Find_allwenti(parameterStringMap);
		for (int i = 0; i < list_wenti.size(); i++) {
			list_wenti.get(i).put("list_wentixuanxiang", tingli_di.Find_allwentixuanxiang(list_wenti.get(i)));
		}
		findall.get(0).put("list_wenti", list_wenti);
		return findall;
	}
}
