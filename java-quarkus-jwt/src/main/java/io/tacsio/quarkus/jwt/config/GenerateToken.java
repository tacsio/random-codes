package io.tacsio.quarkus.jwt.config;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.microprofile.jwt.Claims;

public class GenerateToken {
	public static void main(String[] args) throws Exception {
		String claimsJson = "/JwtClaims.json";
		if (args.length > 0) {
			System.out.println(args[0]);
			
			claimsJson = args[0];
		}

		Map<String, Long> timeClaims = new HashMap<>();
		if (args.length > 1) {
			System.out.println(args[1]);

			long duration = Long.parseLong(args[1]);
			long exp = TokenUtils.currentTimeInSecs() + duration;
			timeClaims.put(Claims.exp.name(), exp);
		}
		String token = TokenUtils.generateTokenString(claimsJson, timeClaims);
		System.out.println(token);
	}
}