/*******************************************************************************
 * Copyright (c) 2017,2022 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.security.mp.jwt.impl.utils;

import java.util.ArrayList;
import java.util.Map;

import com.ibm.websphere.ras.Tr;
import com.ibm.websphere.ras.TraceComponent;
import com.ibm.websphere.security.jwt.JwtToken;
import com.ibm.ws.security.mp.jwt.TraceConstants;
import com.ibm.ws.security.mp.jwt.impl.MicroProfileJwtConfigImpl;

/**
 *
 */
public class JwtPrincipalMapping {

    private static TraceComponent tc = Tr.register(JwtPrincipalMapping.class, TraceConstants.TRACE_GROUP, TraceConstants.MESSAGE_BUNDLE);
    protected static final String REALM_CLAIM = "realm";

    private Map claims = null;

    String realm = null;
    //String uniqueSecurityName = null;
    String userName = null;
    ArrayList<String> groupIds = null;

    public JwtPrincipalMapping(JwtToken jwtToken, String userAttr, String groupAttr, boolean mapToUr) {
        String methodName = "<init>";
        if (tc.isDebugEnabled()) {
            Tr.entry(tc, methodName, jwtToken, userAttr, groupAttr, mapToUr);
        }
        userName = getUserName(userAttr, jwtToken);
        if (userName == null) {
            if (tc.isDebugEnabled()) {
                Tr.exit(tc, methodName);
            }
            return;
        }
        if (TraceComponent.isAnyTracingEnabled() && tc.isDebugEnabled()) {
            Tr.debug(tc, "user name = ", userName);
        }
        if (!mapToUr) {
            realm = getRealm(REALM_CLAIM, jwtToken);
            populateGroupIds(jwtToken, groupAttr);
        }
        if (tc.isDebugEnabled()) {
            Tr.exit(tc, methodName);
        }
    }

    /**
     * @param string
     * @param jsonstr
     * @return
     */
    private String getRealm(String realmAttribute, JwtToken jwtToken) {

        if (jwtToken != null && realmAttribute != null) {
            Object realm = getClaim(jwtToken, realmAttribute);
            if (realm instanceof String) {
                return (String) realm;
            }
        }
        return null;
    }

    public boolean isUserNameNull() {
        return (userName == null || userName.isEmpty());
    }

    public String getMappedRealm() {
        return realm;
    }

    public String getMappedUser() {
        return userName;
    }

    public ArrayList<String> getMappedGroups() {
        return groupIds;
    }

    private String getUserName(String userNameAttr, JwtToken jwtToken) {
        String methodName = "getUserName";
        if (tc.isDebugEnabled()) {
            Tr.entry(tc, methodName, userNameAttr, jwtToken);
        }
        if (jwtToken != null && userNameAttr != null && !userNameAttr.isEmpty()) {
            Object user = getClaim(jwtToken, userNameAttr);
            setUserName(userNameAttr, user);
        }
        if (userName == null) {
            Tr.error(tc, "PRINCIPAL_MAPPING_MISSING_ATTR", new Object[] { userNameAttr, MicroProfileJwtConfigImpl.KEY_userNameAttribute });
        }
        if (tc.isDebugEnabled()) {
            Tr.exit(tc, methodName, userName);
        }
        return userName;
    }

    void setUserName(String userNameAttr, Object user) {
        if (user == null) {
            if (tc.isDebugEnabled()) {
                Tr.debug(tc, "Provided user name object is null. Current user name [" + userName + "] will not be changed");
            }
            return;
        }
        if (user instanceof String) {
            userName = (String) user;
        } else {
            Tr.error(tc, "PRINCIPAL_MAPPING_INCORRECT_CLAIM_TYPE", new Object[] { userNameAttr, MicroProfileJwtConfigImpl.KEY_userNameAttribute });
        }
    }

    void populateGroupIds(JwtToken jwtToken, String groupAttr) {
        String methodName = "populateGroupIds";
        if (tc.isDebugEnabled()) {
            Tr.entry(tc, methodName, jwtToken, groupAttr);
        }
        Object groupClaim = null;
        if (groupAttr != null) {
            groupClaim = getClaim(jwtToken, groupAttr);
        }
        populateGroupIdsFromGroupClaim(groupClaim);
        if (tc.isDebugEnabled()) {
            Tr.exit(tc, methodName);
        }
    }

    Object getClaim(JwtToken jwtToken, String claimAttr) {
        String methodName = "getClaim";
        if (tc.isDebugEnabled()) {
            Tr.entry(tc, methodName, jwtToken, claimAttr);
        }
        Object claim = null;

        try {

            if (claims != null) {
                claim = claims.get(claimAttr);
            } else {
                claims = jwtToken.getClaims();

                if (claims != null) {
                    claim = claims.get(claimAttr);
                }
            }

        } catch (Exception e) {
            Tr.error(tc, "CANNOT_GET_CLAIM_FROM_JSON", new Object[] { claimAttr, e.getLocalizedMessage() });
        }
        if (tc.isDebugEnabled()) {
            Tr.exit(tc, methodName, claim);
        }
        return claim;
    }

    void populateGroupIdsFromGroupClaim(Object groupClaim) {
        String methodName = "populateGroupIdsFromGroupClaim";
        if (tc.isDebugEnabled()) {
            Tr.entry(tc, methodName, groupClaim);
        }
        if (groupClaim == null) {
            if (tc.isDebugEnabled()) {
                Tr.exit(tc, methodName);
            }
            return;
        }
        if (groupClaim instanceof ArrayList<?>) {
            setGroupIdArrayList(groupClaim);
        } else {
            setGroupClaimAsOnlyGroupId(groupClaim);
        }
        if (groupIds != null && TraceComponent.isAnyTracingEnabled() && tc.isDebugEnabled()) {
            Tr.debug(tc, "groups size = ", groupIds.size());
        }
        if (tc.isDebugEnabled()) {
            Tr.exit(tc, methodName);
        }
    }

    @SuppressWarnings("unchecked")
    void setGroupIdArrayList(Object groupClaim) {
        String methodName = "setGroupIdArrayList";
        if (tc.isDebugEnabled()) {
            Tr.entry(tc, methodName, groupClaim);
        }
        groupIds = (ArrayList<String>) groupClaim;
        if (tc.isDebugEnabled()) {
            Tr.exit(tc, methodName);
        }
    }

    void setGroupClaimAsOnlyGroupId(Object groupClaim) {
        String methodName = "setGroupClaimAsOnlyGroupId";
        if (tc.isDebugEnabled()) {
            Tr.entry(tc, methodName, groupClaim);
        }
        try {
            String groupName = (String) groupClaim;
            groupIds = new ArrayList<String>();
            groupIds.add(groupName);
        } catch (ClassCastException cce) {
            if (TraceComponent.isAnyTracingEnabled() && tc.isDebugEnabled()) {
                Tr.debug(tc, "cannot get meaningful group due to CCE: " + cce);
            }
        }
        if (tc.isDebugEnabled()) {
            Tr.exit(tc, methodName);
        }
    }

}
