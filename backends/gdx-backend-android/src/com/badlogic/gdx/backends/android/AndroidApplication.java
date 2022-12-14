/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.backends.android;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.android.surfaceview.FillResolutionStrategy;
import com.badlogic.gdx.utils.*;

/** An implementation of the {@link Application} interface for Android. Create an {@link Activity} that derives from this class.
 * In the {@link Activity#onCreate(Bundle)} method call the {@link #initialize(ApplicationListener)} method specifying the
 * configuration for the GLSurfaceView.
 * 
 * @author mzechner */
//游戏android端的入口
public class AndroidApplication extends Activity implements AndroidApplicationBase {
	//绘制相关的
	protected AndroidGraphics graphics;
	//输入相关的
	protected AndroidInput input;
	//音效有关的
	protected AndroidAudio audio;
	//文件有关的
	protected AndroidFiles files;
	//网络
	protected AndroidNet net;
	//粘贴板
	protected AndroidClipboard clipboard;
	//游戏的接口  完成生命周期回调
	protected ApplicationListener listener;
	//android的
	public Handler handler;
	//第一次回来
	protected boolean firstResume = true;
	//所有的任务
	protected final Array<Runnable> runnables = new Array<Runnable>();
	//执行的任务
	protected final Array<Runnable> executedRunnables = new Array<Runnable>();
	//生命周期
	protected final SnapshotArray<LifecycleListener> lifecycleListeners = new SnapshotArray<LifecycleListener>(
		LifecycleListener.class);
	//android事件
	private final Array<AndroidEventListener> androidEventListeners = new Array<AndroidEventListener>();
	protected int logLevel = LOG_INFO;
	//日志
	protected ApplicationLogger applicationLogger;
	//沉寂模式
	protected boolean useImmersiveMode = false;
	//焦点改变
	private int wasFocusChanged = -1;
	//等待音频
	private boolean isWaitingForAudio = false;

	/** This method has to be called in the {@link Activity#onCreate(Bundle)} method. It sets up all the things necessary to get
	 * input, render via OpenGL and so on. Uses a default {@link AndroidApplicationConfiguration}.
	 * 这个方法必须调用{@link活动#创建(包)}的方法。设置的一切必要的输入,
	 * 通过opengl渲染等等。使用一个默认}{@link Android 应用程序配置
	 * @param listener the {@link ApplicationListener} implementing the program logic **/
	public void initialize (ApplicationListener listener) {
		//初始化方法  传递一个config 和 一个game
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(listener, config);
	}

	/** This method has to be called in the {@link Activity#onCreate(Bundle)} method. It sets up all the things necessary to get
	 * input, render via OpenGL and so on. You can configure other aspects of the application with the rest of the fields in the
	 * {@link AndroidApplicationConfiguration} instance.
	 * 
	 * @param listener the {@link ApplicationListener} implementing the program logic
	 * @param config the {@link AndroidApplicationConfiguration}, defining various settings of the application (use accelerometer,
	 *           etc.). */
	public void initialize (ApplicationListener listener, AndroidApplicationConfiguration config) {
		init(listener, config, false);
	}

	/** This method has to be called in the {@link Activity#onCreate(Bundle)} method. It sets up all the things necessary to get
	 * input, render via OpenGL and so on. Uses a default {@link AndroidApplicationConfiguration}.
	 * <p>
	 * Note: you have to add the returned view to your layout!
	 * 
	 * @param listener the {@link ApplicationListener} implementing the program logic
	 * @return the GLSurfaceView of the application */
	public View initializeForView (ApplicationListener listener) {
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		return initializeForView(listener, config);
	}

	/** This method has to be called in the {@link Activity#onCreate(Bundle)} method. It sets up all the things necessary to get
	 * input, render via OpenGL and so on. You can configure other aspects of the application with the rest of the fields in the
	 * {@link AndroidApplicationConfiguration} instance.
	 * <p>
	 * Note: you have to add the returned view to your layout!
	 * 
	 * @param listener the {@link ApplicationListener} implementing the program logic
	 * @param config the {@link AndroidApplicationConfiguration}, defining various settings of the application (use accelerometer,
	 *           etc.).
	 * @return the GLSurfaceView of the application */
	public View initializeForView (ApplicationListener listener, AndroidApplicationConfiguration config) {
		init(listener, config, true);
		return graphics.getView();
	}

