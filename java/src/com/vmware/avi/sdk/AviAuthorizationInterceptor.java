package com.vmware.avi.sdk;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

public class AviAuthorizationInterceptor implements ClientHttpRequestInterceptor {
	
	private AviCredentials aviCredentials;
	/**
	 * Maintains count of execution at the time of retries.
	 */
	private int numApiExecCount = 0;
	
	public AviAuthorizationInterceptor(AviCredentials aviCredentials) {
		this.aviCredentials = aviCredentials;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		if (null == this.aviCredentials.getSessionID() || this.aviCredentials.getSessionID().isEmpty()) {
			AviRestUtils.authenticateSession(this.aviCredentials);
		}
		HttpHeaders headers = request.getHeaders();
		headers.addIfAbsent("Content-Type", "application/json");
		headers.addIfAbsent("X-Avi-Version", this.aviCredentials.getVersion());
		headers.addIfAbsent("X-Avi-Tenant", this.aviCredentials.getTenant());
		headers.add("X-CSRFToken", this.aviCredentials.getCsrftoken());
		headers.addIfAbsent("Referer", AviRestUtils.getControllerURL(this.aviCredentials));
		headers.add(HttpHeaders.COOKIE, "csrftoken=" + this.aviCredentials.getCsrftoken() + "; " + "avi-sessionid="
				+ this.aviCredentials.getSessionID());
		
		ClientHttpResponse response = execution.execute(request, body);
		
		int responseCode = response.getRawStatusCode();
		
		if (Arrays.asList(419, 401).contains(responseCode)) {
			String response_str = StreamUtils.copyToString(response.getBody(), Charset.defaultCharset());
			this.numApiExecCount++;
			if (numApiExecCount < this.aviCredentials.getNumApiRetries()) {
				headers.remove("X-CSRFToken");
				headers.remove("Cookie");
				AviRestUtils.authenticateSession(this.aviCredentials);
				headers.add("X-CSRFToken", this.aviCredentials.getCsrftoken());
				headers.add("Cookie", "csrftoken=" + this.aviCredentials.getCsrftoken() + "; " + "avi-sessionid="
						+ this.aviCredentials.getSessionID());
				response = execution.execute(request, body);
				this.numApiExecCount = 0;
			}
		}

		return response;
	}

}