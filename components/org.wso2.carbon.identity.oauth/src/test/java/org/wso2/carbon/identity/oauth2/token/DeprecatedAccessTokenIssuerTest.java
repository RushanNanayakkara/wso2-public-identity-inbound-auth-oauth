/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.oauth2.token;

import org.mockito.Mock;
import org.wso2.carbon.identity.oauth.common.OAuthConstants;
import org.wso2.carbon.identity.oauth.config.OAuthServerConfiguration;
import org.wso2.carbon.identity.oauth.dao.OAuthAppDO;
import org.wso2.carbon.identity.oauth2.dto.OAuth2AccessTokenRespDTO;
import org.wso2.carbon.identity.oauth2.token.handlers.grant.PasswordGrantHandler;
import org.wso2.carbon.identity.oauth2.validators.JDBCPermissionBasedInternalScopeValidator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * Unit test cases for {@link AccessTokenIssuer}
 */

public class DeprecatedAccessTokenIssuerTest {

    public static final String SOME_CLIENT_ID = "some-client-id";
    @Mock
    private OAuthServerConfiguration oAuthServerConfiguration;

    @Mock
    private OAuthAppDO mockOAuthAppDO;

    @Mock
    private PasswordGrantHandler passwordGrantHandler;

    @Mock
    private OAuth2AccessTokenRespDTO mockOAuth2AccessTokenRespDTO;

    @Mock
    private JDBCPermissionBasedInternalScopeValidator scopeValidator;

