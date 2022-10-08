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

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.badlogic.gdx.graphics.GL20;

import android.opengl.GLES20;
//绘制相关的一些操作
public class AndroidGL20 implements GL20 {
	private int[] ints = new int[1], ints2 = new int[1], ints3 = new int[1];
	private byte[] buffer = new byte[512];
	//使得texture活跃
	public void glActiveTexture (int texture) {
		GLES20.glActiveTexture(texture);
	}
	//shader附着到程序上
	public void glAttachShader (int program, int shader) {
		GLES20.glAttachShader(program, shader);
	}
	//绑定  使用名字绑定
	public void glBindAttribLocation (int program, int index, String name) {
		GLES20.glBindAttribLocation(program, index, name);
	}
	//绑定一个缓冲区
	public void glBindBuffer (int target, int buffer) {
		GLES20.glBindBuffer(target, buffer);
	}
	//绑定framebuffer
	public void glBindFramebuffer (int target, int framebuffer) {
		GLES20.glBindFramebuffer(target, framebuffer);
	}
	//绑定渲染    这个之前在创一个frameBuffer之后 进行绘制的时候看到过
	//这个一般和FrameBuffer一起使用的
	public void glBindRenderbuffer (int target, int renderbuffer) {
		GLES20.glBindRenderbuffer(target, renderbuffer);
	}
	//绑定纹理
	public void glBindTexture (int target, int texture) {
		GLES20.glBindTexture(target, texture);
	}
	//blend
	//blend除了颜色还有操作
	public void glBlendColor (float red, float green, float blue, float alpha) {
		GLES20.glBlendColor(red, green, blue, alpha);
	}

	//混合的模式
	public void glBlendEquation (int mode) {
		GLES20.glBlendEquation(mode);
	}

	//GLES20.glBlendFunc(nSrcPar,nDstPar);   混合
	//GLES20.glBlendEquation(equaInt[nEquaIndex]);  叠加模式（相加  相减）
	//glBlendFuncSeparate(GLenum srcRGB, GLenum destRGB, GLenum srcAlpha,
	// GLenum destAlpha);
	//参数srcRGB表示颜色值的源混合因子，参数destRGB表示颜色在的目标混合因子，参数
	//srcAlpha表示Alpha值的源混合因子，参数destAlpha表示Alpha值的目标混合因子
	//glBlendEquationSeparate - 分别设置RGB混合方程和alpha混合方程
	public void glBlendEquationSeparate (int modeRGB, int modeAlpha) {
		GLES20.glBlendEquationSeparate(modeRGB, modeAlpha);
	}
	//混合
	public void glBlendFunc (int sfactor, int dfactor) {
		GLES20.glBlendFunc(sfactor, dfactor);
	}

	public void glBlendFuncSeparate (int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) {
		GLES20.glBlendFuncSeparate(srcRGB, dstRGB, srcAlpha, dstAlpha);
	}
	//给buffer设置值
	public void glBufferData (int target, int size, Buffer data, int usage) {
		GLES20.glBufferData(target, size, data, usage);
	}
	//
	public void glBufferSubData (int target, int offset, int size, Buffer data) {
		GLES20.glBufferSubData(target, offset, size, data);
	}

	public int glCheckFramebufferStatus (int target) {
		return GLES20.glCheckFramebufferStatus(target);
	}
	//清除
	public void glClear (int mask) {
		GLES20.glClear(mask);
	}

	//清除后的颜色
	public void glClearColor (float red, float green, float blue, float alpha) {
		GLES20.glClearColor(red, green, blue, alpha);
	}
	//清除深度
	public void glClearDepthf (float depth) {
		GLES20.glClearDepthf(depth);
	}
	//清除
	public void glClearStencil (int s) {
		GLES20.glClearStencil(s);
	}

	public void glColorMask (boolean red, boolean green, boolean blue, boolean alpha) {
		GLES20.glColorMask(red, green, blue, alpha);
	}
	//编译shader
	public void glCompileShader (int shader) {
		GLES20.glCompileShader(shader);
	}
	//加载纹理数据