	private void init (ApplicationListener listener, AndroidApplicationConfiguration config, boolean isForView) {
		//小于14应该是不要了
		if (this.getVersion() < MINIMUM_SDK) {
			throw new GdxRuntimeException("libGDX requires Android API Level " + MINIMUM_SDK + " or later.");
		}
		//加载本地库
		GdxNativesLoader.load();
		//设置日志  （都是调用android的）
		setApplicationLogger(new AndroidApplicationLogger());
		//创建Graphics
		//默认的策略是铺满
		graphics = new AndroidGraphics(this, config,
			config.resolutionStrategy == null ? new FillResolutionStrategy() : config.resolutionStrategy);
		//用户输入
		input = createInput(this, this, graphics.view, config);
		//创建 audio
		audio = createAudio(this, config);
		//文件
		files = createFiles();
		//net
		net = new AndroidNet(this, config);
		//游戏listener
		this.listener = listener;

		this.handler = new Handler();
		//沉寂服务
		this.useImmersiveMode = config.useImmersiveMode;
		//粘贴板
		this.clipboard = new AndroidClipboard(this);
		// Add a specialized audio lifecycle listener
		//处理音效的
		//这个东西对于声明周期的也是可以直接使用的，不一定非要找到什么方法在调用的
		addLifecycleListener(new LifecycleListener() {

			@Override
			public void resume () {
				// No need to resume audio here
			}

			@Override
			public void pause () {
				audio.pause();
			}

			@Override
			public void dispose () {
				audio.dispose();
			}
		});

		Gdx.app = this;
		Gdx.input = this.getInput();
		Gdx.audio = this.getAudio();
		Gdx.files = this.getFiles();
		Gdx.graphics = this.getGraphics();
		Gdx.net = this.getNet();

		if (!isForView) {
			try {
				//没有标题蓝
				requestWindowFeature(Window.FEATURE_NO_TITLE);
			} catch (Exception ex) {
				log("AndroidApplication", "Content already displayed, cannot request FEATURE_NO_TITLE", ex);
			}
			//全屏
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			setContentView(graphics.getView(), createLayoutParams());
		}

		createWakeLock(config.useWakelock);
		//这句话是不是矛盾的
		useImmersiveMode(this.useImmersiveMode);
		if (this.useImmersiveMode && getVersion() >= Build.VERSION_CODES.KITKAT) {
			AndroidVisibilityListener vlistener = new AndroidVisibilityListener();
			vlistener.createListener(this);
		}

		// detect an already connected bluetooth keyboardAvailable
		if (getResources().getConfiguration().keyboard != Configuration.KEYBOARD_NOKEYS) input.setKeyboardAvailable(true);
	}

	protected FrameLayout.LayoutParams createLayoutParams () {
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
			android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		layoutParams.gravity = Gravity.CENTER;
		return layoutParams;
	}

	//使用唤醒
	protected void createWakeLock (boolean use) {
		if (use) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
	}

	@Override
	public void onWindowFocusChanged (boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		useImmersiveMode(this.useImmersiveMode);
		if (hasFocus) {
			this.wasFocusChanged = 1;
			if (this.isWaitingForAudio) {
				this.audio.resume();
				this.isWaitingForAudio = false;
			}
		} else {
			this.wasFocusChanged = 0;
		}
	}

	@TargetApi(19)
	@Override
	public void useImmersiveMode (boolean use) {
		//小于19的不理会
		if (!use || getVersion() < Build.VERSION_CODES.KITKAT) return;
		View view = getWindow().getDecorView();
		int code = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_FULLSCREEN
						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
		view.setSystemUiVisibility(code);
	}

	@Override
	protected void onPause () {
		boolean isContinuous = graphics.isContinuousRendering();
		boolean isContinuousEnforced = AndroidGraphics.enforceContinuousRendering;

		// from here we don't want non continuous rendering
		AndroidGraphics.enforceContinuousRendering = true;
		graphics.setContinuousRendering(true);
		// calls to setContinuousRendering(false) from other thread (ex: GLThread)
		// will be ignored at this point...
		graphics.pause();

		input.onPause();

		if (isFinishing()) {
			graphics.clearManagedCaches();
			graphics.destroy();
		}

		AndroidGraphics.enforceContinuousRendering = isContinuousEnforced;
		graphics.setContinuousRendering(isContinuous);

		graphics.onPauseGLSurfaceView();

		super.onPause();
	}

	@Override
	protected void onResume () {
		Gdx.app = this;
		Gdx.input = this.getInput();
		Gdx.audio = this.getAudio();
		Gdx.files = this.getFiles();
		Gdx.graphics = this.getGraphics();
		Gdx.net = this.getNet();

		input.onResume();

		if (graphics != null) {
			graphics.onResumeGLSurfaceView();
		}

		if (!firstResume) {
			graphics.resume();
		} else
			firstResume = false;

		this.isWaitingForAudio = true;
		if (this.wasFocusChanged == 1 || this.wasFocusChanged == -1) {
			this.audio.resume();
			this.isWaitingForAudio = false;
		}
		super.onResume();
	}

	@Override
	protected void onDestroy () {
		super.onDestroy();
	}

	@Override
	public ApplicationListener getApplicationListener () {
		return listener;
	}

	@Override
	public Audio getAudio () {
		return audio;
	}

	@Override
	public AndroidInput getInput () {
		return input;
	}

	@Override
	public Files getFiles () {
		return files;
	}

	@Override
	public Graphics getGraphics () {
		return graphics;
	}