    private static final String DUMMY_GRANT_TYPE = "dummy_grant_type";
    private static final String ID_TOKEN = "dummyIDToken";
    private static final String[] SCOPES_WITHOUT_OPENID = new String[]{"scope1", "scope2"};
    private static final String[] SCOPES_WITH_OPENID = new String[]{"scope1", OAuthConstants.Scope.OPENID};

//    @BeforeMethod
//    public void setUp() throws Exception {
//
//        mockStatic(CarbonUtils.class);
//
//        mockStatic(OAuthServerConfiguration.class);
//        when(OAuthServerConfiguration.getInstance()).thenReturn(oAuthServerConfiguration);
//        when(oAuthServerConfiguration.getTimeStampSkewInSeconds()).thenReturn(3600L);
//
//        mockStatic(OAuth2Util.class);
//        when(OAuth2Util.getAppInformationByClientId(anyString())).thenReturn(mockOAuthAppDO);
//        when(mockOAuthAppDO.getState()).thenReturn(APP_STATE_ACTIVE);
//        when(OAuth2Util.getTenantDomainOfOauthApp(any(OAuthAppDO.class)))
//                .thenReturn(MultitenantConstants.SUPER_TENANT_DOMAIN_NAME);
//    }
//
//    @AfterMethod
//    public void tearDown() throws Exception {
//        // Reset the singleton
//        Field field = AccessTokenIssuer.class.getDeclaredField("instance");
//        field.setAccessible(true);
//        field.set(null, null);
//    }
//
//    @DataProvider(name = "appConfigProvider")
//    public Object[][] provideAppConfigData() {
//
//        return new Object[][]{
//                {null},
//                {mock(AppInfoCache.class)}
//        };
//    }
//
//    @Test(dataProvider = "appConfigProvider")
//    public void testGetInstance(Object appInfoCache) throws Exception {
//
//        mockStatic(AppInfoCache.class);
//        when(AppInfoCache.getInstance()).thenReturn((AppInfoCache) appInfoCache);
//        CommonTestUtils.testSingleton(AccessTokenIssuer.getInstance(), AccessTokenIssuer.getInstance());
//    }
//
//    @DataProvider(name = "AccessTokenIssue")
//    public Object[][] accessTokenIssue() {
//        // isOfTypeApplicationUser,
//        // isAuthorizedClient,
//        // isValidGrant,
//        // isAuthorizedAccessDelegation,
//        // isValidScope,
//        // isAuthenticatedClient,
//        // isTokenIssuingSuccess
//        return new Object[][]{
//                {true, true, true, true, true, true},
//                {false, true, true, true, true, false},
//                {true, false, true, true, true, false},
//                {true, true, false, true, true, false},
//                {true, true, true, false, true, false},
//                {true, true, true, true, false, false},
//        };
//    }
//
//    @Test(dataProvider = "AccessTokenIssue")
//    public void testIssue(boolean isAuthorizedClient,
//                          boolean isValidGrant,
//                          boolean isAuthorizedAccessDelegation,
//                          boolean isValidScope,
//                          boolean isAuthenticatedClient,
//                          boolean isTokenIssueSuccess) throws IdentityException {
//
//        mockPasswordGrantHandler(isAuthorizedClient, isValidGrant, isAuthorizedAccessDelegation, isValidScope);
//
//        OAuth2AccessTokenReqDTO reqDTO = new OAuth2AccessTokenReqDTO();
//        reqDTO.setGrantType(OAuthConstants.GrantTypes.PASSWORD);
//        reqDTO.setClientId(SOME_CLIENT_ID);
//
//        OAuthClientAuthnContext oAuthClientAuthnContext = new OAuthClientAuthnContext();
//        oAuthClientAuthnContext.setAuthenticated(isAuthenticatedClient);
//        reqDTO.setoAuthClientAuthnContext(oAuthClientAuthnContext);
//
//        AccessTokenIssuer tokenIssuer = AccessTokenIssuer.getInstance();
//        OAuth2AccessTokenRespDTO tokenRespDTO = tokenIssuer.issue(reqDTO);
//
//        if (isTokenIssueSuccess) {
//            Assert.assertFalse(tokenRespDTO.isError());
//        }
//    }
//
//    /**
//     * Multiple Client Authentication mechanisms used to authenticate the request.
//     *
//     * @throws Exception
//     */
//    @Test
//    public void testIssueFailedMultipleClientAuthentication() throws Exception {
//
//        OAuth2AccessTokenReqDTO reqDTO = new OAuth2AccessTokenReqDTO();
//        reqDTO.setGrantType(DUMMY_GRANT_TYPE);
//        OAuthClientAuthnContext oAuthClientAuthnContext = new OAuthClientAuthnContext();
//        oAuthClientAuthnContext.addAuthenticator("ClientAuthenticator1");
//        oAuthClientAuthnContext.addAuthenticator("ClientAuthenticator2");
//        reqDTO.setoAuthClientAuthnContext(oAuthClientAuthnContext);
//        OAuth2AccessTokenRespDTO tokenRespDTO = AccessTokenIssuer.getInstance().issue(reqDTO);
//        assertNotNull(tokenRespDTO);
//        assertTrue(tokenRespDTO.isError());
//        assertEquals(tokenRespDTO.getErrorCode(), OAuthError.TokenResponse.INVALID_REQUEST, "Error Code has been " +
//                "changed. Previously it was: " + OAuthError.TokenResponse.INVALID_REQUEST);
//    }
//
//    @DataProvider(name = "tenantDataProvider")
//    public Object[][] getTenantDomainData() {
//
//        return new Object[][]{
//                {"non_super_tenant.com"},
//                {null}
//        };
//    }
//
//    /**
//     * Tests whether cross tenant token requests fail in tenant qualified URL mode.
//     *
//     * @throws Exception
//     */
//    @Test (dataProvider = "tenantDataProvider" , expectedExceptions = InvalidOAuthClientException.class)
//    public void testCrossTenantTokenRequestError(String tenantInContext) throws Exception {
//
//        OAuth2AccessTokenReqDTO reqDTO = new OAuth2AccessTokenReqDTO();
//        reqDTO.setGrantType("password");
//
//        OAuthClientAuthnContext oAuthClientAuthnContext = new OAuthClientAuthnContext();
//        oAuthClientAuthnContext.setAuthenticated(true);
//        reqDTO.setoAuthClientAuthnContext(oAuthClientAuthnContext);
//
//        mockStatic(IdentityTenantUtil.class);
//        when(IdentityTenantUtil.isTenantQualifiedUrlsEnabled()).thenReturn(true);
//        when(IdentityTenantUtil.getTenantDomainFromContext()).thenReturn(tenantInContext);
//        when(OAuth2Util.class, "validateRequestTenantDomain", anyString()).thenCallRealMethod();
//
//        mockPasswordGrantHandler(true, true, true, true);
//
//        AccessTokenIssuer.getInstance().issue(reqDTO);
//    }
//
//    /**
//     * No authorization grant handler found for the given grant type.
//     *
//     * @throws Exception
//     */
//    @Test
//    public void testIssueNoAuthorizationGrantHandler() throws Exception {
//
//        when(oAuthServerConfiguration.getSupportedGrantTypes())
//                .thenReturn(new HashMap<String, AuthorizationGrantHandler>());
//
//        OAuth2AccessTokenReqDTO reqDTO = new OAuth2AccessTokenReqDTO();
//        reqDTO.setGrantType(DUMMY_GRANT_TYPE);
//        OAuthClientAuthnContext oAuthClientAuthnContext = new OAuthClientAuthnContext();
//        oAuthClientAuthnContext.setAuthenticated(true);
//        reqDTO.setoAuthClientAuthnContext(oAuthClientAuthnContext);
//
//        OAuth2AccessTokenRespDTO tokenRespDTO = AccessTokenIssuer.getInstance().issue(reqDTO);
//        assertNotNull(tokenRespDTO);
//        assertTrue(tokenRespDTO.isError());
//        assertEquals(tokenRespDTO.getErrorCode(), OAuthError.TokenResponse.UNSUPPORTED_GRANT_TYPE);
//    }
//
//    /**
//     * No client authenticators to handle authentication but the grant type is restricted to confidential clients.
//     *
//     * @throws Exception
//     */
//    @Test
//    public void testIssueWithNoClientAuthentication() throws Exception {
//
//        AuthorizationGrantHandler dummyGrantHandler = mock(AuthorizationGrantHandler.class);
//        when(dummyGrantHandler.isConfidentialClient()).thenReturn(true);
//
//        HashMap<String, AuthorizationGrantHandler> authorizationGrantHandlers = new HashMap<>();
//        authorizationGrantHandlers.put(DUMMY_GRANT_TYPE, dummyGrantHandler);
//
//        mockOAuth2ServerConfiguration(authorizationGrantHandlers);
//
//        OAuth2AccessTokenReqDTO reqDTO = new OAuth2AccessTokenReqDTO();
//        reqDTO.setGrantType(DUMMY_GRANT_TYPE);
//
//        OAuth2AccessTokenRespDTO tokenRespDTO = AccessTokenIssuer.getInstance().issue(reqDTO);
//        assertNotNull(tokenRespDTO);
//        assertTrue(tokenRespDTO.isError());
//        assertEquals(tokenRespDTO.getErrorCode(),
//                OAuth2ErrorCodes.INVALID_CLIENT);
//    }
//
//    @DataProvider(name = "unauthorizedClientErrorConditionProvider")
//    public Object[][] getUnauthorizedClientErrorConditions() {
//
//        return new Object[][]{
//                // whether to throw an exception or not for a valid grant, Exception message
//                {true, "Exception when authorizing client."},
//                {false, "The authenticated client is not authorized to use this authorization grant type"}
//        };
//    }
//
//    @Test(dataProvider = "unauthorizedClientErrorConditionProvider")
//    public void testIssueErrorUnauthorizedClient(boolean throwException,
//                                                 String exceptionMsg) throws Exception {
//
//        AuthorizationGrantHandler dummyGrantHandler = mock(AuthorizationGrantHandler.class);
//        when(dummyGrantHandler.isConfidentialClient()).thenReturn(false);
//        // Not a confidential client
//        when(dummyGrantHandler.isOfTypeApplicationUser()).thenReturn(true);
//
//        if (throwException) {
//            when(dummyGrantHandler.isAuthorizedClient(any(OAuthTokenReqMessageContext.class)))
//                    .thenThrow(new IdentityOAuth2Exception(exceptionMsg));
//        } else {
//            // Unauthorized client
//            when(dummyGrantHandler.isAuthorizedClient(any(OAuthTokenReqMessageContext.class))).thenReturn(false);
//        }
//
//        HashMap<String, AuthorizationGrantHandler> authorizationGrantHandlers = new HashMap<>();
//        authorizationGrantHandlers.put(DUMMY_GRANT_TYPE, dummyGrantHandler);
//
//        mockOAuth2ServerConfiguration(authorizationGrantHandlers);
//
//        OAuth2AccessTokenReqDTO reqDTO = new OAuth2AccessTokenReqDTO();
//        reqDTO.setGrantType(DUMMY_GRANT_TYPE);
//
//        OAuthClientAuthnContext oAuthClientAuthnContext = new OAuthClientAuthnContext();
//        oAuthClientAuthnContext.setClientId(SOME_CLIENT_ID);
//        reqDTO.setoAuthClientAuthnContext(oAuthClientAuthnContext);
//
//        OAuth2AccessTokenRespDTO tokenRespDTO = AccessTokenIssuer.getInstance().issue(reqDTO);
//        assertNotNull(tokenRespDTO);
//        assertTrue(tokenRespDTO.isError());
//        assertEquals(tokenRespDTO.getErrorCode(), OAuthError.TokenResponse.UNAUTHORIZED_CLIENT);
//        assertEquals(tokenRespDTO.getErrorMsg(), exceptionMsg);
//    }
//
//    @DataProvider(name = "invalidGrantErrorDataProvider")
//    public Object[][] getInvalidGrantErrorData() {
//
//        return new Object[][]{
//                // whether to throw an exception or not for a valid grant, Exception message
//                {true, "Exception when processing oauth2 grant."},
//                {false, "Provided Authorization Grant is invalid"}
//        };
//    }
//
//    @Test(dataProvider = "invalidGrantErrorDataProvider")
//    public void testIssueValidateGrantError(boolean throwException,
//                                            String exceptionMsg) throws Exception {
//
//        AuthorizationGrantHandler dummyGrantHandler = mock(AuthorizationGrantHandler.class);
//        when(dummyGrantHandler.isConfidentialClient()).thenReturn(false);
//        // Not a confidential client
//        when(dummyGrantHandler.isOfTypeApplicationUser()).thenReturn(true);
//        when(dummyGrantHandler.isAuthorizedClient(any(OAuthTokenReqMessageContext.class))).thenReturn(true);
//
//        if (throwException) {
//            // validate grant will throw an exception
//            when(dummyGrantHandler.validateGrant(any(OAuthTokenReqMessageContext.class)))
//                    .thenThrow(new IdentityOAuth2Exception(exceptionMsg));
//        } else {
//            // validate grant will return false
//            when(dummyGrantHandler.validateGrant(any(OAuthTokenReqMessageContext.class))).thenReturn(false);
//        }
//
//        HashMap<String, AuthorizationGrantHandler> authorizationGrantHandlers = new HashMap<>();
//        authorizationGrantHandlers.put(DUMMY_GRANT_TYPE, dummyGrantHandler);
//
//        mockOAuth2ServerConfiguration(authorizationGrantHandlers);
//
//        OAuth2AccessTokenReqDTO reqDTO = new OAuth2AccessTokenReqDTO();
//        reqDTO.setGrantType(DUMMY_GRANT_TYPE);
//        OAuthClientAuthnContext oAuthClientAuthnContext = new OAuthClientAuthnContext();
//        oAuthClientAuthnContext.setClientId(SOME_CLIENT_ID);
//
//        reqDTO.setoAuthClientAuthnContext(oAuthClientAuthnContext);
//        OAuth2AccessTokenRespDTO tokenRespDTO = AccessTokenIssuer.getInstance().issue(reqDTO);
//        assertNotNull(tokenRespDTO);
//        assertTrue(tokenRespDTO.isError());
//        assertEquals(tokenRespDTO.getErrorCode(), OAuthError.TokenResponse.INVALID_GRANT);
//        assertEquals(tokenRespDTO.getErrorMsg(), exceptionMsg);
//    }
//
//    /**
//     * Exception thrown when issuing access token by the Grant Handler
//     *
//     * @throws Exception
//     */
//    @Test
//    public void testIssueErrorWhenIssue2() throws Exception {
//
//        AuthorizationGrantHandler dummyGrantHandler = getMockGrantHandlerForSuccess(true);
//        when(dummyGrantHandler.issue(any(OAuthTokenReqMessageContext.class))).then(new Answer<Object>() {
//            @Override
//            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
//
//                OAuth2AccessTokenRespDTO accessTokenRespDTO = new OAuth2AccessTokenRespDTO();
//                accessTokenRespDTO.setError(true);
//                return accessTokenRespDTO;
//            }
//        });
//
//        HashMap<String, AuthorizationGrantHandler> authorizationGrantHandlers = new HashMap<>();
//        authorizationGrantHandlers.put(DUMMY_GRANT_TYPE, dummyGrantHandler);
//
//        mockOAuth2ServerConfiguration(authorizationGrantHandlers);
//
//        OAuth2AccessTokenReqDTO reqDTO = new OAuth2AccessTokenReqDTO();
//        reqDTO.setGrantType(DUMMY_GRANT_TYPE);
//        reqDTO.setClientId(SOME_CLIENT_ID);
//
//        OAuth2AccessTokenRespDTO tokenRespDTO = AccessTokenIssuer.getInstance().issue(reqDTO);
//        assertNotNull(tokenRespDTO);
//        assertTrue(tokenRespDTO.isError());
//    }
//
//    @DataProvider(name = "scopeDataProvider")
//    public Object[][] provideDummyData() {
//
//        return new Object[][]{
//                {null, null},
//                {new String[0], null},
//                {SCOPES_WITHOUT_OPENID, "scope1 scope2"},
//                // scopes are not sorted in the OAuth2AccessTokenRespDTO
//                {new String[]{"z", "y", "x"}, "z y x"}
//        };
//    }
//
//    /**
//     * Exception thrown when issuing access token by the Grant Handler
//     *
//     * @throws Exception
//     */
//    @Test(dataProvider = "scopeDataProvider")
//    public void testIssueWithScopes(String[] scopes,
//                                    String expectedScopeString) throws Exception {
//
//        when(OAuth2Util.buildScopeString(Matchers.<String[]>anyObject())).thenCallRealMethod();
//
//        AuthorizationGrantHandler dummyGrantHandler = getMockGrantHandlerForSuccess(false);
//        OAuth2AccessTokenReqDTO reqDTO = new OAuth2AccessTokenReqDTO();
//        reqDTO.setGrantType(DUMMY_GRANT_TYPE);
//        OAuthClientAuthnContext oAuthClientAuthnContext = new OAuthClientAuthnContext();
//        oAuthClientAuthnContext.setClientId(SOME_CLIENT_ID);
//        reqDTO.setoAuthClientAuthnContext(oAuthClientAuthnContext);
//        reqDTO.setScope((String[]) ArrayUtils.clone(scopes));
//
//        final ResponseHeader responseHeader = new ResponseHeader();
//        responseHeader.setKey("Header");
//        responseHeader.setValue("HeaderValue");
//        final ResponseHeader[] responseHeaders = new ResponseHeader[]{responseHeader};
//        // Mock Issue
//        when(dummyGrantHandler.issue(any(OAuthTokenReqMessageContext.class))).then(new Answer<Object>() {
//            @Override
//            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
//
//                OAuthTokenReqMessageContext context =
//                        invocationOnMock.getArgument(0);
//                // set some response headers
//                context.addProperty(OAuthConstants.RESPONSE_HEADERS_PROPERTY, responseHeaders);
//
//                String[] scopeArray = context.getOauth2AccessTokenReqDTO().getScope();
//                context.setScope(scopeArray);
//                return new OAuth2AccessTokenRespDTO();
//            }
//        });
//
//        HashMap<String, AuthorizationGrantHandler> authorizationGrantHandlers = new HashMap<>();
//        authorizationGrantHandlers.put(DUMMY_GRANT_TYPE, dummyGrantHandler);
//
//        mockOAuth2ServerConfiguration(authorizationGrantHandlers);
//        whenNew(JDBCPermissionBasedInternalScopeValidator.class).withNoArguments()
//                .thenReturn(scopeValidator);
//        when(scopeValidator.validateScope(any(OAuthTokenReqMessageContext.class))).thenReturn(null);
//        OAuth2AccessTokenRespDTO tokenRespDTO = AccessTokenIssuer.getInstance().issue(reqDTO);
//
//        assertNotNull(tokenRespDTO);
//        assertFalse(tokenRespDTO.isError());
//        assertEquals(tokenRespDTO.getAuthorizedScopes(), expectedScopeString);
//
//        // Assert response headers set by the grant handler
//        assertNotNull(tokenRespDTO.getResponseHeaders());
//        assertTrue(Arrays.deepEquals(tokenRespDTO.getResponseHeaders(), responseHeaders));
//
//    }
//
//    @DataProvider(name = "grantTypeDataProvider")
//    public Object[][] provideGrantTypes() {
//
//        return new Object[][]{
//                {GrantType.AUTHORIZATION_CODE.toString()},
//                {GrantType.PASSWORD.toString()},
//        };
//    }
//
//    @Test(dataProvider = "grantTypeDataProvider")
//    public void testIssueWithOpenIdScope(String grantType) throws Exception {
//
//        OAuth2AccessTokenReqDTO reqDTO = new OAuth2AccessTokenReqDTO();
//        reqDTO.setGrantType(grantType);
//        reqDTO.setScope((String[]) ArrayUtils.clone(SCOPES_WITH_OPENID));
//        OAuthClientAuthnContext oAuthClientAuthnContext = new OAuthClientAuthnContext();
//        oAuthClientAuthnContext.setClientId(SOME_CLIENT_ID);
//        reqDTO.setoAuthClientAuthnContext(oAuthClientAuthnContext);
//        setupOIDCScopeTest(grantType, true);
//        OAuth2AccessTokenRespDTO tokenRespDTO = AccessTokenIssuer.getInstance().issue(reqDTO);
//
//        assertNotNull(tokenRespDTO);
//        assertFalse(tokenRespDTO.isError());
//        assertTrue(Arrays.deepEquals(tokenRespDTO.getAuthorizedScopes().split(" "), SCOPES_WITH_OPENID));
//        assertNotNull(tokenRespDTO.getIDToken());
//        assertEquals(tokenRespDTO.getIDToken(), ID_TOKEN);
//    }
//
//    @Test
//    public void testIssueWithOpenIdScopeFailure() throws Exception {
//
//        OAuth2AccessTokenReqDTO reqDTO = new OAuth2AccessTokenReqDTO();
//        reqDTO.setGrantType(DUMMY_GRANT_TYPE);
//        reqDTO.setScope(SCOPES_WITH_OPENID);
//
//        setupOIDCScopeTest(DUMMY_GRANT_TYPE, false);
//
//        OAuthClientAuthnContext oAuthClientAuthnContext = new OAuthClientAuthnContext();
//        oAuthClientAuthnContext.setClientId(SOME_CLIENT_ID);
//        reqDTO.setoAuthClientAuthnContext(oAuthClientAuthnContext);
//
//        OAuth2AccessTokenRespDTO tokenRespDTO = AccessTokenIssuer.getInstance().issue(reqDTO);
//
//        assertNotNull(tokenRespDTO);
//        assertTrue(tokenRespDTO.isError());
//        assertEquals(tokenRespDTO.getErrorCode(), OAuth2ErrorCodes.SERVER_ERROR);
//        // ID Token should not be set
//        assertNull(tokenRespDTO.getIDToken());
//    }
//
//    @DataProvider(name = "clientAuthContextDataProvider")
//    public Object[][] clientAuthContextDataProvider() {
//
//        return new Object[][]{
//
//                // Authenticaion has failed from a single authenticator with invalid_client error code.
//                {"sampleID", OAuth2ErrorCodes.INVALID_CLIENT, false, "BasicAuthenticator", null, OAuth2ErrorCodes
//                        .INVALID_CLIENT, true, false},
//
//                // Authentication success scenario.
//                {"sampleID", OAuth2ErrorCodes.INVALID_CLIENT, true, "BasicAuthenticator", null, null, true, true},
//
//                // Authentication has failed from a single authenticator with invalid_request error code
//                {"sampleID", OAuth2ErrorCodes.INVALID_REQUEST, false, "BasicAuthenticator", null, OAuth2ErrorCodes
//                        .INVALID_REQUEST, true, false},
//
//                // Multiple authenticators are engaged. Eventhough the error message set to context is
//                // invalid_client, the actual error message should be invalid_request.
//                {"sampleID", OAuth2ErrorCodes.INVALID_CLIENT, false, "BasicAuthenticator", "AnotherAuthenticator",
//                        OAuth2ErrorCodes
//                                .INVALID_REQUEST, true, false},
//
//                // Non confidential grant type. Hence authentication is not required.
//                {"sampleID", OAuth2ErrorCodes.INVALID_CLIENT, false, "BasicAuthenticator", null,
//                        null, false, true},
//
//                // Multiple authenticators are engaged. Hence invalid request.
//                {"sampleID", OAuth2ErrorCodes.INVALID_CLIENT, false, "BasicAuthenticator", "AnotherAuthenticator",
//                        OAuth2ErrorCodes.INVALID_REQUEST, false, false},
//
//                // No authenticator engaged. Hence authentication should fail
//                {"sampleID", OAuth2ErrorCodes.INVALID_CLIENT, false, null, null,
//                        OAuth2ErrorCodes.INVALID_CLIENT, true, false},
//
//                // Non confidential apps doesn't need authentication
//                {"sampleID", OAuth2ErrorCodes.INVALID_CLIENT, false, null, null,
//                        null, false, true},
//        };
//
//    }
//
//    /**
//     * Make sure oauth client authenticaion is done with context data.
//     *
//     * @throws Exception
//     */
//    @Test(dataProvider = "clientAuthContextDataProvider")
//    public void testClientAuthenticaion(String clientId, String errorCode, boolean isAuthenticated, String
//            authenticator1, String authenticator2, String expectedErrorCode, boolean isConfidential, boolean
//                                                authnResult) throws Exception {
//
//        OAuthClientAuthnContext oAuthClientAuthnContext = new OAuthClientAuthnContext();
//        oAuthClientAuthnContext.setClientId(clientId);
//        oAuthClientAuthnContext.setErrorCode(errorCode);
//        oAuthClientAuthnContext.setAuthenticated(isAuthenticated);
//
//        if (StringUtils.isNotEmpty(authenticator1)) {
//            oAuthClientAuthnContext.addAuthenticator(authenticator1);
//        }
//        if (StringUtils.isNotEmpty(authenticator2)) {
//            oAuthClientAuthnContext.addAuthenticator(authenticator2);
//        }
//
//        AuthorizationGrantHandler dummyGrantHandler = getMockGrantHandlerForSuccess(true);
//
//        final ResponseHeader responseHeader = new ResponseHeader();
//        responseHeader.setKey("Header");
//        responseHeader.setValue("HeaderValue");
//        final ResponseHeader[] responseHeaders = new ResponseHeader[]{responseHeader};
//
//        when(dummyGrantHandler.issue(any(OAuthTokenReqMessageContext.class))).then(new Answer<Object>() {
//            @Override
//            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
//
//                OAuthTokenReqMessageContext context =
//                        invocationOnMock.getArgument(0);
//                // set some response headers
//                context.addProperty(OAuthConstants.RESPONSE_HEADERS_PROPERTY, responseHeaders);
//
//                String[] scopeArray = context.getOauth2AccessTokenReqDTO().getScope();
//                context.setScope(scopeArray);
//                return new OAuth2AccessTokenRespDTO();
//            }
//        });
//
//        when(dummyGrantHandler.isConfidentialClient()).thenReturn(isConfidential);
//
//        HashMap<String, AuthorizationGrantHandler> authorizationGrantHandlers = new HashMap<>();
//        authorizationGrantHandlers.put(DUMMY_GRANT_TYPE, dummyGrantHandler);
//
//        mockOAuth2ServerConfiguration(authorizationGrantHandlers);
//
//        OAuth2AccessTokenReqDTO reqDTO = new OAuth2AccessTokenReqDTO();
//        reqDTO.setGrantType(DUMMY_GRANT_TYPE);
//        reqDTO.setoAuthClientAuthnContext(oAuthClientAuthnContext);
//
//        OAuth2AccessTokenRespDTO tokenRespDTO = AccessTokenIssuer.getInstance().issue(reqDTO);
//        assertNotNull(tokenRespDTO);
//        assertEquals(tokenRespDTO.isError(), !authnResult);
//        assertEquals(tokenRespDTO.getErrorCode(), expectedErrorCode);
//    }
//
//    /**
//     * Test whether the client ID sent in error response for a invalid client token request, is properly encoded.
//     *
//     * @throws Exception
//     */
//    @Test
//    public void testIssueWithInvalidClient() throws Exception {
//
//        OAuthClientAuthnContext oAuthClientAuthnContext = new OAuthClientAuthnContext();
//        oAuthClientAuthnContext.setClientId("sampleID");
//        oAuthClientAuthnContext.setErrorCode(OAuth2ErrorCodes.INVALID_CLIENT);
//        oAuthClientAuthnContext.setAuthenticated(false);
//
//        String malicousClientID = "<img src=a onerror=alert(1)>";
//        String encodedClientID = "&lt;img src=a onerror=alert(1)&gt;";
//
//        OAuth2AccessTokenReqDTO reqDTO = new OAuth2AccessTokenReqDTO();
//        reqDTO.setGrantType(DUMMY_GRANT_TYPE);
//        reqDTO.setoAuthClientAuthnContext(oAuthClientAuthnContext);
//        reqDTO.setClientId(malicousClientID);
//        when(mockOAuthAppDO.getState()).thenReturn(null);
//
//        try {
//            AccessTokenIssuer.getInstance().issue(reqDTO);
//        } catch (InvalidOAuthClientException ex) {
//            assertTrue(ex.getMessage().contains(encodedClientID));
//        }
//    }
//
//    private AuthorizationGrantHandler getMockGrantHandlerForSuccess(boolean isOfTypeApplicationUser)
//            throws IdentityOAuth2Exception {
//
//        AuthorizationGrantHandler dummyGrantHandler = mock(AuthorizationGrantHandler.class);
//        // Not a confidential client
//        when(dummyGrantHandler.isConfidentialClient()).thenReturn(false);
//        // This grant issue token for an APPLICATION
//        when(dummyGrantHandler.isOfTypeApplicationUser()).thenReturn(isOfTypeApplicationUser);
//        // Unauthorized client
//        when(dummyGrantHandler.isAuthorizedClient(any(OAuthTokenReqMessageContext.class))).thenReturn(true);
//        when(dummyGrantHandler.validateGrant(any(OAuthTokenReqMessageContext.class))).thenReturn(true);
//        when(dummyGrantHandler.validateScope(any(OAuthTokenReqMessageContext.class))).thenReturn(true);
//        when(dummyGrantHandler.authorizeAccessDelegation(any(OAuthTokenReqMessageContext.class)))
//                .thenReturn(true);
//        return dummyGrantHandler;
//    }
//
//    private void mockOAuth2ServerConfiguration(Map<String, AuthorizationGrantHandler> authorizationGrantHandlerMap) {
//
//        when(oAuthServerConfiguration.getSupportedGrantTypes()).thenReturn(authorizationGrantHandlerMap);
//    }
//
//    private void setupOIDCScopeTest(String grantType,
//                                    boolean success) throws IdentityOAuth2Exception {
//
//        AuthorizationGrantHandler grantHandler = getMockGrantHandlerForSuccess(false);
//
//        when(OAuth2Util.buildScopeString(Matchers.<String[]>anyObject())).thenCallRealMethod();
//        when(OAuth2Util.isOIDCAuthzRequest(Matchers.<String[]>anyObject())).thenCallRealMethod();
//
//        IDTokenBuilder idTokenBuilder;
//        if (success) {
//            idTokenBuilder = getMockIDTokenBuilderForSuccess();
//        } else {
//            idTokenBuilder = getMockIDTokenBuilderForFailure();
//        }
//
//        when(oAuthServerConfiguration.getOpenIDConnectIDTokenBuilder()).thenReturn(idTokenBuilder);
//
//        // Mock Issue method of the grant handler
//        when(grantHandler.issue(any(OAuthTokenReqMessageContext.class))).then(new Answer<Object>() {
//            @Override
//            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
//
//                OAuthTokenReqMessageContext context =
//                        invocationOnMock.getArgument(0);
//
//                // set the scope sent in the request
//                String[] scopeArray = context.getOauth2AccessTokenReqDTO().getScope();
//
//                // Set the scope array for OIDC
//                context.setScope(scopeArray);
//                return new OAuth2AccessTokenRespDTO();
//            }
//        });
//
//        HashMap<String, AuthorizationGrantHandler> authorizationGrantHandlers = new HashMap<>();
//        authorizationGrantHandlers.put(grantType, grantHandler);
//
//        mockOAuth2ServerConfiguration(authorizationGrantHandlers);
//    }
//
//    private IDTokenBuilder getMockIDTokenBuilderForSuccess() throws IdentityOAuth2Exception {
//
//        IDTokenBuilder idTokenBuilder = mock(IDTokenBuilder.class);
//        when(idTokenBuilder.buildIDToken(any(OAuthTokenReqMessageContext.class), any(OAuth2AccessTokenRespDTO.class)))
//                .then(new Answer<Object>() {
//                    @Override
//                    public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
//
//                        return ID_TOKEN;
//                    }
//                });
//        return idTokenBuilder;
//    }
//
//    private IDTokenBuilder getMockIDTokenBuilderForFailure() throws IdentityOAuth2Exception {
//
//        IDTokenBuilder idTokenBuilder = mock(IDTokenBuilder.class);
//        when(idTokenBuilder.buildIDToken(any(OAuthTokenReqMessageContext.class), any(OAuth2AccessTokenRespDTO.class)))
//                .thenThrow(new IDTokenValidationFailureException("ID Token Validation failed"));
//        return idTokenBuilder;
//    }
//
//    private void mockPasswordGrantHandler(boolean isAuthorizedClient, boolean isValidGrant,
//                                          boolean isAuthorizedAccessDelegation, boolean isValidScope)
//            throws IdentityOAuth2Exception {
//
//        Map<String, AuthorizationGrantHandler> authzGrantHandlers = new Hashtable<>();
//
//        when(passwordGrantHandler.isOfTypeApplicationUser()).thenReturn(true);
//        when(passwordGrantHandler.isAuthorizedClient(any(OAuthTokenReqMessageContext.class)))
//                .thenReturn(isAuthorizedClient);
//        when(passwordGrantHandler.validateGrant(any(OAuthTokenReqMessageContext.class))).thenReturn(isValidGrant);
//        when(passwordGrantHandler.authorizeAccessDelegation(any(OAuthTokenReqMessageContext.class)))
//                .thenReturn(isAuthorizedAccessDelegation);
//        when(passwordGrantHandler.validateScope(any(OAuthTokenReqMessageContext.class))).thenReturn(isValidScope);
//        when(passwordGrantHandler.issue(any(OAuthTokenReqMessageContext.class)))
//                .thenReturn(new OAuth2AccessTokenRespDTO());
//        authzGrantHandlers.put("password", passwordGrantHandler);
//        when(passwordGrantHandler.isConfidentialClient()).thenReturn(true);
//
//        when(oAuthServerConfiguration.getSupportedGrantTypes()).thenReturn(authzGrantHandlers);
//    }
}
