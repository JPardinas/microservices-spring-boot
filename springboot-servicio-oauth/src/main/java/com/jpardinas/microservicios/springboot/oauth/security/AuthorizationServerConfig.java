package com.jpardinas.microservicios.springboot.oauth.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@RefreshScope
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	
	@Autowired
	private Environment env;
	
	// Declarados en SpringSecurityConfig.class
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private InfoAdicionalToken infoAdicionalToken;
	
	/* #1#
	 * 
	 * Se configura:
	 * 	- AuthorizationManager
	 *  - TokenStorage: tipo jwt
	 *  - AccestokenConverter: guarda los datos del usuario en el token (claims -> informacion extra)
	 *  	- Se encarga de tomar los valores y convertirlos en el token codificado a base64
	 * 
	 * */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		
		// Para anadir la informacion extra al token
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(infoAdicionalToken, accessTokenConverter()));
		
		
		
		// Le introducimos el autentificador configurado en SpringSecurityConfig
		endpoints.authenticationManager(authenticationManager)
			// 2. 
			.tokenStore(tokenStore())
			// 1. Creamos e introducimos el token converter de tipo JWT
			.accessTokenConverter(accessTokenConverter())
			.tokenEnhancer(tokenEnhancerChain);
	}
	
	
	
	// 1.1
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		// Creamos el token y añadimos el codigo secreto para el servidor de recursos
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey(env.getProperty("config.security.oauth.jwt.key"));
		return tokenConverter;
	}
	
	
	// 1.2
	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
	
	

	/* #2#
	 * 
	 * Configurar authentificacion de la aplicacion cliente
	 * 
	 * */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
			// Username cliente
			.withClient(env.getProperty("config.security.oauth.client.id"))
			// Password codificada
			.secret(passwordEncoder.encode(env.getProperty("config.security.oauth.client.secret")))
			// Permisos
			.scopes("read", "write")
			// Tipo de autentificacion, como obtener token. Con password en este caso -> datos app ciente + datos usuario
			// Token que permite obtener token renovado
			.authorizedGrantTypes("password", "refresh_token")
			// Validez del token en segundos
			.accessTokenValiditySeconds(3600)
			// Tiempo del refresh token
			.refreshTokenValiditySeconds(3600);
		// .and() para anadir mas clientes
		
		// 'Basic ' + Base64.encode('frontendapp' + ':' + '12345') -> Basic ZnJvbnRlbmRhcHA6MTIzNDU=
	}
	
	
	/* #3#
	 * 
	 * Permisos endpoints del servidor de autentificacion para generar y validar el token
	 * 
	 * */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		// Lo logico es que sea publico - POST: /oauth/token
		security.tokenKeyAccess("permitAll()")
			// Validador del token -> ruta de validacion del token requerimos autentificacion
			// Metodo spring security valida cliente autenticado
			.checkTokenAccess("isAuthenticated()");
	}

}
