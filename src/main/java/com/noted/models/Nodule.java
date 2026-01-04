package com.noted.models;

import java.util.UUID;

public class Nodule {
  public UUID id;

  public UUID parentId;

  public String type = "textNode";

  public Coordinates coordinates;

  public int width;

  public int height;

  public NoduleData data;

}
