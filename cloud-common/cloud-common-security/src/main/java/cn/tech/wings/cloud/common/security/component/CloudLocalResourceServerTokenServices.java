package cn.tech.wings.cloud.common.security.component;

import cn.tech.wings.cloud.common.security.exception.UnauthorizedException;
import cn.tech.wings.cloud.common.core.util.SpringContextHolder;
import cn.tech.wings.cloud.common.security.service.CloudUser;
import cn.tech.wings.cloud.common.security.service.CloudUserDetailsService;
import cn.tech.wings.cloud.common.security.util.CloudSecurityMessageSourceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

/**
 * @author cloud
 * @date 2020/9/29
 */
@Primary
@Service
@RequiredArgsConstructor
public class CloudLocalResourceServerTokenServices implements ResourceServerTokenServices {

	private final TokenStore tokenStore;

	private final UserDetailsChecker preAuthenticationChecks;

	@Override
	public OAuth2Authentication loadAuthentication(String accessToken)
			throws AuthenticationException, InvalidTokenException {
		OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(accessToken);
		if (oAuth2Authentication == null) {
			return null;
		}

		OAuth2Request oAuth2Request = oAuth2Authentication.getOAuth2Request();
		if (!(oAuth2Authentication.getPrincipal() instanceof CloudUser)) {
			return oAuth2Authentication;
		}

		String clientId = oAuth2Request.getClientId();

		Map<String, CloudUserDetailsService> userDetailsServiceMap = SpringContextHolder
				.getBeansOfType(CloudUserDetailsService.class);

		Optional<CloudUserDetailsService> optional = userDetailsServiceMap.values().stream()
				.filter(service -> service.support(clientId, oAuth2Request.getGrantType()))
				.max(Comparator.comparingInt(Ordered::getOrder));

		if (!optional.isPresent()) {
			throw new InternalAuthenticationServiceException("UserDetailsService error , not register");
		}

		// 根据 username 查询 spring cache 最新的值 并返回
		CloudUser pigUser = (CloudUser) oAuth2Authentication.getPrincipal();

		UserDetails userDetails;
		try {
			userDetails = optional.get().loadUserByUser(pigUser);

			preAuthenticationChecks.check(userDetails);
		}
		catch (UsernameNotFoundException notFoundException) {
			throw new UnauthorizedException(String.format("%s username not found", pigUser.getUsername()),
					notFoundException);
		}
		catch (AccountStatusException accountStatusException) {
			throw new UnauthorizedException(
					CloudSecurityMessageSourceUtil.getAccessor().getMessage(
							"AbstractUserDetailsAuthenticationProvider.locked", accountStatusException.getMessage()),
					accountStatusException);
		}
		Authentication userAuthentication = new UsernamePasswordAuthenticationToken(userDetails, "N/A",
				userDetails.getAuthorities());
		OAuth2Authentication authentication = new OAuth2Authentication(oAuth2Request, userAuthentication);
		authentication.setAuthenticated(true);
		return authentication;
	}

	@Override
	public OAuth2AccessToken readAccessToken(String accessToken) {
		throw new UnsupportedOperationException("Not supported: read access token");
	}

}
