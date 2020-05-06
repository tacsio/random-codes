package io.tacsio.quarkus.jwt.config;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

import org.eclipse.microprofile.jwt.Claims;

import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;

public class TokenUtils {

	private static final String PK_STRING = "/rsa.private";

	private TokenUtils() {
	}

	/**
	 * Utility method to generate a JWT string from a JSON resource file that is
	 * signed by the rsa.private test resource key, possibly with invalid fields.
	 *
	 * @param jsonResName - name of test resources file
	 * @param timeClaims  - used to return the exp, iat, auth_time claims
	 * @return the JWT string
	 * @throws Exception on parse failure
	 */
	public static String generateTokenString(String jsonResName, Map<String, Long> timeClaims) throws Exception {
		PrivateKey pk = readPrivateKey(PK_STRING);

		return generateTokenString(pk, PK_STRING, jsonResName, timeClaims);
	}

	public static String generateTokenString(PrivateKey privateKey, String kid, String jsonResName,
			Map<String, Long> timeClaims) throws Exception {

		JwtClaimsBuilder claims = Jwt.claims(jsonResName);
		long currentTimeInSecs = currentTimeInSecs();
		long exp = timeClaims != null && timeClaims.containsKey(Claims.exp.name()) ? timeClaims.get(Claims.exp.name())
				: currentTimeInSecs + 300;

		claims.issuedAt(currentTimeInSecs);
		claims.claim(Claims.auth_time.name(), currentTimeInSecs);
		claims.expiresAt(exp);

		return claims.jws().signatureKeyId(kid).sign(privateKey);
	}

	/**
	 * Read a PEM encoded private key from the classpath
	 *
	 * @param pemResName - key file resource name
	 * @return PrivateKey
	 * @throws Exception on decode failure
	 */
	public static PrivateKey readPrivateKey(final String pemResName) throws Exception {
		try (InputStream inputStream = TokenUtils.class.getResourceAsStream(pemResName)) {
			byte[] tmp = new byte[2048];
			int length = inputStream.read(tmp);
			return decodePrivateKey(new String(tmp, 0, length, StandardCharsets.UTF_8));
		}
	}

	/**
	 * Decode a PEM encoded private key string to an RSA PrivateKey
	 *
	 * @param pemEncoded - PEM string for private key
	 * @return PrivateKey
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws Exception                on decode failure
	 */
	private static PrivateKey decodePrivateKey(String pemEncoded)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] encodedKey = toEncodedBytes(pemEncoded);

		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
		KeyFactory factory = KeyFactory.getInstance("RSA");

		return factory.generatePrivate(keySpec);
	}

	private static byte[] toEncodedBytes(final String pemEncoded) {
		final String normalizedPem = removeBeginEnd(pemEncoded);
		return Base64.getDecoder().decode(normalizedPem);
	}

	private static String removeBeginEnd(String pem) {
		pem = pem.replaceAll("-----BEGIN (.*)-----", "");
		pem = pem.replaceAll("-----END (.*)----", "");
		pem = pem.replaceAll("\r\n", "");
		pem = pem.replaceAll("\n", "");
		return pem.trim();
	}

	/**
	 * @return the current time in seconds since epoch
	 */
	public static int currentTimeInSecs() {
		long currentTimeMS = System.currentTimeMillis();
		return (int) (currentTimeMS / 1000);
	}
}