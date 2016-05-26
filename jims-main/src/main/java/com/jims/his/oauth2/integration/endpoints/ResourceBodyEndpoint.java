/**
 *       Copyright 2010 Newcastle University
 *
 *          http://research.ncl.ac.uk/smart/
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jims.his.oauth2.integration.endpoints;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jims.his.oauth2.common.OAuth;
import com.jims.his.oauth2.common.error.OAuthError;
import com.jims.his.oauth2.common.exception.OAuthProblemException;
import com.jims.his.oauth2.common.exception.OAuthSystemException;
import com.jims.his.oauth2.common.message.OAuthResponse;
import com.jims.his.oauth2.common.utils.OAuthUtils;
import com.jims.his.oauth2.integration.Common;
import com.jims.his.oauth2.rs.request.OAuthAccessResourceRequest;
import com.jims.his.oauth2.rs.response.OAuthRSResponse;
import com.jims.his.oauth2.common.message.types.ParameterStyle;

/**
 *
 *
 *
 */
@Path("/resource_body")
public class ResourceBodyEndpoint {

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/html")
    public Response get(@Context HttpServletRequest request) throws OAuthSystemException {

        try {

        	//构建OAuth资源请求
            OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(request,
                ParameterStyle.BODY);

            //获取Access Token
            String accessToken = oauthRequest.getAccessToken();

            //验证Access Token
            if (Common.ACCESS_TOKEN_VALID.equals(accessToken)) {

                // Return the resource
                return Response.status(Response.Status.OK).entity(accessToken).build();

            }


            // Check if the token is not expired
            if (Common.ACCESS_TOKEN_EXPIRED.equals(accessToken)) {

                // Return the OAuth error message
                OAuthResponse oauthResponse = OAuthRSResponse
                    .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                    .setRealm(Common.RESOURCE_SERVER_NAME)
                    .setError(OAuthError.ResourceResponse.EXPIRED_TOKEN)
                    .buildHeaderMessage();

                // Return the error message
                return Response.status(Response.Status.UNAUTHORIZED)
                    .header(OAuth.HeaderType.WWW_AUTHENTICATE,
                        oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE))
                    .build();
            }


            // Check if the token is sufficient
            if (Common.ACCESS_TOKEN_INSUFFICIENT.equals(accessToken)) {

                // Return the OAuth error message
                OAuthResponse oauthResponse = OAuthRSResponse
                    .errorResponse(HttpServletResponse.SC_FORBIDDEN)
                    .setRealm(Common.RESOURCE_SERVER_NAME)
                    .setError(OAuthError.ResourceResponse.INSUFFICIENT_SCOPE)
                    .buildHeaderMessage();

                // Return the error message
                return Response.status(Response.Status.FORBIDDEN)
                    .header(OAuth.HeaderType.WWW_AUTHENTICATE,
                        oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE))
                    .build();
            }


            // Return the OAuth error message
            OAuthResponse oauthResponse = OAuthRSResponse
                .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                .setRealm(Common.RESOURCE_SERVER_NAME)
                .setError(OAuthError.ResourceResponse.INVALID_TOKEN)
                .buildHeaderMessage();

            //return Response.status(Response.Status.UNAUTHORIZED).build();
            return Response.status(Response.Status.UNAUTHORIZED)
                .header(OAuth.HeaderType.WWW_AUTHENTICATE,
                    oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE))
                .build();

        } catch (OAuthProblemException e) {

            // Check if the error code has been set
            String errorCode = e.getError();
            if (OAuthUtils.isEmpty(errorCode)) {

                // Return the OAuth error message
                OAuthResponse oauthResponse = OAuthRSResponse
                    .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                    .setRealm(Common.RESOURCE_SERVER_NAME)
                    .buildHeaderMessage();

                // If no error code then return a standard 401 Unauthorized response
                return Response.status(Response.Status.UNAUTHORIZED)
                    .header(OAuth.HeaderType.WWW_AUTHENTICATE,
                        oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE))
                    .build();
            }

            OAuthResponse oauthResponse = OAuthRSResponse
                .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setRealm(Common.RESOURCE_SERVER_NAME)
                .setError(e.getError())
                .setErrorDescription(e.getDescription())
                .setErrorUri(e.getUri())
                .buildHeaderMessage();

            return Response.status(oauthResponse.getResponseStatus())
                .header(OAuth.HeaderType.WWW_AUTHENTICATE,
                    oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE))
                .build();
        }
    }

}
