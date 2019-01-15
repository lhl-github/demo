package com.xljy.sys.servers;

import java.io.IOException;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import com.tencent.xinge.ClickAction;
import com.tencent.xinge.Message;
import com.xljy.sys.dao.User_di;
import com.xljy.util.Const;
import com.xljy.util.Pay_util;
import com.xljy.util.Push_ts;
import com.xljy.util.Yzm_util;
import com.xljy.util.ZhangHao;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;

@Service
public class User_sm {

	@Autowired
	User_di user_di;

	public List<Map<String, Object>> findall(Map<String, String> parameterStringMap) {
		List<Map<String, Object>> findall = user_di.findall(parameterStringMap);

		if (findall.size() == 0) {
			return findall;
		}

		if (parameterStringMap.get("usertype_code").equals("5")) {// 学生
			HashMap<String, Object> temp = new HashMap<>();
			for (int i = 0; i < findall.size(); i++) {

				/*
				 * String[] split =
				 * StringUtils.split(findall.get(i).get("user_banji_d").toString
				 * (), ",");// 班级的
				 * 
				 * ArrayList<Map<String, Object>> arr = new ArrayList<>(); for
				 * (int j = 0; j <
				 * split.length; j++) { temp.put("banji_d", split[j]);
				 * arr.add(temp); }
				 * 
				 * findall.get(i).put("list_banji", arr);
				 */
				String xuesheng_id = findall.get(i).get("id").toString();

				List<Map<String, Object>> findbanji4xuesheng = user_di.findbanji4xuesheng(xuesheng_id);
				findall.get(i).put("list_banji", findbanji4xuesheng);

			}
		} else if (parameterStringMap.get("usertype_code").equals("4")) {// 家长

		} else if (parameterStringMap.get("usertype_code").equals("2")) {// 老师

		}

		return findall;
	}

	// 根据用户ID进行查询用户
	public Map<String, String> findUserById(Map<String, String> parameterStringMap) {

		return user_di.findUserById(parameterStringMap);
	}

