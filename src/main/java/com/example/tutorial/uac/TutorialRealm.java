package com.example.tutorial.uac;

import com.google.inject.Inject;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashSet;

public class TutorialRealm extends AuthorizingRealm {

    private TrivialCredentialsStore credentialsStore;

    @Inject
    protected TutorialRealm(TrivialCredentialsStore credentialsStore) {
        super();
        this.credentialsStore = credentialsStore;
        setCachingEnabled(false);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        TrivialUserAccount userAccount = credentialsStore.getAccount((String) token.getPrincipal());
        if (userAccount == null) {
            return null;
        }
        String tokenCredentials = new String((char[]) token.getCredentials());
        if (userAccount.getPassword()
                       .equals(tokenCredentials)) {
            return new SimpleAuthenticationInfo(userAccount.getUserId(), token.getCredentials(), "TutorialRealm");
        } else {
            return null;
        }
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        TrivialUserAccount userAccount = credentialsStore.getAccount((String) principals.getPrimaryPrincipal());
        if (userAccount != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            info.setStringPermissions(new HashSet(userAccount.getPermissions()));
            return info;
        }
        return null;
    }
}