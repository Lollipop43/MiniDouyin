package com.example.minidouyin.model;

import android.graphics.Paint;

public class HeartBean {
	int count = 0;//第几张图 第几帧
	int alpha; // 透明度
	int X; // X坐标点
	int Y; // Y坐标点
	float scanle; // 缩放比例
	int degrees; // 旋转角度
	Paint paint; //

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getAlpha() {
		return alpha;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	public int getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}

	public float getScanle() {
		return scanle;
	}

	public void setScanle(float scanle) {
		this.scanle = scanle;
	}

	public int getDegrees() {
		return degrees;
	}

	public void setDegrees(int degrees) {
		this.degrees = degrees;
	}

	public Paint getPaint() {
		return paint;
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}
}
