package fr.neraud.padlistener.proxy.helper;

import android.content.Context;
import android.content.Intent;

import org.sandrop.webscarab.model.StoreException;
import org.sandrop.webscarab.plugin.Framework;
import org.sandrop.webscarab.plugin.proxy.IClientResolver;
import org.sandrop.webscarab.plugin.proxy.Proxy;
import org.sandrop.webscarab.plugin.proxy.SSLSocketFactoryFactory;
import org.sandroproxy.utils.NetworkHostNameResolver;
import org.sandroproxy.utils.PreferenceUtils;
import org.sandroproxy.utils.network.ClientResolver;
import org.sandroproxy.webscarab.store.sql.SqlLiteStore;

import java.io.File;
import java.io.FileInputStream;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.exception.ListenerSetupException;
import fr.neraud.padlistener.proxy.plugin.PADPlugin;
import fr.neraud.padlistener.service.ListenerService;

/**
 * Helper to start / stop the proxy
 *
 * @author Neraud
 */
public class ProxyHelper {

	private final Context context;

	private Framework framework = null;
	private NetworkHostNameResolver networkHostNameResolver = null;

	public ProxyHelper(Context context) {
		this.context = context;
	}

	public void activateProxy(ListenerService.CaptureListener captureListener) throws ListenerSetupException {
		MyLog.entry();

		framework = new Framework(context);
		setStore(context);
		networkHostNameResolver = new NetworkHostNameResolver(context);
		final IClientResolver clientResolver = new ClientResolver(context);
		final Proxy proxy = new Proxy(framework, networkHostNameResolver, clientResolver);
		framework.addPlugin(proxy);

		final PADPlugin plugin = new PADPlugin(context, captureListener);
		proxy.addPlugin(plugin);

		proxy.run();

		MyLog.exit();
	}

	public void deactivateProxy() {
		MyLog.entry();

		if (framework != null) {
			framework.stop();
		}
		if (networkHostNameResolver != null) {
			networkHostNameResolver.cleanUp();
		}
		networkHostNameResolver = null;
		framework = null;

		MyLog.exit();
	}

	private void setStore(Context context) throws ListenerSetupException {
		MyLog.entry();

		if (framework != null) {
			try {
				final File file = PreferenceUtils.getDataStorageDir(context);
				if (file != null) {
					final File rootDir = new File(file.getAbsolutePath() + "/content");
					if (!rootDir.exists()) {
						rootDir.mkdir();
					}
					framework.setSession("Database", SqlLiteStore.getInstance(context, rootDir.getAbsolutePath()), "");
				}
			} catch (final StoreException e) {
				throw new ListenerSetupException(e);
			}
		}

		MyLog.exit();
	}

	// Copied from SandroProxy /SandroProxyPlugin/src/org/sandroproxy/plugin/gui/MainActivity.java
	private static String ACTION_INSTALL = "android.credentials.INSTALL";
	private static String EXTRA_CERTIFICATE = "CERT";

	/*
	 *  this will work only on sdk 14 or higher
	 */
	public static void exportCACertToUserStore(Context context){

		Intent intent = new Intent(ACTION_INSTALL);
		intent.setClassName("com.android.certinstaller","com.android.certinstaller.CertInstallerMain");
		try {
			String keystoreCAExportFullPath = PreferenceUtils.getCAExportFilePath(context.getApplicationContext());
			File caExportFile = new File(keystoreCAExportFullPath);
			byte[] result = new byte[(int) caExportFile.length()];
			FileInputStream in = new FileInputStream(caExportFile);
			in.read(result);
			in.close();
			intent.putExtra(EXTRA_CERTIFICATE, result);
			context.startActivity(intent);
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}

	// Code mostly taken from SandroProxyLib\src\main\java\org\sandrop\webscarab\plugin\proxy\Proxy.java Proxy constructor.
	// Create a dummy SSLSocketFactoryFactory to force generate a CA key
	public static void generateCACert(Context context) {
		// The cache directory has to exist before it can create the cert files
		context.getExternalCacheDir();

		String keystoreCAFullPath = PreferenceUtils.getCAFilePath(context.getApplicationContext());
		String keystoreCertFullPath = PreferenceUtils.getCertFilePath(context.getApplicationContext());
		String caPassword = PreferenceUtils.getCAFilePassword(context.getApplicationContext());
		String keyStoreType = "PKCS12";
		if (keystoreCAFullPath != null && keystoreCAFullPath.length() > 0 &&
			keystoreCertFullPath != null && keystoreCertFullPath.length() > 0) {

			try {
				new SSLSocketFactoryFactory(keystoreCAFullPath, keystoreCertFullPath, keyStoreType, caPassword.toCharArray());
			} catch(Exception ignored) {
				// Not worrying about reporting errors because the real proxy initialization will report everything
			}
		}
	}
}
