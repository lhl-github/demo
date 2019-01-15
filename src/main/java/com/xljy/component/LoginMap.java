package com.xljy.component;

import java.util.HashMap;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import com.xljy.util.Const;

public class LoginMap {
	private static HashMap<String, Object> s_map = new HashMap<>();

	public static synchronized void AddSession(String bioashi, HttpSession session) {
		if (session != null) {
			
			s_map.put(bioashi, session);
		}
	}
	/**
	 * @param usertype_code
	 * @param user_id
	 * @param session
	 * 
	 * put(usertype_code+"#"+user_id ,session)
	 */
	public static synchronized void AddSession(String usertype_code,String user_id, HttpSession session) {
		if (session != null && StringUtils.isNotEmpty(user_id)&&StringUtils.isNotEmpty(usertype_code)) {		
			s_map.put(usertype_code+"#"+user_id, session);
		}else {
			throw new RuntimeException("AddSession所需的参数都不能空!");
		}
	}

	public static synchronized void DelSession(String bioashi, HttpSession session) {
		if (session != null) {
			s_map.remove(bioashi);
			session.removeAttribute(Const.LOGIN_USER);
			session.invalidate();
		}
	}
	
	public static synchronized void DelSession(String usertype_code,String user_id) {
		
		
		/*HttpSession session = (HttpSession) s_map.get(usertype_code+"#"+user_id);
	
		if(session!=null) {
			try {
				session.removeAttribute(Const.LOGIN_USER);
				session.invalidate();
			} catch (Exception e) {
				//System.out.println(e.getMessage());
				s_map.remove(usertype_code+"#"+user_id);
			}			
		}*/	
		s_map.remove(usertype_code+"#"+user_id);
		
	}

	public static synchronized HttpSession getSession(String bioashi) {
		if (StringUtils.isEmpty(bioashi)) {
			return null;
		}
		return (HttpSession) s_map.get(bioashi);
	}
	
	public static synchronized HttpSession getSession(String usertype_code,String user_id) {
		
		return (HttpSession) s_map.get(usertype_code+"#"+user_id);
	}

}
