package com.fanke.fksupermarket.realm;

import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fanke.fksupermarket.po.User;
import com.fanke.fksupermarket.service.IUserService;

public class MyRealm extends AuthorizingRealm {

	@Resource
	private IUserService userService;

	private static Logger log = LoggerFactory.getLogger(MyRealm.class);

	// ����Realm������
	@Override
	public String getName() {
		return super.getName();
	}

	// ֧��UsernamePasswordToken
	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof UsernamePasswordToken;
	}

	// Ϊ��ǰ��½�ɹ����û�����Ȩ�޺ͽ�ɫ���Ѿ���½�ɹ���
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		log.debug("MyRealm-->>doGetAuthorizationInfo-->>��Ȩ");
		// ��ȡ�û���
		Integer userId = (Integer) principals.getPrimaryPrincipal(); 
		log.debug("MyRealm-->>doGetAuthorizationInfo-->>�û�ID=" + userId);

		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		Set<String> roles = userService.getRoles(userId);
		log.debug("MyRealm-->>doGetAuthorizationInfo-->>��ɫ��=" + roles);
		authorizationInfo.setRoles(roles);
		
		return authorizationInfo;
	}

	// ��֤��ǰ��¼���û�����ȡ��֤��Ϣ
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		//System.out.println("AuthenticationInfo===��¼��֤");
		log.debug("MyRealm-->>doGetAuthenticationInfo-->>��¼��֤");

		String userId = (String) token.getPrincipal();
		//User user = userService.getByUsername(userName);
		User user = userService.getByUid(Integer.valueOf(userId));
		// System.out.println("************11111111111************");
		if (user != null) {
			// System.out.println("��ʼ�����֤:::"+user.getU_name()+"\t"+user.getU_pwd());
			AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(user.getU_id(), user.getU_pwd(), "xx");
			// System.out.println("��¼��֤���������ء�����������"+authcInfo.toString());
			return authcInfo;
		} else {
			return null;
		}
	}
}