	@Override
	public Net getNet () {
		return net;
	}

	@Override
	public ApplicationType getType () {
		return ApplicationType.Android;
	}

	//sdk版本
	@Override
	public int getVersion () {
		return android.os.Build.VERSION.SDK_INT;
	}

	//java栈信息
	@Override
	public long getJavaHeap () {
		return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
	}

	//本地栈
	@Override
	public long getNativeHeap () {
		return Debug.getNativeHeapAllocatedSize();
	}

	//pre
	@Override
	public Preferences getPreferences (String name) {
		return new AndroidPreferences(getSharedPreferences(name, Context.MODE_PRIVATE));
	}

	//得到粘贴板
	@Override
	public Clipboard getClipboard () {
		return clipboard;
	}

	@Override
	public void postRunnable (Runnable runnable) {
		synchronized (runnables) {
			runnables.add(runnable);
			Gdx.graphics.requestRendering();
		}
	}

	//
	@Override
	public void onConfigurationChanged (Configuration config) {
		super.onConfigurationChanged(config);
		boolean keyboardAvailable = false;
		if (config.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) keyboardAvailable = true;
		input.setKeyboardAvailable(keyboardAvailable);
	}

	@Override
	public void exit () {
		handler.post(new Runnable() {
			@Override
			public void run () {
				AndroidApplication.this.finish();
			}
		});
	}

	@Override
	public void debug (String tag, String message) {
		if (logLevel >= LOG_DEBUG) getApplicationLogger().debug(tag, message);
	}

	@Override
	public void debug (String tag, String message, Throwable exception) {
		if (logLevel >= LOG_DEBUG) getApplicationLogger().debug(tag, message, exception);
	}

	@Override
	public void log (String tag, String message) {
		if (logLevel >= LOG_INFO) getApplicationLogger().log(tag, message);
	}

	@Override
	public void log (String tag, String message, Throwable exception) {
		if (logLevel >= LOG_INFO) getApplicationLogger().log(tag, message, exception);
	}

	@Override
	public void error (String tag, String message) {
		if (logLevel >= LOG_ERROR) getApplicationLogger().error(tag, message);
	}

	@Override
	public void error (String tag, String message, Throwable exception) {
		if (logLevel >= LOG_ERROR) getApplicationLogger().error(tag, message, exception);
	}

	@Override
	public void setLogLevel (int logLevel) {
		this.logLevel = logLevel;
	}

	@Override
	public int getLogLevel () {
		return logLevel;
	}

	@Override
	public void setApplicationLogger (ApplicationLogger applicationLogger) {
		this.applicationLogger = applicationLogger;
	}

	@Override
	public ApplicationLogger getApplicationLogger () {
		return applicationLogger;
	}

	@Override
	public void addLifecycleListener (LifecycleListener listener) {
		synchronized (lifecycleListeners) {
			lifecycleListeners.add(listener);
		}
	}

	@Override
	public void removeLifecycleListener (LifecycleListener listener) {
		synchronized (lifecycleListeners) {
			lifecycleListeners.removeValue(listener, true);
		}
	}

	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// forward events to our listeners if there are any installed
		synchronized (androidEventListeners) {
			for (int i = 0; i < androidEventListeners.size; i++) {
				androidEventListeners.get(i).onActivityResult(requestCode, resultCode, data);
			}
		}
	}

	/** Adds an event listener for Android specific event such as onActivityResult(...). */
	public void addAndroidEventListener (AndroidEventListener listener) {
		synchronized (androidEventListeners) {
			androidEventListeners.add(listener);
		}
	}

	/** Removes an event listener for Android specific event such as onActivityResult(...). */
	public void removeAndroidEventListener (AndroidEventListener listener) {
		synchronized (androidEventListeners) {
			androidEventListeners.removeValue(listener, true);
		}
	}

	@Override
	public Context getContext () {
		return this;
	}

	@Override
	public Array<Runnable> getRunnables () {
		return runnables;
	}

	@Override
	public Array<Runnable> getExecutedRunnables () {
		return executedRunnables;
	}

	@Override
	public SnapshotArray<LifecycleListener> getLifecycleListeners () {
		return lifecycleListeners;
	}

	@Override
	public Window getApplicationWindow () {
		return this.getWindow();
	}

	@Override
	public Handler getHandler () {
		return this.handler;
	}

	@Override
	public AndroidAudio createAudio (Context context, AndroidApplicationConfiguration config) {
		return new DefaultAndroidAudio(context, config);
	}

	//键盘输入
	@Override
	public AndroidInput createInput (Application activity, Context context, Object view, AndroidApplicationConfiguration config) {
		return new DefaultAndroidInput(this, this, graphics.view, config);
	}

	protected AndroidFiles createFiles () {
		this.getFilesDir(); // workaround for Android bug #10515463
		return new DefaultAndroidFiles(this.getAssets(), this, true);
	}
}