	// 添加用户
	@Transactional
	public Map<String, Object> addUser(Map<String, String> parameterStringMap) {
		Map<String, Object> Map_ret = new HashMap<>();
		int arm = 0;
		if (parameterStringMap.get("usertype_code").equals("5")) {// 学生
			// 验证存在
			List<Map<String, Object>> findall = user_di.findall_yz(parameterStringMap);
			List<Map<String, Object>> findall_zhanghao_yz = user_di.findall_zhanghao_yz(parameterStringMap);

			if (findall.size() > 0) {
				throw new RuntimeException("用户账号已经存在");
			}
			if (findall_zhanghao_yz.size() > 0) {
				throw new RuntimeException("该账号已经存在");
			}
			// 添加
			arm = user_di.addUser_xuesheng(parameterStringMap);
			Map_ret.put("msg0", "添加学生数" + arm + "人");
			// 加入班级
			if (parameterStringMap.containsKey("list_banji")) {
				String json_banji = parameterStringMap.get("list_banji");
				ObjectMapper mapper = new ObjectMapper();
				List<Map> list_banji = null;
				try {
					list_banji = mapper.readValue(json_banji, List.class);
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
				if (list_banji.size() > 0) {
					// do 加入班级
					// 验证班级存在
					for (int i = 0; i < list_banji.size(); i++) {
						Map banji = list_banji.get(i);
						List<Map<String, Object>> find_yz_banji = user_di.find_yz_banji(banji);// 验证班级存在
						if (find_yz_banji.size() == 0) {
							Map_ret.put("msg1", "异常班级代码:" + banji.get("banji_d").toString() + "不存在");
							throw new RuntimeException("班级代码:" + banji.get("banji_d").toString() + "不存在");
						} else if (find_yz_banji.size() > 1) {
							Map_ret.put("msg1", "异常班级代码:" + banji.get("banji_d").toString() + "存在多个班级");
							throw new RuntimeException("班级代码:" + banji.get("banji_d").toString() + "存在多个班级");
						} else {
							List<Map<String, Object>> findall_zhanghao_yz2 = user_di.findall_zhanghao_yz(parameterStringMap);// 学生的id
							Map<String, String> map = new HashMap<>();
							map.put("stu_id", findall_zhanghao_yz2.get(0).get("Id").toString());// 学生id
							map.put("banji_d", banji.get("banji_d").toString());// 班级代码
							user_di.xuesheng9banji(map);
						}

					}

					Map_ret.put("msg1", "加入班级数" + list_banji.size() + "条");
					// list_banji
				}
			}
		} else if (parameterStringMap.get("usertype_code").equals("4")) {// 家长
			// 验证存在
			List<Map<String, Object>> findall = user_di.findall_yz(parameterStringMap);
			List<Map<String, Object>> find_dianhua = user_di.findall_yz_dianhua(parameterStringMap);

			if (findall.size() > 0) {
				throw new RuntimeException("用户账号已经存在");
			}
			if (find_dianhua.size() > 0) {
				throw new RuntimeException("电话号已经注册");
			}
			// parameterStringMap.put("user_zhanghao",
			// UUID.randomUUID().toString() );
			arm = user_di.addUser_jiazhang(parameterStringMap);
			Map_ret.put("添加数量", arm);
		} else if (parameterStringMap.get("usertype_code").equals("3")) {// 教研员
			// 验证存在
			List<Map<String, Object>> findall = user_di.findall_yz(parameterStringMap);
			List<Map<String, Object>> find_dianhua = user_di.findall_yz_dianhua(parameterStringMap);

			if (findall.size() > 0) {
				throw new RuntimeException("用户账号已经存在");
			}
			if (find_dianhua.size() > 0) {
				throw new RuntimeException("电话号已经注册");
			}
			// parameterStringMap.put("user_zhanghao",
			// UUID.randomUUID().toString() );
			arm = user_di.addUser_jiaoyanyuan(parameterStringMap);
			Map_ret.put("添加数量", arm);
		} else if (parameterStringMap.get("usertype_code").equals("2")) {// 老师
			// 验证存在
			List<Map<String, Object>> findall = user_di.findall_yz(parameterStringMap);
			List<Map<String, Object>> find_dianhua = user_di.findall_yz_dianhua(parameterStringMap);

			if (findall.size() > 0) {
				throw new RuntimeException("用户账号已经存在");
			}
			if (find_dianhua.size() > 0) {
				throw new RuntimeException("电话号已经注册");
			}
			// parameterStringMap.put("user_zhanghao",
			// UUID.randomUUID().toString() );
			arm = user_di.addUser_laoshi(parameterStringMap);
			Map_ret.put("添加数量", arm);
			/*
			 * List<Map<String, Object>> findall2 =
			 * user_di.findall(parameterStringMap);
			 * 
			 * // 判断是否创建正常班级 if (parameterStringMap.containsKey("banji_json")) {
			 * 
			 * if (findall2.size() == 0) { throw new RuntimeException("创建用户失败");
			 * } else if
			 * (findall2.size() > 1) { throw new
			 * RuntimeException("创建班级时发现多个用户__失败"); }
			 * String banji_json = parameterStringMap.get("banji_json"); //
			 * json串 转list
			 * ObjectMapper mapper = new ObjectMapper(); List<Map> list_banji =
			 * null; try {
			 * list_banji = mapper.readValue(banji_json, List.class); } catch
			 * (JsonParseException e) { e.printStackTrace(); throw new
			 * RuntimeException(e.getMessage()); } catch (JsonMappingException
			 * e) {
			 * e.printStackTrace(); throw new RuntimeException(e.getMessage());
			 * } catch
			 * (IOException e) { e.printStackTrace(); throw new
			 * RuntimeException(e.getMessage()); }
			 * 
			 * String laishi_id = findall2.get(0).get("id").toString();// 获取老师id
			 * for (int i
			 * = 0; i < list_banji.size(); i++) { Map map_banji =
			 * list_banji.get(i);
			 * 
			 * map_banji.put("banji_d", ZhangHao.getZhangHaoUuid());// 生成班级代码
			 * map_banji.put("id", laishi_id);// 放入老师id map_banji.put("",
			 * laishi_id);//
			 * 放入老师id user_di.addbanji(map_banji); }
			 * 
			 * }
			 * 
			 * // 判断是否创建自定义班级 if
			 * (parameterStringMap.containsKey("banji_json_zdy")) {
			 * 
			 * if (findall2.size() == 0) { throw new RuntimeException("创建用户失败");
			 * } else if
			 * (findall2.size() > 1) { throw new
			 * RuntimeException("创建班级时发现多个用户__失败"); }
			 * String banji_json = parameterStringMap.get("banji_json_zdy"); //
			 * json串 转list
			 * ObjectMapper mapper = new ObjectMapper(); List<Map> list_banji =
			 * null; try {
			 * list_banji = mapper.readValue(banji_json, List.class); } catch
			 * (JsonParseException e) { e.printStackTrace(); throw new
			 * RuntimeException(e.getMessage()); } catch (JsonMappingException
			 * e) {
			 * e.printStackTrace(); throw new RuntimeException(e.getMessage());
			 * } catch
			 * (IOException e) { e.printStackTrace(); throw new
			 * RuntimeException(e.getMessage()); }
			 * 
			 * String laishi_id = findall2.get(0).get("id").toString();// 获取老师id
			 * for (int i
			 * = 0; i < list_banji.size(); i++) { Map map_banji =
			 * list_banji.get(i);
			 * map_banji.put("banji_d", ZhangHao.getZhangHaoUuid());// 生成班级代码
			 * map_banji.put("id", laishi_id);// 放入老师id
			 * user_di.addbanji(map_banji); }
			 * 
			 * }
			 */

		} else if (parameterStringMap.get("usertype_code").equals("1")) {// 校长
			// 验证存在
			List<Map<String, Object>> findall = user_di.findall_yz(parameterStringMap);
			List<Map<String, Object>> find_dianhua = user_di.findall_yz_dianhua(parameterStringMap);

			if (findall.size() > 0) {
				throw new RuntimeException("用户账号已经存在");
			}
			if (find_dianhua.size() > 0) {
				throw new RuntimeException("电话号已经注册");
			}
			// parameterStringMap.put("user_zhanghao",
			// UUID.randomUUID().toString() );
			arm = user_di.addUser_xiaozhang(parameterStringMap);
			Map_ret.put("添加数量", arm);
		} else if (parameterStringMap.get("usertype_code").equals("0")) {// 管理员
			// 验证存在
			List<Map<String, Object>> findall = user_di.findall_yz(parameterStringMap);
			List<Map<String, Object>> find_dianhua = user_di.findall_yz_dianhua(parameterStringMap);
			List<Map<String, Object>> findall_zhanghao_yz = user_di.findall_zhanghao_yz(parameterStringMap);

			if (findall.size() > 0) {
				throw new RuntimeException("用户账号已经存在");
			}
			if (find_dianhua.size() > 0) {
				throw new RuntimeException("电话号已经注册");
			}
			if (findall_zhanghao_yz.size() > 0) {
				throw new RuntimeException("账号已存在");
			}

			// parameterStringMap.put("user_zhanghao",
			// UUID.randomUUID().toString() );
			arm = user_di.addUser_guanliyuan(parameterStringMap);
			Map_ret.put("添加数量", arm);
		}

		// 添加用户的默认角色
		// user_di.addUser_juese(
		// user_di.findall_yz(parameterStringMap).get(0).get("Id"),parameterStringMap.get("usertype_code"));

		if (arm == 0) {
			throw new RuntimeException("添加用户失败");
		}
		return Map_ret;
	}

	// 修改用户
	@Transactional
	public Map<String, Object> updateUser(Map<String, String> parameterStringMap) {
		Map<String, Object> Map_ret = new HashMap<>();
		int arm = 0;
		if (parameterStringMap.get("usertype_code").equals("5")) {// 学生
			arm = user_di.updateUser(parameterStringMap);

			// 加入班级
			if (parameterStringMap.containsKey("list_banji")) {
				// 删除学生的全部班级关联
				user_di.delbanji4xuesheng(parameterStringMap.get("id").toString());

				String json_banji = parameterStringMap.get("list_banji");
				ObjectMapper mapper = new ObjectMapper();
				List<Map> list_banji = null;
				try {
					list_banji = mapper.readValue(json_banji, List.class);
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
				if (list_banji.size() > 0) {
					// do 加入班级
					// 验证班级存在
					for (int i = 0; i < list_banji.size(); i++) {
						Map banji = list_banji.get(i);
						List<Map<String, Object>> find_yz_banji = user_di.find_yz_banji(banji);// 验证班级存在
						if (find_yz_banji.size() == 0) {
							Map_ret.put("异常", "班级代码:" + banji.get("banji_d").toString() + "不存在");
							throw new RuntimeException("班级代码:" + banji.get("banji_d").toString() + "不存在");
						} else if (find_yz_banji.size() > 1) {
							Map_ret.put("异常", "班级代码:" + banji.get("banji_d").toString() + "存在多个班级");
							throw new RuntimeException("班级代码:" + banji.get("banji_d").toString() + "存在多个班级");
						} else {
							/*
							 * List<Map<String, Object>> findall_zhanghao_yz2 =
							 * user_di
							 * .findall_zhanghao_yz(parameterStringMap);// 学生的id
							 */ Map<String, String> map = new HashMap<>();
							map.put("stu_id", parameterStringMap.get("id").toString());// 学生id
							map.put("banji_d", banji.get("banji_d").toString());// 班级代码
							// 学生关联班级
							user_di.xuesheng9banji(map);
						}

					}

					// list_banji

				}

			}
		} else {// 其他用户
			arm = user_di.updateUser(parameterStringMap);
		}
		Map_ret.put("arm", arm);

		if (arm == 0) {
			throw new RuntimeException("修改用户失败");
		}
		return Map_ret;
	}

	// 删除用户
	@Transactional
	public int delUserById(Map<String, String> parameterStringMap) {

		int use = user_di.delUserById(parameterStringMap);// 删除用户

		if (parameterStringMap.get("usertype_code").equals("2")) {// 老师
			// 教师班级表d113
			// 删除与班级的关联信息
			user_di.del_laoshi_banji(parameterStringMap);
		} else if (parameterStringMap.get("usertype_code").equals("4")) {// 家长
			// 家长学生表d109
			// 删除家长与学生的关联信息
			user_di.del_jiazhang_xuesheng(parameterStringMap);
		} else if (parameterStringMap.get("usertype_code").equals("5")) {// 学生
			// 班级学生表d112
			// 删除与班级的关联信息
			user_di.del_xuesheng_banji(parameterStringMap);
		}

		if (use > 1) {
			throw new RuntimeException("删除多个用户，已阻止");
		}
		return use;
	}

	public int updatePW(Map<String, String> parameterStringMap) {
		return user_di.updatePW(parameterStringMap);
	}

	@Transactional
	public int jiazhangAddhaizi(Map<String, String> parameterStringMap) {
		// 验证孩子是否存在
		// usertype_code

		List<Map<String, Object>> find_yz_xuesheng = user_di.find_yz_xuesheng(parameterStringMap);
		if (find_yz_xuesheng.size() == 0) {
			throw new RuntimeException("孩子不存在,请核对孩子id");
		} else if (find_yz_xuesheng.size() > 1) {
			throw new RuntimeException("孩子id下_存在多个孩子,无法关联");
		}

		int jiazhangAddhaizi = user_di.jiazhangAddhaizi(parameterStringMap);
		return jiazhangAddhaizi;
	}

	// 老师创建班级
	@Transactional
	public Map<String, Object> addbanji(Map<String, String> parameterStringMap) {
		Map<String, Object> Map_ret = new HashMap<>();
		String json = parameterStringMap.get("json");
		ObjectMapper mapper = new ObjectMapper();
		String laoshi_id = null;
		Map readValue = null;
		try {
			readValue = mapper.readValue(json, Map.class);
			laoshi_id = readValue.get("laoshi_id").toString();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Map> list_banji = (List<Map>) readValue.get("list_banji");
		for (int i = 0; i < list_banji.size(); i++) {
			Map banji = list_banji.get(i);
			banji.put("banji_d", ZhangHao.getZhangHaoUuid());// 生成班级代码
			banji.put("laoshi_id", laoshi_id);// 创建的老师id

			int addbanji = user_di.addbanji(banji);

			// 关联班级
			/*
			 * List<Map<String, Object>> findbanji_banji_d =
			 * user_di.findbanji_banji_d(banji.get("banji_d").toString());//
			 * 验证班级代码存在 String
			 * banji_d = null; if (findbanji_banji_d.size() > 1) { throw new
			 * RuntimeException("班级代码关联到多个班级_已阻止"); } else if
			 * (findbanji_banji_d.size() ==
			 * 1) { //正常 banji_d = (String)
			 * findbanji_banji_d.get(0).get("banji_d");
			 * 
			 * } else if (findbanji_banji_d.size() == 0) { throw new
			 * RuntimeException("班级不存在_创建失败"); }
			 */
			// 教师班级表d113 do 关联
			user_di.laoshi9banji(laoshi_id, banji.get("banji_d").toString());
			Map_ret.put(i + "_banji_d", banji.get("banji_d"));
		}

		return Map_ret;
	}

	@Transactional
	public int xuesheng9banji(Map<String, String> parameterStringMap) {
		new HashMap<>();
		// 1验证学生的账号存在否
		List<Map<String, Object>> findall_zhanghao_yz = user_di.findall_zhanghao_yz(parameterStringMap);
		if (findall_zhanghao_yz.size() == 0) {
			throw new RuntimeException("学生账号不存在,请核对学生账号");
		} else if (findall_zhanghao_yz.size() > 1) {
			throw new RuntimeException("学生账号存在重复,无法关联,联系管理员");
		}
		
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put("usertype_code", parameterStringMap.get("usertype_code"));
		hashMap.put("user_zhanghao", parameterStringMap.get("user_zhanghao"));
		hashMap.put("user_mima", parameterStringMap.get("user_mima"));
		
		// 2验证账号密码
		List<Map<String, Object>> findall = user_di.findall(hashMap);
		if (findall.size() == 0) {
			throw new RuntimeException("学生账号/密码不正确,请核对学生账号/密码");
		} else if (findall.size() > 1) {
			throw new RuntimeException("学生账号存在重复,无法关联,联系管理员");
		}
		
		
		// 3验证班级是否存在
		List<Map<String, Object>> find_yz_banji = user_di.find_yz_banji(parameterStringMap);
		if (find_yz_banji.size() == 0) {
			throw new RuntimeException("班级不存在,请核对班级号");
		} else if (find_yz_banji.size() > 1) {
			throw new RuntimeException("班级号下_存在多个班级,无法关联");
		}
		// String banji_id = find_yz_banji.get(0).get("id").toString();
		// 4验证班级是否允许加入
		String sf_njr = find_yz_banji.get(0).get("d107_06").toString();
		if (StringUtils.equals(sf_njr, "0")) {
			throw new RuntimeException("班级代码" + parameterStringMap.get("banji_d") + ":禁止加入 请联系老师开通");
		}

		// 5验证学生是否已经加入班级
		List<Map<String, Object>> list_xuesheng9banji = user_di.findbanji4xuesheng(parameterStringMap.get("stu_id"));
		if (list_xuesheng9banji.size() > 0) {
			throw new RuntimeException("已经加入班级,请从学生端退出班级再操作!");
		}

		// parameterStringMap.put("banji_id", banji_id);
		parameterStringMap.put("stu_id", findall.get(0).get("id").toString());
		// 班级学生表d112
		// do加入
		return user_di.xuesheng9banji(parameterStringMap);
	}

	/**
	 * 老师关联班级
	 * @param parameterStringMap
	 * @return
	 */
	@Transactional
	public int laoshi9banji(Map<String, String> parameterStringMap) {
		// 关联班级
		List<Map<String, Object>> findbanji_banji_d = user_di.findbanji_banji_d(parameterStringMap.get("banji_d"));// 验证班级存在
		// Integer banji_id = null;
		if (findbanji_banji_d.size() > 1) {
			throw new RuntimeException("班级代码关联到多个班级_已阻止");
		} /*
			 * else if (findbanji_banji_d.size() == 1) {
			 * //banji_id = (Integer) findbanji_banji_d.get(0).get("banji_id");
			 * }
			 */ else if (findbanji_banji_d.size() == 0) {
			throw new RuntimeException("班级不存在_关联失败");
		}
		// 教师班级表d113 do 关联
		return user_di.laoshi9banji(parameterStringMap.get("laoshi_id"), parameterStringMap.get("banji_d"));

	}

	public List<Map<String, Object>> findall_banji(Map<String, String> parameterStringMap) {

		return user_di.findall_banji(parameterStringMap);
	}

	@Transactional
	public Map<String, Object> delbanji(Map<String, String> parameterStringMap) {
		Map<String, Object> Map_ret = new HashMap<>();
		String banji_d = parameterStringMap.get("banji_d");

		// 1删班级
		int delbanji = user_di.delbanji(banji_d);
		Map_ret.put("删除班级数", delbanji);

		// 2删班级老师
		int del_banji_laoshi = user_di.del_banji_laoshi(banji_d);
		Map_ret.put("删班级老师关联数", del_banji_laoshi);

		// 3删班级学生
		int del_banji_xuesheng = user_di.del_banji_xuesheng(banji_d);
		Map_ret.put("删班级学生关联数", del_banji_xuesheng);

		// 4 删班级作业
		int del_banji_zuoye = user_di.del_banji_zuoye(banji_d);
		Map_ret.put("删班级作业关联数", del_banji_zuoye);

		return Map_ret;
	}

	@Transactional
	public Map<String, Object> update_banji(Map<String, String> parameterStringMap) {
		Map<String, Object> Map_ret = new HashMap<>();
		int update_banji = user_di.update_banji(parameterStringMap);
		Map_ret.put("修改班级数", update_banji);
		return Map_ret;
	}

	@Transactional
	public Map<String, Object> delxuesheng4banji(Map<String, String> parameterStringMap) {
		Map<String, Object> Map_ret = new HashMap<>();
		String json = parameterStringMap.get("json");
		ObjectMapper mapper = new ObjectMapper();
		String banji_d = null;
		Map readValue = null;
		try {
			readValue = mapper.readValue(json, Map.class);
			banji_d = readValue.get("banji_d").toString();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Map> list_stu_id = (List<Map>) readValue.get("list_stu_id");
		// 移除学生
		int update_banji = user_di.delxuesheng4banji(banji_d, list_stu_id);

		Map_ret.put("班级移除学生数", update_banji);
		return Map_ret;
	}

	/**
	 * 班级下的学生
	 * @param parameterStringMap
	 * @return
	 */
	public Map<String, Object> findall_stu5banji(Map<String, String> parameterStringMap) {
		Map<String, Object> Map_ret = new HashMap<>();
		// list 学生
		List<Map<String, Object>> findall_stu = user_di.findall_stu(parameterStringMap);
		// 作业完成度 得分率
		HashMap<String, Object> zy_wcd_dfl_5banji = new HashMap<>();
		HashMap<String, Object> zsk_syk_5banji = new HashMap<>();
		int rs_zsk = 0;//正式卡人数
		int rs_syk = 0;//试用卡人数
		double zy_wcd_z = 0;//作业完成度总
		double zy_dfl_z = 0;//作业得分率总

		if (findall_stu.size() == 0) {
			zsk_syk_5banji.put("rs_zsk", rs_zsk);
			zsk_syk_5banji.put("rs_syk", rs_syk);

			zy_wcd_dfl_5banji.put("zy_wcd", zy_wcd_z + "%");
			zy_wcd_dfl_5banji.put("zy_dfl", zy_dfl_z + "%");
			Map_ret.put("list_stu", findall_stu);
			Map_ret.put("banji_zy", zy_wcd_dfl_5banji);// 作业情况
			Map_ret.put("banji_ka", zsk_syk_5banji);// 会员情况
			return Map_ret;
		}

		for (int i = 0; i < findall_stu.size(); i++) {
			Map<String, Object> stu = findall_stu.get(i);

			if (stu.get("stu_vip_d").equals("1")) {
				rs_zsk++;
			} else if (stu.get("stu_vip_d").equals("2")) {
				rs_syk++;
			}

			if (stu.get("stu_zy_dfl") != null) {
				// 加法
				zy_dfl_z = NumberUtil.add(zy_dfl_z, Double.parseDouble(stu.get("stu_zy_dfl").toString()));

			}
			if (stu.get("stu_zy_wcd") != null) {
				zy_wcd_z = NumberUtil.add(zy_wcd_z, Double.parseDouble(stu.get("stu_zy_wcd").toString()));

			}

		}
		zsk_syk_5banji.put("rs_zsk", rs_zsk);
		zsk_syk_5banji.put("rs_syk", rs_syk);

		zy_wcd_dfl_5banji.put("zy_wcd", NumberUtil.div(zy_wcd_z, findall_stu.size(), 2, RoundingMode.HALF_UP) + "%");
		zy_wcd_dfl_5banji.put("zy_dfl", NumberUtil.div(zy_dfl_z, findall_stu.size(), 2, RoundingMode.HALF_UP) + "%");

		// Map<String, Object> zy_wcd_dfl_5banji =
		// user_di.zy_wcd_dfl_5banji(parameterStringMap);
		// 正式卡数 试用卡数
		// Map<String, Object> zsk_syk_5banji =
		// user_di.zsk_syk_5banji(parameterStringMap);

		Map_ret.put("list_stu", findall_stu);
		Map_ret.put("banji_zy", zy_wcd_dfl_5banji);// 作业情况
		Map_ret.put("banji_ka", zsk_syk_5banji);// 会员情况

		return Map_ret;
	}

	public Map<String, Object> laoshi_huizong(Map<String, String> parameterStringMap) {
		return user_di.laoshi_huizong(parameterStringMap);
	}

	public List<Map<String, Object>> list_zx_bz() {
		return user_di.list_zx_bz();
	}

	public List<Map<String, Object>> list_ti(Map<String, String> parameterStringMap) {

		String neirong_type_d = parameterStringMap.get("neirong_type_d");

		if (StringUtils.equals(neirong_type_d, "0")) {// 音标 这里不会走了 直接走详情了
			return user_di.list_ti_yinbiao(parameterStringMap);

		} else if (StringUtils.equals(neirong_type_d, "1")) {// 课文 单表的多个
			return user_di.list_ti_kewen(parameterStringMap);

		} else if (StringUtils.equals(neirong_type_d, "2")) {// 单词 单表的 多个 但取全部
																// 不会单个查看
			return user_di.list_ti_danci(parameterStringMap);

		} else if (StringUtils.equals(neirong_type_d, "3")) {// 句子 多表的多个
			return user_di.list_ti_jvzi(parameterStringMap);

			// return user_di.list_ti_duanyu(parameterStringMap);
		} else if (StringUtils.equals(neirong_type_d, "4")) {// 对话 多表的多个
			return user_di.list_ti_duihua(parameterStringMap);

		} else if (StringUtils.equals(neirong_type_d, "5")) {// 听力 多表的多个

			return user_di.list_ti_tingli(parameterStringMap);

		} else if (StringUtils.equals(neirong_type_d, "6")) {// 阅读 多表的多个
			return user_di.list_ti_yuedu(parameterStringMap);

		} else if (StringUtils.equals(neirong_type_d, "7")) {// 作文 暂时没有做 多表的多个
			List<Map<String, Object>> list_ti_zuowen = user_di.list_ti_zuowen(parameterStringMap);
			for (int i = 0; i < list_ti_zuowen.size(); i++) {
				List<Map<String, Object>> list_yaodian = user_di.find_allyaodian(parameterStringMap);
				List<Map<String, Object>> list_zhuyidian = user_di.find_allzhuyidian(parameterStringMap);

				list_ti_zuowen.get(i).put("list_yaodian", list_yaodian);
				list_ti_zuowen.get(i).put("list_zhuyidian", list_zhuyidian);
			}

			return list_ti_zuowen;
		} else if (StringUtils.equals(neirong_type_d, "8")) {// 短语 单表的 多个 但取全部
																// 不会单个查看
			return user_di.list_ti_duanyu(parameterStringMap);

		} else {
			return null;
		}

	}

	public List<Map<String, Object>> list_danyuan(Map<String, String> parameterStringMap) {
		if (parameterStringMap.get("neirong_type_d").equals("0")) {
			return user_di.list_yinbiao(parameterStringMap);// 音标的list
		} else {
			return user_di.list_danyuan(parameterStringMap);
		}

	}

	public List<Map<String, Object>> xiangqing_ti(Map<String, String> parameterStringMap) {

		String neirong_type_d = parameterStringMap.get("neirong_type_d");

		if (StringUtils.equals(neirong_type_d, "0")) {// 音标的 多个表的
			List<Map<String, Object>> xiangqing_ti = user_di.xiangqing_ti(parameterStringMap);// 返回单个音标
			
			List<Map<String, Object>> list_fayinshili = user_di.list_fayinshili(xiangqing_ti.get(0));
			ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
			ArrayList<Map<String, Object>> arrayList2 = new ArrayList<>();
			for (int i = 0; i < list_fayinshili.size(); i++) {
				Map<String, Object> map = list_fayinshili.get(i);
				if(map.containsKey("yb_leixing_ci")) {
					if (StringUtils.equalsIgnoreCase(map.get("yb_leixing_ci").toString(), "1")) {
						arrayList.add(map);
					}else {
						arrayList2.add(map);
					}
				}
			}
			
			xiangqing_ti.get(0).put("list_fayinshili", arrayList);		
			xiangqing_ti.get(0).put("list_tiaozhanyixia", arrayList2);			
			xiangqing_ti.get(0).put("list_fayinxiangqing", user_di.list_fayinxiangqing(xiangqing_ti.get(0)));
			
			
			
			List<Map<String, Object>> list_fayinxiangguan = user_di.list_fayinxiangguan(xiangqing_ti.get(0));
			//System.out.println(list_fayinxiangguan);
			//List<Map<String, Object>> list_yb_danci = user_di.list_yb_danci(xiangqing_ti.get(0));
			
			for (int i = 0; i < list_fayinxiangguan.size(); i++) {
				list_fayinxiangguan.get(i).put("list_fayin_lizi", user_di.list_yb_danci(list_fayinxiangguan.get(i)));
			}

			xiangqing_ti.get(0).put("list_fy_zmzh", list_fayinxiangguan);
			
			
			return xiangqing_ti;
			
		} else if (StringUtils.equals(neirong_type_d, "5")) {// 听力 多表的多个

			List<Map<String, Object>> xiangqing_ti = user_di.xiangqing_ti(parameterStringMap);// 听力下的问题
			for (int i = 0; i < xiangqing_ti.size(); i++) {
				xiangqing_ti.get(i).put("list_wentixuanxiang", user_di.Find_allwentixuanxiang(xiangqing_ti.get(i)));// 听力问题下的选向
			}
			return xiangqing_ti;

		} else if (StringUtils.equals(neirong_type_d, "6")) {// 阅读 多表的多个
			List<Map<String, Object>> xiangqing_ti = user_di.xiangqing_ti(parameterStringMap);
			for (int i = 0; i < xiangqing_ti.size(); i++) {
				xiangqing_ti.get(i).put("list_wentixuanxiang", user_di.Find_allwentixuanxiang_yuedu(xiangqing_ti.get(i)));
			}
			return xiangqing_ti;

		} /*
			 * else if (StringUtils.equals(neirong_type_d, "7")) {// 作文 多表的多个
			 * List<Map<String, Object>> xiangqing_ti =
			 * user_di.xiangqing_ti(parameterStringMap); for (int i = 0; i <
			 * xiangqing_ti.size(); i++) {
			 * xiangqing_ti.get(i).put("list_wentixuanxiang",
			 * user_di.Find_allwentixuanxiang_yuedu(xiangqing_ti.get(i))); }
			 * List<Map<String, Object>> findall
			 * =list_ti(parameterStringMap);//返回作文list if
			 * (findall.size() == 0) { return null; } // findall.get(0).put(key,
			 * value)
			 * List<Map<String, Object>> list_yaodian =
			 * zuowen_d.find_allyaodian(parameterStringMap); List<Map<String,
			 * Object>>
			 * list_zhuyidian = zuowen_d.find_allzhuyidian(parameterStringMap);
			 * 
			 * findall.get(0).put("list_yaodian", list_yaodian);
			 * findall.get(0).put("list_zhuyidian", list_zhuyidian); return
			 * xiangqing_ti;
			 * 
			 * }
			 */ else {
			return user_di.xiangqing_ti(parameterStringMap);
		}
	}

	@Transactional
	public Map<String, Object> buzhi_zuoye(Map<String, String> parameterStringMap) {
		Map<String, Object> Map_ret = new HashMap<>();
		// 验证是否存在未完成的布置作业
		List<Map<String, Object>> find_buzhi_zuoye_wwc = user_di.find_buzhi_zuoye_wwc(parameterStringMap);// 查未完成的

		if (find_buzhi_zuoye_wwc.size() == 0) {// 不存在创建 返回主键id
			Map<String, Object> Map = new HashMap<>();
			Map.put("zuoye_id", UUID.randomUUID().toString());// uuid获取 作业id
			int add_zuoye = user_di.add_zuoye(Map.get("zuoye_id"), parameterStringMap.get("laoshi_id"), parameterStringMap.get("jiaocai_d"));
			Map.put("zuoye_buzhi_status", 0);
			Map_ret.put("msg0", "创建作业数" + add_zuoye + "条");
			Map_ret.put("zuoye", Map);

		} else if (find_buzhi_zuoye_wwc.size() == 1) {// 存在返回未完成的 布置作业
			List<Map<String, String>> find_xiangqing_zuoye = user_di.find_xiangqing_zuoye(find_buzhi_zuoye_wwc.get(0));
			
			find_buzhi_zuoye_wwc.get(0).put("list_xiangqing", find_xiangqing_zuoye);
			Map_ret.put("zuoye", find_buzhi_zuoye_wwc.get(0));

		} else if (find_buzhi_zuoye_wwc.size() > 1) {
			throw new RuntimeException("检测到多个未完成作业,无法确定加载哪个,请联系管理员处理");
		}
		return Map_ret;
	}

	@Transactional
	public Map<String, Object> buzhi_zuoye_add(Map<String, String> parameterStringMap) {
		Map<String, Object> Map_ret = new HashMap<>();
		// 给作业内容添加一条
		int buzhi_zuoye_add = user_di.buzhi_zuoye_add(parameterStringMap);
		Map_ret.put("msg0", "增加作业内容" + buzhi_zuoye_add + "条");
		return Map_ret;
	}

	@Transactional
	public Map<String, Object> buzhi_zuoye_del(Map<String, String> parameterStringMap) {
		Map<String, Object> Map_ret = new HashMap<>();
		// 给作业内容删除一条
		int buzhi_zuoye_del = user_di.buzhi_zuoye_del(parameterStringMap);
		Map_ret.put("msg0", "取消作业内容" + buzhi_zuoye_del + "条");
		return Map_ret;
	}

	@Transactional
	public Map<String, Object> buzhi_zuoye_over(Map<String, String> parameterStringMap) {
		Map<String, Object> Map_ret = new HashMap<>();
		String banji_d = parameterStringMap.get("banji_d_arr");
		String[] banji_d_arr = banji_d.split(",");
		// 1保存作业其他内容 并修改作业布置状态
		int update_zuoye_over = user_di.update_zuoye_over(parameterStringMap);
		Map_ret.put("msg0", "布置作业完成" + update_zuoye_over + "条");
		// 2 关联本次作业的班级
		int banji_zuoye_over = user_di.banji_zuoye_over(banji_d_arr, parameterStringMap);
		Map_ret.put("msg1", "关联本次作业的班级" + banji_zuoye_over + "个");
		
		// 3创建新消息 _作业布置的新消息 给对应的班级的学生
		String zuoye_neirong = parameterStringMap.get("laoshi_m") + "老师布置作业:" + parameterStringMap.get("zuoye_m");
		
		int banji_zuoye_xiaoxi_over = user_di.banji_zuoye_xiaoxi_over(banji_d_arr, zuoye_neirong, parameterStringMap.get("zuoye_id"));
		Map_ret.put("msg2", "本次作业学生的消息" + banji_zuoye_xiaoxi_over + "条");
		//数组转list
		List<String> colList = CollUtil.newArrayList(banji_d_arr);
		
		/*Message message = new Message();
		Message message2 = new Message();
        message2.setType(Message.TYPE_NOTIFICATION);
        message2.setTitle("title");
        message2.setContent("通知点击执行Intent测试");
        //Style style = new Style(1);
        ClickAction action = new ClickAction();
        action.setActionType(ClickAction.TYPE_INTENT);
        //hwid=cd546973-a47d-4b6f-a55a-531dc99c698c, scoreAll=0.0, flag=已结束, homework_cishu=1
        action.setIntent("xgscheme://com.xg.push/notify_detail?param1=aa&param2=bb");
        //message2.setStyle(style);
        message2.setAction(action);*/
        
		//向班级推送
		JSONObject demoPushTags = Push_ts.demoPushTags(colList, "作业通知", zuoye_neirong);
		//JSONObject demoPushTags = Push_ts.demoPushTags(colList, message);
		Map_ret.put("msg_banji_ts",demoPushTags.toString());
		// 4创建消息 _作业布置的新消息 给对应的班级的学生的家长
		int banji_zuoye_xiaoxi_over_jz = user_di.banji_zuoye_xiaoxi_over_jz(banji_d_arr, zuoye_neirong, parameterStringMap.get("zuoye_id"));
		Map_ret.put("msg3", "本次作业家长的消息" + banji_zuoye_xiaoxi_over_jz + "条");
		//Map_ret.put("msg_banji_ts",demoPushTags.toString());
		return Map_ret;
	}

	public List<Map<String, Object>> list_zuoye(Map<String, String> parameterStringMap) {
		// Map<String, Object> Map_ret = new HashMap<>();

		return user_di.list_zuoye(parameterStringMap);

	}

	@Transactional
	public Map<String, Object> del_zuoye(Map<String, String> parameterStringMap) {
		Map<String, Object> Map_ret = new HashMap<>();
		// 1删作业班级
		int del_zuoye_banji = user_di.del_zuoye_banji(parameterStringMap);
		Map_ret.put("msg0", "删除作业班级数" + del_zuoye_banji + "条");
		// 1 验证作业是否还有关联班级
		int shu_zuoye_banji = user_di.shu_zuoye_banji(parameterStringMap);
		if (shu_zuoye_banji > 0) {// 是 结束
			Map_ret.put("msg1", "作业剩余关联班级数" + shu_zuoye_banji + "条");
		} else {// 否 删除作业
			int del_zuoye = user_di.del_zuoye(parameterStringMap);
			Map_ret.put("msg1", "删除作业数" + del_zuoye + "条");
		}
		return Map_ret;
	}

	public Map<String, Object> upd_zuoye_banji(Map<String, String> parameterStringMap) {
		Map<String, Object> Map_ret = new HashMap<>();
		int upd_zuoye = user_di.upd_zuoye_banji(parameterStringMap);
		Map_ret.put("msg0", "更新班级作业数" + upd_zuoye + "条");
		return Map_ret;
	}

	public Map<String, Object> clean_zuoye(Map<String, String> parameterStringMap) {
		Map<String, Object> Map_ret = new HashMap<>();
		int clean_zuoye = user_di.clean_zuoye(parameterStringMap);
		Map_ret.put("msg0", "清空作业内容数" + clean_zuoye + "条");
		return Map_ret;
	}

	/*
	 * public Map<String, Object> upd_zuoye(Map<String, String>
	 * parameterStringMap)
	 * { Map<String, Object> Map_ret = new HashMap<>(); int upd_zuoye =
	 * user_di.upd_zuoye(parameterStringMap); Map_ret.put("msg0", "更新作业数" +
	 * upd_zuoye + "条"); return Map_ret; }
	 */

	public Map<String, Object> jc_zuoye_list(Map<String, String> parameterStringMap) {
		Map<String, Object> Map_ret = new HashMap<>();
		String[] arr_neirong_type_d = null;

		if (StringUtils.isNotEmpty(parameterStringMap.get("arr_neirong_type_d"))) {
			arr_neirong_type_d = parameterStringMap.get("arr_neirong_type_d").split(",");//
		}

		int pageNumber = Integer.parseInt(parameterStringMap.get("pageNumber"));
		int pageSize = Integer.parseInt(parameterStringMap.get("pageSize"));
		//Page<Object> startPage = PageHelper.startPage(pageNumber, pageSize, "a.t101_14");
		List<Map<String, Object>> ddd = user_di.jc_zuoye_shijian_list(arr_neirong_type_d, parameterStringMap); // 时间列表
		//Map_ret.put("Total", startPage.getTotal());
		// arr_neirong_type_d
		for (int i = 0; i < ddd.size(); i++) {
			Map<String, Object> map = ddd.get(i);
			if (StringUtils.equals(parameterStringMap.get("zuoye_status"), "-1")) {
				List<Map<String, Object>> jc_zuoye_banji = user_di.jc_zuoye_banji(parameterStringMap.get("zuoye_status"), arr_neirong_type_d, parameterStringMap ,map.get("zuoye_time").toString());
				ddd.get(i).put("list_zuoye_x", jc_zuoye_banji);// 未开始
				Map_ret.put("date", ddd);
			} else if (StringUtils.equals(parameterStringMap.get("zuoye_status"), "0")) {
				List<Map<String, Object>> jc_zuoye_banji = user_di.jc_zuoye_banji(parameterStringMap.get("zuoye_status"), arr_neirong_type_d, parameterStringMap,map.get("zuoye_time").toString());
				ddd.get(i).put("list_zuoye_i", jc_zuoye_banji);// 进行中
				Map_ret.put("date", ddd);
			} else if (StringUtils.equals(parameterStringMap.get("zuoye_status"), "1")) {
				List<Map<String, Object>> jc_zuoye_banji = user_di.jc_zuoye_banji(parameterStringMap.get("zuoye_status"), arr_neirong_type_d, parameterStringMap,map.get("zuoye_time").toString());
				ddd.get(i).put("list_zuoye_o", jc_zuoye_banji);// 已结束
				Map_ret.put("date", ddd);
			} else {//全部的
				List<Map<String, Object>> list_zuoye_x = user_di.jc_zuoye_banji("-1", arr_neirong_type_d, parameterStringMap,map.get("zuoye_time").toString());
				List<Map<String, Object>> list_zuoye_i = user_di.jc_zuoye_banji("0", arr_neirong_type_d, parameterStringMap,map.get("zuoye_time").toString());
				List<Map<String, Object>> list_zuoye_o = user_di.jc_zuoye_banji("1", arr_neirong_type_d, parameterStringMap,map.get("zuoye_time").toString());
				ddd.get(i).put("list_zuoye_x", list_zuoye_x);// 未开始
				ddd.get(i).put("list_zuoye_i", list_zuoye_i);// 进行中
				ddd.get(i).put("list_zuoye_o", list_zuoye_o);// 已结束			
			}

		}
		Map_ret.put("date", ddd);
		return Map_ret;
	}

	/**
	 * 作业总体情况
	 * @param parameterStringMap
	 * @return
	 */
	public Map<String, Object> zuoye_ztqk(Map<String, String> parameterStringMap) {
		Map<String, Object> Map_ret = new HashMap<>();
		// 1 完成人数 平均分
		Map<String, Object> find_wcrs_pjfs = user_di.find_wcrs_pjfs(parameterStringMap);
		Map_ret.put("wcrs_pjfs", find_wcrs_pjfs);

		// 2 0-20,20-40,40-60,60-80,80-100 的人数分布
		Map<String, Object> find_fs_rs_fb = user_di.find_fs_rs_fb(parameterStringMap);
		Map_ret.put("fs_rs_fb", find_fs_rs_fb);

		// 3 未完成学生名单
		List<Map<String, Object>> find_wwc_stu_list = user_di.find_wwc_stu_list(parameterStringMap);
		Map_ret.put("wwc_stu_list", find_wwc_stu_list);

		return Map_ret;
	}

	/**
	 * 作业学生成绩(全部学生)
	 */
	public List<Map<String, Object>> zuoye_stu_cj(Map<String, String> parameterStringMap) {
		// Map<String, Object> Map_ret = new HashMap<>();
		return user_di.zuoye_stu_cj(parameterStringMap);
	}

	@Transactional
	public Map<String, Object> xx_count(Map<String, String> parameterStringMap) {
		Map<String, Object> Map_ret = new HashMap<>();
		// 做一个判断 创建作业到期消息
		// 找结束的作业
		List<Map<String, Object>> find_zy_jieshu = user_di.find_zy_jieshu(parameterStringMap);// 已经结束
																								// 但结束状态未设置的
		if (find_zy_jieshu.size() > 0) {
			// 更结束状态
			int upd_zy_jieshu = user_di.upd_zy_jieshu(find_zy_jieshu);
			Map_ret.put("mag0", "更结束状态数:" + upd_zy_jieshu + "条");
			// 创建消息 作业结束消息
			int ins_xx_zy_jieshu = user_di.ins_xx_zy_jieshu(find_zy_jieshu, parameterStringMap.get("laoshi_id"));
			Map_ret.put("mag1", "作业结束消息数:" + ins_xx_zy_jieshu + "条");
		}

		Map<String, Object> xx_count = user_di.xx_count(parameterStringMap);
		Map_ret.put("data", xx_count);
		return Map_ret;
	}

	public List<Map<String, Object>> xx_list(Map<String, String> parameterStringMap) {
		return user_di.xx_list(parameterStringMap);
	}

	public Map<String, Object> del_laoshi_xx(Map<String, String> parameterStringMap) {
		String[] arr_xx_id = parameterStringMap.get("arr_xx_id").split(",");

		Map<String, Object> Map_ret = new HashMap<>();
		int del_xx = user_di.del_laoshi_xx(arr_xx_id);
		Map_ret.put("msg0", "删除消息数: " + del_xx + "个");
		return Map_ret;
	}

	@Transactional
	public Map<String, Object> stu_update_mima(HttpServletRequest request, Map<String, String> parameterStringMap) {
		Map<String, Object> Map_ret = new HashMap<>();
		boolean update_mima_yzm = Yzm_util.update_mima_yzm(request, parameterStringMap);
		if (update_mima_yzm) {
			Map_ret.put("msg_", "验证码通过");
			// 验证手机号下有学生
			List<Map<String, Object>> list_stu = user_di.find_stu_by_jiazhang_shoujihao(parameterStringMap.get("user_dianhua"));
			List<Map<String, Object>> find_stu_zh = user_di.findall_zhanghao_yz(parameterStringMap);

			if (find_stu_zh.size() != 1) {
				throw new RuntimeException("学生账号不正确");
			}
			boolean sf_n_up = false;// 是否能更密码
			if (list_stu.size() > 0) {
				for (int i = 0; i < list_stu.size(); i++) {
					Map<String, Object> stu = list_stu.get(i);
					Object object = stu.get("stu_id");// 学生的id
					Object object2 = find_stu_zh.get(0).get("Id");
					if (StringUtils.equals(object.toString(), object2.toString())) {

						sf_n_up = true;
						Map_ret.put("smg0", "家长手机号下有这个学生");
						parameterStringMap.put("id", object2.toString());
						break;
					}

				}

			}else {
				Map_ret.put("smg0", "家长手机号下无学生");
			}

			// 验证结果: 账号下的学生存在
			if (sf_n_up) {
				int updatePW = user_di.updatePW(parameterStringMap);
				Map_ret.put("smg1", "修改密码数" + updatePW + "数");
			}
		}else {
			Map_ret.put("smg1", "验证码不正确");
		}
		return Map_ret;
	}

	/**
	 * 解绑微信
	 * @param request
	 * @param parameterStringMap
	 * @return
	 */
	public Map<String, Object> out_weixin_id(HttpServletRequest request, Map<String, String> parameterStringMap) {
		Map<String, Object> Map_ret = new HashMap<>();
		int out_weixin_id = user_di.out_weixin_id(parameterStringMap);
		Map_ret.put("smg0", "解绑微信成功:" + out_weixin_id);
		return Map_ret;
	}

	/**
	 * 改消息 为已读消息
	 * @param parameterStringMap
	 * @return
	 */
	public Map<String, Object> upd_xx_yidu(Map<String, String> parameterStringMap) {
		Map<String, Object> Map_ret = new HashMap<>();
		String[] arr_xx_id = parameterStringMap.get("arr_xx_id").split(",");
		int upd_xx_yidu = user_di.upd_xx_yidu(arr_xx_id);
		Map_ret.put("smg0", "改为已读消息数:" + upd_xx_yidu);

		return Map_ret;
	}

	/**
	 * 绑定微信
	 * @param request
	 * @param parameterStringMap
	 * @return
	 */
	public Map<String, Object> bd_weixin_id(HttpServletRequest request, Map<String, String> parameterStringMap) {
		Map<String, Object> Map_ret = new HashMap<>();
		HashMap<String, Object> weixin_jscode2session = Const.weixin_jscode2session(parameterStringMap.get("user_weixin_code"),parameterStringMap.get("usertype_code"));

		if (((boolean) weixin_jscode2session.get("sf_ok")) == true) {
			parameterStringMap.put("user_weixin_id", (String) ((HashMap) weixin_jscode2session.get("weixin_result")).get("openid"));
			Map_ret.put("user_weixin_id", parameterStringMap.get("user_weixin_id"));// 返回给前台openid
			int bd_weixin_id = user_di.bd_weixin_id(parameterStringMap);
			Map_ret.put("msg0", "绑定微信号成功:" + bd_weixin_id);
		} else {
			Map_ret.put("msg0", "绑定失败,微信号_code验证异常");
			Map_ret.put("msg1", weixin_jscode2session);
		}
		return Map_ret;
	}

	/**
	 * 生成订单
	 * @param parameterStringMap
	 * @return
	 */
	@Transactional
	public Map<String, Object> do_dingdan(Map<String, String> parameterStringMap) {
		Map<String, Object> Map_ret = new HashMap<>();
		// 商户订单单号
		parameterStringMap.put("out_trade_no", IdUtil.objectId());
		// 订单号保存
		int add_dd = user_di.add_dd(parameterStringMap);
		Map_ret.put("msg0", "生成订单数:" + add_dd);

		// 添加必要的参数
		// 交易金额(分)
		parameterStringMap.put("total_fee", Pay_util.get_total_fee(parameterStringMap.get("pay_tc_d")));
		// 回调地址
		// parameterStringMap.put("notify_url", "/login/zf_go_bk");
		parameterStringMap.put("notify_url", "https://zts100.com/demo/login/zf_go_bk");// 正式的

		// 签名 或 统一订单 返回给app
		if (StringUtils.equals(parameterStringMap.get("pay_type_d"), "1")) {// 微信
			// 微信预付单(签名 + 请求统一订单)
			Map<String, String> wx_yfd = Pay_util.wx_yfd(parameterStringMap);
			Map_ret.put("data", wx_yfd);
			// HttpUtil.post("urlString", "body");
		} else if (StringUtils.equals(parameterStringMap.get("pay_type_d"), "2")) {// 支付宝
			// 支付宝预付单(只是签名)
			Map<String, String> zfb_yfd = Pay_util.zfb_yfd(parameterStringMap);
			Map_ret.put("data", zfb_yfd);
		}
		return Map_ret;
	}

	/**
	 * 查询支付结果
	 * @param parameterStringMap
	 * @return
	 */
	public Map<String, Object> find_zf_Res(Map<String, String> parameterStringMap) {
		Map<String, Object> Map_ret = new HashMap<>();

		// 查询支付结果
		if (StringUtils.equals(parameterStringMap.get("pay_type_d"), "1")) {// 微信
			// 微信支付结果
			Map<String, String> wx_yfd = Pay_util.wx_zf_Res(parameterStringMap);
			Map_ret.put("data", wx_yfd);
			// HttpUtil.post("urlString", "body");
		} else if (StringUtils.equals(parameterStringMap.get("pay_type_d"), "2")) {// 支付宝
			// 支付宝支付结果
			Map<String, String> zfb_yfd = Pay_util.zfb_zf_Res(parameterStringMap);
			Map_ret.put("data", zfb_yfd);
		}
		return Map_ret;
	}

	/**
	 * 退款的接口
	 * @param parameterStringMap
	 * @return
	 */
	public Map<String, Object> do_zf_bak(Map<String, String> parameterStringMap) {
		Map<String, Object> Map_ret = new HashMap<>();

		if (StringUtils.equals(parameterStringMap.get("pay_type_d"), "1")) {// 微信
			// 微信支付退款

			// HttpUtil.post("urlString", "body");
		} else if (StringUtils.equals(parameterStringMap.get("pay_type_d"), "2")) {// 支付宝
			// 支付宝支付退款

		}

		return Map_ret;
	}

	/**
	 * 家长解绑学生
	 * @param request
	 * @return
	 */
	public Map<String, Object> p_ubd_stu(HttpServletRequest request, Map<String, String> parameterStringMap) {
		Map<String, Object> Map_ret = new HashMap<>();
		boolean update_mima_yzm = Yzm_util.db_yzm(request, parameterStringMap);
		if (update_mima_yzm) {
			Map_ret.put("msg_", "验证码通过");
			// 验证手机号下有学生
			int p_ubd_stu = user_di.p_ubd_stu(parameterStringMap);
			if(p_ubd_stu==1) {
				Map_ret.put("msg0", "解绑学生数:"+p_ubd_stu);
			}else if(p_ubd_stu>1){
				throw new RuntimeException("删除数据过大,已阻止");
			}else {
				Map_ret.put("msg2", "未删除任何数据");
			}
		}else {
			Map_ret.put("smg1", "验证码不正确");
		}
		return Map_ret;
	}

	public Map<String, Object> xiugaiUser_sj(HttpServletRequest request, Map<String, String> parameterStringMap) {
		Map<String, Object> Map_ret = new HashMap<>();
		boolean update_mima_yzm = Yzm_util.db_yzm(request, parameterStringMap);
		if (update_mima_yzm) {
			Map_ret.put("msg_", "验证码通过");
			// 验证手机号下有学生
			int p_ubd_stu = user_di.xiugaiUser_sj(parameterStringMap);
			if(p_ubd_stu==1) {
				Map_ret.put("msg0", "修改数量:"+p_ubd_stu);
			}else if(p_ubd_stu>1){
				throw new RuntimeException("修改数量:,已阻止");
			}else {
				Map_ret.put("msg2", "未修改任何数据");
			}
		}else {
			Map_ret.put("smg1", "验证不通过");
		}
		return Map_ret;
	}

}