	/**
	 *
	 * @param target texture
	 * @param level 0
	 * @param internalformat rgb
	 * @param width w
	 * @param height h
	 * @param border 0
	 * @param imageSize
	 * @param data
	 */
	public void glCompressedTexImage2D (int target, int level, int internalformat, int width, int height, int border,
		int imageSize, Buffer data) {
		GLES20.glCompressedTexImage2D(target, level, internalformat, width, height, border, imageSize, data);
	}

	public void glCompressedTexSubImage2D (int target, int level, int xoffset, int yoffset, int width, int height, int format,
		int imageSize, Buffer data) {
		GLES20.glCompressedTexSubImage2D(target, level, xoffset, yoffset, width, height, format, imageSize, data);
	}

	public void glCopyTexImage2D (int target, int level, int internalformat, int x, int y, int width, int height, int border) {
		GLES20.glCopyTexImage2D(target, level, internalformat, x, y, width, height, border);
	}

	public void glCopyTexSubImage2D (int target, int level, int xoffset, int yoffset, int x, int y, int width, int height) {
		GLES20.glCopyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height);
	}

	//创建程序
	public int glCreateProgram () {
		return GLES20.glCreateProgram();
	}

	//创建shader
	public int glCreateShader (int type) {
		return GLES20.glCreateShader(type);
	}

	//怎样围起来为正
	public void glCullFace (int mode) {
		GLES20.glCullFace(mode);
	}

	//删除Buffer
	public void glDeleteBuffers (int n, IntBuffer buffers) {
		GLES20.glDeleteBuffers(n, buffers);
	}

	public void glDeleteBuffer (int buffer) {
		ints[0] = buffer;
		GLES20.glDeleteBuffers(1, ints, 0);
	}

	//删除frameBuffer
	public void glDeleteFramebuffers (int n, IntBuffer framebuffers) {
		GLES20.glDeleteFramebuffers(n, framebuffers);
	}

	public void glDeleteFramebuffer (int framebuffer) {
		ints[0] = framebuffer;
		GLES20.glDeleteFramebuffers(1, ints, 0);
	}

	//删除program
	public void glDeleteProgram (int program) {
		GLES20.glDeleteProgram(program);
	}

	//删除renderbuffer
	public void glDeleteRenderbuffers (int n, IntBuffer renderbuffers) {
		GLES20.glDeleteRenderbuffers(n, renderbuffers);
	}

	public void glDeleteRenderbuffer (int renderbuffer) {
		ints[0] = renderbuffer;
		GLES20.glDeleteRenderbuffers(1, ints, 0);
	}

	public void glDeleteShader (int shader) {
		GLES20.glDeleteShader(shader);
	}

	public void glDeleteTextures (int n, IntBuffer textures) {
		GLES20.glDeleteTextures(n, textures);
	}

	public void glDeleteTexture (int texture) {
		ints[0] = texture;
		GLES20.glDeleteTextures(1, ints, 0);
	}

	public void glDepthFunc (int func) {
		GLES20.glDepthFunc(func);
	}

	public void glDepthMask (boolean flag) {
		GLES20.glDepthMask(flag);
	}

	public void glDepthRangef (float zNear, float zFar) {
		GLES20.glDepthRangef(zNear, zFar);
	}

	public void glDetachShader (int program, int shader) {
		GLES20.glDetachShader(program, shader);
	}

	public void glDisable (int cap) {
		GLES20.glDisable(cap);
	}

	public void glDisableVertexAttribArray (int index) {
		GLES20.glDisableVertexAttribArray(index);
	}

	public void glDrawArrays (int mode, int first, int count) {
		GLES20.glDrawArrays(mode, first, count);
	}

	public void glDrawElements (int mode, int count, int type, Buffer indices) {
		GLES20.glDrawElements(mode, count, type, indices);
	}

	public void glDrawElements (int mode, int count, int type, int indices) {
		GLES20.glDrawElements(mode, count, type, indices);
	}

	public void glEnable (int cap) {
		GLES20.glEnable(cap);
	}

	public void glEnableVertexAttribArray (int index) {
		GLES20.glEnableVertexAttribArray(index);
	}

	public void glFinish () {
		GLES20.glFinish();
	}

	public void glFlush () {
		GLES20.glFlush();
	}

	public void glFramebufferRenderbuffer (int target, int attachment, int renderbuffertarget, int renderbuffer) {
		GLES20.glFramebufferRenderbuffer(target, attachment, renderbuffertarget, renderbuffer);
	}

	public void glFramebufferTexture2D (int target, int attachment, int textarget, int texture, int level) {
		GLES20.glFramebufferTexture2D(target, attachment, textarget, texture, level);
	}

	public void glFrontFace (int mode) {
		GLES20.glFrontFace(mode);
	}

	public void glGenBuffers (int n, IntBuffer buffers) {
		GLES20.glGenBuffers(n, buffers);
	}

	public int glGenBuffer () {
		GLES20.glGenBuffers(1, ints, 0);
		return ints[0];
	}

	public void glGenerateMipmap (int target) {
		GLES20.glGenerateMipmap(target);
	}

	public void glGenFramebuffers (int n, IntBuffer framebuffers) {
		GLES20.glGenFramebuffers(n, framebuffers);
	}

	public int glGenFramebuffer () {
		GLES20.glGenFramebuffers(1, ints, 0);
		return ints[0];
	}

	public void glGenRenderbuffers (int n, IntBuffer renderbuffers) {
		GLES20.glGenRenderbuffers(n, renderbuffers);
	}

	public int glGenRenderbuffer () {
		GLES20.glGenRenderbuffers(1, ints, 0);
		return ints[0];
	}

	public void glGenTextures (int n, IntBuffer textures) {
		GLES20.glGenTextures(n, textures);
	}

	public int glGenTexture () {
		GLES20.glGenTextures(1, ints, 0);
		return ints[0];
	}

	public String glGetActiveAttrib (int program, int index, IntBuffer size, IntBuffer type) {
		GLES20.glGetActiveAttrib(program, index, buffer.length, ints, 0, ints2, 0, ints3, 0, buffer, 0);

		// size
		size.put(ints2[0]);
		// type
		type.put(ints3[0]);

		return new String(buffer, 0, ints[0]);
	}

	public String glGetActiveUniform (int program, int index, IntBuffer size, IntBuffer type) {
		GLES20.glGetActiveUniform(program, index, buffer.length, ints, 0, ints2, 0, ints3, 0, buffer, 0);

		// size
		size.put(ints2[0]);
		// type
		type.put(ints3[0]);

		return new String(buffer, 0, ints[0]);
	}

	public void glGetAttachedShaders (int program, int maxcount, Buffer count, IntBuffer shaders) {
		GLES20.glGetAttachedShaders(program, maxcount, (IntBuffer)count, shaders);
	}

	public int glGetAttribLocation (int program, String name) {
		return GLES20.glGetAttribLocation(program, name);
	}

	public void glGetBooleanv (int pname, Buffer params) {
		GLES20.glGetBooleanv(pname, (IntBuffer)params);
	}

	public void glGetBufferParameteriv (int target, int pname, IntBuffer params) {
		GLES20.glGetBufferParameteriv(target, pname, params);
	}

	public int glGetError () {
		return GLES20.glGetError();
	}

	public void glGetFloatv (int pname, FloatBuffer params) {
		GLES20.glGetFloatv(pname, params);
	}

	public void glGetFramebufferAttachmentParameteriv (int target, int attachment, int pname, IntBuffer params) {
		GLES20.glGetFramebufferAttachmentParameteriv(target, attachment, pname, params);
	}

	public void glGetIntegerv (int pname, IntBuffer params) {
		GLES20.glGetIntegerv(pname, params);
	}

	public void glGetProgramiv (int program, int pname, IntBuffer params) {
		GLES20.glGetProgramiv(program, pname, params);
	}

	public String glGetProgramInfoLog (int program) {
		return GLES20.glGetProgramInfoLog(program);
	}

	public void glGetRenderbufferParameteriv (int target, int pname, IntBuffer params) {
		GLES20.glGetRenderbufferParameteriv(target, pname, params);
	}

	public void glGetShaderiv (int shader, int pname, IntBuffer params) {
		GLES20.glGetShaderiv(shader, pname, params);
	}

	public String glGetShaderInfoLog (int shader) {
		return GLES20.glGetShaderInfoLog(shader);
	}

	public void glGetShaderPrecisionFormat (int shadertype, int precisiontype, IntBuffer range, IntBuffer precision) {
		GLES20.glGetShaderPrecisionFormat(shadertype, precisiontype, range, precision);
	}

	public String glGetString (int name) {
		return GLES20.glGetString(name);
	}

	public void glGetTexParameterfv (int target, int pname, FloatBuffer params) {
		GLES20.glGetTexParameterfv(target, pname, params);
	}

	public void glGetTexParameteriv (int target, int pname, IntBuffer params) {
		GLES20.glGetTexParameteriv(target, pname, params);
	}

	public void glGetUniformfv (int program, int location, FloatBuffer params) {
		GLES20.glGetUniformfv(program, location, params);
	}

	public void glGetUniformiv (int program, int location, IntBuffer params) {
		GLES20.glGetUniformiv(program, location, params);
	}

	public int glGetUniformLocation (int program, String name) {
		return GLES20.glGetUniformLocation(program, name);
	}

	public void glGetVertexAttribfv (int index, int pname, FloatBuffer params) {
		GLES20.glGetVertexAttribfv(index, pname, params);
	}

	public void glGetVertexAttribiv (int index, int pname, IntBuffer params) {
		GLES20.glGetVertexAttribiv(index, pname, params);
	}

	public void glGetVertexAttribPointerv (int index, int pname, Buffer pointer) {
		// FIXME won't implement this shit
	}

	public void glHint (int target, int mode) {
		GLES20.glHint(target, mode);
	}

	public boolean glIsBuffer (int buffer) {
		return GLES20.glIsBuffer(buffer);
	}

	public boolean glIsEnabled (int cap) {
		return GLES20.glIsEnabled(cap);
	}

	public boolean glIsFramebuffer (int framebuffer) {
		return GLES20.glIsFramebuffer(framebuffer);
	}

	public boolean glIsProgram (int program) {
		return GLES20.glIsProgram(program);
	}

	public boolean glIsRenderbuffer (int renderbuffer) {
		return GLES20.glIsRenderbuffer(renderbuffer);
	}

	public boolean glIsShader (int shader) {
		return GLES20.glIsShader(shader);
	}

	public boolean glIsTexture (int texture) {
		return GLES20.glIsTexture(texture);
	}

	public void glLineWidth (float width) {
		GLES20.glLineWidth(width);
	}

	public void glLinkProgram (int program) {
		GLES20.glLinkProgram(program);
	}

	public void glPixelStorei (int pname, int param) {
		GLES20.glPixelStorei(pname, param);
	}

	public void glPolygonOffset (float factor, float units) {
		GLES20.glPolygonOffset(factor, units);
	}

	public void glReadPixels (int x, int y, int width, int height, int format, int type, Buffer pixels) {
		GLES20.glReadPixels(x, y, width, height, format, type, pixels);
	}

	public void glReleaseShaderCompiler () {
		GLES20.glReleaseShaderCompiler();
	}

	public void glRenderbufferStorage (int target, int internalformat, int width, int height) {
		GLES20.glRenderbufferStorage(target, internalformat, width, height);
	}

	public void glSampleCoverage (float value, boolean invert) {
		GLES20.glSampleCoverage(value, invert);
	}

	public void glScissor (int x, int y, int width, int height) {
		GLES20.glScissor(x, y, width, height);
	}

	public void glShaderBinary (int n, IntBuffer shaders, int binaryformat, Buffer binary, int length) {
		GLES20.glShaderBinary(n, shaders, binaryformat, binary, length);
	}

	public void glShaderSource (int shader, String string) {
		GLES20.glShaderSource(shader, string);
	}

	public void glStencilFunc (int func, int ref, int mask) {
		GLES20.glStencilFunc(func, ref, mask);
	}

	public void glStencilFuncSeparate (int face, int func, int ref, int mask) {
		GLES20.glStencilFuncSeparate(face, func, ref, mask);
	}

	public void glStencilMask (int mask) {
		GLES20.glStencilMask(mask);
	}

	public void glStencilMaskSeparate (int face, int mask) {
		GLES20.glStencilMaskSeparate(face, mask);
	}

	public void glStencilOp (int fail, int zfail, int zpass) {
		GLES20.glStencilOp(fail, zfail, zpass);
	}

	public void glStencilOpSeparate (int face, int fail, int zfail, int zpass) {
		GLES20.glStencilOpSeparate(face, fail, zfail, zpass);
	}

	public void glTexImage2D (int target, int level, int internalformat, int width, int height, int border, int format, int type,
		Buffer pixels) {
		GLES20.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
	}

	public void glTexParameterf (int target, int pname, float param) {
		GLES20.glTexParameterf(target, pname, param);
	}

	/**
	 * 给纹理设置参数
	 * @param target
	 * @param pname
	 * @param params
	 */
	public void glTexParameterfv (int target, int pname, FloatBuffer params) {
		GLES20.glTexParameterfv(target, pname, params);
	}

	public void glTexParameteri (int target, int pname, int param) {
		GLES20.glTexParameteri(target, pname, param);
	}

	public void glTexParameteriv (int target, int pname, IntBuffer params) {
		GLES20.glTexParameteriv(target, pname, params);
	}

	/**
	 *函数指定现有一维纹理图像的一部分
	 * @param target 目标纹理。 必须是GL_TEXTURE_2D
	 * @param level 详细级别编号。 级别 0 是基本映像。 级别 n 是 第 n 个 mipmap 缩减图像。
	 * @param xoffset
	 * @param yoffset
	 * @param width 纹理子图像的宽度
	 * @param height
	 * @param format 格式rgb  rgba
	 * @param type 像素数据类型
	 * @param pixels //存储位置
	 */
	public void glTexSubImage2D (int target, int level, int xoffset, int yoffset, int width, int height, int format, int type,
		Buffer pixels) {
		GLES20.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
	}

	public void glUniform1f (int location, float x) {
		GLES20.glUniform1f(location, x);
	}

	public void glUniform1fv (int location, int count, FloatBuffer v) {
		GLES20.glUniform1fv(location, count, v);
	}

	public void glUniform1fv (int location, int count, float[] v, int offset) {
		GLES20.glUniform1fv(location, count, v, offset);
	}

	public void glUniform1i (int location, int x) {
		GLES20.glUniform1i(location, x);
	}

	public void glUniform1iv (int location, int count, IntBuffer v) {
		GLES20.glUniform1iv(location, count, v);
	}

	public void glUniform1iv (int location, int count, int[] v, int offset) {
		GLES20.glUniform1iv(location, count, v, offset);
	}

	public void glUniform2f (int location, float x, float y) {
		GLES20.glUniform2f(location, x, y);
	}

	public void glUniform2fv (int location, int count, FloatBuffer v) {
		GLES20.glUniform2fv(location, count, v);
	}

	public void glUniform2fv (int location, int count, float[] v, int offset) {
		GLES20.glUniform2fv(location, count, v, offset);
	}

	public void glUniform2i (int location, int x, int y) {
		GLES20.glUniform2i(location, x, y);
	}

	public void glUniform2iv (int location, int count, IntBuffer v) {
		GLES20.glUniform2iv(location, count, v);
	}

	public void glUniform2iv (int location, int count, int[] v, int offset) {
		GLES20.glUniform2iv(location, count, v, offset);
	}

	public void glUniform3f (int location, float x, float y, float z) {
		GLES20.glUniform3f(location, x, y, z);
	}

	public void glUniform3fv (int location, int count, FloatBuffer v) {
		GLES20.glUniform3fv(location, count, v);
	}

	public void glUniform3fv (int location, int count, float[] v, int offset) {
		GLES20.glUniform3fv(location, count, v, offset);
	}

	public void glUniform3i (int location, int x, int y, int z) {
		GLES20.glUniform3i(location, x, y, z);
	}

	public void glUniform3iv (int location, int count, IntBuffer v) {
		GLES20.glUniform3iv(location, count, v);
	}

	public void glUniform3iv (int location, int count, int[] v, int offset) {
		GLES20.glUniform3iv(location, count, v, offset);
	}

	public void glUniform4f (int location, float x, float y, float z, float w) {
		GLES20.glUniform4f(location, x, y, z, w);
	}

	public void glUniform4fv (int location, int count, FloatBuffer v) {
		GLES20.glUniform4fv(location, count, v);
	}

	public void glUniform4fv (int location, int count, float[] v, int offset) {
		GLES20.glUniform4fv(location, count, v, offset);
	}

	public void glUniform4i (int location, int x, int y, int z, int w) {
		GLES20.glUniform4i(location, x, y, z, w);
	}

	public void glUniform4iv (int location, int count, IntBuffer v) {
		GLES20.glUniform4iv(location, count, v);
	}

	public void glUniform4iv (int location, int count, int[] v, int offset) {
		GLES20.glUniform4iv(location, count, v, offset);
	}

	public void glUniformMatrix2fv (int location, int count, boolean transpose, FloatBuffer value) {
		GLES20.glUniformMatrix2fv(location, count, transpose, value);
	}

	public void glUniformMatrix2fv (int location, int count, boolean transpose, float[] value, int offset) {
		GLES20.glUniformMatrix2fv(location, count, transpose, value, offset);
	}

	public void glUniformMatrix3fv (int location, int count, boolean transpose, FloatBuffer value) {
		GLES20.glUniformMatrix3fv(location, count, transpose, value);
	}

	public void glUniformMatrix3fv (int location, int count, boolean transpose, float[] value, int offset) {
		GLES20.glUniformMatrix3fv(location, count, transpose, value, offset);
	}

	public void glUniformMatrix4fv (int location, int count, boolean transpose, FloatBuffer value) {
		GLES20.glUniformMatrix4fv(location, count, transpose, value);
	}

	public void glUniformMatrix4fv (int location, int count, boolean transpose, float[] value, int offset) {
		GLES20.glUniformMatrix4fv(location, count, transpose, value, offset);
	}

	/**
	 * 使用程序
	 * @param program
	 */
	public void glUseProgram (int program) {
		GLES20.glUseProgram(program);
	}

	//验证程序
	public void glValidateProgram (int program) {
		GLES20.glValidateProgram(program);
	}

	public void glVertexAttrib1f (int indx, float x) {
		GLES20.glVertexAttrib1f(indx, x);
	}

	public void glVertexAttrib1fv (int indx, FloatBuffer values) {
		GLES20.glVertexAttrib1fv(indx, values);
	}

	public void glVertexAttrib2f (int indx, float x, float y) {
		GLES20.glVertexAttrib2f(indx, x, y);
	}

	public void glVertexAttrib2fv (int indx, FloatBuffer values) {
		GLES20.glVertexAttrib2fv(indx, values);
	}

	public void glVertexAttrib3f (int indx, float x, float y, float z) {
		GLES20.glVertexAttrib3f(indx, x, y, z);
	}

	/**
	 *
	 * @param indx
	 * @param values 3个float的数组
	 */
	public void glVertexAttrib3fv (int indx, FloatBuffer values) {
		GLES20.glVertexAttrib3fv(indx, values);
	}

	/**
	 * @param indx 位置   下面时4个值
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public void glVertexAttrib4f (int indx, float x, float y, float z, float w) {
		GLES20.glVertexAttrib4f(indx, x, y, z, w);
	}
	//给一个4个float
	public void glVertexAttrib4fv (int indx, FloatBuffer values) {
		GLES20.glVertexAttrib4fv(indx, values);
	}
	//给属性设置值
	public void glVertexAttribPointer (int indx, int size, int type, boolean normalized, int stride, Buffer ptr) {
		GLES20.glVertexAttribPointer(indx, size, type, normalized, stride, ptr);
	}
	//给属性设置值(使用缓存区)
	public void glVertexAttribPointer (int indx, int size, int type, boolean normalized, int stride, int ptr) {
		GLES20.glVertexAttribPointer(indx, size, type, normalized, stride, ptr);
	}

	/**
	 *
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void glViewport (int x, int y, int width, int height) {
		GLES20.glViewport(x, y, width, height);
	}
}
