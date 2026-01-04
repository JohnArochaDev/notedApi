package com.noted.models;

public class Coordinates {

  public int x;
  public int y;

  public Coordinates(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return "Coordinated={ x: " + this.x + ", y: " + this.y + " }";
  }

  public int getX() {
    return this.x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return this.y;
  }

  public void setY(int y) {
    this.x = y;
  }
}
