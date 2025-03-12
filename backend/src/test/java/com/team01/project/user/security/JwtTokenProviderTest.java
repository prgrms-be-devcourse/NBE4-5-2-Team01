package com.team01.project.user.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.team01.project.global.security.JwtTokenProvider;

@SpringBootTest
public class JwtTokenProviderTest {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Test
	void testCreateToken() {
		String userId = "31bpa2axr7wfpwwnsfvnne6jeg6a";
		String spotifyAccessToken = "BQBqJk50nj1Wjx0mi6ki0V0GKsuC8VDugE7i9_Ub8UulbCLzV1-LEGIzpkjyhxBDts42-qJXIRVZGN-81K2_KSiIlBdXSmi_OgGZStQNWlQahmKBnBoVk02PuA5NeYNRFZwz5i8zWf9ZPKGljDh6_4bDY2bc_5EabiGBfNsUh5eN9SeFtRhNCPXWuSs8f9mX9zfpgdLvLKbxvx6MjIUZF0i87uKpfy-GkknSE5l6W6FvwDDNvlCV6A";

		String token = jwtTokenProvider.createToken(userId, spotifyAccessToken);
		String extractedUserId = jwtTokenProvider.getUserIdFromToken(token);

		System.out.println("토큰:" + token);
		System.out.println("토큰에서 추출한 사용자 ID:" + extractedUserId);
		assertNotNull(token);
		assertEquals(userId, extractedUserId);
	}

	@Test
	void testValidateTokenValid() {
		// String userId = "31bpa2axr7wfpwwnsfvnne6jeg6a";
		// String spotifyAccessToken = "BQB7fDEBQU4UBh_gYSPzmtUiWrMS3r_ITGAI1szcsG0-txT63eBMTFgWUUq_QAKGZgOujHZLtn3X82TdwCCxAC1rRXDXfB_OpKAxEfLkt4jeGYcW0vc6jU3DDoxf-ln0qlRWgpSdqiwjNNZI9xttNmSg4agmKavx5SfdeYWx2otLJm87PE2vKFtij2tO1_EZCgbVQOVLTAAPoQdXU8jJTPiaHaznJB8SgfZY7ikmsFf8Wuj5KOvq8Q";
		// String token = jwtTokenProvider.createToken(userId, spotifyAccessToken);
		// System.out.println("서버토큰:" + token);
		String clientToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzMWJwYTJheHI3d2Zwd3duc2Z2bm5lNmplZzZhIiwic3BvdGlmeVRva2VuIjoiQlFCNTBFX3NOczlLRFBFREJuR2NoVjJrc1IwbzV5UHg3QW9ZaEV3ajBRcldRT0p3RHowN0NBa2tsamtvSlprdkl5SkJfN0xGWk1ZLWREdXUxNFB0QnYtck1qY2NSeU85SEtrTVphSWs4blBmMzV0bDV4LXdCREx5Q2tuLWk4aVFoaFBCQlYxX3BwSEphVXNBb3ZxT3M2a3VvZWRXQVJWLWtxUDNnSEw0QkkwcjhEUmhBb1FuSGh5UlktaUJhTS1RUGhRdEtqUm5sbFhZbWxwX0I2NDNpbDhtOUdsVkFxcmNsNEJlZWxTN3ZLbUF3cGdjOHhucmFBIiwiaWF0IjoxNzQxNzU0MjQzLCJleHAiOjE3NDE3NTc4NDN9.C5gO18PkeKjmxhiE834CouYLQr-jujjp9IMNKTnh_2o";
		boolean isValid = jwtTokenProvider.validateToken(clientToken);

		assertTrue(isValid);
	}

	@Test
	void testValidateTokenInvaild() {

		String invalidToken = "invalid.token.value";

		boolean isValid = jwtTokenProvider.validateToken(invalidToken);

		assertFalse(isValid);
	}
}
