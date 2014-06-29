package fr.neraud.padlistener.proxy.plugin;

import android.content.Context;
import android.util.Log;

import org.sandrop.webscarab.httpclient.HTTPClient;
import org.sandrop.webscarab.model.HttpUrl;
import org.sandrop.webscarab.model.Request;
import org.sandrop.webscarab.model.Response;
import org.sandrop.webscarab.plugin.proxy.ProxyPlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;
import fr.neraud.padlistener.pad.constant.ApiAction;
import fr.neraud.padlistener.pad.model.ApiCallModel;

/**
 * ProxyPlugin to capture PAD calls to GunHo servers
 *
 * @author Neraud
 */
public class PADPlugin extends ProxyPlugin {

	private final Context context;
	private final String targetHost;
	private boolean _enabled = true;

	private class Plugin implements HTTPClient {

		private final HTTPClient _in;

		public Plugin(HTTPClient in) {
			_in = in;
		}

		@Override
		public Response fetchResponse(Request request) throws IOException {
			if (_enabled) {
				final Response response = _in.fetchResponse(request);

				final HttpUrl reqUrl = request.getURL();
				Log.d(PADPlugin.class.getName(), "fetchResponse : " + reqUrl);
				final String reqHost = reqUrl.getHost();
				final String reqPath = reqUrl.getPath();

				// Read only calls to GunHo API, with a 200 HTTP code
				if ("200".equals(response.getStatus()) && targetHost.equals(reqHost) && "/api.php".equals(reqPath)) {
					final byte[] requestContentByte = request.getContent();
					final String requestContentString = new String(requestContentByte);
					final Map<String, String> requestParams = extractParams(reqUrl);
					final String actionString = requestParams.get("action");
					final ApiAction action = ApiAction.fromString(actionString);

					final byte[] responseContentByte = response.getContent();
					final String responseContentString = new String(responseContentByte);

					final ApiCallModel model = new ApiCallModel();
					model.setAction(action);
					model.setRequestParams(requestParams);
					model.setRequestContent(requestContentString);
					model.setResponseContent(responseContentString);

					Log.d(PADPlugin.class.getName(), "" + model);
					new ApiCallHandlerThread(context, model).start();
				}

				return response;
			}
			// just make normal action without any custom parsing
			return _in.fetchResponse(request);
		}

		private Map<String, String> extractParams(final HttpUrl reqUrl) {
			final Map<String, String> params = new HashMap<String, String>();
			// param1=value1&param2=value2
			final String reqQuery = reqUrl.getQuery();
			if (reqQuery != null && !"".equals(reqQuery)) {
				final String[] reqParams = reqQuery.split("&");
				for (final String oneParam : reqParams) {
					final String[] oneParamSplit = oneParam.split("=");
					params.put(oneParamSplit[0], oneParamSplit[1]);
				}
			}
			return params;
		}
	}

	public PADPlugin(Context context) {
		this.context = context;
		targetHost = new DefaultSharedPreferencesHelper(context).getListenerTargetHostname();
	}

	public void parseProperties() {
	}

	@Override
	public String getPluginName() {
		return "PADListenerPlugin";
	}

	@Override
	public boolean getEnabled() {
		return _enabled;
	}

	public void setEnabled(boolean bool) {
		_enabled = bool;
	}

	@Override
	public HTTPClient getProxyPlugin(HTTPClient in) {
		return new Plugin(in);
	}

}