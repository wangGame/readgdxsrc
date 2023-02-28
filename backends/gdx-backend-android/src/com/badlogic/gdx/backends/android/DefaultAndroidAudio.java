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

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** An implementation of the {@link Audio} interface for Android.
 * 
 * @author mzechner */
public class DefaultAndroidAudio implements AndroidAudio {
	private final SoundPool soundPool;
	private final AudioManager manager;
	private final List<AndroidMusic> musics = new ArrayList<AndroidMusic>();

	public DefaultAndroidAudio (Context context, AndroidApplicationConfiguration config) {
		if (!config.disableAudio) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				AudioAttributes audioAttrib = new AudioAttributes
						.Builder()
						.setUsage(AudioAttributes.USAGE_GAME)
						.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION) //简短的音
						.build();
				soundPool = new SoundPool
						.Builder()
						.setAudioAttributes(audioAttrib)
						.setMaxStreams(config.maxSimultaneousSounds) //设置最大是16     同时播放32
						.build();
			} else {
				soundPool = new SoundPool(config.maxSimultaneousSounds,
						AudioManager.STREAM_MUSIC,
						0);
				// srcQuality: the sample-rate
				// converter quality. Currently
				// has no effect. Use 0 for the
				// default.
			}
			manager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
			if (context instanceof Activity) {
//				建议使用硬件音量控制更改音量的音频流。
//				建议的音频流将绑定到此活动的窗口。当“活动”处于前台时收到的卷请求将影响此流。
//				不能保证硬件音量控制将始终更改此流的音量（例如，如果正在进行呼叫，则可能会更改其流的音量）。
//				要重置回默认值，请使用AudioManager.use_default_STREAM_TYPE。
//				参数：
//				streamType–音频流的类型，其音量应通过硬件音量控制进行更改。
				((Activity)context).setVolumeControlStream(AudioManager.STREAM_MUSIC);
			}
		} else {
			soundPool = null;
			manager = null;
		}
	}

	@Override
	public void pause () {
		if (soundPool == null) {
			return;
		}
		synchronized (musics) {
			for (AndroidMusic music : musics) {
				if (music.isPlaying()) {
					music.pause();
					music.wasPlaying = true;
				} else
					music.wasPlaying = false;
			}
		}
		this.soundPool.autoPause();
	}

	@Override
	public void resume () {
		if (soundPool == null) {
			return;
		}
		synchronized (musics) {
			for (int i = 0; i < musics.size(); i++) {
				if (musics.get(i).wasPlaying) musics.get(i).play();
			}
		}
		this.soundPool.autoResume();
	}

	/** {@inheritDoc} */
	@Override
	public AudioDevice newAudioDevice (int samplingRate, boolean isMono) {
		if (soundPool == null) {
			throw new GdxRuntimeException("Android audio is not enabled by the application config.");
		}
		return new AndroidAudioDevice(samplingRate, isMono);
	}

	/** {@inheritDoc} */
	@Override
	public Music newMusic (FileHandle file) {
		if (soundPool == null) {
			throw new GdxRuntimeException("Android audio is not enabled by the application config.");
		}
		AndroidFileHandle aHandle = (AndroidFileHandle)file;
		MediaPlayer mediaPlayer = createMediaPlayer();
		if (aHandle.type() == FileType.Internal) {
			try {
				AssetFileDescriptor descriptor = aHandle.getAssetFileDescriptor();
				mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
				descriptor.close();
				mediaPlayer.prepare();
				AndroidMusic music = new AndroidMusic(this, mediaPlayer);
				synchronized (musics) {
					musics.add(music);
				}
				return music;
			} catch (Exception ex) {
				throw new GdxRuntimeException(
					"Error loading audio file: " + file + "\nNote: Internal audio files must be placed in the assets directory.", ex);
			}
		} else {
			try {
				mediaPlayer.setDataSource(aHandle.file().getPath());
				mediaPlayer.prepare();
				AndroidMusic music = new AndroidMusic(this, mediaPlayer);
				synchronized (musics) {
					musics.add(music);
				}
				return music;
			} catch (Exception ex) {
				throw new GdxRuntimeException("Error loading audio file: " + file, ex);
			}
		}
	}

	/**
	 * Creates a new Music instance from the provided FileDescriptor. It is the caller's responsibility to close the file
	 * descriptor. It is safe to do so as soon as this call returns.
	 *
	 * @param fd the FileDescriptor from which to create the Music
	 * @see Audio#newMusic(FileHandle) */
	public Music newMusic (FileDescriptor fd) {
		if (soundPool == null) {
			throw new GdxRuntimeException("Android audio is not enabled by the application config.");
		}
		MediaPlayer mediaPlayer = createMediaPlayer();
		try {
			mediaPlayer.setDataSource(fd);
			mediaPlayer.prepare();
			AndroidMusic music = new AndroidMusic(this, mediaPlayer);
			synchronized (musics) {
				musics.add(music);
			}
			return music;
		} catch (Exception ex) {
			throw new GdxRuntimeException("Error loading audio from FileDescriptor", ex);
		}
	}

	/** {@inheritDoc} */
	@Override
	public Sound newSound (FileHandle file) {
		if (soundPool == null) {
			throw new GdxRuntimeException("Android audio is not enabled by the application config.");
		}
		AndroidSound androidSound;
		AndroidFileHandle aHandle = (AndroidFileHandle)file;
		//android 的internal目录是不可以随便获取文件的，也不操作的
		//需要使用asset目录来读取
		if (aHandle.type() == FileType.Internal) {
			try {
				//得到文件句柄
				AssetFileDescriptor descriptor = aHandle.getAssetFileDescriptor();
				androidSound = new AndroidSound(
						soundPool,
						manager,
						soundPool
								.load(descriptor, 1));
				//读取结束  关闭句柄
				descriptor.close();
			} catch (IOException ex) {
				throw new GdxRuntimeException(
					"Error loading audio file: " + file + "\nNote: Internal audio files must be placed in the assets directory.", ex);
			}
		} else {
			try {
				androidSound = new AndroidSound(soundPool, manager, soundPool.load(aHandle.file().getPath(), 1));
			} catch (Exception ex) {
				throw new GdxRuntimeException("Error loading audio file: " + file, ex);
			}
		}
		return androidSound;
	}

	/** {@inheritDoc} */
	/**
	 * 录音
	 * @param samplingRate the sampling rate in Hertz
	 * @param isMono whether the recorder records in mono or stereo
	 * @return
	 */
	@Override
	public AudioRecorder newAudioRecorder (int samplingRate, boolean isMono) {
		if (soundPool == null) {
			throw new GdxRuntimeException("Android audio is not enabled by the application config.");
		}
		return new AndroidAudioRecorder(samplingRate, isMono);
	}

	/** Kills the soundpool and all other resources */
	@Override
	public void dispose () {
		if (soundPool == null) {
			return;
		}
		synchronized (musics) {
			// gah i hate myself.... music.dispose() removes the music from the list...
			ArrayList<AndroidMusic> musicsCopy = new ArrayList<AndroidMusic>(musics);
			for (AndroidMusic music : musicsCopy) {
				music.dispose();
			}
		}
		soundPool.release();
	}

	@Override
	public void notifyMusicDisposed (AndroidMusic music) {
		synchronized (musics) {
			musics.remove(this);
		}
	}

	/**
	 * 创建MediaPlayer 记忆里面这个是用来播放视频的
	 * @return
	 */
	protected MediaPlayer createMediaPlayer () {
		MediaPlayer mediaPlayer = new MediaPlayer();
		if (Build.VERSION.SDK_INT <= 21) {
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		} else {
			AudioAttributes build
					= new AudioAttributes
						.Builder()
						.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
						.setUsage(AudioAttributes.USAGE_GAME)
						.build();
			mediaPlayer.setAudioAttributes(build);
		}
		return mediaPlayer;
	}
}
